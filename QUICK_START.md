# Guide de Démarrage Rapide - Authentification

## 🚀 Démarrage en 5 Minutes

### Étape 1: Préparer la Base de Données

```sql
-- Créer les tables
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    logged_in BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE membres (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(255) NOT NULL,
    telephone VARCHAR(20) NOT NULL UNIQUE,
    code VARCHAR(10) NOT NULL UNIQUE,
    type VARCHAR(50),
    actif BOOLEAN NOT NULL DEFAULT TRUE
);

-- Insérer un administrateur de test
INSERT INTO users (nom, phone, password, logged_in)
VALUES ('Admin Test', '+221771234567', 'password123', FALSE);
```

### Étape 2: Démarrer le Backend

```bash
cd c:\Users\hp\Downloads\backend\backend
mvn spring-boot:run
```

Le serveur démarre sur `http://localhost:8080`

### Étape 3: Tester l'API

#### Test 1: Connexion Admin
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"phone":"+221771234567","password":"password123"}'
```

**Réponse Attendue:**
```json
{
  "success": true,
  "role": "ADMIN",
  "nextPage": "DASHBOARD_ADMIN"
}
```

#### Test 2: Créer un Membre
```bash
curl -X POST http://localhost:8080/membre/create \
  -H "Content-Type: application/json" \
  -d '{
    "nom":"Ami Diallo",
    "telephone":"+221779876543",
    "type":"premium"
  }'
```

**Réponse Attendue:**
```json
{
  "success": true,
  "code": "123456",
  "message": "Membre créé avec succès. Code: 123456"
}
```

#### Test 3: Connexion Membre
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"phone":"+221779876543","password":"n'importe"}'
```

**Réponse Attendue:**
```json
{
  "success": true,
  "role": "MEMBRE",
  "nextPage": "ENTER_CODE"
}
```

#### Test 4: Vérifier le Code
```bash
curl -X POST http://localhost:8080/auth/verify-code \
  -H "Content-Type: application/json" \
  -d '{"code":"123456"}'
```

**Réponse Attendue:**
```json
{
  "success": true,
  "nextPage": "DASHBOARD_MEMBRE",
  "message": "Code accepté. Bienvenue!"
}
```

### Étape 4: Frontend - Structure Minimale

```html
<!DOCTYPE html>
<html>
<head>
    <title>Tontine</title>
</head>
<body>
    <div id="app">
        <!-- Page de connexion -->
        <form id="loginForm" style="display:block;">
            <h2>Connexion</h2>
            <input type="tel" id="phone" placeholder="+221771234567" required>
            <input type="password" id="password" placeholder="Mot de passe" required>
            <button>Se Connecter</button>
        </form>
        
        <!-- Page d'entrée du code -->
        <form id="codeForm" style="display:none;">
            <h2>Saisir le Code</h2>
            <input type="text" id="code" placeholder="000000" maxlength="6" required>
            <button>Valider</button>
        </form>
        
        <!-- Dashboard Admin -->
        <div id="dashboardAdmin" style="display:none;">
            <h2>Dashboard Admin</h2>
            <form id="createMemberForm">
                <input type="text" id="memberNom" placeholder="Nom" required>
                <input type="tel" id="memberTel" placeholder="Téléphone" required>
                <input type="text" id="memberType" placeholder="Type" required>
                <button>Créer Membre</button>
            </form>
            <div id="generatedCode"></div>
        </div>
        
        <!-- Dashboard Membre -->
        <div id="dashboardMembre" style="display:none;">
            <h2>Bienvenue Membre</h2>
            <p id="membreInfo"></p>
            <button onclick="logout()">Déconnexion</button>
        </div>
    </div>

    <script>
        // Configuration
        const API_BASE = 'http://localhost:8080';
        
        // Gestion de la connexion
        document.getElementById('loginForm').addEventListener('submit', async (e) => {
            e.preventDefault();
            const phone = document.getElementById('phone').value;
            const password = document.getElementById('password').value;
            
            try {
                const res = await fetch(`${API_BASE}/auth/login`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ phone, password })
                });
                const data = await res.json();
                
                if (data.success) {
                    localStorage.setItem('user', JSON.stringify(data));
                    
                    if (data.role === 'ADMIN') {
                        showDashboardAdmin();
                    } else {
                        showCodeForm();
                    }
                } else {
                    alert(data.message);
                }
            } catch (err) {
                alert('Erreur de connexion');
            }
        });
        
        // Gestion du code
        document.getElementById('codeForm').addEventListener('submit', async (e) => {
            e.preventDefault();
            const code = document.getElementById('code').value;
            
            try {
                const res = await fetch(`${API_BASE}/auth/verify-code`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ code })
                });
                const data = await res.json();
                
                if (data.success) {
                    showDashboardMembre();
                } else {
                    alert(data.message);
                }
            } catch (err) {
                alert('Erreur de vérification');
            }
        });
        
        // Création de membre
        document.getElementById('createMemberForm').addEventListener('submit', async (e) => {
            e.preventDefault();
            
            const member = {
                nom: document.getElementById('memberNom').value,
                telephone: document.getElementById('memberTel').value,
                type: document.getElementById('memberType').value
            };
            
            try {
                const res = await fetch(`${API_BASE}/membre/create`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(member)
                });
                const data = await res.json();
                
                if (data.success) {
                    document.getElementById('generatedCode').innerHTML = 
                        `<strong>Code généré: ${data.code}</strong>`;
                    document.getElementById('createMemberForm').reset();
                } else {
                    alert(data.message);
                }
            } catch (err) {
                alert('Erreur création membre');
            }
        });
        
        // Fonctions d'affichage
        function hideAll() {
            document.getElementById('loginForm').style.display = 'none';
            document.getElementById('codeForm').style.display = 'none';
            document.getElementById('dashboardAdmin').style.display = 'none';
            document.getElementById('dashboardMembre').style.display = 'none';
        }
        
        function showCodeForm() {
            hideAll();
            document.getElementById('codeForm').style.display = 'block';
        }
        
        function showDashboardAdmin() {
            hideAll();
            document.getElementById('dashboardAdmin').style.display = 'block';
        }
        
        function showDashboardMembre() {
            hideAll();
            const user = JSON.parse(localStorage.getItem('user'));
            document.getElementById('membreInfo').textContent = 
                `Bienvenue ${user.nom}!`;
            document.getElementById('dashboardMembre').style.display = 'block';
        }
        
        function logout() {
            localStorage.removeItem('user');
            location.reload();
        }
    </script>
</body>
</html>
```

---

## 🎯 Résumé Visuel

```
┌─────────────────────────────────────────────┐
│         APPLICATION TONTINE                 │
├─────────────────────────────────────────────┤
│                                             │
│  ┌─────────────────────────────────────┐  │
│  │    PAGE DE CONNEXION UNIQUE         │  │
│  │    (Phone + Password)               │  │
│  │                                     │  │
│  │  Phone: [________________]          │  │
│  │  Password: [________________]       │  │
│  │  [Se Connecter]                     │  │
│  └────────────────┬────────────────────┘  │
│                   │                        │
│      ┌────────────┴────────────┐          │
│      │                         │          │
│  ┌───▼────────┐        ┌──────▼──────┐  │
│  │   ADMIN    │        │   MEMBRE    │  │
│  ├────────────┤        ├─────────────┤  │
│  │ Dashboard  │        │ Saisir Code │  │
│  │ Admin      │        │  ┌────────┐ │  │
│  │ - Créer    │        │  │000000  │ │  │
│  │   Membres  │        │  └────────┘ │  │
│  │ - Générer  │        │[Valider]    │  │
│  │   Codes    │        └──────┬──────┘  │
│  └────────────┘               │         │
│                          ┌─────▼──────┐ │
│                          │  Dashboard │ │
│                          │  Membre    │ │
│                          └────────────┘ │
│                                        │
└─────────────────────────────────────────┘
```

---

## 📋 Fichiers Clés

| Fichier | Description |
|---------|-------------|
| `AuthService.java` | Logique d'authentification |
| `AuthController.java` | Endpoints `/auth/login` et `/auth/verify-code` |
| `MembreController.java` | Endpoint `/membre/create` |
| `AUTHENTICATION_LOGIC.md` | Documentation complète |
| `HTTP_EXAMPLES.md` | Exemples de requêtes |
| `DATABASE_SCHEMA.md` | Schéma des tables |
| `FRONTEND_INTEGRATION_GUIDE.md` | Guide intégration frontend |

---

## 🔧 Commandes Utiles

```bash
# Compiler le projet
mvn clean compile

# Exécuter les tests
mvn test

# Construire le JAR
mvn clean package

# Démarrer l'application
mvn spring-boot:run

# Accéder à la base de données
mysql -u root -p

# Voir les logs
tail -f target/logs/app.log
```

---

## ✅ Checklist Rapide

- [ ] Base de données créée
- [ ] Table `users` créée et remplie
- [ ] Table `membres` créée
- [ ] Backend démarré sur port 8080
- [ ] Tester `/auth/login` avec admin
- [ ] Tester `/membre/create` pour créer un membre
- [ ] Tester `/auth/login` avec membre
- [ ] Tester `/auth/verify-code` avec le code généré
- [ ] Frontend créé et connecté
- [ ] Redirection vers dashboards fonctionne
- [ ] Sessions sauvegardées correctement

---

## 💡 Conseil

Pour faciliter les tests, utilisez **Postman** ou **REST Client VS Code Extension** pour envoyer des requêtes HTTP sans écrire du code de frontend.

---

**Vous êtes maintenant prêt à démarrer! 🎉**

