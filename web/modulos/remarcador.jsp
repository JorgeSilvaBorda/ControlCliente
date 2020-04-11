<script src="modulos/remarcador.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        getSelectEmpalme();
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
                    <label for="select-empalme">Empalme</label>
                    <select onchange="getSelectParqueEmpalme($(this).val());" class="form-control-sm form-control small" id="select-empalme">
                    </select>
                </div>
                <div class="form-group">
                    <label for="select-parque">Parque</label>
                    <select class="form-control-sm form-control small" id="select-parque">
                    </select>
                </div>
                <div class="form-group"><!-- Cambiar a ID Remarcador -->
                    <label for="num-remarcador" >N�mero Remarcador</label>
                    <input type="text" class="form-control form-control-sm small" maxlength="11" id="num-remarcador" />
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
                        <th># Remarcador</th> <!-- Cambiar a ID Remarcador -->
                        <th># Empalme</th>
                        <th>Parque</th>
                        <th>Acci�n</th>
                    </tr>
                </thead>
                <tbody>

                </tbody>
            </table>
        </div>
    </div>
</div>