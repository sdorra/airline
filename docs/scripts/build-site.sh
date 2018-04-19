#!/usr/bin/env bash

VERSION=$1
shift

SCRIPT_DIR=$(dirname "${BASH_SOURCE[0]}")
while [ -L "${SCRIPT_DIR}" ];
do
  SCRIPT_DIR=$(readlink "${SCRIPT_DIR}")
done
SCRIPT_DIR=$(cd ${SCRIPT_DIR} && pwd)

# Add the latest Javadoc
"${SCRIPT_DIR}/add-javadoc.sh" "${VERSION}"

# Regenerate the site
# Make sure we always have the latest dependencies
bundle install
bundle exec jekyll build -b "." -V
exit $?
