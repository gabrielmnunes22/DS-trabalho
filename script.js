const API = '';

const txtArea     = document.getElementById('texto');
const statusDiv   = document.getElementById('status');
const btnTocar    = document.getElementById('btnTocar');
const btnParar    = document.getElementById('btnParar');
const fileInput   = document.getElementById('fileInput');
const bpmSlider   = document.getElementById('bpm');
const volSlider   = document.getElementById('volume');
const bpmValor    = document.getElementById('bpmValor');
const volValor    = document.getElementById('volumeValor');
const oitava      = document.getElementById('oitava');
const instrumento = document.getElementById('instrumento');

bpmSlider.addEventListener('input', () => bpmValor.textContent = bpmSlider.value);
volSlider.addEventListener('input', () => volValor.textContent = volSlider.value);

function setStatus(msg, state) {
  statusDiv.textContent = 'Status: ' + msg;
  statusDiv.className = 'status';
  if (state) statusDiv.classList.add(state);
}

document.getElementById('btnCarregar').addEventListener('click', () => {
  fileInput.click();
});

fileInput.addEventListener('change', (e) => {
  const file = e.target.files[0];
  if (!file) return;

  const reader = new FileReader();
  reader.onload = (ev) => {
    txtArea.value = ev.target.result;
    setStatus('Arquivo carregado: ' + file.name, '');
  };
  reader.readAsText(file);
  fileInput.value = '';
});

document.getElementById('btnSalvarTxt').addEventListener('click', () => {
  const texto = txtArea.value;
  if (!texto.trim()) {
    alert('Digite um texto antes de salvar.');
    return;
  }

  const blob = new Blob([texto], { type: 'text/plain' });
  const url = URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = 'musica.txt';
  a.click();
  URL.revokeObjectURL(url);
  setStatus('Arquivo TXT salvo', '');
});

btnTocar.addEventListener('click', async () => {
  const texto = txtArea.value.trim();
  if (!texto) {
    alert('Digite ou carregue um texto antes de gerar a musica.');
    return;
  }

  btnTocar.disabled = true;
  btnParar.disabled = false;
  setStatus('Enviando para o gerador...', '');

  try {
    const res = await fetch(API + '/api/tocar', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
          texto: texto,
          bpm: bpmSlider.value,
          volume: volSlider.value,
          oitava: oitava.value,
          instrumento: instrumento.value
      })
    });

    const data = await res.json();

    if (res.ok) {
      setStatus('Tocando...', 'tocando');
    } else {
      setStatus('Erro: ' + (data.erro || 'desconhecido'), 'erro');
      btnTocar.disabled = false;
      btnParar.disabled = true;
    }
  } catch (err) {
    setStatus('Erro de conexao: ' + err.message, 'erro');
    btnTocar.disabled = false;
    btnParar.disabled = true;
  }
});

btnParar.addEventListener('click', async () => {
  try {
    await fetch(API + '/api/parar', { method: 'POST' });
    setStatus('Parado', '');
  } catch (err) {
    setStatus('Erro ao parar: ' + err.message, 'erro');
  }
  btnTocar.disabled = false;
  btnParar.disabled = true;
});

document.getElementById('btnSalvarMidi').addEventListener('click', async () => {
  const texto = txtArea.value.trim();
  if (!texto) {
    alert('Digite ou carregue um texto antes de salvar o MIDI.');
    return;
  }

  setStatus('Gerando MIDI...', '');

  try {
    const res = await fetch(API + '/api/salvar-midi', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
          texto: texto,
          bpm: bpmSlider.value,
          volume: volSlider.value,
          oitava: oitava.value,
          instrumento: instrumento.value
      })
    });

    if (!res.ok) {
      const data = await res.json();
      setStatus('Erro: ' + (data.erro || 'desconhecido'), 'erro');
      return;
    }

    const blob = await res.blob();
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'musica.mid';
    a.click();
    URL.revokeObjectURL(url);
    setStatus('MIDI salvo com sucesso!', '');

  } catch (err) {
    setStatus('Erro: ' + err.message, 'erro');
  }
});
