package domain.rules;

import domain.actions.AcaoMusical;
import domain.actions.RepetirNotaAcao;
import domain.model.VoiceState;
import domain.model.GlobalContext;

/**
 * Regra padrão (fallback): aceita qualquer caractere não tratado pelas regras anteriores.
 * Comportamento: repete a última nota se havia nota anterior, caso contrário pausa.
 * Deve ser sempre a última da cadeia.
 */
public class RegraElse implements RegraDeCaractere {

    @Override
    public boolean aceita(char c, char proxima, VoiceState voz, GlobalContext ctx) {
        return true; // fallback — aceita tudo
    }

    @Override
    public AcaoMusical resolver(char c, VoiceState voz, GlobalContext ctx) {
        return new RepetirNotaAcao();
    }
}
