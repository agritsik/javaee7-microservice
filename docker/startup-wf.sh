#!/usr/bin/env bash

sh docker/run-package.sh
sh docker/run-db.sh
sh docker/run-wf.sh

