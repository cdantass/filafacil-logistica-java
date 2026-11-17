package integration;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public record EnderecoViaCep(
        String cep,
        String logradouro,
        String complemento,
        String bairro,
        String localidade,
        String uf,
        Boolean erro
) {

    public boolean naoEncontrado() {
        return Boolean.TRUE.equals(erro);
    }

    public String formatarEndereco() {
        StringBuilder endereco =
                new StringBuilder();

        adicionarParte(endereco, logradouro);
        adicionarParte(endereco, bairro);
        adicionarParte(endereco, localidade);
        adicionarParte(endereco, uf);

        return endereco.toString();
    }

    private void adicionarParte(
            StringBuilder endereco,
            String valor
    ) {
        if (valor == null || valor.isBlank()) {
            return;
        }

        if (!endereco.isEmpty()) {
            endereco.append(", ");
        }

        endereco.append(valor.trim());
    }
}