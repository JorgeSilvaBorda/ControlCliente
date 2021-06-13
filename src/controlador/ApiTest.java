package controlador;

import clases.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.ConsumoMes;
import modelo.lectura.Dia;
import modelo.lectura.Registro;

public class ApiTest extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.addHeader("Content-Encoding", "application/json");
        System.out.println("Entra al ApiTest");
        PrintWriter out = response.getWriter();
        JsonReader jsonreader = new JsonReader(request.getReader());
        Object gson = new Gson().fromJson(jsonreader, Map.class);
        String jsonstring = new Gson().toJson(gson);
        
        //String jsonsalida = new Gson().fromJson(jsonreader, Map.class);
        //new Gson().toJson(jsonsalida, response.getWriter());
        //System.out.println(request.getParameter("data"));
        //System.out.println(jsonstring);
        
        JSONObject entrada = new JSONObject(jsonstring);
        //porMes(entrada);
        //porDia(entrada);
        porRegistro(entrada);
        
    }

    public void porMes(JSONObject entrada){
        LinkedList<Dia> dias = modelo.LecturaController.getDiasDesdeHastaRemarcadorNumremarcador(entrada.getInt("numremarcador"), entrada.getString("fechaini"), entrada.getString("fechafin"));
        
        LinkedList<ConsumoMes> meses = new LinkedList();
        String mesanterior = "";
        
        ConsumoMes cm = new ConsumoMes();
        Dia anterior = new Dia();
        for(Dia d : dias){
            //System.out.println(d.fecha + ";" + d.lecturainicial + ";" + d.lecturafinal + ";" + d.consumo);
            
            //System.out.println(d.fecha.substring(0, 7));
            if(!d.fecha.substring(0, 7).equals(mesanterior)){//Es un nuevo mes
                if(mesanterior.equals("")){//Es el primer mes
                    cm = new ConsumoMes();
                    cm.anio = Integer.parseInt(d.fecha.substring(0, 4));
                    cm.mes = Integer.parseInt(d.fecha.substring(5, 7));
                    cm.consumo = 0;
                    cm.consumo = cm.consumo + d.consumo;
                    cm.lecturaini = d.lecturainicial;
                }else{//Es un cambio de mes
                    cm.lecturafin = anterior.lecturafinal;
                    cm.consumo = cm.consumo + anterior.consumo;
                    meses.add(cm);
                    
                    cm = new ConsumoMes();
                    cm.anio = Integer.parseInt(d.fecha.substring(0, 4));
                    cm.mes = Integer.parseInt(d.fecha.substring(5, 7));
                    cm.consumo = 0;
                    cm.consumo = cm.consumo + d.consumo;
                    cm.lecturaini = d.lecturainicial;
                }
            }else{//Es el mismo mes
                cm.consumo = cm.consumo + d.consumo;
            }
            anterior = d;
            mesanterior = d.fecha.substring(0, 7);
        }
        
        cm.lecturafin = anterior.lecturafinal;
        meses.add(cm);
        
        for(ConsumoMes con : meses){
            System.out.println(con.anio + ";" + con.mes + ";" + con.lecturaini + ";" + con.lecturafin + ";" + con.consumo);
        }
        
        try{
            //defecto(request);
        }catch (Exception ex) {
            System.out.println("Error en la lectura de la pedida");
            System.out.println(ex);
        }
    }
    
    public void porDia(JSONObject entrada){
        LinkedList<Dia> dias = modelo.LecturaController.getDiasDesdeHastaRemarcadorNumremarcador(entrada.getInt("numremarcador"), entrada.getString("fechaini"), entrada.getString("fechafin"));
        
        LinkedList<ConsumoMes> meses = new LinkedList();
        String mesanterior = "";
        
        ConsumoMes cm = new ConsumoMes();
        Dia anterior = new Dia();
        for(Dia d : dias){
            System.out.println(d.fecha + ";" + d.lecturainicial + ";" + d.lecturafinal + ";" + d.consumo);
            
        }
    }
    
    public void porRegistro(JSONObject entrada){
        LinkedList<Registro> registros = modelo.LecturaController.getRegistrosDesdeHastaRemarcadorNumremarcador(entrada.getInt("numremarcador"), entrada.getString("fechaini"), entrada.getString("fechafin"));
        for(Registro r : registros){
            System.out.println(r.toString());
        }
        
    }
}
