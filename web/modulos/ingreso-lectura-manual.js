var FINMESANTERIOR = null;
var FINMESANTERIORFORMAT = null;
var ENERGIA = null;
var POTENCIA = null;
var FECHA = null;
var NUMREMARCADOR = null;

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

function getSelectBodegaIdInstalacion(idinstalacion) {
    var datos = {
        tipo: 'get-select-parque-idinstalacion',
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
                $('#select-parque').html('');
                $('#select-parque').html(obj.options);
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

function getLastMes() {
    var mes = $('#fecha').val();
    var datos = {
        tipo: 'get-finmes-anterior',
        mes: mes
    };
    $.ajax({
        url: 'ParametrosController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (resp) {
            var obj = JSON.parse(resp);
            if (obj.estado === 'ok') {
                FINMESANTERIOR = obj.finmesanterior;
                FINMESANTERIORFORMAT = obj.finmesanteriorformat;
                var maxmes = FINMESANTERIOR.split("-")[0] + "-" + FINMESANTERIOR.split("-")[1];
                $('#fecha').attr("max", maxmes);
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
    var idbodega = $('#select-parque').val();
    var idempalme = $('#select-empalme').val();
    var idremarcador = $('#select-remarcador').val();
    var mesanio = $('#fecha').val();

    if (parseInt(idinstalacion) === 0 || idinstalacion === null) {
        return false;
    }
    if (parseInt(idbodega) === 0 || idbodega === null) {
        return false;
    }
    if (parseInt(idempalme) === 0 || idempalme === null) {
        return false;
    }
    if (parseInt(idremarcador) === 0 || idremarcador === null) {
        return false;
    }
    if (mesanio === '') {
        return false;
    }
    return true;
}

function getLastLecturaMes() {
    if (validarCampos()) {
        var idremarcador = $('#select-remarcador').val();
        var mes = $('#fecha').val().split("-")[1];
        var anio = $('#fecha').val().split("-")[0];
        var datos = {
            tipo: 'get-last-lectura-mes',
            idremarcador: idremarcador,
            mes: mes,
            anio: anio
        };
        $.ajax({
            url: 'RemarcadorController',
            type: 'post',
            data: {
                datos: JSON.stringify(datos)
            },
            success: function (resp) {
                var obj = JSON.parse(resp);
                console.log(obj);
                if (obj.estado === 'ok') {
                    ENERGIA = obj.energia;
                    POTENCIA = obj.potencia;
                    FECHA = obj.fecha;
                    NUMREMARCADOR = obj.numremarcador;

                    $('#cont-last-lectura').html("");
                    $('#cont-lectura-manual').html("");

                    $('#cont-last-lectura').html(obj.tabla);
                    $('#cont-lectura-manual').html(obj.boton);
                    $('#select-instalacion').attr("disabled", "disabled");
                    $('#select-parque').attr("disabled", "disabled");
                    $('#select-empalme').attr("disabled", "disabled");
                    $('#select-remarcador').attr("disabled", "disabled");
                    $('#fecha').attr("disabled", "disabled");
                    $('#btn-buscar').attr("disabled", "disabled");
                }
            },
            error: function (a, b, c) {
                console.log(a);
                console.log(b);
                console.log(c);
            }
        });
    } else {
        alert("Debe ingresar todos los campos para buscar.");
    }

}

function insertar() {
    if (validarCampos()) {
        var lectura = $('#num-lectura-manual').val();
        if (parseInt(lectura) <= 0) {
            alert("Debe ingresar un valor mayor que cero en la lectura manual.");
            return false;
        }
        if (lectura === "" || lectura.length < 1) {
            alert("Debe ingresar un número válido");
            return false;
        }
        var idremarcador = $('#select-remarcador').val();
        var mes = $('#fecha').val().split("-")[1];
        var anio = $('#fecha').val().split("-")[0];

        if (parseInt(lectura) < ENERGIA) {
            alert("El valor de la lectura no puede ser menor a la última lectura registrada en el remarcador para el mes seleccionado.");
            return false;
        }

        if (confirm("Se ingresará el valor " + parseInt(lectura) + " para el remarcador ID: " + NUMREMARCADOR + ", para el mes de " + mesNumeroAPalabraLarga(mes) + " del año " + anio + ". Está seguro de que desea continuar?")) {
            var datos = {
                tipo: 'ins-lectura-manual',
                idremarcador: idremarcador,
                mes: mes,
                anio: anio,
                lectura: parseInt(lectura)
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
                        alert("Registro insertado correctamente.");
                        limpiar();
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
}

function limpiar() {
    $('#select-instalacion').removeAttr("disabled");
    getSelectInstalacion();
    $('#select-parque').html("");
    $('#select-parque').removeAttr("disabled");
    $('#select-empalme').html("");
    $('#select-empalme').removeAttr("disabled");
    $('#select-remarcador').html("");
    $('#select-remarcador').removeAttr("disabled");
    $('#fecha').val("");
    $('#fecha').removeAttr("disabled");
    $('#btn-buscar').removeAttr("disabled");
    $('#cont-last-lectura').html("");
    $('#cont-lectura-manual').html("");
}