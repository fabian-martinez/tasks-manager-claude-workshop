# Claude Code Workflow - Task Manager API

This document details the Claude Code integration to automate the lifecycle described in the `workflow_claude_example.drawio` diagram. By leveraging **Hooks** and **Skills**, we guide Claude through a fully automated development workflow involving context initialization, task brainstorming, code implementation, automated testing, and automatic Git commits.

## Workflow Lanes Implemented

### 1. Orchestrator (UserPromptSubmit Hook)
- **`read-jira-stories.py`**: A python script configured as a `UserPromptSubmit` hook. When you enter a prompt like `Implement TASK-001`, this script reads `src/main/resources/mock-jira-stories` and appends the relevant Jira story details directly into Claude's context automatically.

### 2. Sub Agents Pool & Skills Engine (Skills)
- **`/brainstorming` Skill**: Guides Claude to analyze Jira stories, perform impact analysis, search the codebase, propose an architecture, and present an implementation plan to the user.
- **`/implement-feature` Skill**: Invoked to systematically execute an approved plan. It directs Claude to sequentially create/update Models, DTOs, Repositories, Services, Controllers, and Tests.

### 3. Hooks System (TaskCompleted & Stop Hooks)
- **`verify-tests.sh`**: Configured as a `TaskCompleted` hook. Before Claude considers a task finished, this hook runs `mvn test`. If tests fail, it rejects the completion and sends the error log to Claude to fix it.
- **`auto-commit.sh`**: Configured as a `Stop` hook. When Claude finishes responding and the session stops, this hook automatically checks if there are git modifications. If so, it creates a new branch and commits the changes cleanly.

## How to use

1. Start Claude Code in this repository: `claude`
2. Let the prompt submit hook auto-fetch context. Type a request like:
   `Analyze TASK-001 and /brainstorming`
3. Claude will perform impact analysis and propose a step-by-step plan.
4. Once you approve the plan, tell Claude:
   `Please execute the plan using /implement-feature`
5. Claude will write the implementation and tests.
6. Once Claude finishes, the `TaskCompleted` hook will block completion until `mvn test` passes.
7. Finally, when Claude stops, the `Stop` hook will branch and commit your code!
