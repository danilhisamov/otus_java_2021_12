docker run --rm --name pg-docker \
-e POSTGRES_USER=usr \
-e POSTGRES_PASSWORD=pwd \
-e POSTGRES_DB=client_db \
-p 5432:5432 \
postgres:13