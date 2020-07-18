<script src="modulos/remarcador.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        getSelectInstalacion();
        getRemarcadores();
    });

</script>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-12">
            <div class="page-header">
                <h1>Remarcadores<small> y Ubicaciones</small></h1>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-3">
            <form class="form small"  role="form">
                <div class="form-group">
                    <label for="select-instalación">Instalación</label>
                    <select onchange="getSelectBodegaIdInstalacion($(this).val());" class="form-control-sm form-control small" id="select-instalacion">
                    </select>
                </div>
                <div class="form-group">
                    <label for="select-parque">Bodega</label>
                    <select onchange="getSelectEmpalmeIdParque($(this).val());"class="form-control-sm form-control small" id="select-parque">
                    </select>
                </div>
                <div class="form-group">
                    <label for="select-empalme">Empalme</label>
                    <select class="form-control-sm form-control small" id="select-empalme">
                    </select>
                </div>
                <div class="form-group">
                    <label for="num-remarcador" >ID Remarcador</label>
                    <input type="text" class="form-control form-control-sm small" maxlength="11" id="num-remarcador" />
                </div>
                <div class="form-group">
                    <label for="num-serie" >Nº Serie Equipo</label>
                    <input type="text" class="form-control form-control-sm small" maxlength="50" id="num-serie" />
                </div>
                <div class="form-group">
                    <label for="modulos" >Módulos</label>
                    <input type="text" class="form-control form-control-sm small" maxlength="11" id="modulos" />
                </div>
                <div class="form-group">
                    <button onclick="insRemarcador(getRemarcadores);" type="button" class="btn btn-primary btn-sm" id="btn-insert">Insertar</button>
                    <button onclick="saveRemarcador(getRemarcadores);" hidden="hidden" type="button" class="btn btn-secondary btn-sm" id="btn-guardar">Guardar</button>
                    <button onclick="limpiar();" type="button" class="btn btn-default btn-sm" id="btn-limpiar">Limpiar</button>
                </div>
            </form>
        </div>

        <div class="col-sm-7">
            <table id="tabla-remarcadores" class="table table-condensed table-borderless table-striped table-hover table-sm small">
                <thead>
                    <tr>
                        <th>ID Remarcador</th>
                        <th>Nº Serie</th>
                        <th># Empalme</th>
                        <th>Bodega</th>
                        <th>Módulos</th>
                        <th>Acción</th>
                    </tr>
                </thead>
                <tbody>

                </tbody>
            </table>
        </div>
    </div>
</div>