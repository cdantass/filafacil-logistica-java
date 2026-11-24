CREATE TABLE auditoria_coleta (
                                  id BIGSERIAL PRIMARY KEY,
                                  coleta_id BIGINT NOT NULL,
                                  acao VARCHAR(50) NOT NULL,
                                  data_evento TIMESTAMP NOT NULL,
                                  observacao TEXT
);
