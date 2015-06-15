#!/usr/bin/env bash

docker build -t agritsik/blog-gf docker/blog-gf/

docker rm -f -v blog-gf
docker run -d -p 4848:4848 -p 8080:8080 --link=blog-db:blog-db --name blog-gf agritsik/blog-gf

docker logs -f blog-gf