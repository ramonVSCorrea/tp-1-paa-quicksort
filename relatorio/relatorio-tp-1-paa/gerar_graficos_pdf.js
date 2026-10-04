// Compila localmente os 15 gráficos gerados por gerar_dados_relatorio.js.
// O Overleaf só recebe os PDFs: não precisa executar este script nem carregar pgfplots.
const fs = require('fs');
const path = require('path');
const { spawnSync } = require('child_process');
const dir = path.join(__dirname, 'graficos');
const plots = fs.readdirSync(dir).filter(file => /^(aleatorio|ordenado|inverso|duplicados|pior)_(tempo|comparacoes|movimentos)\.tex$/.test(file));
if (plots.length !== 15) throw Error(`Esperados 15 gráficos; encontrados ${plots.length}`);
for (const plot of plots) {
  const result = spawnSync('pdflatex', ['-interaction=batchmode', '-halt-on-error', plot], { cwd: dir, encoding: 'utf8' });
  if (result.error || result.status !== 0 || !fs.existsSync(path.join(dir, plot.replace(/\.tex$/, '.pdf')))) {
    throw Error(`Erro ao compilar ${plot}: ${result.error || result.stdout + result.stderr}`);
  }
  console.log(`Gráfico gerado: ${plot.replace(/\.tex$/, '.pdf')}`);
}
