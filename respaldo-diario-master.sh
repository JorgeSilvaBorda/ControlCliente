(
IFS='
'
echo $(date '+%Y-%m-%d %H:%M:%S')" Respaldo iniciado"
## Detener la escritura mientras se realiza el export de los datos
echo $(date '+%Y-%m-%d %H:%M:%S')" Se procede a bloquear las tablas de la base de datos"
mysql -u root -pBodenor848 -e "FLUSH TABLES WITH READ LOCK"

## Nombre del archivo en que quedan los datos.
## Ejemplo: bodenor-backup-20210606084515.sql
FILE_NAME=$(date '+bodenor-backup-%Y%m%d%H%M%S.sql')

## Ejecutar mysqldump para respaldar

echo $(date '+%Y-%m-%d %H:%M:%S')" Archivos existentes en el directorio de respaldos /respaldos/:"
echo $(ls -ltrh /respaldos)
echo $(date '+%Y-%m-%d %H:%M:%S')" Ejecutando mysqldump con archivo de salida: "$FILE_NAME
mysqldump -u root -pBodenor848 --routines bodenor > /respaldos/$FILE_NAME

## Una vez que finalice la creación del archivo de respaldo, se deja disponible nuevamente la base de datos.
echo $(date '+%Y-%m-%d %H:%M:%S')" Respaldo ejecutado"
echo $(date '+%Y-%m-%d %H:%M:%S')" Se procede a desbloquear las tablas de la base de datos"
mysql -u root -pBodenor848 -e "UNLOCK TABLES"

echo $(date '+%Y-%m-%d %H:%M:%S')" Inicia la limpieza del directorio de respaldos"
## Se limpia el directorio de los respaldos para dejar sólo los últimos 3 generados

bash eliminar-antiguos.sh

## Copiar a las máquinas remotas
rsync -ae ssh --progress /respaldos/$FILE_NAME remoto@192.168.100.218:/home/remoto/respaldos/
#rsync -ae ssh --progress /respaldos/$FILE_NAME remarc@192.168.100.159:/share/homes/remarc/REMARCBD_BACKUP
echo $(date '+%Y-%m-%d %H:%M:%S')" Respaldo finalizado"
)
