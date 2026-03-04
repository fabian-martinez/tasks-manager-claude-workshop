#!/usr/bin/env bash

# Reads the JSON input from stdin (for TaskCompleted hook)
INPUT=$(cat)
TASK_SUBJECT=$(echo "$INPUT" | jq -r '.task_subject')

echo "Running tests to verify task completion: $TASK_SUBJECT" >&2

# Run the test suite and capture output
if ! mvn test 2>&1; then
  echo "Tests not passing. Fix failing tests before completing: $TASK_SUBJECT" >&2
  exit 2
fi

echo "Tests passed successfully!" >&2
exit 0
