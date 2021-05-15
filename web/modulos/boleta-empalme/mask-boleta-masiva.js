var BOLETAS = [];
function buscarBoletasMasivo(ides){
    var datos = {
        tipo: 'buscar-boletas-masivo',
        ides: ides
    };
    $.ajax({
        url: '/ControlCliente/BoletaController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function(resp){
            var obj = JSON.parse(resp);
            if(obj.estado === 'ok'){
                console.log(obj.boletas);
                BOLETAS = obj.boletas;
                pintarBoletas(obj.boletas);
            }
        },
        error: function(a, b, c){
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function pintarBoletas(boletas){
    for (var i = 0; i < boletas.length; i++){
        var row = document.createElement("div");
        $(row).attr("class", "row");
        var col = document.createElement("div");
        $(col).attr("class", "col-sm-12");
        $(col).css("position", "relative");
        var div = document.createElement("div");
        $(div).attr("id", "boleta_" + boletas[i].boleta.IDBOLETA);
        var mes = boletas[i].boleta.FECHALECTURAACTUAL.split("-")[0] + "-" + boletas[i].boleta.FECHALECTURAACTUAL.split("-")[1];
        $(div).load("mask-boleta-empalme-masivo.jsp?idremarcador=" + boletas[i].boleta.IDREMARCADOR + "&numremarcador=" + boletas[i].boleta.NUMREMARCADOR + "&numserie='" + boletas[i].boleta.NUMSERIE + "'&consumo=" + boletas[i].boleta.CONSUMO + "&mes='" + mes + "'&lecturaanterior=" + boletas[i].boleta.LECTURAANTERIOR + "&lecturaactual=" + boletas[i].boleta.LECTURAACTUAL + "&maxdemandaleida=" + boletas[i].boleta.DEM_MAX_SUMINISTRADA_FACTURADA + "&maxdemandahorapunta=" + boletas[i].boleta.DEM_MAX_HORA_PUNTA_FACTURADA + "&fechalecturaini='" + boletas[i].boleta.FECHALECTURAANTERIOR + "'&fechalecturafin='" + boletas[i].boleta.FECHALECTURAACTUAL + "'&masivo=false&idtarifa=" + boletas[i].boleta.IDTARIFA);
        col.appendChild(div);
        row.appendChild(col);
        document.getElementById("cuerpo").appendChild(row);
    }
}