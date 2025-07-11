# Setup Guide for Weather & News App

## Prerequisites Check

✅ **Java 21 is installed** - You have Java 21 which is perfect for this application!

## Maven Installation (Required)

Maven is not currently installed on your system. Here are the installation options:

### Option 1: Install Maven via Chocolatey (Recommended for Windows)

1. **Install Chocolatey** (if not already installed):
   - Open PowerShell as Administrator
   - Run: `Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))`

2. **Install Maven**:
   ```powershell
   choco install maven
   ```

3. **Verify Installation**:
   ```powershell
   mvn -version
   ```

### Option 2: Manual Maven Installation

1. **Download Maven**:
   - Go to [Apache Maven Download Page](https://maven.apache.org/download.cgi)
   - Download the binary zip archive (e.g., `apache-maven-3.9.6-bin.zip`)

2. **Extract and Setup**:
   - Extract to a folder like `C:\Program Files\Apache\maven`
   - Add `C:\Program Files\Apache\maven\bin` to your system PATH

3. **Verify Installation**:
   ```powershell
   mvn -version
   ```

### Option 3: Use IDE with Built-in Maven

If you prefer using an IDE:
- **IntelliJ IDEA**: Has built-in Maven support
- **Eclipse**: Has built-in Maven support
- **VS Code**: Install the "Extension Pack for Java" which includes Maven support

## Quick Start (After Maven Installation)

1. **Navigate to the project directory**:
   ```powershell
   cd "C:\Users\pawan\Documents\My Projects\JavaProject"
   ```

2. **Configure API Keys**:
   - Edit `src/main/resources/config.properties`
   - Replace placeholder values with your actual API keys:
     ```properties
     weather.api.key=your_openweathermap_api_key_here
     news.api.key=your_newsapi_key_here
     ```

3. **Build and Run**:
   ```powershell
   mvn clean compile exec:java
   ```

## Get Your API Keys

### OpenWeatherMap API Key (Free)
1. Go to [OpenWeatherMap](https://openweathermap.org/api)
2. Click "Sign Up" and create a free account
3. After verification, go to "API Keys" section
4. Copy your API key

### NewsAPI Key (Free)
1. Go to [NewsAPI](https://newsapi.org/)
2. Click "Get API Key" and create a free account
3. After verification, copy your API key

## Alternative: Run Without Maven (Advanced)

If you prefer not to install Maven, you can manually compile and run:

1. **Download Dependencies** (manually):
   - This is complex and not recommended
   - Maven handles 15+ dependencies automatically

2. **Use an IDE**:
   - Import the project in IntelliJ IDEA or Eclipse
   - The IDE will handle dependencies

## Troubleshooting

### "mvn is not recognized"
- Maven is not installed or not in PATH
- Follow the Maven installation steps above

### "Java version error"
- You have Java 21, which is perfect
- Make sure JAVA_HOME is set correctly

### "API key not found"
- Make sure you've edited `config.properties` with real API keys
- Check that the file is in `src/main/resources/`

## Next Steps

1. Install Maven using one of the methods above
2. Get your API keys from OpenWeatherMap and NewsAPI
3. Configure the `config.properties` file
4. Run the application with `mvn clean compile exec:java`

## Need Help?

If you encounter any issues:
1. Check the main README.md for detailed troubleshooting
2. Verify all prerequisites are met
3. Ensure API keys are valid and properly configured 