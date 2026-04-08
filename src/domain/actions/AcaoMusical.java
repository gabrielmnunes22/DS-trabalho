package domain.actions;

import domain.model.GlobalContext;
import domain.model.VoiceState;

/*interface que define o contrato para comando musical
 */


public interface AcaoMusical {
    //"sempre' que for metodo eh public
    // Ele apenas dita a regra: quem quiser ser uma AcaoMusical
    // TEM que receber os estados e devolver uma String

    String executar(VoiceState estadoVoz, GlobalContext contextoGlobal);
}


