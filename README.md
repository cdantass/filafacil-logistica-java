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
- Atualização de status
- Resumo operacional
- Persistência em arquivo
- Testes com JUnit 5 e Mockito

## Tecnologias

Java 21, Maven, Jackson, ViaCEP, JUnit 5, Mockito, Docker, Docker Hub e AWS EC2.

## Executando os testes

```bash
mvn clean test
```

## Gerando o JAR

```bash
mvn clean package
```

O arquivo será gerado em:

```text
target/filafacil-1.0-SNAPSHOT.jar
```

## Executando com Docker

```bash
docker build -t filafacil .
```

```bash
docker run --rm -it \
  --name filafacil \
  -v filafacil-data:/app/data \
  filafacil
```

## Deploy na AWS EC2

A aplicação foi publicada no Docker Hub e executada em uma instância Ubuntu no Amazon EC2.

### Arquitetura

```text
Aplicação Java → Imagem Docker → Docker Hub → AWS EC2
```

### Imagem publicada

```text
cauad/filafacil-logistica:1.0
```

### Configuração da EC2

- Sistema operacional: Ubuntu
- Acesso remoto: SSH pela porta 22
- Execução: Docker
- Persistência: volume Docker
- Portas HTTP/HTTPS: não utilizadas

### Executando na EC2

Após conectar à instância por SSH:

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
  --name filafacil \
  -v filafacil-data:/app/data \
  cauad/filafacil-logistica:1.0
```

O volume `filafacil-data` mantém as coletas salvas mesmo depois que o contêiner é removido.

## Segurança e custos

- A chave `.pem` não deve ser enviada ao GitHubmitado ao IP do responsável.
- A instância EC2 deve permanecer interrompida quando não estiver em uso.
- A aplicação não expõe portas HTTP, pois funciona pelo terminal.

## Autor

**Cauã Dantas Barbosa**