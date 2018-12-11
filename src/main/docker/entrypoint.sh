#!/bin/sh

echo "The application will start in ${YUMMY_SLEEP}s..." && sleep ${YUMMY_SLEEP}
exec java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar "${HOME}/app.jar" "$@"
