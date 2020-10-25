<script src="modulos/ingreso-lectura-manual.js?=<% out.print(modelo.Util.generaRandom(10000, 99999));%>" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        getSelectInstalacion();
        //getLastMes();
    });

</script>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-12">
            <div class="page-header">
                <h1>Ingreso Lectura<small> manual cierre de mes</small></h1>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-3">
            <div class="form-group">
                <label for="select-instalación">Instalación</label>
                <select onchange="getSelectBodegaIdInstalacion($(this).val());" class="form-control-sm form-control small" id="select-instalacion">
                </select>
            </div>
            <div class="form-group">
                <label for="select-parque">Bodega</label>
                <select onchange="getSelectEmpalmeIdParque($(this).val());" class="form-control-sm form-control small" id="select-parque">
                </select>
            </div>
            <div class="form-group">
                <label for="select-empalme">Empalme</label>
                <select onchange="getSelectRemarcadorIdEmpalme($(this).val());" class="form-control-sm form-control small" id="select-empalme">
                </select>
            </div>
            <div class="form-group">
                <label for="select-remarcador">Remarcador</label>
                <select class="form-control-sm form-control small" id="select-remarcador">
                </select>
            </div>
            <div class="form-group">
                <label for="fecha">Mes</label>
                <input type="month" class="form-control form-control-sm small" id="fecha" max="" />
            </div>
            <button type="button" onclick='getLastLecturaMes();' id="btn-buscar" class="btn btn-success btn-sm small">Buscar</button>
            <button type="button" onclick='limpiar();' id="btn-limpiar" class="float-right btn btn-default btn-sm small">Limpiar</button>
        </div>
        <div class="col-sm-9">
            <div class="row">
                <div class="col-sm-6" id="cont-last-lectura">

                </div>
            </div>
            <div class="row">
                <div class="col-sm-2" id="cont-lectura-manual">
                    
                </div>
            </div>
        </div>
    </div>
</div>