CREATE TABLE janelas (
    id BIGSERIAL PRIMARY KEY,
    data DATE NOT NULL,
    horario_inicial TIME NOT NULL,
    horario_final TIME NOT NULL,
    capacidade_maxima INTEGER NOT NULL,
    capacidade_ocupada INTEGER NOT NULL,
    status VARCHAR(30) NOT NULL
);