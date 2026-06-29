package domain.rules;

import domain.actions.AcaoMusical;
import domain.actions.MudarBpmAcao;
import domain.model.VoiceState;
import domain.model.GlobalContext;

/**
 * Regra para controle de BPM: > acelera, < desacelera.
 */
public class RegraBpm implements RegraDeCaractere {

    private static final int DELTA = 10;

    @Override
    public boolean aceita(char c, char proxima, VoiceState voz, GlobalContext ctx) {
        return c == '>' || c == '<';
    }

    @Override
    public AcaoMusical resolver(char c, VoiceState voz, GlobalContext ctx) {
        return new MudarBpmAcao(c == '>' ? DELTA : -DELTA);
    }
}