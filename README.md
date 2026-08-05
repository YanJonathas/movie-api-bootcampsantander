API de Filmes em Java



Esse é meu primeiro projeto de API! Fiz em Java puro, sem usar Spring

nem nenhum framework, só pra entender como as coisas funcionam por

dentro antes de usar ferramentas prontas.



É um desafio do bootcamp Santander/DIO



O que o projeto faz



É uma API bem simples que guarda uma lista de filmes na memória e

deixa eu consultar, adicionar e remover filmes. Usei 4 padrões de

projeto pra praticar:



\- \*\*Singleton\*\* – só existe um "banco de dados" (lista) na aplicação

\- \*\*Builder\*\* – monta o objeto Filme de um jeito mais organizado

\- \*\*Factory Method\*\* – concentra a criação do filme num lugar só

\- \*\*Strategy\*\* – dá pra filtrar/ordenar os filmes de formas diferentes

&#x20; (gênero, ano, nota) sem precisar de vários `if`



Como rodar



```bash

find src -name "\*.java" > sources.txt

javac -d out @sources.txt

java -cp out com.example.movieapi.Main





Depois é só acessar: `http://localhost:8080/movies`



Rotas da API



| Método | Rota | O que faz |

|---|---|---|

| GET | `/movies` | Lista os filmes |

| GET | `/movies?genre=Drama` | Filtra por gênero |

| GET | `/movies?year=1995` | Filtra por ano |

| GET | `/movies?sortByRating=asc` | Ordena por nota |

| GET | `/movies/{id}` | Busca um filme pelo id |

| POST | `/movies` | Cria um filme |

| DELETE | `/movies/{id}` | Apaga um filme |



\### Exemplo de como criar um filme

```bash

curl -X POST http://localhost:8080/movies -d '{"title":"Matrix","genre":"Ficção","year":1999,"rating":8.7}'

