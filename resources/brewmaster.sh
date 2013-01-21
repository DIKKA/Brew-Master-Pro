#!/bin/sh

BMP_HOME="${HOME}/.brew-master-pro"

# For now, the jar file has to be in the same
# directory as the start script
BMP_JAR="brewmasterpro.jar"
JAVA_EXE="java"
JAVA_OPTS="-Xms128m -Xmx768m"
ARGS="$@"

$JAVA_EXE $JAVA_OPTS -jar $BMP_JAR &
