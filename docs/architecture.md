# Architecture d'AdaNext

Ce document explique comment le projet est organise et pourquoi chaque couche existe.

---

## Vue d'ensemble

AdaNext est une application web en trois parties :

```
Navigateur (React)
       |
       | HTTP / JSON
       |
Back-end (Spring Boot)
       |
       | SQL / JPA
       |
Base de donnees (H2 en dev, PostgreSQL en prod)
```

Le front-end et le back-end ne se connaissent que via l'API REST. Le back-end ne sait pas qu'il y a un front-end React -- il pourrait repondre a n'importe quel client (mobile, autre front, outil de test).

---

## Les couches du back-end

Le code Java est organise en 6 packages. Chaque package a une responsabilite precise.

### `model`

Les entites JPA. Chaque classe correspond a une table en base de donnees.

```
User             un compte utilisateur
Organization     une organisation (workspace)
OrganizationMember  lien entre un User et une Organization, avec un role
Project          un projet dans une organisation
Task             une tache dans un projet
```

Une entite JPA est une classe Java normale avec des annotations qui indiquent a Hibernate comment la mapper en SQL. Elle ne contient que les donnees et leurs relations -- aucune logique metier.

### `repository`

Les interfaces Spring Data JPA. Chaque interface donne acces aux operations de base de donnees pour une entite.

Spring Data JPA genere automatiquement les requetes SQL a partir des noms de methodes. Par exemple :

```java
List<Task> findAllByProjectId(Long projectId);
// genere : SELECT * FROM tasks WHERE project_id = ?
```

Pas besoin d'ecrire du SQL pour les cas simples.

### `service`

La logique metier. C'est ici que se trouve le code qui repond a des questions comme :
- "Un utilisateur peut-il voir ce projet ?"
- "Que se passe-t-il quand on cree une organisation ?"

Un service utilise les repositories pour lire et ecrire en base, et leve des exceptions si les regles metier ne sont pas respectees.

### `controller`

Les endpoints REST. Un controller recoit une requete HTTP, appelle le service correspondant, et retourne une reponse HTTP.

Un controller ne contient pas de logique metier -- il delegue tout au service.

```
POST /api/organizations      OrganizationController -> OrganizationService
GET  /api/organizations      OrganizationController -> OrganizationService
GET  /api/organizations/{id} OrganizationController -> OrganizationService
```

### `dto`

Les objets de transfert de donnees (Data Transfer Objects). Il y en a deux types :

- `request/` : ce que le client envoie (avec validation)
- `response/` : ce que l'API retourne

Les DTOs evitent d'exposer directement les entites JPA. Ca protege contre la fuite de donnees sensibles (mot de passe hashee par exemple) et permet de faire evoluer la BDD sans casser l'API.

### `exception`

Les exceptions metier et le handler global.

`GlobalExceptionHandler` intercepte toutes les exceptions et retourne une reponse JSON coherente. Sans lui, Spring retournerait une page HTML d'erreur illisible.

---

## Le cycle d'une requete

Exemple : `POST /api/organizations` (creer une organisation)

```
1. Le client envoie une requete HTTP avec un corps JSON
2. Spring Security verifie que l'utilisateur est connecte
3. OrganizationController recoit la requete
4. Il valide le corps avec @Valid (champs obligatoires, formats)
5. Il appelle OrganizationService.create(request, userEmail)
6. OrganizationService verifie les regles metier
7. Il utilise OrganizationRepository et OrganizationMemberRepository pour sauvegarder
8. Il retourne un OrganizationResponse (DTO)
9. OrganizationController envoie une reponse HTTP 201 avec le DTO en JSON
```

---

## Les relations entre entites

```
User  1----* OrganizationMember *----1 Organization
                                          |
                                          1
                                          |
                                          * Project
                                          |
                                          1
                                          |
                                          * Task
```

Un utilisateur peut appartenir a plusieurs organisations (via `OrganizationMember`).
Une organisation peut avoir plusieurs projets.
Un projet peut avoir plusieurs taches.

---

## Le front-end React

Le front-end est volontairement simple. Pas de Redux, pas de gestion d'etat complexe.

Chaque page charge ses donnees au montage (`useEffect`), les stocke dans un `useState`, et les affiche. Les appels API sont centralises dans `services/api.js`.

```
pages/         une page = une route = un ecran
components/    composants reutilisables (modal, colonne kanban, carte de tache)
services/      toutes les fonctions qui appellent l'API
styles/        variables CSS globales (couleurs, polices)
```

La navigation est geree par `react-router-dom`. Les routes sont definies dans `App.jsx`.
