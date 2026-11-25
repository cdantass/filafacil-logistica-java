package repository;

import com.filafacil.model.Coleta;
import model.Prioridade;
import model.StatusColeta;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

public class ArquivoColetaRepository
        implements ColetaRepository {

    private final Path caminhoArquivo;
    private final List<Coleta> coletas =
            new ArrayList<>();

    public ArquivoColetaRepository(
            String caminhoArquivo
    ) {
        this.caminhoArquivo =
                Path.of(caminhoArquivo);

        carregar();
    }

    @Override
    public void adicionar(Coleta coleta) {
        if (coleta == null) {
            throw new IllegalArgumentException(
                    "A coleta não pode ser nula."
            );
        }

        coletas.add(coleta);

        try {
            salvar();
        } catch (RuntimeException exception) {
            coletas.remove(coleta);
            throw exception;
        }
    }

    @Override
    public List<Coleta> listarTodas() {
        return new ArrayList<>(coletas);
    }

    @Override
    public Optional<Coleta> buscarPorId(int id) {
        return coletas.stream()
                .filter(coleta ->
                        coleta.getId() == id
                )
                .findFirst();
    }

    @Override
    public int gerarProximoId() {
        return coletas.stream()
                .mapToInt(Coleta::getId)
                .max()
                .orElse(0) + 1;
    }

    @Override
    public Coleta atualizarStatus(
            int id,
            StatusColeta novoStatus
    ) {
        if (novoStatus == null) {
            throw new IllegalArgumentException(
                    "O novo status é obrigatório."
            );
        }

        Coleta coleta = buscarPorId(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Coleta não encontrada para atualização."
                        )
                );

        StatusColeta statusAnterior =
                coleta.getStatus();

        coleta.setStatus(novoStatus);

        try {
            salvar();
            return coleta;

        } catch (RuntimeException exception) {
            coleta.setStatus(statusAnterior);
            throw exception;
        }
    }

    @Override
    public void deletarPorId(int id) {
        Coleta coleta = buscarPorId(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Coleta não encontrada para exclusão."
                        )
                );

        coletas.remove(coleta);

        try {
            salvar();

        } catch (RuntimeException exception) {
            coletas.add(coleta);
            throw exception;
        }
    }

    @Override
    public List<Coleta> listarPaginado(int pagina, int tamanhoPagina) {
        return List.of();
    }

    @Override
    public List<Coleta> listarPorStatus(StatusColeta status) {
        return List.of();
    }

    private void salvar() {
        try {
            Path diretorio =
                    caminhoArquivo.getParent();

            if (diretorio != null) {
                Files.createDirectories(
                        diretorio
                );
            }

            List<String> linhas =
                    coletas.stream()
                            .map(this::transformarEmLinha)
                            .toList();

            Files.write(
                    caminhoArquivo,
                    linhas,
                    StandardCharsets.UTF_8
            );

        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Não foi possível salvar as coletas.",
                    exception
            );
        }
    }

    private void carregar() {
        if (!Files.exists(caminhoArquivo)) {
            return;
        }

        try {
            List<String> linhas =
                    Files.readAllLines(
                            caminhoArquivo,
                            StandardCharsets.UTF_8
                    );

            for (String linha : linhas) {
                if (linha.isBlank()) {
                    continue;
                }

                try {
                    coletas.add(
                            transformarEmColeta(linha)
                    );

                } catch (RuntimeException exception) {
                    System.err.println(
                            "Uma linha inválida foi ignorada no arquivo de coletas."
                    );
                }
            }

        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Não foi possível carregar as coletas.",
                    exception
            );
        }
    }

    private String transformarEmLinha(
            Coleta coleta
    ) {
        return String.join(
                "|",
                String.valueOf(coleta.getId()),
                codificar(coleta.getNomeCliente()),
                codificar(coleta.getEndereco()),
                String.valueOf(
                        coleta.getQuantidadePacotes()
                ),
                coleta.getPrioridade().name(),
                coleta.getStatus().name(),
                coleta.getDataCriacao().toString()
        );
    }

    private Coleta transformarEmColeta(
            String linha
    ) {
        String[] campos =
                linha.split("\\|", -1);

        if (campos.length != 7) {
            throw new IllegalArgumentException(
                    "Registro inválido."
            );
        }

        return new Coleta(
                Integer.parseInt(campos[0]),
                decodificar(campos[1]),
                decodificar(campos[2]),
                Integer.parseInt(campos[3]),
                Prioridade.valueOf(campos[4]),
                StatusColeta.valueOf(campos[5]),
                LocalDateTime.parse(campos[6])
        );
    }

    private String codificar(String valor) {
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(
                        valor.getBytes(
                                StandardCharsets.UTF_8
                        )
                );
    }

    private String decodificar(String valor) {
        return new String(
                Base64.getUrlDecoder()
                        .decode(valor),
                StandardCharsets.UTF_8
        );
    }
}