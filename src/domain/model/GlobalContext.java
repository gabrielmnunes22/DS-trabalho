package domain.model;

/**
 *classe responsavel por mantes estado global da musica
 * principal estado global e o BPM
 * alterar o bpm afetos vozes simultaneamente
 */

public class GlobalContext {
    private int bpm;

    public GlobalContext() {
        this.bpm = 120; // valor padrão de BPM
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

        if(this.bpm <= 0){
            this.bpm = 10; //limite de seguranca para nao zerar
        }
    }

}
