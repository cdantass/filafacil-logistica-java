package repository;

import java.util.Map;

public interface RelatorioRepository {

    Map<String, Integer> totalPorStatus();

    Map<String, Integer> totalPorPrioridade();

}