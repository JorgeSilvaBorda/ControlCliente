<script src="modulos/empalme.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        getSelectInstalacion();
        getEmpalmes();
    });

</script>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-12">
            <div class="page-header">
                <h1>Empalmes<small> y sus Instalaciones</small></h1>
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
                    <label for="num-empalme" >Número Empalme</label>
                    <input type="text" class="form-control form-control-sm small" maxlength="200" id="num-empalme" />
                </div>
                <div class="form-group">
                    <button onclick="insEmpalme(getEmpalmes);" type="button" class="btn btn-primary btn-sm" id="btn-insert">Insertar</button>
                </div>
            </form>
        </div>

        <div class="col-sm-7">
            <table id="tabla-empalmes" class="table table-condensed table-borderless table-striped table-hover table-sm small">
                <thead>
                    <tr>
                        <th># Empalme</th>
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