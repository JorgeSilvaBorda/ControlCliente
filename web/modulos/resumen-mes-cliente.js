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

function buscar(idcliente) {
    $('.loader').fadeIn(500);
    var datos = {
        tipo: 'resumen-mes-cliente',
        idcliente: idcliente
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
                $('.loader').fadeOut(500);
                for (var i in obj.data.datasets) {
                    var color = colorDinamicoArr();
                    obj.data.datasets[i].borderColor = "rgba(" + color[0] + ", " + color[1] + ", " + color[2] +  ", 1.0)";
                    obj.data.datasets[i].backgroundColor = "rgba(" + color[0] + ", " + color[1] + ", " + color[2] +  ", 0.3)";
                }
                console.log(obj.data.datasets);
                GRAFICO = new Chart(document.getElementById("line-chart"), {
                    type: 'bar',
                    data: obj.data,
                    options: {
                        title: {
                            display: true,
                            text: 'Consumo Registrado Mes Actual'
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