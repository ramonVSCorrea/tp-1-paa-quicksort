// Gera texto/resultados_detalhados.tex a partir dos CSVs do experimento atual.
// Uso: node gerar_dados_relatorio.js [caminho/para/arrays_testados]
const fs = require('fs');
const path = require('path');
const root = __dirname;
const source = process.argv[2] || path.resolve(root, '..', 'tp-1-paa-quicksort', 'arrays_testados');
const graphicsDir = path.join(root, 'graficos');
fs.mkdirSync(graphicsDir, { recursive: true });
function csv(file) {
  const [head, ...records] = fs.readFileSync(path.join(source, file), 'utf8').replace(/^\uFEFF/, '').trim().split(/\r?\n/);
  const columns = head.split(';');
  return records.map(record => Object.fromEntries(record.split(';').map((v, i) => [columns[i], v])));
}
const rows = csv('resultados.csv');
const calibration = csv('calibracao_m.csv');
if (rows.length !== 111 || rows.some(r => r.sucesso !== 'true') ||
    calibration.filter(r => r.selecionado === 'true').map(r => r.M).join() !== '15') {
  throw Error('Os CSVs não correspondem aos resultados verificados com M=15.');
}
const versions = ['Quicksort Recursivo', 'Quicksort Hibrido (M=15)',
  'Quicksort Hibrido Melhorado (M=15, Mediana-de-3)'];
const scenarios = [
  ['Aleatorio', 'aleatorio', 'Dados aleatórios', 'aleatórios'],
  ['Ordenado', 'ordenado', 'Entrada crescente', 'crescentes'],
  ['Ordenado Inverso', 'inverso', 'Entrada decrescente', 'decrescentes'],
  ['Muitos Duplicados', 'duplicados', 'Muitos valores repetidos', 'com duplicatas'],
  ['Pior Caso', 'pior', 'Pior caso explícito', 'do pior caso explícito'],
];
const metrics = [
  ['tempo', 'Tempo médio (ms)', 'Tempo (ms)', 'tempo médio', 'tempo_medio_ns'],
  ['comparacoes', 'Comparações médias', 'Comparações', 'comparações médias', 'comparacoes_medias'],
  ['movimentos', 'Trocas/movimentos médios', 'Trocas/mov.', 'trocas/movimentos médios', 'trocas_movimentos_medios'],
];
const esc = '\\';
const format = (v, metric) => metric === 'tempo'
  ? (Number(v) / 1e6).toFixed(5).replace('.', ',')
  : Number(v).toLocaleString('de-DE');
const lines = [
  '% Gerado por gerar_dados_relatorio.js usando arrays_testados/resultados.csv.',
  '% Não misturar com as medições diferentes do PDF usado como referência.',
];
for (const [name, slug, title, adjective] of scenarios) {
  const selected = rows.filter(r => r.tipo === name);
  const sizes = [...new Set(selected.map(r => Number(r.tamanho)))].sort((a, b) => a - b);
  if (selected.length !== 3 * sizes.length) throw Error(`Dados incompletos em ${name}`);
  const data = new Map(selected.map(r => [`${r.tamanho}/${r.algoritmo}`, r]));
  const value = (n, version, field) => {
    const r = data.get(`${n}/${version}`);
    if (!r) throw Error(`Falta resultado: ${name} / ${n} / ${version}`);
    return r[field];
  };
  lines.push(`${esc}section{${title}}`, `${esc}label{sec:${slug}}`,
    `Apresentam-se as ${sizes.length} dimensões registradas para dados ${adjective}. ` +
    'R indica o recursivo, H o híbrido com pivô final e M3 o híbrido com mediana-de-três.');
  for (const [metric, caption, ylabel, measure, field] of metrics) {
    lines.push(`${esc}begin{table}[htbp]`, `${esc}centering${esc}small`,
      `${esc}caption{${caption} — ${title.toLowerCase()} (R, H e M3)}`,
      `${esc}label{tab:${slug}:${metric}}`, `${esc}begin{tabular}{r r r r}`, `${esc}hline`,
      `$n$ & R & H & M3 ${esc}${esc}`, `${esc}hline`);
    for (const n of sizes) {
      lines.push(`${n.toLocaleString('de-DE')} & ${versions.map(v => format(value(n, v, field), metric)).join(' & ')} ${esc}${esc}`);
    }
    lines.push(`${esc}hline`, `${esc}end{tabular}`,
      `${esc}par${esc}smallskip${esc}footnotesize Fonte: arrays${esc}_testados/resultados.csv; ` +
      (metric === 'tempo' ? 'tempos convertidos de ns para ms. ' : 'contagens médias de operações. ') +
      'H e M3 usam $M=15$.', `${esc}end{table}`,
      `${esc}begin{grafico}[H]`, `${esc}centering`,
      `${esc}includegraphics[width=.83${esc}textwidth]{graficos/${slug}_${metric}.pdf}`,
      `${esc}caption{${measure.charAt(0).toUpperCase() + measure.slice(1)} por tamanho — ${title.toLowerCase()}}`,
      `${esc}label{graf:${slug}:${metric}}`,
      `${esc}par${esc}smallskip${esc}footnotesize Fonte: arrays${esc}_testados/resultados.csv. ` +
      (metric === 'movimentos' ? 'Eixo horizontal logarítmico; zero preservado.' : 'Eixos logarítmicos.'),
      `${esc}end{grafico}`);
    const plot = [
      `${esc}documentclass[tikz,border=2pt]{standalone}`,
      `${esc}usepackage[T1]{fontenc}`,
      `${esc}usepackage[utf8]{inputenc}`,
      `${esc}usepackage{lmodern}`,
      `${esc}usepackage{pgfplots}`,
      `${esc}pgfplotsset{compat=1.17}`,
      `${esc}begin{document}`,
      `${esc}begin{tikzpicture}`,
      `${esc}begin{axis}[width=12cm,height=5.5cm,xmode=log,` +
      (metric === 'movimentos' ? '' : 'ymode=log,') +
      `xlabel={Tamanho $n$},ylabel={${ylabel}},grid=major,` +
      `legend pos=north west,legend style={font=${esc}small},tick label style={font=${esc}small}]`];
    for (const [i, version] of versions.entries()) {
      const coords = sizes.map(n => `(${n},${metric === 'tempo' ? (Number(value(n, version, field)) / 1e6).toFixed(7) : value(n, version, field)})`).join(' ');
      plot.push(`${esc}addplot+[mark=${['*', 'square*', 'triangle*'][i]}] coordinates {${coords}};`,
        `${esc}addlegendentry{${['R', 'H', 'M3'][i]}}`);
    }
    plot.push(`${esc}end{axis}`, `${esc}end{tikzpicture}`, `${esc}end{document}`);
    fs.writeFileSync(path.join(graphicsDir, `${slug}_${metric}.tex`), plot.join('\n') + '\n', 'utf8');
  }
  lines.push(`${esc}input{texto/analise_${slug}}`);
}
const output = path.join(root, 'texto', 'resultados_detalhados.tex');
fs.writeFileSync(output, lines.join('\n') + '\n', 'utf8');
console.log(`Gerado ${output}: ${rows.length} resultados, 15 tabelas e 15 fontes de gráficos. Execute node gerar_graficos_pdf.js para atualizar os PDFs.`);
