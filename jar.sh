#!/bin/sh
java -cp classes wng.tools.ManifestGenerator
/bin/rm -f wng.jar
jar cfm wng.jar resource/wng.manifest.mf -C classes . || exit 1
/bin/rm -f wngservice.jar
jar cfm wngservice.jar resource/wngservice.manifest.mf -C classes . || exit 1

echo "jar files generated successfully"