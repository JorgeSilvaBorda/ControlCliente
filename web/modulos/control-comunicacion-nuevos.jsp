<script src="modulos/control-comunicacion.js?=<% out.print(modelo.Util.generaRandom(10000, 99999));%>" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        getEventosNuevosComunicacion();
    });

</script>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-12">
            <div class="page-header">
                <h3>Nuevos eventos</h3>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-12">
            <button hidden='hidden' onclick='marcarTodos();' id='btn-marcar-todos' type='button' class='btn btn-sm btn-warning float-right'>Marcar todos leídos</button>
            <br />
            <table style='font-size: 12px;' id='tabla-eventos-comunicacion' class='table table-condensed table-borderless table-striped table-hover table-sm small'>
                <thead>
                    <tr>
                        <th>Fecha</th>
                        <th>Hora</th>
                        <th>ID Remarcador</th>
                        <th>Bodega</th>
                        <th>Instalación</th>
                        <th>Cliente</th>
                        <th>Acción</th>
                    </tr>
                </thead>
                <tbody>

                </tbody>
            </table>
        </div>
    </div>
</div>