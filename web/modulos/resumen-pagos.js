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
        //$('.loader').fadeIn(500);
        getPagos();
    }
}

function getPagos() {
    var mes = $('#mes').val();
    var datos = {
        tipo: 'get-resumen-pagos',
        mes: mes
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

function exportExcel(tableID, filename = '') {
    var downloadLink;
    var dataType = 'application/vnd.ms-excel';
    var tableSelect = document.getElementById(tableID);
    var tableHTML = tableSelect.outerHTML.replace(/ /g, '%20');
    filename = filename ? filename + '.xlsx' : 'Resumen-Pagos-' + $('#mes').val() + '.xlsx';
    downloadLink = document.createElement("a");
    document.body.appendChild(downloadLink);
    if (navigator.msSaveOrOpenBlob) {
        var blob = new Blob(['\ufeff', tableHTML], {
            type: dataType
        });
        navigator.msSaveOrOpenBlob(blob, filename);
    } else {
        downloadLink.href = 'data:' + dataType + ', ' + tableHTML;
        downloadLink.download = filename;
        downloadLink.click();
}
}