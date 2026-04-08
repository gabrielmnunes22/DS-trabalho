package domain.services;

import domain.actions.AcaoMusical;
import domain.actions.TocarNotaAcao;
import domain.actions.MudarBpmAcao;
import domain.actions.MudarOitavaAcao;
import domain.actions.DobrarVolumeAcao;

// liga letra/caracter com comando
public class Mapper {


    //ele pode devolver qualquer classe que tenha assinado o contrato



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

        // VOLUME
        case ' ': // espaco em branco
            return new DobrarVolumeAcao();

        // Bpm
        case '>':
            return new MudarBpmAcao(10); // acelera 10
        case '<':
            new MudarBpmAcao(-10); // desacelera 10

// --- OITAVAS (Você pode criar as classes depois e descomentar aqui) ---
             case '?':
                 return new MudarOitavaAcao(1);
             case 'V':
                 return new MudarOitavaAcao(-1);

            // Se for uma letra que não faz nada ou não reconhecida, retornamos null
            // (Mais para frente podemos criar uma 'PausaAcao' ou apenas ignorar)
        default:
            return null;
    }
    }
    }





