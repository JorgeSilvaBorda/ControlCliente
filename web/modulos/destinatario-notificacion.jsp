<script src="modulos/destinatario-notificacion.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        getDestinatarios();
        getSelectInstalacion();
    });

</script>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-12">
            <div class="page-header">
                <h1>Destinatarios <small>de notificación de eventos</small></h1>
            </div>
        </div>
    </div>
    <form class="form small" role="form">
        <div class="row">
            <div class="col-sm-3">
                <div class="form-group">
                    <label for="nombres">Nombres</label>
                    <input type="text" class="form-control form-control-sm small" maxlength="200" id="nombres" />
                </div>
            </div>
            <div class="col-sm-3">
                <div class="form-group">
                    <label for="email">Email</label>
                    <input type="text" class="form-control form-control-sm small" maxlength="200" id="email" />
                </div>
            </div>
            <div class="col-sm-3">
                <div class="form-group">
                    <label for="select-instalacion">Instalación</label>
                    <select class="form-control form-control-sm small" id="select-instalacion" ></select>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-2">
                <div class="form-group">
                    <label for="btn-insert">&nbsp;</label>
                    <button onclick="insDestinatario();" type="button" class="btn btn-primary btn-sm float-left" id="btn-insert" >Insertar</button>
                    <button onclick="limpiar();" type="button" class="btn btn-default btn-sm" id="btn-limpiar">Limpiar</button>
                </div>
            </div>
        </div>
    </form>
    <div class="row">
        <div class="col-sm-12">
            <table id="tabla-destinatarios" class="table table-condensed table-borderless table-striped table-hover table-sm small">
                <thead>
                    <tr>
                        <th>Nombres</th>
                        <th>Email</th>
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