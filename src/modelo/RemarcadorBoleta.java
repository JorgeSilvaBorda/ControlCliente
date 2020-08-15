package modelo;

import clases.json.JSONArray;
import clases.json.JSONObject;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RemarcadorBoleta {

    private final int idremarcador, consumo, idtarifa, mes, anio;
    private final String mesanio;
    private int numremarcador;
    private int idempalme;
    private int idparque;
    private int idinstalacion;
    private int idcomuna;
    private int idcliente;
    private int rutcliente;
    private String dvcliente;
    private String nomcliente;
    private String razoncliente;
    private String persona;
    private String cargo;
    private String email;
    private int fono;
    private String nomcomuna;
    private String nominstalacion;
    private String nomparque;
    private String numempalme;
    private String numserie;
    private String modulos;
    private String direccion;
    private int idred;
    private String nomred;
    private BigDecimal demmaxpotenciasuministrada;
    private BigDecimal demmaxpotencialeidahorapunta;
    private String demmaxpotenciastring;
    private String demmaxpotenciahorapuntastring;
    
    private String fechaemision, fechainicial, fechafinal;

    public RemarcadorBoleta(int idremarcador, int consumo, int idtarifa, String mesanio, String fechaemision, String fechadesde, String fechahasta) {
        this.idremarcador = idremarcador;
        this.consumo = consumo;
        this.idtarifa = idtarifa;
        this.anio = Integer.parseInt(mesanio.split("-")[0]);
        this.mes = Integer.parseInt(mesanio.split("-")[1]);
        this.mesanio = mesanio;
        this.fechaemision = fechaemision;
        this.fechainicial = fechadesde;
        this.fechafinal = fechahasta;
        construir();
        hacerBoleta();
    }

    private void construir() {
        String queryremarcador = "CALL SP_GET_REMARCADOR_CLIENTE_IDREMARCADOR(" + idremarcador + ", '" + mesanio + "')";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(queryremarcador);
        try {
            while (rs.next()) {
                this.numserie = rs.getString("NUMSERIE");
                this.modulos = rs.getString("MODULOS");
                this.numremarcador = rs.getInt("NUMREMARCADOR");
                this.idcliente = rs.getInt("IDCLIENTE");
                this.rutcliente = rs.getInt("RUTCLIENTE");
                this.dvcliente = rs.getString("DVCLIENTE");
                this.nomcliente = rs.getString("NOMCLIENTE");
                this.razoncliente = rs.getString("RAZONCLIENTE");
                this.persona = rs.getString("PERSONA");
                this.cargo = rs.getString("CARGO");
                this.fono = rs.getInt("FONO");
                this.email = rs.getString("EMAIL");
                this.idempalme = rs.getInt("IDEMPALME");
                this.numempalme = rs.getString("NUMEMPALME");
                this.idparque = rs.getInt("IDPARQUE");
                this.nomparque = rs.getString("NOMPARQUE");
                this.idinstalacion = rs.getInt("IDINSTALACION");
                this.nominstalacion = rs.getString("NOMINSTALACION");
                this.direccion = rs.getString("DIRECCION");
                this.idcomuna = rs.getInt("IDCOMUNA");
                this.nomcomuna = rs.getString("NOMCOMUNA");
                this.idred = rs.getInt("IDRED");
                this.nomred = rs.getString("NOMRED");
                this.demmaxpotenciasuministrada = rs.getBigDecimal("DEM_MAX_POTENCIA_SUMINISTRADA");
                this.demmaxpotencialeidahorapunta = rs.getBigDecimal("DEM_MAX_POTENCIA_LEIDA_H_PUNTA");
                this.demmaxpotenciastring = rs.getString("DEM_MAX_POTENCIA_SUMINISTRADA_STRING");
                this.demmaxpotenciahorapuntastring = rs.getString("DEM_MAX_POTENCIA_LEIDA_H_PUNTA_STRING");
            }
        } catch (Exception ex) {
            System.out.println("No se pudo construir la data del remarcador");
            System.out.println(ex);
        }
        c.cerrar();
    }

    private void hacerBoleta() {
        String queryremarcador = "CALL SP_GET_DETALLE_TARIFA_IDRED_CONSUMO_REMARCADOR("
                + idtarifa + ", "
                + idred + ", "
                + consumo + ", "
                + numremarcador + ", "
                + "'" + mesanio + "'"
                + ")";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(queryremarcador);
        int total = 0;
        int exento = 0;
        JSONArray consumos = new JSONArray();
        JSONObject boleta = new JSONObject();
        try {
            while (rs.next()) {
                JSONObject jsonconsumo = new JSONObject();

                if (rs.getInt("IDCONCEPTO") < 5) {
                    Double cant = Double.parseDouble(rs.getBigDecimal("CANTIDAD").toString());
                    Double net = Double.parseDouble(rs.getBigDecimal("VALORNETO").toString());
                    Double tot = Double.parseDouble(rs.getBigDecimal("TOTAL").toString());

                    if (rs.getInt("IDCONCEPTO") != 2) {
                        total += rs.getInt("TOTAL");
                    } else {
                        exento = rs.getInt("TOTAL");
                    }

                    jsonconsumo.put("idconcepto", rs.getInt("IDCONCEPTO"));
                    jsonconsumo.put("idred", rs.getInt("IDRED"));
                    jsonconsumo.put("nomconcepto", rs.getString("NOMCONCEPTO"));
                    jsonconsumo.put("nomred", rs.getString("NOMRED"));
                    jsonconsumo.put("umedida", rs.getString("UMEDIDA"));
                    jsonconsumo.put("cantidad", cant);
                    jsonconsumo.put("valorneto", net);
                    jsonconsumo.put("total", tot);

                } else if (rs.getInt("IDCONCEPTO") >= 7 || rs.getInt("IDCONCEPTO") <= 5) {
                    Double cant = Double.parseDouble(rs.getBigDecimal("CANTIDAD").toString());
                    Double net = Double.parseDouble(rs.getBigDecimal("VALORNETO").toString());
                    Double tot = Double.parseDouble(rs.getBigDecimal("TOTAL").toString());

                    jsonconsumo.put("idconcepto", rs.getInt("IDCONCEPTO"));
                    jsonconsumo.put("idred", rs.getInt("IDRED"));
                    jsonconsumo.put("nomconcepto", rs.getString("NOMCONCEPTO"));
                    jsonconsumo.put("nomred", rs.getString("NOMRED"));
                    jsonconsumo.put("umedida", rs.getString("UMEDIDA"));
                    jsonconsumo.put("cantidad", cant);
                    jsonconsumo.put("valorneto", net);
                    jsonconsumo.put("total", tot);
                    total += rs.getInt("TOTAL");
                } else if (rs.getInt("IDCONCEPTO") >= 9 && rs.getInt("IDCONCEPTO") <= 11) {
                    Double cant = Double.parseDouble(rs.getBigDecimal("CANTIDAD").toString());
                    Double net = Double.parseDouble(rs.getBigDecimal("VALORNETO").toString());
                    Double tot = Double.parseDouble(rs.getBigDecimal("TOTAL").toString());

                    jsonconsumo.put("idconcepto", rs.getInt("IDCONCEPTO"));
                    jsonconsumo.put("idred", rs.getInt("IDRED"));
                    jsonconsumo.put("nomconcepto", rs.getString("NOMCONCEPTO"));
                    jsonconsumo.put("nomred", rs.getString("NOMRED"));
                    jsonconsumo.put("umedida", rs.getString("UMEDIDA"));
                    jsonconsumo.put("cantidad", cant);
                    jsonconsumo.put("valorneto", net);
                    jsonconsumo.put("total", tot);
                    total += rs.getInt("TOTAL");
                }
                consumos.put(jsonconsumo);
            }

            boleta.put("consumos", consumos);
            boleta.put("totalneto", total);
            boleta.put("iva", Integer.parseInt(Double.toString(total * 0.19).split("\\.")[0]));
            boleta.put("exento", exento);
            boleta.put("total", total + Integer.parseInt(Double.toString(total * 0.19).split("\\.")[0]) + exento);
            boleta.put("totalapagar", total + Integer.parseInt(Double.toString(total * 0.19).split("\\.")[0]) + exento);
        } catch (SQLException ex) {
            System.out.println("No se pudo hacer la boleta.");
            System.out.println(ex);
            ex.printStackTrace();
        }
        c.cerrar();
    }

}
