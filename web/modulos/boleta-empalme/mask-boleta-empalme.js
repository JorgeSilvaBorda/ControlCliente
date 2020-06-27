var REMCLI = null;
var DATOS_BOLETA = {};
var BOLETA = {};
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
                $('#nom-cliente').html(remcli.razoncliente);
                $('#direccion').html(remcli.direccion);
                $('#persona').html(remcli.persona);
                $('#fono').html(remcli.fono);
                $('#email').html(remcli.email);
                $('#num-empalme-boleta').html(remcli.numempalme + '<br /><br />');

                var fechaemision = new Date();
                DATOS_BOLETA.fechaemision = formatFechaYYYYMMDD(fechaemision);
                $('#fecha-emision').html(formatFechaDDMMYYYY(formatFechaYYYYMMDD(fechaemision)) + '<br /><br />');

                var dt = new Date(MES + '-1');
                var nextfecha = new Date(dt.setMonth(dt.getMonth() + 1));
                nextfecha.setMonth(nextfecha.getMonth() + 1);
                nextfecha.setDate(nextfecha.getDate() - 1);
                DATOS_BOLETA.nextfecha = formatFechaYYYYMMDD(nextfecha);
                nextfecha = formatFechaDDMMYYYY(formatFechaYYYYMMDD(nextfecha));
                $('#fecha-prox-lectura').html(nextfecha + '<br /><br />');

                var fechadesde = new Date(FECHA_LECTURA_INICIAL);
                fechadesde.setDate(fechadesde.getDate() + 2);
                var fechahasta = new Date(FECHA_LECTURA_FINAL);
                fechahasta.setDate(fechahasta.getDate() + 2);

                $('#desde').html(formatFechaDDMMYYYY(formatFechaYYYYMMDD(fechadesde)) + '<br /><br />');
                DATOS_BOLETA.fechadesde = formatFechaYYYYMMDD(fechadesde);
                $('#hasta').html(formatFechaDDMMYYYY(formatFechaYYYYMMDD(fechahasta)) + '<br /><br />');
                DATOS_BOLETA.fechahasta = formatFechaYYYYMMDD(fechahasta);
                $('#suministradas').html(remcli.dmps + '<br /><br />');
                $('#horas-punta').html(remcli.dmplhp + '<br /><br />');
                $('#leidas-suministradas').html(MAX_DEMANDA_LEIDA + '<br /><br />');
                $('#leidas-horas-punta').html(MAX_DEMANDA_HORA_PUNTA + '<br /><br />');
                $('#consumo-total-kwh').html(formatMiles(CONSUMO) + '<br /><br />');
                $('#nomred').html(remcli.nomred + '<br /><br />');
                $('#num-serie').html(NUMSERIE + '<br /><br />');
                $('#num-remarcador-boleta').html(NUMREMARCADOR + '<br /><br />');

                DATOS_BOLETA.maxdemandaleida = MAX_DEMANDA_LEIDA;
                DATOS_BOLETA.maxdemandahorapuntaleida = MAX_DEMANDA_HORA_PUNTA;
                DATOS_BOLETA.maxdemandafacturada = remcli.dmps;
                DATOS_BOLETA.maxdemandahorapuntafacturada = remcli.dmplhp;
                DATOS_BOLETA.lecturaactual = LECTURA_ACTUAL;
                DATOS_BOLETA.lecturaanterior = LECTURA_ANTERIOR;
                console.log(DATOS_BOLETA);
                armarDetalleTarifa();
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
    // var consumo = CONSUMO;
    var datos = {
        tipo: 'get-detalle-tarifa-id-red-consumo-remarcador',
        idtarifa: idtarifa,
        idred: idred,
        consumo: CONSUMO,
        numremarcador: numremarcador
    };
    $('#tarifa').html($('#select-tarifa option:selected').html() + '<br /><br />');
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
                BOLETA = obj.boleta;
                BOLETA.maxdemandaleida = DATOS_BOLETA.maxdemandaleida;
                BOLETA.maxdemandahorapuntaleida = DATOS_BOLETA.maxdemandahorapuntaleida;
                BOLETA.maxdemandafacturada = DATOS_BOLETA.maxdemandafacturada;
                BOLETA.maxdemandahorapuntafacturada = DATOS_BOLETA.maxdemandahorapuntafacturada;
                BOLETA.lecturaactual = DATOS_BOLETA.lecturaactual;
                BOLETA.lecturaanterior = DATOS_BOLETA.lecturaanterior;
                BOLETA.fechaemision = DATOS_BOLETA.fechaemision;
                BOLETA.nextfecha = DATOS_BOLETA.nextfecha;
                BOLETA.fechadesde = DATOS_BOLETA.fechadesde;
                BOLETA.fechahasta = DATOS_BOLETA.fechahasta;
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function generar() {
    var datos = {
        tipo: 'genera-boleta',
        remcli: REMCLI,
        boleta: BOLETA
    };
    $.ajax({
        url: 'BoletaController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (res) {
            var obj = JSON.parse(res);
            if (obj.estado === 'ok') {
                
                
                //Generar PDF -----------------------------------------------------
                const element = document.getElementById("modal-body");
                html2pdf().from(element).save("Detalle-" + REMCLI.nomcliente + "-" + REMCLI.numremarcador + $('#mes').val().split("-")[1] + "-" + $('#mes').val().split("-")[0] + ".pdf");
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });

}