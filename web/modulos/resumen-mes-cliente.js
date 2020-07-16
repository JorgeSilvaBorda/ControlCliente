var GRAFICO = new Chart(document.getElementById("line-chart"));

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
        $('.loader').fadeIn(500);
        var idinstalacion = $('#select-instalacion').val();
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
                GRAFICO.destroy();
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
                        obj.data.datasets[i].pointRadius = "2";
                        obj.data.datasets[i].borderRadius = "1";
                        obj.data.datasets[i].lineTension = "0";
                        obj.data.datasets[i].fill = false;
                    }
                    console.log(obj.data.datasets);
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
                                text: 'Consumo Registrado Mes Actual'
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