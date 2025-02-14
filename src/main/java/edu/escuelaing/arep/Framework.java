package edu.escuelaing.arep;

import edu.escuelaing.arep.Http.HttpServer;
import java.io.IOException;


public class Framework{

    public static void main(String[] args) {
        try {
            System.out.println("Iniciando MicroFramework...");
            HttpServer.run();
        } catch (IOException e) {
            System.err.println("Error al iniciar el servidor: " + e.getMessage());
        }
    }
}
