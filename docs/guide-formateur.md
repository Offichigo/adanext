# Guide pour les formateurices

Ce document explique les intentions pedagogiques du projet, les choix techniques qui ont ete faits et pourquoi, et les pistes d'extension que l'on peut proposer aux apprenantes.

---

## Ce que ce projet est

AdaNext est un projet de depart (starter) pour la specialite Dev d'Ada Tech School. Il couvre le meme domaine metier que Lovelace Factory (organisations, projets, taches) mais avec une stack Java Spring Boot + Hibernate + React.

Les apprenantes forkent ce depot. Elles disposent d'une base fonctionnelle -- authentification, CRUD organisations/projets/taches, kanban -- et doivent l'etendre.

---

## Ce que ce projet n'est pas

- Un projet complet et production-ready
- Un modele a suivre aveugllement -- certains choix sont simplifies intentionnellement
- Un projet avec une couverture de tests exhaustive (c'est un objectif de l'Arc 3)

---

## Choix techniques et justifications

### Spring Security session (pas JWT)

Le choix de la session HTTP plutot que du JWT est pedagogique. Une session est plus simple a comprendre au debut : l'utilisateur se connecte, Spring stocke son identite, chaque requete porte automatiquement le cookie de session. Pas de gestion manuelle de token.

Le JWT est un exercice possible pour les apprenantes plus avancees.

### Architecture en couches (pas hexagonale)

Le projet utilise une architecture en couches classique (controller -> service -> repository -> model). C'est l'architecture la plus commune en Spring Boot et la plus documentee. L'architecture hexagonale ou ports-and-adapters serait pertinente a aborder verbalement en soutenance, mais aurait rendu le code de depart trop difficile a lire.

### H2 en developpement

H2 est une base de donnees embarquee en memoire. Elle ne necessite aucune installation, demarre avec l'application et se detruit a l'arret. C'est ideal pour ne pas bloquer les apprenantes sur la configuration de PostgreSQL au debut. La migration vers PostgreSQL est un exercice naturel (configuration du profil `prod`, variables d'environnement).

### DTOs separes des entites

Les classes `*Request` et `*Response` dans `dto/` sont distinctes des entites JPA dans `model/`. Ce choix evite deux problemes concrets :
- Exposer des champs sensibles (le mot de passe hache) par inadvertance
- Casser l'API quand on fait evoluer le schema de base de donnees

C'est un point qui merite discussion en soutenance.

### Pas de Lombok

Le projet n'utilise pas Lombok pour les getters/setters. C'est intentionnel : les apprenantes doivent voir et ecrire le code "boilerplate" une fois pour comprendre ce qu'il fait. Lombok peut etre introduit comme amelioration.

---

## Points d'attention lors des revues de code

- **Logique metier dans le controller** : toute logique qui va au-dela du routage et de la validation doit etre dans le service.
- **Acces direct au repository depuis le controller** : le controller ne doit jamais appeler un repository directement.
- **Entites retournees directement en reponse** : les apprenantes doivent retourner des DTOs, pas des entites JPA (risque de serialisation infinie, fuite de donnees).
- **Absence de `@Transactional`** : les methodes de service qui modifient des donnees doivent etre annotees.
- **Champs hardcodes** : les urls, secrets ou parametres de configuration ne doivent pas etre dans le code.

---

## Fonctionnalites volontairement absentes (extensions possibles)

Ces features sont des exercices naturels pour les apprenantes :

| Feature | Complexite | Concepts couverts |
|---|---|---|
| Assigner une tache a un membre | Faible | Relation ManyToOne, validation metier |
| Inviter un membre dans une organisation | Moyenne | Logique d'invitation, email ou lien |
| Supprimer une organisation / un projet | Faible | DELETE, cascade, confirmation |
| Modifier le nom / la description | Faible | PUT, validation partielle |
| Pagination des listes | Moyenne | Spring Data Pageable, query params |
| Migration vers JWT | Moyenne | Filtre, token, expiration |
| Deploiement sur Railway ou Render | Moyenne | Variables d'env, profil prod, PostgreSQL |
| Authentification via Google (OAuth2) | Elevee | Spring Security OAuth2 |

---

## Livrables associes (Arc 2 et Arc 3)

Ce projet alimente directement les livrables Moodle de la specialite Dev.

| Semaine | Livrable | Ce que ce projet fournit |
|---|---|---|
| S5 | Schema d'architecture | `docs/architecture.md` comme base de discussion |
| S6 | MCD | Relations dans `model/` |
| S7 | Premiere PR de feature | La PR d'extension choisie par l'apprenante |
| S8 | Auth / consolidation | La configuration Spring Security |
| S10 | Strategie de tests | Les tests existants comme modele |
| S13 | Build Maven | `pom.xml`, commandes documentees dans le README |
| S14 | Documentation technique | Ce repo complet |

---

## Swagger

La documentation Swagger est disponible sur `http://localhost:8080/swagger-ui.html` une fois le back-end lance. Toutes les routes sont documentees avec `@Operation` et `@Tag`. Les apprenantes peuvent l'utiliser pour tester l'API sans Postman.
