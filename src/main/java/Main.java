import factory.ColetaFactory;
import integration.ViaCepClient;
import observer.ColetaObserver;
import observer.ContadorColetasObserver;
import observer.LogOperacionalObserver;
import repository.ArquivoColetaRepository;
import repository.ColetaRepository;
import service.ColetaService;
import state.AppState;
import strategy.EstrategiaPrioridade;
import strategy.PrioridadeManualStrategy;
import view.Menu;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        String caminhoArquivo =
                System.getenv()
                        .getOrDefault(
                                "COLETAS_FILE_PATH",
                                "data/coletas.txt"
                        );

        ColetaRepository repository =
                new ArquivoColetaRepository(
                        caminhoArquivo
                );

        AppState appState =
                new AppState();

        ViaCepClient viaCepClient =
                new ViaCepClient();

        ColetaFactory coletaFactory =
                new ColetaFactory();

        EstrategiaPrioridade estrategiaPrioridade =
                new PrioridadeManualStrategy();

        List<ColetaObserver> observers =
                List.of(
                        new LogOperacionalObserver(),
                        new ContadorColetasObserver()
                );

        ColetaService service =
                new ColetaService(
                        repository,
                        appState,
                        viaCepClient,
                        coletaFactory,
                        estrategiaPrioridade,
                        observers
                );

        Menu menu =
                new Menu(service);

        menu.iniciar();
    }
}