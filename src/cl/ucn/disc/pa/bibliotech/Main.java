/*
 * Copyright (c) 2023. Programacion Avanzada, DISC, UCN.
 */

package cl.ucn.disc.pa.bibliotech;

import cl.ucn.disc.pa.bibliotech.model.Libro;
import cl.ucn.disc.pa.bibliotech.services.Sistema;
import cl.ucn.disc.pa.bibliotech.services.Utils;
import edu.princeton.cs.stdlib.StdIn;
import edu.princeton.cs.stdlib.StdOut;

import java.io.IOException;
import java.util.Objects;

/**
 * The Main.
 * Editado por BENJAMÍN RIVERA PORTILLA Y JUANJOSÉ AROCA BUGUEÑO.
 *
 * @author Programacion Avanzada.
 */
public final class Main {

    /**
     * The main.
     *
     * @param args to use.
     * @throws IOException en caso de un error.
     */
    public static void main(final String[] args) throws Exception {

        // Se inicia el sistema.
        Sistema sistema = new Sistema();

        //StdOut.println(sistema.obtegerCatalogoLibros());


        //Al momento de iniciar la aplicación, no hay socio loggeado.
        //Se inicia sesión (1), o se cierra la aplicación (2).

        String opcion = null;
        while (!Objects.equals(opcion, "2")) {

            StdOut.println("""
                    [*] Bienvenido a BiblioTech [*]
                                    
                    [1] Iniciar Sesion
                    [2] Salir
                    """);
            StdOut.print("Escoja una opcion: ");
            opcion = StdIn.readString();

            switch (opcion) {
                case "1" -> iniciarSesion(sistema);
                case "2" -> StdOut.println("¡Hasta Pronto!");
                default -> StdOut.println("Opcion no valida, intente nuevamente");
            }
        }
    }

    /**
     * Inicia la sesion del Socio en el Sistema.
     *
     * @param sistema a utilizar.
     */
    private static void iniciarSesion(final Sistema sistema) throws Exception {
        StdOut.println("[*] Iniciar sesion en BiblioTech [*]");
        int numeroSocio;

        //Se intenta ingresar el número de socio (variable int),
        //y mientras no se recibe un número entero (captura de excepción),
        //no se puede continuar para ingresar contraseña.

        while (true) {
            try {
                StdOut.print("Ingrese su numero de socio: ");
                numeroSocio = StdIn.readInt();
                StdIn.readLine();
                break;
            } catch (Exception exception) {
                StdOut.println("Número de socio no válido. Intente de nuevo.");
            }
        }

        StdOut.print("Ingrese su contrasenia: ");
        String contrasenia = StdIn.readString();

        // Se intenta el inicio de sesión.
        try {
            sistema.iniciarSession(numeroSocio, contrasenia);
        } catch (Exception ex) {
            StdOut.println("Ocurrio un error: " + ex.getMessage());
            return;
        }

        // Se muestra el menú principal
        menuPrincipal(sistema);
    }

    private static void menuPrincipal(final Sistema sistema) throws Exception {
        //Mientras no se elija la opción 5 (cerrar sesión), el programa continúa.
        String opcion = null;
        while (!Objects.equals(opcion, "5")) {
            StdOut.println("""
                    [*] BiblioTech [*]
                                        
                    [1] Prestamo de un libro
                    [2] Editar información
                    [3] Calificar libro
                    [4] Devolver un libro
                                        
                                        
                    [5] Cerrar sesion
                    """);

            StdOut.print("Escoja una opcion: ");
            opcion = StdIn.readString();

            switch (opcion) {
                case "1" -> menuPrestamo(sistema);
                case "2" -> editarInformacion(sistema);
                case "3" -> agregarCalificacion(sistema);
                case "4" -> devolverLibro(sistema);
                case "5" -> sistema.cerrarSession();
                default -> StdOut.println("Opcion no valida, intente nuevamente");
            }
        }
    }

    private static void menuPrestamo(Sistema sistema) throws Exception {
        //Préstamo de libro.
        StdOut.println("[*] Préstamo de un Libro [*]");
        String isbn;

        //En caso que la cantidad actual de libros prestados del socio sea menor a su límite,
        //se puede prestar más.

        if (sistema.cantidadLibrosSocio() < sistema.cantidadMaximaLibrosSocio()) {
            while (true) {
                //Se despliega el catálogo
                try {
                    if (sistema.obtegerCatalogoLibros(0) != null) {
                        StdOut.println(sistema.obtegerCatalogoLibros(0));
                    } else {
                        throw new Exception();
                    }
                } catch (Exception exception) {
                    //Si no hay libros disponibles, se tira una excepción y se regresa al menú principal.
                    StdOut.println("Atención: Todos los libros están prestados");
                    break;
                }

                StdOut.print("Ingrese el ISBN del libro a tomar prestado: ");
                isbn = StdIn.readString();
                try {
                    //Se realiza el préstamo con el ISBN requerido
                    sistema.realizarPrestamoLibro(isbn);
                    StdOut.println("Préstamo realizado con éxito.");
                    break;
                } catch (Exception exception) {
                    //En caso que se cause una excepción (el libro no exista), se despliega mensaje de error.
                    StdOut.println("Error: El libro no existe.");
                    break;
                }
            }
        } else {
            StdOut.println("Error: Cantidad máxima de préstamos alcanzada. Cantidad máxima: " + sistema.cantidadMaximaLibrosSocio());
        }
    }

    private static void editarInformacion(Sistema sistema) throws IOException {
        //Se accede al menú de editar información. Mientras no se ingrese 5,
        //se continuará en este menú.

        String opcion = null;
        while (!Objects.equals(opcion, "5")) {

            StdOut.println("[*] Editar Perfil [*]");
            StdOut.println(sistema.obtenerDatosSocioLogeado());
            StdOut.println("""               
                    [1] Editar Correo Electrónico
                    [2] Editar Contraseña
                    [3] Editar Nombre
                    [4] Editar Apellido
                                        
                    [5] Volver atrás
                    """);
            StdOut.print("Escoja una opción: ");
            opcion = StdIn.readString();

            switch (opcion) {
                case "1" -> editarCorreo(sistema);
                case "2" -> cambiarContrasenia(sistema);
                case "3" -> editarNombreOApellido(sistema, 1);
                case "4" -> editarNombreOApellido(sistema, 2);
                case "5" -> StdOut.println("Volviendo al menú anterior...");
                default -> StdOut.println("Opcion no valida, intente nuevamente");
            }
        }
    }

    private static void editarNombreOApellido(Sistema sistema, int nombre_o_apellido) throws IOException {
        //Este método funciona de igual forma pero para 2 términos diferentes.
        //Se ingresa el valor actual, y el valor nuevo (nombre o apellido).

        String valor_actual;
        String valor_nuevo;

        while (true) {
            if (nombre_o_apellido == 1) {
                StdOut.println("Ingrese su nombre actual: ");
                valor_actual = StdIn.readString();

                //Si el nombre ingresado no coincide con el nombre actual del socio,
                //se pide que se ingrese de nuevo.

                if (sistema.verificarNombre(valor_actual)) {
                    StdOut.println("Ingrese el nuevo nombre.");
                    valor_actual = StdIn.readString();

                    while (true) {
                        StdOut.println("Confirme el nuevo nombre.");
                        valor_nuevo = StdIn.readString();

                        //Si la confirmación del nuevo nombre (segundo ingreso), no coincide
                        //exactamente con el primer ingreso, se pedirá que se ingrese correctamente.

                        if (valor_nuevo.equals(valor_actual)) {
                            //El sistema realiza el cambio de nombre.
                            sistema.cambioDeNombre(valor_nuevo);
                            break;
                        } else {
                            StdOut.println("El nombre a confirmar no es el mismo al ingresado anteriormente. Intente de nuevo.");
                        }
                    }
                    break;
                } else {
                    StdOut.println("El nombre es incorrecto. Intente de nuevo.");
                }
            } else if (nombre_o_apellido == 2) {

                //Lo mismo sucede con el apellido. Se pide ingresar el apellido actual,
                //mientras no se ingrese correctamente se seguirá pidiendo.

                StdOut.println("Ingrese su apellido actual: ");
                valor_actual = StdIn.readString();

                if (sistema.verificarApellido(valor_actual)) {
                    StdOut.println("Ingrese el nuevo apellido.");
                    valor_actual = StdIn.readString();

                    while (true) {
                        StdOut.println("Confirme el nuevo apellido.");
                        valor_nuevo = StdIn.readString();

                        //Si la confirmación no es igual que el primer ingreso, se
                        //pide que se ingrese correctamente.

                        if (valor_nuevo.equals(valor_actual)) {
                            sistema.cambioDeApellido(valor_nuevo);
                            break;
                        } else {
                            StdOut.println("El apellido a confirmar no es el mismo al ingresado anteriormente. Intente de nuevo.");
                        }
                    }
                    break;
                } else {
                    StdOut.println("El apellido es incorrecto. Intente de nuevo.");
                }
            }
        }
    }

    private static void cambiarContrasenia(Sistema sistema) throws IOException {
        while (true) {
            StdOut.println("Ingrese su contraseña actual: ");
            String contrasenia = StdIn.readString();

            //Si la contraseña coincide, se pide ingreso de la nueva y se confirma.

            if (sistema.verificarContraseniaSocioLoggeado(contrasenia)) {
                StdOut.println("Ingrese la nueva contraseña.");
                contrasenia = StdIn.readString();

                while (true) {
                    StdOut.println("Confirme la nueva contraseña.");
                    String nueva_contrasenia = StdIn.readString();

                    if (contrasenia.equals(nueva_contrasenia)) {
                        sistema.cambioDeContrasenia(nueva_contrasenia);
                        break;
                    } else {
                        StdOut.println("Las contraseñas ingresadas no coinciden. Intente de nuevo.");
                    }
                }
                break;
            } else {
                StdOut.println("La contraseña es incorrecta. Intente de nuevo.");
            }
        }
    }

    private static void editarCorreo(Sistema sistema) throws IOException {
        while (true) {
            String correo;
            String nuevo_correo;

            while (true) {
                StdOut.println("Ingrese su correo actual: ");
                try {
                    correo = StdIn.readString();
                    //Se realiza una validación extra (venía por defecto en el código base)
                    //Se valida si el correo está estructuralmente bien.
                    Utils.validarEmail(correo);
                    break;
                } catch (Exception exception) {
                    //Si no está estructuralmente correcto, se tira una excepción y se captura.
                    StdOut.println("Error: Correo inválido. Intente de nuevo.");
                }
            }

            if (sistema.verificarCorreo(correo)) {

                //Se ingresa el correo nuevo, y se confirma.
                while (true) {
                    StdOut.println("Ingrese el nuevo correo.");
                    try {
                        correo = StdIn.readString();
                        Utils.validarEmail(correo);
                        break;
                    } catch (Exception exception) {
                        StdOut.println("Error: Correo inválido. Intente de nuevo.");
                    }
                }

                while (true) {
                    StdOut.println("Confirme el nuevo correo.");
                    try {
                        nuevo_correo = StdIn.readString();
                        Utils.validarEmail(nuevo_correo);

                        if (nuevo_correo.equalsIgnoreCase(correo)) {
                            break;
                        } else {
                            StdOut.println("El correo a confirmar no es el mismo al ingresado anteriormente. Intente de nuevo.");
                        }
                    } catch (Exception exception) {
                        StdOut.println("Error: Correo inválido. Intente de nuevo.");
                    }
                }
                //Se realiza el cambio de correo.
                sistema.cambioDeCorreo(nuevo_correo);
                break;
            } else {
                StdOut.println("El correo es incorrecto. Intente de nuevo.");
            }
        }
    }

    private static void agregarCalificacion(Sistema sistema) throws IOException {
        String isbn;
        //Se imprime todo el registro de libros que hay en el sistema.

        while (true) {
            try {
                if (sistema.obtegerCatalogoLibros(1) != null) {
                    StdOut.println(sistema.obtegerCatalogoLibros(1));
                } else {
                    throw new Exception();
                }
            } catch (Exception exception) {
                StdOut.println("Atención: No hay libros en el sistema.");
                break;
            }

            Libro libro;

            //Se ingresa el isbn del libro. Se busca, y en caso que no exista, se captura la excepción.

            try {
                StdOut.print("Ingrese el ISBN del libro a calificar: ");
                isbn = StdIn.readString();
                libro = sistema.obtenerLibro(isbn);
            } catch (Exception exception) {
                StdOut.println("El libro no está disponible o no existe.");
                break;
            }

            if (libro == null) {
                StdOut.println("El libro no existe o no está disponible, intente de nuevo.");
                break;
            }

            int estrellas;
            while (true) {
                try {
                    //Se pide la cantidad de estrellas (entre 0 y 5, valor entero).
                    StdOut.println("Ingrese la cantidad de estrellas, de 0 a 5 estrellas.");
                    estrellas = StdIn.readInt();
                    while (estrellas < 0 || estrellas > 5) {
                        StdOut.println("Cantidad inválida de estrellas, intente de nuevo.");
                        estrellas = StdIn.readInt();
                    }
                    break;
                } catch (Exception exception) {
                    StdOut.println("Cantidad inválida de estrellas, intente de nuevo.");
                }
            }
            //Se agrega la calificación al libro, con su cantidad de estrellas correspondiente.
            try {
                sistema.calificarLibro(libro, estrellas);
                StdOut.println("Calificación exitosa.");
                StdOut.println("Calificación actual del libro: " + libro.getCalificacion());
            } catch (Exception exception) {
                StdOut.println("Ha ocurrido un error. Intente de nuevo.");
            }
            break;
        }
    }

    private static void devolverLibro(Sistema sistema) throws IOException {
        StdOut.println("[*] Devolver un Libro [*]");
        String isbn;

        //Si la cantidad actual de libros del socio es mayor a 1, se permite devolver un libro.
        //En caso contrario, no se podrá.

        if (sistema.cantidadLibrosSocio() > 0) {
            while (true) {
                try {
                    //Se obtiene el catálogo de los libros prestados por el socio.
                    StdOut.println(sistema.obtenerLibrosSocio());
                } catch (Exception exception) {
                    StdOut.println("Atención: el socio no tiene libros prestados.");
                }

                StdOut.print("Ingrese el ISBN del libro a devolver: ");
                isbn = StdIn.readString();


                try {
                    //Se realiza la devolución del libro por parte del socio.
                    sistema.realizarDevolucionLibro(isbn);
                    StdOut.println("Devolución de libro completada.");
                    break;
                } catch (Exception exception) {
                    StdOut.println("Error: El libro no existe entre los prestados por el usuario.");
                    break;
                }
            }
        } else {
            StdOut.println("Atención: el socio no tiene libros en préstamo.");
        }
    }

}