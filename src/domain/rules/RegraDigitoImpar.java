package domain.rules;

import domain.actions.AcaoMusical;
import domain.actions.TrocarInstrumentoAcao;
import domain.model.VoiceState;
import domain.model.GlobalContext;

/**
 * Regra para dígito ímpar: troca instrumento para Tubular Bells (GM #15).
 */
public class RegraDigitoImpar implements RegraDeCaractere {

    private static final int TUBULAR_BELLS = 15;

    @Override
    public boolean aceita(char c, char proxima, VoiceState voz, GlobalContext ctx) {
        if (!Character.isDigit(c)) return false;
        int digito = c - '0';
        return digito % 2 != 0;
    }

    @Override
    public AcaoMusical resolver(char c, VoiceState voz, GlobalContext ctx) {
        return new TrocarInstrumentoAcao(TUBULAR_BELLS);
    }
}