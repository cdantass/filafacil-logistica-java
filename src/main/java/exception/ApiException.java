package exception;

public class ApiException
        extends RuntimeException {

    public ApiException(String mensagem) {
        super(mensagem);
    }

    public ApiException(
            String mensagem,
            Throwable causa
    ) {
        super(mensagem, causa);
    }
}