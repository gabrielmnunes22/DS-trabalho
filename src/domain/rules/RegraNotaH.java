package domain.rules;

import domain.actions.AcaoMusical;
import domain.actions.TocarNotaAcao;
import domain.model.VoiceState;
import domain.model.GlobalContext;

/**
 * Regra para H = Si bemol (Bb).
 */
public class RegraNotaH implements RegraDeCaractere {

    @Override
    public boolean aceita(char c, char proxima, VoiceState voz, GlobalContext ctx) {
        return c == 'H';
    }

    @Override
    public AcaoMusical resolver(char c, VoiceState voz, GlobalContext ctx) {
        return new TocarNotaAcao("Bb");
    }
}
