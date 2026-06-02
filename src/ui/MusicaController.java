package ui;

import domain.model.ConfiguracaoMusical;
import domain.services.Parser;
import infrastructure.ReprodutorAudio;
import org.jfugue.player.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class MusicaController {

    private final Parser parser;
    private final ReprodutorAudio reprodutor;
    private Thread threadMusica;
    private volatile String ultimoTextoRecebido = "";

    private Player jfuguePlayer;

    public MusicaController(Parser parser, ReprodutorAudio reprodutor) {
        this.parser = parser;
        this.reprodutor = reprodutor;
    }

    public String tocar(String texto, ConfiguracaoMusical config) {
        parar();

        ultimoTextoRecebido = texto;
        String partitura = parser.parsearTexto(texto, config);

        threadMusica = new Thread(() -> {
            try {
                jfuguePlayer = new Player();
                reprodutor.tocarMusica(partitura, jfuguePlayer);
            } catch (Exception e) {
                System.out.println("Erro ao tocar: " + e.getMessage());
            }
        });
        threadMusica.setDaemon(true);
        threadMusica.start();

        return partitura;
    }

    public void parar() {
        if (jfuguePlayer != null) {
            try {
                jfuguePlayer.getManagedPlayer().finish();
            } catch (Exception e) {
                System.out.println("Erro ao parar: " + e.getMessage());
            }
        }
    }

    public byte[] gerarMidi(String texto, ConfiguracaoMusical config) throws IOException {
        String partitura = parser.parsearTexto(texto, config);

        File tempFile = File.createTempFile("musica_", ".mid");
        tempFile.deleteOnExit();

        reprodutor.exportarParaMidi(partitura, tempFile.getAbsolutePath());

        if (!tempFile.exists() || tempFile.length() == 0) {
            throw new IOException("Falha ao gerar o MIDI.");
        }

        byte[] midiBytes = Files.readAllBytes(tempFile.toPath());

        tempFile.delete();
        return midiBytes;
    }

    public String getUltimoTextoRecebido() {
        return ultimoTextoRecebido;
    }
}
