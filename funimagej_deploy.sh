#!/bin/bash

if [[ $TRAVIS_BRANCH == 'master' ]]
then   
   LEIN_SNAPSHOTS_IN_RELEASE=1 lein deploy
   curl -O http://downloads.imagej.net/fiji/latest/fiji-nojre.zip
   unzip fiji-nojre.zip
   mv target/fun.imagej*jar Fiji.app/jars/
   cp -r src/plugins/* Fiji.app/plugins/
   cd Fiji.app
   curl -O https://raw.githubusercontent.com/fiji/fiji/7f13f66968a9d4622e519c8aae04786db6601314/bin/upload-site-simple.sh
   chmod a+x upload-site-simple.sh
   ./upload-site-simple.sh FunImageJ FunImageJ
fi
