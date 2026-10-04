# Compilação no Overleaf

1. Crie um **novo projeto** em **New Project > Upload Project** e envie `Relatorio_Overleaf.zip`. Não envie o ZIP como arquivo dentro do projeto antigo (isso não substitui os arquivos existentes).
2. Em **Menu**, selecione `Projeto.tex` como **Main document** e **pdfLaTeX** como compilador. Recompile; o Overleaf executa o BibTeX automaticamente nas passagens necessárias. Se as citações não aparecerem na primeira tentativa, use **Recompile from scratch** uma vez.
3. O projeto inclui a classe ABNT personalizada, estilo de citação, `Referencias.bib` e `abnt-options.bib` (necessário ao BibTeX ABNT), capítulos, resumo, abstract, 19 tabelas e **15 gráficos PDF vetoriais pré-gerados**. **Não precisa** de Java, Node.js, CSV externo, `pgfplots` nem acesso aos caminhos do Windows para compilar.

As tabelas prontas estão em `texto/resultados_detalhados.tex` e os gráficos em `graficos/*.pdf`. Para atualizar os números após outra execução do experimento Java, rode localmente `node gerar_dados_relatorio.js <pasta/arrays_testados>` e `node gerar_graficos_pdf.js`; depois substitua o `.tex` e os 15 PDFs no projeto online. Esses geradores **não são necessários** para compilar no Overleaf.

O arquivo opcional `imagem/logo_puc.png` não está incluído; se adicionado, será exibido automaticamente na capa. Confira também os nomes dos integrantes antes da entrega.
