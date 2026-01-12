# Contributing to Solrion

Thanks for your interest in contributing to Solrion.
This document describes how to work on the project, its structure,
design principles, and contribution rules.

--------------------------------------------------------------------

Building the project

To build the entire project, run:

    mvn clean install

This builds all modules:

- solrion-core
- solrion-logging-slf4j
- solrion-vertx-mutiny

--------------------------------------------------------------------

Project structure

Module                     Description
-------------------------  -----------------------------------------------
solrion-core               Public API, DSL, protocol mapping, codecs
solrion-logging-slf4j      Logging integration via SLF4J
solrion-vertx-mutiny       Reactive Vert.x / Mutiny transport

IMPORTANT:
The solrion-core module must remain transport- and framework-agnostic.

--------------------------------------------------------------------

Design principles

When contributing, please follow these principles:

- Prefer immutability
- Validate inputs eagerly and explicitly
- Avoid reflection-heavy or implicit "magic"
- Keep Solr protocol mapping explicit and correct
- Favor composition and visitor patterns over inheritance
- Avoid leaking transport, framework, or runtime concerns into solrion-core
- Public APIs should be predictable, stable, and explicit
- Escape hatches (rawOptions, rawParams) are allowed, but should not be the default

--------------------------------------------------------------------

Commit messages (IMPORTANT)

This project strictly follows the Conventional Commits specification:
https://www.conventionalcommits.org/

Required format:

    <type>(<optional scope>): <short description>

Common commit types:

Type        Description
----------  ----------------------------------------------
feat        New feature
fix         Bug fix
refactor    Code change that neither fixes a bug nor adds a feature
docs        Documentation only
test        Tests only
chore       Maintenance, tooling, or cleanup
build       Build system or dependency changes
ci          CI configuration

Examples:

    feat(core): add json stat facet factories
    fix(codec): support mixed bucket formats
    refactor(protocol): introduce visitor-based emitters
    docs(readme): add usage example

Not allowed:

- Mixing multiple types in the commit header (e.g. feat+fix, feat,fix)
- Vague messages (e.g. update stuff, fix bug)
- Missing commit type prefix

--------------------------------------------------------------------

Breaking changes

Breaking changes must be explicitly declared.

Using !:

    feat(api)!: change facet response model

Using a footer:

    feat(api): change facet response model

    BREAKING CHANGE: JsonFacetResult no longer exposes raw map

--------------------------------------------------------------------

Pull requests

- Keep pull requests focused and minimal
- Prefer multiple small commits over a single large commit
- Ensure the project builds successfully before opening a PR
- Update documentation when APIs or behavior change

--------------------------------------------------------------------

Issues

When reporting bugs or requesting features, please include:

- Solr version
- Minimal request example (DSL or raw params)
- Expected behavior
- Actual behavior

--------------------------------------------------------------------

Code style

- Follow existing formatting and naming conventions
- Use meaningful names for public APIs
- Avoid introducing breaking changes without discussion

--------------------------------------------------------------------

Thank you for contributing to Solrion.
