<script src="modulos/conversor.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        getConversores();
    });

</script>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-12">
            <div class="page-header">
                <h1>Conversores<small> y Remarcadores</small></h1>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-2">
            <form class="form small"  role="form">
                <div class="form-group">
                    <label for="ip-conversor" >IP Conversor</label>
                    <input type="text" class="form-control form-control-sm small" maxlength="15" id="ip-conversor" />
                </div>
                <div class="form-group">
                    <label for="num-conversor" >Num. Conversor</label>
                    <input type="number" class="form-control form-control-sm small" id="num-conversor" />
                </div>
                <div class="form-group">
                    <label for="num-puerto" >Puerto</label>
                    <input type="number" class="form-control form-control-sm small" id="num-puerto" />
                </div>
                <div class="form-group">
                    <label for="num-remarcador" >ID Remarcador</label>
                    <input type="text" class="form-control form-control-sm small" id="num-remarcador" />
                </div>
                <div class="form-group">
                    <label for="marca" >Tipo Remarcador</label>
                    <select class="form-control form-control-sm" id="select-tipo-remarcador">
                        <option>circutorcvmc10</option>
                        <option>schneiderpm710</option>
                        <option>schneiderpm5300</option>
                    </select>
                </div>
                <div class="form-group">
                    <button onclick="insRemarcador(getRemarcadores);" type="button" class="btn btn-primary btn-sm" id="btn-insert">Insertar</button>
                    <button onclick="saveRemarcador(getRemarcadores);" hidden="hidden" type="button" class="btn btn-secondary btn-sm" id="btn-guardar">Guardar</button>
                    <button onclick="limpiar();" type="button" class="btn btn-default btn-sm" id="btn-limpiar">Limpiar</button>
                </div>
            </form>
        </div>

        <div class="col-sm-8">
            <table id="tabla-conversores" class="table table-condensed table-borderless table-striped table-hover table-sm small">
                <thead>
                    <tr>
                        <th>IP Conversor</th>
                        <th>Num. Conversor</th>
                        <th>Num. Puerto</th>
                        <th>ID Remarcador</th>
                        <th>Tipo Remarcador</th>
                        <th>Acción</th>
                    </tr>
                </thead>
                <tbody>

                </tbody>
            </table>
        </div>
    </div>
</div>