#!/bin/sh

echo Initiating Elasticsearch Custom Index
# move to the directory of this setup script
cd "$(dirname "$0")"

# for some reason even when port 9200 is open Elasticsearch is unable to be accessed as authentication fails
# a few seconds later it works
until $(curl -sSf -XGET --insecure --user elastic:changeme 'http://localhost:9200/_cluster/health?wait_for_status=yellow' > /dev/null); do
    printf 'AUTHENTICATION ERROR DUE TO X-PACK, trying again in 5 seconds \n'
    sleep 5
done

# create a new index with the settings in indexconfig.json
curl -v --insecure --user elastic:changeme -XPUT '0.0.0.0:9200/gazetter?pretty' -H 'Content-Type: application/json' -d @indexconfig.json