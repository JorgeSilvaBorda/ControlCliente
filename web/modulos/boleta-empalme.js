var KWTOTAL = null;

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

function getRemarcadoresNumEmpalmeBoleta() {
    //Función trabaja sólo con el número de empalme.
    //En BD, el mismo numero de empalme puede tener ID distintos para efectos de asigrnación a las bodegas
    
    var numempalme = $('#select-empalme option:selected').text();
    var mes = $('#mes').val();
    var datos = {
        tipo: 'get-remarcadores-numempalme-boleta',
        numempalme: numempalme,
        mes: mes
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
                KWTOTAL = obj.kwtotal;
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
    var idinstalacion = $('#select-instalacion').val();
    var idempalme = $('#select-empalme').val();
    var mes = $('#mes').val();

    if (idinstalacion === '0' || idinstalacion === '' || idinstalacion === null || idinstalacion === undefined || idinstalacion === 0) {
        alert("Debe seleccionar una Instalación del listado.");
        return false;
    }
    if (idempalme === '0' || idempalme === '' || idempalme === null || idempalme === undefined || idempalme === 0) {
        alert("Debe seleccionar un Empalme del listado.");
        return false;
    }
    if(mes.length < 6){
        alert("El mes indicado es incorrecto.");
        return false;
    }
    return true;
}

function calcularDiferencia(){
    var kwtotal = KWTOTAL;
    var facturadoempalme = $('#consumo-facturado-empalme').val();
    
    
    var resta = kwtotal - facturadoempalme;
    var porc = 100 - ((facturadoempalme * 100) / kwtotal);
    
    resta = resta * -1;
    porc = porc * -1;
    
    $('#kw-diferencia').html(parseInt(resta) + " kW");
    $('#porc-diferencia').html(porc.toFixed(4) + " %");
}

function buscar() {
    if (validarCampos()) {
        $('#btn-buscar').attr("disabled", "disabled");
        $('.loader').fadeIn(500);
        //$('.loader').fadeIn(500);
        getRemarcadoresNumEmpalmeBoleta();
    }
}

function calcular(idremarcador, numremarcador, numserie,consumo, mes, lecturaanterior, lecturaactual, fechalecturaini, fechalecturafin){
    //console.log("modulos/boleta-empalme/mask-boleta-empalme.jsp?idremarcador=" + idremarcador + "&numremarcador=" + numremarcador + "&consumo=" + consumo + "&mes='" + mes + "'&lecturaanterior=" + lecturaanterior + "&lecturaactual=" + lecturaactual + "&fechalecturaini='" + fechalecturaini + "'&fechalecturafin='" + fechalecturafin + "'");
    $('#modal').modal();
    $('#modal-body').load("modulos/boleta-empalme/mask-boleta-empalme.jsp?idremarcador=" + idremarcador + "&numremarcador=" + numremarcador + "&numserie='" + numserie + "'&consumo=" + consumo + "&mes='" + mes + "'&lecturaanterior=" + lecturaanterior + "&lecturaactual=" + lecturaactual + "&fechalecturaini='" + fechalecturaini + "'&fechalecturafin='" + fechalecturafin + "'");
}

function limpiar(){
    KWTOTAL = null;
}