package view;

import com.filafacil.model.Coleta;
import model.Prioridade;
import service.ColetaService;

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
        System.out.println(
                "0. Sair"
        );
    }

    private void executarOpcao(
            int opcao
    ) {

        switch (opcao) {

            case 1 -> cadastrarColeta();

            case 2 -> listarColetas();

            case 3 ->
                    funcionalidadeEmConstrucao(
                            "Filtro por status"
                    );

            case 4 ->
                    funcionalidadeEmConstrucao(
                            "Filtro por prioridade"
                    );

            case 5 ->
                    funcionalidadeEmConstrucao(
                            "Atualização de status"
                    );

            case 6 ->
                    funcionalidadeEmConstrucao(
                            "Resumo operacional"
                    );

            case 0 ->
                    System.out.println(
                            "Encerrando..."
                    );

            default ->
                    System.out.println(
                            "Opção inválida. Tente novamente."
                    );
        }
    }

    private void cadastrarColeta() {

        System.out.println();
        System.out.println(
                "=== CADASTRO DE COLETA ==="
        );

        String nomeCliente =
                lerTextoObrigatorio(
                        "Nome do cliente: "
                );

        String endereco =
                lerTextoObrigatorio(
                        "Endereço da coleta: "
                );

        int quantidadePacotes =
                lerNumeroPositivo(
                        "Quantidade de pacotes: "
                );

        Prioridade prioridade =
                lerPrioridade();

        try {
            Coleta coleta =
                    service.cadastrar(
                            nomeCliente,
                            endereco,
                            quantidadePacotes,
                            prioridade
                    );

            System.out.println();
            System.out.println(
                    "Coleta cadastrada com sucesso!"
            );

            System.out.println(
                    "Identificador: "
                            + coleta.getId()
            );

            System.out.println(
                    "Status inicial: "
                            + coleta.getStatus()
            );

        } catch (IllegalArgumentException exception) {

            System.out.println(
                    "Erro: "
                            + exception.getMessage()
            );
        }
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

    private void funcionalidadeEmConstrucao(
            String funcionalidade
    ) {
        System.out.println(
                funcionalidade
                        + " será implementada na próxima fase."
        );
    }
}