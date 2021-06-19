/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import modelo.Conexion;

/**
 *
 * @author jorge
 */
public class DestinatarioController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html; charset=UTF-8");
        JSONObject entrada = new JSONObject(request.getParameter("datos"));
        switch (entrada.getString("tipo")) {
            case "get-destinatarios":
                out.print(getDestinatarios());
                break;
            case "get-destinatario-iddestinatario":
                out.print(getDestinatarioIdDestinatario(entrada.getInt("iddestinatario")));
                break;
            case "existe-destinatario":
                out.print(existeDestinatario(entrada.getJSONObject("destinatario")));
                break;
            case "ins-destinatario":
                out.print(insDestinatario(entrada));
                break;
            case "del-destinatario":
                out.print(delDestinatario(entrada));
                break;
            case "upd-destinatario":
                out.print(updDestinatario(entrada.getJSONObject("destinatario")));
                break;
        }
    }

    private JSONObject getDestinatarios() {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_GET_DESTINATARIOS_NOTIFICACION()";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        String filas = "";
        try {
            while (rs.next()) {
                filas += "<tr>";
                filas += "<td>" + rs.getString("NOMDESTINATARIO") + "</td>";
                filas += "<td>" + rs.getString("EMAILDESTINATARIO") + "</td>";
                filas += "<td>" + rs.getString("NOMINSTALACION") + "</td>";
                filas += "<td style='width: 15%;'>"
                        + "<button style='font-size:10px; padding: 0.1 rem 0.1 rem;' type='button' class='btn btn-sm btn-warning' onclick='editar(" + rs.getString("IDDESTINATARIO") + ")'>Editar</button>"
                        + "<button style='font-size:10px; padding: 0.1 rem 0.1 rem;' type='button' class='btn btn-sm btn-danger' onclick='eliminar(" + rs.getString("IDDESTINATARIO") + ")'>Eliminar</button>"
                        + "</td>";
                filas += "</tr>";
            }
            salida.put("tabla", filas);
            salida.put("estado", "ok");
        } catch (JSONException | SQLException ex) {
            System.out.println("Problemas en controlador.DestinatarioController.getDestinatarios().");
            System.out.println(ex);
            ex.printStackTrace();
            salida.put("estado", "error");
            salida.put("error", ex);
        }
        c.cerrar();
        return salida;
    }

    private JSONObject getDestinatarioIdDestinatario(int iddestinatario) {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_GET_DESTINATARIO_IDDESTINATARIO(" + iddestinatario + ")";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        try {
            while (rs.next()) {
                JSONObject destinatario = new JSONObject();
                destinatario.put("iddestinatario", rs.getInt("IDDESTINATARIO"));
                destinatario.put("nomdestinatario", rs.getString("NOMDESTINATARIO"));
                destinatario.put("emaildestinatario", rs.getString("EMAILDESTINATARIO"));
                destinatario.put("idinstalacion", rs.getInt("IDINSTALACION"));
                destinatario.put("nominstalacion", rs.getString("NOMINSTALACION"));
                salida.put("destinatario", destinatario);
            }

            salida.put("estado", "ok");
        } catch (JSONException | SQLException ex) {
            System.out.println("Problemas en controlador.DestinatarioController.getDestinatarioIdDestinatario().");
            System.out.println(ex);
            ex.printStackTrace();
            salida.put("estado", "error");
            salida.put("error", ex);
        }
        c.cerrar();
        return salida;
    }

    private JSONObject existeDestinatario(JSONObject destinatario) {
        JSONObject salida = new JSONObject();
        String query = "SELECT COUNT(*) CUENTA FROM DESTINATARIONOTIFICACION "
                + "WHERE NOMDESTINATARIO = '" + destinatario.getString("nomdestinatario") + "' "
                + "AND EMAILDESTINATARIO = '" + destinatario.getString("emaildestinatario") + "'";
        Conexion c = new Conexion();
        System.out.println(query);
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        int cantidad = 0;
        try {
            while (rs.next()) {
                cantidad += rs.getInt("CUENTA");
            }
        } catch (Exception ex) {
            System.out.println("no se pudo verificar si ya existe el destinatario");
            System.out.println(ex);
            ex.printStackTrace();
            salida.put("estado", "error");
        }
        salida.put("cantidad", cantidad);
        salida.put("estado", "ok");
        return salida;
    }

    private JSONObject updDestinatario(JSONObject destinatario) {
        String query = "CALL SP_UPD_DESTINATARIO_NOTIFICACION("
                + destinatario.getInt("iddestinatario") + ", "
                + "'" + destinatario.getString("nomdestinatario") + "', "
                + "'" + destinatario.getString("emaildestinatario") + "', "
                + destinatario.getString("idinstalacion")
                + ")";
        Conexion c = new Conexion();
        c.abrir();
        c.ejecutar(query);
        c.cerrar();
        JSONObject salida = new JSONObject();
        salida.put("estado", "ok");
        return salida;
    }

    private JSONObject insDestinatario(JSONObject entrada) {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_INS_DESTINATARIO_NOTIFICACIONES("
                + "'" + entrada.getString("nombres") + "',"
                + "'" + entrada.getString("email") + "',"
                + entrada.getInt("idinstalacion") + ")";

        Conexion c = new Conexion();
        c.abrir();
        c.ejecutar(query);
        c.cerrar();
        salida.put("estado", "ok");
        return salida;
    }

    private JSONObject delDestinatario(JSONObject entrada) {
        int iddestinatario = entrada.getInt("iddestinatario");
        JSONObject salida = new JSONObject();
        String query = "CALL SP_DEL_DESTINATARIO_NOTIFICACIONES(" + iddestinatario + ")";
        Conexion c = new Conexion();
        c.abrir();
        c.ejecutar(query);
        c.cerrar();
        salida.put("estado", "ok");
        return salida;
    }

}
