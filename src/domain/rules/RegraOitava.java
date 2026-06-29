package domain.rules;

import domain.actions.AcaoMusical;
import domain.actions.MudarOitavaAcao;
import domain.model.VoiceState;
import domain.model.GlobalContext;

/**
 * Regra para mudança de oitava: ? sobe, V desce.
 */
public class RegraOitava implements RegraDeCaractere {

    @Override
    public boolean aceita(char c, char proxima, VoiceState voz, GlobalContext ctx) {
        return c == '?' || c == 'V';
    }

    @Override
    public AcaoMusical resolver(char c, VoiceState voz, GlobalContext ctx) {
        return new MudarOitavaAcao(c == '?' ? 1 : -1);
    }
}