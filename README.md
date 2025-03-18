# Presteo - Home Services Booking Platform

Presteo is a comprehensive web application for booking home maintenance and repair services. The platform connects service providers (plumbers, electricians, cleaners, etc.) with customers (homeowners or tenants) in a seamless booking experience.

## 🚀 Project Overview

Presteo facilitates the entire service booking and fulfillment process:

1. **Browse & Search** - Customers can easily browse service categories or search for specific services
2. **Provider Selection** - Compare service providers based on ratings, prices, and availability
3. **Booking Management** - Schedule appointments with real-time availability checking
4. **Payment Processing** - Secure payment handling with multiple payment options
5. **Service Execution** - Tracking of service status from booking to completion
6. **Review System** - Customers can leave feedback and ratings for service providers
7. **Admin Dashboard** - Platform management tools for administrators

## 🏗️ Project Structure

The project follows a modern client-server architecture:

```
Presteo/
├── Back/                  # Spring Boot backend
│   ├── src/               # Java source code
│   │   ├── main/          
│   │   │   ├── java/      # Application code
│   │   │   └── resources/ # Configuration files
│   │   └── test/          # Test files
│   ├── build.gradle       # Gradle build configuration
│   └── SpringBoot_JPA_Guide.md  # Backend documentation
│
├── Front/                 # Nuxt.js frontend
│   ├── src/               # TypeScript/Vue source files
│   │   ├── components/    # Reusable Vue components
│   │   ├── pages/         # Application pages (routes)
│   │   ├── composables/   # Shared composition functions
│   │   └── utils/         # Utility functions
│   ├── nuxt.config.ts     # Nuxt configuration
│   └── Frontend_Guide.md  # Frontend documentation
│
└── README.md              # You are here
```

## 🛠️ Technology Stack

### Backend
- **Framework**: Spring Boot
- **Language**: Java
- **Database**: JPA/Hibernate with PostgreSQL
- **Security**: Spring Security
- **API Documentation**: OpenAPI/Swagger
- **Build Tool**: Gradle

### Frontend
- **Framework**: Nuxt.js 3 (Vue.js)
- **Language**: TypeScript
- **Styling**: Tailwind CSS
- **State Management**: Pinia
- **API Communication**: Fetch API with custom composables

## 🚦 Getting Started

### Prerequisites
- Java 21 or higher
- Node.js 18 or higher
- PostgreSQL
- PNPM (recommended) or NPM

### Backend Setup

1. Navigate to the backend directory:
   ```bash
   cd Back
   ```

2. Copy the example properties file and configure your database:
   ```bash
   cp src/main/resources/application.properties.example src/main/resources/application.properties
   # Edit the application.properties file with your database credentials
   ```

3. Build and run the Spring Boot application:
   ```bash
   ./gradlew bootRun
   ```
   
   The API will be available at http://localhost:8080/api

### Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd Front
   ```

2. Install dependencies:
   ```bash
   pnpm install
   # or: npm install
   ```

3. Copy the environment file and configure as needed:
   ```bash
   cp .env.example .env
   # Edit the .env file if needed
   ```

4. Start the development server:
   ```bash
   pnpm dev
   # or: npm run dev
   ```
   
   The application will be available at http://localhost:3000

## 🔄 Key Features & Workflows

### For Customers
- Create an account and manage profile
- Browse service categories and providers
- Schedule appointments based on availability
- Track service status and history
- Make secure payments
- Rate and review service providers

### For Service Providers
- Create and manage service listings
- Set availability calendar
- Receive and accept bookings

### For Administrators
- Manage users and providers
- Handle disputes and issues
- Generate reports and analytics
- Configure system settings

## 📚 Documentation

Detailed documentation is available for both parts of the application:

- **Backend**: See [SpringBoot_JPA_Guide.md](Back/SpringBoot_JPA_Guide.md) for detailed information about the Spring Boot architecture, API endpoints, and data models.

- **Frontend**: See [Frontend_Guide.md](Front/Frontend_Guide.md) for comprehensive documentation about the Nuxt.js application structure, components, and communication patterns with the backend.

## 🧪 Testing

### Backend
```bash
cd Back
./gradlew test
```

### Frontend
```bash
cd Front
pnpm test
# or: npm run test
```

## 🚀 Deployment

### Backend
The Spring Boot application can be deployed as a JAR file to any Java-compatible server:

```bash
cd Back
./gradlew build
# Deploy the JAR file from build/libs/
```

### Frontend
Nuxt.js can be deployed as a static site or server-rendered application:

```bash
cd Front
pnpm build
# or: npm run build
```

## 👥 Contributors

This project was developed by ISEP students as part of their web technologies course.

## 📄 License

This project is for educational purposes only.