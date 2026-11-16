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

public class ColetaRepository {

    private final Path caminhoArquivo;

    private final List<Coleta> coletas =
            new ArrayList<>();

    public ColetaRepository(
            String caminhoArquivo
    ) {
        this.caminhoArquivo =
                Path.of(caminhoArquivo);

        carregar();
    }

    public void adicionar(
            Coleta coleta
    ) {
        coletas.add(coleta);
        salvar();
    }

    public List<Coleta> listarTodas() {
        return new ArrayList<>(coletas);
    }

    public int gerarProximoId() {
        return coletas.stream()
                .mapToInt(Coleta::getId)
                .max()
                .orElse(0)
                + 1;
    }

    public void salvar() {

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
                    Coleta coleta =
                            transformarEmColeta(
                                    linha
                            );

                    coletas.add(coleta);

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
                    "Registro inválido"
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

    private String codificar(
            String valor
    ) {
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(
                        valor.getBytes(
                                StandardCharsets.UTF_8
                        )
                );
    }

    private String decodificar(
            String valor
    ) {
        return new String(
                Base64.getUrlDecoder()
                        .decode(valor),
                StandardCharsets.UTF_8
        );
    }
}