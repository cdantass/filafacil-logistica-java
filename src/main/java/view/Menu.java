package view;

import com.filafacil.model.Coleta;
import model.Prioridade;
import model.StatusColeta;
import service.ColetaService;
import java.util.concurrent.CompletionException;
import java.util.List;
import java.util.Scanner;

public class Menu {

    private final Scanner scanner;
    private final ColetaService service;

    public Menu(
            ColetaService service
    ) {
        this.service = service;
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {

        int opcao = -1;

        while (opcao != 0) {

            exibirOpcoes();

            opcao = lerNumeroInteiro(
                    "Escolha uma opção: "
            );

            executarOpcao(opcao);
        }

        scanner.close();

        System.out.println(
                "Sistema encerrado."
        );
    }

    private void exibirOpcoes() {

        System.out.println();
        System.out.println(
                "================================"
        );
        System.out.println(
                "       FILAFÁCIL LOGÍSTICA"
        );
        System.out.println(
                "1. Cadastrar coleta"
        );
        System.out.println(
                "2. Listar coletas"
        );
        System.out.println(
                "3. Filtrar por status"
        );
        System.out.println(
                "4. Filtrar por prioridade"
        );
        System.out.println(
                "5. Atualizar status"
        );
        System.out.println(
                "6. Exibir resumo operacional"
        );
        System.out.println("7. Excluir coleta");

        System.out.println(
                "0. Sair"
        );
    }

    private void executarOpcao(int opcao) {
        try {
            switch (opcao) {
                case 1 -> cadastrarColeta();
                case 2 -> listarColetas();
                case 3 -> filtrarPorStatus();
                case 4 -> filtrarPorPrioridade();
                case 5 -> atualizarStatus();
                case 6 -> exibirResumoOperacional();
                case 7 -> excluirColeta();
                case 0 -> System.out.println("Encerrando...");
                default -> System.out.println(
                        "Opção inválida. Tente novamente."
                );
            }
        } catch (CompletionException exception) {
            Throwable causa = exception.getCause();

            String mensagem = causa != null
                    ? causa.getMessage()
                    : exception.getMessage();

            System.out.println("Erro: " + mensagem);

        } catch (IllegalArgumentException |
                 IllegalStateException exception) {

            System.out.println(
                    "Erro: " + exception.getMessage()
            );
        }
    }

    private void excluirColeta() {
        System.out.println();
        System.out.println("=== EXCLUSÃO DE COLETA ===");

        int id = lerNumeroPositivo(
                "ID da coleta: "
        );

        service.deletar(id);

        System.out.println(
                "Coleta excluída com sucesso."
        );
    }

    private void cadastrarColeta() {
        System.out.println();
        System.out.println("=== CADASTRO DE COLETA ===");

        String nomeCliente =
                lerTextoObrigatorio(
                        "Nome do cliente: "
                );

        String cep =
                lerTextoObrigatorio(
                        "CEP da coleta: "
                );

        String numero =
                lerTextoObrigatorio(
                        "Número do endereço: "
                );

        String complemento =
                lerTextoOpcional(
                        "Complemento, pressione Enter para ignorar: "
                );

        int quantidadePacotes =
                lerNumeroPositivo(
                        "Quantidade de pacotes: "
                );

        Prioridade prioridade =
                lerPrioridade();

        System.out.println();
        System.out.println("Consultando o CEP...");

        Coleta coleta = service.cadastrarPorCep(
                nomeCliente,
                cep,
                numero,
                complemento,
                quantidadePacotes,
                prioridade
        ).join();

        System.out.println();
        System.out.println("Coleta cadastrada com sucesso!");
        System.out.println("Identificador: " + coleta.getId());
        System.out.println("Endereço: " + coleta.getEndereco());
        System.out.println("Status inicial: " + coleta.getStatus());
    }

    private String lerTextoOpcional(
            String mensagem
    ) {
        System.out.print(mensagem);

        return scanner.nextLine().trim();
    }

    private void listarColetas() {

        System.out.println();
        System.out.println(
                "=== LISTA DE COLETAS ==="
        );

        List<Coleta> coletas =
                service.listarTodas();

        if (coletas.isEmpty()) {

            System.out.println(
                    "Nenhuma coleta cadastrada."
            );

            return;
        }

        coletas.forEach(
                System.out::println
        );

        System.out.println(
                "Total de coletas: "
                        + coletas.size()
        );
    }

    private String lerTextoObrigatorio(
            String mensagem
    ) {

        while (true) {

            System.out.print(mensagem);

            String valor =
                    scanner.nextLine()
                            .trim();

            if (!valor.isBlank()) {
                return valor;
            }

            System.out.println(
                    "O valor não pode ficar vazio."
            );
        }
    }

    private int lerNumeroPositivo(
            String mensagem
    ) {

        while (true) {

            int numero =
                    lerNumeroInteiro(
                            mensagem
                    );

            if (numero > 0) {
                return numero;
            }

            System.out.println(
                    "Informe um número maior que zero."
            );
        }
    }

    private int lerNumeroInteiro(
            String mensagem
    ) {

        while (true) {

            System.out.print(mensagem);

            String entrada =
                    scanner.nextLine()
                            .trim();

            try {
                return Integer.parseInt(
                        entrada
                );

            } catch (NumberFormatException exception) {

                System.out.println(
                        "Informe um número inteiro válido."
                );
            }
        }
    }

    private void filtrarPorStatus() {
        System.out.println();
        System.out.println("=== FILTRO POR STATUS ===");

        StatusColeta status = lerStatus();

        exibirColetas(
                service.filtrarPorStatus(status),
                "Nenhuma coleta encontrada com o status informado."
        );
    }

    private void filtrarPorPrioridade() {
        System.out.println();
        System.out.println("=== FILTRO POR PRIORIDADE ===");

        Prioridade prioridade = lerPrioridade();

        exibirColetas(
                service.filtrarPorPrioridade(prioridade),
                "Nenhuma coleta encontrada com a prioridade informada."
        );
    }

    private void atualizarStatus() {
        System.out.println();
        System.out.println("=== ATUALIZAÇÃO DE STATUS ===");

        int id = lerNumeroPositivo("ID da coleta: ");
        StatusColeta novoStatus = lerStatus();

        Coleta coleta = service.atualizarStatus(
                id,
                novoStatus
        );

        System.out.println();
        System.out.println("Status atualizado com sucesso.");
        System.out.println("Coleta: " + coleta.getId());
        System.out.println("Novo status: " + coleta.getStatus());
    }

    private void exibirResumoOperacional() {
        List<Coleta> coletas = service.listarTodas();

        System.out.println();
        System.out.println("=== RESUMO OPERACIONAL ===");
        System.out.println("Total de coletas: " + coletas.size());
        System.out.println(
                "Pendentes: "
                        + service.contarPorStatus(StatusColeta.PENDENTE)
        );
        System.out.println(
                "Em andamento: "
                        + service.contarPorStatus(StatusColeta.EM_ANDAMENTO)
        );
        System.out.println(
                "Concluídas: "
                        + service.contarPorStatus(StatusColeta.CONCLUIDA)
        );
        System.out.println(
                "Total de pacotes: "
                        + service.calcularTotalPacotes()
        );
    }

    private void exibirColetas(
            List<Coleta> coletas,
            String mensagemListaVazia
    ) {
        if (coletas.isEmpty()) {
            System.out.println(mensagemListaVazia);
            return;
        }

        coletas.forEach(System.out::println);

        System.out.println(
                "Total encontrado: " + coletas.size()
        );
    }

    private StatusColeta lerStatus() {
        while (true) {
            System.out.println();
            System.out.println("Status:");
            System.out.println("1. Pendente");
            System.out.println("2. Em andamento");
            System.out.println("3. Concluída");

            int opcao = lerNumeroInteiro(
                    "Escolha o status: "
            );

            switch (opcao) {
                case 1:
                    return StatusColeta.PENDENTE;
                case 2:
                    return StatusColeta.EM_ANDAMENTO;
                case 3:
                    return StatusColeta.CONCLUIDA;
                default:
                    System.out.println("Status inválido.");
            }
        }
    }

    private Prioridade lerPrioridade() {

        while (true) {

            System.out.println();
            System.out.println(
                    "Prioridade:"
            );
            System.out.println(
                    "1. Baixa"
            );
            System.out.println(
                    "2. Média"
            );
            System.out.println(
                    "3. Alta"
            );

            int opcao =
                    lerNumeroInteiro(
                            "Escolha a prioridade: "
                    );

            switch (opcao) {

                case 1:
                    return Prioridade.BAIXA;

                case 2:
                    return Prioridade.MEDIA;

                case 3:
                    return Prioridade.ALTA;

                default:
                    System.out.println(
                            "Prioridade inválida."
                    );
            }
        }
    }
}