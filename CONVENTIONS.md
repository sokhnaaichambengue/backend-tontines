# Conventions et Normes du Projet

## 🏗️ Architecture et Structure

### Dossiers Principaux
```
src/main/java/backend/com/backend/
├── controller/       # API endpoints
├── service/         # Logique métier
├── entities/        # Modèles JPA
├── repository/      # Accès à la base de données
└── dto/            # Objets de transfert de données
```

### Nommage des Fichiers
- **Controllers**: `*Controller.java` (ex: `AuthController`)
- **Services**: `*Service.java` (ex: `AuthService`)
- **Entities**: `*.java` (ex: `User.java`, `Membre.java`)
- **Repositories**: `*Repository.java` (ex: `UserRepository`)
- **DTOs**: `*Response.java`, `*Request.java` (ex: `LoginResponse`)

---

## 📝 Conventions de Code

### Controllers
```java
@RestController
@RequestMapping("/endpoint")
@CrossOrigin("*")  // À remplacer par URLs spécifiques en production
public class MyController {
    
    @Autowired
    private MyService myService;
    
    @PostMapping("/action")
    public ResponseEntity<?> action(@RequestBody Map<String, String> body) {
        // Implementation
    }
}
```

### Services
```java
@Service
public class MyService {
    
    @Autowired
    private MyRepository repository;
    
    public Optional<MyEntity> findById(Long id) {
        // Implementation
    }
}
```

### Entities
```java
@Entity
@Table(name = "my_table")
public class MyEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    // Constructeurs, Getters/Setters
}
```

---

## 🔐 Sécurité - Conventions

### Validation des Entrées
```java
// À FAIRE - Toujours valider les entrées
if(phone == null || phone.isBlank()) {
    return ResponseEntity.badRequest().body(createErrorResponse("Phone requis"));
}
```

### Gestion des Réponses
```java
// TOUJOURS utiliser ResponseEntity avec statut HTTP approprié
ResponseEntity.ok(data);           // 200
ResponseEntity.badRequest().body(data);  // 400
ResponseEntity.status(401).body(data);   // 401
ResponseEntity.status(403).body(data);   // 403
```

### Logs de Sécurité
```java
// À IMPLÉMENTER - Loggez les tentatives de connexion
logger.info("Connexion attempt for phone: {}", phone);
logger.warn("Failed login attempt for phone: {}", phone);
```

---

## 📊 Conventions de Base de Données

### Nommage des Tables
- Nom au pluriel: `users`, `membres`
- Snake_case pour les colonnes: `created_at`, `logged_in`
- Toujours avoir une `id` primaire

### Colonnes Standards
```sql
CREATE TABLE my_table (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    ...
);
```

### Index et Performances
```sql
-- Toujours indexer les colonnes fréquemment recherchées
CREATE INDEX idx_users_phone ON users(phone);
CREATE INDEX idx_membres_telephone ON membres(telephone);
```

---

## 🔄 Processus de Déploiement

### Développement
```bash
# 1. Faire les modifications
# 2. Compiler
mvn clean compile

# 3. Tester localement
mvn spring-boot:run

# 4. Vérifier les tests
mvn test
```

### Production
```bash
# 1. Compiler et packager
mvn clean package

# 2. Lancer le JAR
java -jar target/backend-0.0.1-SNAPSHOT.jar

# 3. Vérifier les logs
tail -f logs/app.log
```

---

## 📝 API Response Standards

### Succès
```json
{
  "success": true,
  "message": "Opération réussie",
  "data": { ... }
}
```

### Erreur
```json
{
  "success": false,
  "message": "Description de l'erreur"
}
```

### HTTP Status Codes à Utiliser
| Code | Utilisation | Exemple |
|------|-------------|---------|
| 200 | Succès | Login réussi |
| 201 | Créé | Nouvelle ressource créée |
| 400 | Mauvaise requête | Paramètre invalide |
| 401 | Non autorisé | Credentials incorrects |
| 403 | Interdit | Accès refusé |
| 404 | Non trouvé | Ressource inexistante |
| 500 | Erreur serveur | Exception non gérée |

---

## ✅ Checklist de Code Review

Avant de faire un commit, vérifiez:

- [ ] Le code compile sans erreurs
- [ ] Pas d'avertissements du compilateur
- [ ] Validation des entrées en place
- [ ] Gestion des erreurs appropriée
- [ ] Pas de hardcoding de URLs/configs
- [ ] Documentation/commentaires ajoutés
- [ ] Tests unitaires créés/passent
- [ ] Pas de credentials en dur
- [ ] Pas de logs sensibles
- [ ] Code formaté correctement

---

## 🔍 Debugging - Tips et Tricks

### Activer les Logs
```properties
# application.properties
logging.level.root=INFO
logging.level.backend.com.backend=DEBUG
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %msg%n
```

### Tester Rapidement
```bash
# Utiliser curl pour tester sans frontend
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"phone":"+221771234567","password":"test"}'
```

### Voir les Requêtes SQL
```properties
# application.properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

---

## 📚 Références et Ressources

### Documentation Locale
- [AUTHENTICATION_LOGIC.md](AUTHENTICATION_LOGIC.md) - Logique d'authentification
- [HTTP_EXAMPLES.md](HTTP_EXAMPLES.md) - Exemples d'API
- [DATABASE_SCHEMA.md](DATABASE_SCHEMA.md) - Schéma BD
- [FRONTEND_INTEGRATION_GUIDE.md](FRONTEND_INTEGRATION_GUIDE.md) - Frontend

### Documentation Externe
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Spring Security](https://spring.io/projects/spring-security)
- [Java Documentation](https://docs.oracle.com/en/java/)

---

## 🚀 Performance - Bonnes Pratiques

### Requêtes BD Optimisées
```java
// ❌ Mauvais - Requête N+1
List<User> users = userRepository.findAll();
for (User user : users) {
    System.out.println(user.getName());  // Requête par user
}

// ✅ Bon - Une seule requête avec fetch
@Query("SELECT u FROM User u LEFT JOIN FETCH u.profile")
List<User> findAllWithProfile();
```

### Utiliser le Caching
```java
@Cacheable("users")
public Optional<User> findByPhone(String phone) {
    return userRepository.findByPhone(phone);
}
```

### Connection Pooling
```properties
# application.properties
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
```

---

## 🔐 Sécurité - Checklist

Avant d'aller en production:

- [ ] Pas de passwords en clair (bcrypt)
- [ ] CORS configuré correctement (pas de "*")
- [ ] HTTPS activé
- [ ] JWT ou sessions implémentées
- [ ] Rate limiting en place
- [ ] SQL Injection prévenue (préparé statements)
- [ ] XSS prévenu (validation inputs)
- [ ] CSRF token en place
- [ ] Secrets en variables d'environnement
- [ ] Logs de sécurité activés

---

## 📊 Metrics et Monitoring

### À Implémenter
```java
// Micrometer pour les metrics
@Timed(value = "auth.login", description = "Temps de login")
public ResponseEntity<?> login(...) {
    // ...
}
```

### À Logger
```java
logger.info("User login: phone={}, role={}", phone, role);
logger.warn("Failed login attempt: phone={}, reason={}", phone, reason);
logger.error("System error in login", exception);
```

---

## 🧪 Testing Standards

### Tests Unitaires
```java
@SpringBootTest
public class AuthServiceTest {
    
    @Autowired
    private AuthService authService;
    
    @Test
    public void testLoginAsAdminSuccess() {
        // Arrange
        String phone = "+221771234567";
        String password = "password123";
        
        // Act
        Optional<User> result = authService.loginAsAdmin(phone, password);
        
        // Assert
        assertTrue(result.isPresent());
    }
}
```

### Tests d'Intégration
```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {
    
    @Test
    public void testLoginEndpoint() {
        // Test complet du endpoint
    }
}
```

---

## 📋 Checklist de Mise en Production

- [ ] Configuration de base de données configurée
- [ ] CORS configuré pour le domaine spécifique
- [ ] Secrets en variables d'environnement
- [ ] HTTPS activé
- [ ] Logging et monitoring en place
- [ ] Backups de base de données configurés
- [ ] Rate limiting implémenté
- [ ] Tests en environnement de staging réussis
- [ ] Documentation mise à jour
- [ ] Équipe entraînée aux procédures

---

## 📞 Support Interne

Pour les questions sur:

**Architecture**: Consultez ce fichier et [AUTHENTICATION_LOGIC.md](AUTHENTICATION_LOGIC.md)
**Implémentation**: Consultez le code source et les commentaires
**Tests**: Consultez les fichiers de test
**Déploiement**: Consultez l'équipe DevOps

---

**Dernière mise à jour**: 2026-06-25  
**Validé par**: Architecture Team  
**Version**: 1.0

