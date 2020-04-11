ID_EMPALME_EDICION = null;

function getEmpalmes() {
    var datos = {
        tipo: 'get-empalmes'
    };
    $.ajax({
        url: 'EmpalmeController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (resp) {
            var obj = JSON.parse(resp);
            if (obj.estado === 'ok') {
                $('.dataTable').DataTable().destroy();
                $('#tabla-empalmes tbody').html(obj.tabla);
                $('#tabla-empalmes').DataTable(OPCIONES_DATATABLES);
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

function insEmpalme(callback){
    var idinstalacion = $('#select-instalacion').val();
    var numempalme = $('#num-empalme').val();
    
    var datos = {
        tipo: 'ins-empalme',
        idinstalacion: idinstalacion, 
        numempalme: numempalme
    };

    $.ajax({
        url: 'EmpalmeController',
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
    var idempalme = $(fila).children(0).children(0).val();

    var datos = {
        tipo: 'get-empalme-idempalme',
        idempalme: idempalme
    };

    $.ajax({
        url: 'EmpalmeController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (res) {
            var obj = JSON.parse(res);
            if (obj.estado === 'ok') {
                armarEmpalme(obj.empalme);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function armarEmpalme(empalme) {
    ID_EMPALME_EDICION = empalme.idinstalacion;
    $('#select-instalacion').val(empalme.idinstalacion);
    $('#num-empalme').val(empalme.numempalme);
    $('#btn-insert').attr("hidden", "hidden");
    $('#btn-guardar').removeAttr("hidden");
}

function saveEmpalme(callback) {
    var idempalme = ID_EMPALME_EDICION;
    var idinstalacion = $('#select-instalacion').val();
    var numempalme = $('#num-empalme').val();
    
    var datos = {
        tipo: 'upd-empalme',
        empalme: {
            idempalme: idempalme,
            idinstalacion: idinstalacion,
            numempalme: numempalme
        }
    };

    $.ajax({
        url: 'EmpalmeController',
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
    ID_EMPALME_EDICION = null;
    $('#num-empalme').val('');
    $('#select-instalacion').val('0');
    $('#btn-guardar').attr("hidden", "hidden");
    $('#btn-insert').removeAttr("hidden");
}