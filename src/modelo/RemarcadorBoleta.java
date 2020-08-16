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
    public BigDecimal maxdemandaleida, maxdemandafacturada, maxdemandaleidahpunta, maxdemandafacturadahpunta;

    private final String fechaemision, fechainicial, fechafinal, nextfecha;

    private String nomtarifa;

    private JSONObject boleta;
    private JSONObject remcli;

    public RemarcadorBoleta(int idremarcador, int consumo, int idtarifa, String mesanio, String fechaemision, String fechadesde, String fechahasta, String nextfecha) {
        this.idremarcador = idremarcador;
        this.consumo = consumo;
        this.idtarifa = idtarifa;
        this.anio = Integer.parseInt(mesanio.split("-")[0]);
        this.mes = Integer.parseInt(mesanio.split("-")[1]);
        this.mesanio = mesanio;
        this.fechaemision = fechaemision;
        this.fechainicial = fechadesde;
        this.fechafinal = fechahasta;
        this.nextfecha = nextfecha;
        boleta = new JSONObject();
        remcli = new JSONObject();
        boleta.put("idtarifa", this.idtarifa);
        boleta.put("nextfecha", this.nextfecha);
        setNomTarifa();
        construir();
        hacerBoleta();
    }

    private void construir() {
        String queryremarcador = "CALL SP_GET_DATOS_UN_REMARCADOR_BOLETA(" + idremarcador + ", '" + mesanio + "')";
        Conexion c = new Conexion();
        System.out.println(queryremarcador);
        c.abrir();
        ResultSet rs = c.ejecutarQuery(queryremarcador);
        try {
            while (rs.next()) {
                boleta.put("lecturaanterior", rs.getInt("LECTURAANTERIOR"));
                boleta.put("lecturaactual", rs.getInt("LECTURAACTUAL"));
                boleta.put("fechadesde", this.fechainicial);
                boleta.put("fechahasta", this.fechafinal);
                boleta.put("consumo", this.consumo);
                
                boleta.put("maxdemandaleida", rs.getBigDecimal("MAX_DEMANDA_LEIDA"));
                boleta.put("maxdemandafacturada", rs.getDouble("MAX_DEMANDA_FACTURADA"));
                boleta.put("maxdemandahorapuntaleida", rs.getBigDecimal("MAX_DEMANDA_LEIDA_HORA_PUNTA"));
                boleta.put("maxdemandahorapuntafacturada", rs.getBigDecimal("MAX_DEMANDA_FACTURADA_HORA_PUNTA"));
                
                boleta.put("numremarcador", rs.getInt("NUMREMARCADOR"));
                
                
                remcli.put("idcliente", rs.getInt("IDCLIENTE"));
                remcli.put("nomcliente", rs.getString("NOMCLIENTE"));
                remcli.put("razoncliente", rs.getString("RAZONCLIENTE"));
                remcli.put("rutcliente", rs.getInt("RUTCLIENTE"));
                remcli.put("dvcliente", rs.getString("DVCLIENTE"));
                remcli.put("direccion", rs.getString("DIRECCION"));
                remcli.put("persona", rs.getString("PERSONA"));
                remcli.put("email", rs.getString("EMAIL"));
                remcli.put("fono", rs.getInt("FONO"));
                remcli.put("idinstalacion", rs.getInt("IDINSTALACION"));
                remcli.put("nominstalacion", rs.getString("NOMINSTALACION"));
                remcli.put("idparque", rs.getInt("IDPARQUE"));
                remcli.put("nomparque", rs.getString("NOMPARQUE"));
                remcli.put("idempalme", rs.getInt("IDEMPALME"));
                remcli.put("numempalme", rs.getString("NUMEMPALME"));
                remcli.put("idcomuna", rs.getInt("IDCOMUNA"));
                remcli.put("nomcomuna", rs.getString("NOMCOMUNA"));
                remcli.put("idred", rs.getInt("IDRED"));
                remcli.put("nomred", rs.getString("NOMRED"));
                remcli.put("idremarcador", rs.getInt("IDREMARCADOR"));
                remcli.put("numremarcador", rs.getInt("NUMREMARCADOR"));
                remcli.put("modulos", rs.getString("MODULOS"));
                remcli.put("numserie", rs.getString("NUMSERIE"));
                
                remcli.put("maxdemandaleida", rs.getBigDecimal("MAX_DEMANDA_LEIDA"));
                remcli.put("maxdemandafacturada", rs.getDouble("MAX_DEMANDA_FACTURADA"));
                remcli.put("maxdemandahorapuntaleida", rs.getBigDecimal("MAX_DEMANDA_LEIDA_HORA_PUNTA"));
                remcli.put("maxdemandahorapuntafacturada", rs.getBigDecimal("MAX_DEMANDA_FACTURADA_HORA_PUNTA"));
                
                this.maxdemandaleida = rs.getBigDecimal("MAX_DEMANDA_LEIDA");
                this.maxdemandafacturada = rs.getBigDecimal("MAX_DEMANDA_FACTURADA");
                this.maxdemandaleidahpunta = rs.getBigDecimal("MAX_DEMANDA_LEIDA_HORA_PUNTA");
                this.maxdemandafacturadahpunta = rs.getBigDecimal("MAX_DEMANDA_FACTURADA_HORA_PUNTA");
                
                System.out.println("Demandas:");

                System.out.println("Max demanda leida: " + rs.getBigDecimal("MAX_DEMANDA_LEIDA"));
                System.out.println("Max demanda facturada: " + rs.getBigDecimal("MAX_DEMANDA_FACTURADA"));
                System.out.println("Max demanda leida h punta: " + rs.getBigDecimal("MAX_DEMANDA_LEIDA_HORA_PUNTA"));
                System.out.println("Max demanda facturada h punta: " + rs.getBigDecimal("MAX_DEMANDA_FACTURADA_HORA_PUNTA"));
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
                + boleta.getInt("idtarifa") + ", "
                + consumo + ", "
                + boleta.getInt("numremarcador") + ", "
                + "'" + mesanio + "'"
                + ")";
        Conexion c = new Conexion();
        System.out.println(queryremarcador);
        c.abrir();
        ResultSet rs = c.ejecutarQuery(queryremarcador);
        int total = 0;
        int exento = 0;
        JSONArray consumos = new JSONArray();
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

    private void setNomTarifa() {
        Conexion c = new Conexion();
        c.abrir();
        try {
            ResultSet rs = c.ejecutarQuery("SELECT NOMTARIFA FROM TARIFA WHERE IDTARIFA = " + this.idtarifa);
            while (rs.next()) {
                this.nomtarifa = rs.getString("NOMTARIFA");
                boleta.put("nomtarifa", rs.getString("NOMTARIFA"));
            }
        } catch (Exception ex) {
            System.out.println("No se pudo obtener el nombre de la tarifa.");
        }
        c.cerrar();
    }

    public JSONObject getBoletaJson() {
        return this.boleta;
    }

    public JSONObject getRemcliJson() {
        return this.remcli;
    }

}
