package domain.actions;

import domain.model.GlobalContext;
import domain.model.VoiceState;

public class MudarOitavaAcao implements AcaoMusical {

    private final int direcao; // 1 para subir, -1 para descer

    public MudarOitavaAcao(int direcao) {
        this.direcao = direcao;
    }

    @Override
    public String executar(VoiceState estadoVoz, GlobalContext contextoGlobal) {

        if (this.direcao > 0) {
            estadoVoz.aumentarOitava(); // Chama o método da TV
        } else if (this.direcao < 0) {
            estadoVoz.diminuirOitava(); // Chama o método da TV
        }

        // Mudar oitava também é silencioso no JFugue (só afeta a próxima nota)
        return "";
    }
}