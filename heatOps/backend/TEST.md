# 📘 HeatOps – Tests API (Postman + Newman)

Ce dossier contient l’ensemble des tests automatisés permettant de valider :

- le **soft delete** (`active = false`)
- le **multi‑tenant** (`tenantFilter`)
- les **opérations CRUD essentielles**
- la **cohérence des données** après suppression logique
- la **stabilité du backend** HeatOps

Les tests sont exécutables :

- **manuellement** via Postman  
- **automatiquement** via Newman  
- **en CI/CD** via GitHub Actions

---

# 📁 Structure du dossier

postman/
├── HeatOps-SoftDelete-Tests.postman_collection.json
├── HeatOps-Local.postman_environment.json
└── TEST.md


---

# 🚀 1. Installation & Pré‑requis

### Backend HeatOps
Démarrer le backend :

mvn spring-boot:run


### Base de données
La base doit contenir :

- un tenant valide
- un utilisateur pour générer un token
- les tables créées par Flyway
- éventuellement les **données de test** (V8)

---

# 🧪 2. Exécuter les tests dans Postman

### 1. Importer la collection
Importer :

postman/HeatOps-SoftDelete-Tests.postman_collection.json

### 2. Importer l’environnement
Importer :

postman/HeatOps-Local.postman_environment.json


### 3. Renseigner les variables

| Variable | Description |
|---------|-------------|
| `baseUrl` | URL du backend (ex: http://localhost:8080) |
| `tenantId` | UUID du tenant |
| `token` | JWT obtenu via `/auth/login` |

### 4. Lancer la collection
Dans Postman → **Run Collection**

Les tests valident automatiquement :

- création d’entités
- visibilité avant suppression
- soft delete via DELETE
- invisibilité après suppression
- persistance en base (`active = false`)

---

# ⚙️ 3. Exécuter les tests via Newman (CLI)

Installer Newman :
npm install -g newman

Lancer les tests :
newman run postman/HeatOps-SoftDelete-Tests.postman_collection.json \
-e postman/HeatOps-Local.postman_environment.json


---

# 🤖 4. Exécution automatique via GitHub Actions

Le workflow CI se trouve dans :

github/workflows/api-tests.yml


Il :

- installe Node
- installe Newman
- exécute la collection
- génère un rapport JUnit
- publie le rapport comme artefact

Pour lancer les tests automatiquement :

- pousser sur `main`
- ou ouvrir une Pull Request

---

# 🧩 5. Jeu de données de test (optionnel)

Un script Flyway est disponible pour précharger :

- un tenant
- des clients
- des techniciens
- des équipements
- des interventions

Fichier recommandé :

V8__test_data.sql


---

# 🧼 6. Nettoyage des données

Pour réinitialiser la base :
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

Puis relancer l’application pour que Flyway recrée les tables.

---

# 🏁 7. Résultat attendu

À la fin des tests :

- les entités supprimées **n’apparaissent plus** dans les listes
- mais restent accessibles via GET direct (`active = false`)
- le tenantFilter empêche tout accès inter‑tenant
- le backend est validé pour le MVP

---

# 📎 Notes

- Aucun test ne supprime physiquement les données
- Tous les tests sont **tenant‑safe**
- Les IDs sont stockés dans les variables Postman
- Le workflow CI peut être enrichi avec Docker si besoin  


