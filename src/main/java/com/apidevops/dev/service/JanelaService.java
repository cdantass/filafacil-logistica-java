package com.apidevops.dev.service;

import com.apidevops.dev.exception.RecursoNaoEncontradoException;
import com.apidevops.dev.model.JanelaColeta;
import com.apidevops.dev.repository.JanelaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JanelaService {

    private final JanelaRepository repository;


    private static final Logger logger =
            LoggerFactory.getLogger(
                    JanelaService.class
            );

    public JanelaService(
            JanelaRepository repository
    ) {
        this.repository = repository;
    }

    public JanelaColeta salvar(
            JanelaColeta janela
    ) {
        return repository.save(janela);
    }

    public List<JanelaColeta> listar() {
        return repository.findAll();
    }

    public JanelaColeta buscarPorId(
            Long id
    ) {
        return repository.findById(id)
                .orElseThrow(
                        () -> new RecursoNaoEncontradoException(
                                "Janela não encontrada."
                        )
                );
    }

    public void excluir(
            Long id
    ) {
        repository.deleteById(id);
    }

    public JanelaColeta atualizar(
            Long id,
            JanelaColeta novaJanela
    ) {

        JanelaColeta atual =
                repository.findById(id)
                        .orElseThrow(
                                () -> new RecursoNaoEncontradoException(
                                        "Janela não encontrada."
                                )
                        );

        atual.setData(
                novaJanela.getData()
        );

        atual.setHorarioInicial(
                novaJanela.getHorarioInicial()
        );

        atual.setHorarioFinal(
                novaJanela.getHorarioFinal()
        );

        atual.setCapacidadeMaxima(
                novaJanela.getCapacidadeMaxima()
        );

        atual.setCapacidadeOcupada(
                novaJanela.getCapacidadeOcupada()
        );

        atual.setStatus(
                novaJanela.getStatus()
        );

        return repository.save(atual);
    }
}