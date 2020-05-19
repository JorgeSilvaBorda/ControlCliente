<script src="modulos/registros-mes-remarcador.js?=<% out.print(modelo.Util.generaRandom(10000, 99999));%>" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        getSelectInstalacion();
    });

</script>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-12">
            <div class="page-header">
                <h1>Registros Lecturas Remarcador <small>en instalaciones y bodegas</small></h1>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-md-3 ">
            <form class="form small"  role="form">
                <div class="form-group">
                    <label for="select-instalacion" >Instalaci�n</label>
                    <select onchange="getSelectBodegasInstalacion($(this).val())" class="form-control form-control-sm small" id="select-instalacion" >
                    </select>
                </div>
                <div class="form-group">
                    <label for="select-bodega" >Bodega</label>
                    <select onchange="getSelectEmpalmeIdParque($(this).val());" class="form-control form-control-sm small" id="select-bodega" >
                    </select>
                </div>
                <div class="form-group">
                    <label for="select-empalme" >Empalme</label>
                    <select onchange="getSelectRemarcadorIdEmpalme($(this).val());" class="form-control form-control-sm small" id="select-empalme" >
                    </select>
                </div>

                <div class="form-group">
                    <label for="select-remarcador" >Remarcador</label>
                    <select class="form-control form-control-sm small" id="select-remarcador" >
                    </select>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <label for="mes" >Mes</label>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-8">
                        <div class="form-group">
                            <input type="month" class="form-control form-control-sm small" id="mes"/>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="form-group">
                            <button id="btn-buscar" onclick="buscar();" type="button" class="btn btn-primary btn-sm float-sm-right ">Buscar</button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
        <div class="col-sm-1">
            <div class="loader" style="display: none;"><!-- Contenedor del Spinner -->
                <br />
                <div class="ldio-sa9px9nknjc"> <!-- El Spinner -->
                    <div>
                    </div>
                    <div>
                        <div></div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-sm-8 float-sm-right" id="detalle-remarcador">
        </div>

    </div>
    <div class="row">

    </div>
    <!-- div class='row'>
        <div class="col-md-12">
            <canvas id="grafico" width="600" height="300"></canvas>
        </div>
    </div -->
</div>