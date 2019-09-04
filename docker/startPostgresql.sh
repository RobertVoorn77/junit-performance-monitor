docker run --name postgres -v "$(PWD)/datadir:/var/lib/postgresql/data" -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -d postgres
