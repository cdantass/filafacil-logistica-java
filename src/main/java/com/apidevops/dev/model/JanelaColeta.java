package com.apidevops.dev.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "janelas")
public class JanelaColeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate data;

    private LocalTime horarioInicial;

    private LocalTime horarioFinal;

    private Integer capacidadeMaxima;

    private Integer capacidadeOcupada;

    @Enumerated(EnumType.STRING)
    private StatusJanela status;

    public JanelaColeta() {
    }

    public Long getId() {
        return id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHorarioInicial() {
        return horarioInicial;
    }

    public void setHorarioInicial(LocalTime horarioInicial) {
        this.horarioInicial = horarioInicial;
    }

    public LocalTime getHorarioFinal() {
        return horarioFinal;
    }

    public void setHorarioFinal(LocalTime horarioFinal) {
        this.horarioFinal = horarioFinal;
    }

    public Integer getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    public void setCapacidadeMaxima(Integer capacidadeMaxima) {
        this.capacidadeMaxima = capacidadeMaxima;
    }

    public Integer getCapacidadeOcupada() {
        return capacidadeOcupada;
    }

    public void setCapacidadeOcupada(Integer capacidadeOcupada) {
        this.capacidadeOcupada = capacidadeOcupada;
    }

    public StatusJanela getStatus() {
        return status;
    }

    public void setStatus(StatusJanela status) {
        this.status = status;
    }
}