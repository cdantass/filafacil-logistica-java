package com.apidevops.dev.controller.v2;

import com.apidevops.dev.dto.JanelaResponseV2;
import com.apidevops.dev.model.JanelaColeta;
import com.apidevops.dev.service.JanelaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/janelas")
public class JanelaControllerV2 {

    private final JanelaService service;

    public JanelaControllerV2(
            JanelaService service
    ) {
        this.service = service;
    }

    @GetMapping
    public List<JanelaResponseV2> listar() {

        return service
                .listar()
                .stream()
                .map(this::converter)
                .toList();
    }

    @GetMapping("/{id}")
    public JanelaResponseV2 buscar(
            @PathVariable Long id
    ) {

        return converter(
                service.buscarPorId(id)
        );
    }

    private JanelaResponseV2 converter(
            JanelaColeta janela
    ) {

        int percentual =
                (int) (
                        (
                                janela.getCapacidadeOcupada()
                                        * 100.0
                        )
                                / janela.getCapacidadeMaxima()
                );

        return new JanelaResponseV2(
                janela.getId(),
                janela.getStatus().name(),
                percentual
        );
    }
}