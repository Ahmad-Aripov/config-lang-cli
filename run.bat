@echo off
REM Простой скрипт для запуска программы в Windows
REM Использование: run.bat test.conf

if "%1"=="" (
    echo Использование: run.bat ^<файл.conf^>
    echo Пример: run.bat test.conf
    exit /b 1
)

echo Компиляция проекта...
call mvn -q compile
if errorlevel 1 (
    echo Ошибка компиляции!
    exit /b 1
)

echo Запуск программы с файлом: %1
echo.
java -cp "target/classes;target/dependency/*" org.example.Main -i %1

