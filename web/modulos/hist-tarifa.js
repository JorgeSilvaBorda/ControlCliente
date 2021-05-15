function getSelectAnioTarifa() {
    var datos = {
        tipo: 'get-select-anio-tarifa'
    };
    $.ajax({
        url: 'TarifaController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (resp) {
            var obj = JSON.parse(resp);
            if (obj.estado === 'ok') {
                $('#select-anio-tarifa').html(obj.options);
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
    var anio = $('#select-anio-tarifa').val();
    if (anio === 0 || anio === '0') {
        alert("Por favor seleccione un año para la búsqueda.");
        return false;
    }
    return true;
}

function buscar() {
    if (validarCampos()) {
        var anio = $('#select-anio-tarifa').val();
        var datos = {
            tipo: 'get-hist-tarifa-anio',
            anio: anio
        };
        $.ajax({
            url: 'TarifaController',
            type: 'post',
            data: {
                datos: JSON.stringify(datos)
            },
            success: function (resp) {
                var obj = JSON.parse(resp);
                if (obj.estado === 'ok') {
                    $('#hist-tarifa').html(obj.tabla);
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
function exportExcel(tableID, filename = '') {
    var downloadLink;
    var dataType = 'application/vnd.ms-excel';
    var tableSelect = document.getElementById(tableID);
    var tableHTML = tableSelect.outerHTML.replace(/ /g, '%20');
    var anio = $('#select-anio-tarifa').val();
    filename = filename ? filename + '.xlsx' : 'Hist-Tarifas-' + anio + '.xlsx';
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