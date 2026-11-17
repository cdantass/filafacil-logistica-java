package integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import exception.ApiException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class ViaCepClient {

    private static final String URL_BASE =
            "https://viacep.com.br/ws/%s/json/";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ViaCepClient() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();

        this.objectMapper = new ObjectMapper();
    }

    public CompletableFuture<EnderecoViaCep> buscarPorCep(
            String cep
    ) {
        String cepNormalizado = normalizarCep(cep);

        validarCep(cepNormalizado);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(
                        URI.create(
                                URL_BASE.formatted(
                                        cepNormalizado
                                )
                        )
                )
                .timeout(Duration.ofSeconds(10))
                .header("Accept", "application/json")
                .GET()
                .build();

        return httpClient.sendAsync(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                )
                .thenApply(this::processarResposta)
                .exceptionally(exception -> {
                    Throwable causa =
                            obterCausaReal(exception);

                    if (causa instanceof ApiException apiException) {
                        throw apiException;
                    }

                    throw new ApiException(
                            "Não foi possível consultar o CEP.",
                            causa
                    );
                });
    }

    private EnderecoViaCep processarResposta(
            HttpResponse<String> response
    ) {
        if (response.statusCode() != 200) {
            throw new ApiException(
                    "A ViaCEP retornou o status HTTP "
                            + response.statusCode()
                            + "."
            );
        }

        try {
            EnderecoViaCep endereco =
                    objectMapper.readValue(
                            response.body(),
                            EnderecoViaCep.class
                    );

            if (endereco.naoEncontrado()) {
                throw new ApiException(
                        "O CEP informado não foi encontrado."
                );
            }

            if (endereco.formatarEndereco().isBlank()) {
                throw new ApiException(
                        "A ViaCEP retornou um endereço incompleto."
                );
            }

            return endereco;

        } catch (JsonProcessingException exception) {
            throw new ApiException(
                    "A resposta da ViaCEP é inválida.",
                    exception
            );
        }
    }

    private String normalizarCep(String cep) {
        if (cep == null) {
            return "";
        }

        return cep.replaceAll("\\D", "");
    }

    private void validarCep(String cep) {
        if (!cep.matches("\\d{8}")) {
            throw new IllegalArgumentException(
                    "O CEP deve conter exatamente 8 números."
            );
        }
    }

    private Throwable obterCausaReal(
            Throwable exception
    ) {
        if (exception instanceof CompletionException
                && exception.getCause() != null) {

            return exception.getCause();
        }

        return exception;
    }
}