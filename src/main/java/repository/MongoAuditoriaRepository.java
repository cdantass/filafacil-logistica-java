package repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import config.MongoConnectionFactory;
import model.EventoAuditoria;
import org.bson.Document;

public class MongoAuditoriaRepository {

    private final MongoCollection<Document> collection;

    public MongoAuditoriaRepository() {

        MongoDatabase database =
                MongoConnectionFactory.getDatabase();

        this.collection =
                database.getCollection(
                        "eventos_auditoria"
                );
    }

    public void salvar(
            EventoAuditoria evento
    ) {

        Document document =
                new Document()
                        .append(
                                "tipo",
                                evento.getTipo()
                        )
                        .append(
                                "usuario",
                                evento.getUsuario()
                        )
                        .append(
                                "entidade",
                                evento.getEntidade()
                        )
                        .append(
                                "dadosAntigos",
                                evento.getDadosAntigos()
                        )
                        .append(
                                "dadosNovos",
                                evento.getDadosNovos()
                        )
                        .append(
                                "dataEvento",
                                evento.getDataEvento()
                                        .toString()
                        );

        collection.insertOne(document);
    }
}