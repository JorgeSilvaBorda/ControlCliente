package modelo;

import clases.json.JSONException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class Remarcador {

    private final int idremarcador;
    private int idparque;
    private String nomparque;
    private int idempalme;
    private int idequipomodbus;
    private String numempalme;
    private String nominstalacion;
    private int numremarcador;
    private String modulos;
    private int idinstalacion;
    private String direccion;
    
    public int diffperiodo;
    public int lecturaactual;
    public int lecturaanterior;
    
    public LinkedList<CargoRemarcador> cargos;

    public Remarcador(int idremarcador) {
        this.idremarcador = idremarcador;
        setParametros(this.idremarcador);
    }
    
    private void setParametros(int idremarcador){
        String query = "CALL SP_GET_REMARCADOR_IDREMARCADOR(" + idremarcador + ")";
        System.out.println(query);
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        try {
            while (rs.next()) {
                this.idparque = rs.getInt("IDPARQUE");
                this.idempalme = rs.getInt("IDEMPALME");
                this.idequipomodbus = rs.getInt("IDEQUIPOMODBUS");
                this.numremarcador = rs.getInt("NUMREMARCADOR");
                this.modulos = rs.getString("MODULOS");
                this.idinstalacion = rs.getInt("IDINSTALACION");  
                this.numempalme = rs.getString("NUMEMPALME");  
                this.nomparque = rs.getString("NOMPARQUE");  
                this.nominstalacion = rs.getString("NOMINSTALACION");
                this.direccion = rs.getString("DIRECCION");
            }
        } catch (SQLException ex) {
            System.out.println("No se puede obtener los datos para instanciar un remarcador.");
            System.out.println(ex);
        }
        c.cerrar();
    }

    public void getSetDiferencia(String fechaIni, String fechaFin) {
        int lecturaAnterior = getLecturaDia(fechaIni);
        int lecturaActual = getLecturaDia(fechaFin);
        int diferencia = lecturaActual - lecturaAnterior;
        this.diffperiodo = diferencia;
        this.lecturaactual = lecturaActual;
        this.lecturaanterior = lecturaAnterior;
    }

    private int getLecturaDia(String fecha) {
        String query = "CALL SP_GET_LECTURA_DIA_REMARCADOR(" + this.idremarcador + ", '" + fecha + "')";
        Conexion c = new Conexion();
        System.out.println(query);
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        try {
            while (rs.next()) {
                return rs.getInt("KWH");
            }
        } catch (JSONException | SQLException ex) {
            System.out.println("No se puede obtener la lectura para un día para un remarcador");
            System.out.println(ex);
            return -1;
        }
        c.cerrar();
        return -1;
    }
    
    public void setCargos(){
        
    }

    public int getIdremarcador() {
        return idremarcador;
    }

    public int getIdparque() {
        return idparque;
    }

    public String getNomparque() {
        return nomparque;
    }

    public int getIdempalme() {
        return idempalme;
    }

    public int getIdequipomodbus() {
        return idequipomodbus;
    }

    public String getNumempalme() {
        return numempalme;
    }

    public String getNominstalacion() {
        return nominstalacion;
    }

    public int getNumremarcador() {
        return numremarcador;
    }

    public String getModulos() {
        return modulos;
    }

    public int getIdinstalacion() {
        return idinstalacion;
    }

    public String getDireccion() {
        return direccion;
    }
    
    

}
