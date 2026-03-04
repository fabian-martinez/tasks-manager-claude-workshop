---
name: brainstorming
description: Use this skill to analyze Jira stories, explore the codebase, identify architectural impact, and propose an implementation plan. Use when the user asks to start a task, "Inicia Un Brainstorming", or analyze a Jira story.
---

When initiating brainstorming for a Jira story:

1. **Obtener detalles de la Historia de Jira**: Read the provided Jira story details to fully understand the requirements.
2. **Analiza el Codigo (Impact Analysis)**:
   - Identify existing architectural patterns in the codebase.
   - Find all files that need to be created or modified (Model, DTO, Repository, Service, Controller, Tests).
   - Look for related existing implementations to ensure consistency.
3. **Inicia Un Brainstorming**:
   - Formulate a detailed step-by-step implementation plan.
   - List the specific files you intend to touch.
   - Identify any potential risks or dependencies.
4. **Propose the Plan**: Present the detailed architecture and implementation plan to the user for approval. Use `$ARGUMENTS` to provide context if applicable. Do not proceed to implementation until the user approves the plan.