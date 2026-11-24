package repository;

import config.ConnectionFactory;
import model.Auditoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class PostgresAuditoriaRepository
        implements AuditoriaRepository {

    @Override
    public void salvar(Auditoria auditoria) {

        String sql = """
            INSERT INTO auditoria_coleta (
                coleta_id,
                acao,
                observacao,
                data_evento
            )
            VALUES (?, ?, ?, ?)
            """;

        try (
                Connection conn =
                        ConnectionFactory.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {

            stmt.setLong(
                    1,
                    auditoria.getColetaId()
            );

            stmt.setString(
                    2,
                    auditoria.getAcao()
            );

            stmt.setString(
                    3,
                    auditoria.getObservacao()
            );

            stmt.setTimestamp(
                    4,
                    Timestamp.valueOf(
                            auditoria.getDataEvento()
                    )
            );

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao salvar auditoria",
                    e
            );
        }
    }
}