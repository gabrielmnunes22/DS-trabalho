package domain.services;

import domain.actions.AcaoMusical;
import domain.actions.TocarNotaAcao;
import domain.actions.MudarBpmAcao;
import domain.actions.MudarOitavaAcao;
import domain.actions.DobrarVolumeAcao;
import domain.actions.TrocarInstrumentoAcao;

// liga letra/caracter com comando
public class Mapper {

    // constantes - número instrumentos JFUGUE
    private static final int ACORDEAO = 22;
    private static final int TUBULAR_BELLS = 15;
    private static final int CHURCH_ORGAN = 20;

    // constantes - aceleração e desaceleração BPM
    private static final int ACELERACAO = 10;
    private static final int DESACELARACAO = -10;

    //ele pode devolver qualquer classe que tenha assinado o contrato

    public AcaoMusical mapearMiBemol(){
        return new TocarNotaAcao("Eb");
    }

    public AcaoMusical mapearCaractere(char caractere){

        switch(caractere){
            // NOTAS MUSICAIS
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
            case 'G':
                // transforma o char em string
                return new TocarNotaAcao(String.valueOf(caractere));

            // si bemol == Bb
            case 'H':
                return new TocarNotaAcao("Bb");

            // Trocar instrumento para GM #22 (acordeão)
            case '!':
                return new TrocarInstrumentoAcao(ACORDEAO);

            // Trocar instrumento para GM #15 (Tubular Bells)
            case ';':
                return new TrocarInstrumentoAcao(TUBULAR_BELLS);

            // Trocar instrumento para GM #20 (Church Organ)
            case ',':
                return new TrocarInstrumentoAcao(CHURCH_ORGAN);

            // VOLUME
            case ' ': // espaco em branco
                return new DobrarVolumeAcao();

            // Bpm
            case '>':
                return new MudarBpmAcao(ACELERACAO); // acelera 10
            case '<':
                new MudarBpmAcao(DESACELARACAO); // desacelera 10

            // --- OITAVAS (Você pode criar as classes depois e descomentar aqui) ---
            case '?':
                return new MudarOitavaAcao(1);  // oitava = oitava + 1
            case 'V':
                return new MudarOitavaAcao(-1); // oitava = oitava - 1

            // Se for uma letra que não faz nada ou não reconhecida, retornamos null
            // (Mais para frente podemos criar uma 'PausaAcao' ou apenas ignorar)
            default:
                return null;
            }
        }
    }





