<script src="modulos/usuario.js?=<% out.print(modelo.Util.generaRandom(10000, 99999));%>" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        $("input#rut-usuario").rut({
            formatOn: 'keyup',
            minimumLength: 8, // validar largo m�nimo; default: 2
            validateOn: null // si no se quiere validar, pasar null
        });
        getUsuarios();
    });

</script>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-12">
            <div class="page-header">
                <h2>Boleta</h2>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-3">
            <form class="form small form-inline"  role="form">
                <div class="form-row">
                    <div class="form-group">
                        <label for="fecha-ini">Fecha Inicio Per�odo</label>
                        <select class="form-control-sm form-control small" id="fecha-ini">
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="fecha-ini">Fecha Fin Per�odo</label>
                        <select class="form-control-sm form-control small" id="fecha-fin">
                        </select>
                    </div>
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
                    <button onclick="saveUsuario(getUsuarios);" hidden="hidden" type="button" class="btn btn-secondary btn-sm" id="btn-guardar">Guardar</button>
                    <button onclick="limpiar();" type="button" class="btn btn-default btn-sm" id="btn-limpiar">Limpiar</button>
                </div>
            </form>
        </div>
    </div>
</div>