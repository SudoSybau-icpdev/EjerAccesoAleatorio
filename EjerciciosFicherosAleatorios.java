package FicherosAleatorios;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class EjerciciosFicherosAleatorios {

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        ejercicio1();
        ejercicio2();
        ejercicio3();
        ejercicio4();
        ejercicio5();
        ejercicio6();
        ejercicio7();
        sc.close();
    }

    // ==================== EJERCICIO 1 ====================
    public static void ejercicio1() {
        String archivo = "enteros.dat";
        try (RandomAccessFile raf = new RandomAccessFile(archivo, "rw")) {
            // setLength(0) borra el contenido previo truncando el archivo a 0 bytes
            raf.setLength(0);
            for (int i = 0; i < 6; i++) {
                raf.writeInt(1);
            }
            for (int i = 0; i < 10; i++) {
                raf.writeInt(2);
            }

            System.out.println("--- Ejercicio 1 ---");
            System.out.print("Contenido inicial de enteros.dat: ");
            mostrarEnteros(raf);

            System.out.print("Introduce un número (0-9) para sustituir los 2: ");
            int nuevoValor = sc.nextInt();

            long numRegistros = raf.length() / 4;
            for (int i = 0; i < numRegistros; i++) {
                // seek() desplaza el puntero al byte exacto (cada entero ocupa 4 bytes)
                raf.seek((long) i * 4);
                int val = raf.readInt();
                if (val == 2) {
                    raf.seek((long) i * 4);
                    raf.writeInt(nuevoValor);
                }
            }

            System.out.print("Contenido modificado de enteros.dat: ");
            mostrarEnteros(raf);
            System.out.println();
        } catch (IOException e) {
            System.err.println("Error en Ejercicio 1: " + e.getMessage());
        }
    }

    private static void mostrarEnteros(RandomAccessFile raf) throws IOException {
        long total = raf.length() / 4;
        for (int i = 0; i < total; i++) {
            raf.seek((long) i * 4);
            System.out.print(raf.readInt() + " ");
        }
        System.out.println();
    }

    // ==================== EJERCICIO 2 ====================
    public static void ejercicio2() {
        String archivo = "enteros.dat";
        System.out.println("--- Ejercicio 2 ---");
        System.out.print("Introduce un número (0-9) a añadir: ");
        int numero = sc.nextInt();
        System.out.print("Introduce cuántas veces añadirlo: ");
        int cantidad = sc.nextInt();

        try (RandomAccessFile raf = new RandomAccessFile(archivo, "rw")) {
            // raf.length() sitúa el puntero al final del fichero para no borrar los datos anteriores
            raf.seek(raf.length());
            for (int i = 0; i < cantidad; i++) {
                raf.writeInt(numero);
            }

            System.out.print("Contenido final de enteros.dat: ");
            mostrarEnteros(raf);
            System.out.println();
        } catch (IOException e) {
            System.err.println("Error en Ejercicio 2: " + e.getMessage());
        }
    }

    // ==================== EJERCICIO 3 ====================
    public static void ejercicio3() {
        String archivo = "juegos.dat";
        System.out.println("--- Ejercicio 3 ---");
        System.out.print("¿Cuántos juegos vas a añadir?: ");
        int numJuegos = sc.nextInt();
        sc.nextLine();

        try (RandomAccessFile raf = new RandomAccessFile(archivo, "rw")) {
            raf.seek(raf.length());

            for (int i = 1; i <= numJuegos; i++) {
                System.out.println("Juego #" + i + ":");
                System.out.print("Número de orden: ");
                int orden = sc.nextInt();
                sc.nextLine();

                System.out.print("Nombre (máx 10 chars): ");
                String nombre = sc.nextLine();

                System.out.print("Edad mínima recomendada: ");
                int edad = sc.nextInt();
                sc.nextLine();

                System.out.print("Temática (máx 10 chars): ");
                String tematica = sc.nextLine();

                raf.writeInt(orden);
                escribirCadenaFija(raf, nombre, 10);
                raf.writeInt(edad);
                escribirCadenaFija(raf, tematica, 10);
            }

            System.out.println("\n--- Contenido de juegos.dat ---");
            raf.seek(0);
            // Tamaño por registro = orden (4B) + nombre (20B) + edad (4B) + temática (20B) = 48 bytes
            long numRegistros = raf.length() / 48;
            for (int i = 0; i < numRegistros; i++) {
                int orden = raf.readInt();
                String nombre = leerCadenaFija(raf, 10);
                int edad = raf.readInt();
                String tematica = leerCadenaFija(raf, 10);
                System.out.printf("Orden: %d | Nombre: %-10s | Edad Mín: %2d | Temática: %-10s%n",
                        orden, nombre, edad, tematica);
            }
            System.out.println();
        } catch (IOException e) {
            System.err.println("Error en Ejercicio 3: " + e.getMessage());
        }
    }

    // ==================== EJERCICIO 4 ====================
    public static void ejercicio4() {
        String archivo = "juegos.dat";
        System.out.println("--- Ejercicio 4 ---");
        System.out.print("Introduce la edad para buscar juegos recomendados: ");
        int edadBuscada = sc.nextInt();

        try (RandomAccessFile raf = new RandomAccessFile(archivo, "r")) {
            long numRegistros = raf.length() / 48;
            System.out.println("Juegos recomendados para " + edadBuscada + " años o menos:");
            boolean encontrado = false;

            for (int i = 0; i < numRegistros; i++) {
                raf.seek((long) i * 48);
                int orden = raf.readInt();
                String nombre = leerCadenaFija(raf, 10);
                int edadMinima = raf.readInt();
                String tematica = leerCadenaFija(raf, 10);

                if (edadMinima <= edadBuscada) {
                    System.out.printf("Nombre: %-10s | Temática: %-10s (Edad mín: %d)%n",
                            nombre, tematica, edadMinima);
                    encontrado = true;
                }
            }

            if (!encontrado) {
                System.out.println("No se encontraron juegos para esa edad.");
            }
            System.out.println();
        } catch (IOException e) {
            System.err.println("Error en Ejercicio 4: " + e.getMessage());
        }
    }

    // ==================== EJERCICIO 5 ====================
    public static void ejercicio5() {
        String archivo = "practicas.dat";
        System.out.println("--- Ejercicio 5 ---");
        System.out.print("¿Cuántos alumnos deseas guardar?: ");
        int numAlumnos = sc.nextInt();
        sc.nextLine();

        try (RandomAccessFile raf = new RandomAccessFile(archivo, "rw")) {
            raf.seek(raf.length());

            for (int i = 1; i <= numAlumnos; i++) {
                System.out.println("Alumno #" + i + ":");
                System.out.print("Nombre (máx 10 chars): ");
                String nombre = sc.nextLine();

                System.out.print("Apellidos (máx 35 chars): ");
                String apellidos = sc.nextLine();

                System.out.print("DNI (9 chars): ");
                String dni = sc.nextLine();

                System.out.print("Vehículo de prácticas (2 dígitos): ");
                int vehiculo = sc.nextInt();

                System.out.print("Número de clases de prácticas: ");
                int clases = sc.nextInt();
                sc.nextLine();

                escribirCadenaFija(raf, nombre, 10);
                escribirCadenaFija(raf, apellidos, 35);
                escribirCadenaFija(raf, dni, 9);
                raf.writeInt(vehiculo);
                raf.writeInt(clases);
            }

            System.out.println("\n--- Contenido de practicas.dat ---");
            raf.seek(0);
            // Tamaño por registro = nombre (20B) + apellidos (70B) + dni (18B) + vehiculo (4B) + clases (4B) = 116 bytes
            long numRegistros = raf.length() / 116;
            for (int i = 0; i < numRegistros; i++) {
                String nombre = leerCadenaFija(raf, 10);
                String apellidos = leerCadenaFija(raf, 35);
                String dni = leerCadenaFija(raf, 9);
                int vehiculo = raf.readInt();
                int clases = raf.readInt();
                System.out.printf("Nombre: %-10s | Apellidos: %-35s | DNI: %-9s | Vehículo: %02d | Clases: %d%n",
                        nombre, apellidos, dni, vehiculo, clases);
            }
            System.out.println();
        } catch (IOException e) {
            System.err.println("Error en Ejercicio 5: " + e.getMessage());
        }
    }

    // ==================== EJERCICIO 6 ====================
    public static void ejercicio6() {
        String origen = "practicas.dat";
        String destino = "precio.dat";
        System.out.println("--- Ejercicio 6 ---");

        try (RandomAccessFile rafOrigen = new RandomAccessFile(origen, "r");
             RandomAccessFile rafDestino = new RandomAccessFile(destino, "rw")) {

            rafDestino.setLength(0);
            long numRegistros = rafOrigen.length() / 116;

            for (int i = 0; i < numRegistros; i++) {
                rafOrigen.seek((long) i * 116);
                String nombre = leerCadenaFija(rafOrigen, 10);
                String apellidos = leerCadenaFija(rafOrigen, 35);
                String dni = leerCadenaFija(rafOrigen, 9);
                int vehiculo = rafOrigen.readInt();
                int clases = rafOrigen.readInt();

                double totalPagar = clases * 45.7;

                escribirCadenaFija(rafDestino, nombre, 10);
                escribirCadenaFija(rafDestino, dni, 9);
                rafDestino.writeInt(clases);
                rafDestino.writeDouble(totalPagar);
            }

            System.out.println("Fichero precio.dat creado correctamente.");
            System.out.println("--- Contenido de precio.dat ---");
            rafDestino.seek(0);
            // Tamaño por registro = nombre (20B) + dni (18B) + clases (4B) + totalPagar (8B) = 50 bytes
            long numDest = rafDestino.length() / 50;
            for (int i = 0; i < numDest; i++) {
                String nombre = leerCadenaFija(rafDestino, 10);
                String dni = leerCadenaFija(rafDestino, 9);
                int clases = rafDestino.readInt();
                double precio = rafDestino.readDouble();
                System.out.printf("Nombre: %-10s | DNI: %-9s | Clases: %d | Total: %.2f €%n",
                        nombre, dni, clases, precio);
            }
            System.out.println();
        } catch (IOException e) {
            System.err.println("Error en Ejercicio 6: " + e.getMessage());
        }
    }

    // ==================== EJERCICIO 7 ====================
    public static void ejercicio7() {
        String origen = "precio.dat";
        String destino = "descuento.dat";
        System.out.println("--- Ejercicio 7 ---");
        System.out.print("Introduce el % de descuento a aplicar: ");
        double porcentaje = sc.nextDouble();

        try (RandomAccessFile rafOrigen = new RandomAccessFile(origen, "r");
             RandomAccessFile rafDestino = new RandomAccessFile(destino, "rw")) {

            rafDestino.setLength(0);
            long numRegistros = rafOrigen.length() / 50;

            for (int i = 0; i < numRegistros; i++) {
                rafOrigen.seek((long) i * 50);
                String nombre = leerCadenaFija(rafOrigen, 10);
                String dni = leerCadenaFija(rafOrigen, 9);
                int clases = rafOrigen.readInt();
                double precioAnterior = rafOrigen.readDouble();

                double precioDescuento = precioAnterior * (1.0 - (porcentaje / 100.0));

                escribirCadenaFija(rafDestino, dni, 9);
                rafDestino.writeDouble(precioAnterior);
                rafDestino.writeDouble(precioDescuento);
            }

            System.out.println("\n--- Contenido de descuento.dat ---");
            rafDestino.seek(0);
            // Tamaño por registro = dni (18B) + precioAnterior (8B) + precioDescuento (8B) = 34 bytes
            long numDest = rafDestino.length() / 34;
            for (int i = 0; i < numDest; i++) {
                String dni = leerCadenaFija(rafDestino, 9);
                double anterior = rafDestino.readDouble();
                double conDescuento = rafDestino.readDouble();
                System.out.printf("DNI: %-9s | Precio Anterior: %7.2f € | Precio con Descuento (%.1f%%): %7.2f €%n",
                        dni, anterior, porcentaje, conDescuento);
            }
            System.out.println();
        } catch (IOException e) {
            System.err.println("Error en Ejercicio 7: " + e.getMessage());
        }
    }

    // Método auxiliar para escribir cadenas de longitud fija en formato de bytes constante (2 bytes por char)
    private static void escribirCadenaFija(RandomAccessFile raf, String texto, int longitud) throws IOException {
        // StringBuffer.setLength() fuerza a que la cadena mida exactamente 'longitud' caracteres
        StringBuffer sb = new StringBuffer(texto != null ? texto : "");
        sb.setLength(longitud);
        // writeChars() escribe cada carácter en UTF-16 (2 bytes por char), garantizando tamaño fijo en disco
        raf.writeChars(sb.toString());
    }

    // Método auxiliar para leer cadenas fijas de exactamente 'longitud' caracteres
    private static String leerCadenaFija(RandomAccessFile raf, int longitud) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < longitud; i++) {
            // readChar() lee 2 bytes por carácter hasta completar la longitud del campo
            sb.append(raf.readChar());
        }
        // trim() elimina los espacios o caracteres nulos de relleno al recuperar el texto limpio
        return sb.toString().trim();
    }
}