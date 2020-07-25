var ID_CONTACTO_EDICION = null;

function getSelectClientes() {
    var datos = {
        tipo: 'get-select-clientes-nombre'
    };

    $.ajax({
        url: 'ClienteController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (resp) {
            var obj = JSON.parse(resp);
            if (obj.estado === 'ok') {
                $('#select-cliente').html(obj.options);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function getContactos() {
    var datos = {
        tipo: 'get-contactos'
    };
    $.ajax({
        url: 'ContactoController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (res) {
            var obj = JSON.parse(res);
            if (obj.estado === 'ok') {
                $('#tabla-contactos').html(obj.tabla);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function insContacto(){
    var idcliente = $('#select-cliente').val();
    var persona = $('#persona').val();
    var cargo = $('#cargo').val();
    var fono = $('#fono').val();
    var email = $('#email').val();
    
    var datos = {
        tipo: 'ins-contacto',
        idcliente: idcliente,
        eprsona: persona,
        cargo:cargo,
        fono: fono,
        email: email
    };
    
    $.ajax({
        url: 'ContactoController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (res) {
            var obj = JSON.parse(res);
            if (obj.estado === 'ok') {
                getContactos();
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function activarEdicion(boton) {
    var fila = $(boton).parent().parent();
    var idcontacto = $(fila).children(0).children(0).val();
    var datos = {
        tipo: 'get-contacto-idcontacto',
        idcontacto: idcontacto
    };

    $.ajax({
        url: 'ContactoController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (res) {
            var obj = JSON.parse(res);
            if (obj.estado === 'ok') {
                armarContacto(obj.contacto); 
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function armarContacto(contacto) {
    ID_CONTACTO_EDICION = contacto.idcontacto;
    $('#btn-insert').attr("hidden", "hidden");
    
    $('#select-cliente').val(contacto.idcliente);
    $('#persona').val(contacto.persona);
    $('#cargo').val(contacto.cargo);
    $('#fono').val(contacto.fono);
    $('#email').val(contacto.email);
    
    $('#btn-guardar').removeAttr("hidden");
}

function limpiar(){
    ID_CONTACTO_EDICION = null;
    getSelectClientes();
    $('#persona').val('');
    $('#cargo').val('');
    $('#fono').val('');
    $('#email').val('');
    
    $('#btn-guardar').attr("hidden", "hidden");
    $('#btn-insert').removeAttr("hidden");
}