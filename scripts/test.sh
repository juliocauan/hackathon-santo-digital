docker compose -f "docker-compose-test.yml" down
docker volume rm --force VOLUME AdventureWorks-test

docker compose -f "docker-compose-test.yml" up -d --build
sleep 10
mvn jacoco:prepare-agent clean package jacoco:report

docker compose -f "docker-compose-test.yml" down
docker volume rm --force VOLUME AdventureWorks-test
