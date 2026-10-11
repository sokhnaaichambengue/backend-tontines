# Exemples de Requêtes HTTP - Système d'Authentification

## 1. Création d'un Administrateur (Base de Données)

```sql
-- Insérer un administrateur
INSERT INTO users (nom, phone, password, logged_in) 
VALUES ('Admin Principal', '+221771234567', 'admin123', FALSE);
```

---

## 2. Test: Connexion Administrateur

### Requête
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "phone": "+221771234567",
    "password": "admin123"
  }'
```

### Réponse (Succès)
```json
{
  "success": true,
  "id": 1,
  "nom": "Admin Principal",
  "phone": "+221771234567",
  "role": "ADMIN",
  "nextPage": "DASHBOARD_ADMIN",
  "message": "Connexion administrateur réussie"
}
```

### Réponse (Erreur - Mauvais Password)
```json
{
  "success": false,
  "message": "Connexion refusée. Numéro de téléphone ou mot de passe incorrect."
}
```

---

## 3. Création d'un Nouveau Membre par l'Admin

### Requête
```bash
curl -X POST http://localhost:8080/membre/create \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Marie Diallo",
    "telephone": "+221779876543",
    "type": "premium"
  }'
```

### Réponse (Succès)
```json
{
  "success": true,
  "id": 5,
  "nom": "Marie Diallo",
  "telephone": "+221779876543",
  "code": "123456",
  "type": "premium",
  "actif": true,
  "message": "Membre créé avec succès. Code: 123456"
}
```

### Réponse (Erreur - Téléphone déjà utilisé)
```json
{
  "success": false,
  "message": "Ce numéro de téléphone est déjà utilisé"
}
```

---

## 4. Test: Connexion Membre (Flux 1/2)

### Requête
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "phone": "+221779876543",
    "password": "n'importe quel mot de passe"
  }'
```

### Réponse (Succès)
```json
{
  "success": true,
  "id": 5,
  "nom": "Marie Diallo",
  "phone": "+221779876543",
  "role": "MEMBRE",
  "nextPage": "ENTER_CODE",
  "message": "Connexion membre réussie. Veuillez saisir votre code.",
  "memberId": 5
}
```

---

## 5. Test: Vérification du Code Membre (Flux 2/2)

### Requête
```bash
curl -X POST http://localhost:8080/auth/verify-code \
  -H "Content-Type: application/json" \
  -d '{
    "code": "123456"
  }'
```

### Réponse (Succès)
```json
{
  "success": true,
  "id": 5,
  "nom": "Marie Diallo",
  "phone": "+221779876543",
  "code": "123456",
  "type": "premium",
  "nextPage": "DASHBOARD_MEMBRE",
  "message": "Code accepté. Bienvenue!"
}
```

### Réponse (Erreur - Code invalide)
```json
{
  "success": false,
  "message": "Code invalide ou expiré"
}
```

---

## 6. Cas d'Erreur - Numéro de Téléphone Inexistant

### Requête
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "phone": "+221700000000",
    "password": "n'importe quel mot de passe"
  }'
```

### Réponse
```json
{
  "success": false,
  "message": "Connexion refusée. Numéro de téléphone ou mot de passe incorrect."
}
```

---

## 7. Cas d'Erreur - Champs Manquants

### Requête
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "phone": "+221779876543"
  }'
```

### Réponse
```json
{
  "success": false,
  "message": "Numéro de téléphone et mot de passe requis"
}
```

---

## Flux Complet - Frontend

### 1️⃣ Page de Connexion Unique
- Utilisateur saisit son numéro de téléphone et mot de passe
- **Endpoint**: `POST /auth/login`

### 2️⃣ Détermination du Rôle (Backend)
- Si ADMIN → Redirection DASHBOARD_ADMIN
- Si MEMBRE → Redirection PAGE_SAISIE_CODE
- Si Erreur → Message d'erreur

### 3️⃣ Pour les Membres: Saisie du Code
- Utilisateur entre le code reçu de l'admin
- **Endpoint**: `POST /auth/verify-code`

### 4️⃣ Accès au Dashboard
- Si code valide → Redirection DASHBOARD_MEMBRE
- Si code invalide → Message d'erreur + nouvelle saisie

---

## Notes pour le Frontend

1. **Stockage du Token/Session**
   - Enregistrer l'`id`, `role` et `memberId` après connexion
   - Utiliser ces données pour les appels API ultérieurs

2. **Gestion des Pages**
   - `nextPage` indique où rediriger l'utilisateur
   - `success` indique si l'opération a réussi

3. **Erreurs**
   - Toutes les réponses d'erreur incluent `"success": false` et un `message`
   - Afficher le message d'erreur à l'utilisateur

4. **Tests Postman**
   - Importer les requêtes ci-dessus dans Postman
   - Mettre à jour les valeurs de `phone`, `password` et `code` selon vos tests

