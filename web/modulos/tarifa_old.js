var ID_TARIFA_EDICION = null;
function getTarifas() {
    var datos = {
        tipo: 'get-tarifas'
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
                $('#tabla-tarifas.dataTable').DataTable().destroy();
                $('#tabla-tarifas tbody').html(obj.tabla);
                $('#tabla-tarifas').DataTable(OPCIONES_DATATABLES);
                getSelectTarifas();
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function getTarifasConceptos() {
    var datos = {
        tipo: 'get-tarifas-conceptos'
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
                $('#tabla-tarifas-conceptos.dataTable').DataTable().destroy();
                $('#tabla-tarifas-conceptos tbody').html('');
                $('#tabla-tarifas-conceptos tbody').html(obj.tabla);
                $('#tabla-tarifas-conceptos').DataTable(OPCIONES_DATATABLES);
                getSelectTarifas();
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function getSelectTarifas() {
    var datos = {
        tipo: 'get-select-tarifas'
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
                $('#select-tarifa').html(obj.options);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function getSelectComunas() {
    var datos = {
        tipo: 'get-select-comunas'
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
                $('#select-comuna').html(obj.options);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function insTarifa(callback) {
    var nomtarifa = $('#nom-tarifa').val();
    var datos = {
        tipo: 'ins-tarifa',
        nomtarifa: nomtarifa
    };
    $.ajax({
        url: 'TarifaController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (res) {
            var obj = JSON.parse(res);
            if (obj.estado === 'ok') {
                limpiar();
                callback();
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function validarCamposTarifaConcepto() {
    var idtarifa = $('#select-tarifa').val();
    var idcomuna = $('#select-comuna').val();
    var nomconcepto = $('#nomconcepto').val();
    var umedida = $('#umedida').val();
    var valorneto = $('#valorneto').val();

    if (idtarifa === '0') {
        alert("Debe seleccionar una tarifa.");
        return false;
    }
    if (idcomuna === '0') {
        alert("Debe seleccionar una comuna.");
        return false;
    }
    if (nomconcepto.length < 2) {
        alert("Debe indicar un nombre de concepto válido (Por lo menos 2 caracteres)).");
        return false;
    }

    if (umedida.length < 1) {
        alert("Debe indicar una unidad de medida válida (Mínimo 1 caracter).");
        return false;
    }
    if ($('#check-red').prop("checked")) {
        var btaa = $('#btaa').val();
        var btas = $('#btas').val();
        var btsa = $('#btsa').val();
        var btss = $('#btss').val();
        if (btaa <= 0 ||
                btas <= 0 ||
                btsa <= 0 ||
                btss <= 0 ||
                btaa === '' ||
                btas === '' ||
                btsa === '' ||
                btss === '' ||
                isNaN(btaa) ||
                isNaN(btas) ||
                isNaN(btsa) ||
                isNaN(btss)) {
            alert("Debe completar el campo de valor para todas las redes.");
            return false;
        }
    } else {
        if (valorneto === '' || valorneto.length < 1 || isNaN(valorneto)) {
            alert("Debe indicar un Valor Neto válido.");
            return false;
        }
    }
    return true;
}

function insTarifaConcepto(callback) {
    if (validarCamposTarifaConcepto()) {
        var idtarifa = $('#select-tarifa').val();
        var idcomuna = $('#select-comuna').val();
        var nomconcepto = $('#nomconcepto').val();
        var umedida = $('#umedida').val();
        var valorneto = $('#valorneto').val();
        var redes = {
            incluyered: 0,
            btaa: 0,
            btas: 0,
            btsa: 0,
            btss: 0
        };
        if ($('#check-red').prop("checked")) {
            redes.incluyered = 1;
            redes.btaa = $('#btaa').val();
            redes.btas = $('#btas').val();
            redes.btsa = $('#btsa').val();
            redes.btss = $('#btss').val();
            valorneto = 0;
        }
        var datos = {
            tipo: 'ins-tarifa-concepto',
            idtarifa: idtarifa,
            idcomuna: idcomuna,
            nomconcepto: nomconcepto,
            umedida: umedida,
            valorneto: valorneto,
            redes: redes
        };
        $.ajax({
            url: 'TarifaController',
            type: 'post',
            data: {
                datos: JSON.stringify(datos)
            },
            success: function (res) {
                var obj = JSON.parse(res);
                if (obj.estado === 'ok') {
                    limpiar();
                    callback();
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

function activarEdicion(boton) {
    var fila = $(boton).parent().parent();
    var idtarifa = $(fila).children(0).children(0).val();
    var datos = {
        tipo: 'get-tarifa-idtarifa',
        idtarifa: idtarifa
    };
    $.ajax({
        url: 'TarifaController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (res) {
            var obj = JSON.parse(res);
            if (obj.estado === 'ok') {
                armarTarifa(obj.tarifa);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function activarEdicionConcepto(boton) {
    var fila = $(boton).parent().parent();
    var idconcepto = $(fila).children(0).children(0).val();
    var datos = {
        tipo: 'get-concepto-idconcepto',
        idconcepto: idconcepto
    };
    $.ajax({
        url: 'TarifaController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (res) {
            var obj = JSON.parse(res);
            if (obj.estado === 'ok') {
                armarConcepto(obj.concepto);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function armarTarifa(tarifa) {
    ID_TARIFA_EDICION = tarifa.idtarifa;
    $('#nom-tarifa').val(tarifa.nomtarifa);
    $('#btn-insert').attr("hidden", "hidden");
    $('#btn-guardar').removeAttr("hidden");
}

function armarConcepto(concepto) {
    ID_CONCEPTO_EDICION = concepto.idconcepto;
    $('#select-tarifa').val(concepto.idtarifa);
    $('#nomconcepto').val(concepto.nomconcepto);
    $('#select-comuna').val(concepto.idcomuna);
    $('#umedida').val(concepto.umedida);
    $('#valorneto').val(concepto.valorneto);
    
    $('#btn-ins-tarifa-concepto').attr("hidden", "hidden");
    $('#btn-save-tarifa-concepto').removeAttr("hidden");
    
    if(concepto.incluyered === '1' || concepto.incluyered === 1){
        $('#check-red').prop("checked", "checked"); 
        $('#btaa').val(concepto.btaa);
        $('#btas').val(concepto.btas);
        $('#btsa').val(concepto.btsa);
        $('#btss').val(concepto.btss);
        toggleRed();
    }else{
        $('#check-red').prop("checked", false);
        toggleRed();
    }
    
    
}

function saveTarifa(callback) {
    var nomtarifa = $('#nom-tarifa').val();
    var datos = {
        tipo: 'upd-tarifa',
        tarifa: {
            idtarifa: ID_TARIFA_EDICION,
            nomtarifa: nomtarifa
        }
    };
    $.ajax({
        url: 'TarifaController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (res) {
            var obj = JSON.parse(res);
            if (obj.estado === 'ok') {
                limpiar();
                callback();
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function saveConcepto(callback) {
    console.log("Entra a save");
    if (validarCamposTarifaConcepto()) {
        console.log("Campos validados");
        var idtarifa = $('#select-tarifa').val();
        var idcomuna = $('#select-comuna').val();
        var nomconcepto = $('#nomconcepto').val();
        var umedida = $('#umedida').val();
        var valorneto = $('#valorneto').val();
        var redes = {
            incluyered: 0,
            btaa: 0,
            btas: 0,
            btsa: 0,
            btss: 0
        };
        if ($('#check-red').prop("checked")) {
            redes.incluyered = 1;
            redes.btaa = $('#btaa').val();
            redes.btas = $('#btas').val();
            redes.btsa = $('#btsa').val();
            redes.btss = $('#btss').val();
            valorneto = 0;
        }
        var datos = {
            tipo: 'upd-concepto',
            concepto: {
                idconcepto: ID_CONCEPTO_EDICION,
                idtarifa: idtarifa,
                idcomuna: idcomuna,
                nomconcepto: nomconcepto,
                umedida: umedida,
                valorneto: valorneto,
                redes: redes
            }
        };
        $.ajax({
            url: 'TarifaController',
            type: 'post',
            data: {
                datos: JSON.stringify(datos)
            },
            success: function (res) {
                var obj = JSON.parse(res);
                if (obj.estado === 'ok') {
                    limpiar();
                    callback();
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

function eliminarConcepto(boton) {
    var fila = $(boton).parent().parent();
    var idconcepto = $(fila).children(0).children(0).val();
    var datos = {
        tipo: 'del-concepto',
        idconcepto: idconcepto
    };
    if (confirm("Está seguro que desea eliminar el concepto?")) {
        $.ajax({
            url: 'TarifaController',
            type: 'post',
            data: {
                datos: JSON.stringify(datos)
            },
            success: function (res) {
                var obj = JSON.parse(res);
                if (obj.estado === 'ok') {
                    getTarifasConceptos();
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

function toggleRed() {
    if ($('#check-red').prop("checked")) {
        $('.red-oculto').show();
        $('#valorneto').attr("disabled", "disabled");
    } else {
        $('.red-oculto').hide();
        $('#valorneto').removeAttr("disabled");
    }
}

function limpiar() {
    ID_TARIFA_EDICION = null;
    ID_CONCEPTO_EDICION = null;
    $('#nom-tarifa').val('');
    $('#valor-tarifa').val('');
    $('#select-tarifa').val('0');
    $('#select-comuna').val('0');
    $('#nomconcepto').val('');
    $('#umedida').val('');
    $('#valorneto').val('');
    $('#btn-guardar').attr("hidden", "hidden");
    $('#btn-insert').removeAttr("hidden");
    $('#btn-ins-tarifa-concepto').removeAttr("hidden");
    $('#btn-save-tarifa-concepto').attr("hidden", "hidden");
    $('.red-oculto').hide();
    $('#check-red').prop("checked", false, );
}