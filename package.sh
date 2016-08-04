#!/bin/sh
VERSION=$1
if [ -x ${VERSION} ];
then
	echo VERSION not defined
	exit 1
fi
PACKAGE=wng-client-${VERSION}.zip
echo PACKAGE="${PACKAGE}"

FILES="changelogs classes conf html lib src resource addons"
FILES="${FILES} wng.jar wngservice.jar"
FILES="${FILES} 3RD-PARTY-LICENSES.txt AUTHORS.txt COPYING.txt DEVELOPER-AGREEMENT.txt LICENSE.txt"
FILES="${FILES} DEVELOPERS-GUIDE.md OPERATORS-GUIDE.md README.md README.txt USERS-GUIDE.md"
FILES="${FILES} mint.bat mint.sh run.bat run.sh run-tor.sh run-desktop.sh start.sh stop.sh compact.sh compact.bat sign.sh"
FILES="${FILES} wng.policy wngdesktop.policy WNG_Wallet.url"
FILES="${FILES} compile.sh javadoc.sh jar.sh package.sh"
FILES="${FILES} win-compile.sh win-javadoc.sh win-package.sh"

echo compile
./compile.sh
echo jar
./jar.sh
echo javadoc
rm -rf html/doc/*
./javadoc.sh

rm -rf wng
rm -rf ${PACKAGE}
mkdir -p wng/
mkdir -p wng/logs
echo copy resources
cp -a ${FILES} wng
echo gzip
for f in `find wng/html -name *.gz`
do
	rm -f "$f"
done
for f in `find wng/html -name *.html -o -name *.js -o -name *.css -o -name *.json -o -name *.ttf -o -name *.svg -o -name *.otf`
do
	gzip -9c "$f" > "$f".gz
done
echo zip
zip -q -X -r ${PACKAGE} wng -x \*/.idea/\* \*/.gitignore \*/.git/\* \*/\*.log \*.iml wng/conf/wng.properties wng/conf/logging.properties
rm -rf wng
echo done
