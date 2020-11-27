<script src="modulos/control-comunicacion.js?=<% out.print(modelo.Util.generaRandom(10000, 99999));%>" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        $('#cont-eventos').load('modulos/control-comunicacion-nuevos.jsp');
    });

</script>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-12">
            <div class="page-header">
                <h1>Eventos<small> de comunicación</small></h1>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-12">
            <ul class="nav nav-tabs">
                <li class="nav-item">
                    <a class="nav-link active" onclick='cargarPestana(this, "control-comunicacion-nuevos");' href="#">Nuevos</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" onclick='cargarPestana(this, "control-comunicacion-todos");' href="#">Todos</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" onclick='cargarPestana(this, "control-comunicacion-excepciones");' href="#">Excepciones</a>
                </li>
            </ul>
        </div>
    </div>
    <div class='row'>
        <div class='col-sm-12' id='cont-eventos'></div>
    </div>
</div>