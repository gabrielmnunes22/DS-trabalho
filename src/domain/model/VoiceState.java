package domain.model;

/**
 * Essa classe deve responder essas perguntas
 * Em qual oitava esta linha específica está tocando agora?
 * Qual o volume desta linha?
 * Qual instrumento esta linha está usando?
 * Qual foi a última nota tocada nesta linha
 * (para o caso de vogais repetirem a nota)?

 **/

public class VoiceState {
    private final int idVoz;
    private int oitavaAtual;
    private int volumeAtual;
    private int instrumentoAtual;
    private String ultimaNota;

    public VoiceState(int idVoz){
        this.idVoz = idVoz;
        this.ultimaNota = "R"; // R == pause na biblio JFugue
        inicializarValoresCiclicosBase();

    }

    /*
    conforme a fase 2, cada 4 voz os parametros
    da vase se repetem
     */

    private void inicializarValoresCiclicosBase(){
        int ciclo = this.idVoz % 4; // retorn so 0,1,2,3

        //oitava base; Voz 0=6, 1=5, 2=4, 3=3
        this.oitavaAtual = 6 - ciclo;

        //volume base; Voz 0=100, 1=80, 2=60, 3=40
        this.volumeAtual = 100 - (ciclo * 20);

        //instrumento base
        switch(ciclo){
            case 0: this.instrumentoAtual = 6; break; // Cravo
            case 1: this.instrumentoAtual = 20; break; // Orgao
            case 2: this.instrumentoAtual = 0; break; // piano
            case 3: this.instrumentoAtual = 70; break; // fagote
        }
    }

// nossos getters
    public int getIdVoz(){
        return idVoz;
    }
    public int getOitavaAtual(){
        return oitavaAtual;
    }
    public int getVolumeAtual(){
        return volumeAtual;
    }
    public int getInstrumentoAtual(){
        return instrumentoAtual;
    }
    public String getUltimaNota(){
        return ultimaNota;
    }

    // setters e metodos de regras

    public void setUltimaNota(String nota){
        this.ultimaNota = nota;
    }
    public void setInstrumentoAtual(int instrumento){
        this.instrumentoAtual = instrumento;
    }
    /**
     * Caractere '?' - Aumenta uma oitava (limite máximo 9).
     */
    public void aumentarOitava() {
        if (this.oitavaAtual < 9) {
            this.oitavaAtual++;
        }
    }
    /**
     * Caractere 'V' - Diminui uma oitava (limite mínimo 0).
     */
    public void diminuirOitava() {
        if (this.oitavaAtual > 0) {
            this.oitavaAtual--;
        }
    }
    /**
     * Caractere ' ' (Espaço) - Dobra o volume (limite 127).
     * Se nao puder dobrar , o requisito da Fase 1
     * pedia para colocar no valor maximo (127).
     */
    public void dobrarVolume() {
        if (this.volumeAtual * 2 <= 127) {
            this.volumeAtual *= 2;
        } else {
            this.volumeAtual = 127;
        }
    }

}
