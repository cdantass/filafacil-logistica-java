package com.apidevops.dev.controller.v1;

import com.apidevops.dev.model.JanelaColeta;
import com.apidevops.dev.service.JanelaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/janelas")
public class JanelaControllerV1 {

    private final JanelaService service;

    public JanelaControllerV1(
            JanelaService service
    ) {
        this.service = service;
    }

    @PostMapping
    public JanelaColeta criar(
            @RequestBody JanelaColeta janela
    ) {
        return service.salvar(janela);
    }

    @GetMapping
    public List<JanelaColeta> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public JanelaColeta buscar(
            @PathVariable Long id
    ) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public JanelaColeta atualizar(
            @PathVariable Long id,
            @RequestBody JanelaColeta janela
    ) {
        return service.atualizar(id, janela);
    }

    @DeleteMapping("/{id}")
    public void excluir(
            @PathVariable Long id
    ) {
        service.excluir(id);
    }
}