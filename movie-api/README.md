# 🎬 Movie API — Java Puro + Design Patterns

API REST simples de filmes, feita **100% em Java puro** (sem Spring, sem
frameworks web) usando apenas o `com.sun.net.httpserver.HttpServer` que já
vem embutido no JDK.

Projeto criado para praticar Design Patterns (GoF) de forma aplicada,
como desafio do bootcamp Santander/DIO.

## 📐 Padrões de Projeto aplicados

| Padrão | Onde | Por quê |
|---|---|---|
| **Singleton** | `MovieRepository` | Garante uma única instância do "banco em memória" em toda a aplicação. |
| **Builder** | `Movie.Builder` | Constrói objetos `Movie` de forma legível e validada, sem construtores telescópicos. |
| **Factory Method** | `MovieFactory` | Isola a lógica de "como criar um Movie a partir de um JSON". Fácil trocar a origem dos dados no futuro. |
| **Strategy** | `FilterStrategy` + `GenreFilterStrategy`, `YearFilterStrategy`, `RatingSortStrategy` | Cada forma de filtrar/ordenar filmes é intercambiável e pode ser escolhida em tempo de execução via query string. |

## 📁 Estrutura

```
src/main/java/com/example/movieapi/
├── Main.java                     # sobe o HttpServer na porta 8080
├── model/Movie.java              # entidade + Builder
├── factory/MovieFactory.java     # Factory Method
├── repository/MovieRepository.java # Singleton (dados em memória)
├── strategy/
│   ├── FilterStrategy.java       # contrato Strategy
│   ├── GenreFilterStrategy.java
│   ├── YearFilterStrategy.java
│   └── RatingSortStrategy.java
└── http/MovieHandler.java        # roteamento HTTP dos endpoints
```

## ▶️ Como rodar

### Opção 1 — Maven
```bash
mvn package
java -jar target/movie-api.jar
```

### Opção 2 — javac direto (sem Maven)
```bash
find src -name "*.java" > sources.txt
javac -d out @sources.txt
java -cp out com.example.movieapi.Main
```

O servidor sobe em: `http://localhost:8080/movies`

## 📡 Endpoints

| Método | Rota | Descrição |
|---|---|---|
| GET | `/movies` | Lista todos os filmes |
| GET | `/movies?genre=Drama` | Filtra por gênero |
| GET | `/movies?year=1995` | Filtra por ano |
| GET | `/movies?sortByRating=desc` | Ordena por nota (`asc` ou `desc`) |
| GET | `/movies/{id}` | Busca um filme pelo id |
| POST | `/movies` | Cria um filme (body JSON) |
| DELETE | `/movies/{id}` | Remove um filme |

### Exemplo de POST
```bash
curl -X POST http://localhost:8080/movies \
  -d '{"title":"Matrix","genre":"Ficção","year":1999,"rating":8.7}'
```

### Exemplo de resposta
```json
{"id":"f7dd5fe5-...","title":"Matrix","genre":"Ficção","year":1999,"rating":8.7}
```

## 💡 Próximos passos (sugestões de evolução)

- Adicionar **Observer** para notificar quando um filme é criado/removido.
- Adicionar **Facade** para simplificar operações compostas (ex: importar vários filmes de uma vez).
- Trocar o "banco em memória" por persistência real (arquivo ou banco), mantendo o Singleton.
- Escrever testes unitários com JUnit para o `MovieFactory` e as `Strategy`.
