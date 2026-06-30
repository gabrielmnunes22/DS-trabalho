package domain.actions;

import domain.model.GlobalContext;
import domain.model.VoiceState;

/**
 * Ação que gera uma pausa (silêncio) incondicional.
 * Usada pelas letras minúsculas a-h, que segundo a especificação
 * NÃO são notas e sim Silêncio/Pausa, independentemente de haver
 * uma nota anterior na voz (diferente do comportamento de
 * RepetirNotaAcao, usado por vogais/consoantes/ELSE).
 */
public class PausaAcao implements AcaoMusical {

    @Override
    public String executar(VoiceState estadoVoz, GlobalContext contextoGlobal) {
        // Pausa não atualiza a última nota tocada: a próxima vogal/consoante
        // ainda deve poder repetir a nota anterior à pausa, se houver.
        return "R ";
    }
}