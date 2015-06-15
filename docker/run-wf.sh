#!/usr/bin/env bash

docker build -t agritsik/blog-wf docker/blog-wf/

docker rm -f -v blog-wf
docker run -d -p 9990:9990 -p 8080:8080 --link=blog-db:blog-db --name blog-wf agritsik/blog-wf

docker logs -f blog-wf