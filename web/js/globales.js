var OPCIONES_DATATABLES = {
    dom: 'Bfrtip',
    buttons: [],
    "language": {
        "lengthMenu": "Mostrar _MENU_ registros por página",
        "zeroRecords": "Nada encontrado",
        "info": "Mostrando página _PAGE_ de _PAGES_",
        "infoEmpty": "No hay registros disponibles",
        "infoFiltered": "(filtrado de _MAX_ registros totales)",
        "paginate": {
            "previous": "Anterior",
            "next": "Siguiente"
        },
        "search": "Buscar"
    },
    "paging": true,
    "ordering": true,
    "drawCallback": function () {
        $('.dataTables_paginate > .pagination').addClass('pagination-sm');
    }
};