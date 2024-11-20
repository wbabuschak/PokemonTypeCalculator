@echo off
setlocal enabledelayedexpansion

REM Directory containing the subdirectory "target"
set base_folder=%~dp0

REM Find the first .jar file in the "target" subdirectory
for /r "%base_folder%" %%f in (target\*.jar) do (
    echo Found JAR file: %%~nxf
    echo Running the JAR file...
    java -jar "%%f"
    goto :eof
)

echo No JAR file found in any "target" subdirectory.
pause
