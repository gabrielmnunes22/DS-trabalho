package domain.actions;

import domain.model.GlobalContext;
import domain.model.VoiceState;

public class MudarBpmAcao implements AcaoMusical{

    //o final para em nhum momento mude do 10

    private final int deltaBpm;

    public MudarBpmAcao(int deltaBpm) {
        this.deltaBpm = deltaBpm;
    }

    @Override
        public String executar(VoiceState estadoVoz, GlobalContext contextoGlobal) {

            contextoGlobal.changeBpm(this.deltaBpm);

        // A biblioteca JFugue usa a letra 'T' (Tempo) para definir o BPM.
         //somar uma String ("T") com um número inteiro (130),

            return "T" + contextoGlobal.getBpm() + " "; // ==T130

        }



}
