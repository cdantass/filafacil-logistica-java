import factory.ColetaFactory;
import integration.ViaCepClient;
import observer.AuditoriaObserver;
import observer.ColetaObserver;
import observer.ContadorColetasObserver;
import observer.LogOperacionalObserver;
import org.flywaydb.core.Flyway;
import repository.*;
import service.CacheService;
import service.ColetaService;
import service.RelatorioService;
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

        MongoAuditoriaRepository auditoriaRepository =
                new MongoAuditoriaRepository();

        RelatorioRepository relatorioRepository =
                new PostgresRelatorioRepository();

        CacheService cacheService =
                new CacheService();

        RelatorioService relatorioService =
                new RelatorioService(
                        relatorioRepository,
                        cacheService
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
                new Menu(
                        service,
                        relatorioService
                );

        menu.iniciar();
    }
}