/*
 * Copyright (c) 2023. Programacion Avanzada, DISC, UCN.
 */

package cl.ucn.disc.pa.bibliotech.services;

import cl.ucn.disc.pa.bibliotech.model.Libro;
import cl.ucn.disc.pa.bibliotech.model.Socio;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The Sistema.
 *
 * @author Programacion Avanzada.
 */
public final class Sistema {

    /**
     * Procesador de JSON.
     */
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /**
     * The list of Socios.
     */
    private Socio[] socios;

    /**
     * The list of Libros.
     */
    private Libro[] libros;

    /**
     * Socio en el sistema.
     */
    private Socio socio;

    /**
     * The Sistema.
     */

    public Sistema() throws IOException {

        //No hay socio loggeado.
        this.socios = new Socio[0];
        this.libros = new Libro[0];
        this.socio = null;

        //Carga de libros y socios.
        try {
            this.cargarInformacion();
        } catch (FileNotFoundException ex) {
            //No se encuentran datos, por ende se agregan por defecto.

            //Se crea un socio
            this.socios = Utils.append(this.socios, new Socio("John", "Doe", "john.doe@ucn.cl", 1, "john123"));

            //Se crea un libro y se agrega
            this.libros = Utils.append(this.libros, new Libro("1491910771", "Head First Java: A Brain-Friendly Guide", " Kathy Sierra", "Programming Languages"));

            //Se crea otro libro y también se agrega
            this.libros = Utils.append(this.libros, new Libro("1491910772", "Effective Java", "Joshua Bloch", "Programming Languages"));
        } finally {
            //Se guarda la información
            this.guardarInformacion();
        }
    }

    /**
     * Método: iniciarSession
     * Permite activar un socio dentro de la lista de socios, dándole el atributo de socio (de null, pasa a ser socio).
     *
     * @param numeroDeSocio a utilizar.
     * @param contrasenia   a validar.
     */
    public void iniciarSession(final int numeroDeSocio, final String contrasenia) throws Exception {

        //Número de socio mayor que 0 (positivo).
        if (numeroDeSocio <= 0) {
            throw new IllegalArgumentException("El numero de socio no es válido. Ingrese una cantidad mayor a 0.");
        }

        //Se busca el socio, y se retorna.
        Socio socio1 = buscarSocio(numeroDeSocio);

        //Se verifica si la contraseña coincide, en ese caso se retorna true.
        boolean contraseniaCorrecta = verificarContraseniaSistema(contrasenia, socio1);

        //Se realiza el loggeo de socio, permitiendo que tenga el atributo de socio.
        loggearSocio(socio1, contraseniaCorrecta);
    }

    /**
     * Método cerraSession:
     * Cierra la sesión del socio.
     * El socio loggeado = null
     */
    public void cerrarSession() {
        this.socio = null;
    }

    /**
     * Método realizarPrestamoLibro: ingresa un libro de los disponibles al socio, permitiendo el prestamo.
     * Si el libro no se encuentra, no se puede realizar el préstamo (el libro es null)
     * En caso contrario, se agrega a la lista de libros prestados por el socio.
     *
     * @param isbn (ISBN del libro a prestar).
     */
    public void realizarPrestamoLibro(final String isbn) throws Exception {
        //El socio debe esta loggeado.
        if (this.socio == null) {
            throw new IllegalArgumentException("El socio no se ha logeado.");
        }


        // Se busca el libro.
        Libro libro = this.buscarLibro(isbn);

        if (libro == null) {
            throw new IllegalArgumentException("Libro con isbn " + isbn + " no existe o no se encuentra disponible.");
        }

        // Se agrega el libro al socio.
        this.socio.agregarLibro(libro);

        //Se obtiene la cantidad de libros del socio.
        int cantidadLibros = cantidadLibrosSocio();

        //Se actualiza la información de los archivos.
        this.guardarInformacion();
    }

    /**
     * Método (obtegerCatalogoLibros):
     * Obtiene un String que representa el listado completo de libros disponibles.
     * Se obtiene todos los libros disponibles ya sea para préstamo, o para calificar, después de recorrer la lista de libros.
     * El parámetro que se trae al método es un código que diferencia la forma de desplegar el código.
     * Si prestamo_calificacion es 0, se imprimirá para préstamo, todos los libros que estén disponibles (prestado = false).
     * Si prestamo_calificacion es 1, se imprimirá para calificación, todos los libros en el catálogo.
     * La cantidad de libros disponibles informa si existe al menos un libro, de forma contraria, se retorna null.
     *
     * @param prestamo_calificacion (el código que permite diferenciar si se desplegará para préstamo, o calificación).
     * @return String con información de los libros disponibles.
     * @throws Exception
     */
    public String obtegerCatalogoLibros(int prestamo_calificacion) throws Exception {
        StringBuilder sb = new StringBuilder();
        int cantidadLibrosDisponibles = 0;

        if (prestamo_calificacion == 0) {
            for (Libro libro : this.libros) {
                //Se recorre toda la lista de libros (el catálogo).
                //Si el libro no está prestado (disponible), se agrega al string.
                if (!libro.getPrestado()) {
                    cantidadLibrosDisponibles++;
                    sb.append("Titulo    : ").append(libro.getTitulo()).append("\n");
                    sb.append("Autor     : ").append(libro.getAutor()).append("\n");
                    sb.append("ISBN      : ").append(libro.getIsbn()).append("\n");
                    sb.append("Categoria : ").append(libro.getCategoria()).append("\n");
                    sb.append("\n");
                }
            }
            if (cantidadLibrosDisponibles > 0) {
                //Si la cantidad de libros disponibles es mayor a 0, se retorna.
                //o si no, null.
                return sb.toString();
            } else {
                return null;
            }
        } else if (prestamo_calificacion == 1) {
            for (Libro libro : this.libros) {
                //Si el libro existe (es diferente de null), se agrega al string.
                if (libro != null) {
                    cantidadLibrosDisponibles++;
                    sb.append("Titulo    : ").append(libro.getTitulo()).append("\n");
                    sb.append("Autor     : ").append(libro.getAutor()).append("\n");
                    sb.append("ISBN      : ").append(libro.getIsbn()).append("\n");
                    sb.append("Categoria : ").append(libro.getCategoria()).append("\n");
                    sb.append("\n");
                }
            }
            return sb.toString();
        }
        //Se retorna null en caso que no hayan libros.
        return null;
    }

    /**
     * Metodo (buscarLibro): busca un libro en los libros disponibles.
     *
     * @param isbn a buscar.
     * @return el libro o null si no fue encontrado.
     */
    private Libro buscarLibro(final String isbn) {
        // Se recorre el arreglo de libros.
        for (Libro libro : this.libros) {
            // Si se encuentra, se retorna el libro.
            if (libro.getIsbn().equals(isbn)) {
                return libro;
            }
        }
        // No se encuentra, se retorna null.
        return null;
    }

    /**
     * Lee los archivos libros.json y socios.json.
     *
     * @throws FileNotFoundException si alguno de los archivos no se encuentra.
     */
    private void cargarInformacion() throws FileNotFoundException {
        // Se trata de leer los libros y socios desde el archivo.
        this.socios = GSON.fromJson(new FileReader("socios.json"), Socio[].class);
        this.libros = GSON.fromJson(new FileReader("libros.json"), Libro[].class);
    }

    /**
     * Guarda los arreglos libros y socios en los archivos libros.json y socios.json.
     *
     * @throws IOException en caso de algun error.
     */
    private void guardarInformacion() throws IOException {
        // Se guardan los socios.
        try (FileWriter writer = new FileWriter("socios.json")) {
            GSON.toJson(this.socios, writer);
        }

        // Se guardan los libros.
        try (FileWriter writer = new FileWriter("libros.json")) {
            GSON.toJson(this.libros, writer);
        }
    }

    /**
     * Método (obtenerDatosSocioLogeado()):
     * Devuelve los datos del socio que está loggeado (nombre completo + correo electrónico).
     *
     * @return el nombre completo del socio, más su correo electrónico.
     */

    public String obtenerDatosSocioLogeado() {
        if (this.socio == null) {
            throw new IllegalArgumentException("No hay un Socio logeado");
        }

        return "Nombre: " + this.socio.getNombreCompleto() + "\n"
                + "Correo Electronico: " + this.socio.getCorreoElectronico();
    }

    /**
     * Método buscarSocio:
     * Recorriendo todos los socios que están en el sistema, se busca el socio
     * que tenga el número de socio que llega desde el parámetro
     *
     * @param numeroDeSocio del socio en cuestión, para poder identificarlo (asumiendo que los numeros de socio son únicos).
     * @return el socio como tal, null en caso que no se encuentre (no exista).
     */
    public Socio buscarSocio(int numeroDeSocio) {
        for (int i = 0; i < socios.length; i++) {
            if (numeroDeSocio == socios[i].getNumeroDeSocio()) {
                return socios[i];
            }
        }
        return null;
    }

    /**
     * Método verificarContraseniaSistema:
     * Permite verificar si la contraseña del socio coincide con la contraseña ingresada al momento de loggear.
     *
     * @param contrasenia del socio (ingresada)
     * @return true si la contraseña coincide, false en caso de que no.
     */

    public boolean verificarContraseniaSistema(String contrasenia, Socio socio1) {
        String contraseniaDelSocio = socio1.getContrasenia();
        if (contrasenia.equals(contraseniaDelSocio)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Método loggearSocio:
     * Le da el estado de socio al socio, cuando se inicia sesión.
     * El socio pasa a ser de null, a otro que esté en el arreglo de socios.
     * En el caso que no se ingrese un socio que existe, o que la contraseña esté equivocada, se despliega un error.
     *
     * @param socio1              (el socio que llega desde afuera, luego de buscarlo con el método buscar socio).
     * @param contraseniaCorrecta (variable que demuestra si la contraseña es correcta (true = sí, false = no)).
     * @throws Exception
     */

    public void loggearSocio(Socio socio1, boolean contraseniaCorrecta) throws Exception {
        if (socio1 != null && contraseniaCorrecta) {
            this.socio = socio1;
        } else {
            throw new IllegalArgumentException("El socio no existe o la contraseña es incorrecta. Intente de nuevo.");
        }
    }

    /*
    IMPORTANTE: en el sistema hay varios métodos que llaman a otros métodos en las otras clases. Por ejemplo,
    que desde el main se llame al sistema, y luego del sistema se llame a un libro o socio. Esto se debe que, como
    no se puede acceder a los métodos del socio o el libro directamente desde el main, se llama primero al sistema, y desde ahí
    se llama a las otras clases. A este tipo de métodos, se denominarán "intermediarios", y en el caso que se encuentre
    un método intermediario en el sistema, se explicará a fondo el método que se esté llamando en la otra clase.
     */


    /**
     * Método (cambioDeContrasenia):
     * Llama el método cambioDeContrasenia de la clase socio (método intermediario).
     * Luego se guarda la información en los archivos.
     *
     * @param contrasenia (la contraseña nueva).
     * @throws IOException
     */
    public void cambioDeContrasenia(String contrasenia) throws IOException {
        socio.cambioDeContrasenia(contrasenia);
        this.guardarInformacion();
    }

    /**
     * Método (cambioDeCorreo):
     * Llama el método cambioDeCorreo de la clase socio (método intermediario).
     * Luego se guarda la información en los archivos.
     *
     * @param correo (el correo nuevo)
     * @throws IOException
     */

    public void cambioDeCorreo(String correo) throws IOException {
        socio.cambioDeCorreo(correo);
        this.guardarInformacion();
    }

    /**
     * Método verificarCorreo
     * Permite verificar si el correo del socio loggeado coincide con el correo ingresado.
     *
     * @param correo (correo ingresado)
     * @return true si el correo coincide, false en caso de que no.
     */
    public boolean verificarCorreo(String correo) {
        String correoDelSocio = socio.getCorreoElectronico();
        if (correoDelSocio.equals(correo)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Método verificarNombre
     * Permite verificar si el nombre del socio coincide con el nombre ingresado.
     *
     * @param nombre (nombre ingresado)
     * @return true si el nombre coincide, false en caso de que no.
     */
    public boolean verificarNombre(String nombre) {
        String nombreDelSocio = socio.getNombre();
        if (nombre.equals(nombreDelSocio)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Método (cambioDeNombre):
     * Llama el método cambioDeNombre de la clase socio (método intermediario).
     * Luego se guarda la información en los archivos.
     *
     * @param nombre (el nombre nuevo)
     * @throws IOException (excepción)
     */
    public void cambioDeNombre(String nombre) throws IOException {
        socio.cambioDeNombre(nombre);
        this.guardarInformacion();
    }

    /**
     * Método verificarApellido
     * Permite verificar si el apellido del socio coincide con el apellido ingresado.
     *
     * @param apellido (apellido ingresado)
     * @return true si el apellido coincide, false en caso de que no.
     */
    public boolean verificarApellido(String apellido) {
        String apellidoDelSocio = socio.getApellido();
        if (apellidoDelSocio.equals(apellido)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Método (cambioDeApellido):
     * Llama el método cambioDeApellido de la clase socio (método intermediario).
     * Luego se guarda la información en los archivos.
     *
     * @param apellido (apellido nuevo)
     * @throws IOException (excepción)
     */
    public void cambioDeApellido(String apellido) throws IOException {
        socio.cambioDeApellido(apellido);
        this.guardarInformacion();
    }

    /**
     * Método obtenerLibro
     * Permite buscar el libro (usando el isbn) en el arreglo de libros.
     *
     * @param isbn (isbn del libro buscado)
     * @return el libro si se encuentra, null si no.
     */
    public Libro obtenerLibro(String isbn) {
        for (int i = 0; i < libros.length; i++) {
            if (isbn.equals(libros[i].getIsbn())) {
                return libros[i];
            }
        }
        return null;
    }

    /**
     * Método (calificarLibro):
     * Llama el método sumarCalificacion de la clase libro (método intermediario).
     * Luego se guarda la información en los archivos.
     *
     * @param libro     (el libro al que se le dará la calificación)
     * @param estrellas (la cantidad de estrellas que tiene la calificación)
     * @throws IOException (la excepción)
     */

    public void calificarLibro(Libro libro, int estrellas) throws IOException {
        libro.sumarCalificacion(estrellas, socio);
        this.guardarInformacion();
    }

    /**
     * Método cantidadLibrosSocio (método intermediario)
     *
     * @return la cantidad de libros actual del socio, desde el método getCantidadLibros
     */

    public int cantidadLibrosSocio() {
        return socio.getCantidadLibros();
    }

    /**
     * Método cantidadMaximaLibrosSocio (método intermediario)
     *
     * @return la cantidad máxima de libros del socio, desde el método getCantidadMaximaLibros
     */
    public int cantidadMaximaLibrosSocio() {
        return socio.getCantidadMaximaLibros();
    }

    /**
     * Método obtenerLibrosSocio (método intermediario)
     *
     * @return String que conforma todos los libros en préstamo del socio.
     */

    public String obtenerLibrosSocio() {
        return socio.obtenerLibrosEnPrestamo();
    }

    /**
     * Método realizarDevolucionLibro
     * Permite devolver el libro de un socio.
     * Primero se busca si existe el libro que se quiere devolver, y luego se envía
     * al método devolverLibro del socio. Luego se guarda la información.
     *
     * @param isbn del libro que se devolverá.
     * @throws IOException (excepción)
     */

    public void realizarDevolucionLibro(String isbn) throws IOException {
        if (this.socio == null) {
            throw new IllegalArgumentException("Socio no se ha logeado!");
        }

        Libro libro = this.buscarLibro(isbn);
        if (libro == null) {
            throw new IllegalArgumentException("Libro con isbn " + isbn + " no existe o no se encuentra disponible.");
        }

        this.socio.devolverLibro(libro);
        this.guardarInformacion();
    }

    /**
     * Método verificarContraseniaSocioLoggeado
     * Hace lo mismo que el método de verificarContraseniaSistema, pero esta vez
     * haciendo referencia al socio en cuestión (en el otro método, el socio es null).
     *
     * @param contrasenia ingresada
     * @return true si coincide, false si no.
     */

    public boolean verificarContraseniaSocioLoggeado(String contrasenia) {
        String contraseniaDelSocio = socio.getContrasenia();
        if (contrasenia.equals(contraseniaDelSocio)) {
            return true;
        } else {
            return false;
        }
    }
}