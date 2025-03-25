# Milestone 1: Project Architecture & Technology Stack

## Table of Contents

1. [Introduction](#introduction)
2. [Technologies, Frameworks & APIs](#technologies-frameworks--apis)
   - [Backend Stack](#backend-stack)
   - [Frontend Stack](#frontend-stack)
   - [Technology Justifications](#technology-justifications)
3. [Backend Architecture](#backend-architecture)
   - [MVC Pattern](#mvc-pattern)
   - [Layered Architecture](#layered-architecture)
   - [Data Flow](#data-flow)
4. [Frontend Overview](#frontend-overview)
   - [Nuxt.js Architecture](#nuxtjs-architecture)
   - [Component Structure](#component-structure)
   - [API Communication](#api-communication)
5. [Conclusion](#conclusion)

## Introduction

Presteo is a home services booking platform that connects service providers with customers. This document presents the first milestone of the project, focusing on the technical architecture, technology choices, and implementation strategy.

## Technologies, Frameworks & APIs

### Backend Stack

- **Java 21**: Latest LTS version offering enhanced performance and modern language features
- **Spring Boot**: Framework for creating Java applications with minimal configuration
- **Spring Data JPA/Hibernate**: ORM framework for database interactions
- **Spring Security**: Authentication and authorization framework
- **PostgreSQL**: Relational database management system
- **Swagger/OpenAPI**: API documentation
- **Lombok**: Reduces boilerplate code
- **Gradle**: Build tool and dependency management

### Frontend Stack

- **Nuxt.js 3**: Vue.js framework with server-side rendering capabilities
- **Vue.js**: Progressive JavaScript framework for building user interfaces
- **TypeScript**: Typed superset of JavaScript for better developer experience
- **Tailwind CSS**: Utility-first CSS framework
- **Pinia**: State management for Vue.js applications
- **Fetch API**: Native browser API for making HTTP requests

### Technology Justifications

#### Backend Choices

1. **Spring Boot**:
   - Rapid development with auto-configuration
   - Production-ready features out of the box
   - Vast ecosystem and community support
   - Built-in support for REST APIs, security, and database access

2. **PostgreSQL**:
   - ACID compliance and reliability
   - Advanced features (JSON support, complex queries)
   - Excellent performance for relational data
   - Open-source with strong community backing

3. **JPA/Hibernate**:
   - Abstracts database operations
   - Reduces boilerplate SQL code
   - Provides type-safe queries
   - Handles complex entity relationships efficiently

#### Frontend Choices

1. **Nuxt.js**:
   - Server-side rendering for better SEO and performance
   - Automatic routing based on file structure
   - Well-structured project organization
   - Modern development experience with hot reloading

2. **TypeScript**:
   - Type safety reduces runtime errors
   - Better IDE support and code completion
   - Enhanced code readability and maintainability
   - Facilitates collaboration in team environments

3. **Tailwind CSS**:
   - Utility-first approach speeds up development
   - Highly customizable design system
   - Reduces CSS bloat with purging
   - Consistent component styling across the application

4. **Pinia**:
   - Modern alternative to Vuex
   - TypeScript support
   - Simpler API with Composition API integration
   - Modular store design

## Backend Architecture

The backend follows a classic layered architecture based on Spring MVC principles, providing a clear separation of concerns.

### MVC Pattern

While Spring Boot applications don't strictly follow the traditional MVC pattern, our implementation adapts the concept:

1. **Model**: Represented by JPA entities in the `model` package
2. **View**: Our RESTful API serves JSON instead of views
3. **Controller**: REST controllers handle HTTP requests and responses

### Layered Architecture

The backend is organized into the following layers:

1. **Controller Layer** (`controller` package):
   - Handles HTTP requests and responses
   - Maps requests to service methods
   - Performs basic validation and error handling
   - Returns appropriate HTTP status codes

   ```java
   @RestController
   @RequestMapping("/api/users")
   public class UserController {
       private final UserService userService;
       
       // CRUD operations, error handling, etc.
   }
   ```

2. **Service Layer** (`service` package):
   - Contains business logic
   - Orchestrates operations involving multiple entities
   - Handles transactions
   - Implements domain rules

   ```java
   @Service
   public class UserService {
       private final UserRepository userRepository;
       
       @Transactional
       public User createUser(User user) {
           // Business logic, validation, etc.
       }
       
       // Other methods...
   }
   ```

3. **Repository Layer** (`repository` package):
   - Interfaces with the database
   - Extends JpaRepository for CRUD operations
   - Contains custom query methods

   ```java
   @Repository
   public interface UserRepository extends JpaRepository<User, Long> {
       Optional<User> findByUsername(String username);
       // Custom query methods...
   }
   ```

4. **Model Layer** (`model` package):
   - JPA entities representing database tables
   - Contains field validations and relationships

   ```java
   @Entity
   @Table(name = "users")
   public class User {
       @Id
       @GeneratedValue(strategy = GenerationType.IDENTITY)
       private Long id;
       
       @NotBlank
       private String username;
       
       // Other fields and relationships...
   }
   ```

5. **DTO Layer** (`dto` package):
   - Data Transfer Objects for API communication
   - Separates internal and external representations
   - Protects sensitive information

   ```java
   @Data
   @Builder
   public class UserDTO {
       private Long id;
       private String username;
       private String email;
       // Password intentionally excluded
   }
   ```

### Data Flow

A typical request in our system follows this path:

1. HTTP request arrives at a controller endpoint
2. Controller validates request parameters and body
3. Controller delegates to appropriate service method
4. Service implements business logic and interacts with repositories
5. Repository performs database operations
6. Service processes the results and returns to controller
7. Controller transforms data to DTOs if needed and sends HTTP response

## Frontend Overview

### Nuxt.js Architecture

The frontend is built with Nuxt.js 3, which provides a well-structured Vue.js application with server-side rendering capabilities.

#### Project Structure

```
Front/
├── src/                  # Main source code
│   ├── app.vue           # Main application component
│   ├── components/       # Reusable Vue components
│   ├── composables/      # Shared composable functions (like useApi)
│   ├── pages/            # Application routes (auto-generated)
│   └── utils/            # Utility functions
├── public/               # Static assets
├── assets/               # Resources to be processed (CSS, etc.)
└── nuxt.config.ts        # Nuxt configuration
```

### Component Structure

Components are built using Vue.js 3's Composition API with `<script setup>` for concise and readable code:

```vue
<template>
  <!-- UI elements -->
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
// Component logic using Composition API
</script>

<style scoped>
/* Component-specific styles */
</style>
```

#### Key Components:

1. **Layout Components**: Header, Footer, etc.
2. **Page Components**: Map to application routes
3. **UI Components**: Buttons, forms, cards, etc.
4. **Feature Components**: Specific to business features

### API Communication

The frontend communicates with the backend using a custom `useApi` composable that wraps the Fetch API:

```typescript
export function useApi<T>(endpoint: string) {
  const data = ref<T | null>(null);
  const loading = ref(false);
  const error = ref<string | null>(null);

  const env = useEnvironment();
  const baseUrl = env.apiBaseUrl;

  const fetchData = async () => {
    // Implementation details
  };

  const postData = async (payload: any) => {
    // Implementation details
  };

  // Additional methods: putData, deleteData, etc.

  return {
    data,
    loading,
    error,
    fetchData,
    postData,
    // Other methods
  };
}
```

This pattern provides several benefits:
- Consistent error handling
- Loading state management
- Centralized API URL configuration
- Reusable across components
- Type safety with generics

#### Environment Configuration

The application uses a runtime configuration system for managing environment-specific settings:

```typescript
// nuxt.config.ts
export default defineNuxtConfig({
  runtimeConfig: {
    apiSecret: process.env.API_SECRET,
    public: {
      apiBaseUrl: process.env.NUXT_PUBLIC_API_BASE_URL || 'http://localhost:8080/api',
      appName: process.env.NUXT_PUBLIC_APP_NAME || 'Presteo',
      // Other public configuration
    }
  },
  // Additional configuration
})
```

This approach allows for:
- Environment-specific configuration
- Secure handling of sensitive information
- Easy access to configuration throughout the application

## Conclusion

The Presteo application is built on modern, robust technologies that provide a solid foundation for development. The backend uses Spring Boot with a layered architecture following MVC principles, while the frontend leverages Nuxt.js for a structured, performant user interface.

This architecture ensures:
- Separation of concerns
- Scalability and maintainability
- Type safety across the stack
- Excellent developer experience
- Modern user experience

In the next milestone, we will focus on implementing core business features, enhancing the UI/UX, and establishing the testing framework.