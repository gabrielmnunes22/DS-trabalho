package domain.actions;

import domain.model.GlobalContext;
import domain.model.VoiceState;


// implements significa para o java que
// assino o contrato da AcaoMusical
public class TocarNotaAcao implements AcaoMusical {

    // final == const em C
    // garante que apos a nota criada, ela nao pode ser alterada
    private final String notaBase;

    // Construtor '==' incializar struct em C
    // Quando o texto tiver a letra 'C', o sistema vai fazer: new TocarNotaAcao("C");
    public TocarNotaAcao(String notaBase) {
        this.notaBase = notaBase;
    }

    // @Override é um aviso de segurança para o compilador.
    // É como dizer estou sobrescrevendo a fucnao da Interface
    // Se eu digitar o nome errado (ex: 'esecutar'), me avise dando erro!"
    @Override
    public String executar(VoiceState estadoVoz, GlobalContext contextoGlobal) {

        // 1. Atualizamos a memória da voz. Se a próxima letra for uma vogal (O, I, U),
        // o requisito da Fase 1 diz que precisamos repetir a última nota.
        estadoVoz.setUltimaNota(this.notaBase);

        // 2. Montamos a string que a biblioteca JFugue entende.
        // Exemplo de saída: "C5a80 " (Nota Dó, Oitava 5, attack(volume) 80)
        return this.notaBase + estadoVoz.getOitavaAtual() + "a" + estadoVoz.getVolumeAtual() + " ";
    }




}
