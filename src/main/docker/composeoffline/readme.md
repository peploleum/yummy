Pour faire fonctionner le tout
charger les images (voir docker-compose.yml) dans le nexus

mettre des xml de flux Rss dans ./feeder/xml
lancer la commande
docker-compose -f docker-compose.yml up -d
se connecter au nifi, instancier le template getFile_TransformXML_Kafka
et demarrer le flow