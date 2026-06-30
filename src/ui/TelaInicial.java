package ui;

import infrastructure.ServidorWeb;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

public class TelaInicial {

    private final ServidorWeb servidor;
    private final MusicaController controller;
    private final int porta;

    public TelaInicial(int porta, String pastaPublica, MusicaController controller) throws IOException {
        this.porta = porta;
        this.controller = controller;
        this.servidor = new ServidorWeb(porta, pastaPublica, controller);
    }

    public void exibirTela() {
        try {
            servidor.iniciar();

            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(URI.create("http://localhost:" + porta));
            }

            System.out.println("Interface aberta em: http://localhost:" + porta);

        } catch (IOException e) {
            System.out.println("Erro ao iniciar a interface: " + e.getMessage());
        }
    }

    public String obterTextoDigitado() {
        return controller.getUltimoTextoRecebido();
    }
}
