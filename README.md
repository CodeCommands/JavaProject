# Weather & News App

A Java-based console application that provides current weather information and local news for any US zipcode.

## Features

- **Weather Information**: Get current weather conditions including temperature, humidity, wind speed, and more
- **Local News**: Fetch relevant news articles for the specified location
- **Zipcode Support**: Enter any valid US zipcode to get location-specific information
- **User-Friendly Interface**: Clean console interface with easy navigation
- **Error Handling**: Robust error handling with informative messages

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- Internet connection for API calls
- API keys for OpenWeatherMap and NewsAPI (free accounts available)

## API Keys Required

### 1. OpenWeatherMap API Key
- Visit [OpenWeatherMap](https://openweathermap.org/api)
- Create a free account
- Generate an API key
- Used for weather data and geocoding services

### 2. NewsAPI Key
- Visit [NewsAPI](https://newsapi.org/)
- Create a free account
- Generate an API key
- Used for fetching news articles

## Installation & Setup

1. **Clone or Download the Project**
   ```bash
   # If using git
   git clone <repository-url>
   cd JavaProject
   
   # Or download and extract the ZIP file
   ```

2. **Configure API Keys**
   - Open `src/main/resources/config.properties`
   - Replace the placeholder values with your actual API keys:
     ```properties
     weather.api.key=your_openweathermap_api_key_here
     news.api.key=your_newsapi_key_here
     ```

3. **Build the Project**
   ```bash
   mvn clean compile
   ```

4. **Run the Application**
   ```bash
   mvn exec:java
   ```

   Or build and run the JAR file:
   ```bash
   mvn clean package
   java -jar target/weather-news-app-1.0.0.jar
   ```

## Usage

1. **Start the Application**
   - Run the application using Maven or the JAR file
   - You'll see a welcome message with instructions

2. **Enter a Zipcode**
   - Type a valid US zipcode (e.g., `90210`, `10001`, `60601`)
   - Press Enter to get weather and news information

3. **View Results**
   - Location information will be displayed first
   - Current weather conditions will be shown
   - Local news articles will be listed with titles, sources, and descriptions

4. **Exit the Application**
   - Type `quit` or `exit` to close the application

## Example Usage

```
Enter a US zipcode (or 'quit' to exit): 90210

============================================================
Processing zipcode: 90210
============================================================
📍 Looking up location...
   Location: Beverly Hills, US 90210 (34.0901, -118.4065)

🌤️  Fetching weather information...
Weather in Beverly Hills, US:
  Temperature: 72.5°F (feels like 74.1°F)
  Condition: Clear - clear sky
  Humidity: 65%
  Wind: 5.2 mph
  Pressure: 1013.2 hPa
  Visibility: 10000 meters

📰 Fetching local news...
   Found 5 news articles:
------------------------------------------------------------
1. Local Beverly Hills News Article
   Source: Local News Source
   Description of the news article...
   URL: https://example.com/article1

2. Another News Article
   Source: News Source
   Description of another article...
   URL: https://example.com/article2
```

## Project Structure

```
JavaProject/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── weathernews/
│   │   │           ├── WeatherNewsApp.java      # Main application class
│   │   │           ├── model/                   # Data models
│   │   │           │   ├── Location.java
│   │   │           │   ├── Weather.java
│   │   │           │   └── NewsArticle.java
│   │   │           └── service/                 # Service classes
│   │   │               ├── WeatherService.java
│   │   │               ├── NewsService.java
│   │   │               └── ZipcodeService.java
│   │   └── resources/
│   │       └── config.properties               # Configuration file
│   └── test/
│       └── java/                               # Test classes (if any)
├── pom.xml                                     # Maven configuration
└── README.md                                   # This file
```

## Dependencies

- **OkHttp**: HTTP client for API calls
- **Gson**: JSON parsing and serialization
- **SLF4J**: Logging framework
- **Apache Commons Configuration**: Configuration management
- **JUnit**: Testing framework (for future tests)

## APIs Used

- **OpenWeatherMap API**: Weather data and geocoding
- **NewsAPI**: News articles and headlines

## Error Handling

The application includes comprehensive error handling for:
- Invalid zipcode formats
- Network connectivity issues
- API rate limits and errors
- Missing or invalid API keys
- Malformed API responses

## Troubleshooting

### Common Issues

1. **"API key not found" error**
   - Ensure you've added your API keys to `config.properties`
   - Check that the keys are valid and active

2. **"Network error" or "Connection timeout"**
   - Check your internet connection
   - Verify that your firewall isn't blocking the application

3. **"Invalid zipcode" error**
   - Ensure you're using a valid US zipcode format (12345 or 12345-6789)
   - Try a different zipcode to verify the service is working

4. **"No news found" message**
   - This is normal for some locations with limited local news
   - The app will fall back to general US news if no local news is available

### Build Issues

1. **Maven build fails**
   - Ensure you have Java 11+ and Maven 3.6+ installed
   - Check your internet connection for dependency downloads

2. **"Class not found" errors**
   - Make sure all dependencies are downloaded: `mvn clean install`
   - Verify your Java version: `java -version`

## Future Enhancements

- Weather forecast (5-day forecast)
- Graphical user interface (GUI)
- Weather alerts and notifications
- Historical weather data
- News filtering by category
- Save favorite locations
- Export data to CSV/JSON

## License

This project is open source and available under the MIT License.

## Contributing

Feel free to submit issues, feature requests, or pull requests to improve the application.

## Support

If you encounter any issues or have questions, please check the troubleshooting section above or create an issue in the project repository. 

## Test 2
Test2