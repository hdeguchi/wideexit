set CLASSES_PATH=..\..\..\..\..\classes
set MAIN_CLASS=soars.tool.grid.portal_ip_address_receiver.main.Application

set CP=%CLASSES_PATH%

start javaw -cp %CP% %MAIN_CLASS% %*
