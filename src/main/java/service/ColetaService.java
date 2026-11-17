package service;

import com.filafacil.model.Coleta;
import factory.ColetaFactory;
import integration.EnderecoViaCep;
import integration.ViaCepClient;
import model.Prioridade;
import model.StatusColeta;
import observer.ColetaObserver;
import repository.ColetaRepository;
import state.AppState;
import strategy.EstrategiaPrioridade;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ColetaService {

    private final ColetaRepository repository;
    private final AppState appState;
    private final ViaCepClient viaCepClient;
    private final ColetaFactory coletaFactory;
    private final EstrategiaPrioridade estrategiaPrioridade;
    private final List<ColetaObserver> observers;

    public ColetaService(
            ColetaRepository repository,
            AppState appState,
            ViaCepClient viaCepClient,
            ColetaFactory coletaFactory,
            EstrategiaPrioridade estrategiaPrioridade,
            List<ColetaObserver> observers
    ) {
        this.repository = repository;
        this.appState = appState;
        this.viaCepClient = viaCepClient;
        this.coletaFactory = coletaFactory;
        this.estrategiaPrioridade = estrategiaPrioridade;

        this.observers = observers == null
                ? new ArrayList<>()
                : new ArrayList<>(observers);

        sincronizarEstado();
    }

    public CompletableFuture<Coleta> cadastrarPorCep(
            String nomeCliente,
            String cep,
            String numero,
            String complemento,
            int quantidadePacotes,
            Prioridade prioridadeInformada
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

        appState.setCarregando(true);
        appState.limparErro();

        Prioridade prioridadeDefinida;

        try {
            prioridadeDefinida =
                    estrategiaPrioridade.definirPrioridade(
                            quantidadePacotes,
                            prioridadeInformada
                    );
        } catch (RuntimeException exception) {
            registrarErro(exception);
            appState.setCarregando(false);
            throw exception;
        }

        return viaCepClient.buscarPorCep(cep)
                .thenApply(enderecoViaCep -> {
                    String enderecoCompleto =
                            montarEnderecoCompleto(
                                    enderecoViaCep,
                                    numero,
                                    complemento
                            );

                    Coleta coleta = coletaFactory.criar(
                            repository.gerarProximoId(),
                            nomeCliente.trim(),
                            enderecoCompleto,
                            quantidadePacotes,
                            prioridadeDefinida
                    );

                    repository.adicionar(coleta);
                    sincronizarEstado();
                    notificarCriacao(coleta);

                    return coleta;
                })
                .whenComplete((coleta, exception) -> {
                    appState.setCarregando(false);

                    if (exception != null) {
                        Throwable causa =
                                exception.getCause() != null
                                        ? exception.getCause()
                                        : exception;

                        appState.setUltimoErro(
                                causa.getMessage()
                        );
                    }
                });
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
        validarId(id);

        if (novoStatus == null) {
            throw new IllegalArgumentException(
                    "O novo status é obrigatório."
            );
        }

        appState.setCarregando(true);
        appState.limparErro();

        try {
            Coleta coletaAnterior = repository.buscarPorId(id)
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Coleta não encontrada para atualização."
                            )
                    );

            StatusColeta statusAnterior =
                    coletaAnterior.getStatus();

            Coleta coletaAtualizada =
                    repository.atualizarStatus(
                            id,
                            novoStatus
                    );

            sincronizarEstado();

            notificarAtualizacao(
                    coletaAtualizada,
                    statusAnterior
            );

            return coletaAtualizada;

        } catch (RuntimeException exception) {
            registrarErro(exception);
            throw exception;

        } finally {
            appState.setCarregando(false);
        }
    }

    public void deletar(int id) {
        validarId(id);

        appState.setCarregando(true);
        appState.limparErro();

        try {
            repository.deletarPorId(id);
            sincronizarEstado();

        } catch (RuntimeException exception) {
            registrarErro(exception);
            throw exception;

        } finally {
            appState.setCarregando(false);
        }
    }

    private void notificarCriacao(
            Coleta coleta
    ) {
        for (ColetaObserver observer : observers) {
            try {
                observer.aoCriar(coleta);
            } catch (RuntimeException exception) {
                System.err.println(
                        "Falha ao notificar criação: "
                                + exception.getMessage()
                );
            }
        }
    }

    private void notificarAtualizacao(
            Coleta coleta,
            StatusColeta statusAnterior
    ) {
        for (ColetaObserver observer : observers) {
            try {
                observer.aoAtualizarStatus(
                        coleta,
                        statusAnterior
                );
            } catch (RuntimeException exception) {
                System.err.println(
                        "Falha ao notificar atualização: "
                                + exception.getMessage()
                );
            }
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

    private String montarEnderecoCompleto(
            EnderecoViaCep enderecoViaCep,
            String numero,
            String complemento
    ) {
        StringBuilder endereco =
                new StringBuilder(
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

    private void validarId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException(
                    "O identificador deve ser maior que zero."
            );
        }
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