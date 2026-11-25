package repository;

import com.filafacil.model.Coleta;
import config.ConnectionFactory;
import model.Prioridade;
import model.StatusColeta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostgresColetaRepository implements ColetaRepository {

    @Override
    public void adicionar(Coleta coleta) {

        String sql = """
            INSERT INTO coleta (
                cliente,
                endereco,
                quantidade_itens,
                prioridade,
                status,
                data_criacao
            )
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {

            stmt.setString(
                    1,
                    coleta.getNomeCliente()
            );

            stmt.setString(
                    2,
                    coleta.getEndereco()
            );

            stmt.setInt(
                    3,
                    coleta.getQuantidadePacotes()
            );

            stmt.setString(
                    4,
                    coleta.getPrioridade().name()
            );

            stmt.setString(
                    5,
                    coleta.getStatus().name()
            );

            stmt.setTimestamp(
                    6,
                    Timestamp.valueOf(
                            coleta.getDataCriacao()
                    )
            );

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao adicionar coleta",
                    e
            );
        }
    }

    @Override
    public List<Coleta> listarTodas() {

        List<Coleta> coletas =
                new ArrayList<>();

        String sql = """
            SELECT *
            FROM coleta
            ORDER BY id
            """;

        try (
                Connection conn =
                        ConnectionFactory.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(sql);

                ResultSet rs =
                        stmt.executeQuery()
        ) {

            while (rs.next()) {

                Coleta coleta =
                        new Coleta(
                                rs.getInt("id"),
                                rs.getString("cliente"),
                                rs.getString("endereco"),
                                rs.getInt("quantidade_itens"),
                                Prioridade.valueOf(
                                        rs.getString(
                                                "prioridade"
                                        )
                                ),
                                StatusColeta.valueOf(
                                        rs.getString(
                                                "status"
                                        )
                                ),
                                rs.getTimestamp(
                                                "data_criacao"
                                        )
                                        .toLocalDateTime()
                        );

                coletas.add(coleta);
            }

            return coletas;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao listar coletas",
                    e
            );
        }
    }

    @Override
    public Optional<Coleta> buscarPorId(int id) {

        String sql = """
            SELECT *
            FROM coleta
            WHERE id = ?
            """;

        try (
                Connection conn =
                        ConnectionFactory.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, id);

            try (
                    ResultSet rs =
                            stmt.executeQuery()
            ) {

                if (!rs.next()) {
                    return Optional.empty();
                }

                return Optional.of(
                        new Coleta(
                                rs.getInt("id"),
                                rs.getString("cliente"),
                                rs.getString("endereco"),
                                rs.getInt("quantidade_itens"),
                                Prioridade.valueOf(
                                        rs.getString(
                                                "prioridade"
                                        )
                                ),
                                StatusColeta.valueOf(
                                        rs.getString(
                                                "status"
                                        )
                                ),
                                rs.getTimestamp(
                                                "data_criacao"
                                        )
                                        .toLocalDateTime()
                        )
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao buscar coleta",
                    e
            );
        }
    }

    @Override
    public int gerarProximoId() {

        String sql = """
            SELECT COALESCE(MAX(id), 0) + 1
            FROM coleta
            """;

        try (
                Connection conn =
                        ConnectionFactory.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(sql);

                ResultSet rs =
                        stmt.executeQuery()
        ) {

            if (rs.next()) {
                return rs.getInt(1);
            }

            return 1;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao gerar próximo ID",
                    e
            );
        }
    }

    @Override
    public Coleta atualizarStatus(
            int id,
            StatusColeta novoStatus
    ) {

        String updateSql = """
            UPDATE coleta
            SET status = ?
            WHERE id = ?
            """;

        try (
                Connection conn =
                        ConnectionFactory.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(updateSql)
        ) {

            stmt.setString(
                    1,
                    novoStatus.name()
            );

            stmt.setInt(
                    2,
                    id
            );

            int linhas =
                    stmt.executeUpdate();

            if (linhas == 0) {

                throw new IllegalArgumentException(
                        "Coleta não encontrada."
                );
            }

            return buscarPorId(id)
                    .orElseThrow();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao atualizar status",
                    e
            );
        }
    }

    @Override
    public void deletarPorId(int id) {

        String sql = """
            DELETE FROM coleta
            WHERE id = ?
            """;

        try (
                Connection conn =
                        ConnectionFactory.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {

            stmt.setInt(
                    1,
                    id
            );

            int linhas =
                    stmt.executeUpdate();

            if (linhas == 0) {

                throw new IllegalArgumentException(
                        "Coleta não encontrada."
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao deletar coleta",
                    e
            );
        }
    }
    @Override
    public List<Coleta> listarPaginado(
            int pagina,
            int tamanhoPagina
    ) {

        int offset =
                (pagina - 1) * tamanhoPagina;

        String sql = """
        SELECT *
        FROM coleta
        ORDER BY id
        LIMIT ?
        OFFSET ?
        """;

        List<Coleta> coletas =
                new ArrayList<>();

        try (
                Connection conn =
                        ConnectionFactory.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, tamanhoPagina);
            stmt.setInt(2, offset);

            try (
                    ResultSet rs =
                            stmt.executeQuery()
            ) {

                while (rs.next()) {

                    coletas.add(
                            new Coleta(
                                    rs.getInt("id"),
                                    rs.getString("cliente"),
                                    rs.getString("endereco"),
                                    rs.getInt("quantidade_itens"),
                                    Prioridade.valueOf(
                                            rs.getString("prioridade")
                                    ),
                                    StatusColeta.valueOf(
                                            rs.getString("status")
                                    ),
                                    rs.getTimestamp("data_criacao")
                                            .toLocalDateTime()
                            )
                    );
                }
            }

            return coletas;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public List<Coleta> listarPorStatus(
            StatusColeta status
    ) {

        String sql = """
        SELECT *
        FROM coleta
        WHERE status = ?
        ORDER BY id
        """;

        List<Coleta> coletas =
                new ArrayList<>();

        try (
                Connection conn =
                        ConnectionFactory.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {

            stmt.setString(
                    1,
                    status.name()
            );

            try (
                    ResultSet rs =
                            stmt.executeQuery()
            ) {

                while (rs.next()) {

                    coletas.add(
                            new Coleta(
                                    rs.getInt("id"),
                                    rs.getString("cliente"),
                                    rs.getString("endereco"),
                                    rs.getInt("quantidade_itens"),
                                    Prioridade.valueOf(
                                            rs.getString("prioridade")
                                    ),
                                    StatusColeta.valueOf(
                                            rs.getString("status")
                                    ),
                                    rs.getTimestamp("data_criacao")
                                            .toLocalDateTime()
                            )
                    );
                }
            }

            return coletas;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}