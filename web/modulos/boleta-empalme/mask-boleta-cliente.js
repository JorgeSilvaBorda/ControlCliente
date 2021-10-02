var GRAFICO = new Chart(document.getElementById("grafico"));
var REMCLI = null;
var DATOS_BOLETA = {};
var BOLETA = {};
var MASIVO = null;

function getRemarcadorClienteIdRemarcador(idremarcador) {
    $('#rut-cliente').html('');
    $('#nom-cliente').html('');
    $('#direccion').html('');
    $('#persona').html('');
    $('#fono').html('');
    $('#email').html('');
    var mesanio = HASTA.split("-")[0] + "-" + HASTA.split("-")[1];

    var datos = {
        tipo: 'get-remarcador-cliente-idremarcador',
        idremarcador: idremarcador,
        desde: DESDE,
        hasta: HASTA,
        mesanio: mesanio
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
                console.log(remcli);
                var aniomes = HASTA.split("-")[0] + "-" + HASTA.split("-")[1];
                if ($("#check-grafico").is(":checked")) {
                    graficarDesde(remcli.idremarcador, aniomes);
                }else{
                    $('#loader-grafico').hide();
                }
                $('#rut-cliente').html($.formatRut(remcli.rutcliente + "-" + remcli.dvcliente));
                $('#nom-cliente').html(remcli.razoncliente);
                $('#direccion').html(remcli.direccion + ',&nbsp;' + remcli.nomcomuna);
                $('#persona').html(remcli.persona);
                $('#fono').html(remcli.fono);
                $('#email').html(remcli.email);
                $('#num-empalme-boleta').html(remcli.numempalme + '<br /><br />');

                var fechaemision = new Date();
                DATOS_BOLETA.fechaemision = formatFechaYYYYMMDD(fechaemision);
                $('#fecha-emision').html(formatFechaDDMMYYYY(formatFechaYYYYMMDD(fechaemision)) + '<br /><br />');

                $('#fecha-prox-lectura').html('-<br /><br />');
                var fechadesde = new Date(DESDE);
                fechadesde.setDate(fechadesde.getDate() + 1);
                var fechahasta = new Date(HASTA);
                fechahasta.setDate(fechahasta.getDate() + 1);
                $('#desde').html(formatFechaDDMMYYYY(formatFechaYYYYMMDD(fechadesde)) + '<br /><br />');
                DATOS_BOLETA.fechadesde = formatFechaYYYYMMDD(fechadesde);
                $('#hasta').html(formatFechaDDMMYYYY(formatFechaYYYYMMDD(fechahasta)) + '<br /><br />');
                DATOS_BOLETA.fechahasta = formatFechaYYYYMMDD(fechahasta);
                $('#suministradas').html(remcli.dmps_string.replace(".", ",") + '<br /><br />');
                $('#horas-punta').html(remcli.dmplhp_string.replace(".", ",") + '<br /><br />');
                $('#leidas-suministradas').html(MAX_DEMANDA_LEIDA.replace(".", ",") + '<br /><br />');
                $('#leidas-horas-punta').html(MAX_DEMANDA_HORA_PUNTA.replace(".", ",") + '<br /><br />');
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
    var mesanio = MES;
    // var consumo = CONSUMO;
    var datos = {
        tipo: 'get-detalle-tarifa-id-red-consumo-remarcador',
        idtarifa: idtarifa,
        idred: idred,
        consumo: CONSUMO,
        numremarcador: numremarcador,
        mesanio: mesanio
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
                BOLETA.nextfecha = '1900-01-01';
                BOLETA.fechadesde = DATOS_BOLETA.fechadesde;
                BOLETA.fechahasta = DATOS_BOLETA.fechahasta;
                BOLETA.idtarifa = idtarifa;

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
    var idtarifa = $('#select-tarifa').val();
    var nomtarifa = $('#select-tarifa option:selected').text();
    BOLETA.idtarifa = idtarifa;
    BOLETA.nomtarifa = nomtarifa;
    var desde = $('#desde').val();
    var datos = {
        tipo: 'genera-boleta',
        remcli: REMCLI,
        boleta: BOLETA,
        tipoboleta: 'ESPECIFICA'
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
                //Actualizar número de boleta antes de emitir----------------------
                $('#num-boleta').html(obj.numboleta);
                //Actualizar grilla de atrás---------------------------------------
                buscar();
                //Generar PDF -----------------------------------------------------
                const element = document.getElementById("modal-body");
                html2pdf().from(element).save("Detalle-" + REMCLI.nomcliente + "-ID" + REMCLI.numremarcador + "-" + MES.split("-")[1] + "-" + MES.split("-")[0] + ".pdf");
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });

}

function graficarDesde(idremarcador, aniomes) {
    $('#btn-generar').attr("disabled", "disabled");
    var idcliente = REMCLI.idcliente;
    var numremarcador = REMCLI.numremarcador;
    var datos = {
        tipo: 'consumo-cliente-remarcador-aniomes',
        idcliente: idcliente,
        idremarcador: idremarcador,
        numremarcador: numremarcador,
        aniomes: aniomes
    };

    $.ajax({
        url: 'ReportesController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (resp) {
            $('#loader-grafico').remove();
            var obj = JSON.parse(resp);
            if (obj.estado === 'ok') {
                var fondo = [];
                var borde = [];
                var cont = 0;
                for (var i in obj.data.labels) {
                    obj.data.labels[i] = fechaAMesPalabraCorto(obj.data.labels[i]);
                    if (cont === obj.data.labels.length - 1) {
                        fondo.push("rgba(5, 82, 16, 0.65)");
                        borde.push("rgba(5, 82, 16, 1)");
                    } else {
                        fondo.push("rgba(117, 0, 0, 0.65)");
                        borde.push("rgba(117, 0, 0, 1)");
                    }
                    cont++;
                }
                obj.data.datasets[0].backgroundColor = fondo;
                obj.data.datasets[0].borderColor = borde;

                GRAFICO.destroy();
                GRAFICO = new Chart(document.getElementById("grafico"), {
                    type: 'bar',
                    data: obj.data,
                    options: {
                        legend: {
                            display: false
                        },
                        title: {
                            display: true,
                            text: 'Últimos 12 meses de consumo'
                        },
                        tooltips: {
                            enabled: false
                        },
                        scales: {
                            yAxes: [{
                                    ticks: {
                                        fontSize: 10
                                    },
                                    scaleLabel: {
                                        display: true,
                                        labelString: 'kWh'
                                    }

                                }],
                            xAxes: [{
                                    ticks: {
                                        fontSize: 10
                                    },
                                    scaleLabel: {
                                        display: true,
                                        labelString: 'Meses'
                                    }

                                }]
                        }
                    }
                });
            }
            $('#btn-generar').removeAttr("disabled");
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function armarLastBoleta() {
    var datos = {
        tipo: 'get-last-boleta-idboleta',
        idboleta: IDBOLETA
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
                console.log(obj);
                REMCLI = {
                    idcliente: obj.boleta.IDCLIENTE,
                    numremarcador: obj.boleta.NUMREMARCADOR
                };
                //var aniomes = obj.boleta.FECHALECTURAACTUAL.toString().split("-")[0] + '-' + obj.boleta.FECHALECTURAACTUAL.toString().split("-")[1];
                var aniomes = HASTA.split("-")[0] + "-" + HASTA.split("-")[1];
                $('#num-medidor').html(obj.boleta.NUMREMARCADOR);
                $('#lectura-anterior').html(formatMiles(obj.boleta.LECTURAANTERIOR));
                $('#lectura-actual').html(formatMiles(obj.boleta.LECTURAACTUAL));
                $('#consumo').html(formatMiles(obj.boleta.CONSUMO));
                $('#rut-cliente').html(formatMiles(obj.boleta.RUTCLIENTE) + '-' + obj.boleta.DVCLIENTE);
                $('#nom-cliente').html(obj.boleta.NOMCLIENTE);
                $('#direccion').html(obj.boleta.DIRECCION + ', ' + obj.boleta.NOMCOMUNA);
                $('#persona').html(obj.boleta.PERSONA);
                $('#fono').html(obj.boleta.FONO);
                $('#email').html(obj.boleta.EMAIL);
                $('#num-boleta').html(obj.boleta.NUMBOLETA);

                $('#num-empalme-boleta').html(obj.boleta.NUMEMPALME + "<br /><br />");
                $('#num-remarcador-boleta').html(obj.boleta.NUMREMARCADOR + "<br /><br />");
                $('#num-serie').html(obj.boleta.NUMSERIE + "<br /><br />");
                $('#fecha-emision').html(obj.boleta.FECHAEMISION + "<br /><br />");
                $('#fecha-prox-lectura').html(obj.boleta.FECHAPROXLECTURA + "<br /><br />");
                $('#num-serie').html(obj.boleta.NUMSERIE + "<br /><br />");
                $('#tarifa').html(obj.boleta.NOMTARIFA + "<br /><br />");
                $('#nomred').html(obj.boleta.NOMRED + "<br /><br />");
                $('#desde').html(formatFechaDDMMYYYY(obj.boleta.FECHALECTURAANTERIOR) + "<br /><br />");
                $('#hasta').html(formatFechaDDMMYYYY(obj.boleta.FECHALECTURAACTUAL) + "<br /><br />");
                $('#leidas-suministradas').html(obj.boleta.DEM_MAX_SUMINISTRADA_LEIDA + "<br /><br />");
                $('#leidas-horas-punta').html(obj.boleta.DEM_MAX_HORA_PUNTA_LEIDA + "<br /><br />");
                $('#suministradas').html(obj.boleta.DEM_MAX_SUMINISTRADA_FACTURADA + "<br /><br />");
                $('#horas-punta').html(obj.boleta.DEM_MAX_HORA_PUNTA_FACTURADA + "<br /><br />");
                $('#detalle-tarifa-remarcador').html(obj.tabla);
                if ($("#check-grafico").is(":checked")) {
                    graficarDesde(obj.boleta.IDREMARCADOR, aniomes);
                }else{
                    $('#loader-grafico').hide();
                }
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function imprimir() {
    const element = document.getElementById("modal-body");
    html2pdf().from(element).save("Detalle-" + $('#nom-cliente').text() + "-ID" + $('#num-remarcador-boleta').text() + "-" + $('#mes').val().split("-")[1] + "-" + $('#mes').val().split("-")[0] + ".pdf");
}