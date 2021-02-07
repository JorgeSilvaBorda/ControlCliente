<script src="modulos/hist-remarcadores-baja.js?=<% out.print(modelo.Util.generaRandom(10000, 99999));%>" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        getRemarcadoresBaja();
    });

</script>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-12">
            <div class="page-header">
                <h1>Remarcadores dados de baja</h1>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-9">
            <table id="tabla-remarcadores-baja" class="table table-condensed table-borderless table-striped table-hover table-sm small">
                <thead>
                    <tr>
                        <th>ID<br />Remarcador</th>
                        <th>Nº<br />Serie</th>
                        <th>#<br />Empalme</th>
                        <th>Bodega</th>
                        <th>Módulos</th>
                        <th>Instalacion</th>
                        <th>Fecha<br />Baja</th>
                        <th>Fecha<br />última lectura<br />válida</th>
                        <th>Usuario<br />baja</th>
                    </tr>
                </thead>
                <tbody>

                </tbody>
            </table>

        </div>
    </div>
</div>