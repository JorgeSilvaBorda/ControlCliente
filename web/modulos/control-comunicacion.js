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
                /*
                 $('#tabla-eventos-comunicacion thead th').each(function () {
                 var title = $(this).text();
                 $(this).html(title + '<input type="text" onclick="return false;" placeholder="Buscar ' + title + '" />');
                 });
                 */


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
                $('#tabla-eventos-comunicacion_filter label input').on('keyup change', function () {
                    TABLA.column(2).search($(this).val()).draw();
                });
                $('.buttons-html5').addClass("btn-sm");
                $('.buttons-html5').addClass("btn-success");
                
                $('input[type=search]').on('search', function () {
                    $('input[type=search]').change();
                });
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

                $('#tabla-eventos-comunicacion_filter label input').on('keyup change', function () {
                    TABLA.column(2).search($(this).val()).draw();
                });
                $('.buttons-html5').addClass("btn-sm");
                $('.buttons-html5').addClass("btn-success");
                $('input[type=search]').on('search', function () {
                    $('input[type=search]').change();
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

        for (var i = 0; i < TABLA.rows({search: 'applied'}).nodes().length; i++) {
            var fila = $(TABLA.rows({search: 'applied'}).nodes()[i]);
            var idnotificacion = fila[0].cells[0].childNodes[0].value;
            ides.push(idnotificacion);
        }
        for (var i = 0; i < TABLA.rows().data().length; i++) {
            for (var x = 0; x < ides.length; x++) {
                if ($(TABLA.rows().data()[i][0]).val() === ides[x]) {
                    TABLA.rows().data()[i][6].replace("button ", "button hidden='hidden' ")
                }
            }
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

function getExcepciones() {
    TABLA = null;
    $('#btn-excel').attr("hidden", "hidden");
    var datos = {
        tipo: 'get-excepciones'
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
                $('#tabla-excepciones tbody').html(obj.cuerpo);
                var OPCIONES = OPCIONES_DATATABLES;
                OPCIONES.order = [[0, "desc"], [1, "asc"]];
                OPCIONES.dom = 'Bfrtip';
                OPCIONES.buttons = [
                    {
                        extend: 'excelHtml5',
                        title: 'Excepciones',
                        exportOptions: {
                            columns: [0, 1, 2, 3, 4]
                        }
                    }

                ];
                TABLA = $('#tabla-excepciones').DataTable(OPCIONES);
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

function validarExcepcion() {

    var valmotivo = $('input:radio[name=opciones]:checked').val();
    var motivo = '';
    if (valmotivo === '1') {
        motivo = 'Bodega vacía';
    } else if (valmotivo === '2') {
        motivo = 'Bodega pulmón';
    } else if (valmotivo === '3') {
        motivo = 'En falla';
    } else {
        alert('Debe seleccionar un motivo para la excepción.');
        return false;
    }

    var numremarcador = $('#numremarcador').val();
    if (numremarcador === '' || numremarcador === 0 || numremarcador === '0') {
        alert('Debe indicar el ID del remarcador para ingresar la excepción.');
        return false;
    }

    var duracion = $('#select-duracion').val();
    if (duracion === 0 || duracion === '0') {
        alert("Debe seleccionar una duración para ingresar la excepción.");
        return false;
    }
    return true;
}

function insExcepcion() {
    if (validarExcepcion()) {
        var valmotivo = $('input:radio[name=opciones]:checked').val();
        var motivo = '';
        if (valmotivo === '1') {
            motivo = 'Bodega vacía';
        } else if (valmotivo === '2') {
            motivo = 'Bodega pulmón';
        } else if (valmotivo === '3') {
            motivo = 'En falla';
        } else if (valmotivo === '4') {
            motivo = 'SSCC BFC';
        }
        var numremarcador = $('#numremarcador').val();
        var duracion = parseInt($('#select-duracion').val());
        var descduracion = '';
        switch (duracion) {
            case 1:
                descduracion = "DIA";
                break;
            case 2:
                descduracion = "SEMANA";
                break;
            case 3:
                descduracion = "MES";
                break;
        }

        var datos = {
            tipo: 'ins-excepcion',
            numremarcador: numremarcador,
            motivo: motivo,
            duracion: duracion,
            descduracion: descduracion
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
                    getExcepciones();
                    limpiarExcepciones();
                }
            },
            error: function (a, b, c) {
                console.log(a);
                console.log(b);
                console.log(c);
            }
        });
    }

    function limpiarExcepciones() {
        $('input:radio').prop("checked", false);
        $('#numremarcador').val('');
        $('#select-duracion').val('0');
    }

}

function getExcepcionesRemarcador() {
    var numremarcador = $('#numremarcador').val();

    var datos = {
        tipo: 'get-excepciones-remarcador',
        numremarcador: numremarcador
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
                if (parseInt(obj.cantidad) > 0) {
                    if (confirm("El remarcador seleccionado ya posee una excepción creada. La anterior se eliminará para crear ésta. ¿Está seguro?")) {
                        insExcepcion();
                    }
                } else {
                    insExcepcion();
                }
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function delExcepcion(idexcepcion) {
    if (confirm("¿Está seguro de que desea eliminar la excepción seleccionada?\nSe comenzará a emitir notificaciones para este remarcador.")) {

        var datos = {
            tipo: 'del-excepcion',
            idexcepcion: idexcepcion
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
                    getExcepciones();
                    limpiarExcepciones();
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

function limpiarExcepciones() {
    $('input:radio').prop("checked", false);
    $('#numremarcador').val('');
    $('#select-duracion').val('0');
}
