package service;

import com.filafacil.model.Coleta;
import model.Prioridade;
import repository.ColetaRepository;

import java.util.List;

public class ColetaService {

    private final ColetaRepository repository;

    public ColetaService(
            ColetaRepository repository
    ) {
        this.repository = repository;
    }

    public Coleta cadastrar(
            String nomeCliente,
            String endereco,
            int quantidadePacotes,
            Prioridade prioridade
    ) {
        validarTexto(
                nomeCliente,
                "O nome do cliente é obrigatório."
        );

        validarTexto(
                endereco,
                "O endereço é obrigatório."
        );

        if (quantidadePacotes <= 0) {
            throw new IllegalArgumentException(
                    "A quantidade de pacotes deve ser maior que zero."
            );
        }

        if (prioridade == null) {
            throw new IllegalArgumentException(
                    "A prioridade é obrigatória."
            );
        }

        int id =
                repository.gerarProximoId();

        Coleta coleta =
                new Coleta(
                        id,
                        nomeCliente.trim(),
                        endereco.trim(),
                        quantidadePacotes,
                        prioridade
                );

        repository.adicionar(coleta);

        return coleta;
    }

    public List<Coleta> listarTodas() {
        return repository.listarTodas();
    }

    private void validarTexto(
            String valor,
            String mensagem
    ) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(
                    mensagem
            );
        }
    }
}