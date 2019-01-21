call mvn clean package -DskipTests dockerfile:build
docker tag yummy peploleum/yummy:1.0.0
docker push peploleum/yummy:1.0
.0