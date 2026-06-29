package domain.rules;

import domain.actions.AcaoMusical;
import domain.actions.DobrarVolumeAcao;
import domain.model.VoiceState;
import domain.model.GlobalContext;

/**
 * Regra para espaço: dobra o volume da voz atual.
 */
public class RegraEspaco implements RegraDeCaractere {

    @Override
    public boolean aceita(char c, char proxima, VoiceState voz, GlobalContext ctx) {
        return c == ' ';
    }

    @Override
    public AcaoMusical resolver(char c, VoiceState voz, GlobalContext ctx) {
        return new DobrarVolumeAcao();
    }
}