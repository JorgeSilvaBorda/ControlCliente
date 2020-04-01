<script src="modulos/usuario.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        $("input#rut-usuario").rut({
            formatOn: 'keyup',
            minimumLength: 8, // validar largo mínimo; default: 2
            validateOn: null // si no se quiere validar, pasar null
        });
        getUsuarios();
    });

</script>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-12">
            <div class="page-header">
                <h1>Usuarios <small>Mantenimiento y edición</small></h1>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-3">
            <form class="form small"  role="form">
                <div class="form-group">
                    <label for="select-tipo-usuario">Tipo usuario</label>
                    <select class="form-control-sm form-control small" id="select-tipo-usuario">
                        <option selected="selected" value="0">Seleccione</option>
                        <option value="1">Administrador</option>
                        <option value="2">Usuario</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="rut-usuario" >Rut</label>
                    <input type="text" class="form-control form-control-sm small" maxlength="12" id="rut-usuario" />
                </div>
                <div class="form-group">
                    <label for="nombres">Nombres</label>
                    <input type="text" class="form-control form-control-sm small" maxlength="50" id="nombres" />
                </div>
                <div class="form-group">
                    <label for="ap-paterno">Apellido Paterno</label>
                    <input type="text" class="form-control form-control-sm small" maxlength="50" id="ap-paterno" />
                </div>
                <div class="form-group">
                    <label for="ap-materno">Apellido Materno</label>
                    <input type="text" class="form-control form-control-sm small" maxlength="50" id="ap-materno" />
                </div>
                <div class="form-group">
                    <button onclick="insUsuario(getUsuarios);" type="button" class="btn btn-primary btn-sm" id="btn-insert">Insertar</button>
                    <button onclick="saveUsuario(getUsuarios);" type="button" class="btn btn-secondary btn-sm hide" id="btn-guardar">Guardar</button>
                    <button onclick="limpiar();" type="button" class="btn btn-default btn-sm" id="btn-limpiar">Limpiar</button>
                </div>
            </form>
        </div>

        <div class="col-sm-7">
            <table id="tabla-usuarios" class="table table-condensed table-borderless table-striped table-hover table-sm small">
                <thead>
                    <tr>
                        <th>Rut</th>
                        <th>Nombres</th>
                        <th>Ap. Paterno</th>
                        <th>Ap. Materno</th>
                        <th>Tipo Usuario</th>
                        <th>Acción</th>
                    </tr>
                </thead>
                <tbody>
                    
                </tbody>
            </table>
        </div>
    </div>
</div>