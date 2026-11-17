package state;

import com.filafacil.model.Coleta;
import model.Prioridade;
import model.StatusColeta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AppStateTest {

    private AppState appState;

    @BeforeEach
    void configurar() {
        appState = new AppState();
    }

    @Test
    void deveIniciarComEstadoPadrao() {
        assertTrue(appState.getColetas().isEmpty());
        assertFalse(appState.isCarregando());
        assertNull(appState.getUltimoErro());
        assertNull(appState.getFiltroStatus());
        assertNull(appState.getFiltroPrioridade());
    }

    @Test
    void deveAtualizarListaDeColetas() {
        Coleta coleta = criarColeta();

        appState.setColetas(
                List.of(coleta)
        );

        assertEquals(
                1,
                appState.getColetas().size()
        );

        assertEquals(
                coleta,
                appState.getColetas().get(0)
        );
    }

    @Test
    void deveCriarCopiaDaListaRecebida() {
        Coleta coleta = criarColeta();

        List<Coleta> listaOriginal =
                new java.util.ArrayList<>();

        listaOriginal.add(coleta);

        appState.setColetas(listaOriginal);

        listaOriginal.clear();

        assertEquals(
                1,
                appState.getColetas().size()
        );
    }

    @Test
    void naoDevePermitirAlteracaoExternaDaLista() {
        appState.setColetas(
                List.of(criarColeta())
        );

        List<Coleta> resultado =
                appState.getColetas();

        assertThrows(
                UnsupportedOperationException.class,
                resultado::clear
        );

        assertEquals(
                1,
                appState.getColetas().size()
        );
    }

    @Test
    void deveUsarListaVaziaQuandoReceberNull() {
        appState.setColetas(
                List.of(criarColeta())
        );

        appState.setColetas(null);

        assertTrue(
                appState.getColetas().isEmpty()
        );
    }

    @Test
    void deveAtualizarEstadoDeCarregamento() {
        appState.setCarregando(true);

        assertTrue(appState.isCarregando());

        appState.setCarregando(false);

        assertFalse(appState.isCarregando());
    }

    @Test
    void deveRegistrarUltimoErro() {
        appState.setUltimoErro(
                "Erro ao consultar o CEP."
        );

        assertEquals(
                "Erro ao consultar o CEP.",
                appState.getUltimoErro()
        );
    }

    @Test
    void deveLimparUltimoErro() {
        appState.setUltimoErro(
                "Erro de teste"
        );

        appState.limparErro();

        assertNull(appState.getUltimoErro());
    }

    @Test
    void deveAtualizarFiltroDeStatus() {
        appState.setFiltroStatus(
                StatusColeta.PENDENTE
        );

        assertEquals(
                StatusColeta.PENDENTE,
                appState.getFiltroStatus()
        );
    }

    @Test
    void deveAtualizarFiltroDePrioridade() {
        appState.setFiltroPrioridade(
                Prioridade.ALTA
        );

        assertEquals(
                Prioridade.ALTA,
                appState.getFiltroPrioridade()
        );
    }

    @Test
    void deveLimparFiltros() {
        appState.setFiltroStatus(
                StatusColeta.CONCLUIDA
        );

        appState.setFiltroPrioridade(
                Prioridade.MEDIA
        );

        appState.limparFiltros();

        assertNull(appState.getFiltroStatus());
        assertNull(appState.getFiltroPrioridade());
    }

    private Coleta criarColeta() {
        return new Coleta(
                1,
                "Cliente Teste",
                "Praça da Sé, 100, São Paulo, SP",
                5,
                Prioridade.ALTA
        );
    }
}