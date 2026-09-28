import java.util.Locale;

public abstract class Equipo {
    private String codigoInventario;
    private String marca;
    private String modelo;
    private double tarifaDiaria;
    private boolean disponible;

    public Equipo(String codigoInventario, String marca, String modelo, double tarifaDiaria, boolean disponible) {
        setCodigoInventario(codigoInventario);
        setMarca(marca);
        setModelo(modelo);
        setTarifaDiaria(tarifaDiaria);
        this.disponible = disponible;
    }

    public abstract double calcularCargoExtra(int dias);

    public double calcularCostoTotal(int dias) {
        if (dias <= 0) {
            throw new IllegalArgumentException("Los días de alquiler deben ser positivos.");
        }
        return (tarifaDiaria * dias) + calcularCargoExtra(dias);
    }

    public abstract String obtenerDescripcionEspecifica();

    public String getCodigoInventario() {
        return codigoInventario;
    }

    public void setCodigoInventario(String codigoInventario) {
        if (codigoInventario == null || codigoInventario.trim().isEmpty()) {
            throw new IllegalArgumentException("El código de inventario no puede estar vacío.");
        }
        this.codigoInventario = codigoInventario.trim();
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        if (marca == null || marca.trim().isEmpty()) {
            throw new IllegalArgumentException("La marca no puede estar vacía.");
        }
        this.marca = marca.trim();
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        if (modelo == null || modelo.trim().isEmpty()) {
            throw new IllegalArgumentException("El modelo no puede estar vacío.");
        }
        this.modelo = modelo.trim();
    }

    public double getTarifaDiaria() {
        return tarifaDiaria;
    }

    public void setTarifaDiaria(double tarifaDiaria) {
        if (tarifaDiaria <= 0) {
            throw new IllegalArgumentException("La tarifa diaria debe ser mayor que cero.");
        }
        this.tarifaDiaria = tarifaDiaria;
    }

    public boolean estaDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public String formatearMonto(double monto) {
        return String.format(Locale.US, "Q %.2f", monto);
    }

    @Override
    public String toString() {
        return codigoInventario + " | " + marca + " " + modelo + " | " +
                (disponible ? "Disponible" : "Alquilado") + " | " +
                formatearMonto(tarifaDiaria) + "/día" + " | " + obtenerDescripcionEspecifica();
    }
}
