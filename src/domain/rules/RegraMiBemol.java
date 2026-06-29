package domain.rules;

import domain.actions.AcaoMusical;
import domain.actions.TocarNotaAcao;
import domain.model.VoiceState;
import domain.model.GlobalContext;

/**
 * Regra para Mi bemol: sequência "Mb".
 * O lookahead (proxima == 'b') é verificado aqui, evitando que o Parser precise saber disso.
 */
public class RegraMiBemol implements RegraDeCaractere {

    @Override
    public boolean aceita(char c, char proxima, VoiceState voz, GlobalContext ctx) {
        return c == 'M' && proxima == 'b';
    }

    @Override
    public AcaoMusical resolver(char c, VoiceState voz, GlobalContext ctx) {
        return new TocarNotaAcao("Eb");
    }
}