package domain.model;

/**
 *classe responsavel por mantes estado global da musica
 * principal estado global e o BPM
 * alterar o bpm afetos vozes simultaneamente
 */

public class GlobalContext {

    private static final int BPM_PADRAO = 120;
    private static final int BPM_MINIMO = 10;
    private static final int BPM_LIMITE_ZERO = 0;

    private int bpm;

    public GlobalContext() {
        this.bpm = BPM_PADRAO; // valor padrão de BPM
    }

    public int getBpm() {
        return bpm;
    }

    public void setBpm(int bpm) {
        this.bpm = bpm;
    }

/**
 * Altera o BPM atual somando o delta passado.
 * delta valor a ser somado (positivo para acelerar, negativo para desacelerar).
 */
    public void changeBpm(int delta){
        this.bpm += delta;

        if(this.bpm <= BPM_LIMITE_ZERO){
            this.bpm = BPM_MINIMO; //limite de seguranca para nao zerar
        }
    }

}
