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
                <h1>Hist�rico de asignaciones</h1>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-12">
            <table id="tabla-hist-asignacion-remarcadores" class="table table-condensed table-borderless table-striped table-hover table-sm small">
                <thead>
                    <tr>
                        <th>ID<br />Remarcador</th>
                        <th>N�<br />Serie</th>
                        <th>#<br />Empalme</th>
                        <th>Bodega</th>
                        <th>M�dulos</th>
                        <th>Instalacion</th>
                        <th>Cliente</th>
                        <th>Fecha<br />Asignaci�n</th>
                        <th>Hora<br />ASignaci�n</th>
                        <th>Fecha<br />�ltima lectura<br />v�lida</th>
                        <th>Hora<br />�ltima lectura<br />v�lida</th>
                        <th>Usuario</th>
                    </tr>
                </thead>
                <tbody>

                </tbody>
            </table>

        </div>
    </div>
</div>