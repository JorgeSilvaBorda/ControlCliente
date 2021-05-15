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

function getRemarcadoresNumEmpalmeBoleta_NEW() {
    $('.loader').fadeIn(500);
    var numempalme = $('#select-empalme').val();
    var aniomes = $('#mes').val();
    var request = new XMLHttpRequest();
    //var request = APIHTTPURL + "/empalme/" + idempalme;

    request.open('GET', APIHTTPURL + "/empalme/" + numempalme + "/remarcadores/lastasignacion", false);  // `false` makes the request synchronous
    request.send(null);

    if (request.status === 200) {
        //console.log(request.responseText);

        var empalme = JSON.parse(request.responseText);
        var tablasalida = "<table style='font-size: 10px;' id='tabla-remarcadores-empalme' class='table table-bordered table-condensed table-sm'>";
        tablasalida += "<caption style='caption-side:top;'><h5>Remarcadores en el Empalme Nº: " + empalme.numempalme + "</h5></caption>";
        tablasalida += "<thead style='text-align: center;' ><tr class='table-info'>";
        tablasalida += "<th># Remarcador</th>";
        tablasalida += "<th>Nº Serie</th>";
        tablasalida += "<th>Bodega</th>";
        tablasalida += "<th>Cliente</th>";
        tablasalida += "<th>Módulos</th>";
        tablasalida += "<th>Instalación</th>";
        tablasalida += "<th>Lectura<br />Anterior</th>";
        tablasalida += "<th>Lectura<br />Final</th>";
        tablasalida += "<th>Consumo (kWh)</th>";
        tablasalida += "<th>Emitir</th>";
        tablasalida += "<th>Última<br />Boleta</th>";
        tablasalida += "</tr></thead><tbody>";
        console.log(tablasalida);
        //console.log(empalme);
        for (var i = 0; i < empalme.remarcadores.length; i++) {
            for (var x = 0; x < empalme.remarcadores[i].asignaciones; x++) {
                if (empalme.remarcadores[i].asignaciones[x].fechaasignacion.indexOf(aniomes) !== -1) {
                    tablasalida += "<tr>";
                    tablasalida += "<td style='text-align: center;' ><input type='hidden' value='" + empalme.remarcadores[i].idremarcador + "' /><span>" + empalme.remarcadores[i].numremarcador + "</span></td>";
                    tablasalida += "<td><input type='hidden' value='" + empalme.remarcadores[i].numserie + "' /><span>" + remarcador.numserie + "</span></td>";
                    tablasalida += "<td><span>" + empalme.remarcadores[i].parque.nomparque + "</span></td>";
                    tablasalida += "<td><span>" + empalme.remarcadores[i].asignaciones[x].cliente.nomcliente + "</span></td>";
                    tablasalida += "<td style='text-align: center;' ><span>" + empalme.remarcadores[i].modulos + "</span></td>";
                    tablasalida += "<td><span>" + empalme.remarcadores[i].instalacion.nominstalacion + "</span></td>";
                    tablasalida += "<td style='text-align: right;'><span></span></td>";
                    tablasalida += "<td style='text-align: right;'><span></span></td>";
                    tablasalida += "<td style='text-align: right;'><span></span></td>";
                    tablasalida += "<td></td>";
                    tablasalida += "<td></td>";
                    tablasalida += "</tr>";

                }
            }
        }
        tablasalida += "<tr class='table-info'>";
        tablasalida += "<td colspan='8' style='text-align: right; padding-right:5px; font-weight: bold;'>Consumo Total Remarcadores(KW): </td>";
        tablasalida += "<td style='font-weight: bold; text-align:right;' ></td>";
        //if (boletasnoemitidas > 2) {
        tablasalida += "<tr><td colspan='2' style='border: 1px solid white; background-color: white; text-align: center;'>"
                + "<button type='button' onclick='generarTodas();' style='padding: 0px 2px 0px 2px; height: 1.5em;' class='btn btn-sm btn-outline-primary'>Generar Todas</button></td>";
        tablasalida += "</tr>";
        //}

        tablasalida += "<tr>";
        tablasalida += "<td colspan='8' style='vertical-align: middle; text-align: right; padding-right:5px; font-weight: bold;'>Consumo Facturado del Empalme: " + empalme.numempalme + "</td>";
        tablasalida += "<td><input type='text' onkeyup='calcularDiferencia();' class='form-control form-control-sm small' style='font-size: 0.9em; padding-top: 0px; padding-bottom: 0px; width: 12em; text-align: right;' id='consumo-facturado-empalme'/></td>";
        tablasalida += "</tr>";

        tablasalida += "<tr>";
        tablasalida += "<td colspan='8' style='text-align: right; padding-right:5px; font-weight: bold;'>KW Diferencia: </td>";
        tablasalida += "<td style='text-align: right;' ><span id='kw-diferencia'></span></td>";
        tablasalida += "</tr>";

        tablasalida += "<tr>";
        tablasalida += "<td colspan='8' style='text-align: right; padding-right:5px; font-weight: bold;'>% Diferencia: </td>";
        tablasalida += "<td style='text-align: right;'><span id='porc-diferencia' ></span></td>";
        tablasalida += "</tr>";

        //tablasalida += filas;
        tablasalida += "</tbody></table>";
        $('.dataTable').DataTable().destroy();
        $('#detalle-remarcadores').html(tablasalida);
        $('.loader').fadeOut(500);
        
        console.log(empalme);
    }
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