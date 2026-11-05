# PROMPTS.md · uso de IA no trabalho

Ferramenta: **Claude** (Anthropic), no app Claude Code, numa conversa de 29 e 30/10/2026.
Para cada prompt: o que pedimos, o que a IA respondeu e o que aceitamos ou mudamos, e por quê.
As explicações técnicas foram conferidas nas páginas oficiais listadas no [README](README.md#referências-oficiais).

## Resumo das decisões

- **Código:** ficamos com a versão alinhada à ficha do Trabalho 5 (registro em processo próprio, `Registrar`/`Localizar`/`Remover`, `NOT_FOUND`, saída com ENTER e teste com `forTarget`). A versão do guia anterior do grupo funcionava, mas só cobria o teste 1 dos 4.
- **Guia:** viramos o guia em HTML numa "cópia" do NetBeans, com diff contra o projeto da aula.
- **Slides:** a IA refez o deck a partir da revisão que pedimos, e o grupo acrescentou o slide do QR Code. Depois embutimos os slides no guia, com roteiro único e modo apresentação.

---

## 29/10

### 1. Guia no estilo NetBeans e revisão dos slides

**Prompt (Hugo):**
> "Cara, tá vendo esse html [...] eu estava pensando em algo bacana, em vez de deixar ele feioso assim, transformar ele basicamente na interface do netbeans, onde na esquerda vai ter a arvore de arquivos do projeto e na direita vai ter o código em si. A parte de diff do projeto vai ser como se fosse no git, arquivos novos ficam verdes, linhas novas ficam verdes também [...] linhas alteradas pode ser que tenha, então ela ficaria em uma cor diferente, talvez um âmbar". Pedimos também uma revisão do pptx, com o original guardado como backup.

**O que a IA respondeu:** leu o projeto da aula, os PDFs da professora e os slides. Antes de mexer no visual, apontou que o código do guia anterior **não cobria os testes da ficha do Trabalho 5**: o nome era `AtendimentoService` (e não `"atendimento"`), nome desconhecido não dava `NOT_FOUND`, não existia sair do registro ao encerrar e não havia teste com `forTarget`. Apontou também que o cronograma dá **50 min** ao nosso trabalho (o slide dizia 40). Escreveu uma versão nova do código, compilou e rodou o roteiro de testes completo.

**O que fizemos:** não aceitamos a troca direto. Primeiro quisemos entender de onde vinham as mudanças (prompt 2).

### 2. "De onde vieram essas mudanças?"

**Prompt (Hugo):**
> "essas edições que você está fazendo é de acordo com o slide que eu mandei ou com os códigos que estão no html? porque nosso colega disse que os códigos estão testados e aprovados"

**O que a IA respondeu:** que as mudanças vinham do PDF da professora, não do slide nem do HTML. Compilou e rodou o código do colega: **funciona** (o totem acha a Central e as senhas saem), mas o teste 2 só dá certo em parte e os testes 3 e 4 não aparecem. Mostrou uma tabela comparando as duas versões teste a teste e perguntou qual usar.

**O que fizemos:** pedimos **as duas versões para comparar** antes de decidir. O guia ganhou um seletor de versão e um modo "lado a lado", e saíram dois pptx, um por versão.
**Por quê:** o colega tinha testado o código dele e queríamos decidir em grupo, olhando as diferenças.

### 3. Ajustes no guia

**Prompt (Hugo):**
> "1. Retirar o botão de copiar bloco em comentários e linhas vazias que foram adicionadas apenas para uma formatação mais bonitinha. 2. Tirar o nome alí 'Roteiro - Colega', deixa apenas 'Roteiro' [...] 3. Tirar menções a Colega ou Enunciado, deixe apenas na combobox da versão"

**O que a IA respondeu:** o "Copiar bloco" passou a ignorar blocos só de comentário ou linha vazia, e o texto copiado deixou de levar linhas vazias nas pontas. Os rótulos viraram "Roteiro" e "Testes", e a aba Testes ficou com a tabela de cada versão.

**O que fizemos:** aceitamos. Mandamos para o grupo comparar as versões.

### 4. Bugs: `pom.xml` e botões sumindo

**Prompt (Hugo):**
> "o pom.xml dos dois projeto estão bugados a visualização [...] quando você abre varios arquivos de uma vez e fica um monte de abas dos arquivos, os botões vão para a direita junto com as abas e não tem como ver eles"

**O que a IA respondeu:** achou as duas causas. No realce de XML, uma substituição casava com o HTML que a anterior tinha inserido; trocou por uma passada só. No layout, a coluna do editor crescia junto com a soma das abas; usou `minmax(0, 1fr)` e `min-width: 0`, e as abas passaram a rolar. Testou com 11 abas abertas.

**O que fizemos:** aceitamos.

### 5. Barra de rolagem

**Prompt (Hugo):**
> "poderia alterar essa barra de scroll para uma menos feia e mais condizente com o design do nosso app?"

**O que a IA respondeu:** barras finas e arredondadas no estilo do FlatLaf (tema do NetBeans), com as cores do tema claro e do escuro. Na barra de abas, ela só aparece com o mouse em cima.

**O que fizemos:** aceitamos.

## 30/10

### 6. Árvore de arquivos sem barra de rolagem

**Prompt (Hugo):**
> "a árvore da esquerda parece não estar no tamanho correto, pois a barra de rolagem não aparece, só aparece quando aumenta o tamanho. acho que seria uma boa solução dar uma aumentada no tamanho 'mínimo' que ele spawna"

**O que a IA respondeu:** mediu e mostrou que a causa não era só o tamanho. A árvore tinha 342 px dentro de uma coluna de 290 px, e a barra ficava escondida embaixo do editor. Corrigiu o transbordo e, como sugerimos, aumentou a largura inicial (de 300 a 360 px, conforme a tela).

**O que fizemos:** aceitamos as duas coisas: a nossa ideia (tamanho inicial maior) e a correção da causa.

### 7. Confirmação no botão de copiar

**Prompt (Hugo):**
> "quando eu copio um bloco ou arquivo, o botão continua o mesmo, só que aparece lá embaixo 'Arquivo copiado' [...] eu queria que tivesse um feedback visual no botão também"

**O que a IA respondeu:** o botão vira "✓ Copiado!" em verde por 1,6 s, ou "✕ Não copiou" em vermelho se falhar. Corrigiu também um caso em que o aviso dizia "copiado" mesmo quando a cópia falhava.

**O que fizemos:** aceitamos.

### 8. Decisão da versão e ideia de slides dentro do guia

**Prompt (Hugo):**
> "Decidimos e vamos utilizar a versão 2 mesmo, sem ser a versão do regis, então acho que já podemos tirar aquele seletor de versões. [...] se teria como fazer um embedding do slide no nosso netbeans [...] pode me dar ideias de como fazer?"

**O que a IA respondeu:** tirou o seletor, o modo lado a lado e os dados da outra versão do guia, que caiu de 203 KB para 138 KB. Desenhou três ideias: **A** (slides como arquivos na árvore), **B** (roteiro único misturando slides e código) e **C** (modo apresentação em tela cheia com "Abrir no NetBeans"). Recomendou B + C.

**O que fizemos:** escolhemos a versão 2, que cobre os 4 testes da ficha.

### 9. Protótipo B + C

**Prompt (Hugo):**
> "Pode ser a opção B + C só para eu ter uma ideia, mas eu quero que você crie ou clone o html para um novo, deixa o que já temos como uma espécie de 'backup', não mexe nele"

**O que a IA respondeu:** criou `guia_servico_de_nomes_netbeans_slides.html`, com os 19 slides exportados do pptx e as notas do apresentador. O roteiro segue os 50 minutos da aula, com os passos do código depois do slide do exemplo com a turma. Tem modo apresentação (F5), "Abrir no NetBeans" nos slides de código e um botão para voltar ao slide. Conferiu que o guia anterior não foi alterado.

**O que fizemos:** aceitamos, mantendo o guia anterior como backup.

### 10. Barra da apresentação aparecendo no teclado

**Prompt (Hugo):**
> "toda vez que passa o slide com a setinha do teclado, ele 'abre' aquela parte de baixo escrito 'abrir no netbeans', 'sair (esc)' [...] eu gostaria que aparecesse só quando eu mexo o mouse"

**O que a IA respondeu:** a barra passou a aparecer só com o movimento do mouse. Ela começa escondida e o teclado (ou o passador de slides) não a mostra mais.

**O que fizemos:** aceitamos.

---

## O que descartamos

- **O código do guia anterior** (registro no mesmo processo da Central, resposta com `encontrado=false`): funcionava, mas não mostrava `NOT_FOUND`, a saída do registro nem o `forTarget`, que a ficha pede.
- **O seletor de versões e o modo lado a lado** do guia: serviram para o grupo comparar. Depois da escolha, saíram.
- **Parar a Central com o botão Stop:** o Stop do NetBeans mata o processo na hora, sem dar chance de sair do registro. Usamos ENTER no Output e deixamos o Stop como demonstração de "a Central caiu sem avisar".
