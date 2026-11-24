<div align="center">

# 🚚 FilaFácil Logística

Aplicação Java de terminal para gerenciamento de coletas, integrada à ViaCEP e executada na AWS EC2 com Docker.

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=docker&logoColor=white)
![AWS](https://img.shields.io/badge/AWS-EC2-FF9900?style=flat-square&logo=amazonwebservices&logoColor=white)

</div>

## Funcionalidades

- Cadastro de coletas por CEP
- Consulta automática de endereço pela ViaCEP
- Listagem e filtros por status e prioridade
- Atualização e exclusão de coletas
- Resumo operacional
- Persistência em arquivo
- Testes com JUnit 5 e Mockito

## Tecnologias

Java 21, Maven, Jackson, ViaCEP, JUnit 5, Mockito, Docker, Docker Hub e AWS EC2.

## Padrões de projeto

- **Factory:** centraliza a criação das coletas.
- **Strategy:** permite trocar a regra de definição de prioridade.
- **Observer:** notifica a criação e a atualização das coletas.
- **Repository:** separa a persistência das regras de negócio.
- **Dependency Injection:** as dependências são criadas no `Main` e injetadas no `ColetaService`.

## Testes e build

```bash
mvn clean test
mvn clean package
```

O JAR executável será gerado em:

```text
target/filafacil-1.0-SNAPSHOT.jar
```

## Docker

Construa e execute a imagem:

```bash
docker build -t filafacil .
```

```bash
docker run --rm -it \
  --name filafacil \
  -v filafacil-data:/app/data \
  filafacil
```

O volume `filafacil-data` preserva as coletas após o encerramento do contêiner.

## Deploy na AWS EC2

A aplicação foi publicada no Docker Hub e executada em uma instância Ubuntu no Amazon EC2.

```text
Aplicação Java → Imagem Docker → Docker Hub → AWS EC2
```

### Configuração

- Sistema operacional: Ubuntu
- Acesso remoto: SSH pela porta 22
- Execução: Docker
- Persistência: volume Docker
- Portas HTTP e HTTPS: não utilizadas

### Executando na EC2

Instale e inicie o Docker:

```bash
sudo apt update
sudo apt install -y docker.io
sudo systemctl enable --now docker
```

Baixe a imagem:

```bash
sudo docker pull cauad/filafacil-logistica:1.0
```

Execute a aplicação:

```bash
sudo docker run --rm -it \
  --name ata:/app/data \
  cauad/filafacil-logistica:1.0
```

## Segurança e custos

- A chave `.pem` não deve ser enviada ao GitHub.
- O acesso SSH deve ser limitado ao IP do responsável.
- A instância EC2 deve ser interrompida quando não estiver em uso.
- A aplicação não expõe portas HTTP porque funciona pelo terminal.

## Banco de Dados

- PostgreSQL
- Flyway
- Docker Compose

## Executando o banco

docker compose up -d

## Migrations

Executadas automaticamente pelo Flyway

## Autor

**Cauã Dantas Barbosa**