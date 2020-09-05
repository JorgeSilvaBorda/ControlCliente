
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

function getSelectEmpalmesNumEmpalmesInstalacion() {
    var idinstalacion = $('#select-instalacion').val();
    var datos = {
        tipo: 'get-select-empalmes-numempalmes-idinstalacion',
        idinstalacion: idinstalacion
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

function validarCampos() {
    var mes = $('#mes').val();
    if (mes.length < 6) {
        alert("El mes indicado es incorrecto.");
        return false;
    }
    return true;
}

function buscar() {
    if (validarCampos()) {
        $('#btn-buscar').attr("disabled", "disabled");
        $('.loader').fadeIn(500);
        getPagos();
    }
}

function getPagos() {
    var mes = $('#mes').val();
    var numempalme = $('#select-empalme').val();
    var datos = {
        tipo: 'get-resumen-pagos',
        mes: mes,
        numempalme: numempalme
    };
    $.ajax({
        url: 'BoletaController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (resp) {
            var obj = JSON.parse(resp);
            if (obj.estado === 'ok') {
                $('.dataTable').DataTable().destroy();
                $('#resumen-pagos').html(obj.tabla);
                $('.loader').fadeOut(500);
            }
        },
        error: function (a, b, c) {
            KWTOTAL = null;
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function getLastBoleta(idboleta) {
    $('#btn-generar').hide();
    $('#btn-imprimir').show();
    $('#modal-body').html('');
    $('#modal').modal();
    $('#modal-body').load("modulos/boleta-empalme/mask-last-boleta-empalme.jsp?idboleta=" + idboleta);
}

function getLastBoleta(idboleta) {
    $('#btn-generar').hide();
    $('#btn-imprimir').show();
    $('#modal-body').html('');
    $('#modal').modal();
    $('#modal-body').load("modulos/boleta-empalme/mask-last-boleta-empalme.jsp?idboleta=" + idboleta);
}

function limpiar() {

}

var tableToExcel = (function () {
    var uri = 'data:application/vnd.ms-excel;base64,'
            , template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--></head><body><table>{table}</table></body></html>'
            , base64 = function (s) {
                return $.base64.encode(s);
            }
    , format = function (s, c) {
        return s.replace(/{(\w+)}/g, function (m, p) {
            return c[p];
        });
    };
    return function (table, name) {
        if (!table.nodeType)
            table = document.getElementById(table);
        var ctx = {worksheet: name || 'Worksheet', table: table.innerHTML};
        var filename = 'Resumen-Pagos-' + $('#mes').val() + '.xls';
        downloadLink = document.createElement("a");
        downloadLink.download = filename;

        downloadLink.href = uri + base64(format(template, ctx));
        document.body.appendChild(downloadLink);
        downloadLink.click();
        document.body.removeChild(downloadLink);
        delete downloadLink;
    };
})();