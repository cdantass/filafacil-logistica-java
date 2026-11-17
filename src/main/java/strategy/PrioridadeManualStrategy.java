package strategy;

import model.Prioridade;

public class PrioridadeManualStrategy
        implements EstrategiaPrioridade {

    @Override
    public Prioridade definirPrioridade(
            int quantidadePacotes,
            Prioridade prioridadeInformada
    ) {
        if (prioridadeInformada == null) {
            throw new IllegalArgumentException(
                    "A prioridade é obrigatória."
            );
        }

        return prioridadeInformada;
    }
}