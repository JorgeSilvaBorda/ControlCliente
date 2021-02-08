<script src="modulos/hist-asignacion-remarcadores.js?=<% out.print(modelo.Util.generaRandom(10000, 99999));%>" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        getHistoricoAsignaciones();
    });

</script>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-12">
            <div class="page-header">
                <h1>Histórico de asignaciones</h1>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-12">
            <table id="tabla-hist-asignacion-remarcadores" class="table table-condensed table-borderless table-striped table-hover table-sm small">
                <thead>
                    <tr>
                        <th>ID<br />Remarcador</th>
                        <th>Nº<br />Serie</th>
                        <th>#<br />Empalme</th>
                        <th>Bodega</th>
                        <th>Módulos</th>
                        <th>Instalacion</th>
                        <th>Cliente</th>
                        <th>Fecha<br />Asignación</th>
                        <th>Hora<br />ASignación</th>
                        <th>Fecha<br />última lectura<br />válida</th>
                        <th>Hora<br />última lectura<br />válida</th>
                        <th>Usuario</th>
                    </tr>
                </thead>
                <tbody>

                </tbody>
            </table>

        </div>
    </div>
</div>