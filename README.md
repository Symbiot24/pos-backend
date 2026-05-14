# POS Backend

Spring Boot backend for the POS application.

## Environment

Create a local `.env` from `.env.example` and fill in real values. The real `.env` file is ignored by Git.

## Run With Docker Compose

Use Compose for local development because it starts both MySQL and the backend on the same Docker network:

```powershell
docker compose up --build
```

Backend: `http://localhost:8080`

MySQL is exposed on host port `3307`, while containers use `mysql:3306`.

## Run The Backend Image Manually

Plain `docker run -p 8080:8080 pos-backend` will not work by itself because there is no MySQL server inside the backend image and no `.env` file is loaded.

If the Compose MySQL container is already running, run the backend image like this:

```powershell
docker run --rm --name pos-backend-manual --network pos-backend_default --env-file .env -p 8080:8080 pos-backend:latest
```

If port `8080` is already used, map a different host port:

```powershell
docker run --rm --name pos-backend-manual --network pos-backend_default --env-file .env -p 8081:8080 pos-backend:latest
```

For deployment, set these environment variables on the hosting platform:

```text
PORT
DB_URL
DB_USERNAME
DB_PASSWORD
JWT_SECRET
CLOUDINARY_CLOUD_NAME
CLOUDINARY_API_KEY
CLOUDINARY_API_SECRET
CORS_ALLOWED_ORIGINS
```
