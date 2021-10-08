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

public class ConversorController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html; charset=UTF-8");
        HttpSession session = request.getSession();
        System.out.println("Request" + request.getParameter("datos"));
        JSONObject entrada = new JSONObject(request.getParameter("datos"));
        switch (entrada.getString("tipo")) {
            case "get-conversores":
                out.print(getConversores());
                break;

        }
    }

    public JSONObject getConversores() {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_GET_CONVERSORES_REMARCADOR()";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        String filas = "";
        try {
            while (rs.next()) {

                filas += "<tr>";
                filas += "<td><input type='hidden' value='" + rs.getInt("IDCONVERSOR") + "' /><span>" + rs.getString("IPCONVERSOR") + "</span></td>";
                filas += "<td><span>" + rs.getInt("NUMCONVERSOR") + "</span></td>";
                filas += "<td><span>" + rs.getInt("NUMPUERTO") + "</span></td>";
                filas += "<td><span>" + rs.getInt("NUMREMARCADOR") + "</span></td>";
                filas += "<td><span>" + rs.getString("TIPOREMARCADOR") + "</span></td>";
                filas += "<td style='width: 10%;'>";

                filas += "<button style='font-size:10px; padding: 0.1 rem 0.1 rem;' type='button' class='btn btn-sm btn-warning' onclick='activarEdicion(this)'>Editar</button>";
                filas += "<button style='font-size:10px; padding: 0.1 rem 0.1 rem;' type='button' class='btn btn-sm btn-danger' onclick='eliminar(this)'>Eliminar</button>";

                filas += "</td>";
                filas += "</tr>";
            }
            salida.put("tabla", filas);
            salida.put("estado", "ok");
        } catch (JSONException | SQLException ex) {
            System.out.println("Problemas en controlador.ConversorController.getConversores().");
            System.out.println(ex);
            ex.printStackTrace();
            salida.put("estado", "error");
            salida.put("error", ex);
        }
        c.cerrar();
        return salida;
    }

}
