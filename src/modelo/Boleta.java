package modelo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class Boleta {

    private final int idcliente;
    private String nomcliente;
    private String razoncliente;
    private int rutcliente;
    private String dvcliente;
    private String direccion;
    private String persona;
    private String cargo;
    private String fono;
    private String email;
    private String fechaini;
    private String fechafin;
    private LinkedList<Remarcador> remarcadores;
    private LinkedList<CargoAdicional> cargosadicionales;
    

    public Boleta(int idcliente, String fechaini, String fechafin) {
        this.idcliente = idcliente;
        this.remarcadores = new LinkedList();
        this.cargosadicionales = new LinkedList();
        this.fechaini = fechaini;
        this.fechafin = fechafin;
        setDatosCliente(this.idcliente);
        cargarRemarcadores(this.idcliente);
    }
    
    private void setDatosCliente(int idcliente){
        String query = "CALL SP_GET_CLIENTE_IDCLIENTE(" + idcliente + ")";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        try{
            while(rs.next()){
                this.rutcliente = rs.getInt("RUTCLIENTE");
                this.dvcliente = rs.getString("DVCLIENTE");
                this.nomcliente = rs.getString("NOMCLIENTE");
                this.razoncliente = rs.getString("RAZONCLIENTE");
                this.direccion = rs.getString("DIRECCION");
                this.persona = rs.getString("PERSONA");
                this.cargo = rs.getString("CARGO");
                this.fono = rs.getString("FONO");
                this.email = rs.getString("EMAIL");
            }
        }catch(SQLException ex){
            System.out.println("No se pueden obtener los parámetros para instanciar un cliente en Boleta");
            System.out.println(ex);
        }
        c.cerrar();
    }
    
    private void cargarRemarcadores(int idcliente){
        String query = "CALL SP_GET_REMARCADORES_ASIGNADOS_IDCLIENTE(" + idcliente + ")";
        System.out.println(query);
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        try{
            while(rs.next()){
                Remarcador r = new Remarcador(rs.getInt("IDREMARCADOR"));
                r.getSetDiferencia(this.fechaini, this.fechafin);
                this.remarcadores.add(r);
            }
        }catch(SQLException ex){
            System.out.println("No se pueden cargar los remarcadores para la boleta");
            System.out.println(ex);
        }
        c.cerrar();
    }
    
    public void addCargoAdicional(String concepto, int valorunitario, int cantidad){
        this.cargosadicionales.add(new CargoAdicional(concepto, valorunitario, cantidad));
    }
    
    private class CargoAdicional{
        public String CONCEPTO;
        public int VALOR_UNITARIO, CANTIDAD;
        private CargoAdicional(String concepto, int valorunitario, int cantidad){
            this.CONCEPTO = concepto;
            this.VALOR_UNITARIO = valorunitario;
            this.CANTIDAD = cantidad;
        }
    }

    public int getIdcliente() {
        return idcliente;
    }

    public String getNomcliente() {
        return nomcliente;
    }

    public String getRazoncliente() {
        return razoncliente;
    }

    public int getRutcliente() {
        return rutcliente;
    }

    public String getDvcliente() {
        return dvcliente;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getPersona() {
        return persona;
    }

    public String getCargo() {
        return cargo;
    }

    public String getFono() {
        return fono;
    }

    public String getEmail() {
        return email;
    }

    public String getFechaini() {
        return fechaini;
    }

    public String getFechafin() {
        return fechafin;
    }

    public LinkedList<Remarcador> getRemarcadores() {
        return remarcadores;
    }

    public LinkedList<CargoAdicional> getCargosadicionales() {
        return cargosadicionales;
    }
    
    
    
}
