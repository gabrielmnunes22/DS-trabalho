package domain.actions;

import domain.model.GlobalContext;
import domain.model.VoiceState;

public class DobrarVolumeAcao implements AcaoMusical {

    @Override
    public String executar(VoiceState estadoVoz, GlobalContext contextoGlobal) {

        estadoVoz.dobrarVolume();

        // Retornamos uma String vazia, pois o JFugue não precisa de um comando
        // O novo volume será usado automaticamente na próxima vez que a TocarNotaAcao for chamada
        return "";
    }
}