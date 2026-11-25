package repository;

import config.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class PostgresRelatorioRepository
        implements RelatorioRepository {

    @Override
    public Map<String, Integer> totalPorStatus() {

        String sql = """
            SELECT status,
                   COUNT(*) quantidade
            FROM coleta
            GROUP BY status
            ORDER BY status
            """;

        return executarConsulta(sql);
    }

    @Override
    public Map<String, Integer> totalPorPrioridade() {

        String sql = """
            SELECT prioridade,
                   COUNT(*) quantidade
            FROM coleta
            GROUP BY prioridade
            ORDER BY prioridade
            """;

        return executarConsulta(sql);
    }

    private Map<String, Integer> executarConsulta(
            String sql
    ) {

        Map<String, Integer> resultado =
                new LinkedHashMap<>();

        try (
                Connection conn =
                        ConnectionFactory.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(sql);

                ResultSet rs =
                        stmt.executeQuery()
        ) {

            while (rs.next()) {

                resultado.put(
                        rs.getString(1),
                        rs.getInt(2)
                );
            }

            return resultado;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao gerar relatório",
                    e
            );
        }
    }
}