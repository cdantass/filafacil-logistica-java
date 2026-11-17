import integration.ViaCepClient;
import repository.ArquivoColetaRepository;
import service.ColetaService;
import state.AppState;
import view.Menu;

public class Main {

    public static void main(String[] args) {

        ViaCepClient viaCepClient = new ViaCepClient();

        ArquivoColetaRepository repository =
                new ArquivoColetaRepository(
                        "src/main/data/coletas.txt"
                );

        AppState appState =
                new AppState();

        ColetaService service = new ColetaService(
                repository,
                appState,
                viaCepClient
        );

        Menu menu =
                new Menu(service);

        menu.iniciar();
    }
}