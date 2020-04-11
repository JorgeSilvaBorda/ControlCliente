<script src="modulos/parque.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        getSelectInstalacion();
        getParques();
    });

</script>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-12">
            <div class="page-header">
                <h1>Bodegas<small> y sus Instalaciones</small></h1> <!-- Bodega como cncepto -->
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-3">
            <form class="form small"  role="form">
                <div class="form-group">
                    <label for="select-instalacion">Instalación</label>
                    <select class="form-control-sm form-control small" id="select-instalacion">
                    </select>
                </div>
                <div class="form-group">
                    <label for="nom-parque" >Nombre Bodega</label>
                    <input type="text" class="form-control form-control-sm small" maxlength="200" id="nom-parque" />
                </div>
                <div class="form-group">
                    <button onclick="insParque(getParques);" type="button" class="btn btn-primary btn-sm" id="btn-insert">Insertar</button>
                    <button onclick="saveParque(getParques);" hidden="hidden" type="button" class="btn btn-secondary btn-sm" id="btn-guardar">Guardar</button>
                    <button onclick="limpiar();" type="button" class="btn btn-default btn-sm" id="btn-limpiar">Limpiar</button>
                </div>
            </form>
        </div>

        <div class="col-sm-7">
            <table id="tabla-parques" class="table table-condensed table-borderless table-striped table-hover table-sm small">
                <thead>
                    <tr>
                        <th>Bodega</th>
                        <th>Instalación</th>
                        <th>Acción</th>
                    </tr>
                </thead>
                <tbody>

                </tbody>
            </table>
        </div>
    </div>
</div>