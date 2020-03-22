<script src="modulos/fase.js" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        getFases();
    });

</script>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-12">
            <div class="page-header">
                <h1>Fases</h1>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-3">
            <form class="form small"  role="form">
                <div class="form-group">
                    <label for="rut-usuario" >Nombre Fase</label>
                    <input type="text" class="form-control form-control-sm small" maxlength="200" id="nom-fase" />
                </div>
                <div class="form-group">
                    <button type="button" class="btn btn-primary btn-sm" id="btn-insert">Insertar</button>
                </div>
            </form>
        </div>

        <div class="col-sm-7">
            <table id="tabla-fases" class="table table-condensed table-borderless table-striped table-hover table-sm small">
                <thead>
                    <tr>
                        <th>Nombre</th>
                    </tr>
                </thead>
                <tbody>
                    
                </tbody>
            </table>
        </div>
    </div>
</div>