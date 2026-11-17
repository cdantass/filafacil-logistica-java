package service;

import com.filafacil.model.Coleta;
import exception.ApiException;
import integration.EnderecoViaCep;
import integration.ViaCepClient;
import model.Prioridade;
import model.StatusColeta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import repository.ArquivoColetaRepository;
import state.AppState;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ColetaServiceTest {

    @TempDir
    Path diretorioTemporario;

    private ColetaService service;
    private AppState appState;
    private ViaCepClient viaCepClient;

    @BeforeEach
    void configurar() {
        Path arquivoTemporario =
                diretorioTemporario.resolve("coletas.txt");

        ArquivoColetaRepository repository =
                new ArquivoColetaRepository(
                        arquivoTemporario.toString()
                );

        appState = new AppState();
        viaCepClient = mock(ViaCepClient.class);

        service = new ColetaService(
                repository,
                appState,
                viaCepClient
        );
    }

    @Test
    void deveCadastrarColetaConsultandoCep() {
        EnderecoViaCep enderecoViaCep =
                criarEnderecoViaCep();

        when(viaCepClient.buscarPorCep("01001-000"))
                .thenReturn(
                        CompletableFuture.completedFuture(
                                enderecoViaCep
                        )
                );

        Coleta coleta = service.cadastrarPorCep(
                "Cliente Teste",
                "01001-000",
                "100",
                "Sala 2",
                5,
                Prioridade.ALTA
        ).join();

        assertNotNull(coleta);
        assertEquals(1, coleta.getId());
        assertEquals(
                "Cliente Teste",
                coleta.getNomeCliente()
        );
        assertEquals(
                5,
                coleta.getQuantidadePacotes()
        );
        assertEquals(
                Prioridade.ALTA,
                coleta.getPrioridade()
        );
        assertEquals(
                StatusColeta.PENDENTE,
                coleta.getStatus()
        );

        assertEquals(
                "Praça da Sé, Sé, São Paulo, SP, nº 100, Sala 2, CEP 01001-000",
                coleta.getEndereco()
        );

        assertEquals(
                1,
                service.listarTodas().size()
        );

        assertEquals(
                1,
                appState.getColetas().size()
        );

        assertFalse(appState.isCarregando());

        verify(viaCepClient)
                .buscarPorCep("01001-000");
    }

    @Test
    void deveCadastrarSemComplemento() {
        when(viaCepClient.buscarPorCep("01001000"))
                .thenReturn(
                        CompletableFuture.completedFuture(
                                criarEnderecoViaCep()
                        )
                );

        Coleta coleta = service.cadastrarPorCep(
                "Cliente",
                "01001000",
                "150",
                "",
                2,
                Prioridade.MEDIA
        ).join();

        assertEquals(
                "Praça da Sé, Sé, São Paulo, SP, nº 150, CEP 01001-000",
                coleta.getEndereco()
        );
    }

    @Test
    void deveRemoverEspacosDosDados() {
        when(viaCepClient.buscarPorCep("01001000"))
                .thenReturn(
                        CompletableFuture.completedFuture(
                                criarEnderecoViaCep()
                        )
                );

        Coleta coleta = service.cadastrarPorCep(
                "  Cliente Teste  ",
                "01001000",
                "  100  ",
                "  Sala 2  ",
                3,
                Prioridade.MEDIA
        ).join();

        assertEquals(
                "Cliente Teste",
                coleta.getNomeCliente()
        );

        assertEquals(
                "Praça da Sé, Sé, São Paulo, SP, nº 100, Sala 2, CEP 01001-000",
                coleta.getEndereco()
        );
    }

    @Test
    void naoDeveCadastrarComNomeVazio() {
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> service.cadastrarPorCep(
                                "   ",
                                "01001000",
                                "100",
                                "",
                                2,
                                Prioridade.BAIXA
                        )
                );

        assertEquals(
                "O nome do cliente é obrigatório.",
                exception.getMessage()
        );

        assertTrue(service.listarTodas().isEmpty());
    }

    @Test
    void naoDeveCadastrarSemNumeroDoEndereco() {
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> service.cadastrarPorCep(
                                "Cliente",
                                "01001000",
                                " ",
                                "",
                                2,
                                Prioridade.BAIXA
                        )
                );

        assertEquals(
                "O número do endereço é obrigatório.",
                exception.getMessage()
        );

        assertTrue(service.listarTodas().isEmpty());
    }

    @Test
    void naoDeveCadastrarComQuantidadeInvalida() {
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> service.cadastrarPorCep(
                                "Cliente",
                                "01001000",
                                "100",
                                "",
                                0,
                                Prioridade.ALTA
                        )
                );

        assertEquals(
                "A quantidade de pacotes deve ser maior que zero.",
                exception.getMessage()
        );

        assertTrue(service.listarTodas().isEmpty());
    }

    @Test
    void naoDeveCadastrarSemPrioridade() {
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> service.cadastrarPorCep(
                                "Cliente",
                                "01001000",
                                "100",
                                "",
                                2,
                                null
                        )
                );

        assertEquals(
                "A prioridade é obrigatória.",
                exception.getMessage()
        );

        assertTrue(service.listarTodas().isEmpty());
    }

    @Test
    void deveRegistrarErroQuandoViaCepFalhar() {
        CompletableFuture<EnderecoViaCep> respostaComErro =
                CompletableFuture.failedFuture(
                        new ApiException(
                                "O CEP informado não foi encontrado."
                        )
                );

        when(viaCepClient.buscarPorCep("99999999"))
                .thenReturn(respostaComErro);

        CompletionException exception =
                assertThrows(
                        CompletionException.class,
                        () -> service.cadastrarPorCep(
                                "Cliente",
                                "99999999",
                                "100",
                                "",
                                2,
                                Prioridade.MEDIA
                        ).join()
                );

        assertTrue(
                exception.getCause() instanceof ApiException
        );

        assertEquals(
                "O CEP informado não foi encontrado.",
                exception.getCause().getMessage()
        );

        assertEquals(
                "O CEP informado não foi encontrado.",
                appState.getUltimoErro()
        );

        assertFalse(appState.isCarregando());
        assertTrue(service.listarTodas().isEmpty());
    }

    @Test
    void deveGerarIdsSequenciais() {
        when(viaCepClient.buscarPorCep("01001000"))
                .thenReturn(
                        CompletableFuture.completedFuture(
                                criarEnderecoViaCep()
                        )
                );

        Coleta primeira = service.cadastrarPorCep(
                "Cliente 1",
                "01001000",
                "10",
                "",
                2,
                Prioridade.BAIXA
        ).join();

        Coleta segunda = service.cadastrarPorCep(
                "Cliente 2",
                "01001000",
                "20",
                "",
                3,
                Prioridade.MEDIA
        ).join();

        assertEquals(1, primeira.getId());
        assertEquals(2, segunda.getId());
    }

    @Test
    void deveFiltrarPorStatus() {
        Coleta coleta = cadastrarColetaPadrao(
                "Cliente 1",
                Prioridade.BAIXA
        );

        cadastrarColetaPadrao(
                "Cliente 2",
                Prioridade.ALTA
        );

        service.atualizarStatus(
                coleta.getId(),
                StatusColeta.CONCLUIDA
        );

        List<Coleta> concluidas =
                service.filtrarPorStatus(
                        StatusColeta.CONCLUIDA
                );

        assertEquals(1, concluidas.size());
        assertEquals(
                coleta.getId(),
                concluidas.get(0).getId()
        );

        assertEquals(
                StatusColeta.CONCLUIDA,
                appState.getFiltroStatus()
        );
    }

    @Test
    void deveFiltrarPorPrioridade() {
        cadastrarColetaPadrao(
                "Cliente 1",
                Prioridade.ALTA
        );

        cadastrarColetaPadrao(
                "Cliente 2",
                Prioridade.BAIXA
        );

        cadastrarColetaPadrao(
                "Cliente 3",
                Prioridade.ALTA
        );

        List<Coleta> altas =
                service.filtrarPorPrioridade(
                        Prioridade.ALTA
                );

        assertEquals(2, altas.size());

        assertTrue(
                altas.stream()
                        .allMatch(
                                coleta ->
                                        coleta.getPrioridade()
                                                == Prioridade.ALTA
                        )
        );

        assertEquals(
                Prioridade.ALTA,
                appState.getFiltroPrioridade()
        );
    }

    @Test
    void deveAtualizarStatus() {
        Coleta coleta = cadastrarColetaPadrao(
                "Cliente",
                Prioridade.MEDIA
        );

        Coleta atualizada =
                service.atualizarStatus(
                        coleta.getId(),
                        StatusColeta.EM_ANDAMENTO
                );

        assertEquals(
                StatusColeta.EM_ANDAMENTO,
                atualizada.getStatus()
        );

        assertEquals(
                StatusColeta.EM_ANDAMENTO,
                appState.getColetas()
                        .get(0)
                        .getStatus()
        );

        assertFalse(appState.isCarregando());
    }

    @Test
    void naoDeveAtualizarColetaInexistente() {
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> service.atualizarStatus(
                                999,
                                StatusColeta.CONCLUIDA
                        )
                );

        assertEquals(
                "Coleta não encontrada para atualização.",
                exception.getMessage()
        );

        assertEquals(
                "Coleta não encontrada para atualização.",
                appState.getUltimoErro()
        );

        assertFalse(appState.isCarregando());
    }

    @Test
    void deveContarColetasPorStatus() {
        Coleta primeira = cadastrarColetaPadrao(
                "Cliente 1",
                Prioridade.BAIXA
        );

        cadastrarColetaPadrao(
                "Cliente 2",
                Prioridade.MEDIA
        );

        service.atualizarStatus(
                primeira.getId(),
                StatusColeta.CONCLUIDA
        );

        assertEquals(
                1,
                service.contarPorStatus(
                        StatusColeta.PENDENTE
                )
        );

        assertEquals(
                1,
                service.contarPorStatus(
                        StatusColeta.CONCLUIDA
                )
        );
    }

    @Test
    void deveCalcularTotalDePacotes() {
        cadastrarColeta(
                "Cliente 1",
                5,
                Prioridade.BAIXA
        );

        cadastrarColeta(
                "Cliente 2",
                8,
                Prioridade.ALTA
        );

        assertEquals(
                13,
                service.calcularTotalPacotes()
        );
    }

    private Coleta cadastrarColetaPadrao(
            String nome,
            Prioridade prioridade
    ) {
        return cadastrarColeta(
                nome,
                2,
                prioridade
        );
    }

    private Coleta cadastrarColeta(
            String nome,
            int quantidade,
            Prioridade prioridade
    ) {
        when(viaCepClient.buscarPorCep("01001000"))
                .thenReturn(
                        CompletableFuture.completedFuture(
                                criarEnderecoViaCep()
                        )
                );

        return service.cadastrarPorCep(
                nome,
                "01001000",
                "100",
                "",
                quantidade,
                prioridade
        ).join();
    }

    private EnderecoViaCep criarEnderecoViaCep() {
        return new EnderecoViaCep(
                "01001-000",
                "Praça da Sé",
                "",
                "Sé",
                "São Paulo",
                "SP",
                false
        );
    }
}