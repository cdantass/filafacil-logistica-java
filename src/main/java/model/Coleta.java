package com.filafacil.model;

import model.Prioridade;
import model.StatusColeta;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Coleta {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern(
                    "dd/MM/yyyy HH:mm"
            );

    private final int id;
    private final String nomeCliente;
    private final String endereco;
    private final int quantidadePacotes;
    private final Prioridade prioridade;
    private StatusColeta status;
    private final LocalDateTime dataCriacao;

    public Coleta(
            int id,
            String nomeCliente,
            String endereco,
            int quantidadePacotes,
            Prioridade prioridade
    ) {
        this(
                id,
                nomeCliente,
                endereco,
                quantidadePacotes,
                prioridade,
                StatusColeta.PENDENTE,
                LocalDateTime.now()
        );
    }
    public Coleta(
            int id,
            String nomeCliente,
            String endereco,
            int quantidadePacotes,
            Prioridade prioridade,
            StatusColeta status,
            LocalDateTime dataCriacao
    ) {
        this.id = id;
        this.nomeCliente = nomeCliente;
        this.endereco = endereco;
        this.quantidadePacotes = quantidadePacotes;
        this.prioridade = prioridade;
        this.status = status;
        this.dataCriacao = dataCriacao;
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
                Quantidade de pacotes: %d
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
                        dataCriacao.format(FORMATTER)
                );
    }
}