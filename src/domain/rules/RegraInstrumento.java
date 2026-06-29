package domain.rules;

import domain.actions.AcaoMusical;
import domain.actions.TrocarInstrumentoAcao;
import domain.model.VoiceState;
import domain.model.GlobalContext;

/**
 * Regra para troca de instrumento por caractere fixo: !, ; e ,
 */
public class RegraInstrumento implements RegraDeCaractere {

    private static final int HARMONICA    = 22;
    private static final int TUBULAR_BELLS = 15;
    private static final int CHURCH_ORGAN  = 20;

    @Override
    public boolean aceita(char c, char proxima, VoiceState voz, GlobalContext ctx) {
        return c == '!' || c == ';' || c == ',';
    }

    @Override
    public AcaoMusical resolver(char c, VoiceState voz, GlobalContext ctx) {
        switch (c) {
            case '!': return new TrocarInstrumentoAcao(HARMONICA);
            case ';': return new TrocarInstrumentoAcao(TUBULAR_BELLS);
            case ',': return new TrocarInstrumentoAcao(CHURCH_ORGAN);
            default:  return new TrocarInstrumentoAcao(TUBULAR_BELLS);
        }
    }
}