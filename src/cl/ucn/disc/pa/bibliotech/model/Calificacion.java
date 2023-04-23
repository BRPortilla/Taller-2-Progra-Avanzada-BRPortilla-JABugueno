/*
 * Copyright (c) 2023. Programacion Avanzada, DISC, UCN.
 */

package cl.ucn.disc.pa.bibliotech.model;

public class Calificacion {
    /**
     * Cantidad de estrellas de la calificación
     */
    private int estrellas;

    /**
     * El número que identifica a un socio.
     */
    private int numero_de_socio;

    /**
     * Constructor de calificación
     *
     * @param estrellas       de la calificación
     * @param numero_de_socio de quién hace la calificación.
     */
    public Calificacion(int estrellas, int numero_de_socio) {
        this.numero_de_socio = numero_de_socio;
        this.estrellas = estrellas;
    }

    /**
     * Método getNumeroDeSocio
     *
     * @return el numero de socio.
     */
    public int getNumeroDeSocio() {
        return numero_de_socio;
    }

    /**
     * Método getEstrellas
     *
     * @return la cantidad de estrellas
     */
    public int getEstrellas() {
        return estrellas;
    }

    /**
     * Método setNumeroDeSocio
     * Permite designar el número de socio a una calificación
     *
     * @param numero_de_socio que realiza la calificación
     */
    public void setNumeroDeSocio(int numero_de_socio) {
        this.numero_de_socio = numero_de_socio;
    }

    /**
     * Método setEstrellas
     * Permite designar la cantidad de estrellas de la calificación
     *
     * @param estrellas de la calificación
     */

    public void setEstrellas(int estrellas) {
        this.estrellas = estrellas;
    }


}