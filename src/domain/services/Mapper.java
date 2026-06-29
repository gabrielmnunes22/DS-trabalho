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

    public AcaoMusical mapearCaractere(char caractere){

        switch(caractere){
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
            case 'G':
                return new TocarNotaAcao(String.valueOf(caractere));

            case 'H':
                return new TocarNotaAcao("Bb");

            case '!':
                return new TrocarInstrumentoAcao(ACORDEAO);

            case ';':
                return new TrocarInstrumentoAcao(TUBULAR_BELLS);

            case ',':
                return new TrocarInstrumentoAcao(CHURCH_ORGAN);

            case ' ':
                return new DobrarVolumeAcao();

            case '>':
                return new MudarBpmAcao(ACELERACAO);
            case '<':
                return new MudarBpmAcao(DESACELARACAO);

            case '?':
                return new MudarOitavaAcao(1);
            case 'V':
                return new MudarOitavaAcao(-1);

            default:
                return null;
            }
        }

        private final List<RegraDeCaractere> cadeia = Arrays.asList(
            new RegraMiBemol(),
            new RegraNotas(),
            new RegraNotaH(),
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
