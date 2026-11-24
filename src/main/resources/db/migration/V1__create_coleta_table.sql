CREATE TABLE coleta (
                        id BIGSERIAL PRIMARY KEY,
                        cliente VARCHAR(100) NOT NULL,
                        endereco VARCHAR(255) NOT NULL,
                        quantidade_itens INTEGER NOT NULL,
                        prioridade VARCHAR(30) NOT NULL,
                        status VARCHAR(30) NOT NULL,
                        data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);