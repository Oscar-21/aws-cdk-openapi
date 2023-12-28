FROM ruby:3.3.0-rc1-alpine3.19

RUN apk update \
    && apk upgrade --no-cache \
    && apk add git \
    && apk add --update nodejs npm \
    && apk add --no-cache build-base

# Install node packages
RUN npm i -g @redocly/cli@latest

# working directory
WORKDIR /openapi

