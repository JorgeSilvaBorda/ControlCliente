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
            case "get-remarcadores-libres":
                out.print(getRemarcadoresLibres());
                break;
            case "get-remarcadores-asignados":
                out.print(getRemarcadoresAsignados());
                break;
            case "get-select-remarcadores-cliente":
                out.print(getSelectRemarcadoresCliente(entrada));
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
        //tabla += "<th># Empalme</th>";
        tabla += "<th>Bodega</th>";
        tabla += "<th>Cliente</th>";
        tabla += "<th>Módulos</th>";
        tabla += "<th>Instalación</th>";
        tabla += "</tr></thead><tbody>";
        JSONObject remarcador;
        JSONArray remarcadores = new JSONArray();
        try {
            while (rs.next()) {
                filas += "<tr>";
                filas += "<td><input type='hidden' value='" + rs.getInt("IDREMARCADOR") + "' /><span>" + rs.getString("NUMREMARCADOR") + "</span></td>";
                //filas += "<td><input type='hidden' value='" + rs.getInt("IDEMPALME") + "' /><span>" + rs.getString("NUMEMPALME") + "</span></td>";
                filas += "<td><input type='hidden' value='" + rs.getInt("IDPARQUE") + "' /><span>" + rs.getString("NOMPARQUE") + "</span></td>";
                filas += "<td><span>" + rs.getString("NOMCLIENTE") + "</span></td>";
                filas += "<td><span>" + rs.getString("MODULOS") + "</span></td>";
                filas += "<td><input type='hidden' value='" + rs.getInt("IDINSTALACION") + "' /><span>" + rs.getString("NOMINSTALACION") + "</span></td>";
                filas += "</tr>";
                remarcador = new JSONObject();
                remarcador.put("idremarcador", rs.getInt("IDREMARCADOR"));
                remarcador.put("numremarcador", rs.getString("NUMREMARCADOR"));
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
        JSONObject salida = new JSONObject();
        String query = "CALL SP_INS_REMARCADOR("
                + "" + entrada.getInt("idempalme") + ","
                + "" + entrada.getInt("idparque") + ","
                + "'" + entrada.getString("numremarcador") + "',"
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
        tabla += "<th>TIMESTAMP</th>";
        tabla += "<th>FECHA</th>";
        tabla += "<th>AÑO</th>";
        tabla += "<th>MES</th>";
        tabla += "<th>DIA</th>";
        tabla += "<th>HORA</th>";
        tabla += "<th>ITEM95</th>";
        tabla += "<th>ITEM96</th>"; 
        tabla += "</tr></thead><tbody>";
        int cont = 0;
        try {
            while (rs.next()) {
                filas += "<tr>";
                filas += "<td><span>" + rs.getString("TIMESTAMP") + "</span></td>";
                filas += "<td><span>" + rs.getString("FECHA") + "</span></td>";
                filas += "<td><span>" + rs.getString("ANIO") + "</span></td>";
                filas += "<td><span>" + rs.getString("MES") + "</span></td>";
                filas += "<td><span>" + rs.getString("DIA") + "</span></td>";
                filas += "<td><span>" + rs.getString("HORA") + "</span></td>";
                filas += "<td><span>" + rs.getString("ITEM95") + "</span></td>";
                filas += "<td><span>" + rs.getString("ITEM96") + "</span></td>";
                filas += "</tr>";
                cont ++;
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
                + "'" + remarcador.getString("modulos") + "')";
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
