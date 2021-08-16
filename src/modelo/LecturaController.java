package modelo;

import clases.json.JSONArray;
import clases.json.JSONObject;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import modelo.lectura.Dia;
import modelo.lectura.DiaPotencia;
import modelo.lectura.Registro;
import modelo.lectura.RegistroPotencia;

public class LecturaController {

    public static JSONObject getDatasetMesRemarcadorNumremarcador(int numremarcador, int anio, int mes) {
        LinkedList<Registro> registros = new LinkedList<>();
        LinkedList<Dia> dias = new LinkedList<>();
        DateTimeFormatter formattertimestamp = DateTimeFormatter.ISO_DATE_TIME.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatterdate = DateTimeFormatter.ISO_DATE.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formattertime = DateTimeFormatter.ISO_TIME.ofPattern("HH:mm:ss");

        JSONObject dataset = new JSONObject();
        JSONArray labels = new JSONArray();
        JSONArray data = new JSONArray();

        System.out.println("(1) Buscando dataset remarcador num: " + numremarcador + " Año: " + anio + " Mes: " + mes);

        Conexion c;

        //Obtener orígen del remarcador que viene con su número
        String queryOrigen = "SELECT FN_GET_ORIGEN_NUMREMARCADOR(" + numremarcador + ") ORIGEN";
        System.out.println(queryOrigen);
        c = new Conexion();
        //c.setReplica();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(queryOrigen);
        String origen = "";
        try {
            while (rs.next()) {
                origen = rs.getString("ORIGEN");
            }
        } catch (Exception ex) {
            System.out.println("No se pudo obtener el origen del remarcador NUM: " + numremarcador);
            System.out.println(ex);
            c.cerrar();
        }
        c.cerrar();
        String query = "";

        //Ir a traer todas las lecturas del mes
        if (origen.equals("circutorcvmC10")) {
            query = "CALL SP_GET_LECTURAS_MES_CIRCUTOR(" + numremarcador + ", " + anio + ", " + mes + ")";
        } else if (origen.equals("schneiderPM710")) {
            query = "CALL SP_GET_LECTURAS_MES_PM710(" + numremarcador + ", " + anio + ", " + mes + ")";
        } else if (origen.equals("schneiderPM5300")) {
            query = "CALL SP_GET_LECTURAS_MES_PM5300(" + numremarcador + ", " + anio + ", " + mes + ")";
        }
        c = new Conexion();
        //c.setReplica();
        c.abrir();
        System.out.println(query);
        rs = c.ejecutarQuery(query);
        Registro anterior = new Registro();
        String fechaanterior = "";
        float consumodiario = 0f;
        float consumototal = 0f;
        Dia d = new Dia();
        Registro r = new Registro();
        try {
            while (rs.next()) {

                r = new Registro();

                float lecactual = rs.getFloat("LECTURA");
                if (rs.wasNull()) {//Si viene una lectura nula-------------------------------------------------------------------------------------------

                    float lecactualmanual = rs.getFloat("LECTURAMANUAL");
                    if (rs.wasNull()) { //Si la lectura manual también es nula --------------------------------------------------------------------------
                        if (anterior.dia != null) { //Cuando existe un registro anterior
                            r = new Registro(rs.getString("DIA"), rs.getInt("NUMREMARCADOR"), rs.getString("TIMESTAMP"), anterior.lectura);
                            //r.esmanual = true;
                            r.delta = 0f; 
                        } else { //Cuando no existe un registro anterior, no se puede obtener lectura hacia atrás.
                            //Se deja la lectura y delta en cero.
                            r = new Registro(rs.getString("DIA"), rs.getInt("NUMREMARCADOR"), rs.getString("TIMESTAMP"), 0f);
                            r.delta = 0f;
                        }
                        r.delta = 0f; //Se deja el delta en cero.
                    } else { //Si la lectura manual no es nula.
                        r = new Registro(rs.getString("DIA"), rs.getInt("NUMREMARCADOR"), rs.getString("TIMESTAMP"), rs.getFloat("LECTURAMANUAL"));
                        r.esmanual = true;
                        if (!anterior.existe) { //Cuando existe un registro anterior, es decir, es el primero
                            r.delta = anterior.lectura - r.lectura;
                        } else { //Cuando no existe un registro anterior, no se puede obtener lectura hacia atrás.
                            //Se deja el delta en cero
                            r.delta = 0f;
                        }
                    }
                    r.existe = false;
                } else { //La lectura que viene no es nula----------------------------------------------------------------------------------------------
                    r = new Registro(rs.getString("DIA"), rs.getInt("NUMREMARCADOR"), rs.getString("TIMESTAMP"), rs.getFloat("LECTURA"));
                    r.existe = true;
                    float lecactualmanual = rs.getFloat("LECTURAMANUAL");
                    if (!rs.wasNull()) {//Si la lectura es manual, manda ésta. Se cambia.
                        r.esmanual = true;
                        r.lectura = rs.getFloat("LECTURAMANUAL");
                    }
                    if (anterior.dia != null) {
                        if(r.lectura >= anterior.lectura){
                            r.delta = r.lectura - anterior.lectura;
                        }else{
                            
                        }
                    }else{
                        r.delta = 0f;
                    }
                }

                //Preparar los días----------------------------------------------------------------------------------------------------------------------
                if (!rs.getString("DIA").equals(fechaanterior)) {//Si la fecha del día que viene es distinta de la almacenada anterior
                    if (fechaanterior.equals("")) {//Cuando el que viene es el primer registro
                        consumodiario = consumodiario + r.delta;
                        consumototal = consumototal + r.delta;
                        d = new Dia();
                        d.consumo = consumodiario;
                        if (r.esmanual) {
                            d.esmanual = true;
                        }
                        d.fecha = r.dia;
                        //labels.put(r.dia);
                        d.lecturainicial = r.lectura;
                        d.numremarcador = r.numremarcador;
                    } else {//Cuando es un cambio de día
                        d.lecturafinal = anterior.lectura;
                        if (anterior.esmanual) {
                            d.esmanual = true;
                        }
                        dias.add(d);
                        //data.put(d.consumo);
                        d = new Dia();
                        d.consumo = 0f + r.delta;
                        consumototal = consumototal + r.delta;
                        d.fecha = r.dia;
                        //labels.put(r.dia);
                        d.lecturainicial = r.lectura;
                        if (r.esmanual) {
                            d.esmanual = true;
                        }
                        d.numremarcador = r.numremarcador;

                    }
                } else {//El dia que viene es igual al anterior
                    d.consumo = d.consumo + r.delta;
                    consumototal = consumototal + r.delta;
                    if (r.esmanual) {
                        d.esmanual = true;
                    }
                }

                anterior = r;
                registros.add(r);
                fechaanterior = rs.getString("DIA");
                //System.out.println(r.numremarcador + ";" + r.timestamp + ";" + r.lectura + ";" + r.lecturaManual + ";" + r.delta + ";" + r.esmanual);
            }
        } catch (Exception ex) {
            System.out.println("No se pudo obtener los registros del remarcador.");
            System.out.println(ex);
            c.cerrar();
        }

        consumodiario = consumodiario + r.delta;
        //consumototal = consumototal + r.delta;
        if (r.esmanual) {
            d.esmanual = true;
        }
        d.lecturafinal = r.lectura;
        dias.add(d);
        //data.put(d.consumo);
        c.cerrar();
        
        //dias.remove(0);
        for(Dia dia : dias){
            //System.out.println(dia.numremarcador + ";" + dia.fecha + ";" + dia.lecturainicial + ";" + dia.lecturafinal + ";" + dia.consumo + ";" + dia.esmanual);
            labels.put(dia.fecha);
            data.put(dia.consumo);
        }
        JSONObject resumen = new JSONObject();

        LocalDateTime timelastlectura = LocalDateTime.parse(registros.get(registros.size() - 1).timestamp, formattertimestamp);

        //System.out.println("TimeLastLectura : " + formattertimestamp.format(timelastlectura).toString());
        
        resumen.put("numremarcador", dias.get(0).numremarcador);
        resumen.put("fechainilectura", labels.get(0));
        resumen.put("fechafinlectura", labels.get(labels.length() - 1));
        resumen.put("lecturaini", registros.get(0).lectura);
        resumen.put("lecturainimanual", dias.get(0).esmanual);
        resumen.put("lecturafin", registros.get(registros.size() - 1).lectura);
        resumen.put("lecturafinmanual", dias.get(dias.size() - 1).esmanual);
        resumen.put("consumototalmes", consumototal);
        resumen.put("timstampfin", formattertimestamp.format(timelastlectura).toString());
        resumen.put("horafin", formattertime.format(timelastlectura).toString());
        dataset.put("resumen", resumen);

        dataset.put("data", data);
        dataset.put("labels", labels);

        dataset.put("borderWidth", "2");
        dataset.put("label", "Remarcador ID: " + numremarcador);
        System.out.println(dataset);
        
        return dataset;
    }
    
    public static JSONObject getDatasetDesdeHastaRemarcadorNumremarcador(int numremarcador, String fechaini, String fechafin) {
        LinkedList<Registro> registros = new LinkedList<>();
        LinkedList<Dia> dias = new LinkedList<>();
        DateTimeFormatter formattertimestamp = DateTimeFormatter.ISO_DATE_TIME.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatterdate = DateTimeFormatter.ISO_DATE.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formattertime = DateTimeFormatter.ISO_TIME.ofPattern("HH:mm:ss");

        JSONObject dataset = new JSONObject();
        JSONArray labels = new JSONArray();
        JSONArray data = new JSONArray();

        System.out.println("(2) Buscando dataset remarcador num: " + numremarcador + " Desde: " + fechaini + " Hasta: " + fechafin);

        Conexion c;

        //Obtener orígen del remarcador que viene con su número
        String queryOrigen = "SELECT FN_GET_ORIGEN_NUMREMARCADOR(" + numremarcador + ") ORIGEN";
        System.out.println(queryOrigen);
        c = new Conexion();
        //c.setReplica();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(queryOrigen);
        String origen = "";
        try {
            while (rs.next()) {
                origen = rs.getString("ORIGEN");
            }
        } catch (Exception ex) {
            System.out.println("No se pudo obtener el origen del remarcador NUM: " + numremarcador);
            System.out.println(ex);
            c.cerrar();
        }
        c.cerrar();
        String query = "";

        //Ir a traer todas las lecturas del mes
        if (origen.equals("circutorcvmC10")) {
            query = "CALL SP_GET_LECTURAS_DESDE_HASTA_CIRCUTOR(" + numremarcador + ", '" + fechaini + "', '" + fechafin + "')";
        } else if (origen.equals("schneiderPM710")) {
            query = "CALL SP_GET_LECTURAS_DESDE_HASTA_PM710(" + numremarcador + ", '" + fechaini + "', '" + fechafin + "')";
        } else if (origen.equals("schneiderPM5300")) {
            query = "CALL SP_GET_LECTURAS_DESDE_HASTA_PM5300(" + numremarcador + ", '" + fechaini + "', '" + fechafin + "')";
        }
        c = new Conexion();
        //c.setReplica();
        c.abrir();
        System.out.println(query);
        rs = c.ejecutarQuery(query);
        Registro anterior = new Registro();
        String fechaanterior = "";
        float consumodiario = 0f;
        float consumototal = 0f;
        Dia d = new Dia();
        Registro r = new Registro();
        try {
            while (rs.next()) {

                r = new Registro();

                float lecactual = rs.getFloat("LECTURA");
                if (rs.wasNull()) {//Si viene una lectura nula-------------------------------------------------------------------------------------------

                    float lecactualmanual = rs.getFloat("LECTURAMANUAL");
                    if (rs.wasNull()) { //Si la lectura manual también es nula --------------------------------------------------------------------------
                        if (anterior.dia != null) { //Cuando existe un registro anterior
                            r = new Registro(rs.getString("DIA"), rs.getInt("NUMREMARCADOR"), rs.getString("TIMESTAMP"), anterior.lectura);
                            //r.esmanual = true;
                            r.delta = 0f;
                        } else { //Cuando no existe un registro anterior, no se puede obtener lectura hacia atrás.
                            //Se deja la lectura y delta en cero.
                            r = new Registro(rs.getString("DIA"), rs.getInt("NUMREMARCADOR"), rs.getString("TIMESTAMP"), 0f);
                            r.delta = 0f;
                        }
                        r.delta = 0f; //Se deja el delta en cero.
                    } else { //Si la lectura manual no es nula.
                        r = new Registro(rs.getString("DIA"), rs.getInt("NUMREMARCADOR"), rs.getString("TIMESTAMP"), rs.getFloat("LECTURAMANUAL"));
                        r.esmanual = true;
                        if (!anterior.existe) { //Cuando existe un registro anterior, es decir, es el primero
                            r.delta = anterior.lectura - r.lectura;
                        } else { //Cuando no existe un registro anterior, no se puede obtener lectura hacia atrás.
                            //Se deja el delta en cero
                            r.delta = 0f;
                        }
                    }
                } else { //La lectura que viene no es nula----------------------------------------------------------------------------------------------
                    r = new Registro(rs.getString("DIA"), rs.getInt("NUMREMARCADOR"), rs.getString("TIMESTAMP"), rs.getFloat("LECTURA"));
                    r.existe = true;
                    float lecactualmanual = rs.getFloat("LECTURAMANUAL");
                    if (!rs.wasNull()) {//Si la lectura es manual, manda ésta. Se cambia.
                        r.esmanual = true;
                        r.lectura = rs.getFloat("LECTURAMANUAL");
                    }
                    if (!anterior.existe) { //Si no existe anterior. es decir, ésta es la primera
                        r.delta = 0;
                    } else {//No es la primera
                        if(r.lectura >= anterior.lectura){
                            r.delta = r.lectura - anterior.lectura;
                        }else{
                            //r.lectura = anterior.lectura;
                            r.delta = 0f;
                        }
                        
                    }
                }

                //Preparar los días----------------------------------------------------------------------------------------------------------------------
                if (!rs.getString("DIA").equals(fechaanterior)) {//Si la fecha del día que viene es distinta de la almacenada anterior
                    if (fechaanterior.equals("")) {//Cuando el que viene es el primer registro
                        consumodiario = consumodiario + r.delta;
                        consumototal = consumototal + r.delta;
                        d = new Dia();
                        d.consumo = consumodiario;
                        if (r.esmanual) {
                            d.esmanual = true;
                        }
                        d.fecha = r.dia;
                        //labels.put(r.dia);
                        d.lecturainicial = r.lectura;
                        d.numremarcador = r.numremarcador;
                    } else {//Cuando es un cambio de día
                        d.lecturafinal = anterior.lectura;
                        if (anterior.esmanual) {
                            d.esmanual = true;
                        }
                        dias.add(d);
                        //data.put(d.consumo);
                        d = new Dia();
                        d.consumo = 0f + r.delta;
                        consumototal = consumototal + r.delta;
                        d.fecha = r.dia;
                        //labels.put(r.dia);
                        d.lecturainicial = r.lectura;
                        if (r.esmanual) {
                            d.esmanual = true;
                        }
                        d.numremarcador = r.numremarcador;

                    }
                } else {//El dia que viene es igual al anterior
                    d.consumo = d.consumo + r.delta;
                    consumototal = consumototal + r.delta;
                    if (r.esmanual) {
                        d.esmanual = true;
                    }
                }

                anterior = r;
                registros.add(r);
                fechaanterior = rs.getString("DIA");
                //System.out.println(r.numremarcador + ";" + r.timestamp + ";" + r.lectura + ";" + r.lecturaManual + ";" + r.delta + ";" + r.esmanual);
            }
        } catch (Exception ex) {
            System.out.println("No se pudo obtener los registros del remarcador.");
            System.out.println(ex);
            c.cerrar();
        }

        consumodiario = consumodiario + r.delta;
        consumototal = consumototal + r.delta;
        if (r.esmanual) {
            d.esmanual = true;
        }
        d.lecturafinal = r.lectura;
        dias.add(d);
        //data.put(d.consumo);
        c.cerrar();
        
        //dias.remove(0);
        for(Dia dia : dias){
            //System.out.println(dia.numremarcador + ";" + dia.fecha + ";" + dia.lecturainicial + ";" + dia.lecturafinal + ";" + dia.consumo + ";" + dia.esmanual);
            labels.put(dia.fecha);
            data.put(dia.consumo);
        }
        JSONObject resumen = new JSONObject();

        LocalDateTime timelastlectura = LocalDateTime.parse(registros.get(registros.size() - 1).timestamp, formattertimestamp);

        //System.out.println("TimeLastLectura : " + formattertimestamp.format(timelastlectura).toString());
        
        resumen.put("numremarcador", dias.get(0).numremarcador);
        resumen.put("fechainilectura", labels.get(0));
        resumen.put("fechafinlectura", labels.get(labels.length() - 1));
        resumen.put("lecturaini", dias.get(0).lecturainicial);
        resumen.put("lecturainimanual", dias.get(0).esmanual);
        resumen.put("lecturafin", dias.get(dias.size() - 1).lecturafinal);
        resumen.put("lecturafinmanual", dias.get(dias.size() - 1).esmanual);
        resumen.put("consumototalmes", consumototal);
        resumen.put("timstampfin", formattertimestamp.format(timelastlectura).toString());
        resumen.put("horafin", formattertime.format(timelastlectura).toString());
        dataset.put("resumen", resumen);

        dataset.put("data", data);
        dataset.put("labels", labels);

        dataset.put("borderWidth", "2");
        dataset.put("label", "Remarcador ID: " + numremarcador);
        System.out.println(dataset);
        
        return dataset;
    }
    
    public static LinkedList<Dia> getDiasDesdeHastaRemarcadorNumremarcador(int numremarcador, String fechaini, String fechafin) {
        LinkedList<Registro> registros = new LinkedList<>();
        LinkedList<Dia> dias = new LinkedList<>();
        DateTimeFormatter formattertimestamp = DateTimeFormatter.ISO_DATE_TIME.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatterdate = DateTimeFormatter.ISO_DATE.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formattertime = DateTimeFormatter.ISO_TIME.ofPattern("HH:mm:ss");

        JSONObject dataset = new JSONObject();
        JSONArray labels = new JSONArray();
        JSONArray data = new JSONArray();

        System.out.println("(3) Buscando dataset remarcador num: " + numremarcador + " Desde: " + fechaini + " Hasta: " + fechafin);

        Conexion c;

        //Obtener orígen del remarcador que viene con su número
        String queryOrigen = "SELECT FN_GET_ORIGEN_NUMREMARCADOR(" + numremarcador + ") ORIGEN";
        System.out.println(queryOrigen);
        c = new Conexion();
        //c.setReplica();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(queryOrigen);
        String origen = "";
        try {
            while (rs.next()) {
                origen = rs.getString("ORIGEN");
            }
        } catch (Exception ex) {
            System.out.println("No se pudo obtener el origen del remarcador NUM: " + numremarcador);
            System.out.println(ex);
            c.cerrar();
        }
        c.cerrar();
        String query = "";

        //Ir a traer todas las lecturas del mes
        if (origen.equals("circutorcvmC10")) {
            query = "CALL SP_GET_LECTURAS_DESDE_HASTA_CIRCUTOR(" + numremarcador + ", '" + fechaini + "', '" + fechafin + "')";
        } else if (origen.equals("schneiderPM710")) {
            query = "CALL SP_GET_LECTURAS_DESDE_HASTA_PM710(" + numremarcador + ", '" + fechaini + "', '" + fechafin + "')";
        } else if (origen.equals("schneiderPM5300")) {
            query = "CALL SP_GET_LECTURAS_DESDE_HASTA_PM5300(" + numremarcador + ", '" + fechaini + "', '" + fechafin + "')";
        }
        c = new Conexion();
        //c.setReplica();
        c.abrir();
        System.out.println(query);
        rs = c.ejecutarQuery(query);
        Registro anterior = new Registro();
        String fechaanterior = "";
        float consumodiario = 0f;
        float consumototal = 0f;
        Dia d = new Dia();
        Registro r = new Registro();
        try {
            while (rs.next()) {

                r = new Registro();

                float lecactual = rs.getFloat("LECTURA");
                if (rs.wasNull()) {//Si viene una lectura nula-------------------------------------------------------------------------------------------

                    float lecactualmanual = rs.getFloat("LECTURAMANUAL");
                    if (rs.wasNull()) { //Si la lectura manual también es nula --------------------------------------------------------------------------
                        if (anterior.dia != null) { //Cuando existe un registro anterior
                            r = new Registro(rs.getString("DIA"), rs.getInt("NUMREMARCADOR"), rs.getString("TIMESTAMP"), anterior.lectura);
                            //r.esmanual = true;
                            r.delta = 0f;
                        } else { //Cuando no existe un registro anterior, no se puede obtener lectura hacia atrás.
                            //Se deja la lectura y delta en cero.
                            r = new Registro(rs.getString("DIA"), rs.getInt("NUMREMARCADOR"), rs.getString("TIMESTAMP"), 0f);
                            r.delta = 0f;
                        }
                        r.delta = 0f; //Se deja el delta en cero.
                    } else { //Si la lectura manual no es nula.
                        r = new Registro(rs.getString("DIA"), rs.getInt("NUMREMARCADOR"), rs.getString("TIMESTAMP"), rs.getFloat("LECTURAMANUAL"));
                        r.esmanual = true;
                        if (anterior.existe) { //Cuando existe un registro anterior, es decir, es el primero
                            if(r.lectura >= anterior.lectura){
                                r.delta = anterior.lectura - r.lectura;
                            }else{
                                r.delta = 0f;
                            }
                            
                        } else { //Cuando no existe un registro anterior, no se puede obtener lectura hacia atrás.
                            //Se deja el delta en cero
                            r.delta = 0f;
                        }
                    }
                } else { //La lectura que viene no es nula----------------------------------------------------------------------------------------------
                    r = new Registro(rs.getString("DIA"), rs.getInt("NUMREMARCADOR"), rs.getString("TIMESTAMP"), rs.getFloat("LECTURA"));
                    r.existe = true;
                    float lecactualmanual = rs.getFloat("LECTURAMANUAL");
                    if (!rs.wasNull()) {//Si la lectura es manual, manda ésta. Se cambia.
                        r.esmanual = true;
                        r.lectura = rs.getFloat("LECTURAMANUAL");
                    }
                    if (!anterior.existe) { //Si no existe anterior. es decir, ésta es la primera
                        r.delta = 0;
                    } else {//No es la primera
                        if(r.lectura >= anterior.lectura){
                            r.delta = r.lectura - anterior.lectura;
                        }else{
                            //r.lectura = anterior.lectura;
                            r.delta = 0f;
                        }
                        
                    }
                }

                //Preparar los días----------------------------------------------------------------------------------------------------------------------
                if (!rs.getString("DIA").equals(fechaanterior)) {//Si la fecha del día que viene es distinta de la almacenada anterior
                    if (fechaanterior.equals("")) {//Cuando el que viene es el primer registro
                        consumodiario = 0f;
                        consumodiario = consumodiario + r.delta;
                        consumototal = consumototal + r.delta;
                        d = new Dia();
                        d.consumo = consumodiario;
                        if (r.esmanual) {
                            d.esmanual = true;
                        }
                        d.fecha = r.dia;
                        //labels.put(r.dia);
                        d.lecturainicial = r.lectura;
                        d.numremarcador = r.numremarcador;
                    } else {//Cuando es un cambio de día
                        d.lecturafinal = anterior.lectura;
                        if (anterior.esmanual) {
                            d.esmanual = true;
                        }
                        consumodiario = consumodiario + anterior.delta;
                        dias.add(d);
                        //data.put(d.consumo);
                        d = new Dia();
                        consumodiario = 0f;
                        d.consumo = 0f + r.delta;
                        consumototal = consumototal + r.delta;
                        d.fecha = r.dia;
                        //labels.put(r.dia);
                        d.lecturainicial = r.lectura;
                        if (r.esmanual) {
                            d.esmanual = true;
                        }
                        d.numremarcador = r.numremarcador;

                    }
                } else {//El dia que viene es igual al anterior
                    d.consumo = d.consumo + r.delta;
                    consumototal = consumototal + r.delta;
                    if (r.esmanual) {
                        d.esmanual = true;
                    }
                }

                anterior = r;
                registros.add(r);
                fechaanterior = rs.getString("DIA");
                //System.out.println(r.numremarcador + ";" + r.timestamp + ";" + r.lectura + ";" + r.lecturaManual + ";" + r.delta + ";" + r.esmanual);
            }
        } catch (Exception ex) {
            System.out.println("No se pudo obtener los registros del remarcador.");
            System.out.println(ex);
            c.cerrar();
        }

        consumodiario = consumodiario + r.delta;
        consumototal = consumototal + r.delta;
        if (r.esmanual) {
            d.esmanual = true;
        }
        d.lecturafinal = r.lectura;
        dias.add(d);
        //data.put(d.consumo);
        c.cerrar();
        /* -----------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //dias.remove(0);
        for(Dia dia : dias){
            //System.out.println(dia.numremarcador + ";" + dia.fecha + ";" + dia.lecturainicial + ";" + dia.lecturafinal + ";" + dia.consumo + ";" + dia.esmanual);
            labels.put(dia.fecha);
            data.put(dia.consumo);
        }
        JSONObject resumen = new JSONObject();

        LocalDateTime timelastlectura = LocalDateTime.parse(registros.get(registros.size() - 1).timestamp, formattertimestamp);

        //System.out.println("TimeLastLectura : " + formattertimestamp.format(timelastlectura).toString());
        
        resumen.put("numremarcador", dias.get(0).numremarcador);
        resumen.put("fechainilectura", labels.get(0));
        resumen.put("fechafinlectura", labels.get(labels.length() - 1));
        resumen.put("lecturaini", dias.get(0).lecturainicial);
        resumen.put("lecturainimanual", dias.get(0).esmanual);
        resumen.put("lecturafin", dias.get(dias.size() - 1).lecturafinal);
        resumen.put("lecturafinmanual", dias.get(dias.size() - 1).esmanual);
        resumen.put("consumototalmes", consumototal);
        resumen.put("timstampfin", formattertimestamp.format(timelastlectura).toString());
        resumen.put("horafin", formattertime.format(timelastlectura).toString());
        dataset.put("resumen", resumen);

        dataset.put("data", data);
        dataset.put("labels", labels);

        dataset.put("borderWidth", "2");
        dataset.put("label", "Remarcador ID: " + numremarcador);
        System.out.println(dataset);
        ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/
        return dias;
    }
    
    public static LinkedList<Registro> getRegistrosDesdeHastaRemarcadorNumremarcador(int numremarcador, String fechadesde, String fechahasta) {
        LinkedList<Registro> registros = new LinkedList<>();
        System.out.println("Buscando dataset remarcador num: " + numremarcador + " Desde: " + fechadesde + " hasta: " + fechahasta);

        Conexion c;

        //Obtener orígen del remarcador que viene con su número
        String queryOrigen = "SELECT FN_GET_ORIGEN_NUMREMARCADOR(" + numremarcador + ") ORIGEN";
        System.out.println(queryOrigen);
        c = new Conexion();
        //c.setReplica();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(queryOrigen);
        String origen = "";
        try {
            while (rs.next()) {
                origen = rs.getString("ORIGEN");
            }
        } catch (Exception ex) {
            System.out.println("No se pudo obtener el origen del remarcador NUM: " + numremarcador);
            System.out.println(ex);
            c.cerrar();
        }
        c.cerrar();
        String query = "";

        //Ir a traer todas las lecturas del mes
        if (origen.equals("circutorcvmC10")) {
            query = "CALL SP_GET_LECTURAS_DESDE_HASTA_CIRCUTOR(" + numremarcador + ", '" + fechadesde + "', '" + fechahasta + "')";
        } else if (origen.equals("schneiderPM710")) {
            query = "CALL SP_GET_LECTURAS_DESDE_HASTA_PM710(" + numremarcador + ", '" + fechadesde + "', '" + fechahasta + "')";
        } else if (origen.equals("schneiderPM5300")) {
            query = "CALL SP_GET_LECTURAS_DESDE_HASTA_PM5300(" + numremarcador + ", '" + fechadesde + "', '" + fechahasta + "')";
        }
        c = new Conexion();
        //c.setReplica();
        c.abrir();
        System.out.println(query);
        rs = c.ejecutarQuery(query);
        Registro anterior = new Registro();
        Dia d = new Dia();
        Registro r = new Registro();
        try {
            while (rs.next()) {

                r = new Registro();

                float lecactual = rs.getFloat("LECTURA");
                if (rs.wasNull()) {//Si viene una lectura nula-------------------------------------------------------------------------------------------
                    float lecactualmanual = rs.getFloat("LECTURAMANUAL");
                    if (rs.wasNull()) { //Si la lectura manual también es nula --------------------------------------------------------------------------
                        if (anterior.dia != null) { //Cuando existe un registro anterior
                            r = new Registro(rs.getString("DIA"), rs.getInt("NUMREMARCADOR"), rs.getString("TIMESTAMP"), anterior.lectura);
                            //r.esmanual = true;
                            r.delta = 0f;
                        } else { //Cuando no existe un registro anterior, no se puede obtener lectura hacia atrás.
                            //Se deja la lectura y delta en cero.
                            r = new Registro(rs.getString("DIA"), rs.getInt("NUMREMARCADOR"), rs.getString("TIMESTAMP"), 0f);
                            r.delta = 0f;
                        }
                        r.delta = 0f; //Se deja el delta en cero.
                    } else { //Si la lectura manual no es nula.
                        r = new Registro(rs.getString("DIA"), rs.getInt("NUMREMARCADOR"), rs.getString("TIMESTAMP"), rs.getFloat("LECTURAMANUAL"));
                        r.existe = true;
                        r.esmanual = true;
                        if (!anterior.existe) { //Cuando existe un registro anterior, es decir, es el primero
                            r.delta = anterior.lectura - r.lectura;
                        } else { //Cuando no existe un registro anterior, no se puede obtener lectura hacia atrás.
                            //Se deja el delta en cero
                            r.delta = 0f;
                        }
                    }
                } else { //La lectura que viene no es nula----------------------------------------------------------------------------------------------
                    r = new Registro(rs.getString("DIA"), rs.getInt("NUMREMARCADOR"), rs.getString("TIMESTAMP"), rs.getFloat("LECTURA"));
                    r.existe = true;
                    float lecactualmanual = rs.getFloat("LECTURAMANUAL");
                    if (!rs.wasNull()) {//Si la lectura es manual, manda ésta. Se cambia.
                        r.esmanual = true;
                        r.lectura = rs.getFloat("LECTURAMANUAL");
                    }
                    if (!anterior.existe) { //Si no existe anterior. es decir, ésta es la primera
                        r.delta = 0;
                    } else {//No es la primera
                        if(r.lectura >= anterior.lectura){
                            r.delta = r.lectura - anterior.lectura;
                        }else{
                            //r.lectura = anterior.lectura;
                            r.delta = 0f;
                        }
                        
                    }
                }

                anterior = r;
                registros.add(r);
            }
        } catch (Exception ex) {
            System.out.println("No se pudo obtener los registros del remarcador.");
            System.out.println(ex);
            c.cerrar();
        }
        return registros;
    }
    
    public static LinkedList<Registro> getRegistrosMesRemarcadorNumremarcador(int numremarcador, int anio, int mes) {
        LinkedList<Registro> registros = new LinkedList<>();
        System.out.println("Buscando dataset remarcador num: " + numremarcador + " Año: " + anio + " mes: " + mes);

        Conexion c;

        //Obtener orígen del remarcador que viene con su número
        String queryOrigen = "SELECT FN_GET_ORIGEN_NUMREMARCADOR(" + numremarcador + ") ORIGEN";
        System.out.println(queryOrigen);
        c = new Conexion();
        //c.setReplica();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(queryOrigen);
        String origen = "";
        try {
            while (rs.next()) {
                origen = rs.getString("ORIGEN");
            }
        } catch (Exception ex) {
            System.out.println("No se pudo obtener el origen del remarcador NUM: " + numremarcador);
            System.out.println(ex);
            c.cerrar();
        }
        c.cerrar();
        String query = "";

        //Ir a traer todas las lecturas del mes
        if (origen.equals("circutorcvmC10")) {
            query = "CALL SP_GET_LECTURAS_MES_CIRCUTOR(" + numremarcador + ", " + anio + ", " + mes + ")";
        } else if (origen.equals("schneiderPM710")) {
            query = "CALL SP_GET_LECTURAS_MES_PM710(" + numremarcador + ", " + anio + ", " + mes + ")";
        } else if (origen.equals("schneiderPM5300")) {
            query = "CALL SP_GET_LECTURAS_MES_PM5300(" + numremarcador + ", " + anio + ", " + mes + ")";
        }
        c = new Conexion();
        //c.setReplica();
        c.abrir();
        System.out.println(query);
        rs = c.ejecutarQuery(query);
        Registro anterior = new Registro();
        Dia d = new Dia();
        Registro r = new Registro();
        try {
            while (rs.next()) {

                r = new Registro();

                float lecactual = rs.getFloat("LECTURA");
                if (rs.wasNull()) {//Si viene una lectura nula-------------------------------------------------------------------------------------------
                    float lecactualmanual = rs.getFloat("LECTURAMANUAL");
                    if (rs.wasNull()) { //Si la lectura manual también es nula --------------------------------------------------------------------------
                        if (anterior.dia != null) { //Cuando existe un registro anterior
                            r = new Registro(rs.getString("DIA"), rs.getInt("NUMREMARCADOR"), rs.getString("TIMESTAMP"), anterior.lectura);
                            //r.esmanual = true;
                            r.delta = 0f;
                        } else { //Cuando no existe un registro anterior, no se puede obtener lectura hacia atrás.
                            //Se deja la lectura y delta en cero.
                            r = new Registro(rs.getString("DIA"), rs.getInt("NUMREMARCADOR"), rs.getString("TIMESTAMP"), 0f);
                            r.delta = 0f;
                        }
                        r.delta = 0f; //Se deja el delta en cero.
                    } else { //Si la lectura manual no es nula.
                        r = new Registro(rs.getString("DIA"), rs.getInt("NUMREMARCADOR"), rs.getString("TIMESTAMP"), rs.getFloat("LECTURAMANUAL"));
                        r.existe = true;
                        r.esmanual = true;
                        if (!anterior.existe) { //Cuando existe un registro anterior, es decir, es el primero
                            r.delta = anterior.lectura - r.lectura;
                        } else { //Cuando no existe un registro anterior, no se puede obtener lectura hacia atrás.
                            //Se deja el delta en cero
                            r.delta = 0f;
                        }
                    }
                } else { //La lectura que viene no es nula----------------------------------------------------------------------------------------------
                    r = new Registro(rs.getString("DIA"), rs.getInt("NUMREMARCADOR"), rs.getString("TIMESTAMP"), rs.getFloat("LECTURA"));
                    r.existe = true;
                    float lecactualmanual = rs.getFloat("LECTURAMANUAL");
                    if (!rs.wasNull()) {//Si la lectura es manual, manda ésta. Se cambia.
                        r.esmanual = true;
                        r.lectura = rs.getFloat("LECTURAMANUAL");
                    }
                    if (!anterior.existe) { //Si no existe anterior. es decir, ésta es la primera
                        r.delta = 0;
                    } else {//No es la primera
                        if(r.lectura >= anterior.lectura){
                            r.delta = r.lectura - anterior.lectura;
                        }else{
                            //r.lectura = anterior.lectura;
                            r.delta = 0f;
                        }
                        
                    }
                }

                anterior = r;
                registros.add(r);
            }
        } catch (Exception ex) {
            System.out.println("No se pudo obtener los registros del remarcador.");
            System.out.println(ex);
            c.cerrar();
        }
        return registros;
    }

    public static JSONObject getDatasetPotenciasMesRemarcadorNumremarcador(Integer numremarcador, Integer anio, Integer mes) {
        LinkedList<RegistroPotencia> registros = new LinkedList<>();
        JSONObject dataset = new JSONObject();

        System.out.println("Buscando dataset remarcador num: " + numremarcador + " Año: " + anio + " Mes: " + mes);

        //Obtener orígen del remarcador que viene con su número
        String queryOrigen = "SELECT FN_GET_ORIGEN_NUMREMARCADOR(" + numremarcador + ") ORIGEN";
        System.out.println(queryOrigen);
        Conexion c = new Conexion();
        //c.setReplica();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(queryOrigen);
        String origen = "";

        try {
            while (rs.next()) {
                origen = rs.getString("ORIGEN");
            }
        } catch (Exception ex) {
            System.out.println("No se pudo obtener el origen del remarcador NUM: " + numremarcador);
            System.out.println(ex);
            c.cerrar();
            //return new JSONObject();
        }
        c.cerrar();
        String query = "";

        //Ir a traer todas las lecturas del mes
        if (origen.equals("circutorcvmC10")) {
            query = "CALL SP_GET_POTENCIAS_MES_CIRCUTOR(" + numremarcador + ", " + anio + ", " + mes + ")";
        } else if (origen.equals("schneiderPM710")) {
            query = "CALL SP_GET_POTENCIAS_MES_PM710(" + numremarcador + ", " + anio + ", " + mes + ")";
        } else if (origen.equals("schneiderPM5300")) {
            query = "CALL SP_GET_POTENCIAS_MES_PM5300(" + numremarcador + ", " + anio + ", " + mes + ")";
        }
        c = new Conexion();
        //c.setReplica();
        c.abrir();
        System.out.println(query);
        rs = c.ejecutarQuery(query);
        try {
            while (rs.next()) {

                RegistroPotencia rp = new RegistroPotencia();
                double lec = rs.getDouble("POTENCIA");
                if (rs.wasNull()) {
                    rp = new RegistroPotencia(rs.getString("FECHA"), rs.getInt("NUMREMARCADOR"), rs.getString("TIMESTAMP"), null, rs.getInt("ANIO"), rs.getInt("MES"), rs.getInt("DIA"), rs.getInt("HH"), rs.getInt("MM"), rs.getInt("SS"));
                } else {
                    rp = new RegistroPotencia(rs.getString("FECHA"), rs.getInt("NUMREMARCADOR"), rs.getString("TIMESTAMP"), rs.getBigDecimal("POTENCIA"), rs.getInt("ANIO"), rs.getInt("MES"), rs.getInt("DIA"), rs.getInt("HH"), rs.getInt("MM"), rs.getInt("SS"));
                }
                registros.add(rp);
            }
        } catch (Exception ex) {
            System.out.println("No se pudo obtener los registros de potencia del remarcador.");
            System.out.println(ex);
            c.cerrar();
            //return new JSONObject();
        }
        c.cerrar();

        LinkedList<DiaPotencia> dias = new LinkedList<>();
        String fechaActual = "";
        BigDecimal consumoDia = new BigDecimal(0);

        BigDecimal demmax = new BigDecimal(0);
        BigDecimal demmaxhp = new BigDecimal(0);
        int last = 0;
        for (int i = 0; i < registros.size(); i++) {
            //if (registros.get(i).potencia != null) {
            if (!registros.get(i).timestamp.substring(0, 10).equals(fechaActual)) {
                if (!fechaActual.equals("")) {
                    DiaPotencia d = new DiaPotencia();
                    d.demmax = demmax;
                    d.demmaxhpunta = demmaxhp;
                    d.fecha = fechaActual;
                    d.numremarcador = registros.get(i).numremarcador;
                    dias.add(d);
                }
                demmax = new BigDecimal(0);
                demmaxhp = new BigDecimal(0);
                fechaActual = registros.get(i).timestamp.substring(0, 10);
                if (registros.get(i).hh == null) {
                    demmax = new BigDecimal(0);
                    demmaxhp = new BigDecimal(0);
                } else {
                    if (registros.get(i).hh >= 18 && registros.get(i).hh <= 23) {
                        if (demmaxhp.compareTo(registros.get(i).potencia) <= 0) {
                            demmaxhp = registros.get(i).potencia;
                        }
                    } else {
                        if (demmax.compareTo(registros.get(i).potencia) <= 0) {
                            demmax = registros.get(i).potencia;
                        }
                    }
                }

            } else {
                if (registros.get(i).hh == null) {
                    demmax = new BigDecimal(0);
                    demmaxhp = new BigDecimal(0);
                } else {
                    if (registros.get(i).hh >= 18 && registros.get(i).hh <= 23) {
                        if (demmaxhp.compareTo(registros.get(i).potencia) <= 0) {
                            demmaxhp = registros.get(i).potencia;
                        }
                    } else {
                        if (demmax.compareTo(registros.get(i).potencia) <= 0) {
                            demmax = registros.get(i).potencia;
                        }
                    }
                }

            }
            last = i;
            //}

            //System.out.println(r.numremarcador + ";" + r.timestamp + ";" + r.lectura + ";" + r.lecturaManual + ";" + r.delta + ";");
        }
        DiaPotencia d = new DiaPotencia();
        d.demmax = demmax;
        d.demmaxhpunta = demmaxhp;
        d.fecha = fechaActual;
        d.numremarcador = registros.get(last).numremarcador;
        dias.add(d);

        //QUITAR LOS ANTERIORES Y POSTERIORES AL MES QUE SE CONSULTA
        LinkedList<DiaPotencia> diasSalida = new LinkedList<>();

        for (DiaPotencia dia : dias) {
            if (Integer.parseInt(dia.fecha.substring(5, 7)) == mes) {
                diasSalida.add(dia);
            }
        }

        JSONArray labels = new JSONArray();
        JSONArray datademanda = new JSONArray();
        JSONArray datademandahp = new JSONArray();

        JSONObject datasetdemanda = new JSONObject();
        JSONObject datasethpunta = new JSONObject();

        BigDecimal maxdemandaleida = new BigDecimal(0);
        BigDecimal maxdemandahp = new BigDecimal(0);

        for (DiaPotencia dia : diasSalida) {
            labels.put(dia.fecha);
            datademanda.put(dia.demmax);
            if (maxdemandaleida.compareTo(dia.demmax) == -1) {
                maxdemandaleida = dia.demmax;
            }
            datademandahp.put(dia.demmaxhpunta);
            if (maxdemandahp.compareTo(dia.demmaxhpunta) == -1) {
                maxdemandahp = dia.demmaxhpunta;
            }
        }

        JSONObject resumen = new JSONObject();
        resumen.put("numremarcador", diasSalida.get(0).numremarcador);
        resumen.put("maxdemandaleida", maxdemandaleida);
        resumen.put("maxdemandahpunta", maxdemandahp);

        datasetdemanda.put("label", "Demanda Máx. Remarcador ID: " + numremarcador);
        datasethpunta.put("label", "Demanda Máx. H. Punta Remarcador ID: " + numremarcador);

        datasetdemanda.put("data", datademanda);
        datasethpunta.put("data", datademandahp);

        dataset.put("resumen", resumen);
        dataset.put("datasetdemanda", datasetdemanda);
        dataset.put("datasethpunta", datasethpunta);
        dataset.put("labels", labels);
        dataset.put("borderWidth", "2");
        dataset.put("label", "Remarcador ID: " + numremarcador);

        //System.out.println(dataset);
        return dataset;
    }
    
    public static LinkedList<RegistroPotencia> getRegistrosPotenciaMesRemarcadorNumremarcador(Integer numremarcador, Integer anio, Integer mes) {
        LinkedList<RegistroPotencia> registros = new LinkedList<>();

        System.out.println("Buscando dataset remarcador num: " + numremarcador + " Año: " + anio + " Mes: " + mes);

        //Obtener orígen del remarcador que viene con su número
        String queryOrigen = "SELECT FN_GET_ORIGEN_NUMREMARCADOR(" + numremarcador + ") ORIGEN";
        System.out.println(queryOrigen);
        Conexion c = new Conexion();
        //c.setReplica();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(queryOrigen);
        String origen = "";

        try {
            while (rs.next()) {
                origen = rs.getString("ORIGEN");
            }
        } catch (Exception ex) {
            System.out.println("No se pudo obtener el origen del remarcador NUM: " + numremarcador);
            System.out.println(ex);
            c.cerrar();
            //return new JSONObject();
        }
        c.cerrar();
        String query = "";

        //Ir a traer todas las lecturas del mes
        if (origen.equals("circutorcvmC10")) {
            query = "CALL SP_GET_POTENCIAS_MES_CIRCUTOR(" + numremarcador + ", " + anio + ", " + mes + ")";
        } else if (origen.equals("schneiderPM710")) {
            query = "CALL SP_GET_POTENCIAS_MES_PM710(" + numremarcador + ", " + anio + ", " + mes + ")";
        } else if (origen.equals("schneiderPM5300")) {
            query = "CALL SP_GET_POTENCIAS_MES_PM5300(" + numremarcador + ", " + anio + ", " + mes + ")";
        }
        c = new Conexion();
        //c.setReplica();
        c.abrir();
        System.out.println(query);
        rs = c.ejecutarQuery(query);
        try {
            while (rs.next()) {

                RegistroPotencia rp = new RegistroPotencia();
                double lec = rs.getDouble("POTENCIA");
                if (rs.wasNull()) {
                    rp = new RegistroPotencia(rs.getString("FECHA"), rs.getInt("NUMREMARCADOR"), rs.getString("TIMESTAMP"), null, rs.getInt("ANIO"), rs.getInt("MES"), rs.getInt("DIA"), rs.getInt("HH"), rs.getInt("MM"), rs.getInt("SS"));
                } else {
                    rp = new RegistroPotencia(rs.getString("FECHA"), rs.getInt("NUMREMARCADOR"), rs.getString("TIMESTAMP"), rs.getBigDecimal("POTENCIA"), rs.getInt("ANIO"), rs.getInt("MES"), rs.getInt("DIA"), rs.getInt("HH"), rs.getInt("MM"), rs.getInt("SS"));
                }
                registros.add(rp);
            }
        } catch (Exception ex) {
            System.out.println("No se pudo obtener los registros de potencia del remarcador.");
            System.out.println(ex);
            c.cerrar();
            //return new JSONObject();
        }
        c.cerrar();

        return registros;
    }

    public static LinkedList<DiaPotencia> getPotenciaPorAnioMes(Integer numremarcador, Integer anio, Integer mes) {
        LinkedList<RegistroPotencia> registros = new LinkedList<>();

        System.out.println("Buscando dataset remarcador num: " + numremarcador + " Año: " + anio + " Mes: " + mes);

        //Obtener orígen del remarcador que viene con su número
        String queryOrigen = "SELECT FN_GET_ORIGEN_NUMREMARCADOR(" + numremarcador + ") ORIGEN";
        System.out.println(queryOrigen);
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(queryOrigen);
        String origen = "";

        try {
            while (rs.next()) {
                origen = rs.getString("ORIGEN");
            }
        } catch (Exception ex) {
            System.out.println("No se pudo obtener el origen del remarcador NUM: " + numremarcador);
            System.out.println(ex);
            c.cerrar();
            //return new JSONObject();
        }
        c.cerrar();
        String query = "";

        //Ir a traer todas las lecturas del mes
        if (origen.equals("circutorcvmC10")) {
            query = "CALL SP_GET_POTENCIAS_MES_CIRCUTOR(" + numremarcador + ", " + anio + ", " + mes + ")";
        } else if (origen.equals("schneiderPM710")) {
            query = "CALL SP_GET_POTENCIAS_MES_PM710(" + numremarcador + ", " + anio + ", " + mes + ")";
        } else if (origen.equals("schneiderPM5300")) {
            query = "CALL SP_GET_POTENCIAS_MES_PM5300(" + numremarcador + ", " + anio + ", " + mes + ")";
        }
        c = new Conexion();
        c.abrir();
        System.out.println(query);
        rs = c.ejecutarQuery(query);
        try {
            while (rs.next()) {

                RegistroPotencia rp = new RegistroPotencia();
                double lec = rs.getDouble("POTENCIA");
                if (rs.wasNull()) {
                    rp = new RegistroPotencia(rs.getString("FECHA"), rs.getInt("NUMREMARCADOR"), rs.getString("TIMESTAMP"), null, rs.getInt("ANIO"), rs.getInt("MES"), rs.getInt("DIA"), rs.getInt("HH"), rs.getInt("MM"), rs.getInt("SS"));
                } else {
                    rp = new RegistroPotencia(rs.getString("FECHA"), rs.getInt("NUMREMARCADOR"), rs.getString("TIMESTAMP"), rs.getBigDecimal("POTENCIA"), rs.getInt("ANIO"), rs.getInt("MES"), rs.getInt("DIA"), rs.getInt("HH"), rs.getInt("MM"), rs.getInt("SS"));
                }
                registros.add(rp);
            }
        } catch (Exception ex) {
            System.out.println("No se pudo obtener los registros de potencia del remarcador.");
            System.out.println(ex);
            c.cerrar();
            //return new JSONObject();
        }
        c.cerrar();

        LinkedList<DiaPotencia> dias = new LinkedList<>();
        String fechaActual = "";
        BigDecimal consumoDia = new BigDecimal(0);

        BigDecimal demmax = new BigDecimal(0);
        BigDecimal demmaxhp = new BigDecimal(0);
        int last = 0;
        for (int i = 0; i < registros.size(); i++) {
            //if (registros.get(i).potencia != null) {
            if (!registros.get(i).timestamp.substring(0, 10).equals(fechaActual)) {
                if (!fechaActual.equals("")) {
                    DiaPotencia d = new DiaPotencia();
                    d.demmax = demmax;
                    d.demmaxhpunta = demmaxhp;
                    d.fecha = fechaActual;
                    d.numremarcador = registros.get(i).numremarcador;
                    dias.add(d);
                }
                demmax = new BigDecimal(0);
                demmaxhp = new BigDecimal(0);
                fechaActual = registros.get(i).timestamp.substring(0, 10);
                if (registros.get(i).hh == null) {
                    demmax = new BigDecimal(0);
                    demmaxhp = new BigDecimal(0);
                } else {
                    if (registros.get(i).hh >= 18 && registros.get(i).hh <= 23) {
                        if (demmaxhp.compareTo(registros.get(i).potencia) <= 0) {
                            demmaxhp = registros.get(i).potencia;
                        }
                    } else {
                        if (demmax.compareTo(registros.get(i).potencia) <= 0) {
                            demmax = registros.get(i).potencia;
                        }
                    }
                }

            } else {
                if (registros.get(i).hh == null) {
                    demmax = new BigDecimal(0);
                    demmaxhp = new BigDecimal(0);
                } else {
                    if (registros.get(i).hh >= 18 && registros.get(i).hh <= 23) {
                        if (demmaxhp.compareTo(registros.get(i).potencia) <= 0) {
                            demmaxhp = registros.get(i).potencia;
                        }
                    } else {
                        if (demmax.compareTo(registros.get(i).potencia) <= 0) {
                            demmax = registros.get(i).potencia;
                        }
                    }
                }

            }
            last = i;
            //}

            //System.out.println(r.numremarcador + ";" + r.timestamp + ";" + r.lectura + ";" + r.lecturaManual + ";" + r.delta + ";");
        }
        DiaPotencia d = new DiaPotencia();
        d.demmax = demmax;
        d.demmaxhpunta = demmaxhp;
        d.fecha = fechaActual;
        d.numremarcador = registros.get(last).numremarcador;
        dias.add(d);

        //QUITAR LOS ANTERIORES Y POSTERIORES AL MES QUE SE CONSULTA
        LinkedList<DiaPotencia> diasSalida = new LinkedList<>();

        for (DiaPotencia dia : dias) {
            if (Integer.parseInt(dia.fecha.substring(5, 7)) == mes) {
                diasSalida.add(dia);
            }
        }

        return diasSalida;
    }

}
