package modelo.lectura;

public class Registro {

    public String dia;
    public Integer numremarcador;
    public String timestamp;
    public float lectura;
    public float lecturaManual;
    public float delta;
    public float proyeccion;
    public boolean esmanual = false;

    public Registro(String dia, Integer numremarcador, String timestamp, float lectura) {
        this.dia = dia;
        this.numremarcador = numremarcador;
        this.timestamp = timestamp;
        this.lectura = lectura;
    }

    public Registro() {

    }
}
