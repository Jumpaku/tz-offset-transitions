#!/bin/sh

set -eux

if [ -e '/gen/tzot.json' ]; then
  cat '/gen/tzot.json' > '/gen/tzot.json.backup'
else
  echo '' > '/gen/tzot.json.backup'
fi

curl -o tzdata.tar.gz https://data.iana.org/time-zones/tzdata-latest.tar.gz

java -jar ziupdater.jar -v -f -l "file:tzdata.tar.gz"

/workspace/gradlew run --args='/gen/tzot.json'

tar -xzvf tzdata.tar.gz version
cat version > /gen/version

echo '/gen/tzot.json successfully updated'
