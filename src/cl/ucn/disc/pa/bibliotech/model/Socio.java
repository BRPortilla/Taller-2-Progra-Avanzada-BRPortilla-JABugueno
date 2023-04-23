/*
 * Copyright (c) 2023. Programacion Avanzada, DISC, UCN.
 */

package cl.ucn.disc.pa.bibliotech.model;

import cl.ucn.disc.pa.bibliotech.services.Utils;

/**
 * Clase que representa a un Socio.
 *
 * @author Programacion Avanzada.
 */
public final class Socio {

    /**
     * Número máximo de libros que puede tener el Socio.
     */
    private static final int NUMERO_LIBROS_MAXIMO = 10;

    /**
     * Nombre del socio.
     */
    private String nombre;

    /**
     * Apellido del socio.
     */
    private String apellido;

    /**
     * Email del socio.
     */
    private String correoElectronico;

    /**
     * Numero del socio.
     */
    private int numeroDeSocio;

    /**
     * Contrasenia del socio.
     */
    private String contrasenia;

    /**
     * Libros que el Socio tiene en prestamo (maximo 10).
     */
    private Libro[] librosEnPrestamo;

    /**
     * Cantidad actual de libros prestados.
     */
    private int cantidadLibros = 0;

    /**
     * @param nombre            del socio
     * @param apellido          del socio
     * @param correoElectronico del socio
     * @param numeroDeSocio     del socio
     * @param contrasenia       del socio
     */
    public Socio(String nombre, String apellido, String correoElectronico, int numeroDeSocio, String contrasenia) {

        if (nombre == null || nombre.length() == 0) {
            throw new IllegalArgumentException("Nombre de socio no válido.");
        }
        this.nombre = nombre;

        if (apellido == null || apellido.length() == 0) {
            throw new IllegalArgumentException("Apellido de socio no válido.");
        }
        this.apellido = apellido;

        // metodo estatico para validacion de email.
        Utils.validarEmail(correoElectronico);
        this.correoElectronico = correoElectronico;

        if (numeroDeSocio <= 0) {
            throw new IllegalArgumentException("Número de socio no válido.");
        }
        this.numeroDeSocio = numeroDeSocio;

        if (contrasenia == null || contrasenia.length() == 0) {
            throw new IllegalArgumentException("Contraseña no válida.");
        }
        this.contrasenia = contrasenia;
        this.cantidadLibros = 0;
    }

    /**
     * @return el nombre del Socio.
     */
    public String getNombre() {
        return this.nombre;
    }

    /**
     * @return el apellido del Socio.
     */
    public String getApellido() {
        return this.apellido;
    }

    /**
     * @return el nombre completo del Socio.
     */
    public String getNombreCompleto() {
        return this.nombre + " " + this.apellido;
    }

    /**
     * @return el correo electronico del Socio.
     */
    public String getCorreoElectronico() {
        return this.correoElectronico;
    }

    /**
     * @return el numero del Socio.
     */
    public int getNumeroDeSocio() {
        return this.numeroDeSocio;
    }

    /**
     * @return la contrasenia del Socio.
     */
    public String getContrasenia() {
        return this.contrasenia;
    }

    /**
     * Método agregarLibro
     * Agrega un libro en préstamo al Socio. Si ya existe un arreglo de préstamo, pero la cantidad de libros actuales es 0, se
     * vuelve a crear un arreglo con máximo 10 libros. No se pueden agregar más libros si se alcanza la cantidad máxima. Se copian
     * las características del libro de afuera, a uno local.
     * <p>
     * Si la cantidad de libros actuales es 0, se agrega directamente en la primera posición, se aumenta la cantidad de libros en 1,
     * y el libro pasa a estar ocupado (prestado = true). Si la cantidad es mayor que 0, y en la posición actual no hay nada, se
     * agrega en la última posición, también se aumenta en 1 la cantidad, y el libro pasa a estar ocupado.
     *
     * @param libro a agregar.
     */
    public void agregarLibro(final Libro libro) {
        if (this.librosEnPrestamo != null && cantidadLibros == 0) {
            this.librosEnPrestamo = new Libro[NUMERO_LIBROS_MAXIMO];
        }

        //Validación
        if (cantidadLibros == NUMERO_LIBROS_MAXIMO) {
            throw new IllegalArgumentException("El Socio ya tiene la maxima cantidad de libros en prestamo: " + NUMERO_LIBROS_MAXIMO);
        }


        Libro libro1 = libro;


        while (true) {
            if (cantidadLibros > 0 && librosEnPrestamo[cantidadLibros] == null) {
                librosEnPrestamo[cantidadLibros] = libro1;
                cantidadLibros++;
                libro1.setPrestado(true);
                break;
            }

            if (librosEnPrestamo[0] == null) {
                librosEnPrestamo[0] = libro1;
                cantidadLibros++;
                libro1.setPrestado(true);
                break;
            }
        }
    }

    /**
     * Método cambioDeContraseña
     * Designa la nueva contraseña al socio (setContraseña).
     *
     * @param contrasenia (nueva contraseña)
     */
    public void cambioDeContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    /**
     * Método cambioDeCorreo
     * Designa el nuevo correo al socio (setCorreo).
     *
     * @param correo (nuevo correo)
     */
    public void cambioDeCorreo(String correo) {
        this.correoElectronico = correo;
    }

    /**
     * Método cambioDeNombre
     * Designa el nuevo nombre al socio (setNombre).
     *
     * @param nombre (nuevo nombre)
     */
    public void cambioDeNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Método cambioDeApellido
     * Designa el nuevo apellido al socio (setApellido).
     *
     * @param apellido (nuevo apellido)
     */

    public void cambioDeApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     * Método getCantidadLibros
     *
     * @return la cantidad de libros actuales.
     */

    public int getCantidadLibros() {
        return cantidadLibros;
    }

    /**
     * Método getCantidadMaximaLibros
     *
     * @return la cantidad máxima de libros que se pueden solicitar.
     */
    public int getCantidadMaximaLibros() {
        return NUMERO_LIBROS_MAXIMO;
    }

    /**
     * Método obtenerLibrosEnPrestamo
     * Funciona de la misma manera que el método de la obtención de catálogo (en el sistema),
     * pero esta vez aplica con los libros en préstamo por el socio (si un libro no es null).
     *
     * @return String de todos los libros en préstamo por el socio.
     */
    public String obtenerLibrosEnPrestamo() {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cantidadLibros; i++) {
            Libro libro = librosEnPrestamo[i];
            if (libro != null) {
                sb.append("Titulo    : ").append(libro.getTitulo()).append("\n");
                sb.append("Autor     : ").append(libro.getAutor()).append("\n");
                sb.append("ISBN      : ").append(libro.getIsbn()).append("\n");
                sb.append("Categoria : ").append(libro.getCategoria()).append("\n");
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * Método devolverLibro
     * Se devuelve el libro prestado por el socio. Se elimina del arreglo de libros en préstamo,
     * y la cantidad de libros actuales se reduce en 1. El libro pasa a estar desocupado (prestado = false).
     *
     * @param libro que se va a devolver.
     */
    public void devolverLibro(Libro libro) {
        for (int i = 0; i < cantidadLibros; i++) {
            if (libro == librosEnPrestamo[i] && libro != null) {
                for (int j = i; j < cantidadLibros - 1; j++) {
                    librosEnPrestamo[j] = librosEnPrestamo[j + 1];
                }
                break;
            }
        }
        this.cantidadLibros--;
        libro.setPrestado(false);
    }
}