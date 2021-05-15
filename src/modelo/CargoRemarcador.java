package modelo;

import java.math.BigInteger;

public class CargoRemarcador {
    private final int idtarifa, idconcepto, idcomuna;
    private final String nomconcepto, unidadmedida;
    private final BigInteger valorneto;
    
    public CargoRemarcador(int idtarifa, int idconcepto, int idcomuna, String nomconcepto, String unidadmedida, BigInteger valorneto){
        this.idtarifa = idtarifa;
        this.idconcepto = idconcepto;
        this.idcomuna = idcomuna;
        this.nomconcepto = nomconcepto;
        this.unidadmedida = unidadmedida;
        this.valorneto = valorneto;
    }

    public int getIdtarifa() {
        return idtarifa;
    }

    public int getIdconcepto() {
        return idconcepto;
    }

    public int getIdcomuna() {
        return idcomuna;
    }

    public String getNomconcepto() {
        return nomconcepto;
    }

    public String getUnidadmedida() {
        return unidadmedida;
    }

    public BigInteger getValorneto() {
        return valorneto;
    }
    
    
}