#!/bin/sh
# Сборка war без Maven: ./build.sh /путь/к/wildfly
WILDFLY=${1:-$HOME/wildfly}
API=$(find "$WILDFLY/modules" -name 'jakarta.servlet-api*.jar' | head -1)

rm -rf build && mkdir -p build/war/WEB-INF/classes
cp -r src/main/webapp/* build/war/
javac --release 17 -encoding UTF-8 -cp "$API" -d build/war/WEB-INF/classes $(find src/main/java -name '*.java')
(cd build/war && jar cf ../web-lab3.war .)
echo "Готово: build/web-lab3.war -> скопируй в $WILDFLY/standalone/deployments/"
