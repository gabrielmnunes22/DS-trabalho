package domain.rules;

import domain.actions.AcaoMusical;
import domain.actions.TrocarInstrumentoAcao;
import domain.model.VoiceState;
import domain.model.GlobalContext;

/**
 * Regra para dígito par: troca instrumento para atual + valor do dígito.
 */
public class RegraDigitoPar implements RegraDeCaractere {

    @Override
    public boolean aceita(char c, char proxima, VoiceState voz, GlobalContext ctx) {
        if (!Character.isDigit(c)) return false;
        int digito = c - '0';
        return digito % 2 == 0;
    }

    @Override
    public AcaoMusical resolver(char c, VoiceState voz, GlobalContext ctx) {
        int digito = c - '0';
        int novoInstrumento = voz.getInstrumentoAtual() + digito;
        return new TrocarInstrumentoAcao(novoInstrumento);
    }
}