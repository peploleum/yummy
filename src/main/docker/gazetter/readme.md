
elastichsearch:
les fichiers  
./elk/elasticsearch/config/setup.sh
./elk/elasticsearch/utils/wait-for-it.sh
./elk/elasticsearch/docker-entrypoint.sh
./elk/elasticsearch/Dockerfile

permettent "d'attendre" qu'elasticseach se lance pour
crée un index "gazetter" decrit dans
./elk/elasticsearch/config/indexconfig.json

cet index contient un champ "location" type "geo_point" qui permet
la localisation sur carte 

logstash:
Dans ./elk/logstash/pipeline/logstash.conf
les champs "latitude" et "longitude" des csv sont extrait et assemblé
pour remplir "location"
Attention:
Si le l'index "gazetter" n'est pas crée avant que logstash se lance,
le champ location en sera pas en type "geo_point"



Pour lancer

docker-compose -f docker-compose.yml up -d

