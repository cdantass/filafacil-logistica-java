package observer;

import com.filafacil.model.Coleta;
import model.EventoAuditoria;
import model.StatusColeta;
import repository.MongoAuditoriaRepository;

import java.time.LocalDateTime;

public class AuditoriaObserver
        implements ColetaObserver {

    private final MongoAuditoriaRepository repository;

    public AuditoriaObserver(
            MongoAuditoriaRepository repository
    ) {
        this.repository = repository;
    }

    @Override
    public void aoCriar(Coleta coleta) {

        EventoAuditoria evento =
                new EventoAuditoria(
                        "CRIACAO",
                        "sistema",
                        "coleta",
                        null,
                        coleta.toString(),
                        LocalDateTime.now()
                );

        repository.salvar(evento);
    }

    @Override
    public void aoAtualizarStatus(
            Coleta coleta,
            StatusColeta statusAnterior
    ) {

        EventoAuditoria evento =
                new EventoAuditoria(
                        "ALTERACAO_STATUS",
                        "sistema",
                        "coleta",
                        statusAnterior.name(),
                        coleta.getStatus().name(),
                        LocalDateTime.now()
                );

        repository.salvar(evento);
    }
}
