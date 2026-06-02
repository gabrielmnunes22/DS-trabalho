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

            if (linhaAtual.isEmpty()) {
                continue;
            }

            VoiceState estadoVoz = new VoiceState(i, config);

            stringJFugue.append("V").append(i).append(" ");
            stringJFugue.append("I").append(estadoVoz.getInstrumentoAtual()).append(" ");

            linhaAtual = processarAtrasoInicial(linhaAtual, stringJFugue);

            char[] letrasDaLinha = linhaAtual.toCharArray();

            for (int j = 0; j < letrasDaLinha.length; j++) {

                char letra = letrasDaLinha[j];
                char proximaLetra = (j + 1 < letrasDaLinha.length) ? letrasDaLinha[j + 1] : '\0';

                AcaoMusical acao;

                if(letra == 'M' && proximaLetra == 'b'){
                    j++;
                    acao = mapper.mapearMiBemol();
                }else{
                    acao = mapper.mapearCaractere(letra);
                }

                if (acao != null) {
                    String comandoGerado = acao.executar(estadoVoz, contextoGlobal);
                    stringJFugue.append(comandoGerado);
                } else {
                    if (ehVogal(letra)) {
                        String ultimaNota = estadoVoz.getUltimaNota();
                        stringJFugue.append(ultimaNota);
                        if (!ultimaNota.equals("R")) {
                            stringJFugue.append(estadoVoz.getOitavaAtual())
                                    .append("a")
                                    .append(estadoVoz.getVolumeAtual());
                        }
                        stringJFugue.append(" ");

                    } else if (ehDigitoPar(letra)) {
                        String novoInstrumento = "I" + String.valueOf(estadoVoz.getInstrumentoAtual() + (letra - '0')) + " ";
                        System.out.printf("Novo instrumento: %s\n", novoInstrumento);
                        stringJFugue.append(novoInstrumento);

                    } else {
                        stringJFugue.append("R ");
                    }
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
