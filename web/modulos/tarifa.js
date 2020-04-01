var ID_TARIFA_EDICION = null;
function getTarifas() {
    var datos = {
        tipo: 'get-tarifas'
    };
    $.ajax({
        url: 'TarifaController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (resp) {
            var obj = JSON.parse(resp);
            if (obj.estado === 'ok') {
                $('#tabla-tarifas tbody').html(obj.tabla);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function insTarifa(callback) {
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

function activarEdicion(boton) {
    var fila = $(boton).parent().parent();
    var idtarifa = $(fila).children(0).children(0).val();

    var datos = {
        tipo: 'get-tarifa-idtarifa',
        idtarifa: idtarifa
    };

    $.ajax({
        url: 'TarifaController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (res) {
            var obj = JSON.parse(res);
            if (obj.estado === 'ok') {
                armarTarifa(obj.tarifa);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function armarTarifa(tarifa) {
    ID_TARIFA_EDICION = tarifa.idtarifa;
    $('#nom-tarifa').val(tarifa.nomtarifa);
    $('#valor-tarifa').val(tarifa.valortarifa);

    $('#btn-insert').attr("hidden", "hidden");
    $('#btn-guardar').removeAttr("hidden");
}

function saveTarifa(callback) {
    var nomtarifa = $('#nom-tarifa').val();
    var valortarifa = $('#valor-tarifa').val();

    var datos = {
        tipo: 'upd-tarifa',
        tarifa: {
            idtarifa: ID_TARIFA_EDICION,
            nomtarifa: nomtarifa,
            valortarifa: valortarifa
        }
    };

    $.ajax({
        url: 'TarifaController',
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

function limpiar() {
    ID_TARIFA_EDICION = null;
    $('#nom-tarifa').val('');
    $('#valor-tarifa').val('');
    $('#btn-guardar').attr("hidden", "hidden");
    $('#btn-insert').removeAttr("hidden");
}