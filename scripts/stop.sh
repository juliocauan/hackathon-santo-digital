docker compose -f docker-compose.yaml down
docker volume rm --force AdventureWorks
docker image rm hackathon-santo-digital-server:latest
docker image prune --force
