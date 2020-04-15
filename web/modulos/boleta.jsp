<script src="modulos/boleta.js?=<% out.print(modelo.Util.generaRandom(10000, 99999));%>" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        getSelectClientes();
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
    
    <!-- Para la búsqueda ------------------------------------------------------>
    <div class="row">
        <div class="col-sm-4">
            <div class="card">
                <div class="card-header">
                    <strong>Búsqueda</strong>
                </div>

                <div class="card-body small">
                    <div class="form-group mb-2 row">
                        <div class="col-sm-12">
                            <label for="select-cliente" >Cliente</label>
                            <select class="form-control form-control-sm" id="select-cliente" >
                            </select>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-12">
                            <div class="form-group mb-2 float-sm-left">
                                <label for="fecha-ini">Fecha Inicio Período</label>
                                <input type="date" class="form-control form-control-sm" id="fecha-ini" />
                            </div>
                            <div class="form-group mb-2 float-sm-right">
                                <label for="fecha-fin">Fecha Fin Período</label>
                                <input type="date" class="form-control form-control-sm" id="fecha-fin" />
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-12">
                            <div class="form-group mb-2">
                                <button type="button" onclick="buscar();" class="btn btn-primary btn-sm " >Buscar</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <br />
    
    <!-- Para los cargos adicionales ------------------------------------------->
    <div class="row">
        <div class="col-sm-4">
            <div class="card">
                <div class="card-header">
                    <strong>Cargos adicionales</strong>
                </div>

                <div class="card-body small">
                    <table >
                        <tbody>
                            <tr>
                                <td style="width:58%;">
                                    <div class="form-group mb-2">
                                        <label for="concepto">Concepto</label>
                                        <input type="text" class="form-control form-control-sm" id="concepto" maxlength="200" placeholder="Ej.: Cobro por corte y reposición" />
                                    </div>
                                </td>
                                <td style="width:8%;">
                                    <div class="form-group mb-2">
                                        <label for="cantidad">Cant.</label>
                                        <input type="number" class="form-control form-control-sm" id="cantidad" />
                                    </div>
                                </td>
                                <td style="width:26%;">
                                    <div class="form-group mb-2">
                                        <label for="valor-unitario">$ Unit.</label>
                                        <input type="number" class="form-control form-control-sm" id="valor-unitario"  />
                                    </div>
                                </td>
                                <td style="width:8%;">
                                    <br />
                                    <button type="button" onclick="agregarCargo();" class="btn btn-success btn-md oi oi-plus" ></button>
                                </td>
                            </tr>
                        </tbody>
                    </table> 
                </div>
                <div class="card-body small" id="cargos-extra">
                    
                </div>
            </div>
        </div>
    </div>
</div>