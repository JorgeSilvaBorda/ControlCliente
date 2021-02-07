package controlador;

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
import javax.servlet.http.HttpSession;
import modelo.Conexion;

public class ClienteRemarcadorController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        response.setContentType("text/html; charset=UTF-8");
        JSONObject entrada = new JSONObject(request.getParameter("datos"));
        switch (entrada.getString("tipo")) {
            case "get-remarcadores-asignados-idcliente":
                out.print(getRemarcadoresAsignadosIdCliente(entrada));
                break;
            case "asignar-remarcador-cliente":
                out.print(asignarRemarcadorCliente(entrada, session));
                break;
            case "quitar-remarcador-cliente":
                out.print(quitarRemarcadorCliente(entrada, session));
                break;
            case "get-last-asignacion-remarcador-cliente":
                out.print(getLastAsigncionRemarcadorCliente(entrada));
                break;
            case "reasignar-remarcador-cliente":
                out.print(reasignarRemarcadorCliente(entrada, session));
                break;
        }
    }

    private JSONObject getRemarcadoresAsignadosIdCliente(JSONObject entrada) {
        int idcliente = entrada.getInt("idcliente");
        JSONObject salida = new JSONObject();
        String query = "CALL SP_GET_REMARCADORES_ASIGNADOS_IDCLIENTE(" + idcliente + ")";
        System.out.println(query);
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
                filas += "<td><input type='hidden' value='" + rs.getInt("IDINSTALACION") + "' /><span>" + rs.getString("NOMINSTALACION") + "</span></td>";
                filas += "<td><span>" + rs.getString("PERSONA") + "</span></td>";
                filas += "<td><span>" + rs.getString("FONO") + "</span></td>";
                filas += "<td><span>" + rs.getString("FECHAASIGNACION") + "</span></td>";
                filas += "<td><span>" + rs.getString("NOMUSUARIO") + "</span></td>";
                filas += "<td>"
                        + "<a href='#' style='color:#D25E45; font-size:12px;' class='oi oi-x' onclick='quitar(" + rs.getInt("IDREMARCADOR") + ", " + rs.getInt("IDCLIENTE") + ")'></a>"
                        + "</td>";
                filas += "<td>"
                        + "<a href='#' style='font-size:12px;' class='oi oi-chevron-right warning' onclick='panelReAsignacion(" + rs.getInt("IDREMARCADOR") + ", " + rs.getInt("IDCLIENTE") + ")'></a>"
                        + "</td>";
                filas += "</tr>";
            }
            salida.put("tabla", filas);
            salida.put("estado", "ok");
        } catch (JSONException | SQLException ex) {
            System.out.println("Problemas en controlador.ClienteRemarcadorController.getRemarcadoresAsignadosIdCliente().");
            System.out.println(ex);
            ex.printStackTrace();
            salida.put("estado", "error");
            salida.put("error", ex);
        }
        c.cerrar();
        return salida;
    }

    private JSONObject getLastAsigncionRemarcadorCliente(JSONObject entrada) {
        int idcliente = entrada.getInt("idclienteanterior");
        int idremarcador = entrada.getInt("idremarcador");

        String select = "<select style='font-size: 1em;' id='select-cliente-reasig' onchange='getContactosClienteIdCliente($(this).val());'class='form-control form-control-sm small' ></select>";
        String selectcontacto = "<select style='font-size: 1em;' id='select-contacto-reasig' class='form-control form-control-sm small' ></select>";

        JSONObject salida = new JSONObject();
        String query = "CALL SP_GET_LAST_ASIG_REMARCADOR_CLIENTE(" + idcliente + ", " + idremarcador + ")";
        System.out.println(query);
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        String filas = "<table style='border: none; border-collapse: collapse; font-size: 0.7em;'><tbody>";
        try {
            while (rs.next()) {
                filas += "<tr>";
                filas += "<td style='font-weight: bold; padding: 2px 1px 4px 1px;'>Remarcador ID:</td>";
                filas += "<td>" + rs.getInt("NUMREMARCADOR") + "<input type='hidden' id='hid-id-remarcador' value='" + idremarcador + "'/></td>";
                filas += "<td style='font-weight: bold; padding: 2px 1px 4px 1px;'>Cliente actual:</td>";
                filas += "<td colspan='2' >" + rs.getString("NOMCLIENTE") + "<input type='hidden' id='hid-id-cliente-anterior' value='" + idcliente + "'/></td>";
                filas += "</tr>";

                filas += "<tr>";
                filas += "<td colspan='1' style='font-weight: bold; padding: 2px 1px 4px 1px;'>Nuevo Cliente:</td>";
                filas += "<td colspan='1' >" + select + "</td>";
                filas += "<td colspan='1' style='font-weight: bold; padding: 2px 1px 4px 1px;'>Contacto:</td>";
                filas += "<td colspan='1' >" + selectcontacto + "</td>";
                filas += "<td colspan='1' >" + "<button style='font-size: 1em;' type='button' onclick='reasignarRemarcador(" + rs.getInt("IDCLIENTE") + ", " + rs.getInt("IDREMARCADOR") + ")' class='btn btn-sm btn-warning'>Reasignar</button>" + "</td>";
                filas += "</tr>";

            }
            filas += "</body></table>";
            salida.put("tabla", filas);
            salida.put("estado", "ok");
        } catch (JSONException | SQLException ex) {
            System.out.println("Problemas en controlador.ClienteRemarcadorController.getRemarcadoresAsignadosIdCliente().");
            System.out.println(ex);
            ex.printStackTrace();
            salida.put("estado", "error");
            salida.put("error", ex);
        }
        c.cerrar();
        return salida;
    }

    private JSONObject asignarRemarcadorCliente(JSONObject entrada, HttpSession session) {
        int idusuario = Integer.parseInt(session.getAttribute("idusuario").toString());
        JSONObject salida = new JSONObject();
        String query = "CALL SP_ASIGNAR_REMARCADOR_CLIENTE("
                + entrada.getInt("idremarcador") + ", "
                + entrada.getInt("idcliente") + ", "
                + entrada.getInt("idcontacto") + ", "
                + idusuario
                + ")";
        System.out.println(query);
        Conexion c = new Conexion();
        c.abrir();
        c.ejecutar(query);
        salida.put("estado", "ok");
        c.cerrar();
        return salida;
    }

    private JSONObject quitarRemarcadorCliente(JSONObject entrada, HttpSession session) {
        JSONObject salida = new JSONObject();
        int idusuario = Integer.parseInt(session.getAttribute("idusuario").toString());
        String query = "CALL SP_QUITAR_REMARCADOR_CLIENTE("
                + entrada.getInt("idremarcador") + ", "
                + entrada.getInt("idcliente") + ", "
                + idusuario + ""
                + ")";
        Conexion c = new Conexion();
        c.abrir();
        c.ejecutar(query);
        salida.put("estado", "ok");
        c.cerrar();
        return salida;
    }

    private JSONObject reasignarRemarcadorCliente(JSONObject entrada, HttpSession session) {
        JSONObject jsonAsigna = new JSONObject();
        JSONObject jsonQuita = new JSONObject();

        jsonQuita.put("idremarcador", entrada.getInt("idremarcador"));
        jsonQuita.put("idcliente", entrada.getInt("idclienteanterior"));
        JSONObject salidaquita = quitarRemarcadorCliente(jsonQuita, session);
        JSONObject salida = new JSONObject();
        if (salidaquita.getString("estado").equals("ok")) {
            jsonAsigna.put("idremarcador", entrada.getInt("idremarcador"));
            jsonAsigna.put("idcliente", entrada.getInt("idcliente"));
            jsonAsigna.put("idcontacto", entrada.getInt("idcontacto"));
            salida = asignarRemarcadorCliente(jsonAsigna, session);
            return salida;
        }
        salida.put("estado", "error");
        return salida;
    }
    
}
