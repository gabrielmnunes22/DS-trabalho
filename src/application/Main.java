package application;

import domain.services.Parser;
import infrastructure.ReprodutorAudio;

public class Main {

    public static void main(String[] args) {
        System.out.println("🎹 Iniciando o Gerador de Música TCP...");

        // CRIANDO O MAESTRO E CAIXA DE SOM
        Parser parser = new Parser();
        ReprodutorAudio reprodutor = new ReprodutorAudio();

        // TESTE PARA VER SE ESTA FUNCIONANDO O CODIGO
//        String textoDaTela =
//                "[0] C D E F G A B C H\n" +  // Voz 0 (Cravo) começa na hora
//                        "[2] E F G A B C D E E RF D DF F FDF FD AFDF FDF \n" +  // Voz 1 (Órgão) espera 2 tempos
//                        "[4] C C D D E E F F";     // Voz 2 (Piano) espera 4 tempos

        String textoDaTela =
                "G A H C 2 D E F G";

        System.out.println("Texto recebido:");
        System.out.println(textoDaTela);
        System.out.println("--------------------------------------------------");

        // parser traduz o texto
        String partituraJFugue = parser.parsearTexto(textoDaTela);

        System.out.println("String traduzida para o JFugue:");
        System.out.println(partituraJFugue);
        System.out.println("--------------------------------------------------");

        // infraestrutura toca a música na caixa de som
        reprodutor.tocarMusica(partituraJFugue);

        // Salva o arquivo MIDI na mesma pasta do projeto
        reprodutor.exportarParaMidi(partituraJFugue, "minha_primeira_fuga.mid");
    }
}