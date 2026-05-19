# AdaNext

Application de gestion de projets collaboratifs -- Ada Tech School, specialite Dev.

AdaNext repose sur le meme domaine metier que Lovelace Factory : organisations, projets, taches. L'objectif est de le reconstruire avec une stack differente pour comparer les deux approches.

---

## Prerequis

Avant de commencer, verifiez que vous avez les outils suivants installes.

| Outil | Version minimale | Verification |
|---|---|---|
| Java (JDK) | 21 | `java -version` |
| Maven | 3.9 | `mvn -version` |
| Node.js | 20 | `node -v` |
| npm | 10 | `npm -v` |
| Git | 2.x | `git --version` |

Pas besoin d'installer PostgreSQL pour demarrer en local. La base H2 est embarquee et se cree automatiquement au demarrage.

---

## Demarrage rapide

### 1. Forker et cloner le depot

Sur GitHub, cliquez sur **Fork** en haut a droite du depot, puis clonez votre fork :

```bash
git clone https://github.com/VOTRE_NOM/adanext.git
cd adanext
```

### 2. Lancer le back-end

```bash
mvn spring-boot:run
```

Le serveur demarre sur `http://localhost:8080`.

Pour verifier que tout fonctionne :

```bash
curl http://localhost:8080/swagger-ui.html
```

Vous devriez voir la documentation Swagger s'afficher dans votre navigateur.

### 3. Lancer le front-end

Dans un second terminal :

```bash
cd frontend
npm install
npm run dev
```

Le front-end demarre sur `http://localhost:5173`.

### 4. Ouvrir l'application

Rendez-vous sur `http://localhost:5173`, creez un compte et commencez a explorer.

---

## Variables d'environnement

En developpement (profil par defaut), aucune variable d'environnement n'est necessaire. La base H2 en memoire est configuree automatiquement.

Pour un deploiement en production (profil `prod`), copiez `.env.example` en `.env` et renseignez les valeurs :

```bash
cp .env.example .env
```

| Variable | Description | Exemple |
|---|---|---|
| `DATABASE_URL` | URL JDBC de la base PostgreSQL | `jdbc:postgresql://localhost:5432/adanext` |
| `DATABASE_USERNAME` | Identifiant PostgreSQL | `postgres` |
| `DATABASE_PASSWORD` | Mot de passe PostgreSQL | `changeme` |
| `PORT` | Port du serveur (optionnel) | `8080` |

Pour lancer avec le profil prod :

```bash
mvn spring-boot:run -Pprod
```

---

## Outils disponibles en developpement

| Outil | URL | Description |
|---|---|---|
| Application | `http://localhost:5173` | Interface React |
| API REST | `http://localhost:8080/api` | Back-end Spring Boot |
| Swagger UI | `http://localhost:8080/swagger-ui.html` | Documentation interactive de l'API |
| Console H2 | `http://localhost:8080/h2-console` | Base de donnees en memoire (dev uniquement) |

Pour la console H2, utilisez ces parametres de connexion :
- JDBC URL : `jdbc:h2:mem:adanext`
- Utilisateur : `sa`
- Mot de passe : (laisser vide)

---

## Lancer les tests

```bash
mvn test
```

Pour voir un rapport de couverture :

```bash
mvn verify
```

Le rapport JaCoCo est genere dans `target/site/jacoco/index.html`.

---

## Structure du projet

```
adanext/
  src/
    main/
      java/fr/adatechschool/adanext/
        config/       configuration Spring Security et OpenAPI
        controller/   endpoints REST (ce que l'API expose)
        dto/          objets de transfert request et response
        exception/    exceptions metier et handler global
        model/        entites JPA (les tables de la base)
        repository/   requetes base de donnees (Spring Data JPA)
        service/      logique metier
      resources/
        application.properties         configuration commune
        application-dev.properties     configuration developpement (H2)
        application-prod.properties    configuration production (PostgreSQL)
    test/
      java/fr/adatechschool/adanext/
        controller/   tests d'API (MockMvc)
        service/      tests unitaires (Mockito)
  frontend/
    src/
      components/   composants React reutilisables
      pages/        une page = une route
      services/     appels API
      styles/       variables CSS et styles globaux
```

Pour comprendre comment les couches s'articulent, consultez [docs/architecture.md](docs/architecture.md).

---

## Contribuer

Ce projet est un point de depart. Pour ajouter une fonctionnalite, consultez [docs/ajouter-une-feature.md](docs/ajouter-une-feature.md).
