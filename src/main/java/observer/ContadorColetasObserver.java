package observer;

import com.filafacil.model.Coleta;
import model.StatusColeta;

public class ContadorColetasObserver
        implements ColetaObserver {

    private int totalCriacoes;
    private int totalAtualizacoes;

    @Override
    public void aoCriar(Coleta coleta) {
        totalCriacoes++;
    }

    @Override
    public void aoAtualizarStatus(
            Coleta coleta,
            StatusColeta statusAnterior
    ) {
        totalAtualizacoes++;
    }

    public int getTotalCriacoes() {
        return totalCriacoes;
    }

    public int getTotalAtualizacoes() {
        return totalAtualizacoes;
    }
}