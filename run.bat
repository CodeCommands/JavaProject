@echo off
echo =============================================
echo Weather and News App - Quick Start
echo =============================================
echo.

REM Check if Maven is installed
where mvn >nul 2>nul
if %errorlevel% neq 0 (
    echo ❌ Maven is not installed or not in PATH
    echo.
    echo Please install Maven first:
    echo 1. Install Chocolatey: https://chocolatey.org/install
    echo 2. Run: choco install maven
    echo 3. Or see SETUP.md for manual installation
    echo.
    pause
    exit /b 1
)

REM Check if Java is installed
where java >nul 2>nul
if %errorlevel% neq 0 (
    echo ❌ Java is not installed or not in PATH
    echo Please install Java 11 or higher
    echo.
    pause
    exit /b 1
)

echo ✅ Maven and Java are available
echo.

REM Check if config file exists and has API keys
if not exist "src\main\resources\config.properties" (
    echo ❌ Configuration file not found
    echo Please make sure src\main\resources\config.properties exists
    echo.
    pause
    exit /b 1
)

REM Check for placeholder API keys
findstr /C:"YOUR_OPENWEATHERMAP_API_KEY_HERE" "src\main\resources\config.properties" >nul
if %errorlevel% equ 0 (
    echo ⚠️  WARNING: OpenWeatherMap API key not configured
    echo Please edit src\main\resources\config.properties
    echo and replace YOUR_OPENWEATHERMAP_API_KEY_HERE with your actual API key
    echo.
    echo Get your free API key at: https://openweathermap.org/api
    echo.
    set /p continue="Continue anyway? (y/n): "
    if /i not "%continue%"=="y" exit /b 1
)

findstr /C:"YOUR_NEWSAPI_KEY_HERE" "src\main\resources\config.properties" >nul
if %errorlevel% equ 0 (
    echo ⚠️  WARNING: NewsAPI key not configured
    echo Please edit src\main\resources\config.properties
    echo and replace YOUR_NEWSAPI_KEY_HERE with your actual API key
    echo.
    echo Get your free API key at: https://newsapi.org/
    echo.
    set /p continue="Continue anyway? (y/n): "
    if /i not "%continue%"=="y" exit /b 1
)

echo 🚀 Starting Weather and News App...
echo.

REM Build and run the application
mvn clean compile exec:java

echo.
echo Application finished. Press any key to exit...
pause >nul 