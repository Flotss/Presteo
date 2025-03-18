# Guide Spring Boot et JPA pour Presteo

Ce guide explique les concepts fondamentaux du développement backend avec Spring Boot et JPA utilisés dans l'application Presteo.

## Table des matières

1. [Introduction à Spring Boot](#introduction-à-spring-boot)
2. [Architecture en couches](#architecture-en-couches)
3. [Modèles et entités JPA](#modèles-et-entités-jpa)
4. [Repositories et JPA](#repositories-et-jpa)
5. [Services](#services)
6. [Contrôleurs REST](#contrôleurs-rest)
7. [Data Transfer Objects (DTOs)](#data-transfer-objects-dtos)
8. [Gestion des exceptions](#gestion-des-exceptions)
9. [Configuration de l'application](#configuration-de-lapplication)
10. [Tests](#tests)

## Introduction à Spring Boot

Spring Boot est un framework qui simplifie le développement d'applications Java en fournissant un ensemble de fonctionnalités prêtes à l'emploi. Il est basé sur le framework Spring et adopte le principe "convention over configuration" (convention plutôt que configuration).

### Avantages de Spring Boot

- Configuration automatique basée sur les dépendances
- Serveur embarqué (Tomcat, Jetty, etc.)
- Démarrage rapide des applications
- Système de gestion des dépendances simplifié
- Métriques, contrôles de santé et configuration externalisée

### Composant principal : L'application

```java
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class PresteoApplication {
    public static void main(String[] args) {
        SpringApplication.run(PresteoApplication.class, args);
    }
}
```

L'annotation `@SpringBootApplication` combine plusieurs annotations :

- `@Configuration` : Permet de définir des beans Spring
- `@EnableAutoConfiguration` : Active la configuration automatique
- `@ComponentScan` : Analyse les composants dans le package de l'application

Dans notre cas, nous avons exclu la configuration automatique de sécurité (`SecurityAutoConfiguration.class`) pour configurer manuellement notre sécurité.

## Architecture en couches

L'application Presteo suit une architecture en couches classique de Spring :

1. **Couche Contrôleur** : Gère les requêtes HTTP
2. **Couche Service** : Contient la logique métier
3. **Couche Repository** : Interface avec la base de données
4. **Couche Modèle** : Représente les données et leur structure

### Exemple de flux de données

1. Une requête HTTP arrive sur un endpoint du contrôleur
2. Le contrôleur délègue au service approprié
3. Le service utilise un repository pour interagir avec la base de données
4. Les données sont récupérées/modifiées et transformées si nécessaire
5. Une réponse est renvoyée au client

## Modèles et entités JPA

JPA (Java Persistence API) est une spécification Java pour la gestion des données relationnelles. Dans Spring Boot, les entités JPA représentent des tables dans la base de données.

### Exemple d'entité : User

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    @Size(min = 3, max = 50, message = "Le nom d'utilisateur doit contenir entre 3 et 50 caractères")
    private String username;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    private String password;
}
```

### Annotations importantes

- `@Entity` : Désigne la classe comme une entité JPA
- `@Table` : Spécifie le nom de la table dans la base de données
- `@Id` : Marque le champ comme clé primaire
- `@GeneratedValue` : Définit la stratégie de génération de la clé primaire
- `@NotBlank`, `@Size`, `@Email` : Annotations de validation pour les contraintes de données
- `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor` : Annotations Lombok qui génèrent automatiquement getters, setters, constructeurs, etc.

## Repositories et JPA

Les repositories sont des interfaces qui étendent `JpaRepository` et fournissent des méthodes pour interagir avec la base de données.

### Exemple de repository : UserRepository

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Méthodes générées automatiquement par JpaRepository:
    // save(), findById(), findAll(), delete(), etc.

    // Méthodes personnalisées basées sur le nom (Spring Data JPA les implémente automatiquement)
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    // Requête JPQL personnalisée
    @Query("SELECT u FROM User u WHERE u.email LIKE %:domain")
    List<User> findByEmailDomain(String domain);
}
```

### Avantages de Spring Data JPA

- Génère automatiquement des implémentations pour les méthodes standard (CRUD)
- Permet de créer des requêtes personnalisées simplement en nommant les méthodes selon des conventions
- Supporte les requêtes JPQL et SQL natives via l'annotation `@Query`
- Gère les transactions automatiquement

### Méthodes de requête par convention de nommage

Spring Data JPA peut générer automatiquement des requêtes à partir du nom des méthodes :

- `findBy<AttributeName>` : trouve par attribut
- `findBy<AttributeName>And<AttributeName>` : trouve par attributs combinés
- `findBy<AttributeName>OrderBy<AttributeName>Asc/Desc` : trouve et trie les résultats

## Services

La couche service contient la logique métier de l'application et fait le lien entre les contrôleurs et les repositories.

### Exemple de service : UserService

```java
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Transactional
    public Optional<User> updateUser(Long id, User userDetails) {
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setUsername(userDetails.getUsername());
            existingUser.setEmail(userDetails.getEmail());
            return userRepository.save(existingUser);
        });
    }

    // Autres méthodes...
}
```

### Annotations importantes

- `@Service` : Indique que la classe est un bean de service
- `@RequiredArgsConstructor` : Génère un constructeur avec les champs finaux
- `@Transactional` : Gère les transactions de base de données
  - `readOnly = true` : Optimise les transactions en lecture seule

## Contrôleurs REST

Les contrôleurs définissent les endpoints REST et traitent les requêtes HTTP.

### Exemple de contrôleur : UserController

```java
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "API de gestion des utilisateurs")
public class UserController {

    private final UserService userService;

    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    @GetMapping
    @Operation(summary = "Obtenir tous les utilisateurs", description = "Récupère la liste de tous les utilisateurs enregistrés")
    @ApiResponse(responseCode = "200", description = "Liste des utilisateurs récupérée avec succès")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> userDTOs = userService.getAllUsers().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    @PostMapping
    @Operation(summary = "Créer un utilisateur", description = "Création d'un nouvel utilisateur dans le système")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Utilisateur créé avec succès"),
        @ApiResponse(responseCode = "400", description = "Données d'utilisateur invalides")
    })
    public ResponseEntity<UserDTO> createUser(
            @Parameter(description = "Données de l'utilisateur à créer")
            @Valid @RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(createdUser));
    }

    // Autres méthodes...
}
```

### Annotations importantes

- `@RestController` : Combine `@Controller` et `@ResponseBody`
- `@RequestMapping` : Définit l'URL de base pour tous les endpoints du contrôleur
- `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping` : Définit un endpoint pour une méthode HTTP spécifique
- `@RequestBody` : Lie le corps de la requête à un objet
- `@PathVariable` : Accède aux variables de chemin
- `@RequestParam` : Accède aux paramètres de requête
- `@Valid` : Déclenche la validation de l'objet
- `@Operation`, `@ApiResponses`, `@Parameter` : Annotations Swagger pour la documentation de l'API

## Data Transfer Objects (DTOs)

Les DTOs sont des objets qui transportent des données entre les couches de l'application, en particulier pour exposer uniquement les données nécessaires aux clients.

### Exemple de DTO : UserDTO

```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    // Le mot de passe n'est pas inclus pour des raisons de sécurité
}
```

### Avantages des DTOs

- Séparation des modèles de données internes et externes
- Contrôle des données exposées aux clients
- Évite l'exposition de données sensibles (comme les mots de passe)
- Permet d'adapter la représentation des données aux besoins spécifiques des vues

## Gestion des exceptions

La gestion centralisée des exceptions permet de traiter les erreurs de manière cohérente dans toute l'application.

### Exemple de gestionnaire d'exceptions : GlobalExceptionHandler

```java
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("errors", errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralExceptions(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("message", "Une erreur s'est produite");
        response.put("error", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Autres gestionnaires d'exceptions...
}
```

### Annotations importantes

- `@ControllerAdvice` : Permet de gérer les exceptions globalement pour tous les contrôleurs
- `@ExceptionHandler` : Spécifie le type d'exception à gérer

## Configuration de l'application

Spring Boot utilise des fichiers de propriétés pour configurer l'application.

### application.properties

```properties
spring.application.name=Presteo

# Configuration Database
spring.datasource.url=jdbc:postgresql://<host>:<port>/<database>?currentSchema=<schema>&sslmode=<sslmode>
spring.datasource.username=<username>
spring.datasource.password=<password>
spring.datasource.driver-class-name=org.postgresql.Driver

# Configuration JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

# Configuration du serveur
server.port=8080
server.error.include-message=always
server.error.include-binding-errors=always

# Configuration du logging
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE

# Configuration Swagger/OpenAPI
springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui
springdoc.enable-spring-security=false

```

### Profils de configuration

Spring Boot permet d'utiliser différents profils (dev, test, prod) pour configurer l'application selon l'environnement.

Activer un profil :

```java
@SpringBootTest
@ActiveProfiles("test") // Utilise le profil de test
class PresteoApplicationTests {
    // ...
}
```

## Tests

Les tests sont essentiels pour garantir la qualité du code et le bon fonctionnement de l'application.

### Types de tests

1. **Tests unitaires** : Testent des composants individuels (services, repositories)
2. **Tests d'intégration** : Testent l'interaction entre plusieurs composants
3. **Tests de l'API** : Testent les endpoints REST

### Exemple de test d'intégration

```java
@SpringBootTest
@ActiveProfiles("test")
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateUser() throws Exception {
        User user = new User(null, "testuser", "test@example.com", "password123");

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }
}
```

### Annotations de test importantes

- `@SpringBootTest` : Configure l'application pour les tests
- `@ActiveProfiles` : Active un profil spécifique pour les tests
- `@WebMvcTest` : Configure les tests pour les contrôleurs REST
- `@DataJpaTest` : Configure les tests pour les repositories
- `@MockBean` : Crée un mock pour un bean Spring
- `@BeforeEach` : Exécute du code avant chaque test

## Conclusion

Spring Boot et JPA offrent un cadre puissant et flexible pour développer des applications backend. En comprenant ces concepts fondamentaux, vous serez en mesure de maintenir et d'étendre l'application Presteo efficacement.

Les points clés à retenir sont :

- L'architecture en couches pour une séparation claire des responsabilités
- L'utilisation de JPA pour simplifier l'accès aux données
- Les DTOs pour contrôler les données exposées aux clients
- La gestion centralisée des exceptions pour une expérience utilisateur cohérente
- Les tests automatisés pour garantir la qualité du code

Ce guide devrait vous aider à comprendre les principes fondamentaux du backend de Presteo et vous permettre de contribuer au projet avec confiance.
