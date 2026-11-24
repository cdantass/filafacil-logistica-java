package model;

import java.time.LocalDateTime;

public class Auditoria {

    private Long id;
    private Long coletaId;
    private String acao;
    private String observacao;
    private LocalDateTime dataEvento;

    public Auditoria(
            Long coletaId,
            String acao,
            String observacao,
            LocalDateTime dataEvento
    ) {
        this.coletaId = coletaId;
        this.acao = acao;
        this.observacao = observacao;
        this.dataEvento = dataEvento;
    }

    public Long getColetaId() {
        return coletaId;
    }

    public String getAcao() {
        return acao;
    }

    public String getObservacao() {
        return observacao;
    }

    public LocalDateTime getDataEvento() {
        return dataEvento;
    }
}