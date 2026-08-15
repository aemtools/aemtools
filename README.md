# AEM Tools - IntelliJ IDEA Plugin

[![Build](https://github.com/aemtools/aemtools/actions/workflows/build.yml/badge.svg)](https://github.com/aemtools/aemtools/actions/workflows/build.yml) | [![codecov](https://codecov.io/gh/aemtools/aemtools/branch/master/graph/badge.svg)](https://codecov.io/gh/aemtools/aemtools) | [![downloads](https://img.shields.io/jetbrains/plugin/d/9397-aem-tools.svg)](https://plugins.jetbrains.com/plugin/9397-aem-tools)

<!-- Plugin description -->

**Speed up Adobe Experience Manager (AEM) development with powerful IntelliJ IDEA integration.**

AEM Tools adds IDE support for the parts of AEM projects that are usually hard to work with as plain text: HTL, Expression Language, OSGi configurations, ClientLib declarations, and Jackrabbit FileVault XML. It brings completion, navigation, validation, quick documentation, and refactoring support closer to the files AEM developers edit every day.

## Key Features

### HTL and Expression Language

- Syntax highlighting, parsing, completion, and navigation for HTL and EL.
- Refactoring and quick documentation support for common HTL workflows.
- Sling Models and Java Use class resolution.
- HTL version awareness for HTL 1.3 and 1.4 features.
- Paired editing support inside EL expressions.

### OSGi Configurations

- Resolution between OSGi configuration files and Felix or OSGi R6-R7 components.
- Navigation from configuration properties to Java declarations.
- Metadata validation for supported OSGi component annotations.

### Jackrabbit FileVault XML

- Syntax highlighting and validation for AEM XML content files.
- Classic UI `dialog.xml` completion based on xtype documentation.
- Completion and highlighting for `cq:Component`, `cq:editConfig`, and related AEM content structures.

### Client Libraries

- Completion for ClientLib category names in HTL and ClientLib declarations.
- Completion and reference resolution for `js.txt` and `css.txt` entries.
- Inline documentation for categories and client library templates.

## Installation

AEM Tools is available from the [JetBrains Marketplace](https://plugins.jetbrains.com/plugin/9397-aem-tools).

1. Open **Settings | Plugins** in IntelliJ IDEA.
2. Select **Marketplace**.
3. Search for **AEM Tools**.
4. Install the plugin and restart the IDE when prompted.

## Compatibility

The current release supports IntelliJ IDEA **2024.3 through 2026.2** and matching IntelliJ Platform-based IDEs. See JetBrains' [build number ranges](https://plugins.jetbrains.com/docs/intellij/build-number-ranges.html#intellij-platform-based-products-of-recent-ide-versions) when matching an IDE version to a platform build.

| IDE Build Range | Recommended Version                                                    |
|-----------------|------------------------------------------------------------------------|
| 243-263         | [v1.1.0](https://github.com/aemtools/aemtools/releases/tag/v1.1.0)     |
| 243-261         | [v1.0.10](https://github.com/aemtools/aemtools/releases/tag/v1.0.10)   |
| 243-252         | [v1.0.7](https://github.com/aemtools/aemtools/releases/tag/v1.0.7)     |
| 223-243         | [v1.0.6](https://github.com/aemtools/aemtools/releases/tag/v1.0.6)     |
| 222-243         | [v1.0.5](https://github.com/aemtools/aemtools/releases/tag/v1.0.5)     |
| 203-222         | [v0.9.4.1](https://github.com/aemtools/aemtools/releases/tag/v0.9.4.1) |

## Why Use AEM Tools?

- Stay in the IDE while working with HTL, dialogs, ClientLibs, OSGi configs, and FileVault content.
- Catch common AEM markup and configuration mistakes earlier.
- Navigate between AEM declarations instead of searching manually.
- Use a free, open-source plugin built specifically for AEM projects.

## Contributing

Issues and pull requests are welcome. For larger changes, open an issue first so the implementation can be discussed before work begins.

<!-- Plugin description end -->
