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

public class UsuarioController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html; charset=UTF-8");
        JSONObject entrada = new JSONObject(request.getParameter("datos"));
        switch (entrada.getString("tipo")) {
            /*
            case "login":
                out.print(loginUsuario(entrada.getJSONObject("usuario"), request));
                break;
                */
            case "get-usuarios":
                out.print(getUsuarios());
                break;
            case "get-usuario-idusuario":
                out.print(getUsuarioIdUsuario(entrada));
                break;
            case "carga-select-tipousuario":
                out.print(getTipoUsuarios());
                break;
            /*
            case "existe-usuario":
                out.print(existeUsuario(entrada.getInt("rutusuario")));
                break;
             */
            case "ins-usuario":
                out.print(insUsuario(entrada));
                break;
            case "upd-usuario":
                out.print(updUsuario(entrada.getJSONObject("usuario")));
                break;
            /*    
            case "del-usuario":
                out.print(delUsuario(entrada.getInt("idusuario")));
                break;
             */
            case "reset-pass-usuario":
                out.print(resetPassUsuario(entrada));
                break;
            case "cambio-pass":
                out.print(cambioPass(entrada.getString("rutfull"), entrada.getString("claveAnterior"), entrada.getString("claveNueva"), request));
                break;
        }
    }

    private JSONObject loginUsuario(JSONObject credenciales, HttpServletRequest request) {
        JSONObject salida = new JSONObject();
        int rut = Integer.parseInt(credenciales.getString("rut").replace("\\.", "").replace("\\.", ""));
        String password = credenciales.getString("password");

        String query = "CALL SP_VALIDA_USUARIO(" + rut + ", '" + modelo.Util.hashMD5(password) + "')";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        int cont = 0;
        try {
            while (rs.next()) {
                JSONObject usuario = new JSONObject();
                usuario.put("idusuario", rs.getInt("IDUSUARIO"));
                usuario.put("idtipousuario", rs.getInt("IDTIPOUSUARIO"));
                usuario.put("nomtipousuario", rs.getString("NOMTIPOUSUARIO"));
                usuario.put("rut", rs.getInt("RUT"));
                usuario.put("dv", rs.getString("DV"));
                usuario.put("nombres", rs.getString("NOMBRES"));
                usuario.put("appaterno", rs.getString("APPATERNO"));
                usuario.put("apmaterno", rs.getString("APMATERNO"));
                cont++;
                salida.put("usuario", usuario);
            }
            salida.put("filas", cont);
            if (cont < 1) {
                salida.put("estado", "no-valido");
                salida.put("mensaje", "No se encuentra el registro de usuario/clave ingresados.");
                request.getSession().invalidate();
            }else if(cont > 1){
                salida.put("estado", "no-valido");
                salida.put("mensaje", "No se encuentra el registro de usuario/clave ingresados.");
                System.out.println("[WRN] Se encuentra más de una combinación Usuario(rut)/password!");
                System.out.println("    Rut: " + rut);
                request.getSession().invalidate();
            } else {
                salida.put("estado", "ok");
                HttpSession session = request.getSession();
                JSONObject usuario = salida.getJSONObject("usuario");
                session.setAttribute("idusuario", usuario.getInt("idusuario"));
                session.setAttribute("idtipousuario", usuario.getInt("idtipousuario"));
                session.setAttribute("nomtipousuario", usuario.getString("nomtipousuario"));
                session.setAttribute("rut", usuario.getInt("rut"));
                session.setAttribute("dv", usuario.getString("dv"));
                session.setAttribute("nombres", usuario.getString("nombres"));
                session.setAttribute("appaterno", usuario.getString("appaterno"));
                session.setAttribute("apmaterno", usuario.getString("apmaterno"));
                session.setAttribute("usuario.json", usuario.toString());
            }
        } catch (JSONException | SQLException ex) {
            System.out.println("Problemas en  controlador.UsuarioControler.loginUsuario()");
            System.out.println(ex);
            salida.put("estado", "error");
            salida.put("mensaje", ex);
        }
        c.cerrar();
        return salida;
    }

    private JSONObject getUsuarios() {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_GET_USUARIOS()";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        String filas = "";
        try {
            while (rs.next()) {
                filas += "<tr>";
                filas += "<td><input type='hidden' value='" + rs.getInt("IDUSUARIO") + "' /><span>" + modelo.Util.formatRut(rs.getString("RUT") + "-" + rs.getString("DV")) + "</span></td>";
                filas += "<td>" + rs.getString("NOMBRES") + "</td>";
                filas += "<td>" + rs.getString("APPATERNO") + "</td>";
                filas += "<td>" + rs.getString("APMATERNO") + "</td>";
                filas += "<td><input type='hidden' value='" + rs.getInt("IDTIPOUSUARIO") + "' />" + rs.getString("NOMTIPOUSUARIO") + "</td>";
                filas += "<td><button style='font-size:10px; padding: 0.1 rem 0.1 rem;' type='button' class='btn btn-sm btn-warning' onclick='activarEdicion(this)'>Editar</button><button style='font-size:10px;  padding: 0.1 rem 0.1 rem;' type='button' class='btn btn-sm btn-success' onclick='modalCambiar(this)'>Cambio pass</button></td>";
                filas += "</tr>";
            }
            salida.put("tabla", filas);
            salida.put("estado", "ok");
        } catch (JSONException | SQLException ex) {
            System.out.println("Problemas en controlador.UsuarioController.getUsuarios().");
            System.out.println(ex);
            ex.printStackTrace();
            salida.put("estado", "error");
            salida.put("error", ex);
        }
        c.cerrar();
        return salida;
    }
    
    private JSONObject getUsuarioIdUsuario(JSONObject entrada) {
        int idusuario = entrada.getInt("idusuario");
        JSONObject salida = new JSONObject();
        JSONObject usuario = new JSONObject();
        String query = "CALL SP_GET_USUARIO_IDUSUARIO(" + idusuario + ")";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        try {
            while (rs.next()) {
                usuario.put("idusuario", rs.getInt("IDUSUARIO"));
                usuario.put("idtipousuario", rs.getInt("IDTIPOUSUARIO"));
                usuario.put("nomtipousuario", rs.getString("NOMTIPOUSUARIO"));
                usuario.put("rut", rs.getInt("RUT"));
                usuario.put("dv", rs.getString("DV"));
                usuario.put("nombres", rs.getString("NOMBRES"));
                usuario.put("appaterno", rs.getString("APPATERNO"));
                usuario.put("apmaterno", rs.getString("APMATERNO"));
            }
            salida.put("usuario", usuario);
            salida.put("estado", "ok");
        } catch (JSONException | SQLException ex) {
            System.out.println("Problemas en controlador.UsuarioController.getUsuarioIdUsuario().");
            System.out.println(ex);
            ex.printStackTrace();
            salida.put("estado", "error");
            salida.put("error", ex);
        }
        c.cerrar();
        return salida;
    }

    private JSONObject getTipoUsuarios() {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_GET_TIPOUSUARIOS()";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        String options = modelo.Util.armarSelect(rs, "0", "Seleccione", "IDTIPOUSUARIO", "DESCTIPOUSUARIO");
        salida.put("estado", "ok");
        salida.put("options", options);
        c.cerrar();
        return salida;
    }

    private JSONObject existeUsuario(int rutusuario) {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_EXISTE_USUARIO(" + rutusuario + ")";
        Conexion c = new Conexion();
        c.abrir();
        int cant = 0;
        try {
            ResultSet rs = c.ejecutarQuery(query);
            while (rs.next()) {
                cant = rs.getInt("CANTIDAD");
            }
            salida.put("estado", "ok");
            salida.put("cantidad", cant);
        } catch (SQLException ex) {
            System.out.println("Problemas en modelo.UsuarioController.existeUsuario()");
            System.out.println(ex);
            salida.put("estado", "error");
            salida.put("mensaje", ex);
        }
        c.cerrar();
        return salida;
    }

    private JSONObject insUsuario(JSONObject usuario) {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_INS_USUARIO("
                + usuario.getInt("rutusuario") + ", "
                + "'" + usuario.getString("dvusuario") + "', "
                + "'" + usuario.getString("nombres") + "', "
                + "'" + usuario.getString("appaterno") + "', "
                + "'" + usuario.getString("apmaterno") + "', "
                + usuario.getInt("idtipousuario") + ")";
        Conexion c = new Conexion();
        c.abrir();
        c.ejecutar(query);
        c.cerrar();
        salida.put("estado", "ok");
        return salida;
    }

    private JSONObject updUsuario(JSONObject usuario) {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_UPD_USUARIO("
                + usuario.getInt("idusuario") + ","
                + usuario.getInt("idtipousuario") + ","
                + usuario.getInt("rutusuario") + ","
                + "'" + usuario.getString("dvusuario") + "',"
                + "'" + usuario.getString("nombres") + "',"
                + "'" + usuario.getString("appaterno") + "',"
                + "'" + usuario.getString("apmaterno") + "')";
        Conexion c = new Conexion();
        c.abrir();
        c.ejecutar(query);
        c.cerrar();
        salida.put("estado", "ok");
        return salida;
    }

    private JSONObject delUsuario(int idusuario) {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_DEL_USUARIO(" + idusuario + ")";
        Conexion c = new Conexion();
        c.abrir();
        c.ejecutar(query);
        c.cerrar();
        salida.put("estado", "ok");
        return salida;
    }

    private JSONObject resetPassUsuario(JSONObject entrada) {
        JSONObject salida = new JSONObject();
        String rutfullusuario = entrada.getString("rutfullusuario");
        int rutusuario = Integer.parseInt(rutfullusuario.substring(0, rutfullusuario.length() - 1));
        int rutadmin = Integer.parseInt(entrada.getString("rutadmin"));
        String nuevapass = entrada.getString("nuevapass");
        String passMD5 = modelo.Util.hashMD5(nuevapass);
        String query = "CALL SP_RESET_PASSWORD(" + rutadmin + ", " + rutusuario + ", '" + passMD5 + "')";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        try {
            while (rs.next()) {
                if (Integer.parseInt(rs.getString("SALIDA")) == 0) {
                    salida.put("estado", "ok");
                    salida.put("mensaje", "Contraseña cambiada correctamente.");
                } else if (Integer.parseInt(rs.getString("SALIDA")) == -1) {
                    salida.put("estado", "ok");
                    salida.put("mensaje", "Usuario no autorizado para cambiar contraseñas.");
                }
            }
        } catch (JSONException | NumberFormatException | SQLException ex) {
            salida.put("estado", "error");
            salida.put("error", ex);
            System.out.println("No se pudo ejecutar el cambio de contraseña.");
            System.out.println(ex);
        }
        c.cerrar();
        return salida;
    }

    public JSONObject cambioPass(String rutfull, String passAnterior, String passNueva, HttpServletRequest request) {
        JSONObject salida = new JSONObject();
        JSONObject credenciales = new JSONObject();
        credenciales.put("rutusuario", rutfull.substring(0, rutfull.length() - 1));
        credenciales.put("password", passAnterior);
        JSONObject login = loginUsuario(credenciales, request);
        if (login.getInt("filas") > 0) {
            int rut = Integer.parseInt(rutfull.substring(0, rutfull.length() - 1));
            String query = "CALL SP_UPD_PASSWORD(" + rut + ", '" + modelo.Util.hashMD5(passNueva) + "')";
            Conexion c = new Conexion();
            c.abrir();
            c.ejecutar(query);
            c.cerrar();
            salida.put("estado", "ok");
        } else {
            salida.put("estado", "error");
            salida.put("mensaje", "La contraseña ingresada no corresponde con la registrada. Intente nuevamente");
        }
        return salida;
    }
}
