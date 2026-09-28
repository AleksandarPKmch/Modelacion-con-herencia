public class Proyector extends Equipo {
    private int lumens;
    private boolean conectividadInalambrica;

    public Proyector(String codigoInventario, String marca, String modelo, double tarifaDiaria,
                    int lumens, boolean conectividadInalambrica, boolean disponible) {
        super(codigoInventario, marca, modelo, tarifaDiaria, disponible);
        if (lumens <= 0) {
            throw new IllegalArgumentException("Los lúmenes del proyector deben ser mayores que cero.");
        }
        this.lumens = lumens;
        this.conectividadInalambrica = conectividadInalambrica;
    }

    public int getLumens() {
        return lumens;
    }

    public boolean isConectividadInalambrica() {
        return conectividadInalambrica;
    }

    @Override
    public double calcularCargoExtra(int dias) {
        if (dias <= 0) {
            throw new IllegalArgumentException("Los días de alquiler deben ser positivos.");
        }
        return conectividadInalambrica ? dias * 50 : 0;
    }

    @Override
    public String obtenerDescripcionEspecifica() {
        return "Lúmenes: " + lumens + " | Conectividad inalámbrica: " +
                (conectividadInalambrica ? "Sí" : "No");
    }
}
