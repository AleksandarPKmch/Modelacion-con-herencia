public class Camara extends Equipo {
    private int resolucionMaxima;

    public Camara(String codigoInventario, String marca, String modelo, double tarifaDiaria,
                 int resolucionMaxima, boolean disponible) {
        super(codigoInventario, marca, modelo, tarifaDiaria, disponible);
        if (resolucionMaxima <= 0) {
            throw new IllegalArgumentException("La resolución máxima debe ser mayor que cero.");
        }
        this.resolucionMaxima = resolucionMaxima;
    }

    public int getResolucionMaxima() {
        return resolucionMaxima;
    }

    @Override
    public double calcularCargoExtra(int dias) {
        if (dias <= 0) {
            throw new IllegalArgumentException("Los días de alquiler deben ser positivos.");
        }
        return resolucionMaxima > 1080 ? 75 : 0;
    }

    @Override
    public String obtenerDescripcionEspecifica() {
        return "Resolución máxima: " + resolucionMaxima + " px";
    }
}
