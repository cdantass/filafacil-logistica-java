import repository.ColetaRepository;
import service.ColetaService;
import view.Menu;

public class Main {

    public static void main(
            String[] args
    ) {

        ColetaRepository repository =
                new ColetaRepository(
                        "data/coletas.txt"
                );

        ColetaService service =
                new ColetaService(
                        repository
                );

        Menu menu =
                new Menu(
                        service
                );

        menu.iniciar();
    }
}