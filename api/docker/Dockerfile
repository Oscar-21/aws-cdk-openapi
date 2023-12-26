FROM ruby:3.3.0-rc1-alpine3.19

RUN apk update \
    && apk upgrade --no-cache \
    && apk add git \
    && apk add --update nodejs npm \
    && apk add --no-cache build-base

# Install node packages and gems
RUN npm install -g widdershins \
    && gem install bundler 

# working directory
WORKDIR /openapi

RUN git clone https://github.com/slatedocs/slate
RUN cd slate \
    && bundle install
