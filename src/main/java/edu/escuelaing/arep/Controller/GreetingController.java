package edu.escuelaing.arep.Controller;

import edu.escuelaing.arep.Annotations.GetMapping;
import edu.escuelaing.arep.Annotations.RequestParam;
import edu.escuelaing.arep.Annotations.RestController;

@RestController
public class GreetingController {

    @GetMapping("/greeting")
    public static String greeting(@RequestParam(value ="name", defaultValue = "World") String name) {
        return "Hola " + name;
    }

    @GetMapping("/pi")
    public static String pi() {
        return Double.toString(Math.PI);
    }

    @GetMapping("/e")
    public static String e() {
        return Double.toString(Math.E);
    }
}
