@echo off


BMP_HOME="${HOME}/.brew-master-pro

REM For now, the jar file has to be in the same
REM directory as the start script
set BMP_JAR="brewmasterpro.jar"
set JAVA_EXE="java"
set JAVA_OPTS="-Xms128m -Xmx768m"
REM set ARGS="$@"

%JAVA_EXE% %JAVA_OPTS% -jar %BMP_JAR% 
