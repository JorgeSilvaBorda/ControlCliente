
(
LOCAL=$(pwd)
LOCAL=${LOCAL//$'\r'}
IFS='
'
## Variables internas. Deben estar definidas en $RUTA_PROPERTIES ##########
declare BD_HOST=$(grep -i "bd.host" "$RUTA_PROPERTIES" | cut -d "=" -f 2  )
BD_HOST=${BD_HOST//$'\r'}
declare BD_USER=$(grep -i "bd.user" "$RUTA_PROPERTIES" | cut -d "=" -f 2 )
BD_USER=${BD_USER//$'\r'}
declare BD_PASSWORD=$(grep -i "bd.password" "$RUTA_PROPERTIES" | cut -d "=" -f 2 )
BD_PASSWORD=${BD_PASSWORD//$'\r'}
declare BD_BD=$(grep -i "bd.bd" "$RUTA_PROPERTIES" | cut -d "=" -f 2 )
BD_BD=${BD_BD//$'\r'}
declare RUTA_BATCH=$(grep -i "proc.dir" "$RUTA_PROPERTIES" | cut -d "=" -f 2 )
RUTA_BATCH=${RUTA_BATCH//$'\r'}
## ########################################################################

## Entradas de la shell ###################################################
declare ID_REMARCADOR=$1
declare MES_SOLO=$2
declare ANIO_SOLO=$3
declare RUTA_FILE=${RUTA_BATCH}/${ID_REMARCADOR}_$(date "+%Y-%m-%d_%H_%M_%S").csv
## ########################################################################

# QUERY -------------------------------------------------------------------

declare QUERY="CALL SP_GET_REGISTROS_MES_REMARCADOR("${ID_REMARCADOR}", "${ANIO_SOLO}", "${MES_SOLO}")"

mysql -u ${BD_USER} -p${BD_PASSWORD} -h ${BD_HOST} ${BD_BD} -e ${QUERY} >> ${RUTA_FILE}
sed -i 's/\t/,/g' ${RUTA_FILE}
echo $RUTA_FILE

)
