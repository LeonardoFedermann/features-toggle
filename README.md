# users-and-features-toggle
Java and Spring Boot application with a simple registration of users and features, along with a toggle functionality for them to be active or inactive.

## Tasks
Except for the first one, all tasks should have unit tests. The tasks are:

- [x] **Create project:** create a spring project with JPA and hibernate and stuff configured, a Postgres database, docker file, docker compose to make it easier to test.
- [ ] **Create User CRUD:** email, name, password, isActive, createdBy and updatedBy and when.
- [ ] **Create Login and authentication on endpoints:** one admin user should be added on boot; only a logged user can add other users and use all endpoints.
- [ ] **Create feature CRUD:** name, isActive, createdBy and updatedBy and when.
- [ ] **Create feature toggle open endpoints:** check if feature is on or off, turn a feature on and off easily.
- [ ] **Create job to disable and enable all features on weekends:** find a way to turn all the features off on weekends and turn on again all the necessary features on when the week starts.
