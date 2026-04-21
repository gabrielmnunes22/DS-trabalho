package domain.actions;

import domain.model.GlobalContext;
import domain.model.VoiceState;

public class TrocarInstrumentoAcao implements AcaoMusical {

    private final int numInstrumento;

    public TrocarInstrumentoAcao(int numInstrumento){this.numInstrumento = numInstrumento;}

    @Override
    public String executar(VoiceState estadoVoz, GlobalContext contextoGlobal) {

        estadoVoz.setInstrumentoAtual(numInstrumento);

        // afeta apenas a voz atual
        return "I" + estadoVoz.getInstrumentoAtual() + " ";
    }
}
