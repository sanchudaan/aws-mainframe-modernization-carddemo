# CardDemo Java 21 Application

This is the Java 21 migration of the CardDemo COBOL mainframe application, originally running on IBM Enterprise COBOL 6.3 with CICS, DB2, and VSAM.

## Project Structure

```
carddemo-java/
├── carddemo-common/     # Shared utilities, constants, converters
├── carddemo-core/       # Domain entities, repositories, services
├── carddemo-api/        # REST API controllers and configuration
├── carddemo-batch/      # Spring Batch jobs
├── docker-compose.yml   # Docker orchestration
├── Dockerfile           # API application container
└── Dockerfile.batch     # Batch application container
```

## Technology Stack

- **Java 21** with virtual threads support
- **Spring Boot 3.2** for application framework
- **Spring Data JPA** for database access (replaces VSAM/DB2)
- **Spring Security 6** for authentication (replaces RACF)
- **Spring Batch 5** for batch processing (replaces JCL/COBOL batch)
- **PostgreSQL 16** for production database
- **H2** for development/testing
- **Redis** for caching and session management
- **OpenAPI/Swagger** for API documentation

## Migration Mapping

### CICS Transactions to REST Endpoints

| CICS Transaction | Description | REST Endpoint |
|-----------------|-------------|---------------|
| CC00 | Sign On | POST /api/users/auth |
| CM00 | Main Menu | GET /api/ |
| CAVW | Account View | GET /api/accounts/{id} |
| CAUP | Account Update | PUT /api/accounts/{id} |
| CALI | Account List | GET /api/accounts |
| CCLI | Card List | GET /api/cards |
| CTLI | Transaction List | GET /api/transactions |
| CTAD | Transaction Add | POST /api/transactions |

### COBOL Batch Programs to Spring Batch Jobs

| COBOL Program | Description | Spring Batch Job |
|--------------|-------------|------------------|
| CBACT01C | Account Processing | accountReportJob |
| CBTRN02C | Transaction Post | transactionPostJob |

### VSAM Files to JPA Entities

| VSAM File | Description | JPA Entity |
|-----------|-------------|------------|
| USRSEC | User Security | User |
| ACCTDAT | Account Data | Account |
| CARDDAT | Card Data | Card |
| CUSTDAT | Customer Data | Customer |
| CCXREF | Card Cross Reference | CardCrossReference |
| TRANSACT | Transactions | Transaction |

## Building the Application

### Prerequisites

- Java 21 JDK
- Gradle 8.5+

### Build Commands

```bash
# Build all modules
./gradlew build

# Build API application
./gradlew :carddemo-api:bootJar

# Build Batch application
./gradlew :carddemo-batch:bootJar

# Run tests
./gradlew test
```

## Running the Application

### Development Mode (H2 Database)

```bash
# Run API application
./gradlew :carddemo-api:bootRun

# Run Batch application
./gradlew :carddemo-batch:bootRun
```

### Docker Mode (PostgreSQL)

```bash
# Start all services
docker-compose up -d

# Start with batch profile
docker-compose --profile batch up -d

# View logs
docker-compose logs -f carddemo-api

# Stop all services
docker-compose down
```

## API Documentation

Once the application is running, access the Swagger UI at:
- http://localhost:8080/swagger-ui.html

## Configuration

### Application Properties

| Property | Description | Default |
|----------|-------------|---------|
| server.port | API server port | 8080 |
| spring.datasource.url | Database URL | H2 in-memory |
| spring.jpa.hibernate.ddl-auto | Schema generation | create-drop |

### Environment Variables (Docker)

| Variable | Description |
|----------|-------------|
| SPRING_PROFILES_ACTIVE | Active profile (docker) |
| SPRING_DATASOURCE_URL | PostgreSQL connection URL |
| SPRING_DATASOURCE_USERNAME | Database username |
| SPRING_DATASOURCE_PASSWORD | Database password |

## Migration Notes

This application was migrated from IBM Enterprise COBOL 6.3 following the comprehensive migration plan. Key considerations:

1. **Data Type Conversion**: COBOL packed decimal (COMP-3) converted to Java BigDecimal
2. **Transaction Semantics**: CICS pseudo-conversational model replaced with stateless REST
3. **Screen Handling**: BMS maps replaced with REST API + JSON responses
4. **File Access**: VSAM KSDS replaced with JPA repositories
5. **Batch Processing**: JCL/COBOL batch replaced with Spring Batch

## License

Apache License 2.0
