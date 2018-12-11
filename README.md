# yummy

Sadly tested on Docker for Windows. Up the broker:

    docker-compose.exe -f .\broker.yml up -d
    
Build docker image including packaged Uberjar:

    mvn clean package -DskipTests dockerfile:build