package modelo;

import clases.json.JSONArray;
import clases.json.JSONObject;
import java.sql.ResultSet;
import java.util.LinkedList;

public class Lectura {

    public static JSONObject getDatasetMesRemarcadorNumremarcador(Integer numremarcador, Integer anio, Integer mes) {
        LinkedList<Registro> registros = new LinkedList<>();
        JSONObject dataset = new JSONObject();

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
            query = "CALL SP_GET_LECTURAS_MES_CIRCUTOR(" + numremarcador + ", " + anio + ", " + mes + ")";
        } else if (origen.equals("schneiderPM710")) {
            query = "CALL SP_GET_LECTURAS_MES_PM710(" + numremarcador + ", " + anio + ", " + mes + ")";
        } else if (origen.equals("schneiderPM5300")) {
            query = "CALL SP_GET_LECTURAS_MES_PM5300(" + numremarcador + ", " + anio + ", " + mes + ")";
        }
        c = new Conexion();
        c.abrir();
        System.out.println(query);
        rs = c.ejecutarQuery(query);
        try {
            while (rs.next()) {

                Registro r = new Registro();
                double lec = rs.getDouble("LECTURA");
                if (rs.wasNull()) {
                    r = new Registro(rs.getString("DIA"), rs.getInt("NUMREMARCADOR"), rs.getString("TIMESTAMP"), null);
                } else {
                    r = new Registro(rs.getString("DIA"), rs.getInt("NUMREMARCADOR"), rs.getString("TIMESTAMP"), rs.getDouble("LECTURA"));
                }
                registros.add(r);
            }
        } catch (Exception ex) {
            System.out.println("No se pudo obtener los registros del remarcador.");
            System.out.println(ex);
            c.cerrar();
            //return new JSONObject();
        }
        c.cerrar();

        //Validar si hay lectura manual para el primer registro
        String queryLecturaManual = "SELECT * FROM LECTURAMANUAL WHERE NUMREMARCADOR = " + numremarcador + " AND ANIO = " + anio + " AND MES = " + mes + " ORDER BY TIMESTAMP ASC";
        c = new Conexion();
        c.abrir();
        rs = c.ejecutarQuery(queryLecturaManual);
        try {
            while (rs.next()) {
                if (rs.getString("FECHA").equals(registros.get(1).dia)) {
                    Registro reg = registros.get(1);
                    reg.esmanual = true;
                    registros.set(1, reg);
                    //registros.get(0).lecturaManual = rs.getDouble("LECTURA");
                    for (Registro r : registros) {
                        r.lecturaManual = rs.getDouble("LECTURA");
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("No se pudo obtener la lectura manual inicial para el remarcador");
            System.out.println(ex);
            c.cerrar();
            //return new JSONObject();
        }
        c.cerrar();

        //Validar si hay lectura manual para el último registro
        queryLecturaManual = "SELECT * FROM LECTURAMANUAL WHERE NUMREMARCADOR = " + numremarcador + " AND ANIO = " + anio + " AND MES = " + mes + " ORDER BY TIMESTAMP ASC";
        c = new Conexion();
        c.abrir();
        rs = c.ejecutarQuery(queryLecturaManual);
        boolean encontrado = false;
        try {
            while (rs.next() && !encontrado) {

                for (int i = registros.size() - 1; i >= 0 || !encontrado; i--) {
                    if (rs.getString("FECHA").equals(registros.get(i).dia)) {
                        System.out.println("La fecha que viene con la manual: " + rs.getString("FECHA"));

                        registros.get(i).lecturaManual = rs.getDouble("LECTURA");
                        registros.get(i).esmanual = true;
                        encontrado = true;
                        break;
                    }
                }

            }
        } catch (Exception ex) {
            System.out.println("No se pudo obtener la lectura manual final para el remarcador");
            System.out.println(ex);
            c.cerrar();
            //return new JSONObject();
        }
        c.cerrar();

        //Procesar 
        for (int i = 0; i < registros.size(); i++) {
            Registro actual = registros.get(i);
            if (i >= 0 && i < registros.size()) {
                if (actual.lectura == null) {
                    if (actual.lecturaManual != null) {
                        actual.lectura = actual.lecturaManual;
                    } else {
                        boolean encontrado2 = false;
                        for (int x = i + 1; x < registros.size(); x++) {
                            if (registros.get(x).lectura != null) {
                                actual.lectura = registros.get(x).lectura;
                                encontrado2 = true;
                                break;
                            }
                        }
                        if (!encontrado2) {
                            for (int x = i - 1; x >= 0; x--) {
                                if (registros.get(x).lectura != null) {
                                    actual.lectura = registros.get(x).lectura;
                                    break;
                                }
                            }
                        }
                    }
                    if (actual.lectura == null) {
                        actual.lectura = 0.0D;
                    }
                } else {
                    if (actual.lecturaManual != null) {
                        if (actual.lectura < actual.lecturaManual) {
                            actual.lectura = actual.lecturaManual;
                        }
                    }
                }
            } else {
                if (actual.lectura == null) {
                    if (actual.lecturaManual != null) {
                        actual.lectura = actual.lecturaManual;
                    } else {
                        actual.lectura = registros.get(i - 2).lectura;
                    }
                    if (actual.lectura == null) {
                        actual.lectura = 0.0D;
                    }
                }
            }
            if (i == 0) {
                actual.delta = 0.0D;
            } else {
                Double lecanterior = registros.get(i - 1).lectura;
                if (lecanterior > actual.lectura) {
                    actual.delta = 0.0D;
                } else {
                    actual.delta = actual.lectura - lecanterior;
                }

            }

            registros.set(i, actual);
        }

        LinkedList<Dia> dias = new LinkedList<>();
        String fechaActual = "";
        Double consumoDia = 0.0D;
        int last = 0;
        Double lecturaini = 0.0D;
        Double lecturafin = 0.0D;
        boolean inidia = false;
        for (int i = 0; i < registros.size(); i++) {

            if (!registros.get(i).timestamp.substring(0, 10).equals(fechaActual)) {

                if (!fechaActual.equals("")) {
                    Dia d = new Dia();
                    d.consumo = consumoDia;
                    d.fecha = fechaActual;
                    d.numremarcador = registros.get(i - 1).numremarcador;
                    d.esmanual = registros.get(i - 1).esmanual;
                    d.lecturafinal = registros.get(i - 1).lectura;
                    d.lecturainicial = lecturaini;
                    dias.add(d);
                }
                consumoDia = 0.0D;
                fechaActual = registros.get(i).timestamp.substring(0, 10);
                consumoDia += registros.get(i).delta;

                lecturaini = registros.get(i).lectura;
            } else {
                consumoDia += registros.get(i).delta;
            }
            //System.out.println(registros.get(i).numremarcador + ";" + registros.get(i).timestamp + ";" + registros.get(i).lectura + ";" + registros.get(i).lecturaManual + ";" + registros.get(i).delta + ";" + registros.get(i).esmanual);
            last = i;
        }
        Dia d = new Dia();
        d.consumo = consumoDia;
        d.fecha = fechaActual;
        d.numremarcador = registros.get(last).numremarcador;
        d.esmanual = registros.get(last).esmanual;
        d.lecturainicial = lecturaini;
        d.lecturafinal = registros.get(last).lectura;
        dias.add(d);

        //QUITAR LOS ANTERIORES Y POSTERIORES AL MES QUE SE CONSULTA
        LinkedList<Dia> diasSalida = new LinkedList<>();

        for (Dia dia : dias) {
            if (Integer.parseInt(dia.fecha.substring(5, 7)) == mes) {
                diasSalida.add(dia);

            }
        }

        for (Registro r : registros) {
            for (Dia ds : diasSalida) {
                if (r.dia.equals(ds.fecha)) {
                    if (r.esmanual) {
                        ds.esmanual = true;
                    }
                }
                //System.out.println(dia.numremarcador + ";" + dia.fecha + ";" + dia.consumo + ";" + dia.esmanual);
            }
        }

        for (Dia ds : diasSalida) {
            System.out.println(ds.numremarcador + ";" + ds.fecha + ";" + ds.lecturainicial + ";" + ds.lecturafinal + ";" + ds.consumo + ";" + ds.esmanual);
        }

        JSONArray labels = new JSONArray();
        JSONArray data = new JSONArray();
        //JSONObject dataset = new JSONObject();
        Double consumoTotal = 0.0D;
        for (Dia dia : diasSalida) {
            labels.put(dia.fecha);
            data.put(dia.consumo);
            consumoTotal += dia.consumo;
        }
        JSONObject resumen = new JSONObject();

        resumen.put("numremarcador", diasSalida.get(0).numremarcador);
        resumen.put("lecturaini", diasSalida.get(0).lecturainicial);
        resumen.put("lecturainimanual", diasSalida.get(0).esmanual);
        resumen.put("lecturafin", diasSalida.get(diasSalida.size() - 1).lecturafinal);
        resumen.put("lecturafinmanual", diasSalida.get(diasSalida.size() - 1).esmanual);
        resumen.put("consumototalmes", consumoTotal);
        dataset.put("resumen", resumen);

        dataset.put("data", data);
        dataset.put("labels", labels);

        dataset.put("borderWidth", "2");
        dataset.put("label", "Remarcador ID: " + numremarcador);
        //System.out.println(dataset);
        return dataset;
    }

    public static JSONObject getDatasetPotenciasMesRemarcadorNumremarcador(Integer numremarcador, Integer anio, Integer mes) {
        LinkedList<RegistroPotencia> registros = new LinkedList<>();
        JSONObject dataset = new JSONObject();

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
                    rp = new RegistroPotencia(rs.getString("FECHA"), rs.getInt("NUMREMARCADOR"), rs.getString("TIMESTAMP"), rs.getDouble("POTENCIA"), rs.getInt("ANIO"), rs.getInt("MES"), rs.getInt("DIA"), rs.getInt("HH"), rs.getInt("MM"), rs.getInt("SS"));
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
        Double consumoDia = 0.0D;

        Double demmax = 0.0D;
        Double demmaxhp = 0.0D;
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
                demmax = 0.0D;
                demmaxhp = 0.0D;
                fechaActual = registros.get(i).timestamp.substring(0, 10);
                if (registros.get(i).hh == null) {
                    demmax = 0.0D;
                    demmaxhp = 0.0D;
                } else {
                    if (registros.get(i).hh >= 18 && registros.get(i).hh <= 23) {
                        if (demmaxhp <= registros.get(i).potencia) {
                            demmaxhp = registros.get(i).potencia;
                        }
                    } else {
                        if (demmax <= registros.get(i).potencia) {
                            demmax = registros.get(i).potencia;
                        }
                    }
                }

            } else {
                if (registros.get(i).hh == null) {
                    demmax = 0.0D;
                    demmaxhp = 0.0D;
                } else {
                    if (registros.get(i).hh >= 18 && registros.get(i).hh <= 23) {
                        if (demmaxhp <= registros.get(i).potencia) {
                            demmaxhp = registros.get(i).potencia;
                        }
                    } else {
                        if (demmax <= registros.get(i).potencia) {
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

        Double maxdemandaleida = 0.0D;
        Double maxdemandahp = 0.0D;

        for (DiaPotencia dia : diasSalida) {
            labels.put(dia.fecha);
            datademanda.put(dia.demmax);
            if (maxdemandaleida < dia.demmax) {
                maxdemandaleida = dia.demmax;
            }
            datademandahp.put(dia.demmaxhpunta);
            if(maxdemandahp < dia.demmaxhpunta){
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
                    rp = new RegistroPotencia(rs.getString("FECHA"), rs.getInt("NUMREMARCADOR"), rs.getString("TIMESTAMP"), rs.getDouble("POTENCIA"), rs.getInt("ANIO"), rs.getInt("MES"), rs.getInt("DIA"), rs.getInt("HH"), rs.getInt("MM"), rs.getInt("SS"));
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
        Double consumoDia = 0.0D;

        Double demmax = 0.0D;
        Double demmaxhp = 0.0D;
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
                demmax = 0.0D;
                demmaxhp = 0.0D;
                fechaActual = registros.get(i).timestamp.substring(0, 10);
                if (registros.get(i).hh == null) {
                    demmax = 0.0D;
                    demmaxhp = 0.0D;
                } else {
                    if (registros.get(i).hh >= 18 && registros.get(i).hh <= 23) {
                        if (demmaxhp <= registros.get(i).potencia) {
                            demmaxhp = registros.get(i).potencia;
                        }
                    } else {
                        if (demmax <= registros.get(i).potencia) {
                            demmax = registros.get(i).potencia;
                        }
                    }
                }

            } else {
                if (registros.get(i).hh == null) {
                    demmax = 0.0D;
                    demmaxhp = 0.0D;
                } else {
                    if (registros.get(i).hh >= 18 && registros.get(i).hh <= 23) {
                        if (demmaxhp <= registros.get(i).potencia) {
                            demmaxhp = registros.get(i).potencia;
                        }
                    } else {
                        if (demmax <= registros.get(i).potencia) {
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

    static class Registro {

        public String dia;
        public Integer numremarcador;
        public String timestamp;
        public Double lectura;
        public Double lecturaManual;
        public Double delta;
        public Double proyeccion;
        public boolean esmanual = false;

        public Registro(String dia, Integer numremarcador, String timestamp, Double lectura) {
            this.dia = dia;
            this.numremarcador = numremarcador;
            this.timestamp = timestamp;
            this.lectura = lectura;
        }

        public Registro() {

        }
    }

    static class RegistroPotencia {

        public String fecha;
        public Integer numremarcador;
        public String timestamp;
        public Double potencia;

        public Integer anio, mes, dia, hh, mm, ss;

        public RegistroPotencia(String fecha, Integer numremarcador, String timestamp, Double potencia, Integer anio, Integer mes, Integer dia, Integer hh, Integer mm, Integer ss) {
            this.fecha = fecha;
            this.numremarcador = numremarcador;
            this.timestamp = timestamp;
            this.potencia = potencia;

            this.anio = anio;
            this.mes = mes;
            this.dia = dia;
            this.hh = hh;
            this.mm = mm;
            this.ss = ss;
        }

        public RegistroPotencia() {

        }
    }

    static class Dia {

        public String fecha;
        public Integer numremarcador;
        public Double consumo;
        public Double lecturainicial;
        public Double lecturafinal;
        public boolean esmanual = false;
    }

    static class DiaPotencia {

        public String fecha;
        public Integer numremarcador;
        public Double demmax;
        public Double demmaxhpunta;
    }

}
