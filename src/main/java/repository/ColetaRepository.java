package repository;

import com.filafacil.model.Coleta;
import model.StatusColeta;

import java.util.List;
import java.util.Optional;

public interface ColetaRepository {

    void adicionar(Coleta coleta);

    List<Coleta> listarTodas();

    Optional<Coleta> buscarPorId(int id);

    int gerarProximoId();

    Coleta atualizarStatus(
            int id,
            StatusColeta novoStatus
    );

    void deletarPorId(int id);

    List<Coleta> listarPaginado(
            int pagina,
            int tamanhoPagina
    );

    List<Coleta> listarPorStatus(
            StatusColeta status
    );
}