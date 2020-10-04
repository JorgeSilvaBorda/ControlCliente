<script src="modulos/control-comunicacion.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        getEventosComunicacion();
    });

</script>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-12">
            <div class="page-header">
                <h1>Registro Eventos<small> de comunicación remarcadores</small></h1>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-12">
            
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