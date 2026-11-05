# Serviço de Nomes · Central de Senhas com gRPC

**Trabalho 5 — Sistemas Paralelos e Distribuídos** · ULBRA 2026/2 · Profª Madianita Bogo Marioti
**Grupo 5:** Regis, Danilo e Hugo

> O totem acha a Central pelo nome.

Partimos do projeto da Aula 10 (Central de Senhas com gRPC). Antes, cada totem tinha o endereço da Central escrito no código (`.forAddress("localhost", 50051)`). Acrescentamos um **mini Serviço de Nomes em gRPC**: a Central se registra como `"atendimento"` ao subir e sai do registro ao encerrar, e os totens perguntam pelo nome antes de conectar.

## O que acrescentamos

| Projeto | Arquivo | Situação | O que faz |
|---|---|---|---|
| ServidorAtendimentoRPC | `src/main/proto/atendimento.proto` | alterado | + `service ServicoNomes` (`Registrar`, `Localizar`, `Remover`) e as mensagens `Registro`, `Nome`, `Endereco`, `Vazio` |
| ServidorAtendimentoRPC | `ServicoNomesImpl.java` | novo | o registro: mapa `nome → endereço` em `ConcurrentHashMap`; nome desconhecido termina com `Status.NOT_FOUND` |
| ServidorAtendimentoRPC | `ServidorNomes.java` | novo | `main` do Serviço de Nomes, porta **50052**, em processo próprio |
| ServidorAtendimentoRPC | `ServidorAtendimentoRPC.java` | alterado | a Central registra `"atendimento" → localhost:50051` ao subir e chama `Remover` ao apertar ENTER |
| ServidorAtendimentoRPC | `AtendimentoServiceImpl.java` | igual | continua gerando as senhas |
| ClienteAtendimentoRPC | `src/main/proto/atendimento.proto` | alterado | idêntico ao do servidor |
| ClienteAtendimentoRPC | `ClienteNomes.java` | novo | `localizar(nome)`: pergunta ao Serviço de Nomes (porta 50052) e devolve host e porta |
| ClienteAtendimentoRPC | `TelaTotem.java` | alterado | o construtor localiza `"atendimento"` antes de criar o canal; o rodapé mostra o endereço recebido |
| ClienteAtendimentoRPC | `TesteNomes.java` | novo | roteiro dos testes 2, 3 e 4 do enunciado |

Nenhuma biblioteca nova: o `pom.xml` é o mesmo da aula.

## Arquitetura

```
                 1. Localizar("atendimento")
 Totens 1, 2, 3  ─────────────────────────────▶  ServidorNomes  (porta 50052)
 (TelaTotem)     ◀─────────────────────────────   nome → endereço
                 2. localhost:50051                     ▲
       │                                                │ 0. Registrar ao subir
       │ 3. PegarSenha                                  │    Remover ao encerrar
       ▼                                                │
 ServidorAtendimentoRPC (Central, porta 50051) ─────────┘
```

O Serviço de Nomes roda **separado** da Central. Assim a Central pode sair ou cair sem levar o registro junto, e dá para mostrar o teste 3.

## Como rodar no NetBeans

Pré-requisitos: NetBeans com JDK 25 ou mais novo (o `pom.xml` usa `maven.compiler.release` 25) e internet no primeiro build.

1. Botão direito em cada projeto › **Clean and Build** (o Maven gera as classes do `.proto`).
2. `ServidorNomes.java` › botão direito › **Run File** (Shift+F6). Espere `Serviço de Nomes no ar (porta 50052).`
3. Projeto **ServidorAtendimentoRPC** › **Run** (F6). A Central imprime `Registrada como "atendimento" no Serviço de Nomes.`
4. `TelaTotem.java` › **Run File**, três vezes (um por totem). Peça senhas nos três.
5. `TesteNomes.java` › **Run File**.
6. Clique no Output da Central e aperte **ENTER**: ela sai do registro e desliga.
7. Rode o `TesteNomes` de novo.

> Linhas `WARNING` sobre `sun.misc.Unsafe` no Output vêm do Netty no JDK novo e podem ser ignoradas.

## Testes do enunciado

Resultados reais, rodando o código deste repositório:

| # | Teste (ficha do Trabalho 5) | Como | Resultado |
|---|---|---|---|
| 1 | Totens 1, 2 e 3 localizam "atendimento" e pegam senhas da mesma fila | TelaTotem 3× | Central: `N-001` Biblioteca, `P-002` Central de Relacionamento, `N-003` Financeiro |
| 2 | Localizar "financeiro" → NOT_FOUND | TesteNomes | `Localizar "financeiro" -> NOT_FOUND` |
| 3 | Encerrar o "atendimento" → sai do registro | ENTER na Central, TesteNomes de novo | `Removido: atendimento`, depois `Localizar "atendimento" -> NOT_FOUND` |
| 4 | Conectar pelo nome, via DNS, com `forTarget` | TesteNomes | `forTarget dns:///localhost:50051 -> senha N-004` |
| + | Extra: a Central cai sem avisar (Stop) | Stop na Central, TesteNomes | `Localizar "atendimento" -> localhost:50051`, mas `forTarget ... -> UNAVAILABLE` |

O extra mostra **por que quem cai precisa sair do registro**: o registro continua entregando um endereço morto, e o totem só descobre na hora da chamada.

## O que o gRPC fez por nós, e o que continuou sendo nosso

| O gRPC fez | Continuou sendo nosso |
|---|---|
| Gerou stubs e classes a partir do `.proto` (`ServicoNomesGrpc` e as mensagens) | O registro em si: o mapa nome → endereço |
| Transporte e serialização (HTTP/2 + Protocol Buffers) | Escolher o nome lógico (`"atendimento"`) |
| Prazos (deadline) e códigos de status padronizados (`NOT_FOUND`, `UNAVAILABLE`) | Entrar no registro ao subir e sair ao encerrar |
| Um resolvedor DNS dentro do canal (`forTarget`) | O que fazer quando não acha: mensagem e fechar o totem |
| | Saúde e expiração dos registros (não fizemos) |

## Material da apresentação (`docs/`)

| Arquivo | O que é |
|---|---|
| `Grupo5_Servico_de_Nomes_v2.pptx` | slides da apresentação (19 slides, com notas do apresentador) |
| `guia_servico_de_nomes_netbeans.html` | guia interativo do "exemplo com a turma", no estilo do NetBeans: árvore do projeto, diff contra o projeto da aula (verde = novo, âmbar = alterado), botões de copiar, saídas reais e testes |
| `guia_servico_de_nomes_netbeans_slides.html` | o mesmo guia com os slides embutidos: roteiro único da aula (slides + código) e modo apresentação em tela cheia (F5) |

Os dois HTML são arquivos únicos e funcionam offline: é só abrir no navegador.

## Referências oficiais

| Assunto | Página | Onde está |
|---|---|---|
| Como um nome vira endereço: target no formato URI, DNS como padrão, resolvedores próprios | https://grpc.io/docs/guides/custom-name-resolution/ | seções *Overview* e *Life of a Target String* |
| `NOT_FOUND` e `UNAVAILABLE` | https://grpc.io/docs/guides/status-codes/ | *The full list of status codes* |
| Stubs, `ImplBase` e `StreamObserver` em Java | https://grpc.io/docs/languages/java/basics/ | tutorial básico de Java |
| Mensagens e números de campo do `.proto` | https://protobuf.dev/programming-guides/proto3/ | guia proto3 |
| Livro de apoio | Indrasiri e Kuruppu, *gRPC: Up and Running* (O'Reilly, 2020) | |

## Prompts

O uso de IA está registrado em [`PROMPTS.md`](PROMPTS.md): cada prompt, o que a IA respondeu, o que aceitamos ou mudamos e por quê.
