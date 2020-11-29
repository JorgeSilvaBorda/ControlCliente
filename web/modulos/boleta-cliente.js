var IDCOMUNA = null;
var KWTOTAL = null;
function getSelectClientes() {
    var datos = {
        tipo: 'get-select-clientes-nombre'
    };

    $.ajax({
        url: 'ClienteController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (resp) {
            var obj = JSON.parse(resp);
            if (obj.estado === 'ok') {
                $('#select-cliente').html(obj.options);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function getSelectInstalacionesCliente(idcliente) {
    $('#select-remarcador').html("");
    var datos = {
        tipo: 'get-select-instalaciones-idcliente-individual',
        idcliente: idcliente
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

function getSelectRemarcadoresIdInstalacion(idinstalacion) {
    var idcliente = $('#select-cliente').val();
    var datos = {
        tipo: 'get-select-remarcadores-idinstalacion-idcliente',
        idinstalacion: idinstalacion,
        idcliente: idcliente
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
                $('#select-remarcador').html(obj.options);
                getSelectTarifasIdInstalacion(idinstalacion);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function getSelectTarifasIdInstalacion(idinstalacion) {
    var datos = {
        idinstalacion: idinstalacion,
        tipo: "get-select-tarifas-idinstalacion"
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

function validarCampos() {
    var idcliente = $('#select-cliente').val();
    var idinstalacion = $('#select-instalacion').val();
    var idremarcador = $('#select-remarcador').val();
    var desde = $('#fecha-desde').val();
    var hasta = $('#fecha-hasta').val();
    var idtarifa = $('#select-tarifa').val();

    if (idcliente === 0 || idcliente === '0' || idcliente === '') {
        alert("Debe seleccionar un cliente del listado.");
        return false;
    }
    if (idinstalacion === 0 || idinstalacion === '0' || idinstalacion === '') {
        alert("Debe seleccionar una instalación del listado.");
        return false;
    }
    if (idremarcador === 0 || idremarcador === '0' || idremarcador === '') {
        alert("Debe seleccionar un remarcador del listado.");
        return false;
    }
    if (idtarifa === 0 || idtarifa === '0' || idtarifa === '') {
        alert("Debe seleccionar una tarifa del listado.");
        return false;
    }
    if (desde === 0 || desde === '0' || desde === '') {
        alert("Debe indicar una fecha desde cuando inicia el cobro.");
        return false;
    }
    if (hasta === 0 || hasta === '0' || hasta === '') {
        alert("Debe indicar una fecha hasta cuando termina el cobro.");
        return false;
    }
    var diferencia = diffFechaDias(desde, hasta);
    if (diferencia < 1) {
        alert("La fecha desde no puede ser superior a la fecha hasta.");
        return false;
    }
    if (diferencia > 31) {
        alert("El rango de fechas no puede ser superior a un mes.");
        return false;
    }
    return true;
}

function buscar() {
    
    if (validarCampos()) {
        $('#detalle-remarcadores').html("");
        var idcliente = $('#select-cliente').val();
        var idinstalacion = $('#select-instalacion').val();
        var idremarcador = $('#select-remarcador').val();
        var numremarcador = $('#select-remarcador option:selected').text();
        var numempalme = $('#select-empalme option:selected').text();
        var desde = $('#fecha-desde').val();
        var hasta = $('#fecha-hasta').val();
        var idtarifa = $('#select-tarifa').val();
        var datos = {
            tipo: 'get-remarcador-un-cliente-boleta',
            idcliente: idcliente,
            idinstalacion: idinstalacion,
            idremarcador: idremarcador,
            numremarcador: numremarcador,
            numempalme: numempalme,
            desde: desde,
            hasta: hasta,
            idtarifa: idtarifa
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
                    $('#detalle-remarcadores').html(obj.tabla);
                    IDCOMUNA = obj.idcomuna;
                    KWTOTAL = obj.kwtotal;
                    $('.loader').fadeOut(500);
                }
            },
            error: function (a, b, c) {
                console.log(a);
                console.log(b);
                console.log(c);
            }
        });
    }
}

function calcular(idremarcador, numremarcador, numserie, consumo, desde, hasta, lecturaanterior, lecturaactual, maxdemandaleida, maxdemandahorapunta, fechalecturaini, fechalecturafin){
    var idtarifa = $('#select-tarifa').val();
    if(idtarifa === '0' || idtarifa === 0 || idtarifa === '' || idtarifa === null || idtarifa === undefined){
        alert("Debe seleccionar una tarifa para poder calcular la boleta.");
        return false;
    }
    $('#btn-imprimir').hide();
    $('#btn-generar').show();
    $('#modal-body').html('');
    $('#modal').modal();
    $('#modal-body').load("modulos/boleta-empalme/mask-boleta-cliente.jsp?idremarcador=" + idremarcador + "&numremarcador=" + numremarcador + "&numserie='" + numserie + "'&consumo=" + consumo + "&desde='" + desde + "'&hasta='" + hasta + "'&lecturaanterior=" + lecturaanterior + "&lecturaactual=" + lecturaactual + "&maxdemandaleida=" + maxdemandaleida + "&maxdemandahorapunta=" + maxdemandahorapunta + "&fechalecturaini='" + fechalecturaini + "'&fechalecturafin='" + fechalecturafin + "'");
}

function calcularDiferencia(){
    var text = $('#consumo-facturado-empalme').val().replaceAll("\\.", "");
    var num = text.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
    $('#consumo-facturado-empalme').val(num);
    var kwtotal = KWTOTAL;
    var facturadoempalme = $('#consumo-facturado-empalme').val().replaceAll("\\.", "");
    var resta = kwtotal - facturadoempalme;
    var porc = 100 - ((facturadoempalme * 100) / kwtotal);
    
    resta = resta * -1;
    porc = porc * -1;
    
    $('#kw-diferencia').text(formatMiles(parseInt(resta)));
    $('#porc-diferencia').text(porc.toFixed(2) + " %");
}

function getLastBoleta(idboleta){
    $('#btn-generar').hide();
    $('#btn-imprimir').show();
    $('#modal-body').html('');
    $('#modal').modal();
    $('#modal-body').load("modulos/boleta-empalme/mask-last-boleta-cliente.jsp?idboleta=" + idboleta);
}

function habilitarSobreescritura(id){
    $('#botones_' + id).show();
    $('#btn_' + id).hide();
}

function deshabilitarSobreescritura(id){
    $('#botones_' + id).hide();
    $('#btn_' + id).show();
}

function limpiar(){
    getSelectClientes();
    $('#select-instalacion').html('');
    $('#select-remarcador').html('');
    $('#fecha-desde').val('');
    $('#fecha-hasta').val('');
    $('#select-tarifa').html('');
    $('#detalle-remarcadores').html('');
    IDCOMUNA = null;
    KWTOTAL = null;
}