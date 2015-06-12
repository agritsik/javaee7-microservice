#!/usr/bin/env bash

docker rm -f -v blog-db

docker run -d -p 3306:3306 \
        -e MYSQL_ROOT_PASSWORD=root1 \
        -e MYSQL_USER=blogger \
        -e MYSQL_PASSWORD=blogger1 \
        -e MYSQL_DATABASE=blog \
        --name blog-db \
        mysql