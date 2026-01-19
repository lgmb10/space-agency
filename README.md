# space-agency

API REST – Space Agency (Spring Boot Training)

--------------------------------------------------
1. Contexte
--------------------------------------------------

On veut une API REST Spring Boot pour une “Space Agency” qui :

- gère des vaisseaux spatiaux
- gère des passagers / astronautes
- planifie des voyages (missions)
- gère les réservations
- sécurise l’accès (rôles)
- est testée (tests unitaires + tests d’intégration)

Rôles proposés :

- ADMIN : tout gérer
- PLANNER : gérer les missions et les réservations
- OPERATOR : consulter l’état, lancer/terminer des missions
- ASTRONAUT (ou CUSTOMER) : consulter ses voyages

Objectif pédagogique : manipuler les principaux starters Spring Boot :
- Web (REST)
- Data JPA
- Validation
- Security (avec rôles)
- Test (unitaires et intégration)
- Actuator (optionnel)

--------------------------------------------------
2. EPIC 0 – Infrastructure & Sécurité
--------------------------------------------------

2.1 User Story 0.1 – Authentification & JWT

En tant qu’utilisateur
je veux pouvoir m’authentifier via un login/mot de passe
afin d’obtenir un token JWT et d’appeler l’API de manière sécurisée.

Critères d’acceptation :
- Endpoint POST /auth/login qui retourne un JWT valide.
- Mot de passe stocké hashé (ex : BCrypt).
- Accès aux endpoints protégé par un header Authorization: Bearer <token>.
- Les appels sans JWT ou avec JWT invalide renvoient 401 Unauthorized.

Technos Spring Boot à utiliser :
- spring-boot-starter-security
- Mise en place d’un filtre JWT
- Configuration HttpSecurity (routes publiques / protégées)

Tests :
- Tests unitaires : service d’authentification, encodage du mot de passe.
- Tests d’intégration : appel à /auth/login avec MockMvc, vérification :
    - succès avec bons identifiants
    - 401 avec mauvais identifiants
    - accès /ships (par ex.) sans JWT => 401

--------------------------------------------------

2.2 User Story 0.2 – Gestion des rôles

En tant qu’administrateur
je veux gérer différents rôles (ADMIN, PLANNER, OPERATOR, ASTRONAUT)
afin de restreindre l’accès à certaines fonctionnalités de l’API.

Critères d’acceptation :
- Seul un ADMIN peut créer / modifier / supprimer des vaisseaux.
- La planification des missions est réservée aux rôles PLANNER ou ADMIN.
- La consultation de ses propres réservations est accessible au rôle ASTRONAUT.
- Les utilisateurs sans le rôle approprié reçoivent 403 Forbidden.

Technos :
- Annotations de sécurité (@PreAuthorize, @Secured) ou configuration des règles d’authorization.

Tests :
- Tests d’intégration :
    - Vérifier qu’un ADMIN peut accéder aux endpoints d’admin.
    - Vérifier qu’un PLANNER/OPERATOR/ASTRONAUT se voit refuser l’accès quand ce n’est pas son rôle.

--------------------------------------------------
3. EPIC 1 – Gestion des Vaisseaux
--------------------------------------------------

3.1 User Story 1.1 – Créer / Lister les vaisseaux

En tant qu’administrateur
je veux créer et consulter des vaisseaux
afin de gérer la flotte disponible pour les missions.

Détails métier :
Chaque vaisseau possède au minimum :
- id
- name (unique)
- capacity (nombre maximum de passagers)
- maxWeight (poids total maximum autorisé)
- status (ACTIVE, IN_MAINTENANCE, RETIRED)

Critères d’acceptation :
- POST /ships : crée un vaisseau avec validation des champs.
- GET /ships : renvoie une liste paginée de vaisseaux.
- GET /ships/{id} : renvoie le détail d’un vaisseau par son id.
- Le nom du vaisseau doit être unique.

Validation (Bean Validation) :
- name non vide (NotBlank) et longueur maximale (par ex. 100).
- capacity > 0 (Positive).
- maxWeight > 0 (Positive).

Technos Spring Boot :
- spring-boot-starter-web
- spring-boot-starter-data-jpa
- spring-boot-starter-validation

Tests :
- Tests unitaires : service de gestion des vaisseaux (création, validation métier simple).
- Tests d’intégration :
    - @DataJpaTest pour le repository de Ship.
    - MockMvc pour les endpoints REST (création OK, erreurs 400 en cas de mauvais input).

--------------------------------------------------

3.2 User Story 1.2 – Mettre à jour / Supprimer un vaisseau avec règles métier

En tant qu’administrateur
je veux pouvoir modifier ou supprimer un vaisseau
tout en respectant les contraintes liées aux missions planifiées.

Critères d’acceptation :
- PUT /ships/{id} : met à jour les informations du vaisseau.
- DELETE /ships/{id} :
    - Suppression impossible si le vaisseau est affecté à une mission future
      (mission avec statut PLANNED ou IN_PROGRESS et date >= aujourd’hui).
    - En cas de suppression impossible, renvoyer 409 CONFLICT avec un message explicite.

Technos :
- JPA : relation entre Ship et Mission.
- Gestion d’erreurs personnalisée via @ControllerAdvice.

Tests :
- Tests unitaires : règle métier “interdire la suppression si missions futures”.
- Tests d’intégration : création d’un vaisseau, association à une mission future, tentative de suppression => 409.

--------------------------------------------------
4. EPIC 2 – Gestion des Passagers / Astronautes
--------------------------------------------------

4.1 User Story 2.1 – CRUD Passagers

En tant que planner
je veux gérer la liste des passagers/astronautes
afin de préparer les missions spatiales.

Attributs d’un passager :
- id
- firstName
- lastName
- email (unique)
- weight
- medicalClearance (booléen ou date de validité)
- éventuellement : passportNumber

Critères d’acceptation :
- POST /passengers : crée un passager avec validations.
- GET /passengers : liste paginée, filtrable par nom ou email.
- GET /passengers/{id} : détail d’un passager.
- PUT /passengers/{id} : mise à jour.
- DELETE /passengers/{id} : suppression (sous réserve de règles métier éventuelles).

Validation :
- email au bon format, unique.
- weight > 0.
- medicalClearance doit être valide (true ou date non expirée).

Tests :
- Tests unitaires : règles métier (email unique, validation médicale).
- Tests d’intégration : création d’un passager, recherche, erreurs de validation 400 (email invalide, poids négatif, etc.).

--------------------------------------------------
5. EPIC 3 – Planification des Missions
--------------------------------------------------

5.1 User Story 3.1 – Créer une mission

En tant que planner
je veux créer une mission spatiale en liant un vaisseau, une date de départ et une destination
afin d’organiser les voyages.

Attributs d’une mission :
- id
- ship (référence à un vaisseau)
- departureDate
- arrivalDate
- origin
- destination
- status : PLANNED, IN_PROGRESS, COMPLETED, CANCELLED
- maxPassengers (optionnel, par défaut = ship.capacity)

Critères d’acceptation :
- POST /missions :
    - Vérifie que le vaisseau existe.
    - Vérifie que le vaisseau a le statut ACTIVE.
    - Vérifie qu’il n’existe pas déjà une mission avec le même vaisseau sur une plage de dates qui se chevauche.
    - departureDate < arrivalDate, dates cohérentes.
- Retourne 201 Created en cas de succès, 400/409 en cas de violation des règles métier.

Tests :
- Tests unitaires :
    - Vérification du non-chevauchement des missions pour un même vaisseau.
    - Vérification que le vaisseau est actif.
- Tests d’intégration :
    - Création d’une mission valide.
    - Tentative de création d’une mission avec chevauchement => 409.

--------------------------------------------------

5.2 User Story 3.2 – Changer le statut d’une mission

En tant qu’operator
je veux pouvoir démarrer, terminer ou annuler une mission
pour suivre le cycle de vie des voyages.

Critères d’acceptation :
- PATCH /missions/{id}/status (ou endpoints dédiés comme /start, /complete, /cancel) :
    - Transition PLANNED -> IN_PROGRESS possible uniquement si departureDate <= maintenant.
    - Transition IN_PROGRESS -> COMPLETED autorisée.
    - Transition IN_PROGRESS -> CANCELLED autorisée.
    - Il est interdit de repasser à PLANNED depuis un autre statut.
- En cas de transition invalide, renvoyer 400 Bad Request ou 409 Conflict.

Tests :
- Tests unitaires : mise en place d’une machine à états (statuts autorisés).
- Tests d’intégration : vérifier les transitions valides et invalides via les endpoints.

--------------------------------------------------
6. EPIC 4 – Réservations & Remplissage des Vaisseaux
--------------------------------------------------

6.1 User Story 4.1 – Ajouter un passager à une mission (côté back-office)

En tant que planner
je veux ajouter des passagers à une mission
afin de remplir le vaisseau sans dépasser ses limites.

Critères d’acceptation :
- Endpoint POST /missions/{id}/passengers (corps = passengerId, ou un DTO).
- Règles métier :
    - La mission doit être en statut PLANNED.
    - Le passager doit avoir une medicalClearance valide.
    - Le nombre total de passagers ne doit pas dépasser ship.capacity ou maxPassengers.
    - Le poids total des passagers ne doit pas dépasser ship.maxWeight.
    - Un même passager ne peut pas être ajouté deux fois à la même mission.
- En cas de dépassement de capacité/poids, renvoyer 409 CONFLICT.

Modélisation :
- Soit une relation ManyToMany entre Mission et Passenger,
- Soit une entité de jointure Booking (recommandé pour évoluer plus tard : statut de réservation, prix, etc.).

Tests :
- Tests unitaires :
    - Fonction de calcul du poids total et de la capacité restante.
    - Vérification des règles (clearance, doublon).
- Tests d’intégration :
    - Scénario d’ajout de passagers jusqu’à la limite puis échec au-delà.

--------------------------------------------------

6.2 User Story 4.2 – Réserver un vol côté “client” (ASTRONAUT)

En tant qu’astronaut (ou client)
je veux réserver une place sur une mission disponible
afin de planifier mon voyage spatial.

Critères d’acceptation :
- GET /missions/available :
    - Liste des missions en statut PLANNED.
    - Uniquement celles qui ont encore des places disponibles.
- POST /bookings :
    - Associe l’utilisateur authentifié (rôle ASTRONAUT) et une mission.
    - Applique les mêmes règles que pour l’ajout de passager (capacité, poids, clearance).
    - Empêche l’utilisateur de réserver deux fois la même mission (conflit 409).

Tests :
- Tests d’intégration :
    - Réservation OK avec un utilisateur ayant le rôle ASTRONAUT.
    - Tentative de réservation avec rôle non autorisé => 403 Forbidden.
    - Double réservation de la même mission => 409 Conflict.

--------------------------------------------------
7. EPIC 5 – Consultation & Reporting
--------------------------------------------------

7.1 User Story 5.1 – Liste des missions d’un passager (profil)

En tant qu’astronaut
je veux voir la liste de mes missions passées et futures
afin de suivre mon historique de vols spatiaux.

Critères d’acceptation :
- GET /me/missions :
    - Retourne les missions associées au compte connecté.
    - Inclut au minimum : destination, dates, statut (PLANNED, COMPLETED, etc.).

Tests :
- Tests d’intégration :
    - Un utilisateur A voit uniquement ses missions, pas celles d’un autre utilisateur.

--------------------------------------------------

7.2 User Story 5.2 – Tableau de bord des missions (pour les opérateurs)

En tant qu’operator
je veux voir un résumé des missions (par statut, par destination)
afin de suivre l’activité de la flotte.

Critères d’acceptation :
- GET /dashboard/missions :
    - Nombre de missions par statut (PLANNED, IN_PROGRESS, COMPLETED, CANCELLED).
    - Total des passagers par destination.
- Optionnel : filtrer par période (dates de départ) ou par vaisseau.

Tests :
- Tests unitaires : agrégation/queries dans la couche service ou repository.
- Tests d’intégration : vérifier les statistiques à partir de données insérées en base.

--------------------------------------------------
8. EPIC 6 – Monitoring & Qualité
--------------------------------------------------

8.1 User Story 6.1 – Healthcheck & Metrics

En tant qu’admin système
je veux vérifier facilement que l’API fonctionne correctement
afin de détecter rapidement les problèmes.

Critères d’acceptation :
- Actuator activé avec au minimum :
    - /actuator/health
    - /actuator/info
- Optionnel : ajouter une métrique personnalisée, par exemple :
    - nombre de missions IN_PROGRESS.

Technos Spring Boot :
- spring-boot-starter-actuator

Tests :
- Tests d’intégration :
    - Appel à /actuator/health retourne UP.

--------------------------------------------------
9. Récapitulatif des Starters Spring Boot
--------------------------------------------------

Starters à utiliser dans le projet :

- spring-boot-starter-web
  Pour exposer l’API REST (controllers, JSON, etc.).

- spring-boot-starter-data-jpa
  Pour la persistence des entités : Ship, Passenger, Mission, Booking.

- spring-boot-starter-validation
  Pour la validation des DTO/entités (Bean Validation).

- spring-boot-starter-security
  Pour la sécurité, l’authentification, les rôles et la protection des endpoints.

- spring-boot-starter-test
  Pour les tests unitaires et d’intégration (JUnit, MockMvc, etc.).

- spring-boot-starter-actuator (optionnel)
  Pour le healthcheck et les métriques.

--------------------------------------------------
10. Pistes pédagogiques (pour structurer le TP)
--------------------------------------------------

Proposition d’itérations possibles :

Itération 1 :
- Mise en place du projet Spring Boot. OK
- Entités Ship et Passenger. OK
- CRUD basique avec JPA + validations. OK
- Tests unitaires et @DataJpaTest. Unit OK

Itération 2 :
- Entité Mission + règles de planification (chevauchement, vaisseau actif).
- Endpoints REST pour les missions.
- Tests métier (statuts, dates).

Itération 3 :
- Sécurité Spring Security + JWT.
- Mise en place des rôles et restrictions sur les endpoints.
- Tests d’intégration sur la sécurité.

Itération 4 :
- Réservations (Booking), gestion de la capacité et du poids.
- Endpoints “back-office” et “client”.
- Tests d’intégration complets (scénarios de bout en bout).

Itération 5 :
- Dashboard / reporting.
- Actuator, healthcheck.
- Nettoyage, documentation de l’API (optionnel : OpenAPI/Swagger).