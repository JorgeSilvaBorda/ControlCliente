ID_REMARCADOR_EDICION = null;

function getRemarcadores() {
    var datos = {
        tipo: 'get-remarcadores'
    };
    $.ajax({
        url: 'RemarcadorController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (resp) {
            var obj = JSON.parse(resp);
            if (obj.estado === 'ok') {
                $('.dataTable').DataTable().destroy();
                $('#tabla-remarcadores tbody').html(obj.tabla);
                $('#tabla-remarcadores').DataTable(OPCIONES_DATATABLES);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function getSelectEmpalme() {
    var datos = {
        tipo: 'get-select-empalme'
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
                $('#select-empalme').html(obj.options);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function getSelectParqueEmpalme(idempalme) {
    var datos = {
        tipo: 'get-select-parque-empalme',
        idempalme: idempalme
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
                $('#select-parque').html(obj.options);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function insRemarcador(callback) {
    var idempalme = $('#select-empalme').val();
    var idparque = $('#select-parque').val();
    var numremarcador = $('#num-remarcador').val();

    var datos = {
        tipo: 'ins-remarcador',
        idempalme: idempalme,
        idparque: idparque,
        numremarcador: numremarcador
    };

    $.ajax({
        url: 'RemarcadorController',
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
    var idremarcador = $(fila).children(0).children(0).val();

    var datos = {
        tipo: 'get-remarcador-idremarcador',
        idremarcador: idremarcador
    };

    $.ajax({
        url: 'RemarcadorController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (res) {
            var obj = JSON.parse(res);
            console.log(obj);
            if (obj.estado === 'ok') {
                armarRemarcador(obj.remarcador);
                $('#select-parque').html(obj.remarcador.idparque);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function saveRemarcador(callback) {
    var idremarcador = ID_REMARCADOR_EDICION;
    var idempalme = $('#select-empalme').val();
    var idparque = $('#select-parque').val();
    var numremarcador = $('#num-remarcador').val();

    var datos = {
        tipo: 'upd-remarcador',
        remarcador: {
            idremarcador: idremarcador,
            idempalme: idempalme,
            idparque: idparque,
            numremarcador: numremarcador
        }
    };

    $.ajax({
        url: 'RemarcadorController',
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

function armarRemarcador(remarcador) {
    ID_REMARCADOR_EDICION = remarcador.idremarcador;
    $('#num-remarcador').val(remarcador.numremarcador);
    $('#select-empalme').val(remarcador.idempalme);
    $('#select-empalme').change();
    $('#btn-insert').attr("hidden", "hidden");
    $('#btn-guardar').removeAttr("hidden");
}

function limpiar() {
    ID_REMARCADOR_EDICION = null;
    $('#num-remarcador').val('');
    $('#select-empalme').val('0');
    $('#select-parque').html('');
    $('#btn-guardar').attr("hidden", "hidden");
    $('#btn-insert').removeAttr("hidden");
}