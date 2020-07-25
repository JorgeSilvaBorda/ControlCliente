var GRAFICO = new Chart(document.getElementById("line-chart"));
var GRAFICO_DEMANDAS = new Chart(document.getElementById("line-chart-demandas"));

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
    var datos = {
        tipo: 'get-select-instalaciones-idcliente',
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

function buscar() {
    if (validarCampos()) {
        GRAFICO.destroy();
        GRAFICO_DEMANDAS.destroy();
        $('.loader').fadeIn(500);
        var idinstalacion = $('#select-instalacion').val();
        var nomcliente = $('#select-cliente option:selected').text();
        var idcliente = $('#select-cliente').val();
        var mesanio = $('#mes').val();
        var mes = mesanio.split("-")[1];
        var anio = mesanio.split("-")[0];
        var datos = {
            tipo: 'resumen-mes-cliente',
            idcliente: idcliente,
            idinstalacion: idinstalacion,
            mes: mes,
            anio: anio
        };

        $.ajax({
            url: 'ReportesController',
            type: 'post',
            data: {
                datos: JSON.stringify(datos)
            },
            success: function (resp) {
                var colores = [];

                var obj = JSON.parse(resp);
                if (obj.estado === 'ok') {
                    $('#div-tabla-resumen').html(obj.data.tablaresumen);
                    $('.loader').fadeOut(500);
                    for (var i in obj.data.labels) {
                        obj.data.labels[i] = formatFechaDDMMYYYY(obj.data.labels[i]);
                    }
                    for (var i in obj.data.datasets) {
                        var color = colorDinamicoArr();
                        obj.data.datasets[i].borderColor = "rgba(" + color[0] + ", " + color[1] + ", " + color[2] + ", 1.0)";
                        colores.push("rgba(" + color[0] + ", " + color[1] + ", " + color[2] + ", 1.0)");
                        obj.data.datasets[i].pointRadius = "2";
                        obj.data.datasets[i].borderRadius = "1";
                        obj.data.datasets[i].lineTension = "0";
                        obj.data.datasets[i].fill = false;
                    }
                    for (var i in obj.datademandas.labels) {
                        obj.datademandas.labels[i] = formatFechaDDMMYYYY(obj.datademandas.labels[i]);
                    }
                    var contColor = 0;
                    for (var i in obj.datademandas.datasets) {

                        obj.datademandas.datasets[i].pointRadius = "2";
                        obj.datademandas.datasets[i].borderRadius = "1";
                        obj.datademandas.datasets[i].lineTension = "0";

                        if (i === 0) {
                            obj.datademandas.datasets[i].borderColor = colores[0];
                            obj.datademandas.datasets[i].borderDash = [10, 5];
                        } else {
                            if (i % 2 !== 0) {
                                obj.datademandas.datasets[i].borderColor = colores[contColor];
                                obj.datademandas.datasets[i].borderDash = [10, 5];
                                contColor++;
                            } else {
                                obj.datademandas.datasets[i].borderColor = colores[contColor];
                            }
                        }

                        obj.datademandas.datasets[i].fill = false;
                    }
                    GRAFICO = new Chart(document.getElementById("line-chart"), {
                        type: 'line',
                        data: obj.data,
                        options: {
                            tooltips: {
                                enabled: true,
                                mode: 'single',
                                callbacks: {
                                    label: function (tooltipItems, data) {
                                        return data.datasets[tooltipItems.datasetIndex].label.replace(":", "") + ': ' + tooltipItems.yLabel + ' kWh';
                                    }
                                }
                            },
                            title: {
                                display: true,
                                text: 'Consumo ' + nomcliente + ' ' + mesNumeroAPalabraLarga(mes) + ' de ' + anio
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
                                            labelString: 'Día'
                                        }

                                    }]
                            }
                        }
                    });

                    GRAFICO_DEMANDAS = new Chart(document.getElementById("line-chart-demandas"), {
                        type: 'line',
                        data: obj.datademandas,
                        options: {
                            tooltips: {
                                enabled: true,
                                mode: 'single',
                                callbacks: {
                                    label: function (tooltipItems, data) {
                                        return data.datasets[tooltipItems.datasetIndex].label.replace(":", "") + ': ' + tooltipItems.yLabel + ' kW';
                                    }
                                }
                            },
                            title: {
                                display: true,
                                text: 'Demandas de Potencia ' + nomcliente + ' ' + mesNumeroAPalabraLarga(mes) + ' de ' + anio
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
                                            labelString: 'Día'
                                        }

                                    }]
                            }
                        }
                    });

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

function buscarDemandas() {
    var idcliente = $('#select-cliente').val();
    var mesanio = $('#mes').val();
    var idinstalacion = $('#select-instalacion').val();
    var anio = mesanio.split("-")[0];
    var mes = mesanio.split("-")[1];
    var datos = {
        tipo: 'get-dataset-demandas',
        idcliente: idcliente,
        idinstalacion: idinstalacion,
        anio: anio,
        mes: mes
    };

    $.ajax({
        url: 'ReportesController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (res) {
            var obj = JSON.parse(res);
            if (obj.estado === 'ok') {

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

    if (idcliente === 0 || idcliente === undefined || idcliente === null || idcliente === '' || idcliente === '0') {
        alert("Debe escoger un cliente del listado.");
        return false;
    }
    if (idinstalacion === undefined || idinstalacion === null || idinstalacion === '') {
        alert("Debe seleccionar todas o alguna instalación del listado.");
        return false;
    }
    return true;
}