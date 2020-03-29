<script src="modulos/parque.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        getSelectFase();
        getParques();
    });

</script>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-12">
            <div class="page-header">
                <h1>Parques<small> y sus Fases</small></h1>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-3">
            <form class="form small"  role="form">
                <div class="form-group">
                    <label for="select-fase">Fase</label>
                    <select class="form-control-sm form-control small" id="select-fase">
                    </select>
                </div>
                <div class="form-group">
                    <label for="nom-parque" >Nombre Parque</label>
                    <input type="text" class="form-control form-control-sm small" maxlength="200" id="nom-parque" />
                </div>
                <div class="form-group">
                    <button onclick="insParque(getParques);" type="button" class="btn btn-primary btn-sm" id="btn-insert">Insertar</button>
                </div>
            </form>
        </div>

        <div class="col-sm-7">
            <table id="tabla-parques" class="table table-condensed table-borderless table-striped table-hover table-sm small">
                <thead>
                    <tr>
                        <th>Parque</th>
                        <th>Fase</th>
                        <th>Acción</th>
                    </tr>
                </thead>
                <tbody>

                </tbody>
            </table>
        </div>
    </div>
</div>