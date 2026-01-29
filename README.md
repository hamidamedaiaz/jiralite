# Jiralite

Repository reorganized to separate backend and frontend.

Structure now:

- backend/ : Java Spring Boot application (copied from project root)
- frontend/: placeholder for frontend app (React/Vue/Angular)

Notes:
- This repo was a single Spring Boot project. To make a professional separation I copied the existing sources under `backend/` and left build wrappers and `pom.xml` in place at the root to allow `mvnw` to run as before.
- Next steps: convert the project to a multi-module Maven project or create an independent frontend repo and link via CI/CD.

