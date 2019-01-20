call mvn clean package -DskipTests dockerfile:build
docker tag yummy peploleum/yummy:latest
docker push peploleum/yummy:latest