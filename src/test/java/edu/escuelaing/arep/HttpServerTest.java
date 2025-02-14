package edu.escuelaing.arep;

import edu.escuelaing.arep.Http.HttpServer;
import org.junit.jupiter.api.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HttpServerTest {

    private static Thread serverThread;

    @BeforeAll
    public static void setUp() {
        serverThread = new Thread(() -> {
            try {
                HttpServer.run();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        serverThread.start();

        // Esperar a que el servidor se inicie completamente antes de probarlo
        try {
            Thread.sleep(4000); // Aumentado a 4000 ms para asegurar que el servidor esté listo
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }



    @AfterAll
    public static void tearDown() {
        serverThread.interrupt();
    }

    /**
     * Prueba si el servidor responde correctamente en el puerto 35000
     */
    @Test
    @Order(1)
    public void testServerRunning() {
        try {
            URL url = new URL("http://localhost:36000/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            assertEquals(200, responseCode);
        } catch (Exception e) {
            fail("No se pudo conectar al servidor.");
        }
    }

    /**
     * Prueba si la API `/api/restaurants` devuelve un JSON válido.
     */
    @Test
    @Order(2)
    public void testGetRestaurants() {
        try {
            URL url = new URL("http://localhost:36000/api/restaurants");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            assertEquals(200, responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = in.readLine();
            in.close();

            assertNotNull(response);
            assertTrue(response.startsWith("[") && response.endsWith("]"));
        } catch (Exception e) {
            fail("Error al obtener restaurantes.");
        }
    }

    /**
     * Prueba agregar un restaurante usando `POST`
     */
    @Test
    @Order(3)
    public void testPostRestaurant() {
        try {
            URL url = new URL("http://localhost:36000/api/restaurants");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            OutputStream os = connection.getOutputStream();
            os.write("\"McDonald's\"".getBytes());
            os.flush();
            os.close();

            int responseCode = connection.getResponseCode();
            assertEquals(201, responseCode);
        } catch (Exception e) {
            fail("Error al agregar restaurante.");
        }
    }

    /**
     *  Prueba eliminar un restaurante con `DELETE`
     */
    @Test
    @Order(4)
    public void testDeleteRestaurant() {
        try {
            URL url = new URL("http://localhost:36000/api/restaurants/0");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");

            int responseCode = connection.getResponseCode();
            assertEquals(200, responseCode);
        } catch (Exception e) {
            fail("Error al eliminar restaurante.");
        }
    }

    /**
     *  Prueba si el servidor devuelve `404 Not Found` cuando se accede a una ruta inexistente.
     */
    @Test
    @Order(5)
    public void testNotFound() {
        try {
            URL url = new URL("http://localhost:36000/invalidpath");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            assertEquals(404, responseCode);
        } catch (Exception e) {
            fail("Error al probar ruta inexistente.");
        }
    }

    /**
     * Prueba si el servidor devuelve `405 Method Not Allowed` cuando se usa un método no permitido.
     */
    @Test
    @Order(6)
    public void testMethodNotAllowed() {
        try {
            URL url = new URL("http://localhost:36000/api/restaurants");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT"); // PUT no está permitido

            int responseCode = connection.getResponseCode();
            assertEquals(405, responseCode);
        } catch (Exception e) {
            fail("Error al probar método no permitido.");
        }
    }
}
