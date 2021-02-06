<script src="modulos/cliente-remarcador.js?=<% out.print(modelo.Util.generaRandom(10000, 99999));%>" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        getRemarcadoresLibres();
        getSelectClientes();
    });

</script>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-12">
            <!-- Modal -->
            <div class="modal fade" id="modal">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">

                        <!-- Header -->
                        <div class="modal-header">
                            <h4 id="modal-title" class="modal-title"></h4>
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                        </div>

                        <!-- Body -->
                        <div id="modal-body" class="modal-body">

                        </div>

                        <!-- Modal footer -->
                        <div id="modal-footer" class="modal-footer">
                            <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-sm-12">
            <div class="page-header">
                <h1>Asignar Remarcador a Cliente</h1>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-3">
            <div class="card">
                <div class="card-header">
                    <strong>Búsqueda</strong>
                </div>
                <div class="card-body">
                    <form class="form small"  role="form">
                        <div class="form-group">
                            <label for="select-cliente" >Cliente</label>
                            <select onchange="getRemarcadoresAsignadosIdCliente($(this).val())" class="form-control form-control-sm small" id="select-cliente" >
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="select-contacto" >Contactos Cliente</label>
                            <select class="form-control form-control-sm small" id="select-contacto" >
                            </select>
                        </div>
                        <div class="form-group">
                            <button type="button" onclick="verAsignados();" class="btn btn-outline-info" id="btn-mostrar-asignados">Ver todos los remarcadores asignados</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <div class="col-sm-9">
            <div class="card">
                <div class="card-header">
                    <strong>Remarcadores sin asignar</strong>
                </div>
                <div class="card-body">
                    <table id="tabla-remarcadores-libres" class="table table-condensed table-borderless table-striped table-hover table-sm small">
                        <thead>
                            <tr>
                                <th>ID Remarcador</th>
                                <th>Nº Serie</th>
                                <th># Empalme</th>
                                <th>Bodega</th>
                                <th>Módulos</th>
                                <th>Instalacion</th>
                                <th>Asignar</th>
                            </tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
            </div>

        </div>
    </div>
    <br />
    <div class='row'>
        <div class='col-sm-12'>
            <div class="card">
                <div class="card-header">
                    <strong>Remarcadores asignados al Cliente seleccionado</strong>
                </div>
                <div class="card-body">
                    <table id='tabla-cliente-remarcador' class="table table-condensed table-borderless table-striped table-hover table-sm small">
                        <thead>
                            <tr>
                                <th>ID Remarcador</th>
                                <th>Nº Serie</th>
                                <th>Empalme</th>
                                <th>Bodega</th>
                                <th>Instalación</th>
                                <th>Contacto</th>
                                <th>Fono</th>
                                <th>Fecha Asignación</th>
                                <th>Usuario</th>
                                <th>Quitar</th>
                            </tr>
                        </thead>
                    </table>
                </div>
            </div>

        </div>
    </div>
</div>