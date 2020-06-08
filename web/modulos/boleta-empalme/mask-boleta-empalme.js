var REMCLI = null;

function getPreBoletaRemarcador(idremarcador, consumo, fechaini, fechafin, leacturaanterior, lecturaactual) {

}

function getSelectTarifasIdComuna(idcomuna) {
    var datos = {
        idcomuna: idcomuna,
        tipo: "get-select-tarifas-idcomuna"
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
                $('#select-tarifa').html(obj.options);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function getRemarcadorClienteIdRemarcador(idremarcador) {
    $('#rut-cliente').html('');
    $('#nom-cliente').html('');
    $('#direccion').html('');
    $('#persona').html('');
    $('#fono').html('');
    $('#email').html('');

    var datos = {
        tipo: 'get-remarcador-cliente-idremarcador',
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
            if (obj.estado === 'ok') {
                REMCLI = obj.remarcador;
                var remcli = obj.remarcador;
                $('#rut-cliente').html($.formatRut(remcli.rutcliente + "-" + remcli.dvcliente));
                $('#nom-cliente').html(remcli.nomcliente);
                $('#direccion').html(remcli.direccion);
                $('#persona').html(remcli.persona);
                $('#fono').html(remcli.fono);
                $('#email').html(remcli.email);
                getSelectTarifasIdComuna(remcli.idcomuna);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function armarDetalleTarifa() {
    var idtarifa = $('#select-tarifa').val();
    var idred = REMCLI.idred;
    var numremarcador = REMCLI.numremarcador;
    var consumo = CONSUMO;
    var datos = {
        tipo: 'get-detalle-tarifa-id-red-consumo-remarcador',
        idtarifa: idtarifa,
        idred: idred,
        consumo: consumo,
        numremarcador: numremarcador,
        fechafin: fechafin
    };
    console.log(datos);
    $.ajax({
        url: 'BoletaController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (res) {
            var obj = JSON.parse(res);
            if (obj.estado === 'ok') {
                $('#detalle-tarifa-remarcador').html(obj.tabla);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}