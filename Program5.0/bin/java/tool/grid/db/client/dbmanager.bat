set JAR=..\..\..\..\..\..\lib\hsqldb\hsqldb.jar
set MAIN_CLASS=org.hsqldb.util.DatabaseManager

start javaw -cp %JAR% %MAIN_CLASS% -url jdbc:hsqldb:hsql://portal %*
