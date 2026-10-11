# Architecture et Schéma de Base de Données

## Diagramme du Flux d'Authentification

```
┌─────────────────────────────────────────────────────────────────┐
│                    PAGE DE CONNEXION UNIQUE                     │
│                   (phone + password requis)                     │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
          ┌──────────────────────────────────┐
          │    POST /auth/login              │
          │  Déterminer le rôle de l'user    │
          └───┬───────────────────────────┬──┘
              │                           │
        ┌─────▼──────────┐          ┌─────▼──────────┐
        │ ADMIN FOUND?   │          │ MEMBRE FOUND?  │
        │ (Table: users) │          │(Table: membres)│
        └──┬──────────┬──┘          └──┬──────────┬──┘
      OUI │          │ NON       OUI │          │ NON
         ▼           │               ▼           │
   ┌──────────────┐   │          ┌──────────────┐ │
   │ ADMIN Auth   │   │          │ MEMBRE FOUND │ │
   │Check Password│   │          │  (Actif=1)  │ │
   └──┬────────┬──┘   │          └──┬────────┬──┘ │
 OUI  │        │ NON  │        OUI  │        │NON │
     ▼        ▼       │            ▼        ▼    ▼
┌──────────┐┌───────┐  │       ┌──────────┐┌───────┐
│ SUCCESS  ││FAIL   │  │       │ CONTINUE ││ FAIL  │
│role:    ││401    │  │       │role:     ││401    │
│ADMIN    ││       │  │       │MEMBRE    ││       │
│DASH:    ││       │  │       │DASH:     ││       │
│ADMIN    ││       │  │       │ENTER_CODE││       │
└──────────┘└───────┘  │       └──────────┘└───────┘
                       │
         └─────────────┘

┌─────────────────────────────────────────────────────────────────┐
│              POUR LES ADMINISTRATEURS                           │
│         Accès Direct au Dashboard Admin (CREER MEMBRES)         │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│              POUR LES MEMBRES - ÉTAPE 2                         │
│            PAGE DE SAISIE DU CODE MEMBRE                        │
│                (code fourni par l'admin)                        │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
          ┌──────────────────────────────────┐
          │    POST /auth/verify-code        │
          │  Vérifier le code du membre      │
          └───┬───────────────────────────┬──┘
              │                           │
       OUI    │                           │ NON
              ▼                           ▼
         ┌──────────┐               ┌──────────┐
         │ VALID    │               │  INVALID │
         │ CODE     │               │   CODE   │
         │DASH:     │               │ ERROR    │
         │MEMBRE    │               │ RETRY    │
         └──────────┘               └──────────┘
```

---

## Schéma des Tables

### Table 1: `users` (Administrateurs)

```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    logged_in BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Index pour améliorer les recherches
CREATE INDEX idx_users_phone ON users(phone);
```

### Table 2: `membres` (Membres de Tontines)

```sql
CREATE TABLE membres (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(255) NOT NULL,
    telephone VARCHAR(20) NOT NULL UNIQUE,
    code VARCHAR(10) NOT NULL UNIQUE,
    type VARCHAR(50),
    actif BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Index pour améliorer les recherches
CREATE INDEX idx_membres_telephone ON membres(telephone);
CREATE INDEX idx_membres_code ON membres(code);
CREATE INDEX idx_membres_actif ON membres(actif);
```

---

## Relations et Contraintes

```
┌─────────────────────────────────────────────────────────────────┐
│                    POINTS CLÉS                                  │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│ 1. Un phone ne peut être que ADMIN ou MEMBRE (jamais les deux) │
│    - UNIQUE dans users                                         │
│    - UNIQUE dans membres                                       │
│    - Jamais le même phone dans les deux tables                 │
│                                                                 │
│ 2. Les codes sont générés automatiquement et uniques            │
│    - Format: 6 chiffres (ex: 123456)                           │
│    - UNIQUE dans membres                                       │
│                                                                 │
│ 3. Un membre doit être actif pour se connecter                 │
│    - actif = TRUE pour permettre la connexion                  │
│    - actif = FALSE pour désactiver un membre                   │
│                                                                 │
│ 4. Le password n'est vérifié que pour les ADMIN                │
│    - IGNORED pour les MEMBRES                                  │
│    - N'importe quel password fonctionne pour les membres       │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## Exemples de Données

### Users (Administrateurs)

| id | nom | phone | password | logged_in |
|----|-----|-------|----------|-----------|
| 1 | Jean Admin | +221771234567 | admin123 | false |
| 2 | Marie Admin | +221779876543 | secure456 | false |

### Membres

| id | nom | telephone | code | type | actif |
|----|-----|-----------|------|------|-------|
| 1 | Moussa Diop | +221771111111 | 854729 | premium | true |
| 2 | Fatou Sall | +221779999999 | 391527 | basic | true |
| 3 | Aliou Kone | +221770000000 | 645831 | premium | true |
| 4 | Awa Ndiaye | +221788888888 | 927463 | basic | false |

---

## Fluxes de Données

### Flux 1: Authentification Admin
```
FRONTEND                    BACKEND                    DATABASE
   │                           │                            │
   ├─ POST /auth/login ──────➤ │                            │
   │  {phone, password}        │                            │
   │                           ├─ Query users ────────────➤ │
   │                           │  WHERE phone = ?            │
   │                           │                            │
   │                           │ ◀─ User Found ────────────┤
   │                           │                            │
   │                           ├─ Check password            │
   │                           │  (matches?)                │
   │                           │                            │
   │ ◀─ Response OK ─────────┤                            │
   │  {role: ADMIN, ...}      │                            │
```

### Flux 2: Authentification Membre (Étape 1)
```
FRONTEND                    BACKEND                    DATABASE
   │                           │                            │
   ├─ POST /auth/login ──────➤ │                            │
   │  {phone, password}        │                            │
   │                           ├─ Query users ────────────➤ │
   │                           │  WHERE phone = ?            │
   │                           │                            │
   │                           │ ◀─ Not Found ────────────┤
   │                           │                            │
   │                           ├─ Query membres ─────────➤ │
   │                           │  WHERE telephone = ?        │
   │                           │    AND actif = 1            │
   │                           │                            │
   │                           │ ◀─ Membre Found ────────┤
   │                           │                            │
   │ ◀─ Response OK ─────────┤                            │
   │  {role: MEMBRE, ...}     │                            │
```

### Flux 3: Vérification Code Membre (Étape 2)
```
FRONTEND                    BACKEND                    DATABASE
   │                           │                            │
   ├─ POST /verify-code ─────➤ │                            │
   │  {code}                   │                            │
   │                           ├─ Query membres ─────────➤ │
   │                           │  WHERE code = ?             │
   │                           │    AND actif = 1            │
   │                           │                            │
   │                           │ ◀─ Membre Found ────────┤
   │                           │                            │
   │ ◀─ Response OK ─────────┤                            │
   │  {nextPage: DASHBOARD,..}│                            │
```

---

## Considérations de Sécurité

1. **Stockage des Passwords**
   - ⚠️ ATTENTION: Les passwords sont actuellement en clair
   - ✅ À FAIRE: Utiliser bcrypt ou un autre algorithme de hashing

2. **Validation des Codes**
   - Les codes doivent correspondre EXACTEMENT
   - Les codes devraient avoir une durée d'expiration (optionnel)

3. **Gestion des Sessions**
   - Implémenter JWT ou un système de session
   - Valider le token/session pour les endpoints protégés

4. **Rate Limiting**
   - Implémenter un rate limiter pour /auth/login
   - Limiter les tentatives de vérification du code

