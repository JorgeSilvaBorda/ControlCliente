var REMARCADORES = null;
var KWTOTAL = null;
var IDCOMUNA = null;
var MASIVO = false;
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

function getRemarcadoresNumEmpalmeBoleta() {
    REMARCADORES = null;
    //Función trabaja sólo con el número de empalme.
    //En BD, el mismo numero de empalme puede tener ID distintos para efectos de asigrnación a las bodegas
    $('#detalle-remarcadores').html("");
    var numempalme = $('#select-empalme option:selected').text();
    var mes = $('#mes').val();
    var datos = {
        tipo: 'get-remarcadores-numempalme-boleta',
        numempalme: numempalme,
        mes: mes
    };
    $.ajax({
        url: 'RemarcadorController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (resp) {
            var obj = JSON.parse(resp);
            console.log(obj);
            if (obj.estado === 'ok') {
                $('.dataTable').DataTable().destroy();
                $('#detalle-remarcadores').html(obj.tabla);
                REMARCADORES = obj.remarcadores;
                KWTOTAL = obj.kwtotal;
                IDCOMUNA = obj.idcomuna;
                $('.loader').fadeOut(500);
            }
        },
        error: function (a, b, c) {
            KWTOTAL = null;
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function getSelectEmpalmesNumEmpalmesInstalacion() {
    var idinstalacion = $('#select-instalacion').val();

    var datos = {
        tipo: 'get-select-empalmes-numempalmes-idinstalacion',
        idinstalacion: idinstalacion
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
                var idinstalacion = $('#select-instalacion').val();
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

function validarCampos() {
    var idinstalacion = $('#select-instalacion').val();
    var idempalme = $('#select-empalme').val();
    var mes = $('#mes').val();

    if (idinstalacion === '0' || idinstalacion === '' || idinstalacion === null || idinstalacion === undefined || idinstalacion === 0) {
        alert("Debe seleccionar una Instalación del listado.");
        return false;
    }
    if (idempalme === '0' || idempalme === '' || idempalme === null || idempalme === undefined || idempalme === 0) {
        alert("Debe seleccionar un Empalme del listado.");
        return false;
    }
    if (mes.length < 6) {
        alert("El mes indicado es incorrecto.");
        return false;
    }
    return true;
}

function calcularDiferencia() {
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

function buscar() {
    if (validarCampos()) {
        $('#btn-buscar').attr("disabled", "disabled");
        $('.loader').fadeIn(500);
        //$('.loader').fadeIn(500);
        getRemarcadoresNumEmpalmeBoleta();
    }
}

function calcular(idremarcador, numremarcador, numserie, consumo, mes, lecturaanterior, lecturaactual, maxdemandaleida, maxdemandahorapunta, fechalecturaini, fechalecturafin) {
    //console.log("modulos/boleta-empalme/mask-boleta-empalme.jsp?idremarcador=" + idremarcador + "&numremarcador=" + numremarcador + "&consumo=" + consumo + "&mes='" + mes + "'&lecturaanterior=" + lecturaanterior + "&lecturaactual=" + lecturaactual + "&fechalecturaini='" + fechalecturaini + "'&fechalecturafin='" + fechalecturafin + "'");
    var idtarifa = $('#select-tarifa').val();
    if (idtarifa === '0' || idtarifa === 0 || idtarifa === '' || idtarifa === null || idtarifa === undefined) {
        alert("Debe seleccionar una tarifa para poder calcular la boleta.");
        return false;
    }
    $('#btn-imprimir').hide();
    $('#btn-generar').show();
    $('#modal-body').html('');
    $('#modal').modal();
    $('#modal-body').load("modulos/boleta-empalme/mask-boleta-empalme.jsp?idremarcador=" + idremarcador + "&numremarcador=" + numremarcador + "&numserie='" + numserie + "'&consumo=" + consumo + "&mes='" + mes + "'&lecturaanterior=" + lecturaanterior + "&lecturaactual=" + lecturaactual + "&maxdemandaleida=" + maxdemandaleida + "&maxdemandahorapunta=" + maxdemandahorapunta + "&fechalecturaini='" + fechalecturaini + "'&fechalecturafin='" + fechalecturafin + "'&masivo=" + MASIVO);
}

function generarTodas() {
    var remarcadores = [];
    var REMARCADORES_MASIVO = [];
    var mes = $('#mes').val();
    $('#tabla-remarcadores-empalme tbody tr').each(function (i) {
        var fila = $(this)[0];
        var celdas = $(fila).children('td');
        if (celdas.length === 11) {
            if ($($(celdas[9]).children('button')[0]).text() === 'Calcular Boleta') {
                remarcadores.push($(celdas[0]).text());
            }
        }
    });
    for (var i = 0; i < remarcadores.length; i++) {
        var buscado = remarcadores[i];
        for (var x = 0; x < REMARCADORES.length; x++) {
            if (parseInt(REMARCADORES[x].numremarcador) === parseInt(buscado)) {
                REMARCADORES_MASIVO.push(REMARCADORES[x]);
            }
        }
    }

    $('#detalle-remarcadores').html('<br /><h4 style="color: grey;">Generando boletas. Por favor espere.</h4><div class="loader"><div class="ldio-sa9px9nknjc"><div></div><div><div></div></div></div></div>');

    var fechaemision = new Date();
    var fechaemisionformato = formatFechaYYYYMMDD(fechaemision);
    var dt = new Date(mes + '-1');
    var nextfecha = new Date(dt.setMonth(dt.getMonth() + 1));
    nextfecha.setMonth(nextfecha.getMonth() + 1);
    nextfecha.setDate(nextfecha.getDate() - 1);
    var nextfechaformato = formatFechaYYYYMMDD(nextfecha);

    var datos = {
        tipo: 'boleta-masiva',
        idtarifa: $('#select-tarifa').val(),
        mesanio: mes,
        idinstalacion: $('#select-instalacion').val(),
        idempalme: $('#select-empalme').val(),
        fechaemision: fechaemisionformato,
        nextfecha: nextfechaformato,
        remarcadores: REMARCADORES_MASIVO
    };
    $.ajax({
        url: 'BoletaController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (resp) {
            var obj = JSON.parse(resp);
            if (obj.estado === 'ok') {
                buscar();
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function imprimirMasivo() {
    var ides = [];
    var stringparameter = "%5B";
    $('#tabla-remarcadores-empalme tbody tr').each(function (i) {
        var fila = $(this)[0];
        var celdas = $(fila).children('td');
        if (celdas.length === 11) {
            if ($($(celdas[9]).children('button')[0]).text() === 'Habilitar') {
                var idboleta = $(celdas[10]).text();
                idboleta = parseInt(idboleta);
                ides.push(idboleta);
                stringparameter += idboleta + "%2C";
            }
        }
    });
    stringparameter += "%5D";
    console.log(stringparameter);
    window.open('modulos/boleta-empalme/mask-boleta-masiva.jsp?jsonboletas=' + stringparameter);
}

function getLastBoleta(idboleta) {
    $('#btn-generar').hide();
    $('#btn-imprimir').show();
    $('#modal-body').html('');
    $('#modal').modal();
    $('#modal-body').load("modulos/boleta-empalme/mask-last-boleta-empalme.jsp?idboleta=" + idboleta);
}

function habilitarSobreescritura(id) {
    $('#botones_' + id).show();
    $('#btn_' + id).hide();
}

function deshabilitarSobreescritura(id) {
    $('#botones_' + id).hide();
    $('#btn_' + id).show();
}

function limpiar() {
    KWTOTAL = null;
    REMARCADORES = null;
}