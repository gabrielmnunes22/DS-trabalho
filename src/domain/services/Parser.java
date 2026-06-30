package domain.services;

import domain.actions.AcaoMusical;
import domain.model.ConfiguracaoMusical;
import domain.model.GlobalContext;
import domain.model.VoiceState;

public class Parser {

    private final Mapper mapper;

    public Parser() {
        this.mapper = new Mapper();
    }

    public String parsearTexto(String textoBruto, ConfiguracaoMusical config) {
        GlobalContext contextoGlobal = new GlobalContext();
        contextoGlobal.setBpm(config.getBpm());

        StringBuilder stringJFugue = new StringBuilder();
        stringJFugue.append("T").append(contextoGlobal.getBpm()).append(" ");

        String[] linhas = textoBruto.split("\\r?\\n");

        for (int i = 0; i < linhas.length; i++) {
            String linhaAtual = linhas[i].trim();

            if (linhaAtual.isEmpty()) continue;

            VoiceState estadoVoz = new VoiceState(i, config);

            stringJFugue.append("V").append(i).append(" ");
            stringJFugue.append("I").append(estadoVoz.getInstrumentoAtual()).append(" ");

            linhaAtual = processarAtrasoInicial(linhaAtual, stringJFugue);

            char[] letras = linhaAtual.toCharArray();

            for (int j = 0; j < letras.length; j++) {
                char letra = letras[j];
                char proxima = (j + 1 < letras.length) ? letras[j + 1] : '\0';

                AcaoMusical acao = mapper.mapearCaractere(letra, proxima, estadoVoz, contextoGlobal);
                stringJFugue.append(acao.executar(estadoVoz, contextoGlobal));

                // se foi Mi bemol (Mb), consome o 'b' que já foi usado pela RegraMiBemol
                if (letra == 'M' && proxima == 'b') {
                    j++;
                }
            }

            stringJFugue.append(" ");
        }

        return stringJFugue.toString().trim();
    }

    private String processarAtrasoInicial(String linha, StringBuilder stringJFugue) {
        if (linha.startsWith("[")) {
            int indexFimColchete = linha.indexOf(']');
            if (indexFimColchete != -1) {
                String numeroDelayStr = linha.substring(1, indexFimColchete);
                try {
                    int delay = Integer.parseInt(numeroDelayStr);
                    for (int j = 0; j < delay; j++) {
                        stringJFugue.append("R ");
                    }
                    return linha.substring(indexFimColchete + 1).trim();
                } catch (NumberFormatException e) {
                }
            }
        }
        return linha;
    }

    private boolean ehVogal(char c) {
        char letra = Character.toUpperCase(c);
        return letra == 'O' || letra == 'I' || letra == 'U';
    }

    private boolean ehDigitoPar(char c) {
        if (Character.isDigit(c)) {
            int digito = c - '0';
            return digito % 2 == 0;
        }
        return false;
    }
}
