# Guide d'Intégration Frontend - API d'Authentification

## 📱 Intégration avec le Frontend

Ce guide explique comment intégrer l'API d'authentification backend avec votre frontend (React, Vue, Angular, etc.).

---

## 1️⃣ Connexion Unique (Page de Connexion)

### Structure HTML
```html
<form id="loginForm">
    <div class="form-group">
        <label>Numéro de Téléphone</label>
        <input type="tel" id="phone" name="phone" placeholder="+221771234567" required>
    </div>
    
    <div class="form-group">
        <label>Mot de Passe</label>
        <input type="password" id="password" name="password" required>
    </div>
    
    <button type="submit">Se Connecter</button>
    <div id="errorMessage" class="error"></div>
</form>
```

### Code JavaScript (Exemple)
```javascript
document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const phone = document.getElementById('phone').value;
    const password = document.getElementById('password').value;
    
    try {
        const response = await fetch('http://localhost:8080/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ phone, password })
        });
        
        const data = await response.json();
        
        if (data.success) {
            // Sauvegarder les données de l'utilisateur
            sessionStorage.setItem('userId', data.id);
            sessionStorage.setItem('userRole', data.role);
            sessionStorage.setItem('userName', data.nom);
            sessionStorage.setItem('userPhone', data.phone);
            
            if (data.role === 'ADMIN') {
                // Rediriger vers Dashboard Admin
                window.location.href = '/dashboard-admin';
            } else if (data.role === 'MEMBRE') {
                // Sauvegarder le memberId pour la vérification du code
                sessionStorage.setItem('memberId', data.memberId);
                
                // Rediriger vers la page de saisie du code
                window.location.href = '/enter-code';
            }
        } else {
            document.getElementById('errorMessage').textContent = data.message;
        }
    } catch (error) {
        document.getElementById('errorMessage').textContent = 'Erreur de connexion';
    }
});
```

### Code React (Exemple)
```jsx
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

export function LoginPage() {
    const [phone, setPhone] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();
    
    const handleLogin = async (e) => {
        e.preventDefault();
        setError('');
        
        try {
            const response = await fetch('http://localhost:8080/auth/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ phone, password })
            });
            
            const data = await response.json();
            
            if (data.success) {
                // Sauvegarder les données
                localStorage.setItem('userId', data.id);
                localStorage.setItem('userRole', data.role);
                localStorage.setItem('userName', data.nom);
                localStorage.setItem('userPhone', data.phone);
                
                if (data.role === 'ADMIN') {
                    navigate('/dashboard-admin');
                } else {
                    localStorage.setItem('memberId', data.memberId);
                    navigate('/enter-code');
                }
            } else {
                setError(data.message);
            }
        } catch (err) {
            setError('Erreur de connexion');
        }
    };
    
    return (
        <form onSubmit={handleLogin}>
            <input
                type="tel"
                placeholder="+221771234567"
                value={phone}
                onChange={(e) => setPhone(e.target.value)}
                required
            />
            <input
                type="password"
                placeholder="Mot de passe"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
            />
            <button type="submit">Se Connecter</button>
            {error && <div className="error">{error}</div>}
        </form>
    );
}
```

---

## 2️⃣ Page de Saisie du Code (Pour les Membres)

### Structure HTML
```html
<div class="code-input-container">
    <h2>Saisir votre code d'accès</h2>
    <p>Entrez le code que vous a fourni l'administrateur</p>
    
    <form id="codeForm">
        <input type="text" id="code" placeholder="000000" maxlength="6" required>
        <button type="submit">Valider</button>
        <div id="codeError" class="error"></div>
    </form>
</div>
```

### Code JavaScript (Exemple)
```javascript
document.getElementById('codeForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const code = document.getElementById('code').value;
    
    try {
        const response = await fetch('http://localhost:8080/auth/verify-code', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ code })
        });
        
        const data = await response.json();
        
        if (data.success) {
            // Code accepté - accéder au dashboard membre
            window.location.href = '/dashboard-membre';
        } else {
            document.getElementById('codeError').textContent = data.message;
        }
    } catch (error) {
        document.getElementById('codeError').textContent = 'Erreur de vérification';
    }
});
```

### Code React (Exemple)
```jsx
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

export function EnterCodePage() {
    const [code, setCode] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();
    
    const handleVerifyCode = async (e) => {
        e.preventDefault();
        setError('');
        
        try {
            const response = await fetch('http://localhost:8080/auth/verify-code', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ code })
            });
            
            const data = await response.json();
            
            if (data.success) {
                navigate('/dashboard-membre');
            } else {
                setError(data.message);
            }
        } catch (err) {
            setError('Erreur de vérification du code');
        }
    };
    
    return (
        <form onSubmit={handleVerifyCode}>
            <input
                type="text"
                placeholder="000000"
                maxLength="6"
                value={code}
                onChange={(e) => setCode(e.target.value)}
                required
            />
            <button type="submit">Valider le Code</button>
            {error && <div className="error">{error}</div>}
        </form>
    );
}
```

---

## 3️⃣ Dashboard Admin

### Page Création de Membre
```jsx
export function CreateMemberPage() {
    const [nom, setNom] = useState('');
    const [telephone, setTelephone] = useState('');
    const [type, setType] = useState('');
    const [generatedCode, setGeneratedCode] = useState(null);
    
    const handleCreateMember = async (e) => {
        e.preventDefault();
        
        try {
            const response = await fetch('http://localhost:8080/membre/create', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ nom, telephone, type })
            });
            
            const data = await response.json();
            
            if (data.success) {
                setGeneratedCode(data.code);
                alert(`Membre créé! Code: ${data.code}`);
                // Réinitialiser le formulaire
                setNom('');
                setTelephone('');
                setType('');
            } else {
                alert(`Erreur: ${data.message}`);
            }
        } catch (error) {
            alert('Erreur lors de la création du membre');
        }
    };
    
    return (
        <form onSubmit={handleCreateMember}>
            <input
                type="text"
                placeholder="Nom du membre"
                value={nom}
                onChange={(e) => setNom(e.target.value)}
                required
            />
            <input
                type="tel"
                placeholder="+221779876543"
                value={telephone}
                onChange={(e) => setTelephone(e.target.value)}
                required
            />
            <select value={type} onChange={(e) => setType(e.target.value)} required>
                <option value="">Sélectionner un type</option>
                <option value="basic">Basic</option>
                <option value="premium">Premium</option>
            </select>
            <button type="submit">Créer Membre</button>
            
            {generatedCode && (
                <div className="success">
                    <p>✅ Code généré: <strong>{generatedCode}</strong></p>
                    <button type="button" onClick={() => navigator.clipboard.writeText(generatedCode)}>
                        Copier le Code
                    </button>
                </div>
            )}
        </form>
    );
}
```

---

## 4️⃣ Gestion de la Session et du Contexte (React)

### Context API
```jsx
import { createContext, useContext, useState } from 'react';

const AuthContext = createContext();

export function AuthProvider({ children }) {
    const [user, setUser] = useState(() => {
        const stored = localStorage.getItem('user');
        return stored ? JSON.parse(stored) : null;
    });
    
    const login = (userData) => {
        setUser(userData);
        localStorage.setItem('user', JSON.stringify(userData));
    };
    
    const logout = () => {
        setUser(null);
        localStorage.removeItem('user');
    };
    
    return (
        <AuthContext.Provider value={{ user, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() {
    return useContext(AuthContext);
}
```

---

## 5️⃣ Protection des Routes

### Composant PrivateRoute
```jsx
import { Navigate } from 'react-router-dom';
import { useAuth } from './AuthContext';

export function PrivateRoute({ children, requiredRole }) {
    const { user } = useAuth();
    
    if (!user) {
        return <Navigate to="/login" />;
    }
    
    if (requiredRole && user.role !== requiredRole) {
        return <Navigate to="/unauthorized" />;
    }
    
    return children;
}
```

### Configuration des Routes
```jsx
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/login" element={<LoginPage />} />
                
                <Route 
                    path="/dashboard-admin" 
                    element={
                        <PrivateRoute requiredRole="ADMIN">
                            <DashboardAdmin />
                        </PrivateRoute>
                    }
                />
                
                <Route 
                    path="/enter-code" 
                    element={
                        <PrivateRoute requiredRole="MEMBRE">
                            <EnterCodePage />
                        </PrivateRoute>
                    }
                />
                
                <Route 
                    path="/dashboard-membre" 
                    element={
                        <PrivateRoute requiredRole="MEMBRE">
                            <DashboardMembre />
                        </PrivateRoute>
                    }
                />
                
                <Route path="*" element={<Navigate to="/login" />} />
            </Routes>
        </BrowserRouter>
    );
}
```

---

## 6️⃣ Service API Réutilisable

```javascript
// api.service.js
class ApiService {
    constructor(baseURL = 'http://localhost:8080') {
        this.baseURL = baseURL;
    }
    
    async request(endpoint, options = {}) {
        const response = await fetch(`${this.baseURL}${endpoint}`, {
            headers: {
                'Content-Type': 'application/json',
                ...options.headers
            },
            ...options
        });
        return response.json();
    }
    
    async login(phone, password) {
        return this.request('/auth/login', {
            method: 'POST',
            body: JSON.stringify({ phone, password })
        });
    }
    
    async verifyCode(code) {
        return this.request('/auth/verify-code', {
            method: 'POST',
            body: JSON.stringify({ code })
        });
    }
    
    async createMember(member) {
        return this.request('/membre/create', {
            method: 'POST',
            body: JSON.stringify(member)
        });
    }
}

export default new ApiService();
```

---

## 7️⃣ Gestion des Erreurs Globale

```jsx
// ErrorBoundary.jsx
class ErrorBoundary extends React.Component {
    constructor(props) {
        super(props);
        this.state = { hasError: false };
    }
    
    static getDerivedStateFromError(error) {
        return { hasError: true };
    }
    
    componentDidCatch(error, errorInfo) {
        console.error('Error:', error, errorInfo);
    }
    
    render() {
        if (this.state.hasError) {
            return <h1>Une erreur s'est produite</h1>;
        }
        return this.props.children;
    }
}
```

---

## 8️⃣ Stockage de la Session

### localStorage vs sessionStorage
```javascript
// À utiliser selon le besoin

// Pour une persistence longue (restent même après fermeture du navigateur)
localStorage.setItem('userRole', 'ADMIN');

// Pour une session de navigateur seulement (supprimés à la fermeture)
sessionStorage.setItem('userRole', 'ADMIN');

// Nettoyer la session
function logout() {
    localStorage.removeItem('userRole');
    localStorage.removeItem('userId');
    sessionStorage.clear();
}
```

---

## ✅ Checklist d'Intégration

- [ ] Page de connexion créée et connectée à `/auth/login`
- [ ] Redirection vers Dashboard Admin pour les ADMIN
- [ ] Redirection vers Enter Code pour les MEMBRES
- [ ] Page de saisie du code créée
- [ ] Vérification du code connectée à `/auth/verify-code`
- [ ] Dashboard Admin avec création de membres
- [ ] Dashboard Membre implémenté
- [ ] Sessions/Contexte configuré
- [ ] Routes protégées implémentées
- [ ] Gestion des erreurs complète
- [ ] Service API réutilisable créé
- [ ] Tests complétés

---

## 🔒 Points de Sécurité

1. **Toujours utiliser HTTPS en production**
2. **Ne jamais stocker les passwords en clair**
3. **Implémenter CORS correctement** (ne pas utiliser "*" en production)
4. **Ajouter JWT pour la sécurité des tokens**
5. **Valider tous les inputs côté frontend ET backend**
6. **Implémenter un rate limiter**
7. **Ajouter du logging et du monitoring**

---

## 📞 Troubleshooting

### Erreur CORS
```javascript
// Assurez-vous que le backend a CORS activé
// Backend (déjà fait avec @CrossOrigin("*"))
@CrossOrigin("*")
@RestController
```

### Session Perdue
- Vérifier que les données sont bien sauvegardées dans localStorage
- Implémenter un middleware pour valider le token
- Vérifier que la clé de stockage est correcte

### Code Non Valide
- Vérifier que le code est saisi correctement (case sensitive)
- Vérifier que le membre est actif en base de données
- Vérifier que le code n'a pas expiré (si implémenté)

