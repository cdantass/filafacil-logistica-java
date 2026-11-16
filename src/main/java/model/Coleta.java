package model;

import java.time.LocalDateTime;

public class Coleta {

    private int id;
    private String nomeCliente;
    private String endereco;
    private int quantidadePacotes;
    private Prioridade prioridade;
    private StatusColeta status;
    private LocalDateTime dataCriacao;

    public Coleta(
            int id,
            String nomeCliente,
            String endereco,
            int quantidadePacotes,
            Prioridade prioridade
    ) {
        this.id = id;
        this.nomeCliente = nomeCliente;
        this.endereco = endereco;
        this.quantidadePacotes = quantidadePacotes;
        this.prioridade = prioridade;
        this.status = StatusColeta.PENDENTE;
        this.dataCriacao = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public String getEndereco() {
        return endereco;
    }

    public int getQuantidadePacotes() {
        return quantidadePacotes;
    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public StatusColeta getStatus() {
        return status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setStatus(
            StatusColeta status
    ) {
        this.status = status;
    }

    @Override
    public String toString() {
        return """
                --------------------------------
                ID: %d
                Cliente: %s
                Endereço: %s
                Pacotes: %d
                Prioridade: %s
                Status: %s
                Data de criação: %s
                --------------------------------
                """
                .formatted(
                        id,
                        nomeCliente,
                        endereco,
                        quantidadePacotes,
                        prioridade,
                        status,
                        dataCriacao
                );
    }
}