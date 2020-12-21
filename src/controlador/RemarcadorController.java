package controlador;

import clases.json.JSONArray;
import clases.json.JSONException;
import clases.json.JSONObject;
import etl.FilaNormal;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.LinkedList;
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
            case "get-remarcador-un-cliente-boleta":
                out.print(getRemarcadorUnClienteBoleta(entrada));
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
            case "get-select-remarcadores-idinstalacion-idcliente":
                out.print(getSelectRemarcadoresClienteIdInstalacion(entrada));
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
            case "get-last-lectura-mes":
                out.print(getLastLecturaMes(entrada));
                break;
            case "ins-lectura-manual":
                out.print(insLecturaManual(entrada));
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
                filas += "<td><span>" + rs.getString("MARCA") + "</span></td>";
                filas += "<td><span>" + rs.getString("MODELO") + "</span></td>";
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
        System.out.println(query);
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

    @Deprecated
    private JSONObject getRemarcadoresNumEmpalmeBoletaOld(JSONObject entrada) {
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

        String tabla = "<table style='font-size: 10px;' id='tabla-remarcadores-empalme' class='table table-bordered table-condensed table-sm'>";
        tabla += "<caption style='caption-side:top;'><h5>Remarcadores en el Empalme Nº: " + entrada.getString("numempalme") + "</h5></caption>";
        tabla += "<thead style='text-align: center;' ><tr class='table-info'>";
        tabla += "<th># Remarcador</th>";
        tabla += "<th>Nº Serie</th>";
        tabla += "<th>Bodega</th>";
        tabla += "<th>Cliente</th>";
        tabla += "<th>Módulos</th>";
        tabla += "<th>Instalación</th>";
        tabla += "<th>Lectura<br />Anterior</th>";
        tabla += "<th>Lectura<br />Actual</th>";
        tabla += "<th>Consumo (kWh)</th>";
        tabla += "<th>Emitir</th>";
        tabla += "<th>Última<br />Boleta</th>";
        tabla += "</tr></thead><tbody>";
        JSONObject remarcador;
        int idcomuna = 0;
        int boletasnoemitidas = 0;
        JSONArray remarcadores = new JSONArray();
        try {
            while (rs.next()) {
                filas += "<tr>";
                filas += "<td style='text-align: center;' ><input type='hidden' value='" + rs.getInt("IDREMARCADOR") + "' /><span>" + rs.getString("NUMREMARCADOR") + "</span></td>";
                filas += "<td><input type='hidden' value='" + rs.getString("NUMSERIE") + "' /><span>" + rs.getString("NUMSERIE") + "</span></td>";
                filas += "<td><input type='hidden' value='" + rs.getInt("IDPARQUE") + "' /><span>" + rs.getString("NOMPARQUE") + "</span></td>";
                filas += "<td><span>" + rs.getString("NOMCLIENTE") + "</span></td>";
                filas += "<td style='text-align: center;' ><span>" + rs.getString("MODULOS") + "</span></td>";
                filas += "<td><input type='hidden' value='" + rs.getInt("IDINSTALACION") + "' /><span>" + rs.getString("NOMINSTALACION") + "</span></td>";
                filas += "<td style='text-align: right;'><span>" + Util.formatMiles(rs.getString("LECTURAANTERIOR")) + "</span></td>";
                filas += "<td style='text-align: right;'><span>" + Util.formatMiles(rs.getString("LECTURAACTUAL")) + "</span></td>";
                filas += "<td style='text-align: right;'><span>" + Util.formatMiles(rs.getInt("CONSUMO")) + "</span></td>";

                if (rs.getInt("HAYBOLETA") == 0) {
                    boletasnoemitidas++; //Para ver si se anota al menos un candidato a generación masiva
                    filas += "<td><button type='button' onclick='calcular(" + rs.getInt("IDREMARCADOR") + ", " + rs.getInt("NUMREMARCADOR") + ", \"" + rs.getString("NUMSERIE") + "\", " + rs.getInt("CONSUMO") + ", \"" + entrada.getString("mes") + "\", " + rs.getInt("LECTURAANTERIOR") + ", " + rs.getInt("LECTURAACTUAL") + ", \"" + rs.getString("MAX_DEMANDA_LEIDA_STRING") + "\", \"" + rs.getString("MAX_DEMANDA_HORA_PUNTA_STRING") + "\", \"" + rs.getDate("FECHA_LECTURA_INICIAL") + "\", \"" + rs.getDate("FECHA_LECTURA_FINAL") + "\");' class='btn btn-sm btn-outline-success' style='padding: 0px 2px 0px 2px;'>Calcular Boleta</button></td>";
                } else {
                    filas += "<td>"
                            + "<div id='botones_" + rs.getInt("IDREMARCADOR") + "' style='display:none;' class='btn-group' role='group' aria-label='Sobreescritura'>"
                            + "<button type='button' onclick='calcular(" + rs.getInt("IDREMARCADOR") + ", " + rs.getInt("NUMREMARCADOR") + ", \"" + rs.getString("NUMSERIE") + "\", " + rs.getInt("CONSUMO") + ", \"" + entrada.getString("mes") + "\", " + rs.getInt("LECTURAANTERIOR") + ", " + rs.getInt("LECTURAACTUAL") + ", \"" + rs.getString("MAX_DEMANDA_LEIDA_STRING") + "\", \"" + rs.getString("MAX_DEMANDA_HORA_PUNTA_STRING") + "\", \"" + rs.getDate("FECHA_LECTURA_INICIAL") + "\", \"" + rs.getDate("FECHA_LECTURA_FINAL") + "\");' class='btn btn-sm btn-outline-warning' style='padding: 0px 2px 0px 2px;'>Sobreescribir</button>"
                            + "<button type='button' onclick='deshabilitarSobreescritura(" + rs.getInt("IDREMARCADOR") + ");' class='btn btn-sm btn-warning' style='padding: 0px 5px 0px 5px; vertical-align:middle;'>x</button>"
                            + "</div>"
                            + "<button id='btn_" + rs.getInt("IDREMARCADOR") + "' type='button' onclick='habilitarSobreescritura(" + rs.getInt("IDREMARCADOR") + ");' class='btn btn-sm btn-outline-warning' style='padding: 0px 2px 0px 2px;'>Habilitar</button>"
                            + "</td>";
                }

                if (rs.getInt("IDBOLETA") == 0) {
                    filas += "<td style='text-align: right;'>-</td>";
                } else {
                    filas += "<td style='text-align: right;'><a href='#' onclick=getLastBoleta(" + rs.getInt("IDBOLETA") + "); >" + rs.getString("NUMBOLETA") + "</a></td>";
                }

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
                remarcador.put("lecturaanterior", rs.getInt("LECTURAANTERIOR"));
                remarcador.put("lecturaactual", rs.getInt("LECTURAACTUAL"));
                remarcador.put("fechainicial", rs.getDate("FECHA_LECTURA_INICIAL"));
                remarcador.put("fechafinal", rs.getDate("FECHA_LECTURA_FINAL"));
                remarcadores.put(remarcador);
            }
            filas += "<tr class='table-info'>";
            filas += "<td colspan='8' style='text-align: right; padding-right:5px; font-weight: bold;'>Consumo Total Remarcadores(KW): </td>";
            filas += "<td style='font-weight: bold; text-align:right;' >" + Util.formatMiles(kwtotal) + "</td>";
            if (boletasnoemitidas > 2) {
                filas += "<td colspan='2' style='border: 1px solid white; background-color: white; text-align: center;'>"
                        + "<button type='button' onclick='generarTodas();' style='padding: 0px 2px 0px 2px; height: 1.5em;' class='btn btn-sm btn-outline-primary'>Generar Todas</button></td>" + "</td>";
                filas += "</tr>";
            }

            filas += "<tr>";
            filas += "<td colspan='8' style='vertical-align: middle; text-align: right; padding-right:5px; font-weight: bold;'>Consumo Facturado del Empalme: " + entrada.getString("numempalme") + "</td>";
            filas += "<td><input type='text' onkeyup='calcularDiferencia();' class='form-control form-control-sm small' style='font-size: 0.9em; padding-top: 0px; padding-bottom: 0px; width: 12em; text-align: right;' id='consumo-facturado-empalme'/></td>";
            filas += "</tr>";

            filas += "<tr>";
            filas += "<td colspan='8' style='text-align: right; padding-right:5px; font-weight: bold;'>KW Diferencia: </td>";
            filas += "<td style='text-align: right;' ><span id='kw-diferencia'></span></td>";
            filas += "</tr>";

            filas += "<tr>";
            filas += "<td colspan='8' style='text-align: right; padding-right:5px; font-weight: bold;'>% Diferencia: </td>";
            filas += "<td style='text-align: right;'><span id='porc-diferencia' ></span></td>";
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

    private JSONObject getRemarcadoresNumEmpalmeBoleta(JSONObject entrada) {
        JSONObject salida = new JSONObject();
        int anio = Integer.parseInt(entrada.getString("mes").split("-")[0]);
        int mes = Integer.parseInt(entrada.getString("mes").split("-")[1]);
        int kwtotal = 0;
        boolean haymanual = false;
        boolean haymanualini = false;
        String query = "CALL SP_GET_REMARCADORES_NUMEMPALME_BOLETA("
                + "'" + entrada.getString("numempalme") + "',"
                + "'" + entrada.getString("mes") + "'"
                + ")";
        System.out.println(query);
        LinkedList<Remarcador> remarcadores = new LinkedList();
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        try {
            while (rs.next()) {
                remarcadores.add(new Remarcador(rs.getInt("IDREMARCADOR"), rs.getInt("NUMREMARCADOR"), rs.getString("NUMSERIE"), rs.getString("MODULOS"), rs.getString("NUMEMPALME"), rs.getString("NOMPARQUE"), rs.getString("NOMINSTALACION"), rs.getString("NOMCOMUNA"), rs.getInt("RUTCLIENTE"), rs.getString("DVCLIENTE"), rs.getString("NOMCLIENTE"), rs.getString("RAZONCLIENTE"), rs.getInt("IDBOLETA"), rs.getString("NUMBOLETA"), rs.getInt("HAYBOLETA"), rs.getInt("IDCOMUNA"), rs.getInt("IDPARQUE"), rs.getInt("IDINSTALACION")));
            }
        } catch (SQLException ex) {
            System.out.println("Problemas al intentar obtener el listado de remarcadores.");
            System.out.println(ex);
        }

        String tablasalida = "<table style='font-size: 10px;' id='tabla-remarcadores-empalme' class='table table-bordered table-condensed table-sm'>";
        tablasalida += "<caption style='caption-side:top;'><h5>Remarcadores en el Empalme Nº: " + entrada.getString("numempalme") + "</h5></caption>";
        tablasalida += "<thead style='text-align: center;' ><tr class='table-info'>";
        tablasalida += "<th># Remarcador</th>";
        tablasalida += "<th>Nº Serie</th>";
        tablasalida += "<th>Bodega</th>";
        tablasalida += "<th>Cliente</th>";
        tablasalida += "<th>Módulos</th>";
        tablasalida += "<th>Instalación</th>";
        tablasalida += "<th>Lectura<br />Anterior</th>";
        tablasalida += "<th>Lectura<br />Final</th>";
        tablasalida += "<th>Consumo (kWh)</th>";
        tablasalida += "<th>Emitir</th>";
        tablasalida += "<th>Última<br />Boleta</th>";
        tablasalida += "</tr></thead><tbody>";
        JSONArray remarcadoresJson = new JSONArray();
        JSONObject remarcadorJson;
        int idcomuna = 0;
        int boletasnoemitidas = 0;

        LinkedList<FilaNormal[]> tablas = new LinkedList();
        for (Remarcador remarcador : remarcadores) {
            //System.out.println(r.printCsv());
            FilaNormal[] filas = etl.ETL.getDatasetRemarcador(remarcador.numremarcador, mes, anio);

            double demmax = 0;
            double demmaxhp = 0;
            double consumo = 0;
            String fechainilectura = filas[0].fecha;
            String fechafinlectura = filas[filas.length - 1].fecha;
            //int lecturaanterior = (int) filas[0].lecturaproyectada;
            //int lecturafinal = (int) filas[filas.length - 1].lecturaproyectada;

            int lecturaanterior = (int) filas[0].lecturareal;
            int lecturafinal = (int) filas[filas.length - 1].lecturareal;
            if (filas[filas.length - 1].esmanual) {
                lecturafinal = filas[filas.length - 1].lecturamanual;
                haymanual = true;
            }
            if (filas[0].esmanual) {
                haymanualini = true;
            }

            for (FilaNormal fila : filas) {
                if (fila.lecturaproyectada != fila.delta) {
                    consumo += fila.delta;
                }
                if (fila.potencia > demmax) {
                    demmax = fila.potencia;
                }
                if (Integer.parseInt(fila.hora.substring(0, 2)) >= 18 && Integer.parseInt(fila.hora.substring(0, 2)) <= 23) {
                    if (fila.potencia > demmaxhp) {
                        demmaxhp = fila.potencia;
                    }
                }
            }
            //Si existe lectura manual, el consumo se calcula en base a lecturamanual - lecturaanterior.
            if (haymanual || haymanualini) {
                consumo = lecturafinal - lecturaanterior;
            }

            String demmaxString = new DecimalFormat("#.##").format(demmax).replace(",", ".");
            String demmaxhpString = new DecimalFormat("#.##").format(demmaxhp).replace(",", ".");

            tablasalida += "<tr>";
            tablasalida += "<td style='text-align: center;' ><input type='hidden' value='" + remarcador.idremarcador + "' /><span>" + remarcador.numremarcador + "</span></td>";
            tablasalida += "<td><input type='hidden' value='" + remarcador.numserie + "' /><span>" + remarcador.numserie + "</span></td>";
            tablasalida += "<td><span>" + remarcador.nomparque + "</span></td>";
            tablasalida += "<td><span>" + remarcador.nomcliente + "</span></td>";
            tablasalida += "<td style='text-align: center;' ><span>" + remarcador.modulos + "</span></td>";
            tablasalida += "<td><span>" + remarcador.nominstalacion + "</span></td>";
            tablasalida += "<td style='text-align: right;'><span>" + Util.formatMiles(lecturaanterior) + (filas[0].esmanual ? " *" : "") + "</span></td>";
            tablasalida += "<td style='text-align: right;'><span>" + Util.formatMiles(lecturafinal) + (filas[filas.length - 1].esmanual ? " *" : "") + "</span></td>";
            tablasalida += "<td style='text-align: right;'><span>" + Util.formatMiles((int) consumo) + "</span></td>";

            if (remarcador.hayboleta == 0) {
                boletasnoemitidas++; //Para ver si se anota al menos un candidato a generación masiva
                tablasalida += "<td><button type='button' onclick='calcular(" + remarcador.idremarcador + ", " + remarcador.numremarcador + ", \"" + remarcador.numserie + "\", " + (int) consumo + ", \"" + entrada.getString("mes") + "\", " + lecturaanterior + ", " + lecturafinal + ", \"" + demmaxString + "\", \"" + demmaxhpString + "\", \"" + fechainilectura + "\", \"" + fechafinlectura + "\");' class='btn btn-sm btn-outline-success' style='padding: 0px 2px 0px 2px;'>Calcular Boleta</button></td>";
            } else {
                tablasalida += "<td>"
                        + "<div id='botones_" + remarcador.idremarcador + "' style='display:none;' class='btn-group' role='group' aria-label='Sobreescritura'>"
                        + "<button type='button' onclick='calcular(" + remarcador.idremarcador + ", " + remarcador.numremarcador + ", \"" + remarcador.numserie + "\", " + (int) consumo + ", \"" + entrada.getString("mes") + "\", " + lecturaanterior + ", " + lecturafinal + ", \"" + demmaxString + "\", \"" + demmaxhpString + "\", \"" + fechainilectura + "\", \"" + fechafinlectura + "\");' class='btn btn-sm btn-outline-warning' style='padding: 0px 2px 0px 2px;'>Sobreescribir</button>"
                        + "<button type='button' onclick='deshabilitarSobreescritura(" + remarcador.idremarcador + ");' class='btn btn-sm btn-warning' style='padding: 0px 5px 0px 5px; vertical-align:middle;'>x</button>"
                        + "</div>"
                        + "<button id='btn_" + remarcador.idremarcador + "' type='button' onclick='habilitarSobreescritura(" + remarcador.idremarcador + ");' class='btn btn-sm btn-outline-warning' style='padding: 0px 2px 0px 2px;'>Habilitar</button>"
                        + "</td>";
            }

            if (remarcador.hayboleta == 0) {
                tablasalida += "<td style='text-align: right;'>-</td>";
            } else {
                tablasalida += "<td style='text-align: right;'><a href='#' onclick=getLastBoleta(" + remarcador.idboleta + "); >" + remarcador.numboleta + "</a></td>";
            }

            tablasalida += "</tr>";
            kwtotal += (int) consumo;
            idcomuna = remarcador.idcomuna;
            remarcadorJson = new JSONObject();
            remarcadorJson.put("idremarcador", remarcador.idremarcador);
            remarcadorJson.put("numremarcador", remarcador.numremarcador);
            remarcadorJson.put("numserie", remarcador.numserie);
            remarcadorJson.put("idparque", remarcador.idparque);
            remarcadorJson.put("modulos", remarcador.modulos);
            remarcadorJson.put("idinstalacion", remarcador.idinstalacion);
            remarcadorJson.put("consumo", (int) consumo);
            remarcadorJson.put("lecturaanterior", lecturaanterior);
            remarcadorJson.put("lecturaactual", lecturafinal);
            remarcadorJson.put("fechainicial", fechainilectura);
            remarcadorJson.put("fechafinal", fechafinlectura);
            remarcadoresJson.put(remarcadorJson);
        }
        tablasalida += "<tr class='table-info'>";
        tablasalida += "<td colspan='8' style='text-align: right; padding-right:5px; font-weight: bold;'>Consumo Total Remarcadores(KW): </td>";
        tablasalida += "<td style='font-weight: bold; text-align:right;' >" + Util.formatMiles(kwtotal) + "</td>";
        if (boletasnoemitidas > 2) {
            tablasalida += "<td colspan='2' style='border: 1px solid white; background-color: white; text-align: center;'>"
                    + "<button type='button' onclick='generarTodas();' style='padding: 0px 2px 0px 2px; height: 1.5em;' class='btn btn-sm btn-outline-primary'>Generar Todas</button></td>" + "</td>";
            tablasalida += "</tr>";
        }

        tablasalida += "<tr>";
        tablasalida += "<td colspan='8' style='vertical-align: middle; text-align: right; padding-right:5px; font-weight: bold;'>Consumo Facturado del Empalme: " + entrada.getString("numempalme") + "</td>";
        tablasalida += "<td><input type='text' onkeyup='calcularDiferencia();' class='form-control form-control-sm small' style='font-size: 0.9em; padding-top: 0px; padding-bottom: 0px; width: 12em; text-align: right;' id='consumo-facturado-empalme'/></td>";
        tablasalida += "</tr>";

        tablasalida += "<tr>";
        tablasalida += "<td colspan='8' style='text-align: right; padding-right:5px; font-weight: bold;'>KW Diferencia: </td>";
        tablasalida += "<td style='text-align: right;' ><span id='kw-diferencia'></span></td>";
        tablasalida += "</tr>";

        tablasalida += "<tr>";
        tablasalida += "<td colspan='8' style='text-align: right; padding-right:5px; font-weight: bold;'>% Diferencia: </td>";
        tablasalida += "<td style='text-align: right;'><span id='porc-diferencia' ></span></td>";
        tablasalida += "</tr>";

        //tablasalida += filas;
        tablasalida += "</tbody></table>";
        if (haymanual || haymanualini) {
            tablasalida += "<span style='font-size: 12px; font-weight: bold;'>* La lectura fue ingresada de forma manual</span>";
        }
        salida.put("tabla", tablasalida);
        salida.put("remarcadores", remarcadores);
        salida.put("kwtotal", kwtotal);
        salida.put("idcomuna", idcomuna);
        salida.put("estado", "ok");

        c.cerrar();
        return salida;
    }

    private JSONObject getRemarcadorUnClienteBoleta(JSONObject entrada) {
        JSONObject datosrem = new JSONObject();
        String mesanio = entrada.getString("hasta").substring(0, 7);
        int anio = Integer.parseInt(mesanio.split("-")[0]);
        int mes = Integer.parseInt(mesanio.split("-")[1]);
        datosrem.put("idremarcador", entrada.getInt("idremarcador"));
        datosrem.put("mesanio", mesanio);
        JSONObject remcli = this.getRemarcadorClienteIdRemarcador(datosrem).getJSONObject("remarcador");
        System.out.println("remcli: " + remcli);
        FilaNormal[] registrosRemarcador = etl.ETL.getDatasetRemarcador(entrada.getInt("numremarcador"), entrada.getString("desde"), entrada.getString("hasta"));
        //Calcular consumo desde tabla de remarcador
        int consumo = 0;
        int lecturaini = 0;
        int lecturafin = 0;
        String fechalecturaini = "";
        String fechalecturafin = "";
        boolean haymanual = false;
        for(FilaNormal registro : registrosRemarcador){
            if(registro.fecha.equals(entrada.getString("desde"))){
                fechalecturaini = registro.fecha;
                lecturaini = (int)registro.lecturaproyectada;
            }
            if(registro.fecha.equals(entrada.getString("hasta"))){
                fechalecturafin = registro.fecha;
                lecturafin = (int)registro.lecturaproyectada;
            }
        }
        if(entrada.getString("hasta").equals(registrosRemarcador[registrosRemarcador.length - 1].fecha) && registrosRemarcador[registrosRemarcador.length - 1].esmanual){
            haymanual = true;
            lecturafin = registrosRemarcador[registrosRemarcador.length - 1].lecturamanual;
        }
        consumo = lecturafin - lecturaini;
        
        
        System.out.println(entrada);
        JSONObject salida = new JSONObject();
        int kwtotal = 0;
        String filas = "";

        String tabla = "<table style='font-size: 10px;' id='tabla-remarcadores-empalme' class='table table-bordered table-condensed table-sm'>";
        tabla += "<caption style='caption-side:top;'><h5>Remarcadores en el Empalme Nº: " + entrada.getString("numempalme") + "</h5></caption>";
        tabla += "<thead style='text-align: center;' ><tr class='table-info'>";
        tabla += "<th># Remarcador</th>";
        tabla += "<th>Nº Serie</th>";
        tabla += "<th>Bodega</th>";
        tabla += "<th>Cliente</th>";
        tabla += "<th>Módulos</th>";
        tabla += "<th>Instalación</th>";
        tabla += "<th>Lectura<br />Anterior</th>";
        tabla += "<th>Lectura<br />Actual</th>";
        tabla += "<th>Consumo (kWh)</th>";
        tabla += "<th>Emitir</th>";
        tabla += "<th>Última<br />Boleta</th>";
        tabla += "</tr></thead><tbody>";
        JSONObject remarcador;
        int idcomuna = 0;
        int boletasnoemitidas = 0;
        JSONArray remarcadores = new JSONArray();

        Conexion con = new Conexion();
        String querybol = "CALL SP_GET_LAST_BOLETA_IDREM_MESANIO("
                + remcli.getInt("idremarcador") + ", "
                + mes + ", "
                + anio
                + ")";
        System.out.println("Obtener boleta si es que hay");
        System.out.println(querybol);
        con.abrir();
        ResultSet rs = con.ejecutarQuery(querybol);
        int idboleta = 0;
        String numboleta = "";
        try{
            while(rs.next()){
                idboleta = rs.getInt("IDBOLETA");
                numboleta = rs.getString("NUMBOLETA");
            }
        }catch (SQLException ex) {
            System.out.println("No se puede obtener la última boleta del mes seleccionado para el remarcador.");
            System.out.println(ex);
            ex.printStackTrace();
        }
        
        filas += "<tr>";
        filas += "<td style='text-align: center;' ><input type='hidden' value='" + remcli.getInt("idremarcador") + "' /><span>" + remcli.getInt("numremarcador") + "</span></td>";
        filas += "<td><input type='hidden' value='" + remcli.getString("numserie") + "' /><span>" + remcli.getString("numserie") + "</span></td>";
        filas += "<td><input type='hidden' value='" + remcli.getInt("idparque") + "' /><span>" + remcli.getString("nomparque") + "</span></td>";
        filas += "<td><span>" + remcli.getString("nomcliente") + "</span></td>";
        filas += "<td style='text-align: center;' ><span>" + remcli.getString("modulos") + "</span></td>";
        filas += "<td><input type='hidden' value='" + remcli.getInt("idinstalacion") + "' /><span>" + remcli.getString("nominstalacion") + "</span></td>";
        filas += "<td style='text-align: right;'><span>" + Util.formatMiles(lecturaini) + "</span></td>";
        filas += "<td style='text-align: right;'><span>" + Util.formatMiles(lecturafin) + "" + (haymanual ? " *" : "") + "</span></td>";
        filas += "<td style='text-align: right;'><span>" + Util.formatMiles(consumo) + "</span></td>";

        if (idboleta == 0) {
            boletasnoemitidas++; //Para ver si se anota al menos un candidato a generación masiva
            filas += "<td><button type='button' onclick='calcular(" + remcli.getInt("idremarcador") + ", " + remcli.getInt("numremarcador") + ", \"" + remcli.getString("numserie") + "\", " + consumo + ", \"" + entrada.getString("desde") + "\", \"" + entrada.getString("hasta") + "\", " + lecturaini + ", " + lecturafin + ", \"" + remcli.getString("dmps_string") + "\", \"" + remcli.getString("dmplhp_string") + "\", \"" + fechalecturaini + "\", \"" + fechalecturafin + "\");' class='btn btn-sm btn-outline-success' style='padding: 0px 2px 0px 2px;'>Calcular Boleta</button></td>";
        } else {
            filas += "<td>"
                    + "<div id='botones_" + remcli.getInt("idremarcador") + "' style='display:none;' class='btn-group' role='group' aria-label='Sobreescritura'>"
                    + "<button type='button' onclick='calcular(" + remcli.getInt("idremarcador") + ", " + remcli.getInt("numremarcador") + ", \"" + remcli.getString("numserie") + "\", " + consumo + ", \"" + entrada.getString("desde") + "\", \"" + entrada.getString("hasta") + "\", " + lecturaini + ", " + lecturafin+ ", \"" + remcli.getString("dmps_string") + "\", \"" + remcli.getString("dmplhp_string") + "\", \"" + fechalecturaini + "\", \"" + fechalecturafin + "\");' class='btn btn-sm btn-outline-warning' style='padding: 0px 2px 0px 2px;'>Sobreescribir</button>"
                    + "<button type='button' onclick='deshabilitarSobreescritura(" + remcli.getInt("idremarcador") + ");' class='btn btn-sm btn-warning' style='padding: 0px 5px 0px 5px; vertical-align:middle;'>x</button>"
                    + "</div>"
                    + "<button id='btn_" + remcli.getInt("idremarcador") + "' type='button' onclick='habilitarSobreescritura(" + remcli.getInt("idremarcador") + ");' class='btn btn-sm btn-outline-warning' style='padding: 0px 2px 0px 2px;'>Habilitar</button>"
                    + "</td>";
        }

        if (idboleta == 0) {
            filas += "<td style='text-align: right;'>-</td>";
        } else {
            filas += "<td style='text-align: right;'><a href='#' onclick=getLastBoleta(" + idboleta + "); >" + numboleta + "</a></td>";
        }

        filas += "</tr>";
        kwtotal += consumo;
        idcomuna = remcli.getInt("idcomuna");
        remarcador = new JSONObject();
        remarcador.put("idremarcador", remcli.getInt("idremarcador"));
        remarcador.put("numremarcador", remcli.getInt("numremarcador"));
        remarcador.put("numserie", remcli.getString("numserie"));
        remarcador.put("idparque", remcli.getInt("idparque"));
        remarcador.put("modulos", remcli.getString("modulos"));
        remarcador.put("idinstalacion", remcli.getInt("idinstalacion"));
        remarcador.put("consumo", consumo);
        remarcadores.put(remarcador);

        filas += "<tr class='table-info'>";
        filas += "<td colspan='8' style='text-align: right; padding-right:5px; font-weight: bold;'>Consumo Total Remarcadores(KW): </td>";
        filas += "<td style='font-weight: bold; text-align:right;' >" + Util.formatMiles(kwtotal) + "</td>";

        filas += "<tr>";
        filas += "<td colspan='8' style='vertical-align: middle; text-align: right; padding-right:5px; font-weight: bold;'>Consumo Facturado del Empalme: " + entrada.getString("numempalme") + "</td>";
        filas += "<td><input type='text' onkeyup='calcularDiferencia();' class='form-control form-control-sm small' style='font-size: 0.9em; padding-top: 0px; padding-bottom: 0px; width: 12em; text-align: right;' id='consumo-facturado-empalme'/></td>";
        filas += "</tr>";

        filas += "<tr>";
        filas += "<td colspan='8' style='text-align: right; padding-right:5px; font-weight: bold;'>KW Diferencia: </td>";
        filas += "<td style='text-align: right;' ><span id='kw-diferencia'></span></td>";
        filas += "</tr>";

        filas += "<tr>";
        filas += "<td colspan='8' style='text-align: right; padding-right:5px; font-weight: bold;'>% Diferencia: </td>";
        filas += "<td style='text-align: right;'><span id='porc-diferencia' ></span></td>";
        filas += "</tr>";

        tabla += filas;
        tabla += "</tbody></table>";
        if(haymanual){
            tabla += "* El registro fue ingresado de forma manual.";
        }
        salida.put("tabla", tabla);
        salida.put("remarcadores", remarcadores);
        salida.put("kwtotal", kwtotal);
        salida.put("idcomuna", idcomuna);
        salida.put("estado", "ok");
        System.out.println("Boletas no emitidas: " + boletasnoemitidas);

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
        System.out.println(query);
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        String filas = "";
        String tabla = "<table style='font-size: 10px;' id='tabla-remarcadores-asignados' class='table table-bordered table-sm small'>";
        tabla += "<thead style='text-align:center;'><tr>";
        tabla += "<th># Remarcador</th>";
        tabla += "<th>Nº Serie</th>";
        tabla += "<th># Empalme</th>";
        tabla += "<th>Bodega</th>";
        tabla += "<th>Módulos</th>";
        tabla += "<th>Instalación</th>";
        tabla += "<th>Cliente</th>";
        tabla += "<th>Contacto</th>";
        tabla += "<th>Fono</th>";
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
                filas += "<td>" + rs.getString("PERSONA") + "</td>";
                filas += "<td>" + rs.getInt("FONO") + "</td>";
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
                + "'" + entrada.getString("modulos") + "',"
                + "'" + entrada.getString("marca") + "',"
                + "'" + entrada.getString("modelo") + "'"
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
                remarcador.put("marca", rs.getString("MARCA"));
                remarcador.put("modelo", rs.getString("MODELO"));
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
        String mesanio = entrada.getString("mesanio");
        String query = "CALL SP_GET_REMARCADOR_CLIENTE_IDREMARCADOR(" + idremarcador + ", '" + mesanio + "')";
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
                remarcador.put("dmps", rs.getDouble("DEM_MAX_POTENCIA_SUMINISTRADA"));
                remarcador.put("dmplhp", rs.getDouble("DEM_MAX_POTENCIA_LEIDA_H_PUNTA"));
                remarcador.put("dmps_string", rs.getString("DEM_MAX_POTENCIA_SUMINISTRADA_STRING"));
                remarcador.put("dmplhp_string", rs.getString("DEM_MAX_POTENCIA_LEIDA_H_PUNTA_STRING"));
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
                + "'" + remarcador.getString("modulos") + "', "
                + "'" + remarcador.getString("marca") + "', "
                + "'" + remarcador.getString("modelo") + "'"
                + ")";
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

    private JSONObject getSelectRemarcadoresClienteIdInstalacion(JSONObject entrada) {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_GET_REMARCADORES_IDINSTALACION(" + entrada.getInt("idinstalacion") + ", " + entrada.getInt("idcliente") + ")";
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

    private JSONObject getLastLecturaMes(JSONObject entrada) {
        JSONObject salida = new JSONObject();
        int idremarcador = entrada.getInt("idremarcador");
        int numremarcador = entrada.getInt("numremarcador");
        int anio = entrada.getInt("anio");
        int mes = entrada.getInt("mes");

        FilaNormal[] registros = etl.ETL.getDatasetRemarcador(numremarcador, mes, anio);
        FilaNormal ultregistro = registros[registros.length - 1];

        String tabla = "<label for='tab-last-lectura-mes'>Última lectura del mes</label><table style='font-size: 12px;' class='table table-bordered table-sm small'id='tab-last-lectura-mes'>\n"
                + "<thead>\n"
                + "<tr>\n"
                + "<th>ID</th>\n"
                + "<th>Energía (KWh)</th>\n"
                + "<th>Potencia Activa (KW)</th>\n"
                + "<th>Fecha</th>\n"
                + "<th>Hora</th>\n"
                + "</tr>\n"
                + "</thead>\n"
                + "<tbody>\n";
        String filas = "";
        DecimalFormat df = new DecimalFormat("#.##");
        filas += "<tr>";
        filas += "<td>" + ultregistro.idremarcador + "</td>";
        filas += "<td>" + (int) ultregistro.lecturareal + "</td>";
        filas += "<td>" + df.format(ultregistro.potencia).replace(",", ".") + "</td>";
        filas += "<td>" + Util.invertirFecha(ultregistro.fecha) + "</td>";
        filas += "<td>" + ultregistro.hora + "</td>";
        filas += "</tr>";

        salida.put("numremarcador", ultregistro.idremarcador);
        salida.put("energia", (int) ultregistro.lecturareal);
        salida.put("potencia", df.format(ultregistro.potencia).replace(",", "."));
        salida.put("fechaformat", Util.invertirFecha(ultregistro.fecha));
        salida.put("fecha", ultregistro.fecha);
        salida.put("hora", ultregistro.hora);

        tabla += (filas + "</tbody></table>");
        salida.put("tabla", tabla);
        String botonInsert = "<table style='border: none; border-collapse: collapse'>\n"
                + "<tr>\n"
                + "<td>\n"
                + "<div class='input-group mb-4'>\n"
                + "<input id='num-lectura-manual' type='number' class='form-control form-control-sm' aria-label='Lectura manual'>\n"
                + "<div class='input-group-append'>\n"
                + "<button onclick='insertar();' class='btn btn-sm btn-success' type='button'>Insertar</button>\n"
                + "</div>\n"
                + "</div>\n"
                + "</td>\n"
                + "<td>\n"
                + "<div class='loader' style='display: none; margin-top: -1.7em;'><!-- Contenedor del Spinner -->\n"
                + "<div class='ldio-sa9px9nknjc'> <!-- El Spinner -->\n"
                + "<div>\n"
                + "</div>\n"
                + "<div>\n"
                + "<div></div>\n"
                + "</div>\n"
                + "</div>\n"
                + "</div>\n"
                + "</td>\n"
                + "</tr>\n"
                + "</table>";
        String etiqueta = "<label for='num-lectura-manual'>Valor Lectura (KW)</label>";
        salida.put("boton", botonInsert);
        salida.put("etiqueta", etiqueta);
        salida.put("estado", "ok");

        return salida;
    }

    @Deprecated
    private JSONObject insLecturaManualOld(JSONObject entrada) {
        System.out.println(entrada);
        JSONObject salida = new JSONObject();
        int idremarcador = entrada.getInt("idremarcador");
        int anio = entrada.getInt("anio");
        int mes = entrada.getInt("mes");
        int lectura = entrada.getInt("lectura");
        String query = "CALL SP_INS_LECTURA_MANUAL("
                + idremarcador + ", "
                + lectura + ", "
                + anio + ", "
                + mes + ""
                + ")";
        System.out.println(query);
        Conexion c = new Conexion();
        c.abrir();
        c.ejecutar(query);
        salida.put("estado", "ok");
        c.cerrar();
        return salida;
    }

    private JSONObject insLecturaManual(JSONObject entrada) {
        System.out.println(entrada);
        JSONObject salida = new JSONObject();
        int numremarcador = entrada.getInt("numremarcador");
        String fecha = entrada.getString("fecha");
        String hora = entrada.getString("hora");
        int lectura = entrada.getInt("lectura");
        String query = "CALL SP_INS_LECTURA_MANUAL("
                + numremarcador + ", "
                + lectura + ", "
                + "'" + fecha + "', "
                + "'" + hora + "'"
                + ")";
        System.out.println(query);
        Conexion c = new Conexion();
        c.abrir();
        c.ejecutar(query);
        salida.put("estado", "ok");
        c.cerrar();
        return salida;

    }

}

class Remarcador {

    public int idremarcador;
    public int numremarcador;
    public String numserie;
    public String modulos;
    public String numempalme;
    public String nomparque;
    public String nominstalacion;
    public String nomcomuna;
    public int rutcliente;
    public String dvcliente;
    public String nomcliente;
    public String razoncliente;

    public String fechalecturaactual;
    public String fechalecturaanterior;

    public int lecturaactual;
    public int lecturaanterior;

    public double maxdemandaleida;
    public double maxdemandaleidahp;
    public int idboleta;
    public String numboleta;
    public int hayboleta;

    public int idcomuna;
    public int idparque;
    public int idinstalacion;

    public Remarcador(int idremarcador, int numremarcador, String numserie, String modulos, String numempalme, String nomparque, String nominstalacion, String nomcomuna, int rutcliente, String dvcliente, String nomcliente, String razoncliente, int idboleta, String numboleta, int hayboleta, int idcomuna, int idparque, int idinstalacion) {
        this.idremarcador = idremarcador;
        this.numremarcador = numremarcador;
        this.numserie = numserie;
        this.modulos = modulos;
        this.numempalme = numempalme;
        this.nomparque = nomparque;
        this.nominstalacion = nominstalacion;
        this.nomcomuna = nomcomuna;
        this.rutcliente = rutcliente;
        this.dvcliente = dvcliente;
        this.nomcliente = nomcliente;
        this.razoncliente = razoncliente;
        this.idboleta = idboleta;
        this.numboleta = numboleta;
        this.hayboleta = hayboleta;
        this.idcomuna = idcomuna;
        this.idparque = idparque;
        this.idinstalacion = idinstalacion;
    }

    public String printCsv() {
        return this.idremarcador + ";" + this.numremarcador + ";" + this.numserie + ";" + this.modulos + ";" + this.numempalme + ";" + this.nomparque + ";"
                + this.nominstalacion + ";" + this.nomcomuna + ";" + this.rutcliente + ";" + this.dvcliente + ";" + this.nomcliente + ";" + this.razoncliente + ";"
                + this.idboleta + ";" + this.numboleta + ";" + this.hayboleta;
    }

}
