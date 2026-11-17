package strategy;

import model.Prioridade;

public class PrioridadePorQuantidadeStrategy
        implements EstrategiaPrioridade {

    private static final int LIMITE_PRIORIDADE_BAIXA = 5;
    private static final int LIMITE_PRIORIDADE_MEDIA = 15;

    @Override
    public Prioridade definirPrioridade(
            int quantidadePacotes,
            Prioridade prioridadeInformada
    ) {
        if (quantidadePacotes <= 0) {
            throw new IllegalArgumentException(
                    "A quantidade de pacotes deve ser maior que zero."
            );
        }

        if (quantidadePacotes
                <= LIMITE_PRIORIDADE_BAIXA) {

            return Prioridade.BAIXA;
        }

        if (quantidadePacotes
                <= LIMITE_PRIORIDADE_MEDIA) {

            return Prioridade.MEDIA;
        }

        return Prioridade.ALTA;
    }
}