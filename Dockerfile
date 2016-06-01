FROM ruby:2.3.1

RUN apt-get --yes update \
    && apt-get --yes upgrade \
    && apt-get install --yes nodejs \
    && apt-get install --yes build-essential \
    && apt-get install --yes npm \
    && npm install -g bower \
    && apt-get install --yes qt5-default libqt5webkit5-dev

RUN gem install bundler

COPY Gemfile* /tmp/
WORKDIR /tmp

RUN bundle install
