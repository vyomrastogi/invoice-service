# Invoice Service - Project


![Build and Deploy status](https://github.com/vyomrastogi/invoice-service/actions/workflows/heroku.yml/badge.svg)
![Test status](https://github.com/vyomrastogi/invoice-service/actions/workflows/gradle.yml/badge.svg)
![Code Coverage](https://github.com/vyomrastogi/invoice-service/blob/main/.github/badges/jacoco.svg)
![Branch Coverage](https://github.com/vyomrastogi/invoice-service/blob/main/.github/badges/branches.svg)


A RESTful service built with `Spring Boot & ecosystem`. It uses an in-memory H2 Database for local setup, whereas a Postgres instance on [Heroku Cloud](https://heroku.com). The initial app installation and Postgres add-on setup is managed by [Terraform Configurations](https://terraform.io). Build and Deployment to Heroku is managed by [Github Actions](https://docs.github.com/en/actions).

---

## About 
This started as part of a capstone project in one of the trainings. Now I am trying to expand this as a learning project daily changing landscape of technologies. Few ideas I want to explore are 
- How to use terraform to maintain resources in a cloud service 
- How to implement CI/CD with Github Actions
- How to implement Functional Test cases as part of CI/CD
- and you can help more ideas we can implement by raising a new idea issue (_at some time in future hopefully I will have a template inserted here_)

Features are nice and cool, but one of the main objectives/vision for this repository is to provide a uniform experience to all developers. Each developer should be able to setup a similar service in with certain steps. 

_Let's learn together :smiley:_

---

## Getting Started 
_work in progrss_

### Prerequisite 
- A `Java 11` runtime for running application locally without docker. 
- Docker Desktop - Docker download link : https://docs.docker.com/get-docker/ 
- Terraform CLI - https://learn.hashicorp.com/tutorials/terraform/install-cli 
- Heroku CLI - https://devcenter.heroku.com/articles/heroku-cli 
- Heroku Account - https://www.heroku.com/free (_Only if you are interested on deploying there_)
- Any IDE of your choice (_I am starting to like (vscode)[https://code.visualstudio.com/docs/setup/setup-overview]_)
- and Ofcourse Github account



---
## Design 

### Class Relationship

![Class Relationship](https://github.com/vyomrastogi/invoice-service/blob/main/static/uml-diagram.png)

### Entity Relationship

![Entity Relationship](https://github.com/vyomrastogi/invoice-service/blob/main/static/er-diagram.png)


_Some highlights of project_

### Test Coverage Check

![Coverage Report on PR](https://github.com/vyomrastogi/invoice-service/blob/main/static/github-testrun-summary.PNG)


### Logging 

![Logging](https://github.com/vyomrastogi/invoice-service/blob/main/static/logging-dashboard.PNG)

---

## Useful Links 

- [heroku terraform provider](https://registry.terraform.io/providers/heroku/heroku/latest/docs)
