# Conventional Commits Specification

This project follows the [Conventional Commits](https://www.conventionalcommits.org/) specification for commit messages. This provides an easy set of rules for creating an explicit commit history, which makes it easier to write automated tools on top of.

## Commit Message Format

Each commit message consists of a **header**, an optional **body**, and an optional **footer**. The header has a special format that includes a **type**, an optional **scope**, and a **description**:

```
<type>[optional scope]: <description>

[optional body]

[optional footer(s)]
```

### 1. Type

Must be one of the following:

* **feat**: A new feature
* **fix**: A bug fix
* **docs**: Documentation only changes
* **style**: Changes that do not affect the meaning of the code (white-space, formatting, missing semi-colons, etc)
* **refactor**: A code change that neither fixes a bug nor adds a feature
* **perf**: A code change that improves performance
* **test**: Adding missing tests or correcting existing tests
* **build**: Changes that affect the build system or external dependencies (example scopes: maven, npm)
* **ci**: Changes to our CI configuration files and scripts (example scopes: Travis, Circle, BrowserStack, SauceLabs)
* **chore**: Other changes that don't modify `src` or test files
* **revert**: Reverts a previous commit

### 2. Scope (Optional)

A scope may be provided to a commit's type, to provide additional contextual information and is contained within parenthesis, e.g., `feat(parser): add ability to parse arrays`.

### 3. Description

The description contains a succinct description of the change:
* use the imperative, present tense: "change" not "changed" nor "changes"
* don't capitalize the first letter
* no dot (.) at the end

### 4. Body (Optional)

Just as in the description, use the imperative, present tense: "change" not "changed" nor "changes". The body should include the motivation for the change and contrast this with previous behavior.

### 5. Footer (Optional)

The footer should contain any information about Breaking Changes and is also the place to reference GitHub issues that this commit closes.

## Examples

**Feature with scope**
```
feat(lang): add Polish language
```

**Bug fix with issue reference**
```
fix: correct minor typos in code

see the issue for details

on typos fixed.

Reviewed-by: Z
Refs #133
```

**Breaking change**
```
feat: allow provided config object to extend other configs

BREAKING CHANGE: `extends` key in config file is now used for extending other config files
```