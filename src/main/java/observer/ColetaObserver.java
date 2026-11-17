package observer;

import com.filafacil.model.Coleta;
import model.StatusColeta;

public interface ColetaObserver {

    void aoCriar(Coleta coleta);

    void aoAtualizarStatus(
            Coleta coleta,
            StatusColeta statusAnterior
    );
}