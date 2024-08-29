bash ./scripts/stop.sh
mvn clean package -DskipTests
docker compose -f docker-compose.yaml up -d --build