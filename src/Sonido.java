public class Sonido extends Equipo {
    private double potenciaNominal;

    public Sonido(String codigoInventario, String marca, String modelo, double tarifaDiaria,
                 double potenciaNominal, boolean disponible) {
        super(codigoInventario, marca, modelo, tarifaDiaria, disponible);
        if (potenciaNominal <= 0) {
            throw new IllegalArgumentException("La potencia nominal debe ser mayor que cero.");
        }
        this.potenciaNominal = potenciaNominal;
    }

    public double getPotenciaNominal() {
        return potenciaNominal;
    }

    @Override
    public double calcularCargoExtra(int dias) {
        if (dias <= 0) {
            throw new IllegalArgumentException("Los días de alquiler deben ser positivos.");
        }
        return potenciaNominal * 100 * dias;
    }

    @Override
    public String obtenerDescripcionEspecifica() {
        return "Potencia nominal: " + String.format(java.util.Locale.US, "%.2f", potenciaNominal) + " kW";
    }
}
