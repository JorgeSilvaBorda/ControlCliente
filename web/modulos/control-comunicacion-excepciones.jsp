<script src="modulos/control-comunicacion.js?=<% out.print(modelo.Util.generaRandom(10000, 99999));%>" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        getExcepciones();
    });

</script>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-12">
            <div class="page-header">
                <h3>Excepciones</h3>
            </div>
        </div>
    </div>
    <br />
    <div class="row">
        <div class="col-sm-12">
            <div class="page-header">
                <h4>Crear</h4>
            </div>
        </div>


    </div>
    <div class="row">
        <div class="col-sm-2">
            <div class="card">
                <div class="card-header">
                    Motivo
                </div>
                <div class="card-body" style="padding-left: 2em;">
                    <p>
                        <label for="opt-vacio" class="form-check-label">
                            <input id="opt-vacio" type="radio" class="form-check-input" value="1" name="opciones" />Bodega vac�a
                        </label>
                    </p>
                    <p>
                        <label for="opt-pulmon" class="form-check-label">
                            <input id="opt-pulmon" type="radio" class="form-check-input" value="2" name="opciones" />Bodega pulm�n
                        </label>
                    </p>
                    <p>
                        <label for="opt-falla" class="form-check-label">
                            <input id="opt-falla" type="radio" class="form-check-input" value="3" name="opciones" />En falla
                        </label> 
                    </p>

                </div>
            </div>
            <br />
            <div class="card">
                <div class="card-header">
                    Datos Excepci�n
                </div>
                <div class="card-body" style="padding-left: 1em;">
                    <div class="form-group small">
                        <label for="numremarcador">Id Remarcador</label>
                        <input id="numremarcador" style="width: 7em;" type="number" class="form-control form-control-sm" />
                    </div>
                    <div class="form-group small">
                        <label for="select-duracion">Duraci�n excepci�n</label>
                        <select id="select-duracion" style="width: 8em;" class="form-control form-control-sm">
                            <option selected="selected" value="0">Seleccione</option>
                            <option value="1">1 d�a</option>
                            <option value="2">1 semana</option>
                            <option value="3">1 mes</option>
                        </select> 
                    </div>
                </div>
            </div>
            <br />
            <button onclick="getExcepcionesRemarcador();" type="button" class="btn btn-sm btn-success">Ingresar</button>
        </div>
        <div class="col-sm-8">
            <div class="page-header">
                <h4>Excepciones activas</h4>
            </div>
            <table style='font-size: 12px;' id='tabla-excepciones' class='table table-condensed table-borderless table-striped table-hover table-sm small'>
                <thead>
                    <tr>
                        <th>Fecha Creaci�n</th>
                        <th>Id Remarcador</th>
                        <th>Motivo</th>
                        <th>Duraci�n</th>
                        <th>Fecha T�rmino</th>
                        <th>Acci�n</th>
                    </tr>
                </thead>
                <tbody>

                </tbody>
            </table>
        </div>
    </div>
    <div class="row">


    </div>
</div>