package state;

import com.filafacil.model.Coleta;
import model.Prioridade;
import model.StatusColeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppState {

    private List<Coleta> coletas;
    private boolean carregando;
    private String ultimoErro;
    private StatusColeta filtroStatus;
    private Prioridade filtroPrioridade;

    public AppState() {
        this.coletas = new ArrayList<>();
        this.carregando = false;
        this.ultimoErro = null;
        this.filtroStatus = null;
        this.filtroPrioridade = null;
    }

    public List<Coleta> getColetas() {
        return Collections.unmodifiableList(
                new ArrayList<>(coletas)
        );
    }

    public void setColetas(List<Coleta> coletas) {
        if (coletas == null) {
            this.coletas = new ArrayList<>();
            return;
        }

        this.coletas = new ArrayList<>(coletas);
    }

    public boolean isCarregando() {
        return carregando;
    }

    public void setCarregando(boolean carregando) {
        this.carregando = carregando;
    }

    public String getUltimoErro() {
        return ultimoErro;
    }

    public void setUltimoErro(String ultimoErro) {
        this.ultimoErro = ultimoErro;
    }

    public void limparErro() {
        this.ultimoErro = null;
    }

    public StatusColeta getFiltroStatus() {
        return filtroStatus;
    }

    public void setFiltroStatus(
            StatusColeta filtroStatus
    ) {
        this.filtroStatus = filtroStatus;
    }

    public Prioridade getFiltroPrioridade() {
        return filtroPrioridade;
    }

    public void setFiltroPrioridade(
            Prioridade filtroPrioridade
    ) {
        this.filtroPrioridade = filtroPrioridade;
    }

    public void limparFiltros() {
        this.filtroStatus = null;
        this.filtroPrioridade = null;
    }
}