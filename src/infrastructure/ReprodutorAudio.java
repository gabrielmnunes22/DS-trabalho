package infrastructure;

import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;
import org.jfugue.midi.MidiFileManager;

import java.io.File;
import java.io.IOException;

public class ReprodutorAudio {

    public void tocarMusica(String stringJFugue, Player player) {
        Pattern partitura = new Pattern(stringJFugue);

        System.out.println("Tocando a musica...");
        player.play(partitura);
        System.out.println("Musica finalizada!");
    }

    public void exportarParaMidi(String stringJFugue, String caminhoDoArquivo) {
        Pattern partitura = new Pattern(stringJFugue);

        try {
            File arquivoMidi = new File(caminhoDoArquivo);
            MidiFileManager.savePatternToMidi(partitura, arquivoMidi);
            System.out.println("Arquivo salvo em: " + arquivoMidi.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Erro ao salvar MIDI: " + e.getMessage());
        }
    }
}
