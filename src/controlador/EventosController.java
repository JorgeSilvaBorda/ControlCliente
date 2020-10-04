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

public class EventosController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html; charset=UTF-8");
        JSONObject entrada = new JSONObject(request.getParameter("datos"));
        switch (entrada.getString("tipo")) {
            case "actualiza-nuevos-eventos":
                out.print(actualizaNuevosEventos());
                break;
            case "get-nuevos-eventos-comunicacion":
                out.print(getNuevosEventosComuniacacion());
                break;
            case "marcar-evento-leido":
                out.print(marcarLeido(entrada));
                break;
        }
    }

    private JSONObject actualizaNuevosEventos() {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_GET_CANT_EVENTOS()";
        System.out.println(query);
        Conexion c = new Conexion();
        c.abrir();
        try {
            ResultSet rs = c.ejecutarQuery(query);
            while (rs.next()) {
                salida.put("comunicacion", rs.getInt("COMUNICACION"));
                salida.put("continuidad", rs.getInt("CONTINUIDAD"));
                salida.put("total", rs.getInt("TOTAL"));
            }
            salida.put("estado", "ok");
            return salida;
        } catch (JSONException | SQLException ex) {
            salida.put("error", ex);
        }
        c.cerrar();
        return salida;
    }

    private JSONObject getNuevosEventosComuniacacion() {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_GET_NUEVOS_EVENTOS_COMUNICACION()";
        System.out.println(query);
        Conexion c = new Conexion();
        c.abrir();
        int cont = 0;
        String tabla = "";
        try {
            ResultSet rs = c.ejecutarQuery(query);
            while (rs.next()) {
                tabla += "<tr>";
                tabla += "<td>" + rs.getString("FECHAFORMAT") + "</td>";
                tabla += "<td>" + rs.getString("HORA") + "</td>";
                tabla += "<td>" + rs.getInt("REMARCADOR_ID") + "</td>";
                tabla += "<td>" + rs.getString("NOMPARQUE") + "</td>";
                tabla += "<td>" + rs.getString("NOMINSTALACION") + "</td>";
                tabla += "<td>" + rs.getString("NOMCLIENTE") + "</td>";
                tabla += "<td><button type='button' onclick='marcarLeido(this, " + rs.getInt("IDEVENTO") + ");' class='btn btn-outline-success btn-block btn-sm'>Marcar Leído</button></td>";
                tabla += "</tr>";
                cont++;
            }
            salida.put("estado", "ok");
            salida.put("cant", cont);
            salida.put("cuerpo", tabla);

        } catch (JSONException | SQLException ex) {
            salida.put("error", ex);
            System.out.println("Problemas al obtener los nuevos eventos de comunicación.");
            System.out.println(ex);
        }
        c.cerrar();
        System.out.println(salida);
        return salida;
    }

    private JSONObject marcarLeido(JSONObject entrada) {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_MARCAR_EVENTO_LEIDO("
                + entrada.getInt("idevento")
                + ")";
        System.out.println(query);
        Conexion c = new Conexion();
        c.abrir();
        c.ejecutar(query);
        c.cerrar();
        salida.put("estado", "ok");
        return salida;
    }
}
