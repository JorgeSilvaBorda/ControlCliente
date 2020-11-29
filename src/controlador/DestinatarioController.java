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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html; charset=UTF-8");
        JSONObject entrada = new JSONObject(request.getParameter("datos"));
        switch (entrada.getString("tipo")) {
            case "get-destinatarios":
                out.print(getDestinatarios());
                break;
            case "ins-destinatario":
                out.print(insDestinatario(entrada));
                break;
            case "del-destinatario":
                out.print(delDestinatario(entrada));
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
                filas += "<td style='width: 10%;'>"
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
    
    private JSONObject insDestinatario(JSONObject entrada) {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_INS_DESTINATARIO_NOTIFICACIONES("
                + "'" + entrada.getString("nombres") + "',"
                + "'" + entrada.getString("email") + "')";

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
