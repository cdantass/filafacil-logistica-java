package service;

import com.filafacil.model.Coleta;
import integration.EnderecoViaCep;
import integration.ViaCepClient;
import model.Prioridade;
import model.StatusColeta;
import repository.ArquivoColetaRepository;
import state.AppState;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ColetaService {

    private final ViaCepClient viaCepClient;
    private final ArquivoColetaRepository repository;
    private final AppState appState;

    public ColetaService(
            ArquivoColetaRepository repository,
            AppState appState,
            ViaCepClient viaCepClient
    ) {
        this.repository = repository;
        this.appState = appState;
        this.viaCepClient = viaCepClient;

        sincronizarEstado();
    }

    public CompletableFuture<Coleta> cadastrarPorCep(
            String nomeCliente,
            String cep,
            String numero,
            String complemento,
            int quantidadePacotes,
            Prioridade prioridade
    ) {
        validarTexto(
                nomeCliente,
                "O nome do cliente é obrigatório."
        );

        validarTexto(
                numero,
                "O número do endereço é obrigatório."
        );

        if (quantidadePacotes <= 0) {
            throw new IllegalArgumentException(
                    "A quantidade de pacotes deve ser maior que zero."
            );
        }

        if (prioridade == null) {
            throw new IllegalArgumentException(
                    "A prioridade é obrigatória."
            );
        }

        appState.setCarregando(true);
        appState.limparErro();

        return viaCepClient.buscarPorCep(cep)
                .thenApply(enderecoViaCep -> {
                    String enderecoCompleto =
                            montarEnderecoCompleto(
                                    enderecoViaCep,
                                    numero,
                                    complemento
                            );

                    Coleta coleta = new Coleta(
                            repository.gerarProximoId(),
                            nomeCliente.trim(),
                            enderecoCompleto,
                            quantidadePacotes,
                            prioridade
                    );

                    repository.adicionar(coleta);
                    sincronizarEstado();

                    return coleta;
                })
                .whenComplete((coleta, exception) -> {
                    appState.setCarregando(false);

                    if (exception != null) {
                        Throwable causa = exception.getCause() != null
                                ? exception.getCause()
                                : exception;

                        appState.setUltimoErro(
                                causa.getMessage()
                        );
                    }
                });
    }

    private String montarEnderecoCompleto(
            EnderecoViaCep enderecoViaCep,
            String numero,
            String complemento
    ) {
        StringBuilder endereco = new StringBuilder(
                enderecoViaCep.formatarEndereco()
        );

        endereco.append(", nº ")
                .append(numero.trim());

        if (complemento != null
                && !complemento.isBlank()) {

            endereco.append(", ")
                    .append(complemento.trim());
        }

        endereco.append(", CEP ")
                .append(enderecoViaCep.cep());

        return endereco.toString();
    }

    public Coleta cadastrar(
            String nomeCliente,
            String endereco,
            int quantidadePacotes,
            Prioridade prioridade
    ) {
        appState.setCarregando(true);
        appState.limparErro();

        try {
            validarTexto(
                    nomeCliente,
                    "O nome do cliente é obrigatório."
            );

            validarTexto(
                    endereco,
                    "O endereço é obrigatório."
            );

            if (quantidadePacotes <= 0) {
                throw new IllegalArgumentException(
                        "A quantidade de pacotes deve ser maior que zero."
                );
            }

            if (prioridade == null) {
                throw new IllegalArgumentException(
                        "A prioridade é obrigatória."
                );
            }

            Coleta coleta = new Coleta(
                    repository.gerarProximoId(),
                    nomeCliente.trim(),
                    endereco.trim(),
                    quantidadePacotes,
                    prioridade
            );

            repository.adicionar(coleta);
            sincronizarEstado();

            return coleta;

        } catch (RuntimeException exception) {
            registrarErro(exception);
            throw exception;

        } finally {
            appState.setCarregando(false);
        }
    }

    public List<Coleta> listarTodas() {
        return appState.getColetas();
    }

    public List<Coleta> filtrarPorStatus(
            StatusColeta status
    ) {
        if (status == null) {
            throw new IllegalArgumentException(
                    "O status é obrigatório."
            );
        }

        appState.setFiltroStatus(status);

        return appState.getColetas()
                .stream()
                .filter(coleta ->
                        coleta.getStatus() == status
                )
                .toList();
    }

    public List<Coleta> filtrarPorPrioridade(
            Prioridade prioridade
    ) {
        if (prioridade == null) {
            throw new IllegalArgumentException(
                    "A prioridade é obrigatória."
            );
        }

        appState.setFiltroPrioridade(prioridade);

        return appState.getColetas()
                .stream()
                .filter(coleta ->
                        coleta.getPrioridade() == prioridade
                )
                .toList();
    }

    public Coleta atualizarStatus(
            int id,
            StatusColeta novoStatus
    ) {
        if (id <= 0) {
            throw new IllegalArgumentException(
                    "O identificador deve ser maior que zero."
            );
        }

        if (novoStatus == null) {
            throw new IllegalArgumentException(
                    "O novo status é obrigatório."
            );
        }

        appState.setCarregando(true);
        appState.limparErro();

        try {
            Coleta coletaAtualizada =
                    repository.atualizarStatus(
                            id,
                            novoStatus
                    );

            sincronizarEstado();

            return coletaAtualizada;

        } catch (RuntimeException exception) {
            registrarErro(exception);
            throw exception;

        } finally {
            appState.setCarregando(false);
        }
    }

    public long contarPorStatus(
            StatusColeta status
    ) {
        if (status == null) {
            throw new IllegalArgumentException(
                    "O status é obrigatório."
            );
        }

        return appState.getColetas()
                .stream()
                .filter(coleta ->
                        coleta.getStatus() == status
                )
                .count();
    }

    public int calcularTotalPacotes() {
        return appState.getColetas()
                .stream()
                .mapToInt(
                        Coleta::getQuantidadePacotes
                )
                .sum();
    }

    public AppState getAppState() {
        return appState;
    }

    private void sincronizarEstado() {
        appState.setColetas(
                repository.listarTodas()
        );
    }

    private void registrarErro(
            RuntimeException exception
    ) {
        appState.setUltimoErro(
                exception.getMessage()
        );
    }

    private void validarTexto(
            String valor,
            String mensagem
    ) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(
                    mensagem
            );
        }
    }
}