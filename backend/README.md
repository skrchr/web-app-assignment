# TMDB Backend Service

## Environment Setup

### Required Configuration

The application requires a TMDB API key. Set it up using the `local.properties` file in the **project root** (not in the backend folder).

#### Step 1: Get your TMDB API Key

1. Go to [TMDB Settings](https://www.themoviedb.org/settings/api)
2. Create an account or log in
3. Request an API key (choose "Developer" option)
4. Copy your API key

#### Step 2: Configure local.properties

1. In the **project root** directory (`web-app-assignment/`), open `local.properties`
2. Replace `YOUR_API_KEY_HERE` with your actual API key:

```properties
tmdb.api.key=your_actual_api_key_here
```

**Note:** The `local.properties` file is ignored by git, so your API key stays private.

### Running the Application

```bash
# Make sure local.properties is configured first!
# Then run the application
mvn spring-boot:run
```

### Running Tests

#### Unit Tests (Fast, No API Key Required)

```bash
mvn test -Dtest=TmdbApiServiceTest
```

#### Integration Tests (Requires API Key)

**Prerequisites:** Configure `local.properties` as described above.

````bash
# Run integration tests
mvn test -Dtest=TmdbApiServiceIntegrationTest

#### All Tests

```bash
# Make sure local.properties is configured first!
mvn test
````

### IDE Support (VS Code / Cursor)

The `local.properties` configuration works seamlessly with VS Code, Cursor, and other IDEs. No additional IDE configuration needed - just set up the `local.properties` file and run tests directly from your IDE.

## Development

The application uses Spring Boot with the following features:

- REST API for TMDB movie data
- Unit tests with TDD approach
- Docker support
- Clean architecture pattern
