ID_PARQUE_EDICION = null;

function getParques() {
    var datos = {
        tipo: 'get-parques'
    };
    $.ajax({
        url: 'ParqueController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (resp) {
            var obj = JSON.parse(resp);
            if (obj.estado === 'ok') {
                $('.dataTable').DataTable().destroy();
                $('#tabla-parques tbody').html(obj.tabla);
                $('#tabla-parques').DataTable(OPCIONES_DATATABLES);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function getSelectInstalacion() {
    var datos = {
        tipo: 'get-select-instalacion'
    };
    $.ajax({
        url: 'InstalacionController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (resp) {
            var obj = JSON.parse(resp);
            if (obj.estado === 'ok') {
                $('#select-instalacion').html(obj.options);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function insParque(callback){
    var idinstalacion = $('#select-instalacion').val();
    var nomparque = $('#nom-parque').val();
    
    var datos = {
        tipo: 'ins-parque',
        idinstalacion: idinstalacion, 
        nomparque: nomparque
    };

    $.ajax({
        url: 'ParqueController',
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
    var idparque = $(fila).children(0).children(0).val();

    var datos = {
        tipo: 'get-parque-idparque',
        idparque: idparque
    };

    $.ajax({
        url: 'ParqueController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (res) {
            var obj = JSON.parse(res);
            console.log(obj);
            if (obj.estado === 'ok') {
                armarParque(obj.parque);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function armarParque(parque) {
    ID_PARQUE_EDICION = parque.idparque;
    $('#select-instalacion').val(parque.idinstalacion);
    $('#nom-parque').val(parque.nomparque);
    $('#btn-insert').attr("hidden", "hidden");
    $('#btn-guardar').removeAttr("hidden");
}

function saveParque(callback) {
    var idparque = ID_PARQUE_EDICION;
    var idinstalacion = $('#select-instalacion').val();
    var nomparque = $('#nom-parque').val();
    
    var datos = {
        tipo: 'upd-parque',
        parque: {
            idparque: idparque,
            idinstalacion: idinstalacion,
            nomparque: nomparque
        }
    };

    $.ajax({
        url: 'ParqueController',
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
    ID_PARQUE_EDICION = null;
    $('#nom-parque').val('');
    $('#select-instalacion').val('0');
    $('#btn-guardar').attr("hidden", "hidden");
    $('#btn-insert').removeAttr("hidden");
}