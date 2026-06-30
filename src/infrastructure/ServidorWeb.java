package infrastructure;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import domain.model.ConfiguracaoMusical;
import ui.MusicaController;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ServidorWeb {

    private final int porta;
    private final String pastaPublica;
    private final MusicaController controller;
    private HttpServer server;

    public ServidorWeb(int porta, String pastaPublica, MusicaController controller) {
        this.porta = porta;
        this.pastaPublica = pastaPublica;
        this.controller = controller;
    }

    public void iniciar() throws IOException {
        server = HttpServer.create(new InetSocketAddress(porta), 0);

        server.createContext("/api/tocar", this::handleTocar);
        server.createContext("/api/parar", this::handleParar);
        server.createContext("/api/salvar-midi", this::handleSalvarMidi);
        server.createContext("/", this::handleArquivoEstatico);

        server.setExecutor(null);
        server.start();

        System.out.println("Servidor rodando em http://localhost:" + porta);
    }

    public MusicaController getController() {
        return controller;
    }

    private void handleTocar(HttpExchange exchange) throws IOException {
        if (!validarPost(exchange)) return;

        try {
            String corpo = new String(exchange.getRequestBody().readAllBytes(), "UTF-8");
            String texto = extrairValorJson(corpo, "texto");
            if (texto == null) return;

            ConfiguracaoMusical config = extrairConfiguracao(corpo);

            String partitura = controller.tocar(texto, config);

            enviarJson(exchange, 200,
                "{\"status\": \"tocando\", \"partitura\": \"" + escapar(partitura) + "\"}");

        } catch (Exception e) {
            enviarJson(exchange, 500, "{\"erro\": \"" + escapar(e.getMessage()) + "\"}");
        }
    }

    private void handleParar(HttpExchange exchange) throws IOException {
        if (!validarPost(exchange)) return;

        controller.parar();
        enviarJson(exchange, 200, "{\"status\": \"parado\"}");
    }

    private void handleSalvarMidi(HttpExchange exchange) throws IOException {
        if (!validarPost(exchange)) return;

        try {
            String corpo = new String(exchange.getRequestBody().readAllBytes(), "UTF-8");
            String texto = extrairValorJson(corpo, "texto");
            if (texto == null) return;

            ConfiguracaoMusical config = extrairConfiguracao(corpo);

            byte[] midiBytes = controller.gerarMidi(texto, config);

            exchange.getResponseHeaders().set("Content-Type", "audio/midi");
            exchange.getResponseHeaders().set("Content-Disposition", "attachment; filename=\"musica.mid\"");
            exchange.sendResponseHeaders(200, midiBytes.length);
            exchange.getResponseBody().write(midiBytes);
            exchange.getResponseBody().close();

        } catch (Exception e) {
            enviarJson(exchange, 500, "{\"erro\": \"" + escapar(e.getMessage()) + "\"}");
        }
    }

    private void handleArquivoEstatico(HttpExchange exchange) throws IOException {
        String caminho = exchange.getRequestURI().getPath();
        if (caminho.equals("/")) caminho = "/interface.html";

        Path arquivo = Paths.get(pastaPublica, caminho);

        if (!Files.exists(arquivo) || Files.isDirectory(arquivo)) {
            enviarJson(exchange, 404, "{\"erro\": \"Arquivo nao encontrado\"}");
            return;
        }

        byte[] conteudo = Files.readAllBytes(arquivo);
        exchange.getResponseHeaders().set("Content-Type", detectarContentType(caminho));
        exchange.sendResponseHeaders(200, conteudo.length);
        exchange.getResponseBody().write(conteudo);
        exchange.getResponseBody().close();
    }

    private String extrairTextoDoBody(HttpExchange exchange) throws IOException {
        String corpo = new String(exchange.getRequestBody().readAllBytes());
        String texto = extrairValorJson(corpo, "texto");

        if (texto == null || texto.trim().isEmpty()) {
            enviarJson(exchange, 400, "{\"erro\": \"Texto vazio\"}");
            return null;
        }
        return texto;
    }

    private boolean validarPost(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "POST, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");

        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            return false;
        }

        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            enviarJson(exchange, 405, "{\"erro\": \"Use POST\"}");
            return false;
        }
        return true;
    }

    private void enviarJson(HttpExchange exchange, int codigo, String corpo) throws IOException {
        byte[] bytes = corpo.getBytes("UTF-8");
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(codigo, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.getResponseBody().close();
    }

    private String detectarContentType(String caminho) {
        if (caminho.endsWith(".html")) return "text/html; charset=UTF-8";
        if (caminho.endsWith(".css"))  return "text/css; charset=UTF-8";
        if (caminho.endsWith(".js"))   return "application/javascript; charset=UTF-8";
        if (caminho.endsWith(".json")) return "application/json; charset=UTF-8";
        return "application/octet-stream";
    }

    private String extrairValorJson(String json, String chave) {
        String busca = "\"" + chave + "\"";
        int idx = json.indexOf(busca);
        if (idx == -1) return null;

        idx = json.indexOf(":", idx + busca.length());
        if (idx == -1) return null;

        idx++;
        while (idx < json.length() && json.charAt(idx) == ' ') idx++;
        if (idx >= json.length() || json.charAt(idx) != '"') return null;

        idx++;
        StringBuilder sb = new StringBuilder();
        while (idx < json.length() && json.charAt(idx) != '"') {
            if (json.charAt(idx) == '\\' && idx + 1 < json.length()) {
                idx++;
                switch (json.charAt(idx)) {
                    case 'n': sb.append('\n'); break;
                    case 'r': sb.append('\r'); break;
                    case 't': sb.append('\t'); break;
                    case '"': sb.append('"'); break;
                    case '\\': sb.append('\\'); break;
                    default: sb.append(json.charAt(idx));
                }
            } else {
                sb.append(json.charAt(idx));
            }
            idx++;
        }
        return sb.toString();
    }

    private String escapar(String texto) {
        if (texto == null) return "";
        return texto.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r");
    }

    private ConfiguracaoMusical extrairConfiguracao(String corpo) {
        String bpmStr         = extrairValorJson(corpo, "bpm");
        String volumeStr      = extrairValorJson(corpo, "volume");
        String oitavaStr      = extrairValorJson(corpo, "oitava");
        String instrumentoStr = extrairValorJson(corpo, "instrumento");

        int bpm         = parseSafe(bpmStr, 120);
        int volume      = parseSafe(volumeStr, 100);
        int oitava      = parseSafe(oitavaStr, 6);
        int instrumento = parseSafe(instrumentoStr, 6);

        return new ConfiguracaoMusical(bpm, volume, oitava, instrumento);
    }

    private int parseSafe(String valor, int padrao) {
        if (valor == null) return padrao;
        try { return Integer.parseInt(valor.trim()); }
        catch (NumberFormatException e) { return padrao; }
    }
}
