package ui;

import domain.services.Parser;
import infrastructure.ReprodutorAudio;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class MusicaController {

    private final Parser parser;
    private final ReprodutorAudio reprodutor;
    private Thread threadMusica;
    private volatile String ultimoTextoRecebido = "";

    public MusicaController(Parser parser, ReprodutorAudio reprodutor) {
        this.parser = parser;
        this.reprodutor = reprodutor;
    }

    public String tocar(String texto) {
        parar();

        ultimoTextoRecebido = texto;
        String partitura = parser.parsearTexto(texto);

        threadMusica = new Thread(() -> {
            try {
                reprodutor.tocarMusica(partitura);
            } catch (Exception e) {
                System.out.println("Erro ao tocar: " + e.getMessage());
            }
        });
        threadMusica.start();

        return partitura;
    }

    public void parar() {
        if (threadMusica != null && threadMusica.isAlive()) {
            threadMusica.interrupt();
        }
    }

    public byte[] gerarMidi(String texto) throws IOException {
        String partitura = parser.parsearTexto(texto);

        File tempFile = File.createTempFile("musica_", ".mid");
        tempFile.deleteOnExit();

        reprodutor.exportarParaMidi(partitura, tempFile.getAbsolutePath());
        byte[] midiBytes = Files.readAllBytes(tempFile.toPath());

        tempFile.delete();
        return midiBytes;
    }

    public String getUltimoTextoRecebido() {
        return ultimoTextoRecebido;
    }
}
