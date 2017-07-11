set CLASSES_PATH=..\..\..\..\classes
set LIBRARY_PATH=..\..\..\..\lib
set MAIN_CLASS=soars.application.animator.main.Application

set CP=%CLASSES_PATH%

start javaw -Xmx512m -cp %CP% %MAIN_CLASS% -language en %*
