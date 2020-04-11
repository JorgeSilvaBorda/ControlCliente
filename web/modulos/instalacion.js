ID_INSTALACION_EDICION = null;

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

function activarEdicion(boton) {
    var fila = $(boton).parent().parent();
    var idinstalacion = $(fila).children(0).children(0).val();

    var datos = {
        tipo: 'get-instalacion-idinstalacion',
        idinstalacion: idinstalacion
    };

    $.ajax({
        url: 'InstalacionController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (res) {
            var obj = JSON.parse(res);
            if (obj.estado === 'ok') {
                armarInstalacion(obj.instalacion);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function armarInstalacion(instalacion) {
    ID_INSTALACION_EDICION = instalacion.idinstalacion;
    //$('#select-plogistico').val(instalacion.idplogistico);
    $('#nom-instalacion').val(instalacion.nominstalacion);
    $('#btn-insert').attr("hidden", "hidden");
    $('#btn-guardar').removeAttr("hidden");
}

function saveInstalacion(callback) {
    var idinstalacion = ID_INSTALACION_EDICION;
    var idplogistico = $('#select-plogistico').val();
    var nominstalacion = $('#nom-instalacion').val();
    
    var datos = {
        tipo: 'upd-instalacion',
        instalacion: {
            idinstalacion: idinstalacion,
            idplogistico: idplogistico,
            nominstalacion: nominstalacion
        }
    };

    $.ajax({
        url: 'InstalacionController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (res) {
            var obj = JSON.parse(res);
            if (obj.estado === 'ok') {
                limpiar();
                callback();
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function limpiar(){
    ID_INSTALACION_EDICION = null;
    $('#nom-instalacion').val('');
    //$('#select-plogistico').val('0');
    $('#btn-guardar').attr("hidden", "hidden");
    $('#btn-insert').removeAttr("hidden");
}