package modelo.lectura;

import java.math.BigDecimal;

public class RegistroPotencia {

    public String fecha;
    public Integer numremarcador;
    public String timestamp;
    public BigDecimal potencia;

    public Integer anio, mes, dia, hh, mm, ss;

    public RegistroPotencia(String fecha, Integer numremarcador, String timestamp, BigDecimal potencia, Integer anio, Integer mes, Integer dia, Integer hh, Integer mm, Integer ss) {
        this.fecha = fecha;
        this.numremarcador = numremarcador;
        this.timestamp = timestamp;
        this.potencia = potencia;

        this.anio = anio;
        this.mes = mes;
        this.dia = dia;
        this.hh = hh;
        this.mm = mm;
        this.ss = ss;
    }

    public RegistroPotencia() {

    }
}
