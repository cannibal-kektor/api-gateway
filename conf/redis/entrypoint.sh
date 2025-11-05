#!/bin/sh
set -e

sed -e "s/REDIS_USER/$REDIS_USER/g" \
    -e "s/REDIS_PASSWORD/$REDIS_PASSWORD/g" \
    /usr/local/etc/redis/users.template.acl > /usr/local/etc/redis/users.acl

sed -e "s/REDIS_DEFAULT_PASSWORD/$REDIS_REDIS_DEFAULT_PASSWORD/g" \
    /usr/local/etc/redis/redis.template.conf > /usr/local/etc/redis/redis.conf

exec docker-entrypoint.sh redis-server /usr/local/etc/redis/redis.conf --aclfile /usr/local/etc/redis/users.acl