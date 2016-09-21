#!/bin/bash

SCRIPT_DIR=$(dirname "${BASH_SOURCE[0]}")
while [ -L "${SCRIPT_DIR}" ];
do
  SCRIPT_DIR=$(readlink "${SCRIPT_DIR}")
done
SCRIPT_DIR=$(cd ${SCRIPT_DIR} && pwd)

source "${SCRIPT_DIR}/repo.sh"

VERSION=$1
if [ -z "${VERSION}" ]; then
  echo "Must specify a version of the Javadocs to add"
  exit 1
fi
ALT_REPO=$2
if [ -n "${ALT_REPO}" ]; then
  RELEASE_REPO=$2
  DEV_REPO=$2
fi

TARGET_DIR="${SCRIPT_DIR}/../javadoc/${VERSION}"
if [ ! -d "${TARGET_DIR}" ]; then
  mkdir -p "${TARGET_DIR}"
  if [ $? -ne 0 ]; then
    echo "Failed to create target directory ${TARGET_DIR}"
    exit 1
  fi
fi

ARTIFACTS=(airline airline-help-man airline-help-bash airline-help-html airline-help-markdown airline-io)
for ARTIFACT in "${ARTIFACTS[@]}"; do
  # Download
  ARTIFACT_FILE=$(getArtifact ${ARTIFACT} ${VERSION} "javadoc" ".jar")
  if [ -z "${ARTIFACT_FILE}" ]; then
    echo "Failed to download Javadoc for Artifact ${ARTIFACT} version ${VERSION}"
    exit 1
  fi
  echo "Got Javadoc for ${ARTIFACT} version ${VERSION} (${ARTIFACT_FILE})"
  echo

  # Unpack
  ARTIFACT_DIR="${TARGET_DIR}/${ARTIFACT}/"
  rm -Rf "${ARTIFACT_DIR}"
  mkdir -p "${ARTIFACT_DIR}"
  if [ $? -ne 0 ]; then
    echo "Failed to create directory ${ARTIFACT_DIR}"
    exit 1
  fi
  pushd "${ARTIFACT_DIR}"
  tar -xvf "${SCRIPT_DIR}/${ARTIFACT_FILE}" > extract.log 2>&1
  if [ $? -ne 0 ]; then
    cat extract.log
    echo "Failed to unpack Javadoc from file ${ARTIFACT_FILE}"
    exit 1
  fi
  rm extract.log
  popd
done
