package domain.rules;

import domain.actions.AcaoMusical;
import domain.actions.PausaAcao;
import domain.model.VoiceState;
import domain.model.GlobalContext;

/**
 * Regra para letras minúsculas a, b, c, d, e, f, g, h.
 * Segundo a especificação, essas letras NÃO são notas e sim
 * Silêncio/Pausa — incondicionalmente, mesmo que a nota anterior
 * processada tenha sido uma nota válida.
 */
public class RegraPausa implements RegraDeCaractere {

    private static final String LETRAS_PAUSA = "abcdefgh";

    @Override
    public boolean aceita(char c, char proxima, VoiceState voz, GlobalContext ctx) {
        return LETRAS_PAUSA.indexOf(c) >= 0;
    }

    @Override
    public AcaoMusical resolver(char c, VoiceState voz, GlobalContext ctx) {
        return new PausaAcao();
    }
}
