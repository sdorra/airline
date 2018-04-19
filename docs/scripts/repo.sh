#!/usr/bin/env bash

SCRIPT_DIR=$(dirname "${BASH_SOURCE[0]}")
while [ -L "${SCRIPT_DIR}" ];
do
  SCRIPT_DIR=$(readlink "${SCRIPT_DIR}")
done
SCRIPT_DIR=$(cd ${SCRIPT_DIR} && pwd)

export RELEASE_REPO=https://repo1.maven.org/maven2
export DEV_REPO=https://oss.sonatype.org/content/repositories/snapshots/
export GROUP_ID="com/github/rvesse"

function getArtifact() {
  local ARTIFACT_ID=$1
  local VERSION=$2
  local CLASSIFIER=$3
  local EXT=$4

  local REPO=${RELEASE_REPO}
  local FILE_VERSION=${VERSION}
  if [[ "${VERSION}" == *"SNAPSHOT"* ]]; then
    # Find latest version
    FILE_VERSION=$(findLatestVersion "${ARTIFACT_ID}" "${VERSION}" "${CLASSIFIER}" "${EXT}")
    if [ -z "${VERSION}" ]; then
      echo "Failed to determine latest version"     1>&2
      exit 1
    fi
    REPO=${DEV_REPO}
  fi

  local URL="${REPO}/${GROUP_ID}/${ARTIFACT_ID}/${VERSION}/${ARTIFACT_ID}-${FILE_VERSION}"
  if [ -n "${CLASSIFIER}" ]; then
    URL="${URL}-${CLASSIFIER}"
  fi
  URL="${URL}${EXT}"
  local CHECKSUM_URL="${URL}.md5"

  local DOWNLOAD_FILE="${SCRIPT_DIR}/${ARTIFACT_ID}-${FILE_VERSION}-${CLASSIFIER}${EXT}"
  local CHECKSUM_FILE="${SCRIPT_DIR}/${DOWNLOAD_FILE}.md5"
  curl -f "${URL}" > "${DOWNLOAD_FILE}"
  if [ $? -ne 0 ]; then
    echo "CURL failed to download artifact from URL ${URL}"     1>&2
    exit 1
  fi
  curl -f "${CHECKSUM_URL}" > "${CHECKSUM_FILE}"
  if [ $? -ne 0 ]; then
    echo "CURL failed to download artifact checksum from URL ${CHECKSUM_URL}"     1>&2
    exit 1
  fi

  # Verify the checksum
  local VERIFIED=$(verifyChecksum "${DOWNLOAD_FILE}")
  if [ -z "${VERIFIED}" ]; then
    echo "Failed to verify checksum for file ${DOWNLOAD_FILE}"     1>&2
    exit 1
  fi

  echo "${DOWNLOAD_FILE}"
}

function findLatestVersion() {
  local ARTIFACT_ID=$1
  local VERSION=$2
  local CLASSIFER=$3
  local EXT=${4##\.}

  local URL="${DEV_REPO}/${GROUP_ID}/${ARTIFACT_ID}/${VERSION}/maven-metadata.xml"
  if [ -e "maven-metadata.xml" ]; then
    rm maven-metadata.xml
  fi
  curl "${URL}" > maven-metadata.xml
  if [ $? -ne 0 ]; then
    echo "CURL failed to download Maven metadata from URL ${URL}"     1>&2
    exit 1
  fi

  # Use xsltproc to extract out the version of the latest distribution
  if [ -e find.xslt ]; then
    rm find.xslt
  fi
  cat "${SCRIPT_DIR}/findArtifactVersion.xslt" | sed "s/CLASSIFIER/${CLASSIFIER}/" | sed "s/EXTENSION/${EXT}/" > find.xslt
  xsltproc find.xslt maven-metadata.xml > dist-version.txt
  if [ $? -ne 0 ]; then
    echo "xsltproc failed to extract front end distribtion version from maven metadata" 1>&2
    exit 1
  fi
  rm maven-metadata.xml
  rm find.xslt

  # Use some sed magic to strip out the superflous junk
  cat dist-version.txt | sed '/^$/d' > dist-version-noblanks.txt
  cat dist-version-noblanks.txt | sed 's/^[ \t]*//;s/[ \t]*$//' > dist-version-cleaned.txt
  VERSION=`tail -n 1 dist-version-cleaned.txt`
  rm dist-version*
  echo ${VERSION}
}

function verifyChecksum() {
  local FILE=$1

  if [ ! -e "${FILE}" ]; then
    echo "Given file ${FILE} does not exist"     1>&2
    exit 1
  elif [ ! -e "${FILE}.md5" ]; then
    echo "Given file ${FILE} has no .md5 checksum file"     1>&2
    exit 1
  else
    EXPECTED_CHECKSUM=$(cat "${FILE}.md5")
    ACTUAL_CHECKSUM=$(openssl md5 "${FILE}" | cut -d = -f 2 | cut -c 2-)
    if [ "${EXPECTED_CHECKSUM}" != "${ACTUAL_CHECKSUM}" ]; then
      echo "Checksum for ${FILE} is incorrect - Expected ${EXPECTED_CHECKSUM} but got ${ACTUAL_CHECKSUM}"     1>&2
      exit 1
    fi
    echo "Checksum for ${FILE} is valid"
  fi

  # Clean up checksum file as this is no longer needed
  rm "${FILE}.md5"
}
