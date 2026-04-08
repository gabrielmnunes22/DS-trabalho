package infrastructure;

import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;
import org.jfugue.midi.MidiFileManager;

import java.io.File;
import java.io.IOException;

public class ReprodutorAudio {

    /**
     * Pega a String gigante gerada pelo nosso Parser e faz sair som
     */
    public void tocarMusica(String stringJFugue) {
        // Pattern é como o JFugue chama a partitura
        // Ele pega o texto e prepara para tocar
        Pattern partitura = new Pattern(stringJFugue);

        // O Player é a ferramenta do JFugue que aperta o play
        Player jfuguePlayer = new Player();

        System.out.println(" Tocando a música...");
        jfuguePlayer.play(partitura);
        System.out.println(" Música finalizada!");
    }

    /**
     * Pega a mesma String gigante e salva um arquivo .mid no computador.
     */
    public void exportarParaMidi(String stringJFugue, String caminhoDoArquivo) {
        Pattern partitura = new Pattern(stringJFugue);

        try {
            // Cria um arquivo no Windows/Mac com o nome que o usuário escolheu
            File arquivoMidi = new File(caminhoDoArquivo);

            // O JFugue converte o padrão para o formato MIDI real
            MidiFileManager.savePatternToMidi(partitura, arquivoMidi);

            System.out.println(" Arquivo salvo com sucesso em: " + arquivoMidi.getAbsolutePath());

        } catch (IOException e) {
            // Se der erro (ex: pasta não existe, sem permissão), avisamos aqui
            System.out.println(" Erro ao salvar o arquivo MIDI: " + e.getMessage());
        }
    }
}