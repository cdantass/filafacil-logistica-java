import integration.ViaCepClient;
import repository.ArquivoColetaRepository;
import service.ColetaService;
import state.AppState;
import view.Menu;

public class Main {

    public static void main(String[] args) {

        ViaCepClient viaCepClient = new ViaCepClient();

        String caminhoArquivo = System.getenv()
                .getOrDefault(
                        "COLETAS_FILE_PATH",
                        "data/coletas.txt"
                );

        ArquivoColetaRepository repository =
                new ArquivoColetaRepository(caminhoArquivo);

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