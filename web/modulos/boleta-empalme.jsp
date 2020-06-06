<script src="modulos/boleta-empalme.js?=<% out.print(modelo.Util.generaRandom(10000, 99999));%>" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        getSelectInstalacion();
    });

</script>
<div class="container-fluid">
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
    <div class="row">
        <div class="col-sm-12">
            <div class="page-header">
                <h1>Emitir Boleta <small>Por Empalme</small></h1>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-2">
            <form class="form small"  role="form">
                <div class="form-group">
                    <label for="select-instalacion" >Instalación</label>
                    <select onchange="getSelectEmpalmesNumEmpalmesInstalacion()" class="form-control form-control-sm small" id="select-instalacion">
                    </select>
                </div>
                <div class="form-group">
                    <label for="select-empalme" >Empalme</label>
                    <select class="form-control form-control-sm small" id="select-empalme" >
                    </select>
                </div>

                <div class="form-group" style="">
                    <label for="fecha-ini">Fecha Ini</label>
                    <input type="date" id="fecha-ini" class="form-control form-control-sm"/>
                </div>
                <div class="form-group" style="">
                    <label for="fecha-fin">Fecha Fin</label>
                    <input type="date" id="fecha-fin" class="form-control form-control-sm"/>
                </div>
                <div class="form-group" style="">
                    <button type="button" class="btn btn-primary btn-sm" onclick="buscar();" >Buscar</button>
                    <!-- button type="button" class="btn btn-outline-primary btn-sm float-right" onclick="limpiar()">Limpiar</button-->
                </div>
            </form>
        </div>
        <div class="col-sm-8 float-sm-right" id="detalle-remarcadores">
        </div>

    </div>
</div>