var GRAFICO = new Chart(document.getElementById("grafico"));

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

function getSelectBodegasInstalacion() {
    var idinstalacion = $('#select-instalacion').val();
    $('#select-bodega').html('<option value="0" slected="selected" >Seleccione</option>');
    $('#select-empalme').html('<option value="0" slected="selected" >Seleccione</option>');
    var datos = {
        tipo: 'get-select-bodegas-instalacion',
        idinstalacion: idinstalacion
    };
    $.ajax({
        url: 'ParqueController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (resp) {
            var obj = JSON.parse(resp);
            if (obj.estado === 'ok') {
                $('#select-bodega').html(obj.options);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function getSelectEmpalmeIdParque(idparque) {
    $('#select-empalme').html('<option value="0" selected="selected" >Seleccione</option>');
    var datos = {
        tipo: 'get-select-empalme-idparque',
        idparque: idparque
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
                $('#select-empalme').html('');
                $('#select-empalme').html(obj.options);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function getRemarcadoresIdEmpalme() {
    var idempalme = $('#select-empalme').val();
    var numempalme = $('#select-empalme option:selected').text();
    var datos = {
        tipo: 'get-remarcadores-idempalme',
        idempalme: idempalme,
        numempalme: numempalme
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
                $('.dataTable').DataTable().destroy();
                $('#detalle-remarcadores').html(obj.tabla);
                $('#tabla-remarcadores-empalme').DataTable(OPCIONES_DATATABLES);
                $('.dataTables_filter').css("font-size", "12px;");
                $('.dataTables_filter').css("font-size", 12);
                $('.dataTables_filter').addClass("small");
                graficarRemarcadores(obj);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function graficarRemarcadores(obj) {
    var remarcadores = [];
    for (var x in obj.remarcadores) {
        remarcadores.push({idremarcador: obj.remarcadores[x].idremarcador, numremarcador: obj.remarcadores[x].numremarcador});
    }
    var mes = $('#mes').val().split("-")[1];
    var anio = $('#mes').val().split("-")[0];
    var datos = {
        tipo: 'resumen-mes-remarcadores-empalme',
        remarcadores: remarcadores,
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
            var obj = JSON.parse(resp);
            if (obj.estado === 'ok') {
                GRAFICO.destroy();
                $('.loader').fadeOut(500);
                $('#btn-buscar').removeAttr("disabled");
                for (var i in obj.data.datasets) {
                    var color = colorDinamicoArr();
                    obj.data.datasets[i].borderColor = "rgba(" + color[0] + ", " + color[1] + ", " + color[2] + ", 1.0)";
                    
                    obj.data.datasets[i].pointRadius = "2";
                    obj.data.datasets[i].borderWidth = "1";
                    obj.data.datasets[i].lineTension = "0";
                    obj.data.datasets[i].fill = false;
                }
                GRAFICO = new Chart(document.getElementById("grafico"), {
                    type: 'line',
                    data: obj.data,
                    options: {
                        title: {
                            display: true,
                            text: 'Consumo Registrado ' + $('#mes').val()
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

function validarCampos() {
    var idinstalacion = $('#select-instalacion').val();
    var idparque = $('#select-bodega').val();
    var idempalme = $('#select-empalme').val();
    var mes = $('#mes').val();

    if (idinstalacion === '0' || idinstalacion === '' || idinstalacion === null || idinstalacion === undefined || idinstalacion === 0) {
        alert("Debe seleccionar una Instalación del listado.");
        return false;
    }
    if (idparque === '0' || idparque === '' || idparque === null || idparque === undefined || idparque === 0) {
        alert("Debe seleccionar una Bodega del listado.");
        return false;
    }
    if (idempalme === '0' || idempalme === '' || idempalme === null || idempalme === undefined || idempalme === 0) {
        alert("Debe seleccionar un Empalme del listado.");
        return false;
    }
    if (mes.length < 6) {
        alert("Debe indicar un mes válido.");
        return false;
    }
    return true;
}

function buscar() {
    if (validarCampos()) {
        $('#btn-buscar').attr("disabled", "disabled");
        $('.loader').fadeIn(500);
        getRemarcadoresIdEmpalme();
    }
}