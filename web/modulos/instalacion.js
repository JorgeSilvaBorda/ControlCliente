function getInstalaciones() {
    var datos = {
        tipo: 'get-instalaciones'
    };
    $.ajax({
        url: 'InstalacionController',
        type: 'post',
        data:{
            datos: JSON.stringify(datos)
        },
        success: function(resp){
            var obj = JSON.parse(resp);
            if(obj.estado === 'ok'){
                $('.dataTable').DataTable().destroy();
                $('#tabla-instalaciones tbody').html(obj.tabla);
                $('#tabla-instalaciones').DataTable(OPCIONES_DATATABLES);
            }
        },
        error: function(a, b, c){
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function insInstalacion(callback){
    var nominstalacion = $('#nom-instalacion').val();
    
    var datos = {
        tipo: 'ins-instalacion',
        nominstalacion: nominstalacion
    };

    $.ajax({
        url: 'InstalacionController',
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
    $('#nom-instalacion').val('');
}