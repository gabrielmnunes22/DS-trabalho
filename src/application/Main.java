package application;

import domain.services.Parser;
import infrastructure.ReprodutorAudio;
import ui.MusicaController;
import ui.TelaInicial;

public class Main {

    public static void main(String[] args) {
        try {
            Parser processador = new Parser();
            ReprodutorAudio reprodutor = new ReprodutorAudio();
            MusicaController controller = new MusicaController(processador, reprodutor);

            String pastaPublica = System.getProperty("user.dir");
            int porta = 8080;

            TelaInicial tela = new TelaInicial(porta, pastaPublica, controller);
            tela.exibirTela();

            System.out.println("Pressione Ctrl+C para encerrar.");

        } catch (Exception e) {
            System.out.println("Erro ao iniciar: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
