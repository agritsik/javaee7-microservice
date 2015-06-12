#!/usr/bin/env bash

mvn package -DskipTests -Pdocker-dev

cp target/blog-1.0-SNAPSHOT.war docker/blog-gf/blog.war