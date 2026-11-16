package view;

import java.util.Scanner;

public class Menu {

    private final Scanner scanner;


    public Menu(){
        this.scanner = new Scanner(System.in);

    }

    public void iniciar(){
        int opcao = -1;

        while (opcao != 0){
            exibirOpcoes();

            opcao = lerOpcao();

            executarOpcao(opcao);
        }

        scanner.close();

        System.out.println("Sistema encerrado");
    }

    private void exibirOpcoes(){
        System.out.println();
        System.out.println(
                "================================"
        );
        System.out.println(
                "       FILAFÁCIL LOGÍSTICA"
        );
        System.out.println(
                "================================"
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
        System.out.println(
                "================================"
        );
        System.out.print(
                "Escolha uma opção: "
        );
    }

    private int lerOpcao(){
        String entrada = scanner.nextLine();

        try{
            return Integer.parseInt(entrada);
        }catch(NumberFormatException exception){
            return -1;
        }
    }

    public void executarOpcao(int opcao){
        switch (opcao){
            case 1 ->
                    funcionalidadeEmConstrucao(
                            "Cadastrar coleta"
                    );

            case 2 ->
                    funcionalidadeEmConstrucao(
                            "Listar coletas"
                    );

            case 3 ->
                    funcionalidadeEmConstrucao(
                            "Filtrar por status"
                    );

            case 4 ->
                    funcionalidadeEmConstrucao(
                            "Filtrar por prioridade"
                    );

            case 5 ->
                    funcionalidadeEmConstrucao(
                            "Atualizar status"
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

    private void funcionalidadeEmConstrucao(String funcionalidade){
        System.out.println(funcionalidade + " ainda será implementada");
    }
}