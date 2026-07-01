package test;

import domain.model.ConfiguracaoMusical;
import domain.services.Parser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários do Parser.
 * Verificam se o mapeamento de texto para partitura JFugue
 * está correto.
 */
class ParserTest {

    private Parser parser;
    private ConfiguracaoMusical config;

    @BeforeEach
    void setUp() {
        parser = new Parser();

        // BPM=120, Volume=100, Oitava=6, Instrumento=6
        config = new ConfiguracaoMusical(120, 100, 6, 6);
    }

    @Test
    @DisplayName("Teste 1a - Notas A a G geram as notas corretas")
    void testNotasBasicas() {
        String resultado = parser.parsearTexto("ABCDEFG", config);
        assertTrue(resultado.contains("A6a100"), "Lá esperado");
        assertTrue(resultado.contains("B6a100"), "Si esperado");
        assertTrue(resultado.contains("C6a100"), "Dó esperado");
        assertTrue(resultado.contains("D6a100"), "Ré esperado");
        assertTrue(resultado.contains("E6a100"), "Mi esperado");
        assertTrue(resultado.contains("F6a100"), "Fá esperado");
        assertTrue(resultado.contains("G6a100"), "Sol esperado");
    }

    @Test
    @DisplayName("Teste 1b - Letra H gera Si bemol (Bb)")
    void testNotaH() {
        String resultado = parser.parsearTexto("H", config);
        assertTrue(resultado.contains("Bb6a100"), "Si bemol esperado");
    }

    @Test
    @DisplayName("Teste 1c - Sequência Mb gera Mi bemol (Eb)")
    void testMiBemol() {
        String resultado = parser.parsearTexto("CMbG", config);
        assertTrue(resultado.contains("Eb6a100"), "Mi bemol esperado");
        assertTrue(resultado.contains("C6a100"), "Dó esperado antes do Eb");
        assertTrue(resultado.contains("G6a100"), "Sol esperado após o Eb");
    }

    @Test
    @DisplayName("Teste 2 - Letras minúsculas a-h geram pausa incondicional")
    void testPausaMinusculas() {
        String resultado = parser.parsearTexto("CaCbC", config);
        String[] partes = resultado.trim().split("\\s+");
        int pausas = 0;
        for (String p : partes) {
            if (p.equals("R")) pausas++;
        }
        assertEquals(2, pausas, "Esperadas 2 pausas para 'a' e 'b'");
    }

    @Test
    @DisplayName("Teste 3 - Vogal sem nota anterior gera pausa")
    void testVogalSemNotaAnterior() {
        String resultado = parser.parsearTexto("oC", config);
        assertTrue(resultado.contains("R"), "Pausa esperada para vogal sem nota anterior");
        assertTrue(resultado.contains("C6a100"), "Dó esperado após a pausa");
    }

    @Test
    @DisplayName("Teste 4 - Vogal com nota anterior repete a nota")
    void testVogalRepeteNota() {
        String resultado = parser.parsearTexto("Con", config);
        long count = resultado.chars()
                .filter(c -> resultado.indexOf("C6a100") >= 0)
                .count();
        int ocorrencias = resultado.split("C6a100", -1).length - 1;
        assertEquals(3, ocorrencias, "C6a100 deve aparecer 3 vezes");
    }

    @Test
    @DisplayName("Teste 5 - Espaço dobra volume, limitado a 127")
    void testDobrarVolume() {
        String resultado = parser.parsearTexto("C C", config);
        assertTrue(resultado.contains("C6a100"), "Primeiro Dó com volume 100");
        assertTrue(resultado.contains("C6a127"), "Segundo Dó com volume 127 (limitado)");
    }

    @Test
    @DisplayName("Teste 6 - ? sobe oitava, V desce oitava")
    void testMudancaOitava() {
        String resultado = parser.parsearTexto("C?CVC", config);
        assertTrue(resultado.contains("C6a100"), "Dó na oitava 6");
        assertTrue(resultado.contains("C7a100"), "Dó na oitava 7 após ?");
        long count6 = (resultado.split("C6a100", -1).length - 1);
        assertTrue(count6 >= 2, "Dó deve voltar para oitava 6 após V");
    }

    @Test
    @DisplayName("Teste 6b - Oitava não ultrapassa o máximo (9)")
    void testOitavaMaxima() {
        String resultado = parser.parsearTexto("C?????????C", config);
        assertTrue(resultado.contains("C9a100"), "Oitava máxima deve ser 9");
        assertFalse(resultado.contains("C10a100"), "Oitava não deve ultrapassar 9");
    }

    @Test
    @DisplayName("Teste 6c - Oitava não vai abaixo do mínimo (0)")
    void testOitavaMinima() {
        String resultado = parser.parsearTexto("CVVVVVVVVVVC", config);
        assertTrue(resultado.contains("C0a100"), "Oitava mínima deve ser 0");
        assertFalse(resultado.contains("C-1a100"), "Oitava não deve ser negativa");
    }

    @Test
    @DisplayName("Teste 7 - ! troca para Harmonica (GM 22)")
    void testInstrumentoExclamacao() {
        String resultado = parser.parsearTexto("C!C", config);
        assertTrue(resultado.contains("I22"), "Harmonica GM 22 esperado");
    }

    @Test
    @DisplayName("Teste 8 - ; troca para Tubular Bells (GM 15)")
    void testInstrumentoPontoVirgula() {
        String resultado = parser.parsearTexto("C;C", config);
        assertTrue(resultado.contains("I15"), "Tubular Bells GM 15 esperado");
    }

    @Test
    @DisplayName("Teste 9 - , troca para Church Organ (GM 20)")
    void testInstrumentoVirgula() {
        String resultado = parser.parsearTexto("C,C", config);
        assertTrue(resultado.contains("I20"), "Church Organ GM 20 esperado");
    }

    @Test
    @DisplayName("Teste 10 - > acelera BPM em 10")
    void testAcelerarBpm() {
        String resultado = parser.parsearTexto("C>C", config);
        assertTrue(resultado.contains("T130"), "BPM deve aumentar para 130");
    }

    @Test
    @DisplayName("Teste 11 - < desacelera BPM em 10")
    void testDesacelerarBpm() {
        String resultado = parser.parsearTexto("C<C", config);
        assertTrue(resultado.contains("T110"), "BPM deve diminuir para 110");
    }

    @Test
    @DisplayName("Teste 12 - BPM não vai abaixo do mínimo (10)")
    void testBpmMinimo() {
        String entrada = "C" + "<".repeat(20) + "C";
        String resultado = parser.parsearTexto(entrada, config);
        assertFalse(resultado.contains("T0"), "BPM não deve chegar a 0");
        assertFalse(resultado.contains("T-"), "BPM não deve ser negativo");
    }

    @Test
    @DisplayName("Teste 13 - Cada linha é uma voz independente")
    void testMultiplasVozes() {
        String resultado = parser.parsearTexto("CD\nEF", config);
        assertTrue(resultado.contains("V0"), "Voz 0 esperada");
        assertTrue(resultado.contains("V1"), "Voz 1 esperada");
    }

    @Test
    @DisplayName("Teste 14 - Voz 0 começa na oitava 6, voz 1 na oitava 5")
    void testOitavasPorVoz() {
        String resultado = parser.parsearTexto("C\nC", config);
        assertTrue(resultado.contains("C6a100"), "Voz 0 deve tocar na oitava 6");
        assertTrue(resultado.contains("C5a80"),  "Voz 1 deve tocar na oitava 5 com volume 80");
    }

    @Test
    @DisplayName("Teste 15 - Instrumento base correto por voz")
    void testInstrumentosPorVoz() {
        String resultado = parser.parsearTexto("C\nC\nC\nC", config);
        assertTrue(resultado.contains("I6"),  "Voz 0: Cravo (GM 6)");
        assertTrue(resultado.contains("I20"), "Voz 1: Órgão (GM 20)");
        assertTrue(resultado.contains("I0"),  "Voz 2: Piano (GM 0)");
        assertTrue(resultado.contains("I70"), "Voz 3: Fagote (GM 70)");
    }

    @Test
    @DisplayName("Teste 16 - Atraso [n] gera n pausas antes da voz")
    void testAtrasoInicial() {
        String resultado = parser.parsearTexto("[4]C", config);
        int indexNota = resultado.indexOf("C6a100");
        String antes = resultado.substring(0, indexNota);
        long pausas = java.util.Arrays.stream(antes.split("\\s+"))
                .filter(s -> s.equals("R"))
                .count();
        assertEquals(4, pausas, "Devem existir 4 pausas antes da nota");
    }

    @Test
    @DisplayName("Teste 17 - Linha vazia é ignorada, índice de voz continua avançando")
    void testLinhaVaziaIgnorada() {
        String resultado = parser.parsearTexto("C\n\nC", config);
        assertTrue(resultado.contains("V0"), "Voz 0 esperada para primeira linha");
        assertTrue(resultado.contains("V2"), "Voz 2 esperada para terceira linha (índice 2)");
        assertFalse(resultado.contains("V1"), "Voz 1 não deve existir pois linha vazia foi pulada");
    }
}