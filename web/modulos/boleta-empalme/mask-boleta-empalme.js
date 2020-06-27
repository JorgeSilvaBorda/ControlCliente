var REMCLI = null;

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
                //getSelectTarifasIdComuna(remcli.idcomuna);

                //$('#num-cliente').html($.formatRut(remcli.rutcliente + "-" + remcli.dvcliente) + '<br /><br />');
                var fechaemision = new Date();
                $('#fecha-emision').html(formatFechaDDMMYYYY(formatFechaYYYYMMDD(fechaemision)) + '<br /><br />');
                var dt = new Date(MES + '-1');
                var nextfecha = new Date(dt.setMonth(dt.getMonth() + 1));
                nextfecha.setMonth(nextfecha.getMonth() + 1);
                nextfecha.setDate(nextfecha.getDate() - 1);
                
                nextfecha = formatFechaDDMMYYYY(formatFechaYYYYMMDD(nextfecha));
                $('#fecha-prox-lectura').html(nextfecha + '<br /><br />');
                //$('#direccion-suministro').html(remcli.direccion + '<br /><br />');
                var fechadesde = new Date(FECHA_LECTURA_INICIAL);
                fechadesde.setDate(fechadesde.getDate() + 2);
                var fechahasta = new Date(FECHA_LECTURA_FINAL);
                fechahasta.setDate(fechahasta.getDate() + 2);
                /*
                $('#desde').html(formatFechaDDMMYYYY(FECHA_LECTURA_INICIAL) + '<br /><br />');
                $('#hasta').html(formatFechaDDMMYYYY(FECHA_LECTURA_FINAL) + '<br /><br />');
                */
                $('#desde').html(formatFechaDDMMYYYY(formatFechaYYYYMMDD(fechadesde)) + '<br /><br />');
                $('#hasta').html(formatFechaDDMMYYYY(formatFechaYYYYMMDD(fechahasta)) + '<br /><br />');
                $('#suministradas').html(remcli.dmps + '<br /><br />');
                $('#horas-punta').html(remcli.dmplhp + '<br /><br />');
                $('#leidas-suministradas').html(MAX_DEMANDA_LEIDA + '<br /><br />');
                $('#leidas-horas-punta').html(MAX_DEMANDA_HORA_PUNTA + '<br /><br />');
                $('#consumo-total-kwh').html(formatMiles(CONSUMO) + '<br /><br />');
                $('#nomred').html(remcli.nomred + '<br /><br />');
                $('#num-serie').html(NUMSERIE + '<br /><br />');
                $('#num-remarcador-boleta').html(NUMREMARCADOR + '<br /><br />');
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
    // Choose the element that our invoice is rendered in.
    const element = document.getElementById("modal-body");
    // Choose the element and save the PDF for our user.
    html2pdf()
            .from(element)
            .save("Detalle-" + REMCLI.nomcliente + "-" + REMCLI.numremarcador + $('#mes').val().split("-")[1] + "-" + $('#mes').val().split("-")[0] + ".pdf");
}