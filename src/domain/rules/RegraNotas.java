package domain.rules;

import domain.actions.AcaoMusical;
import domain.actions.TocarNotaAcao;
import domain.model.VoiceState;
import domain.model.GlobalContext;

/**
 * Regra para notas musicais: A, B, C, D, E, F, G (e H = Si bemol).
 */
public class RegraNotas implements RegraDeCaractere {

    private static final String NOTAS = "ABCDEFG";

    @Override
    public boolean aceita(char c, char proxima, VoiceState voz, GlobalContext ctx) {
        return NOTAS.indexOf(c) >= 0;
    }

    @Override
    public AcaoMusical resolver(char c, VoiceState voz, GlobalContext ctx) {
        return new TocarNotaAcao(String.valueOf(c));
    }
}