# 🚚 FilaFácil Logística

Sistema de gerenciamento de coletas desenvolvido em Java para simular operações logísticas, com integração ViaCEP, persistência relacional, cache e auditoria de eventos.

## Tecnologias

- Java 21
- PostgreSQL
- Flyway
- Redis
- MongoDB
- Docker e Docker Compose
- Maven
- JUnit 5
- Mockito

## Funcionalidades

- Cadastro de coletas por CEP
- Consulta automática de endereço via ViaCEP
- Listagem e filtros por status e prioridade
- Atualização de status
- Exclusão de coletas
- Relatórios operacionais
- Cache de relatórios com Redis
- Auditoria de eventos com MongoDB

## Arquitetura

### PostgreSQL
Persistência principal das coletas.

### Redis
Cache de relatórios com expiração automática (TTL).

### MongoDB
Armazenamento flexível de eventos de auditoria.

### Flyway
Controle de versão e execução automática das migrations.

## Padrões de Projeto

- Factory
- Strategy
- Observer
- Repository
- Dependency Injection

## Executando

Subir infraestrutura:

```bash
docker compose up -d
```

Executar aplicação:

```bash
mvn clean package
java -jar target/filafacil-1.0-SNAPSHOT.jar
```

## Estrutura

```text
src
├── config
├── factory
├── integration
├── model
├── observer
├── repository
├── service
├── strategy
└── view
```

## Autor

**Cauã Dantas Barbosa**