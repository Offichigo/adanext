# Ajouter une fonctionnalite

Ce guide explique pas a pas comment ajouter une nouvelle fonctionnalite a AdaNext, en suivant l'architecture du projet.

L'exemple utilise "assigner une tache a un membre" -- une feature marquee `TODO` dans le code.

---

## Etape 1 : Modifier l'entite si necessaire

Si la fonctionnalite necessite de stocker de nouvelles donnees, modifiez l'entite concernee dans `model/`.

Pour l'assignation, on ajoute un champ `assignee` dans `Task.java` :

```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "assignee_id")
private User assignee;
```

Ajoutez le getter et le setter correspondants.

---

## Etape 2 : Mettre a jour les DTOs

Dans `dto/request/`, ajoutez les champs necessaires dans la requete (ou creez une nouvelle classe si c'est une nouvelle operation).

Dans `dto/response/`, ajoutez les champs que l'API doit retourner.

Pour l'assignation, on ajoute `assigneeId` dans `UpdateTaskStatusRequest`, ou on cree un `AssignTaskRequest` avec un champ `userId`.

---

## Etape 3 : Ajouter la logique dans le service

Dans le service concerne (`service/`), ajoutez une methode qui :

1. Recupere les entites necessaires depuis les repositories
2. Verifie les regles metier (l'utilisateur cible est-il bien membre de l'organisation ?)
3. Effectue l'operation
4. Retourne un DTO de reponse

```java
@Transactional
public TaskResponse assignTask(Long taskId, Long userId, String callerEmail) {
    Task task = findTaskById(taskId);
    checkMembership(task.getProject().getOrganization().getId(), callerEmail);

    User assignee = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable : " + userId));

    boolean assigneeIsMember = memberRepository.existsByOrganizationIdAndUserId(
        task.getProject().getOrganization().getId(), userId
    );
    if (!assigneeIsMember) {
        throw new AccessDeniedException("Cet utilisateur n'est pas membre de l'organisation");
    }

    task.setAssignee(assignee);
    return TaskResponse.from(task);
}
```

---

## Etape 4 : Exposer le endpoint dans le controller

Dans le controller concerne (`controller/`), ajoutez une methode avec l'annotation HTTP appropriee.

```java
@PatchMapping("/{taskId}/assignee")
@Operation(summary = "Assigner une tache a un membre")
public ResponseEntity<TaskResponse> assign(
    @PathVariable Long projectId,
    @PathVariable Long taskId,
    @Valid @RequestBody AssignTaskRequest request,
    @AuthenticationPrincipal UserDetails currentUser
) {
    TaskResponse response = taskService.assignTask(taskId, request.getUserId(), currentUser.getUsername());
    return ResponseEntity.ok(response);
}
```

---

## Etape 5 : Ecrire les tests

Dans `src/test/java/`, ajoutez des tests pour le service :

- Le cas nominal (la fonctionnalite fonctionne)
- Les cas d'erreur (ressource introuvable, acces refuse)

Consultez `TaskServiceTest.java` pour un exemple de structure.

---

## Etape 6 : Appeler le nouvel endpoint depuis le front-end

Dans `frontend/src/services/api.js`, ajoutez la fonction d'appel :

```javascript
assignTask: (projectId, taskId, userId) =>
  request(`/projects/${projectId}/tasks/${taskId}/assignee`, {
    method: 'PATCH',
    body: JSON.stringify({ userId }),
  }),
```

Puis utilisez cette fonction dans le composant React qui en a besoin.

---

## Recap : ou poser le code

| Quoi | Ou |
|---|---|
| Nouvelle table ou nouveau champ | `model/` |
| Nouvelle requete BDD | `repository/` |
| Nouvelle logique metier | `service/` |
| Nouveau endpoint REST | `controller/` |
| Nouveaux champs dans les requetes/reponses | `dto/` |
| Nouvel appel API cote front | `frontend/src/services/api.js` |
| Nouveau composant visuel | `frontend/src/components/` |
| Nouvelle page | `frontend/src/pages/` + route dans `App.jsx` |
