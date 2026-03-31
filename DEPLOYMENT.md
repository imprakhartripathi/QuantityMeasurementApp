# QMA Docker Deployment

## 1) Prepare env
```bash
cp .env.example .env
```

For EC2 direct-host deployment, you can start from:
```bash
cp .env.ec2.example .env
```

Set at least:
- `QMA_JWT_SECRET`
- `GOOGLE_CLIENT_ID`
- `GOOGLE_CLIENT_SECRET`
- `QMA_FRONTEND_ORIGIN`
- `QMA_OAUTH2_REDIRECT_URI`
- `QMA_GOOGLE_AUTH_REDIRECT_URI`
- `QMA_CORS_ALLOWED_ORIGINS`
- `QMA_COOKIE_SECURE=true` (for HTTPS)

Important: Google OAuth Console must include the same redirect URL as `QMA_GOOGLE_AUTH_REDIRECT_URI`.

URL mode notes:
- Local JVM mode uses `QMA_AUTH_BASE_URL_LOCAL` and `QMA_SERVICE_BASE_URL_LOCAL`
- Docker mode uses `QMA_DOCKER_AUTH_BASE_URL` and `QMA_DOCKER_SERVICE_BASE_URL`

## 2) Local Docker run (build from source)
```bash
./start-microservices.sh docker
```

## 3) Deploy mode (pull prebuilt images)
Set image refs in `.env`:
- `QMA_API_IMAGE`
- `QMA_AUTH_IMAGE`
- `QMA_SERVICE_IMAGE`
- `QMA_CLIENT_IMAGE`

Then:
```bash
./start-microservices.sh docker-deploy
```

## 4) Stop
```bash
./start-microservices.sh stop
```

## DNS/Subdomain mapping
- `app.your-domain.com` -> frontend (port 8000 or reverse proxy target)
- `api.your-domain.com` -> gateway (port 4000)

Use HTTPS in front (ALB/CloudFront/Nginx + ACM cert).  
Google OAuth redirect URI must exactly match your deployed callback URL.
