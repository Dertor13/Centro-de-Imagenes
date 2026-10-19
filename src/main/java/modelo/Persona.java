package modelo;

/**
 * Clase abstracta que representa de forma genérica a una persona en el sistema.
 * Aplica los conceptos de Abstracción y Encapsulamiento de la POO.
 */
public abstract class Persona {
    protected String dni;
    protected String nombre;
    protected String apellido;
    protected String telefono;

    public Persona() {
    }

    public Persona(String dni, String nombre, String apellido, String telefono) {
        this.dni = validarDni(dni);
        this.nombre = validarNombre(nombre, "nombre");
        this.apellido = validarNombre(apellido, "apellido");
        this.telefono = telefono;
    }

    /** Valida que el DNI cumpla con el formato de 7 u 8 dígitos numéricos. */
    protected static String validarDni(String dni) {
        if (dni == null || !dni.matches("\\d{7,8}")) {
            throw new IllegalArgumentException("El DNI debe contener 7 u 8 números.");
        }
        return dni;
    }

    /** Valida que los nombres y apellidos no estén vacíos y no contengan símbolos inválidos. */
    protected static String validarNombre(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("El " + campo + " es obligatorio.");
        }
        if (!valor.matches("[\\p{L} .'-]+")) {
            throw new IllegalArgumentException("El " + campo + " no puede contener números ni símbolos inválidos.");
        }
        return valor.trim();
    }

    /** Método abstracto que obliga a las clases hijas a implementar el formato de sus datos completos. */
    public abstract String obtenerDatosCompletos();

    // Getters y Setters con validación encapsulada
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = validarDni(dni); }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = validarNombre(nombre, "nombre"); }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = validarNombre(apellido, "apellido"); }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}