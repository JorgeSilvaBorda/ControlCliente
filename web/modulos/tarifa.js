function getTarifas() {
    var datos = {
        tipo: 'get-tarifas'
    };
    $.ajax({
        url: 'TarifaController',
        type: 'post',
        data:{
            datos: JSON.stringify(datos)
        },
        success: function(resp){
            var obj = JSON.parse(resp);
            if(obj.estado === 'ok'){
                $('#tabla-tarifas tbody').html(obj.tabla);
            }
        },
        error: function(a, b, c){
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function insTarifa(callback){
    var nomtarifa = $('#nom-tarifa').val();
    var valortarifa = $('#valor-tarifa').val();
    
    var datos = {
        tipo: 'ins-tarifa',
        nomtarifa: nomtarifa, 
        valortarifa: valortarifa
    };

    $.ajax({
        url: 'TarifaController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function(res){
            var obj = JSON.parse(res);
            if(obj.estado === 'ok'){
                limpiar();
                callback();
            }
        },
        error: function(a, b, c){
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function limpiar(){
    $('#nom-tarifa').val('');
    $('#valor-tarifa').val('');
}