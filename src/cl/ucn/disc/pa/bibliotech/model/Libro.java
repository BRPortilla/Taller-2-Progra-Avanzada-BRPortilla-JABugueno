/*
 * Copyright (c) 2023. Programacion Avanzada, DISC, UCN.
 */

package cl.ucn.disc.pa.bibliotech.model;

/**
 * Clase que representa un Libro.
 *
 * @author Programacion Avanzada.
 */
public final class Libro {

    /**
     * The ISBN.
     */
    private String isbn;

    /**
     * The Titulo.
     */
    private String titulo;

    /**
     * The Author.
     */
    private String autor;

    /**
     * The Categoria
     */
    private String categoria;


    /**
     * The Lista de Calificaciones
     */
    private Calificacion[] calificaciones;

    /**
     * La cantidad de calificaciones
     */
    private int cantidadCalificaciones;

    /**
     * Si el libro está prestado (true), o no (false)
     */
    private boolean prestado;

    /**
     * The Constructor.
     *
     * @param isbn      del libro.
     * @param titulo    del libro.
     * @param autor     del libro
     * @param categoria del libro.
     */
    public Libro(final String isbn, final String titulo, final String autor, final String categoria) {
        //Validaciones de todos los atributos del libro.

        if (isbn == null || isbn.length() == 0) {
            throw new IllegalArgumentException("ISBN no válido.");
        }
        this.isbn = isbn;

        if (titulo == null || titulo.length() == 0) {
            throw new IllegalArgumentException("Título no válido.");
        }
        this.titulo = titulo;

        if (autor == null || autor.length() == 0) {
            throw new IllegalArgumentException("Autor no válido.");
        }
        this.autor = autor;

        if (categoria == null || categoria.length() == 0) {
            throw new IllegalArgumentException("Categoría no válida.");
        }

        //Un libro siempre empieza con 0 calificaciones, y no está prestado.
        cantidadCalificaciones = 0;
        this.categoria = categoria;
        this.prestado = false;
    }

    /**
     * @return the ISBN.
     */
    public String getIsbn() {
        return this.isbn;
    }

    /**
     * @return the titulo.
     */
    public String getTitulo() {
        return this.titulo;
    }

    /**
     * @return the autor.
     */
    public String getAutor() {
        return this.autor;
    }

    /**
     * @return the categoria.
     */
    public String getCategoria() {
        return this.categoria;
    }

    /**
     * Método sumarCalificacion
     * Permite añadir o editar una calificacion hecha por un socio. Se toma en consideración el número de socio (asumiendo que es único).
     * Si la lista de calificaciones es nula, se crea una nueva sobredimensionada. La variable existeCalificacion siempre es falsa al comienzo,
     * y sirve para detectar si ya existe una calificación del socio al libro en cuestión. En caso que el número de socio de la califiación
     * coincida con el del socio que llega de afuera, se vuelve true, y por ende, se sobreescribe la calificación. Si no existe califiación
     * luego del ciclo, entonces se añade la calificación (cantidad + 1), con la cantidad de estrellas y el número de socio correspondiente.
     *
     * @param estrellas (cantidad de estrellas).
     * @param socio     (el socio que hace la calificación).
     */
    public void sumarCalificacion(int estrellas, Socio socio) {
        boolean existeCalificacion = false;

        int numeroDeSocio = socio.getNumeroDeSocio();

        if (this.calificaciones == null) {
            this.calificaciones = new Calificacion[100];
        }

        while (!existeCalificacion) {
            for (int i = 0; i < cantidadCalificaciones; i++) {
                if ((calificaciones[i].getNumeroDeSocio()) == numeroDeSocio) {
                    calificaciones[i].setEstrellas(estrellas);
                    calificaciones[i].setNumeroDeSocio(numeroDeSocio);
                    existeCalificacion = true;
                    break;
                }
            }

            if (!existeCalificacion) {
                Calificacion calificacion = new Calificacion(estrellas, numeroDeSocio);
                this.calificaciones[cantidadCalificaciones] = calificacion;
                cantidadCalificaciones++;
                existeCalificacion = true;
                break;
            }
        }
    }

    /**
     * Método getCalificación
     * Se cuentan todas las estrellas entre todas las calificaciones no nulas, luego se cuentan todos
     * los entre las calificaciones no nulas los socios que han hecho una opinión. Si la cantidad de opiniones
     * es mayor a 0, se retorna el promedio. O sino, se retorna 0 al no haber calificaciones.
     *
     * @return el promedio total entre todas las califiaciones, por parte de los usuarios.
     */
    public double getCalificacion() {
        double sumaCalificacion = 0;
        int cantidad = 0;

        for (int i = 0; i < calificaciones.length; i++) {
            if (calificaciones[i] != null) {
                sumaCalificacion += calificaciones[i].getEstrellas();
            }
        }

        for (int i = 0; i < this.calificaciones.length; i++) {
            if (calificaciones[i] != null && calificaciones[i].getNumeroDeSocio() != 0) {
                cantidad++;
            }
        }

        if (cantidad > 0) {
            return (sumaCalificacion / cantidad);
        } else {
            return 0;
        }
    }

    /**
     * Método setPrestado
     *
     * @param prestado nuevo estado del libro (true/false).
     */
    public void setPrestado(boolean prestado) {
        this.prestado = prestado;
    }

    /**
     * Método getPrestado
     *
     * @return el estado actual del libro (true = prestado, false = desocupado).
     */
    public boolean getPrestado() {
        return prestado;
    }
}