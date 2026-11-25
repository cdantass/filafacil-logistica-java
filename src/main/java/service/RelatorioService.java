package service;

import repository.RelatorioRepository;

import java.util.Map;

public class RelatorioService {

    private final RelatorioRepository repository;
    private final CacheService cacheService;

    public RelatorioService(
            RelatorioRepository repository,
            CacheService cacheService
    ) {
        this.repository = repository;
        this.cacheService = cacheService;
    }

    public void exibirRelatorioStatus() {

        System.out.println(
                "\n===== RELATÓRIO POR STATUS ====="
        );

        String cache =
                cacheService.buscar(
                        "relatorio-status"
                );

        if (cache != null) {

            System.out.println(
                    "(dados vindos do Redis)"
            );

            System.out.println(cache);
            return;
        }

        Map<String, Integer> dados =
                repository.totalPorStatus();

        StringBuilder resultado =
                new StringBuilder();

        dados.forEach(
                (status, quantidade) -> {

                    String linha =
                            String.format(
                                    "%s: %d%n",
                                    status,
                                    quantidade
                            );

                    System.out.print(linha);

                    resultado.append(linha);
                }
        );

        cacheService.salvar(
                "relatorio-status",
                resultado.toString()
        );
    }

    public void exibirRelatorioPrioridade() {

        System.out.println(
                "\n===== RELATÓRIO POR PRIORIDADE ====="
        );

        String cache =
                cacheService.buscar(
                        "relatorio-prioridade"
                );

        if (cache != null) {

            System.out.println(
                    "(dados vindos do Redis)"
            );

            System.out.println(cache);
            return;
        }

        Map<String, Integer> dados =
                repository.totalPorPrioridade();

        StringBuilder resultado =
                new StringBuilder();

        dados.forEach(
                (prioridade, quantidade) -> {

                    String linha =
                            String.format(
                                    "%s: %d%n",
                                    prioridade,
                                    quantidade
                            );

                    System.out.print(linha);

                    resultado.append(linha);
                }
        );

        cacheService.salvar(
                "relatorio-prioridade",
                resultado.toString()
        );
    }

    public void limparCache() {

        cacheService.remover(
                "relatorio-status"
        );

        cacheService.remover(
                "relatorio-prioridade"
        );
    }
}