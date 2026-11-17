package strategy;

import model.Prioridade;

public interface EstrategiaPrioridade {

    Prioridade definirPrioridade(
            int quantidadePacotes,
            Prioridade prioridadeInformada
    );
}