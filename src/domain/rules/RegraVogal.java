package domain.rules;

import domain.actions.AcaoMusical;
import domain.actions.RepetirNotaAcao;
import domain.model.VoiceState;
import domain.model.GlobalContext;

/**
 * Regra para vogais O, I, U (maiúsculas ou minúsculas): repete a última nota ou pausa.
 */
public class RegraVogal implements RegraDeCaractere {

    @Override
    public boolean aceita(char c, char proxima, VoiceState voz, GlobalContext ctx) {
        char upper = Character.toUpperCase(c);
        return upper == 'O' || upper == 'I' || upper == 'U';
    }

    @Override
    public AcaoMusical resolver(char c, VoiceState voz, GlobalContext ctx) {
        return new RepetirNotaAcao();
    }
}