#!/usr/bin/env bash
# auto-commit.sh
# Accepts a commit message as the first parameter.
# It handles branching and committing code changes.

COMMIT_MESSAGE=${1:-"chore: automated commit by Claude Code workflow"}

# Check if there are changes in git
if [[ -z $(git status -s) ]]; then
  # No changes, exit gracefully
  echo "No changes to commit. Exiting gracefully."
  exit 0
fi

# Determine branch name
BRANCH_NAME="claude-update-$(date +%s)"
git checkout -b "$BRANCH_NAME" > /dev/null 2>&1

# Add all files and commit
git add .
git commit -m "$COMMIT_MESSAGE" > /dev/null 2>&1

echo "Changes were automatically committed to branch $BRANCH_NAME with message: $COMMIT_MESSAGE"
exit 0
