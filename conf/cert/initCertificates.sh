#!/bin/sh
set -e

if [ -e auth_certs/auth-server.pem ]; then
  echo "Certificates already exists."
  exit 0;
fi

echo "Generating api gateway server certificate"
openssl genrsa -out gateway_certs/gateway-server.key 4096
openssl req -x509 -new -key gateway_certs/gateway-server.key --days 1024 -out gateway_certs/gateway-server.pem -section gateway -config conf/openssl.conf
echo "Certificate for Gateway api server generated successfully at ${PWD}/gateway_certs/"

echo "Generating authentication server public/private keys"
openssl genrsa -out auth_certs/auth-server.key 4096
openssl rsa -in auth_certs/auth-server.key -pubout -out auth_certs/auth-pubkey.pem
echo "Signing Keys for authentication server generated successfully at ${PWD}/auth_certs/"

find gateway_certs -type f \( -name "*.pem" -o -name "*.key" \) -exec chmod 644 {} \;
find auth_certs -type f \( -name "*.pem" -o -name "*.key" \) -exec chmod 644 {} \;