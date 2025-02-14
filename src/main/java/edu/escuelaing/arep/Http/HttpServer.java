package edu.escuelaing.arep.Http;

import java.io.*;
import java.lang.reflect.Method;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class HttpServer {
    private static final int PORT = 36000;
    private static final String BASE_DIRECTORY = "src/main/resources";
    private static final Map<String, Method> services = new HashMap<>();
    private static final List<String> restaurants = new ArrayList<>(); // Lista en memoria para almacenar restaurantes

    public static void run() throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Servidor iniciado en http://localhost:" + PORT);

        while (true) {
            try (Socket clientSocket = serverSocket.accept()) {
                handleRequest(clientSocket);
            }
        }
    }

    private static void handleRequest(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             OutputStream dataOut = clientSocket.getOutputStream()) {

            String requestLine = in.readLine();
            if (requestLine == null || requestLine.isEmpty()) return;

            System.out.println("Solicitud recibida: " + requestLine);
            String[] requestParts = requestLine.split(" ");
            String method = requestParts[0];
            String path = requestParts[1];

            if (path.startsWith("/api/restaurants")) {
                handleApiRequest(method, path, in, out);
            } else if (method.equals("GET")) {
                serveStaticFile(path, out, dataOut);
            } else {
                sendResponse(out, 405, "Method Not Allowed", "Método no permitido.");
            }
        } catch (IOException e) {
            System.err.println("Error al manejar la solicitud: " + e.getMessage());
        }
    }

    /**
     * Maneja las solicitudes de la API REST `/api/restaurants`
     */
    private static void handleApiRequest(String method, String path, BufferedReader in, PrintWriter out) throws IOException {
        if (method.equals("GET")) {
            // Devuelve la lista de restaurantes en formato JSON
            String jsonResponse = "[" + String.join(",", restaurants.stream().map(r -> "\"" + r + "\"").collect(Collectors.toList())) + "]";
            sendResponse(out, 200, "OK", jsonResponse);
        } else if (method.equals("POST")) {
            // Agregar un restaurante
            String body = getRequestBody(in);
            restaurants.add(body);
            sendResponse(out, 201, "Created", "Restaurante agregado: " + body);
        } else if (method.equals("DELETE")) {
            // Eliminar un restaurante por índice
            try {
                int index = Integer.parseInt(path.split("/")[3]);
                if (index >= 0 && index < restaurants.size()) {
                    restaurants.remove(index);
                    sendResponse(out, 200, "OK", "Restaurante eliminado");
                } else {
                    sendResponse(out, 400, "Bad Request", "Índice inválido");
                }
            } catch (Exception e) {
                sendResponse(out, 400, "Bad Request", "Índice inválido");
            }
        } else {
            sendResponse(out, 405, "Method Not Allowed", "Método no permitido.");
        }
    }

    /**
     * Obtiene el cuerpo de la solicitud (usado en POST)
     */
    private static String getRequestBody(BufferedReader in) throws IOException {
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null && !line.isEmpty()) {
            body.append(line);
        }
        return body.toString();
    }

    private static void serveStaticFile(String path, PrintWriter out, OutputStream dataOut) throws IOException {
        String filePath = BASE_DIRECTORY + (path.equals("/") ? "/index.html" : path);
        File file = new File(filePath);

        if (file.exists() && !file.isDirectory()) {
            String contentType = Files.probeContentType(file.toPath());
            byte[] fileData = Files.readAllBytes(file.toPath());

            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: " + contentType);
            out.println("Content-Length: " + fileData.length);
            out.println();
            out.flush();

            dataOut.write(fileData, 0, fileData.length);
            dataOut.flush();
        } else {
            sendResponse(out, 404, "Not Found", "Archivo no encontrado.");
        }
    }

    private static void sendResponse(PrintWriter out, int statusCode, String statusMessage, String body) {
        out.printf("HTTP/1.1 %d %s\r\n", statusCode, statusMessage);
        out.println("Content-Type: application/json");
        out.println();
        out.println(body);
    }
}
