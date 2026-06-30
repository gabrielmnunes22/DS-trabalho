package domain.services;

import domain.actions.AcaoMusical;
import domain.actions.TocarNotaAcao;
import domain.actions.MudarBpmAcao;
import domain.actions.MudarOitavaAcao;
import domain.actions.DobrarVolumeAcao;
import domain.actions.TrocarInstrumentoAcao;
import domain.model.GlobalContext;
import domain.model.VoiceState;
import domain.rules.*;

import java.util.Arrays;
import java.util.List;

public class Mapper {

    private static final int ACORDEAO = 22;
    private static final int TUBULAR_BELLS = 15;
    private static final int CHURCH_ORGAN = 20;

    private static final int ACELERACAO = 10;
    private static final int DESACELARACAO = -10;

    public AcaoMusical mapearMiBemol(){
        return new TocarNotaAcao("Eb");
    }

    public AcaoMusical mapearCaractere(char c, char proxima, VoiceState voz, GlobalContext ctx) {
        for (RegraDeCaractere regra : cadeia) {
            if (regra.aceita(c, proxima, voz, ctx)) {
                return regra.resolver(c, voz, ctx);
            }
        }
        return new domain.actions.RepetirNotaAcao(); // segurança — nunca deve chegar aqui
    }

    private final List<RegraDeCaractere> cadeia = Arrays.asList(
            new RegraMiBemol(),
            new RegraNotas(),
            new RegraNotaH(),
            new RegraPausa(),
            new RegraEspaco(),
            new RegraInstrumento(),
            new RegraOitava(),
            new RegraBpm(),
            new RegraDigitoPar(),
            new RegraDigitoImpar(),
            new RegraVogal(),
            new RegraElse()
    );
    }
