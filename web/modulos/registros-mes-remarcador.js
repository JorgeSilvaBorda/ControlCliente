function getSelectInstalacion() {
    $('#select-instalacion').html('<option value="0" selected="selected" >Seleccione</option>');
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
    $('#select-remarcador').html('<option value="0" selected="selected" >Seleccione</option>');
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
    $('#select-remarcador').html('<option value="0" selected="selected" >Seleccione</option>');
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

function getSelectRemarcadorIdEmpalme(idempalme) {
    $('#select-remarcador').html('<option value="0" selected="selected" >Seleccione</option>');
    var datos = {
        tipo: 'get-select-remarcador-idempalme',
        idempalme: idempalme
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
                $('#select-remarcador').html('');
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
    var numremarcador = $('#select-remarcador option:selected').text();
    var mes = $('#mes').val();
    var messolo = mes.split("-")[1];
    var aniosolo = mes.split("-")[0];
    var datos = {
        tipo: 'get-registros-mes-remarcador',
        idremarcador: idremarcador,
        numremarcador: numremarcador,
        mes: mes,
        messolo: messolo,
        aniosolo: aniosolo
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
                console.log(obj);
                $('.loader').fadeOut(500);
                $('#btn-buscar').removeAttr("disabled");
                $('.dataTable').DataTable().destroy();
                $('#detalle-remarcador').html(obj.tabla);
                var OPCIONES_EXCEL = [
                    {
                        extend: 'excelHtml5',
                        title: 'Lecturas-R-' + numremarcador + "-" + mes
                    }
                ];
                OPCIONES_DATATABLES.buttons = OPCIONES_EXCEL;
                $('#tabla-detalle-remarcador').DataTable(OPCIONES_DATATABLES);
                $('.dataTables_filter').css("font-size", "12px;");
                $('.dataTables_filter').css("font-size", 12);
                $('.dataTables_filter').addClass("small");
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
    var idremarcador = $('#select-remarcador').val();
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
    if (idremarcador === '0' || idremarcador === '' || idremarcador === null || idremarcador === undefined || idremarcador === 0) {
        alert("Debe seleccionar un Remarcador del listado.");
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
        getDatosRemarcador();
    }
}