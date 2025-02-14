# AREP_Taller3

## Taller de Arquitecturas de Servidores de Aplicaciones, Meta protocolos de objetos, Patrón IoC, Reflexión
En este taller usted explorará la arquitectura de las aplicaciones distribuidas. Concretamente, exploraremos la arquitectura de  los servidores web y el protocolo http sobre el que están soportados. 

## Description 

Esta aplicación es un servidor web en Java que implementa un microframework IoC para manejar solicitudes HTTP y servir tanto archivos estáticos como endpoints REST.

### Características principales

Servidor web propio desarrollado con Java y Sockets.
Framework IoC que detecta automáticamente controladores anotados con @RestController.
Manejo de rutas con @GetMapping y @RequestParam para generar respuestas dinámicas.
API REST /api/restaurants que permite:
Listar restaurantes (GET).
Agregar nuevos restaurantes (POST).

# Project Structure
![image](https://github.com/user-attachments/assets/fa0ca5e0-14b0-442f-9dde-9f60f04e50dd)


#  Prerequisites
Before running the project, make sure you have the following installed:
- [JDK 21](https://www.oracle.com/co/java/technologies/downloads/)
- [Maven](https://maven.apache.org/download.cgi)
- [Git](https://git-scm.com/downloads)

1. **Ensure You Have Java Installed:**
   ```sh
   java -version
   ```
2. **Ensure You Have Maven Installed:**
   ```sh
   mvn -version
   ```
###  Instalación

1. **Clone this repository:**
   ```sh
   https://github.com/miguel-tellez/taller3-AREP.git
   ```

2. **Navigate to the workshop directory:**
   ```sh
   cd AREP_Taller3
   ```

2. **By running the following command create the project:**
   ```sh
   mvn clean install
   ```

3. **run the server:**
   ```sh
   java -cp target/classes edu.escuelaing.arep.Framework
   ```

4. **Access the web application in your browser:**
   ```
   http://localhost:36000
   ```

![image](https://github.com/user-attachments/assets/bf5b44d1-57ee-4c51-93d0-12fefae46c5e)

![image](https://github.com/user-attachments/assets/602a46e4-5edf-464a-9760-3ffa7e7336e0)


**Get all restaurants**
```sh
http://localhost:36000/api/restaurants
```
**TEST**

![image](https://github.com/user-attachments/assets/5c1a4330-aeb0-4182-817f-ea5f12ac81a7)


##  Web interface
The interface allows you to view restaurants dynamically. Use **HTML, CSS and JavaScript** to interact with the server.


##  Authors
- **Miguel Camilo Tellez** - [miguel-tellez](https://github.com/miguel-tellez)
  
