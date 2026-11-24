import factory.ColetaFactory;
import integration.ViaCepClient;
import observer.AuditoriaObserver;
import observer.ColetaObserver;
import observer.ContadorColetasObserver;
import observer.LogOperacionalObserver;
import org.flywaydb.core.Flyway;
import repository.AuditoriaRepository;
import repository.ColetaRepository;
import repository.PostgresAuditoriaRepository;
import repository.PostgresColetaRepository;
import service.ColetaService;
import state.AppState;
import strategy.EstrategiaPrioridade;
import strategy.PrioridadeManualStrategy;
import view.Menu;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        Flyway flyway =
                Flyway.configure()
                        .dataSource(
                                "jdbc:postgresql://localhost:5432/filafacil",
                                "postgres",
                                "2604"
                        )
                        .load();

        flyway.migrate();

        ColetaRepository repository =
                new PostgresColetaRepository();

        AuditoriaRepository auditoriaRepository =
                new PostgresAuditoriaRepository();

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
                        new ContadorColetasObserver(),
                        new AuditoriaObserver(
                                auditoriaRepository
                        )
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