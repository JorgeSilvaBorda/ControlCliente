  GNU nano 4.3                                                   /batch/eliminar-antiguos.sh                                                              
cont=1
for archivo in $(ls -t /respaldos/); 
do
    if((cont > 3)); then
        rm -rf /respaldos/$archivo;
        echo "Archivo: "$archivo" eliminado";
    fi;
    ((cont=cont+1));
done; 
