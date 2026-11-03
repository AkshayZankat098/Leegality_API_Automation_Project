#!/usr/bin/env sh

set -eu

APP_HOME=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
APP_NAME="Gradle"

if [ -z "${JAVA_HOME:-}" ]; then
  JAVA_CMD="java"
else
  JAVA_CMD="$JAVA_HOME/bin/java"
fi

CLASSPATH="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"

if [ ! -f "$CLASSPATH" ]; then
  echo "ERROR: Gradle wrapper jar not found at $CLASSPATH" >&2
  exit 1
fi

if [ -n "${JAVA_HOME:-}" ] && [ -x "$JAVA_HOME/bin/java" ]; then
  JAVA_CMD="$JAVA_HOME/bin/java"
fi

exec "$JAVA_CMD" \
  ${JAVA_OPTS:-} \
  -classpath "$CLASSPATH" \
  org.gradle.wrapper.GradleWrapperMain "$@"
