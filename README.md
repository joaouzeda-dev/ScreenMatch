# 🎬 ScreenMatch

Aplicação backend desenvolvida em **Java com Spring Boot** para busca, armazenamento e análise de informações sobre séries e episódios.

O projeto consome dados da **OMDb API**, converte as respostas JSON em objetos Java e utiliza **Spring Data JPA com PostgreSQL** para persistência dos dados.

O ScreenMatch foi desenvolvido como projeto de estudos com foco na aplicação prática de conceitos de **Programação Orientada a Objetos, APIs RESTful, persistência de dados, consultas ao banco de dados, Streams e Lambdas**.

---

## 🚀 Tecnologias utilizadas

* Java
* Spring Boot
* Spring Data JPA
* Hibernate
* PostgreSQL
* Maven
* Java HttpClient
* OMDb API
* Jackson Databind
* Java Records
* Stream API
* Lambda Expressions
* JPQL
* Git
* GitHub
* Visual Studio Code

---

## 📌 Funcionalidades

A aplicação possui funcionalidades para:

* Buscar séries através da OMDb API;
* Buscar episódios das temporadas de uma série;
* Converter dados JSON em objetos Java;
* Armazenar séries no PostgreSQL;
* Armazenar e relacionar episódios às respectivas séries;
* Listar séries cadastradas no banco de dados;
* Realizar consultas utilizando Spring Data JPA;
* Utilizar consultas personalizadas com JPQL;
* Filtrar e ordenar informações;
* Trabalhar com avaliações de séries e episódios;
* Manipular coleções utilizando Stream API;
* Utilizar Lambda Expressions para processamento dos dados.

---

## 🗂️ Estrutura do projeto

```text
ScreenMatch/
├── .mvn/
│   └── wrapper/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── screenmatch/
│   │   │       ├── model/
│   │   │       ├── principal/
│   │   │       ├── repository/
│   │   │       ├── service/
│   │   │       └── ScreenmatchApplication.java
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│       └── java/
│           └── screenmatch/
│
├── .gitattributes
├── .gitignore
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

---

## 🧩 Organização das classes

### `model`

Contém as classes e Records responsáveis pela representação dos dados utilizados pela aplicação, como séries, episódios, temporadas e categorias.

Entre elas:

* `Serie`
* `Episodio`
* `Categoria`
* `DadosSerie`
* `DadosEpisodio`
* `DadosTemporada`

---

### `service`

Responsável pelos serviços utilizados pela aplicação.

Inclui funcionalidades como:

* consumo da OMDb API;
* requisições HTTP;
* conversão de JSON para objetos Java;
* processamento dos dados recebidos.

---

### `repository`

Contém a interface responsável pelo acesso ao banco de dados utilizando **Spring Data JPA**.

Exemplo:

```java
public interface SerieRepository extends JpaRepository<Serie, Long> {
}
```

O Spring Data JPA fornece automaticamente diversas operações de persistência, como:

```text
save()
findAll()
findById()
deleteById()
count()
```

Além disso, o projeto utiliza consultas derivadas de métodos e consultas personalizadas.

---

### `principal`

Contém o fluxo principal da aplicação e os menus utilizados pelo usuário através do console.

A aplicação permite realizar operações como:

```text
Buscar séries
Buscar episódios
Listar séries buscadas
```

---

## 🌐 Consumo da OMDb API

O ScreenMatch utiliza a **OMDb API** para obter informações sobre séries e seus episódios.

As requisições HTTP são realizadas utilizando o `HttpClient` nativo do Java.

Exemplo simplificado:

```java
HttpClient client = HttpClient.newHttpClient();

HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(endereco))
        .build();

HttpResponse<String> response =
        client.send(request, HttpResponse.BodyHandlers.ofString());
```

Os dados recebidos em JSON são posteriormente convertidos para objetos Java.

---

## 🔄 Conversão de JSON

A conversão dos dados JSON é realizada utilizando **Jackson Databind**.

O projeto também utiliza **Java Records** para representar os dados recebidos pela API.

Exemplo:

```java
@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosSerie(
        @JsonAlias("Title") String titulo,
        @JsonAlias("totalSeasons") Integer totalTemporadas,
        @JsonAlias("imdbRating") String avaliacao
) {
}
```

---

## 🗄️ Banco de dados

O projeto utiliza:

```text
PostgreSQL
```

com:

```text
Spring Data JPA
Hibernate
```

A aplicação trabalha com entidades relacionadas, principalmente:

```text
Serie
   │
   └── Episodios
```

Uma série pode possuir vários episódios, formando um relacionamento **um-para-muitos**.

No PostgreSQL, os dados são armazenados principalmente nas tabelas:

```text
series
episodios
```

---

## 🔗 Relacionamento entre entidades

O relacionamento entre séries e episódios é realizado utilizando anotações JPA como:

```java
@OneToMany
@ManyToOne
```

Isso permite associar cada episódio à sua respectiva série dentro do banco de dados.

---

## 🔎 Consultas com Spring Data JPA

O projeto utiliza os recursos do Spring Data JPA para criar consultas através dos nomes dos métodos.

Exemplos de possibilidades:

```java
findByTituloContainingIgnoreCase(...)
findByCategoria(...)
findTop5ByOrderByAvaliacaoDesc()
```

O Spring interpreta o nome do método e gera automaticamente a consulta necessária.

---

## 🔍 JPQL

Para consultas mais específicas, o projeto também utiliza **JPQL — Java Persistence Query Language**.

Diferentemente do SQL tradicional, o JPQL trabalha com **classes e atributos Java**, em vez de trabalhar diretamente com nomes de tabelas e colunas.

Exemplo:

```java
@Query("""
       SELECT s
       FROM Serie s
       WHERE s.avaliacao >= :nota
       """)
List<Serie> buscarPorAvaliacao(Double nota);
```

---

## ⚙️ Configuração do PostgreSQL

Por segurança, os dados locais de acesso ao PostgreSQL **não são armazenados no GitHub**.

O arquivo:

```text
application-local.properties
```

é ignorado pelo Git através do `.gitignore`.

Para executar o projeto localmente, crie esse arquivo na **raiz do projeto**:

```text
ScreenMatch/
├── application-local.properties
├── pom.xml
├── src/
└── ...
```

Adicione:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/seu_banco
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

O arquivo:

```text
src/main/resources/application.properties
```

possui:

```properties
spring.config.import=optional:file:./application-local.properties

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

> ⚠️ Nunca publique usuário, senha ou outras credenciais pessoais em um repositório público.

---

## 🧠 Conceitos aplicados

Durante o desenvolvimento do projeto foram aplicados conceitos como:

* Programação Orientada a Objetos;
* Encapsulamento;
* Abstração;
* Classes e objetos;
* Interfaces;
* Generics;
* Enums;
* Java Records;
* Java Collections Framework;
* List e ArrayList;
* Stream API;
* Lambda Expressions;
* Method References;
* Optional;
* Tratamento de exceções;
* Consumo de APIs RESTful;
* Protocolo HTTP;
* Manipulação de JSON;
* Persistência de dados;
* ORM;
* JPA;
* Spring Data JPA;
* Hibernate;
* PostgreSQL;
* Relacionamentos entre entidades;
* Repository Pattern;
* JPQL;
* Injeção de dependências;
* Maven.

---

## ▶️ Como executar o projeto

### Pré-requisitos

Para executar o projeto é necessário ter instalado:

* Java;
* PostgreSQL;
* Git.

O projeto utiliza **Maven Wrapper**, portanto não é obrigatório possuir o Maven instalado separadamente.

---

### 1. Clone o repositório

```bash
git clone https://github.com/joaouzeda-dev/ScreenMatch.git
```

---

### 2. Entre na pasta do projeto

```bash
cd ScreenMatch
```

---

### 3. Configure o PostgreSQL

Crie o arquivo:

```text
application-local.properties
```

na raiz do projeto e configure seus dados locais:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/seu_banco
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

---

### 4. Execute o projeto

No Linux:

```bash
./mvnw spring-boot:run
```

No Windows:

```bash
mvnw.cmd spring-boot:run
```

---

## 💻 Ambiente de desenvolvimento

O projeto foi desenvolvido utilizando:

```text
Visual Studio Code
```

com Java, Spring Boot, Maven e PostgreSQL.

---

## 🎯 Objetivo do projeto

O principal objetivo do ScreenMatch é consolidar conhecimentos de **desenvolvimento backend com Java**, evoluindo de uma aplicação que consome uma API externa para uma aplicação com **persistência de dados, relacionamentos entre entidades e consultas utilizando Spring Data JPA e JPQL**.

O projeto também permite praticar a organização de uma aplicação Java em diferentes responsabilidades, separando:

```text
Model
Service
Repository
Principal
```

---

## 👨‍💻 Autor

**João Vitor Uzeda Medeiros**

Estudante de **Análise e Desenvolvimento de Sistemas (ADS)** com foco em desenvolvimento backend em Java.

* GitHub: https://github.com/joaouzeda-dev
* LinkedIn: https://www.linkedin.com/in/joao-uzeda/
