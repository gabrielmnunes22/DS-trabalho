package domain.rules;

import domain.actions.AcaoMusical;
import domain.model.VoiceState;
import domain.model.GlobalContext;

/**
 * Interface do padrão Chain of Responsibility.
 * Cada implementação representa uma regra de mapeamento de caractere.
 * A cadeia percorre as regras em ordem até encontrar uma que aceite o caractere.
 */
public interface RegraDeCaractere {

    /**
     * Verifica se esta regra é responsável pelo caractere fornecido.
     */
    boolean aceita(char c, char proxima, VoiceState voz, GlobalContext ctx);

    /**
     * Resolve o caractere e retorna a ação musical correspondente.
     * Só é chamado se aceita() retornou true.
     */
    AcaoMusical resolver(char c, VoiceState voz, GlobalContext ctx);
}