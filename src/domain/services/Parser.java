package domain.services;

import domain.actions.AcaoMusical;
import domain.model.GlobalContext;
import domain.model.VoiceState;
//import domain.services.Mapper;

public class Parser {

    private final Mapper mapper;

    public Parser() {
        this.mapper = new Mapper();
    }

    public String parsearTexto(String textoBruto) {
        GlobalContext contextoGlobal = new GlobalContext();
        StringBuilder stringJFugue = new StringBuilder();

        String[] linhas = textoBruto.split("\\r?\\n");

        for (int i = 0; i < linhas.length; i++) {
            String linhaAtual = linhas[i].trim();

            if (linhaAtual.isEmpty()) {
                continue;
            }

            VoiceState estadoVoz = new VoiceState(i);

            stringJFugue.append("V").append(i).append(" ");
            stringJFugue.append("I").append(estadoVoz.getInstrumentoAtual()).append(" ");

            // Resolvemos os colchetes (ex: [4]) no começo da linha
            linhaAtual = processarAtrasoInicial(linhaAtual, stringJFugue);

            // Logica da traducao
            // Transformamos a linha "C D > E" num array de letras e passamos por cada uma
            char[] letrasDaLinha = linhaAtual.toCharArray();

            for (char letra : letrasDaLinha) {

                // Pedimos para o Dicionário fabricar a ação correspondente a essa letra
                AcaoMusical acao = mapper.mapearCaractere(letra);

                if (acao != null) {
                    // Se o Dicionário reconheceu a letra (ex: 'C', ' ', '?', '>'), 
                    // nós apertamos o botão e anotamos o som (ou silêncio) gerado.
                    String comandoGerado = acao.executar(estadoVoz, contextoGlobal);
                    stringJFugue.append(comandoGerado);
                } else {
                    // REGRAS ESPECIAIS (Não mapeadas pelo dicionário)
                    // Se for uma vogal (O, I, U, etc - que não são A e nem E)
                    if (ehVogal(letra)) {
                        // Toca a última nota com a oitava e volume atuais
                        String ultimaNota = estadoVoz.getUltimaNota();
                        // Montamos no formato JFugue: "C5a100 " ou "R " (pausa)
                        stringJFugue.append(ultimaNota);
                        if (!ultimaNota.equals("R")) {
                            stringJFugue.append(estadoVoz.getOitavaAtual())
                                    .append("a")
                                    .append(estadoVoz.getVolumeAtual());
                        }
                        stringJFugue.append(" ");
                    } else {
                        // Se for uma consoante qualquer ou símbolo não mapeado, vira pausa (Rest)
                        stringJFugue.append("R ");
                    }
                }
            }

            // Colocamos um espaço extra no final da linha só para garantir a formatação
            stringJFugue.append(" ");
        }

        return stringJFugue.toString().trim(); // Devolve a música inteira pronta!
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
                    // Ignora se não for número
                }
            }
        }
        return linha;
    }

    // Função auxiliar para descobrir se é vogal (desconsiderando A e E, que já são notas no Mapper)
    private boolean ehVogal(char c) {
        char letra = Character.toUpperCase(c);
        return letra == 'O' || letra == 'I' || letra == 'U';
    }
}