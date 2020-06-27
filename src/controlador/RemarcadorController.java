package controlador;

import clases.json.JSONArray;
import clases.json.JSONException;
import clases.json.JSONObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.Conexion;
import modelo.Util;

public class RemarcadorController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html; charset=UTF-8");
        JSONObject entrada = new JSONObject(request.getParameter("datos"));
        switch (entrada.getString("tipo")) {
            case "get-remarcadores":
                out.print(getRemarcadores());
                break;
            case "get-remarcadores-idempalme":
                out.print(getRemarcadoresIdEmpalme(entrada));
                break;
            case "get-remarcadores-numempalme-boleta":
                out.print(getRemarcadoresNumEmpalmeBoleta(entrada));
                break;
            case "get-remarcadores-libres":
                out.print(getRemarcadoresLibres());
                break;
            case "get-remarcadores-asignados":
                out.print(getRemarcadoresAsignados());
                break;
            case "get-select-remarcadores-cliente":
                out.print(getSelectRemarcadoresCliente(entrada));
                break;
            case "get-remarcador-cliente-idremarcador":
                out.print(getRemarcadorClienteIdRemarcador(entrada));
                break;
            case "get-select-remarcador-idempalme":
                out.print(getSelectRemarcadorIdEmpalme(entrada));
                break;
            case "ins-remarcador":
                out.print(insRemarcador(entrada));
                break;
            case "get-remarcador-idremarcador":
                out.print(getRemarcadorIdRemarcador(entrada));
                break;
            case "get-registros-mes-remarcador":
                out.print(getRegistrosMesRemarcador(entrada));
                break;
            case "upd-remarcador":
                out.print(updRemarcador(entrada.getJSONObject("remarcador")));
                break;
            case "del-remarcador":
                out.print(delRemarcador(entrada));
                break;
        }
    }

    private JSONObject getRemarcadores() {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_GET_REMARCADORES()";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        String filas = "";
        try {
            while (rs.next()) {
                filas += "<tr>";
                filas += "<td><input type='hidden' value='" + rs.getInt("IDREMARCADOR") + "' /><span>" + rs.getString("NUMREMARCADOR") + "</span></td>";
                filas += "<td><input type='hidden' value='" + rs.getString("NUMSERIE") + "' /><span>" + rs.getString("NUMSERIE") + "</span></td>";
                filas += "<td><input type='hidden' value='" + rs.getInt("IDEMPALME") + "' /><span>" + rs.getString("NUMEMPALME") + "</span></td>";
                filas += "<td><input type='hidden' value='" + rs.getInt("IDPARQUE") + "' /><span>" + rs.getString("NOMPARQUE") + "</span></td>";
                filas += "<td><span>" + rs.getString("MODULOS") + "</span></td>";
                filas += "<td style='width: 15%;'>"
                        + "<button style='font-size:10px; padding: 0.1 rem 0.1 rem;' type='button' class='btn btn-sm btn-warning' onclick='activarEdicion(this)'>Editar</button>"
                        + "<button style='font-size:10px; padding: 0.1 rem 0.1 rem;' type='button' class='btn btn-sm btn-danger' onclick='eliminar(this)'>Eliminar</button>"
                        + "</td>";
                filas += "</tr>";
            }
            salida.put("tabla", filas);
            salida.put("estado", "ok");
        } catch (JSONException | SQLException ex) {
            System.out.println("Problemas en controlador.RemarcadorController.getRemarcadores().");
            System.out.println(ex);
            ex.printStackTrace();
            salida.put("estado", "error");
            salida.put("error", ex);
        }
        c.cerrar();
        return salida;
    }

    private JSONObject getRemarcadoresIdEmpalme(JSONObject entrada) {
        JSONObject salida = new JSONObject();
        JSONArray ides = new JSONArray();
        String query = "CALL SP_GET_REMARCADORES_IDEMPALME(" + entrada.getInt("idempalme") + ")";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        String filas = "";

        String tabla = "<table style='font-size: 12px;' id='tabla-remarcadores-empalme' class='table table-borderless table-condensed table-sm'>";
        tabla += "<caption style='caption-side:top;'><h5>Remarcadores en el Empalme Nº: " + entrada.getString("numempalme") + "</h5></caption>";
        tabla += "<thead><tr>";
        tabla += "<th># Remarcador</th>";
        tabla += "<th>Nº Serie</th>";
        tabla += "<th>Bodega</th>";
        tabla += "<th>Cliente</th>";
        tabla += "<th>Módulos</th>";
        tabla += "<th>Instalación</th>";
        tabla += "</tr></thead><tbody>";
        JSONObject remarcador;
        JSONArray remarcadores = new JSONArray();
        try {
            while (rs.next()) {
                String numserie = rs.getString("NUMSERIE");
                filas += "<tr>";
                filas += "<td><input type='hidden' value='" + rs.getInt("IDREMARCADOR") + "' /><span>" + rs.getString("NUMREMARCADOR") + "</span></td>";
                filas += "<td><input type='hidden' value='" + numserie + "' /><span>" + numserie + "</span></td>";
                filas += "<td><input type='hidden' value='" + rs.getInt("IDPARQUE") + "' /><span>" + rs.getString("NOMPARQUE") + "</span></td>";
                filas += "<td><span>" + rs.getString("NOMCLIENTE") + "</span></td>";
                filas += "<td><span>" + rs.getString("MODULOS") + "</span></td>";
                filas += "<td><input type='hidden' value='" + rs.getInt("IDINSTALACION") + "' /><span>" + rs.getString("NOMINSTALACION") + "</span></td>";
                filas += "</tr>";
                remarcador = new JSONObject();
                remarcador.put("idremarcador", rs.getInt("IDREMARCADOR"));
                remarcador.put("numremarcador", rs.getString("NUMREMARCADOR"));
                remarcador.put("numserie", numserie);
                remarcador.put("idparque", rs.getInt("IDPARQUE"));
                remarcador.put("modulos", rs.getString("MODULOS"));
                remarcador.put("idinstalacion", rs.getInt("IDINSTALACION"));
                remarcadores.put(remarcador);
            }
            tabla += filas;
            tabla += "</tbody></table>";
            salida.put("tabla", tabla);
            salida.put("remarcadores", remarcadores);
            salida.put("estado", "ok");
        } catch (JSONException | SQLException ex) {
            System.out.println("Problemas en controlador.RemarcadorController.getRemarcadoresIdEmpalme().");
            System.out.println(ex);
            ex.printStackTrace();
            salida.put("estado", "error");
            salida.put("error", ex);
        }
        c.cerrar();
        return salida;
    }

    private JSONObject getRemarcadoresNumEmpalmeBoleta(JSONObject entrada) {
        JSONObject salida = new JSONObject();
        int kwtotal = 0;
        String query = "CALL SP_GET_REMARCADORES_NUMEMPALME_BOLETA("
                + "'" + entrada.getString("numempalme") + "',"
                + "'" + entrada.getString("mes") + "'"
                + ")";
        System.out.println(query);
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        String filas = "";

        String tabla = "<table style='font-size: 12px;' id='tabla-remarcadores-empalme' class='table table-bordered table-condensed table-sm'>";
        tabla += "<caption style='caption-side:top;'><h5>Remarcadores en el Empalme Nº: " + entrada.getString("numempalme") + "</h5></caption>";
        tabla += "<thead><tr class='table-info'>";
        tabla += "<th># Remarcador</th>";
        tabla += "<th>Nº Serie</th>";
        tabla += "<th>Bodega</th>";
        tabla += "<th>Cliente</th>";
        tabla += "<th>Módulos</th>";
        tabla += "<th>Instalación</th>";
        tabla += "<th>Lectura<br />Anterior</th>";
        tabla += "<th>Lectura<br />Actual</th>";
        tabla += "<th>Consumo (kW)</th>";
        tabla += "<th>Acción</th>";
        tabla += "</tr></thead><tbody>";
        JSONObject remarcador;
        int idcomuna = 0;
        JSONArray remarcadores = new JSONArray();
        try {
            while (rs.next()) {
                filas += "<tr>";
                filas += "<td><input type='hidden' value='" + rs.getInt("IDREMARCADOR") + "' /><span>" + rs.getString("NUMREMARCADOR") + "</span></td>";
                filas += "<td><input type='hidden' value='" + rs.getString("NUMSERIE") + "' /><span>" + rs.getString("NUMSERIE") + "</span></td>";
                filas += "<td><input type='hidden' value='" + rs.getInt("IDPARQUE") + "' /><span>" + rs.getString("NOMPARQUE") + "</span></td>";
                filas += "<td><span>" + rs.getString("NOMCLIENTE") + "</span></td>";
                filas += "<td><span>" + rs.getString("MODULOS") + "</span></td>";
                filas += "<td><input type='hidden' value='" + rs.getInt("IDINSTALACION") + "' /><span>" + rs.getString("NOMINSTALACION") + "</span></td>";
                filas += "<td style='text-align: right;'><span>" + Util.formatMiles(rs.getString("LECTURAANTERIOR")) + "</span></td>";
                filas += "<td style='text-align: right;'><span>" + Util.formatMiles(rs.getString("LECTURAACTUAL")) + "</span></td>";
                filas += "<td style='text-align: right;'><span>" + Util.formatMiles(rs.getInt("CONSUMO")) + "</span></td>";
                filas += "<td><button type='button' onclick='calcular(" + rs.getInt("IDREMARCADOR") + ", " + rs.getInt("NUMREMARCADOR") + ", \"" + rs.getString("NUMSERIE") + "\", " + rs.getInt("CONSUMO") + ", \"" + entrada.getString("mes") + "\", " + rs.getInt("LECTURAANTERIOR") + ", " + rs.getInt("LECTURAACTUAL") + ", " + rs.getBigDecimal("MAX_DEMANDA_LEIDA") + ", " + rs.getBigDecimal("MAX_DEMANDA_HORA_PUNTA") + ", \"" + rs.getDate("FECHA_LECTURA_INICIAL") + "\", \"" + rs.getDate("FECHA_LECTURA_FINAL") + "\");' class='btn btn-sm btn-outline-success' style='padding: 0px 2px 0px 2px;'>Calcular Boleta</button></td>";
                filas += "</tr>";
                kwtotal += rs.getInt("CONSUMO");
                idcomuna = rs.getInt("IDCOMUNA");
                remarcador = new JSONObject();
                remarcador.put("idremarcador", rs.getInt("IDREMARCADOR"));
                remarcador.put("numremarcador", rs.getString("NUMREMARCADOR"));
                remarcador.put("numserie", rs.getString("NUMSERIE"));
                remarcador.put("idparque", rs.getInt("IDPARQUE"));
                remarcador.put("modulos", rs.getString("MODULOS"));
                remarcador.put("idinstalacion", rs.getInt("IDINSTALACION"));
                remarcador.put("consumo", rs.getInt("CONSUMO"));
                remarcadores.put(remarcador);
            }
            filas += "<tr class='table-info'>";
            filas += "<td colspan='8' style='text-align: right; padding-right:5px; font-weight: bold;'>Consumo Total Remarcadores: </td>";
            filas += "<td>" + kwtotal + " kW</td>";
            filas += "</tr>";

            filas += "<tr>";
            filas += "<td colspan='8' style='vertical-align: middle; text-align: right; padding-right:5px; font-weight: bold;'>Consumo Facturado del Empalme: " + entrada.getString("numempalme") + "</td>";
            filas += "<td><input type='number' onkeyup='calcularDiferencia();' class='form-control form-control-sm small' style='font-size: 0.9em; padding-top: 0px; padding-bottom: 0px;' id='consumo-facturado-empalme'/></td>";
            filas += "</tr>";

            filas += "<tr>";
            filas += "<td colspan='8' style='text-align: right; padding-right:5px; font-weight: bold;'>KW Diferencia: </td>";
            filas += "<td><span id='kw-diferencia'></span></td>";
            filas += "</tr>";

            filas += "<tr>";
            filas += "<td colspan='8' style='text-align: right; padding-right:5px; font-weight: bold;'>% Diferencia: </td>";
            filas += "<td><span id='porc-diferencia'></span></td>";
            filas += "</tr>";

            tabla += filas;
            tabla += "</tbody></table>";
            salida.put("tabla", tabla);
            salida.put("remarcadores", remarcadores);
            salida.put("kwtotal", kwtotal);
            salida.put("idcomuna", idcomuna);
            salida.put("estado", "ok");
        } catch (JSONException | SQLException ex) {
            System.out.println("Problemas en controlador.RemarcadorController.getRemarcadoresNumEmpalmeBoleta().");
            System.out.println(ex);
            ex.printStackTrace();
            salida.put("estado", "error");
            salida.put("error", ex);
        }
        c.cerrar();
        return salida;
    }

    private JSONObject getSelectRemarcadorIdEmpalme(JSONObject entrada) {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_GET_REMARCADORES_IDEMPALME(" + entrada.getInt("idempalme") + ")";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        String options = modelo.Util.armarSelect(rs, "0", "Seleccione", "IDREMARCADOR", "NUMREMARCADOR");
        salida.put("options", options);
        salida.put("estado", "ok");
        c.cerrar();
        return salida;
    }

    private JSONObject getRemarcadoresLibres() {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_GET_REMARCADORES_LIBRES()";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        String filas = "";
        try {
            while (rs.next()) {
                filas += "<tr>";
                filas += "<td><input type='hidden' value='" + rs.getInt("IDREMARCADOR") + "' /><span>" + rs.getString("NUMREMARCADOR") + "</span></td>";
                filas += "<td><input type='hidden' value='" + rs.getString("NUMSERIE") + "' /><span>" + rs.getString("NUMSERIE") + "</span></td>";
                filas += "<td><input type='hidden' value='" + rs.getInt("IDEMPALME") + "' /><span>" + rs.getString("NUMEMPALME") + "</span></td>";
                filas += "<td><input type='hidden' value='" + rs.getInt("IDPARQUE") + "' /><span>" + rs.getString("NOMPARQUE") + "</span></td>";
                filas += "<td><span>" + rs.getString("MODULOS") + "</span></td>";
                filas += "<td><input type='hidden' value='" + rs.getInt("IDINSTALACION") + "' /><span>" + rs.getString("NOMINSTALACION") + "</span></td>";
                filas += "<td><a href='#' style='color:#669900; font-size:12px;' class='oi oi-check success' onclick='asignar(" + rs.getInt("IDREMARCADOR") + ")'></a></td>";
                filas += "</tr>";
            }
            salida.put("tabla", filas);
            salida.put("estado", "ok");
        } catch (JSONException | SQLException ex) {
            System.out.println("Problemas en controlador.RemarcadorController.getRemarcadores().");
            System.out.println(ex);
            ex.printStackTrace();
            salida.put("estado", "error");
            salida.put("error", ex);
        }
        c.cerrar();
        return salida;
    }

    private JSONObject getRemarcadoresAsignados() {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_GET_REMARCADORES_ASIGNADOS()";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        String filas = "";
        String tabla = "<table style='font-size: 10px;' id='tabla-remarcadores-asignados' class='table table-bordered table-sm small'>";
        tabla += "<thead><tr>";
        tabla += "<th># Remarcador</th>";
        tabla += "<th>Nº Serie</th>";
        tabla += "<th># Empalme</th>";
        tabla += "<th>Bodega</th>";
        tabla += "<th>Módulos</th>";
        tabla += "<th>Instalación</th>";
        tabla += "<th>Cliente</th>";
        tabla += "<th style='width: 10%;'>Fecha Asignación</th>";
        tabla += "</tr></thead><tbody>";
        try {
            while (rs.next()) {
                filas += "<tr>";
                filas += "<td>" + rs.getInt("NUMREMARCADOR") + "</td>";
                filas += "<td>" + rs.getString("NUMSERIE") + "</td>";
                filas += "<td>" + rs.getString("NUMEMPALME") + "</td>";
                filas += "<td>" + rs.getString("NOMPARQUE") + "</td>";
                filas += "<td>" + rs.getString("MODULOS") + "</td>";
                filas += "<td>" + rs.getString("NOMINSTALACION") + "</td>";
                filas += "<td>" + rs.getString("NOMCLIENTE") + "</td>";
                filas += "<td>" + rs.getDate("FECHAASIGNACION") + "</td>";
                filas += "</tr>";
            }
            tabla += filas;
            tabla += "</tbody></table>";

            salida.put("tabla", tabla);
            salida.put("estado", "ok");
        } catch (JSONException | SQLException ex) {
            System.out.println("Problemas en controlador.RemarcadorController.getRemarcadoresAsignados().");
            System.out.println(ex);
            ex.printStackTrace();
            salida.put("estado", "error");
            salida.put("error", ex);
        }
        c.cerrar();
        return salida;
    }

    private JSONObject insRemarcador(JSONObject entrada) {
        System.out.println(entrada);
        JSONObject salida = new JSONObject();
        String query = "CALL SP_INS_REMARCADOR("
                + "" + entrada.getInt("idempalme") + ","
                + "" + entrada.getInt("idparque") + ","
                + "'" + entrada.getString("numremarcador") + "',"
                + "'" + entrada.getString("numserie") + "',"
                + "'" + entrada.getString("modulos") + "'"
                + ")";
        Conexion c = new Conexion();
        c.abrir();
        c.ejecutar(query);
        c.cerrar();
        salida.put("estado", "ok");
        return salida;
    }

    private JSONObject getRemarcadorIdRemarcador(JSONObject entrada) {
        int idremarcador = entrada.getInt("idremarcador");
        JSONObject salida = new JSONObject();
        JSONObject remarcador = new JSONObject();
        String query = "CALL SP_GET_REMARCADOR_IDREMARCADOR(" + idremarcador + ")";
        System.out.println(query);
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        try {
            while (rs.next()) {
                remarcador.put("idremarcador", rs.getInt("IDREMARCADOR"));
                remarcador.put("idparque", rs.getInt("IDPARQUE"));
                remarcador.put("idempalme", rs.getInt("IDEMPALME"));
                remarcador.put("idequipomodbus", rs.getInt("IDEQUIPOMODBUS"));
                remarcador.put("numremarcador", rs.getInt("NUMREMARCADOR"));
                remarcador.put("numserie", rs.getString("NUMSERIE"));
                remarcador.put("modulos", rs.getString("MODULOS"));
                remarcador.put("idinstalacion", rs.getInt("IDINSTALACION"));
                remarcador.put("numempalme", rs.getString("NUMEMPALME"));
                remarcador.put("nomparque", rs.getString("NOMPARQUE"));
                remarcador.put("nominstalacion", rs.getString("NOMINSTALACION"));
                remarcador.put("direccion", rs.getString("DIRECCION"));
                remarcador.put("nomcomuna", rs.getString("NOMCOMUNA"));
            }
            salida.put("remarcador", remarcador);
            salida.put("estado", "ok");
        } catch (JSONException | SQLException ex) {
            System.out.println("Problemas en controlador.RemarcadorController.getRemarcadorIdRemarcador().");
            System.out.println(ex);
            ex.printStackTrace();
            salida.put("estado", "error");
            salida.put("error", ex);
        }
        c.cerrar();
        return salida;
    }

    private JSONObject getRemarcadorClienteIdRemarcador(JSONObject entrada) {
        int idremarcador = entrada.getInt("idremarcador");
        JSONObject salida = new JSONObject();
        JSONObject remarcador = new JSONObject();
        String query = "CALL SP_GET_REMARCADOR_CLIENTE_IDREMARCADOR(" + idremarcador + ")";
        System.out.println(query);
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        try {
            while (rs.next()) {
                remarcador.put("idremarcador", rs.getInt("IDREMARCADOR"));
                remarcador.put("numremarcador", rs.getInt("NUMREMARCADOR"));
                remarcador.put("numserie", rs.getString("NUMSERIE"));
                remarcador.put("idcliente", rs.getInt("IDCLIENTE"));
                remarcador.put("modulos", rs.getString("MODULOS"));
                remarcador.put("rutcliente", rs.getInt("RUTCLIENTE"));
                remarcador.put("dvcliente", rs.getString("DVCLIENTE"));
                remarcador.put("nomcliente", rs.getString("NOMCLIENTE"));
                remarcador.put("razoncliente", rs.getString("RAZONCLIENTE"));
                remarcador.put("direccion", rs.getString("DIRECCION"));
                remarcador.put("persona", rs.getString("PERSONA"));
                remarcador.put("cargo", rs.getString("CARGO"));
                remarcador.put("fono", rs.getInt("FONO"));
                remarcador.put("email", rs.getString("EMAIL"));
                remarcador.put("idempalme", rs.getInt("IDEMPALME"));
                remarcador.put("numempalme", rs.getString("NUMEMPALME"));
                remarcador.put("idparque", rs.getInt("IDPARQUE"));
                remarcador.put("nomparque", rs.getString("NOMPARQUE"));
                remarcador.put("idinstalacion", rs.getInt("IDINSTALACION"));
                remarcador.put("nominstalacion", rs.getString("NOMINSTALACION"));
                remarcador.put("idcomuna", rs.getInt("IDCOMUNA"));
                remarcador.put("nomcomuna", rs.getString("NOMCOMUNA"));
                remarcador.put("idred", rs.getInt("IDRED"));
                remarcador.put("nomred", rs.getString("NOMRED"));
                remarcador.put("dmps", rs.getString("DEM_MAX_POTENCIA_SUMINISTRADA"));
                remarcador.put("dmplhp", rs.getString("DEM_MAX_POTENCIA_LEIDA_H_PUNTA"));
            }
            salida.put("remarcador", remarcador);
            salida.put("estado", "ok");
        } catch (JSONException | SQLException ex) {
            System.out.println("Problemas en controlador.RemarcadorController.getRemarcadorIdRemarcador().");
            System.out.println(ex);
            ex.printStackTrace();
            salida.put("estado", "error");
            salida.put("error", ex);
        }
        c.cerrar();
        return salida;
    }

    private JSONObject getRegistrosMesRemarcador(JSONObject entrada) {

        JSONObject salida = new JSONObject();
        String query = "CALL SP_GET_REGISTROS_MES_REMARCADOR("
                + entrada.getInt("idremarcador") + ", "
                + entrada.getInt("messolo") + ", "
                + entrada.getInt("aniosolo")
                + ")";
        Conexion c = new Conexion();
        System.out.println(query);
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        String filas = "";
        String tabla = "<table style='font-size: 10px;' id='tabla-detalle-remarcador' class='table table-bordered table-hover table-sm small'>";
        tabla += "<thead><tr>";
        tabla += "<th>FECHA REGISTRO</th>";
        tabla += "<th>ID REMARCADOR</th>";
        tabla += "<th>ENERGIA ACTIVA CONSUMIDA (KWH)</th>";
        tabla += "<th>POTENCIA ACTIVA TOTAL (KW)</th>";
        tabla += "</tr></thead><tbody>";
        int cont = 0;
        try {
            while (rs.next()) {
                filas += "<tr>";
                filas += "<td><span>" + rs.getString("FECHAREGISTRO") + "</span></td>";
                filas += "<td><span>" + rs.getInt("IDREMARCADOR") + "</span></td>";
                filas += "<td><span>" + rs.getInt("EnergiaActivaConsumida_KWH") + "</span></td>";
                filas += "<td><span>" + rs.getBigDecimal("PotenciaActivaTotal_KW") + "</span></td>";
                filas += "</tr>";
                cont++;
            }
            tabla += filas;
            tabla += "</tbody></table>";
            salida.put("tabla", tabla);
            salida.put("estado", "ok");
        } catch (JSONException | SQLException ex) {
            System.out.println("Problemas en controlador.RemarcadorController.getRegistrosMesRemarcador().");
            System.out.println(ex);
            ex.printStackTrace();
            salida.put("estado", "error");
            salida.put("error", ex);
        }
        c.cerrar();
        return salida;
    }

    private JSONObject updRemarcador(JSONObject remarcador) {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_UPD_REMARCADOR("
                + remarcador.getInt("idremarcador") + ","
                + remarcador.getInt("idempalme") + ","
                + remarcador.getInt("idparque") + ","
                + remarcador.getInt("numremarcador") + ","
                + "'" + remarcador.getString("numserie") + "',"
                + "'" + remarcador.getString("modulos") + "')";
        System.out.println(query);
        Conexion c = new Conexion();
        c.abrir();
        c.ejecutar(query);
        c.cerrar();
        salida.put("estado", "ok");
        return salida;
    }

    private JSONObject getSelectRemarcadoresCliente(JSONObject entrada) {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_GET_SELECT_REMARCADORES_CLIENTE(" + entrada.getInt("idcliente") + ")";
        Conexion c = new Conexion();
        System.out.println(query);
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);

        String options = modelo.Util.armarSelect(rs, "0", "Seleccione", "IDREMARCADOR", "NUMREMARCADOR");
        salida.put("options", options);
        salida.put("estado", "ok");
        c.cerrar();
        return salida;
    }

    private JSONObject delRemarcador(JSONObject remarador) {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_DEL_REMARCADOR(" + remarador.getInt("idremarcador") + ")";
        Conexion c = new Conexion();
        c.abrir();
        c.ejecutar(query);
        salida.put("estado", "ok");
        c.cerrar();
        return salida;
    }

}
