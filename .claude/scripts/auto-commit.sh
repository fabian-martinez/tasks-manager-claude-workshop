#!/usr/bin/env bash
# auto-commit.sh
# Intended to be run as a Stop hook to automate branching and committing at the end of a session if there are changes.
# It reads JSON input from stdin (for the Stop hook context)

# Check if there are changes in git
if [[ -z $(git status -s) ]]; then
  # No changes, exit gracefully
  echo "{\"hookSpecificOutput\": {\"hookEventName\": \"Stop\", \"additionalContext\": \"No changes to commit. Exiting session cleanly.\"}}"
  exit 0
fi

# Determine branch name
BRANCH_NAME="claude-update-$(date +%s)"
git checkout -b "$BRANCH_NAME" > /dev/null 2>&1

# Add all files and commit
git add .
git commit -m "Automated commit by Claude Code workflow: implemented task features" > /dev/null 2>&1

echo "{\"hookSpecificOutput\": {\"hookEventName\": \"Stop\", \"additionalContext\": \"Changes were automatically committed to branch $BRANCH_NAME.\"}}"
exit 0
