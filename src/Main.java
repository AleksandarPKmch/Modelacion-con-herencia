import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final Scanner SC = new Scanner(System.in);
    private static final Map<String, Equipo> INVENTARIO = new LinkedHashMap<>();
    private static double ingresosAcumulados = 0;

    public static void main(String[] args) {
        cargarEquiposIniciales();

        while (true) {
            mostrarMenu();
            int opcion = leerEntero("Seleccione una opción: ");
            if (opcion == -1) {
                System.out.println("Entrada inválida. Intente nuevamente.");
                continue;
            }

            switch (opcion) {
                case 1:
                    registrarEquipo();
                    break;
                case 2:
                    consultarInventario();
                    break;
                case 3:
                    cotizarAlquiler();
                    break;
                case 4:
                    confirmarAlquiler();
                    break;
                case 5:
                    registrarDevolucion();
                    break;
                case 6:
                    reporteGeneral();
                    break;
                case 0:
                    System.out.println("Gracias por usar el sistema de EnEscena.");
                    return;
                default:
                    System.out.println("Opción no válida.");
                    break;
            }
        }
    }

    private static void cargarEquiposIniciales() {
        try {
            INVENTARIO.put("P-001", new Proyector("P-001", "Epson", "EB-X06", 180, 3500, true, true));
            INVENTARIO.put("P-002", new Proyector("P-002", "BenQ", "MW560", 210, 3200, false, true));
            INVENTARIO.put("C-001", new Camara("C-001", "Canon", "XA25", 220, 2160, true));
            INVENTARIO.put("C-002", new Camara("C-002", "Sony", "FDR-AX43", 190, 1080, true));
            INVENTARIO.put("S-001", new Sonido("S-001", "JBL", "SRX812P", 200, 1.5, true));
            INVENTARIO.put("S-002", new Sonido("S-002", "Yamaha", "DBR12", 250, 2.0, true));
        } catch (IllegalArgumentException e) {
            System.out.println("No se pudieron inicializar los equipos base: " + e.getMessage());
        }
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("=========================================");
        System.out.println("        ENESCENA - GESTIÓN DE INVENTARIO");
        System.out.println("=========================================");
        System.out.println("1. Registrar equipo");
        System.out.println("2. Consultar inventario");
        System.out.println("3. Cotizar alquiler");
        System.out.println("4. Confirmar alquiler");
        System.out.println("5. Registrar devolución");
        System.out.println("6. Reporte general");
        System.out.println("0. Salir");
        System.out.println("============================================");
    }

    private static void registrarEquipo() {
        System.out.println("\nRegistrar nuevo equipo");
        String categoria = leerTexto("Categoría (Proyector/Camara/Sonido): ").trim();
        if (categoria.isEmpty()) {
            System.out.println("La categoría no puede estar vacía.");
            return;
        }

        String codigo = leerTexto("Código de inventario: ").trim();
        if (codigo.isEmpty()) {
            System.out.println("El código de inventario no puede estar vacío.");
            return;
        }
        if (INVENTARIO.containsKey(codigo)) {
            System.out.println("El código de inventario ya existe. No se puede duplicar.");
            return;
        }

        String marca = leerTexto("Marca: ").trim();
        String modelo = leerTexto("Modelo: ").trim();
        double tarifa = leerDoublePositivo("Tarifa diaria (Q): ");
        if (tarifa == -1) {
            System.out.println("La tarifa debe ser mayor que cero.");
            return;
        }

        try {
            Equipo equipo;
            switch (categoria.toLowerCase(Locale.ROOT)) {
                case "proyector":
                    int lumens = leerEnteroPositivo("Lúmenes: ");
                    boolean wireless = leerBoolean("¿Tiene conectividad inalámbrica? (s/n): ");
                    equipo = new Proyector(codigo, marca, modelo, tarifa, lumens, wireless, true);
                    break;
                case "camara":
                    int resolucion = leerEnteroPositivo("Resolución máxima en píxeles verticales: ");
                    equipo = new Camara(codigo, marca, modelo, tarifa, resolucion, true);
                    break;
                case "sonido":
                    double potencia = leerDoublePositivo("Potencia nominal en kW: ");
                    equipo = new Sonido(codigo, marca, modelo, tarifa, potencia, true);
                    break;
                default:
                    System.out.println("Categoría no válida. Use Proyector, Camara o Sonido.");
                    return;
            }
            INVENTARIO.put(codigo, equipo);
            System.out.println("Equipo registrado correctamente.");
            System.out.println(equipo);
        } catch (IllegalArgumentException e) {
            System.out.println("No se pudo registrar el equipo: " + e.getMessage());
        }
    }

    private static void consultarInventario() {
        System.out.println("\nInventario actual");
        if (INVENTARIO.isEmpty()) {
            System.out.println("No hay equipos registrados.");
            return;
        }

        for (Equipo equipo : INVENTARIO.values()) {
            System.out.println(equipo);
        }
    }

    private static void cotizarAlquiler() {
        System.out.println("\nCotizar alquiler");
        String codigo = leerTexto("Código de inventario: ").trim();
        Equipo equipo = INVENTARIO.get(codigo);
        if (equipo == null) {
            System.out.println("Código inexistente. No existe un equipo con ese inventario.");
            return;
        }

        int dias = leerEnteroPositivo("Cantidad de días: ");
        if (dias == -1) {
            System.out.println("La cantidad de días debe ser un entero positivo.");
            return;
        }

        double base = equipo.getTarifaDiaria() * dias;
        double extra = equipo.calcularCargoExtra(dias);
        double total = base + extra;

        System.out.println("\nCotización para " + equipo.getCodigoInventario());
        System.out.println("Equipo: " + equipo.getMarca() + " " + equipo.getModelo());
        System.out.println("Disponibilidad: " + (equipo.estaDisponible() ? "Disponible" : "Ocupado"));
        System.out.println("Detalle del cobro:");
        System.out.println("- Tarifa diaria: " + formatearMonto(equipo.getTarifaDiaria()) + " x " + dias + " días = " +
                formatearMonto(base));
        System.out.println("- Cargo extra: " + formatearMonto(extra));
        System.out.println("TOTAL: " + formatearMonto(total));
    }

    private static void confirmarAlquiler() {
        System.out.println("\nConfirmar alquiler");
        String codigo = leerTexto("Código de inventario: ").trim();
        Equipo equipo = INVENTARIO.get(codigo);
        if (equipo == null) {
            System.out.println("Código inexistente. No se puede alquilar un equipo que no existe.");
            return;
        }

        if (!equipo.estaDisponible()) {
            System.out.println("El equipo ya está alquilado y no puede rentarse nuevamente.");
            return;
        }

        int dias = leerEnteroPositivo("Cantidad de días: ");
        if (dias == -1) {
            System.out.println("La cantidad de días debe ser un entero positivo.");
            return;
        }

        double total = equipo.calcularCostoTotal(dias);
        System.out.println("Total a pagar: " + formatearMonto(total));
        String respuesta = leerTexto("¿Desea confirmar el alquiler? (s/n): ").trim().toLowerCase(Locale.ROOT);

        if (!respuesta.equals("s") && !respuesta.equals("si") && !respuesta.equals("y")) {
            System.out.println("Alquiler cancelado. No se registró ningún ingreso ni cambio de estado.");
            return;
        }

        equipo.setDisponible(false);
        ingresosAcumulados += total;
        System.out.println("Alquiler confirmado correctamente.");
        System.out.println("El equipo queda marcado como ocupado.");
    }

    private static void registrarDevolucion() {
        System.out.println("\nRegistrar devolución");
        String codigo = leerTexto("Código de inventario: ").trim();
        Equipo equipo = INVENTARIO.get(codigo);
        if (equipo == null) {
            System.out.println("Código inexistente. No se puede devolver un equipo que no existe.");
            return;
        }

        if (equipo.estaDisponible()) {
            System.out.println("La devolución no se puede registrar porque el equipo ya está disponible.");
            return;
        }

        equipo.setDisponible(true);
        System.out.println("Devolución registrada correctamente. El equipo ya puede alquilarse otra vez.");
    }

    private static void reporteGeneral() {
        System.out.println("\nReporte general");

        int totalEquipos = INVENTARIO.size();
        int disponibles = 0;
        int alquilados = 0;
        int totalProyectores = 0;
        int totalCamaras = 0;
        int totalSonidos = 0;

        for (Equipo equipo : INVENTARIO.values()) {
            if (equipo instanceof Proyector) {
                totalProyectores++;
            } else if (equipo instanceof Camara) {
                totalCamaras++;
            } else if (equipo instanceof Sonido) {
                totalSonidos++;
            }

            if (equipo.estaDisponible()) {
                disponibles++;
            } else {
                alquilados++;
            }
        }

        System.out.println("Total de equipos registrados: " + totalEquipos);
        System.out.println("Total de proyectores: " + totalProyectores + " | Disponibles: " + contarDisponiblesPorTipo(Proyector.class) +
                " | Alquilados: " + contarAlquiladosPorTipo(Proyector.class));
        System.out.println("Total de cámaras: " + totalCamaras + " | Disponibles: " + contarDisponiblesPorTipo(Camara.class) +
                " | Alquilados: " + contarAlquiladosPorTipo(Camara.class));
        System.out.println("Total de equipos de sonido: " + totalSonidos + " | Disponibles: " + contarDisponiblesPorTipo(Sonido.class) +
                " | Alquilados: " + contarAlquiladosPorTipo(Sonido.class));
        System.out.println("Disponibles en total: " + disponibles);
        System.out.println("Alquilados en total: " + alquilados);
        System.out.println("Ingresos acumulados: " + formatearMonto(ingresosAcumulados));
    }

    private static int contarDisponiblesPorTipo(Class<?> tipo) {
        int contador = 0;
        for (Equipo equipo : INVENTARIO.values()) {
            if (tipo.isInstance(equipo) && equipo.estaDisponible()) {
                contador++;
            }
        }
        return contador;
    }

    private static int contarAlquiladosPorTipo(Class<?> tipo) {
        int contador = 0;
        for (Equipo equipo : INVENTARIO.values()) {
            if (tipo.isInstance(equipo) && !equipo.estaDisponible()) {
                contador++;
            }
        }
        return contador;
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return SC.nextLine();
    }

    private static int leerEntero(String mensaje) {
        String valor = leerTexto(mensaje);
        try {
            return Integer.parseInt(valor.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static int leerEnteroPositivo(String mensaje) {
        while (true) {
            String valor = leerTexto(mensaje);
            try {
                int numero = Integer.parseInt(valor.trim());
                if (numero > 0) {
                    return numero;
                }
                System.out.println("Debe ingresar un número entero mayor que cero.");
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Debe ingresar un número entero.");
            }
        }
    }

    private static double leerDoublePositivo(String mensaje) {
        while (true) {
            String valor = leerTexto(mensaje);
            try {
                double numero = Double.parseDouble(valor.trim().replace(',', '.'));
                if (numero > 0) {
                    return numero;
                }
                System.out.println("Debe ingresar un número mayor que cero.");
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Debe ingresar un número válido.");
            }
        }
    }

    private static boolean leerBoolean(String mensaje) {
        while (true) {
            String valor = leerTexto(mensaje).trim().toLowerCase(Locale.ROOT);
            if (valor.equals("s") || valor.equals("si") || valor.equals("y") || valor.equals("yes")) {
                return true;
            }
            if (valor.equals("n") || valor.equals("no")) {
                return false;
            }
            System.out.println("Respuesta inválida. Use s/n.");
        }
    }

    private static String formatearMonto(double monto) {
        return String.format(Locale.US, "Q %.2f", monto);
    }
}

