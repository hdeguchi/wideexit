set CLASSES_PATH=..\..\..\..\classes
set LIBRARY_PATH=..\..\..\..\lib
set MAIN_CLASS=soars.application.visualshell.main.Application

set CP=%CLASSES_PATH%;%LIBRARY_PATH%\spring\spring.jar;%LIBRARY_PATH%\spring\commons-logging.jar;%LIBRARY_PATH%\xerces\xercesImpl.jar;%LIBRARY_PATH%\xalan\xalan.jar;%LIBRARY_PATH%\j2ssh\j2ssh-ant.jar;%LIBRARY_PATH%\j2ssh\j2ssh-common.jar;%LIBRARY_PATH%\j2ssh\j2ssh-core.jar;%LIBRARY_PATH%\j2ssh\j2ssh-dameon.jar;%LIBRARY_PATH%\beanshell\bsh-2.0b4.jar

start javaw -Xmx512m -cp %CP% %MAIN_CLASS% -language en %*
