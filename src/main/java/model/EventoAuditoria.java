package model;

import java.time.LocalDateTime;

public class EventoAuditoria {

    private String tipo;
    private String usuario;
    private String entidade;
    private String dadosAntigos;
    private String dadosNovos;
    private LocalDateTime dataEvento;

    public EventoAuditoria(
            String tipo,
            String usuario,
            String entidade,
            String dadosAntigos,
            String dadosNovos,
            LocalDateTime dataEvento
    ) {
        this.tipo = tipo;
        this.usuario = usuario;
        this.entidade = entidade;
        this.dadosAntigos = dadosAntigos;
        this.dadosNovos = dadosNovos;
        this.dataEvento = dataEvento;
    }

    public String getTipo() {
        return tipo;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getEntidade() {
        return entidade;
    }

    public String getDadosAntigos() {
        return dadosAntigos;
    }

    public String getDadosNovos() {
        return dadosNovos;
    }

    public LocalDateTime getDataEvento() {
        return dataEvento;
    }
}