# ⚡ TP 1 — PAA: Estudo Comparativo de Quicksort

> 🎓 Trabalho prático de **Projeto e Análise de Algoritmos (PAA)**: comparação entre Quicksort recursivo, Quicksort híbrido e Quicksort híbrido com mediana-de-três.

**Alunos:** Ian Pereira Pinto Bomfim · João Miguel de Abreu Constâncio · Ramon Vinícius Silva Corrêa

O projeto executa as três versões sobre as **mesmas massas de dados**, mede tempo e operações, seleciona empiricamente o parâmetro `M` e exporta os resultados em arquivos de texto e CSV para análise posterior.

**☕ Tecnologia:** Java 17+ · **🔧 Build:** Maven ou `javac` · **📦 Dependências externas:** nenhuma no código-fonte

**📁 Diretório do projeto:** `tp-1-paa-quicksort`

## 🧭 Sumário

- 🧠 [Algoritmos implementados](#algoritmos-implementados)
- 🚀 [Como executar](#como-executar)
- ✅ [Como executar os testes](#como-executar-os-testes)
- 🧪 [Protocolo experimental](#protocolo-experimental)
- 📂 [Arquivos gerados](#arquivos-gerados)
- 🏗️ [Estrutura do projeto](#estrutura-do-projeto)
- 📊 [Como interpretar os resultados](#como-interpretar-os-resultados)
- 📚 [Escopo da entrega](#escopo-da-entrega)

<a id="algoritmos-implementados"></a>
## 🧠 Algoritmos implementados

| Versão | Escolha do pivô | Tratamento de subvetores pequenos |
| --- | --- | --- |
| **Quicksort recursivo** | Último elemento | Continua a recursão |
| **Quicksort híbrido** | Último elemento | Insertion Sort quando o tamanho é **menor que `M`** |
| **Quicksort híbrido com mediana-de-três** | Mediana entre primeiro, central e último elemento | Insertion Sort quando o tamanho é **menor que `M`** |

O `M` utilizado nas duas versões híbridas é escolhido com testes da versão híbrida **com pivô no último elemento**. Isso permite comparar as duas estratégias de pivô com o mesmo limite de corte.

<details>
<summary><strong>🔎 O que esperar em termos de complexidade?</strong></summary>

- Quicksort tem custo médio esperado de **O(n log n)** e pode atingir **O(n²)** no pior caso.
- Insertion Sort custa **O(n²)** no pior caso, mas é utilizado apenas em subvetores pequenos das versões híbridas.
- Para dados distintos já ordenados, escolher sempre o último elemento como pivô produz partições desbalanceadas. A mediana-de-três melhora esse cenário específico, **mas não elimina todos os piores casos**, especialmente com muitos valores iguais nesta implementação com partição de duas vias.

</details>

<a id="como-executar"></a>
## 🚀 Como executar

Execute os comandos **na raiz do projeto**, onde está o `pom.xml`. É necessário ter o **JDK 17 ou superior**; o Maven é opcional.

No Windows, verifique se o JDK está disponível com `javac -version`. Se não estiver instalado, execute no **CMD**:

```cmd
winget install --id EclipseAdoptium.Temurin.17.JDK -e --source winget
```

Depois da instalação, **feche e abra novamente o CMD** para atualizar o `PATH` e confirme com `javac -version` (deve indicar a versão 17 ou superior).

### 🪟 Sem Maven — Prompt de Comando (CMD, Windows)

Abra o CMD na raiz do projeto e execute:

```cmd
if not exist target\classes mkdir target\classes
javac -encoding UTF-8 -d target\classes -sourcepath src\main\java src\main\java\paa\sort\Main.java
java -cp target\classes paa.sort.Main
```

### 🪟 Sem Maven — PowerShell (Windows)

```powershell
New-Item -ItemType Directory -Force target/classes | Out-Null
javac -encoding UTF-8 -d target/classes -sourcepath src/main/java src/main/java/paa/sort/Main.java
java -cp target/classes paa.sort.Main
```

### 🔧 Com Maven

```bash
mvn compile
mvn exec:java -Dexec.mainClass=paa.sort.Main
```

> 💡 **Dica:** a primeira execução pode baixar plugins do Maven. O projeto não depende de um projeto-pai externo.

O programa imprime o `M` selecionado e **uma tabela por cenário**: cada linha representa um tamanho, as três colunas centrais mostram os tempos médios (ms), e a última indica a versão com menor tempo. Os nomes foram abreviados apenas no terminal: `Recursivo`, `Hibrido` e `Mediana-3`. As comparações e trocas/movimentos continuam disponíveis em `arrays_testados/resultados.csv` e nos arquivos de texto. Os arquivos são salvos em [`arrays_testados/`](#arquivos-gerados), criada automaticamente.

<a id="como-executar-os-testes"></a>
## ✅ Como executar os testes

Após compilar o programa com um dos métodos acima:

```text
javac -encoding UTF-8 -cp target/classes -d target/classes src/test/java/paa/sort/SortRegressionTest.java
java -cp target/classes paa.sort.SortRegressionTest
```

Os comandos de teste também funcionam no **CMD** (as barras `/` nos caminhos são aceitas pelo Java no Windows).

Os testes de regressão **não exigem JUnit**. Eles verificam a ordenação em entradas pequenas e variadas, os cinco tipos de massa, diferentes valores de `M`, a preservação do vetor original, a rejeição de uma implementação incorreta e a quantidade de comparações do pior caso explícito.

<a id="protocolo-experimental"></a>
## 🧪 Protocolo experimental

### 1. 🎯 Calibração de `M`

| Configuração | Valor |
| --- | --- |
| Candidatos | `5, 10, 15, 20, 25, 30, 40, 50, 75, 100` |
| Massa | 1.000 inteiros aleatórios, com seed `42` |
| Aquecimentos | 5 por candidato |
| Medições | 10 por candidato |
| Critério | Menor tempo médio medido |

Todos os candidatos recebem **cópias da mesma massa**. O resultado detalhado fica em `arrays_testados/calibracao_m.csv`. Como se trata de um teste de tempo, o `M` escolhido pode variar entre máquinas ou execuções.

### 2. 🔄 Comparação das versões

Os tamanhos dependem do cenário: todos começam em **100, 500, 1.000, 2.000, 5.000 e 10.000** elementos, mas somente **aleatório** e **muitos duplicados** continuam em **20.000, 50.000 e 100.000**.

| Cenário | Geração | Maior tamanho | O que permite observar |
| --- | --- | ---: | --- |
| Aleatório | Inteiros pseudoaleatórios | **100.000** | Comportamento em dados sem ordem inicial |
| Ordenado | `0, 1, 2, ..., n−1` | **10.000** | Efeito de uma entrada crescente |
| Inverso | `n−1, ..., 2, 1, 0` | **10.000** | Efeito de uma entrada decrescente |
| Muitos duplicados | Valores sorteados de até `n/10` valores distintos | **100.000** | Comportamento com repetições |
| Pior caso | Entrada crescente | **10.000** | Demonstração explícita do pior caso com pivô final |

> 📝 **Nota:** as massas “Ordenado” e “Pior caso” contêm os mesmos valores crescentes; “Pior caso” identifica explicitamente o experimento com pivô final inadequado. Há ainda uma medição adicional de **200 elementos** nesse cenário.

> ⚠️ **Por que não testar 100.000 em todos os cenários?** Nos vetores ordenados e inversos, o Quicksort com pivô no último elemento pode fazer partições muito desbalanceadas, com tempo **O(n²)** e recursão profunda. Em uma entrada crescente de 100.000 elementos, o Quicksort recursivo realizaria `100.000 × 99.999 / 2 = 4.999.950.000` comparações entre valores e poderia esgotar a pilha antes de terminar. Limitar esses cenários a 10.000 mantém a comparação entre **as três implementações** executável e deixa o pior caso visível. A ampliação até 100.000 fica reservada às massas aleatória e com muitos duplicados; isso **não significa** que entradas maiores sejam impossíveis, apenas que o protocolo evita esse risco nas entradas degeneradas.

Para cada combinação de cenário e tamanho, os três algoritmos recebem cópias **idênticas** da massa. São feitos **3 aquecimentos** e **5 medições** por versão. Assim, a execução completa produz **111 resultados**: `3 cenários × 6 tamanhos × 3 versões + 2 cenários × 9 tamanhos × 3 versões + 1 tamanho adicional × 3 versões`.

### 3. ⏱️ O que é medido

`System.nanoTime()` cronometra **somente a chamada de ordenação**, incluindo a cópia interna feita pelas implementações. Geração de dados, validação e escrita de arquivos não entram no tempo. Após cada execução, o vetor retornado é comparado com uma cópia ordenada por `Arrays.sort`.

| Métrica | Convenção usada neste projeto |
| --- | --- |
| **Tempo médio** | Média aritmética das durações das execuções cronometradas |
| **Comparações médias** | Comparações entre valores dos vetores, inclusive na escolha por mediana-de-três; não inclui testes de controle do laço ou da recursão |
| **Trocas/movimentos médios** | Trocas entre posições distintas no Quicksort; deslocamentos e inserção final no Insertion Sort |

> ⚠️ **Atenção:** “trocas/movimentos” é uma métrica combinada; movimentos do Insertion Sort **não são trocas literais**. O pior caso crescente com pivô final realiza `n(n−1)/2` comparações entre elementos no Quicksort recursivo.

<a id="arquivos-gerados"></a>
## 📂 Arquivos gerados

Ao executar o programa, é criada a pasta `arrays_testados/`:

```text
arrays_testados/
├── calibracao_m.csv          M candidatos, tempos médios e M escolhido
├── resultados.csv            Médias de todas as combinações medidas
├── resumo_geral.txt          Resumo legível das execuções
├── aleatorio/
│   ├── arrays_originais/     Vetores antes da ordenação
│   ├── arrays_ordenados/     Um arquivo por algoritmo e tamanho
│   └── resultados/          Resumo do cenário em texto
├── ordenado/
├── ordenado_inverso/
├── muitos_duplicados/
└── pior_caso/                Inclui a análise adicional de 200 elementos
```

As pastas dos cinco cenários seguem a mesma organização interna. Os CSV usam **ponto e vírgula (`;`)** como separador e apresentam o tempo em **nanossegundos**; para obter milissegundos, divida o valor por `1.000.000`. Os arquivos de `arrays_testados/` podem ser versionados como registro da execução; **execute o programa novamente se quiser gerar resultados na sua máquina**, pois os tempos podem variar.

<a id="estrutura-do-projeto"></a>
## 🏗️ Estrutura do projeto

```text
src/
├── main/java/paa/sort/
│   ├── Main.java
│   ├── application/
│   │   └── QuickSortComparativeStudy.java  Orquestra os experimentos
│   ├── domain/
│   │   ├── SortingAlgorithm.java         Contrato de ordenação
│   │   ├── algorithms/                  Três Quicksorts + Insertion Sort
│   │   ├── performance/                 Métricas, medições e calibração de M
│   │   └── testdata/                    Tipos e gerador de dados
│   └── infrastructure/export/
│       └── ArrayExporter.java          Geração dos TXT e CSV
└── test/java/paa/sort/
    └── SortRegressionTest.java         Testes de regressão
```

A separação mantém as implementações de ordenação independentes da exportação: `QuickSortComparativeStudy` coordena o fluxo, `PerformanceTester` centraliza as medições e `ArrayExporter` grava os resultados.

<a id="como-interpretar-os-resultados"></a>
## 📊 Como interpretar os resultados

1. Consulte `calibracao_m.csv` para identificar o `M` selecionado e comparar seus candidatos.
2. Em `resultados.csv`, filtre por **tipo** e **tamanho** para comparar as três versões com a mesma massa.
3. Compare **tempo**, **comparações** e **trocas/movimentos** separadamente: menor contagem de operações não garante menor tempo em toda máquina.
4. Observe as entradas ordenadas e o pior caso explícito para relacionar os resultados ao desbalanceamento das partições; analise duplicatas separadamente.

Os tempos dependem do hardware, da JVM e das condições de execução. As médias facilitam a comparação, mas **não representam, por si só, uma garantia de significância estatística**.
O terminal arredonda os tempos para **três casas decimais em milissegundos**. Se dois valores parecerem empatados na tela, consulte os nanossegundos em `resultados.csv`: é com esses valores não arredondados que o menor tempo é escolhido.

<a id="escopo-da-entrega"></a>
## 📚 Escopo da entrega

Este repositório contém o **código, os testes e os arquivos gerados pelo experimento**. O [relatório no Overleaf](https://www.overleaf.com/read/gtjvwsdwthbf#6c921e) apresenta o referencial teórico, as tabelas, os gráficos, a análise crítica, a conclusão e as referências.
