package observer;

import com.filafacil.model.Coleta;
import model.StatusColeta;

public class LogOperacionalObserver
        implements ColetaObserver {

    @Override
    public void aoCriar(Coleta coleta) {
        System.out.println(
                "[LOG] Coleta criada: ID "
                        + coleta.getId()
        );
    }

    @Override
    public void aoAtualizarStatus(
            Coleta coleta,
            StatusColeta statusAnterior
    ) {
        System.out.println(
                "[LOG] Coleta "
                        + coleta.getId()
                        + " alterada de "
                        + statusAnterior
                        + " para "
                        + coleta.getStatus()
        );
    }
}