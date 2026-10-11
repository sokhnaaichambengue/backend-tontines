# Logique d'Authentification et d'Autorisation

## Vue d'ensemble

Le système utilise une logique d'authentification unique où tous les utilisateurs (administrateurs et membres) se connectent via une même page de connexion. Le rôle de l'utilisateur est déterminé après la connexion en fonction de ses identifiants.

## Flux de Connexion

### 1. Page de Connexion Unique
- **Endpoint**: `POST /auth/login`
- **Paramètres**: 
  - `phone` (String): Numéro de téléphone
  - `password` (String): Mot de passe

### 2. Détermination du Rôle

#### Cas 1: ADMINISTRATEUR
**Condition**: 
- Le numéro de téléphone existe dans la table `users` (administrateurs)
- Le mot de passe correspond exactement

**Action**:
- Retourner `role: "ADMIN"`
- Rediriger vers le tableau de bord administrateur (DASHBOARD_ADMIN)

**Réponse**:
```json
{
  "success": true,
  "id": 1,
  "nom": "Jean Admin",
  "phone": "+221771234567",
  "role": "ADMIN",
  "nextPage": "DASHBOARD_ADMIN",
  "message": "Connexion administrateur réussie"
}
```

#### Cas 2: MEMBRE
**Condition**:
- Le numéro de téléphone existe dans la table `membres` (membres)
- Le membre est actif (`actif = true`)
- Le mot de passe n'est PAS utilisé pour l'identification (n'importe quel mot de passe fonctionne)

**Action**:
- Retourner `role: "MEMBRE"`
- Rediriger vers la page de saisie du code (`ENTER_CODE`)

**Réponse**:
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

#### Cas 3: ERREUR
**Condition**:
- Le numéro de téléphone n'existe dans aucune table
- Ou le mot de passe ne correspond pas (pour les administrateurs)

**Action**:
- Refuser la connexion avec un message d'erreur

**Réponse**:
```json
{
  "success": false,
  "message": "Connexion refusée. Numéro de téléphone ou mot de passe incorrect."
}
```

---

## Flux d'Accès aux Tontines pour les Membres

### Étape 1: Login du Membre
- Membre se connecte avec son numéro de téléphone (enregistré par l'admin)
- N'importe quel mot de passe est accepté
- Reçoit `nextPage: "ENTER_CODE"`

### Étape 2: Saisie du Code
- **Endpoint**: `POST /auth/verify-code`
- **Paramètres**: 
  - `code` (String): Code généré par l'administrateur

- Le code doit correspondre à un code dans la table `membres`
- Le membre associé au code doit être actif

### Étape 3: Accès au Dashboard
Si le code est valide:
- **Réponse**:
```json
{
  "success": true,
  "id": 5,
  "nom": "Marie Diallo",
  "phone": "+221779876543",
  "code": "123456",
  "type": "type_du_membre",
  "nextPage": "DASHBOARD_MEMBRE",
  "message": "Code accepté. Bienvenue!"
}
```
- Redirection vers le tableau de bord membre (DASHBOARD_MEMBRE)

---

## Création d'un Nouveau Membre (Admin)

### Endpoint: `POST /membre/create`

**Paramètres**:
```json
{
  "nom": "Moussa Sarr",
  "telephone": "+221771234567",
  "type": "type_du_membre"
}
```

**Processus**:
1. Vérifier que le nom et le téléphone sont fournis
2. Vérifier que le numéro de téléphone n'existe pas déjà
3. Générer automatiquement un code unique (6 chiffres)
4. Sauvegarder le membre en base de données
5. Retourner le code généré

**Réponse Succès**:
```json
{
  "success": true,
  "id": 6,
  "nom": "Moussa Sarr",
  "telephone": "+221771234567",
  "code": "654321",
  "type": "type_du_membre",
  "actif": true,
  "message": "Membre créé avec succès. Code: 654321"
}
```

---

## Résumé des Tables

### Table: `users` (Administrateurs)
| Champ | Type | Description |
|-------|------|-------------|
| id | Long | Identifiant unique |
| nom | String | Nom complet |
| phone | String | Numéro de téléphone unique |
| password | String | Mot de passe de l'admin |
| logged_in | Boolean | Statut de connexion |

### Table: `membres` (Membres de Tontines)
| Champ | Type | Description |
|-------|------|-------------|
| id | Long | Identifiant unique |
| nom | String | Nom complet |
| telephone | String | Numéro de téléphone (enregistré par l'admin) |
| code | String | Code unique généré automatiquement |
| type | String | Type de membre |
| actif | Boolean | Si le membre est actif |

---

## Points Clés à Retenir

1. **Seul le phone détermine le rôle** - Le password n'est utilisé que pour l'authentification des administrateurs

2. **Mot de passe du membre ignoré** - N'importe quel mot de passe fonctionne pour les membres

3. **Code requis pour l'accès** - Les membres doivent fournir le code reçu de l'admin

4. **Phone unique par rôle** - Un phone ne peut être admin OU membre, mais pas les deux

5. **Membres actifs seulement** - Les membres désactivés ne peuvent pas se connecter

---

## Sécurité et Validation

- Tous les champs requis sont validés (non null, non vides)
- Les codes doivent correspondre et le membre doit être actif
- Les erreurs sont explicites mais ne révèlent pas trop d'informations
- Les réponses incluent un statut `success` pour faciliter le traitement côté client

