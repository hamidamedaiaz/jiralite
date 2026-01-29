# Backend (Spring Boot)

This folder contains the Java Spring Boot backend application.

How to build and run:

- From the repository root using the bundled wrapper script (Windows PowerShell):

  ./mvnw.cmd -f backend\pom.xml spring-boot:run

- Alternatively, use the helper PowerShell script:

  .\run-backend.ps1

- If you have Maven installed system-wide, you can also run from the project root:

  mvn -f backend\pom.xml spring-boot:run

Notes:
- This is a copy of the original project files placed under `backend/` for clearer separation between frontend and backend.
- If you prefer a single-module layout at the root, or a multi-module Maven setup, tell me and I can convert it.
