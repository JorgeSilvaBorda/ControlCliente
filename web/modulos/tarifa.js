var TARIFA_EDICION = null;
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

function buscar() {
    var idtarifa = $('#select-tarifa').val();
    var datos = {
        tipo: 'get-detalle-tarifa',
        idtarifa: idtarifa
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
                pintarTarifa(obj.tarifa);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function pintarTarifa(tarifa) {
    TARIFA_EDICION = tarifa;
    $('#nom-tarifa').val(tarifa.nomtarifa);
    $('#select-comuna').val(tarifa.idcomuna);

    for (var i in tarifa.conceptos) {
        var concepto = tarifa.conceptos[i];
        console.log(concepto.nomconcepto);
        switch (concepto.idconcepto) {
            case 1:
                $('#cargo-fijo').val(concepto.valorneto);
                break;
            case 2:
                $('#cargo-servicio-publico').val(concepto.valorneto);
                break;
            case 3:
                $('#transporte-electricidad').val(concepto.valorneto);
                break;
            case 4:
                $('#cargo-energia').val(concepto.valorneto);
                break;
            case 5:
                $('#cdmplhp-btaa').val(concepto.valorneto);
                break;
            case 6:
                $('#cdmplhp-btas').val(concepto.valorneto);
                break;
            case 7:
                $('#cdmplhp-btsa').val(concepto.valorneto);
                break;
            case 8:
                $('#cdmplhp-btss').val(concepto.valorneto);
                break;
            case 9:
                $('#cdmps-btaa').val(concepto.valorneto);
                break;
            case 10:
                $('#cdmps-btas').val(concepto.valorneto);
                break;
            case 11:
                $('#cdmps-btsa').val(concepto.valorneto);
                break;
            case 12:
                $('#cdmps-btss').val(concepto.valorneto);
                break;
        }
    }
    $('#btn-save').show();
    $('#btn-insert').hide();
}

function validarCampos() {
    var nomtarifa = $('#nom-tarifa').val();
    var idcomuna = $('#select-comuna').val();
    var cargofijo = $('#cargo-fijo').val();
    var cargoserviciopublico = $('#cargo-servicio-publico').val();
    var transporteelectricidad = $('#transporte-electricidad').val();
    var cargoenergia = $('#cargo-energia').val();
    var cdmplhpbtaa = $('#cdmplhp-btaa').val();
    var cdmplhpbtsa = $('#cdmplhp-btsa').val();
    var cdmplhpbtas = $('#cdmplhp-btas').val();
    var cdmplhpbtss = $('#cdmplhp-btss').val();
    var cdmpsbtaa = $('#cdmps-btaa').val();
    var cdmpsbtsa = $('#cdmps-btsa').val();
    var cdmpsbtas = $('#cdmps-btas').val();
    var cdmpsbtss = $('#cdmps-btss').val();

    if (nomtarifa.length < 2) {
        alert("Debe indicar un nombre de tarifa válido.");
        return false;
    }
    if (idcomuna === '0') {
        alert("Debe seleccionar una comuna del listado para la tarifa.");
        return false;
    }
    if (parseInt(cargofijo) < 0 || isNaN(cargofijo) || cargofijo.length < 1) {
        alert("Debe indicar un valor neto válido en Cargo Fijo.");
        return false;
    }
    if (parseInt(cargoserviciopublico) < 0 || isNaN(cargoserviciopublico) || cargoserviciopublico.length < 1) {
        alert("Debe indicar un valor neto válido en Cargo por Servicio Público.");
        return false;
    }
    if (parseInt(transporteelectricidad) < 0 || isNaN(transporteelectricidad) || transporteelectricidad.length < 1) {
        alert("Debe indicar un valor neto válido en Transporte de Electricidad.");
        return false;
    }
    if (parseInt(cargoenergia) < 0 || isNaN(cargoenergia) || cargoenergia.length < 1) {
        alert("Debe indicar un valor neto válido en Cargo por Energía.");
        return false;
    }
    if (parseInt(cdmplhpbtaa) < 0 || isNaN(cdmplhpbtaa) || cdmplhpbtaa.length < 1) {
        alert("Debe indicar un valor neto válido en Cargo por Demanda Máxima de Potencia Leída en Horas de Punta BT_AA.");
        return false;
    }
    if (parseInt(cdmplhpbtsa) < 0 || isNaN(cdmplhpbtsa) || cdmplhpbtsa.length < 1) {
        alert("Debe indicar un valor neto válido en Cargo por Demanda Máxima de Potencia Leída en Horas de Punta BT_SA.");
        return false;
    }
    if (parseInt(cdmplhpbtas) < 0 || isNaN(cdmplhpbtas) || cdmplhpbtas.length < 1) {
        alert("Debe indicar un valor neto válido en Cargo por Demanda Máxima de Potencia Leída en Horas de Punta BT_AS.");
        return false;
    }
    if (parseInt(cdmplhpbtss) < 0 || isNaN(cdmplhpbtss) || cdmplhpbtss.length < 1) {
        alert("Debe indicar un valor neto válido en Cargo por Demanda Máxima de Potencia Leída en Horas de Punta BT_SS.");
        return false;
    }
    if (parseInt(cdmpsbtaa) < 0 || isNaN(cdmpsbtaa) || cdmpsbtaa.length < 1) {
        alert("Debe indicar un valor neto válido en Cargo por Demanda Máxima de Potencia Suministrada BT_AA.");
        return false;
    }
    if (parseInt(cdmpsbtas) < 0 || isNaN(cdmpsbtas) || cdmpsbtas.length < 1) {
        alert("Debe indicar un valor neto válido en Cargo por Demanda Máxima de Potencia Suministrada BT_AS.");
        return false;
    }
    if (parseInt(cdmpsbtsa) < 0 || isNaN(cdmpsbtsa) || cdmpsbtsa.length < 1) {
        alert("Debe indicar un valor neto válido en Cargo por Demanda Máxima de Potencia Suministrada BT_SA.");
        return false;
    }
    if (parseInt(cdmpsbtss) < 0 || isNaN(cdmpsbtss) || cdmpsbtss.length < 1) {
        alert("Debe indicar un valor neto válido en Cargo por Demanda Máxima de Potencia Suministrada BT_SS.");
        return false;
    }
    return true;
}

function insTarifa() {
    if (validarCampos()) {
        var tarifa = {
            nomtarifa: $('#nom-tarifa').val(),
            idcomuna: $('#select-comuna').val(),
            cargofijo: $('#cargo-fijo').val(),
            cargoserviciopublico: $('#cargo-servicio-publico').val(),
            transporteelectricidad: $('#transporte-electricidad').val(),
            cargoenergia: $('#cargo-energia').val(),
            cdmplhpbtaa: $('#cdmplhp-btaa').val(),
            cdmplhpbtsa: $('#cdmplhp-btsa').val(),
            cdmplhpbtas: $('#cdmplhp-btas').val(),
            cdmplhpbtss: $('#cdmplhp-btss').val(),
            cdmpsbtaa: $('#cdmps-btaa').val(),
            cdmpsbtsa: $('#cdmps-btsa').val(),
            cdmpsbtas: $('#cdmps-btas').val(),
            cdmpsbtss: $('#cdmps-btss').val()
        };

        var datos = {
            tipo: 'ins-tarifa',
            tarifa: tarifa
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
                    alert("Tarifa insertada exitosamente!");
                    limpiar();
                }else if(obj.estado === 'existe'){
                    alert("La tarifa que intenta ingresar ya existe. Por favor utilice la función editar.");
                    return false;
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

function saveTarifa() {
    if (validarCampos()) {
        var tarifa = {
            nomtarifa: $('#nom-tarifa').val(),
            idcomuna: $('#select-comuna').val(),
            cargofijo: $('#cargo-fijo').val(),
            cargoserviciopublico: $('#cargo-servicio-publico').val(),
            transporteelectricidad: $('#transporte-electricidad').val(),
            cargoenergia: $('#cargo-energia').val(),
            cdmplhpbtaa: $('#cdmplhp-btaa').val(),
            cdmplhpbtsa: $('#cdmplhp-btsa').val(),
            cdmplhpbtas: $('#cdmplhp-btas').val(),
            cdmplhpbtss: $('#cdmplhp-btss').val(),
            cdmpsbtaa: $('#cdmps-btaa').val(),
            cdmpsbtsa: $('#cdmps-btsa').val(),
            cdmpsbtas: $('#cdmps-btas').val(),
            cdmpsbtss: $('#cdmps-btss').val()
        };

        var datos = {
            tipo: 'upd-tarifa',
            newtarifa: tarifa,
            tarifa: TARIFA_EDICION
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
                    alert("Tarifa guardada exitosamente!");
                    limpiar();
                }else if(obj.estado === 'existe'){
                    alert("Los datos que intenta cambiar a la tarifa ya se encuentran en uso por otra.");
                    return false;
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

function limpiar() {
    $('#btn-insert').show();
    $('#btn-save').hide();
    $('input').val('');
    $('select').val('0');
    getSelectTarifas();
    getSelectComunas();
    TARIFA_EDICION = null;
}