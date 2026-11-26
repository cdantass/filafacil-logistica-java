package com.apidevops.dev.dto;

public class JanelaResponseV2 {

    private Long id;

    private String status;

    private Integer ocupacaoPercentual;

    public JanelaResponseV2(
            Long id,
            String status,
            Integer ocupacaoPercentual
    ) {
        this.id = id;
        this.status = status;
        this.ocupacaoPercentual =
                ocupacaoPercentual;
    }

    public Long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public Integer getOcupacaoPercentual() {
        return ocupacaoPercentual;
    }
}