var REMCLI = null;

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
            console.log("Se parsea");
            if (obj.estado === 'ok') {
                console.log(obj);
                REMCLI = obj.remarcador;
                var remcli = obj.remarcador;
                console.log(remcli);
                $('#rut-cliente').html($.formatRut(remcli.rutcliente + "-" + remcli.dvcliente));
                $('#nom-cliente').html(remcli.nomcliente);
                $('#direccion').html(remcli.direccion);
                $('#persona').html(remcli.persona);
                $('#fono').html(remcli.fono);
                $('#email').html(remcli.email);
                getSelectTarifasIdComuna(remcli.idcomuna);

                $('#num-cliente').html($.formatRut(remcli.rutcliente + "-" + remcli.dvcliente) + '<br /><br />');
                $('#fecha-emision').html(formatFechaDDMMYYYY(FECHAFIN) + '<br /><br />');
                var dt = new Date(FECHAFIN);
                var nextfecha = new Date(dt.setMonth(dt.getMonth() + 1));
                nextfecha = formatFechaDDMMYYYY(nextfecha);
                $('#fecha-prox-lectura').html(nextfecha + '<br /><br />');
                $('#direccion-suministro').html(remcli.direccion + '<br /><br />');
                //$('#tarifa').html($('#select-tarifa option:selected').html() + '<br /><br />');
                $('#desde').html(formatFechaDDMMYYYY(FECHAINI) + '<br /><br />');
                $('#hasta').html(formatFechaDDMMYYYY(FECHAFIN) + '<br /><br />');
                $('#suministradas').html(formatMiles(remcli.dmps) + '<br /><br />');
                $('#horas-punta').html(formatMiles(remcli.dmplhp) + '<br /><br />');
                $('#consumo-total-kwh').html(formatMiles(CONSUMO) + '<br /><br />');
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
        numremarcador: numremarcador,
        fechafin: fechafin
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
    html2canvas(document.getElementById('cont-boleta'), {
        onrendered: function (canvas) {
            var data = canvas.toDataURL();
            var docDefinition = {
                content: [{
                        image: data,
                        width: 1000
                    }]
            };
            var pdfDoc = pdfMake.createPdf(docDefinition);
            var docDefinition = getRWABELPDF(data);
            var createPdf = pdfMake.createPdf(docDefinition);
            var base64data = null;

            createPdf.getBase64(function (encodedString) {
                base64data = encodedString;
                console.log(base64data);


                var byteCharacters = atob(base64data);
                var byteNumbers = new Array(byteCharacters.length);
                for (var i = 0; i < byteCharacters.length; i++) {
                    byteNumbers[i] = byteCharacters.charCodeAt(i);
                }
                var byteArray = new Uint8Array(byteNumbers);
                var file = new Blob([byteArray], {type: 'application/pdf;base64'});
                var fileURL = URL.createObjectURL(file);
                window.open(fileURL);
            });
        }
    });

}