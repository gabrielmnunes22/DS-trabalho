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
    // variáveis
    private final int idVoz;
    private int oitavaAtual;
    private int volumeAtual;
    private int instrumentoAtual;
    private String ultimaNota;

    // constantes - números instrumentos JFUGUE
    private static final int CRAVO = 6;
    private static final int ORGAO = 20;
    private static final int PIANO = 0;
    private static final int FAGOTE = 70;

    // constantes - parâmetros globais
    private static final int VOLUME_MAXIMO = 120;
    private static final int OITAVA_MAXIMA = 9;
    private static final int OITAVA_MINIMA = 0;

    // constantes - ciclo vozes/oitavas e vozes/volumes
    private static final int TOTAL_VOZES = 4;
    private static final int OITAVA_BASE = 6;
    private static final int VOLUME_BASE = 100;


    // idVoz = linha atual do arquivo texto. "Cada linha representa uma voz diferente"
    public VoiceState(int idVoz){
        this.idVoz = idVoz;
        this.ultimaNota = "R"; // R == pause na biblio JFugue
        inicializarValoresCiclicosBase();

    }

    /*
    conforme a fase 2, depois da 4º voz os parâmetros de oitavas e volumes se repetem
     */

    private void inicializarValoresCiclicosBase(){
        int ciclo = this.idVoz % TOTAL_VOZES; // retorn so 0,1,2,3

        //oitava base; Voz 0=6, 1=5, 2=4, 3=3
        this.oitavaAtual = OITAVA_BASE - ciclo;

        //volume base; Voz 0=100, 1=80, 2=60, 3=40
        this.volumeAtual = VOLUME_BASE - (ciclo * 20);

        //instrumento base
        switch(ciclo){
            case 0: this.instrumentoAtual = CRAVO; break; // Cravo
            case 1: this.instrumentoAtual = ORGAO; break; // Orgao
            case 2: this.instrumentoAtual = PIANO; break; // piano
            case 3: this.instrumentoAtual = FAGOTE; break; // fagote
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
        if (this.oitavaAtual < OITAVA_MAXIMA) {
            this.oitavaAtual++;
        }
    }
    /**
     * Caractere 'V' - Diminui uma oitava (limite mínimo 0).
     */
    public void diminuirOitava() {
        if (this.oitavaAtual > OITAVA_MINIMA) {
            this.oitavaAtual--;
        }
    }
    /**
     * Caractere ' ' (Espaço) - Dobra o volume (limite 127).
     * Se nao puder dobrar , o requisito da Fase 1
     * pedia para colocar no valor maximo (127).
     */
    public void dobrarVolume() {
        if (this.volumeAtual * 2 <= VOLUME_MAXIMO) {
            this.volumeAtual *= 2;
        } else {
            this.volumeAtual = VOLUME_MAXIMO;
        }
    }

}
