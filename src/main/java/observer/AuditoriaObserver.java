package observer;

import com.filafacil.model.Coleta;
import model.Auditoria;
import model.StatusColeta;
import repository.AuditoriaRepository;

import java.time.LocalDateTime;

public class AuditoriaObserver
        implements ColetaObserver {

    private final AuditoriaRepository auditoriaRepository;

    public AuditoriaObserver(
            AuditoriaRepository auditoriaRepository
    ) {
        this.auditoriaRepository =
                auditoriaRepository;
    }

    @Override
    public void aoCriar(Coleta coleta) {

        auditoriaRepository.salvar(
                new Auditoria(
                        (long) coleta.getId(),
                        "CRIACAO",
                        "Coleta criada para o cliente "
                                + coleta.getNomeCliente(),
                        LocalDateTime.now()
                )
        );
    }

    @Override
    public void aoAtualizarStatus(
            Coleta coleta,
            StatusColeta statusAnterior
    ) {

        auditoriaRepository.salvar(
                new Auditoria(
                        (long) coleta.getId(),
                        "ALTERACAO_STATUS",
                        "Status alterado de "
                                + statusAnterior
                                + " para "
                                + coleta.getStatus(),
                        LocalDateTime.now()
                )
        );
    }
}