---
name: implement-feature
description: Use this skill to sequentially implement the approved plan for a feature or Jira story. Use when the user approves a brainstorming plan, asks for "implementación de funcionalidad", or wants to start coding.
---

When implementing a feature according to an approved plan:

1. **Implementación de funcionalidad**:
   - First, create or modify the Data Models and DTOs.
   - Next, implement the Repositories if database interaction is required.
   - Then, develop the Service layer containing the business logic. Ensure it is transactional if necessary.
   - Finally, create or update the Controllers to expose the new functionality via API endpoints.
2. **Implementación de pruebas unitarias y de integración**:
   - Write comprehensive unit tests for the Services you modified.
   - Write or update integration tests for the Controllers to verify API behavior.
3. **Review and Verify**:
   - Ensure the code compiles successfully (`mvn clean compile`).
   - Ensure the code aligns with the project's architecture and coding standards.
   - Inform the user once the implementation and tests are complete, so the QA/Hooks can run.

Use `$ARGUMENTS` to provide focus or specific instructions if applicable.