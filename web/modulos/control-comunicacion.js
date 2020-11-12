var TABLA;
function cargarPestana(enlace, nombre) {
    $('a.nav-link').removeClass("active");
    $(enlace).addClass("active");
    $('#cont-eventos').load('modulos/' + nombre + '.jsp');
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

function getEventosNuevosComunicacion() {
    TABLA = null;
    $('#btn-marcar-todos').attr("hidden", "hidden");
    $('#btn-excel').attr("hidden", "hidden");
    var datos = {
        tipo: 'get-nuevos-eventos-comunicacion'
    };
    $.ajax({
        url: 'EventosController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (resp) {
            var obj = JSON.parse(resp);
            if (obj.estado === 'ok') {
                $('.dataTable').DataTable().destroy();
                $('#tabla-eventos-comunicacion tbody').html(obj.cuerpo);
                var OPCIONES = OPCIONES_DATATABLES;
                OPCIONES.order = [[0, "desc"], [1, "desc"]];
                OPCIONES.dom = 'Bfrtip';
                OPCIONES.buttons = [
                    {
                        extend: 'excelHtml5',
                        title: 'Problemas de comunicación',
                        exportOptions: {
                            columns: [0, 1, 2, 3, 4, 5]
                        }
                    }

                ];
                TABLA = $('#tabla-eventos-comunicacion').DataTable(OPCIONES);
                $('.buttons-html5').addClass("btn-sm");
                $('.buttons-html5').addClass("btn-success");
            }
            if (obj.cant > 0) {
                $('#btn-marcar-todos').removeAttr("hidden");
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function getEventosTodosComunicacion() {
    TABLA = null;
    $('#btn-excel').attr("hidden", "hidden");
    var datos = {
        tipo: 'get-todos-eventos-comunicacion'
    };
    $.ajax({
        url: 'EventosController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (resp) {
            var obj = JSON.parse(resp);
            if (obj.estado === 'ok') {
                $('.dataTable').DataTable().destroy();
                $('#tabla-eventos-comunicacion tbody').html(obj.cuerpo);
                var OPCIONES = OPCIONES_DATATABLES;
                OPCIONES.order = [[0, "desc"], [1, "desc"]];
                OPCIONES.dom = 'Bfrtip';
                OPCIONES.buttons = [
                    {
                        extend: 'excelHtml5',
                        title: 'Problemas de comunicación',
                        exportOptions: {
                            columns: [0, 1, 2, 3, 4, 5]
                        }
                    }

                ];
                TABLA = $('#tabla-eventos-comunicacion').DataTable(OPCIONES);
                $('.buttons-html5').addClass("btn-sm");
                $('.buttons-html5').addClass("btn-success");
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function marcarLeido(boton, idnotificacion) {
    var datos = {
        tipo: 'marcar-evento-leido',
        idnotificacion: idnotificacion
    };
    $.ajax({
        url: 'EventosController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (resp) {
            var obj = JSON.parse(resp);
            if (obj.estado === 'ok') {
                $(boton).hide();
                actualizaNuevosEventos();
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function marcarTodos() {
    if (confirm("Está seguro de que desea marcar todos los eventos como leídos? Esta acción los quitará de esta vista y podrá consultarlos en la pestaña \"Todos\"")) {
        var ides = [];
        for (var i = 0; i < TABLA.rows().data().length; i++) {
            var arr = TABLA.rows().data()[i];
            TABLA.rows().data()[i][6].replace("button ", "button hidden='hidden' ");
            ides.push($(arr[0]).val());
        }

        var datos = {
            tipo: 'marcar-todos-leidos',
            ides: ides
        };
        $.ajax({
            url: 'EventosController',
            type: 'post',
            data: {
                datos: JSON.stringify(datos)
            },
            success: function (resp) {
                var obj = JSON.parse(resp);
                if (obj.estado === 'ok') {
                    actualizaNuevosEventos();
                    getEventosNuevosComunicacion();
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
