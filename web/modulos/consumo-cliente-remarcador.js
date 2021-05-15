var GRAFICO = new Chart(document.getElementById("grafico"));

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
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function getDatosRemarcador() {
    var idremarcador = $('#select-remarcador').val();
    var datos = {
        tipo: 'get-remarcador-idremarcador',
        idremarcador: idremarcador
    };
    console.log(datos);
    $.ajax({
        url: 'RemarcadorController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (resp) {
            var obj = JSON.parse(resp);
            if (obj.estado === 'ok') {
                armarDetalleRemarcador(obj.remarcador);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function armarDetalleRemarcador(remarcador) {
    var tabla = '<table>' +
            '<tr>' +
            '<td><strong># Empalme:</strong></td>' +
            '<td>' + remarcador.numempalme + '</td>' +
            '</tr>' +
            '<tr>' +
            '<td><strong># Remarcador:</strong></td>' +
            '<td>' + remarcador.numremarcador + '</td>' +
            '</tr>' +
            '<tr>' +
            '<td><strong>Nº Serie: </strong></td>' +
            '<td>' + remarcador.numserie + '</td>' +
            '</tr>' +
            '<tr>' +
            '<td><strong>Bodega:</strong></td>' +
            '<td>' + remarcador.nomparque + '</td>' +
            '</tr>' +
            '<tr>' +
            '<td><strong>Instalación:</strong></td>' +
            '<td>' + remarcador.nominstalacion + '</td>' +
            '</tr>' +
            '<tr>' +
            '<td><strong>Dirección:</strong></td>' +
            '<td>' + remarcador.direccion + '</td>' +
            '</tr>' +
            '<tr>' +
            '<td><strong>Módulos:</strong></td>' +
            '<td>' + remarcador.modulos + '</td>' +
            '</tr>' +
            '<tr>' +
            '<td><strong>Comuna:</strong></td>' +
            '<td>' + remarcador.nomcomuna + '</td>' +
            '</tr>' +
            '</table>';
    $('#detalle-remarcador').html(tabla);
}

function graficar() {
    var idremarcador = $('#select-remarcador').val();
    getDatosRemarcador();
    $('.loader').fadeIn(500);
    var idcliente = $('#select-cliente').val();
    var nomcliente = $('#select-cliente option:selected').text();
    var numremarcador = $("#select-remarcador option:selected").text();
    var datos = {
        tipo: 'consumo-cliente-remarcador',
        idcliente: idcliente,
        idremarcador: idremarcador,
        numremarcador: numremarcador
    };

    $.ajax({
        url: 'ReportesController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (resp) {
            $('.loader').fadeOut(500);
            var obj = JSON.parse(resp);
            if (obj.estado === 'ok') {
                for (var x in obj.data.labels) {
                    obj.data.labels[x] = fechaAMesPalabraCorto(obj.data.labels[x]);
                }
                for (var i in obj.data.datasets) {
                    var color = colorDinamicoArr();
                    obj.data.datasets[i].borderColor = "rgba(" + color[0] + ", " + color[1] + ", " + color[2] + ", 1.0)";
                    obj.data.datasets[i].backgroundColor = "rgba(" + color[0] + ", " + color[1] + ", " + color[2] + ", 0.3)";
                }
                GRAFICO.destroy();
                GRAFICO = new Chart(document.getElementById("grafico"), {
                    type: 'bar',
                    data: obj.data,
                    options: {
                        title: {
                            display: true,
                            text: 'Últimos 12 meses de consumo ' + nomcliente
                        },
                        tooltips: {
                            enabled: true,
                            mode: 'single',
                            callbacks: {
                                label: function (tooltipItems, data) {
                                    return data.datasets[tooltipItems.datasetIndex].label.replace(":", "") + ': ' + tooltipItems.yLabel + ' kWh';
                                }
                            }
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
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}