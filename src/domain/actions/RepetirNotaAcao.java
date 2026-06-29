package domain.actions;

import domain.model.GlobalContext;
import domain.model.VoiceState;

/**
 * Ação que repete a última nota tocada na voz atual.
 * Se não houver nota anterior, gera uma pausa (R).
 * Usada por vogais (O, I, U), consoantes não-nota e pelo fallback (ELSE).
 */
public class RepetirNotaAcao implements AcaoMusical {

    @Override
    public String executar(VoiceState estadoVoz, GlobalContext contextoGlobal) {
        String ultimaNota = estadoVoz.getUltimaNota();
        if (ultimaNota != null && !ultimaNota.equals("R")) {
            return ultimaNota + estadoVoz.getOitavaAtual() + "a" + estadoVoz.getVolumeAtual() + " ";
        }
        return "R ";
    }
}
