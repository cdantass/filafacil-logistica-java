package factory;

import com.filafacil.model.Coleta;
import model.Prioridade;
import model.StatusColeta;

import java.time.LocalDateTime;

public class ColetaFactory {

    public Coleta criar(
            int id,
            String nomeCliente,
            String endereco,
            int quantidadePacotes,
            Prioridade prioridade
    ) {
        return new Coleta(
                id,
                nomeCliente,
                endereco,
                quantidadePacotes,
                prioridade,
                StatusColeta.PENDENTE,
                LocalDateTime.now()
        );
    }
}