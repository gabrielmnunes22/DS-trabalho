package config;

public class Config {

    private final int bpmInicial;
    private final int volumeInicial;
    private final int instrumentoInicial;
    private final int oitavaPadrao;

    public Config(int bpmInicial, int volumeInicial, int instrumentoInicial, int oitavaPadrao) {
        this.bpmInicial = bpmInicial;
        this.volumeInicial = volumeInicial;
        this.instrumentoInicial = instrumentoInicial;
        this.oitavaPadrao = oitavaPadrao;
    }

    public int getBpmInicial() {
        return bpmInicial;
    }

    public int getVolumeInicial() {
        return volumeInicial;
    }

    public int getInstrumentoInicial() {
        return instrumentoInicial;
    }

    public int getOitavaPadrao() {
        return oitavaPadrao;
    }

    @Override
    public String toString() {
        return "Config{" +
                "bpm=" + bpmInicial +
                ", volume=" + volumeInicial +
                ", instrumento=" + instrumentoInicial +
                ", oitava=" + oitavaPadrao +
                '}';
    }
}
