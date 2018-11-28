javac *.java

set mypath=%cd%

del "%mypath%\*.VAL"
del "%mypath%\*.BT"

java CS110_Project1 data.bt data.val

pause