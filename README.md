# AEM Tools – IntelliJ IDEA Plugin

[![Build status](https://ci.appveyor.com/api/projects/status/i0jamppjexluy5xk/branch/master?svg=true)](https://ci.appveyor.com/project/aemtools/aemtools/branch/master) | [![codebeat badge](https://codebeat.co/badges/0cc34216-7e9d-4154-8bfc-2561f77f1cbc)](https://codebeat.co/projects/github-com-dmytrotroynikov-aemtools-master) | [![codecov](https://codecov.io/gh/aemtools/aemtools/branch/master/graph/badge.svg)](https://codecov.io/gh/aemtools/aemtools) | [![downloads](https://img.shields.io/jetbrains/plugin/d/9397-aem-tools.svg)](https://plugins.jetbrains.com/plugin/9397-aem-tools)

<!-- Plugin description -->

**Speed up Adobe Experience Manager (AEM) development with powerful IntelliJ IDEA integration.**

AEM Tools is a feature-rich IntelliJ IDEA plugin designed to make AEM project development faster, smarter, and more convenient by harnessing the full power of the IDE. It provides native support for HTL, OSGi configurations, ClientLibs, and Jackrabbit FileVault XML, giving you code intelligence where it matters most.

## 🔗 Key Features
1. 📝 HTL (Sightly) support
   * Syntax highlighting
   * Code completion and navigation
   * Refactoring and documentation support
   * Sling Models & `Java Use` class support
   * Supports HTL versions **1.3 - 1.4**

2. ⚙️ OSGi Configuration Support
   * Resolves Felix and OSGi R6-R7 configurations and properties
   * Metadata property validation 

3. 📦 Jackrabbit FileVault (XML content) Support
   1. Syntax highlighting for XML content files
   2. `dialog.xml` auto-completion (Classic UI):
      * Full xtype list from official documentation
      * Suggests fields based on selected xtype
      * Quick documentation for fields and xtypes
   3. `cq:Component`, `cq:editConfig` completion and highlighting support

4. 📁 ClientLibs (Client Libraries) Support
   * Autocompletion for:
     * Category names in HTL and clientlibs declarations 
     * js.txt and css.txt file entries 
   * Reference resolution for clientlib assets 
   * Inline documentation for categories and templates

## 🧩 Installation
Available directly in the **JetBrains Plugin Repository**:
1. Go to Settings → Plugins
2. Click Marketplace
3. Search for **AEM Tools**
4. Click **Install** and restart the IDE

Or install manually from the [JetBrains plugin page](https://plugins.jetbrains.com/plugin/9397-aem-tools).

## ⚙️ Compatibility

Supports IntelliJ IDEA **2020.3+**.
Refer to [JetBrains build version mapping](https://plugins.jetbrains.com/docs/intellij/build-number-ranges.html#intellij-platform-based-products-of-recent-ide-versions) for version compatibility.

| Build Range | Recommended Version                                                    |
| ----------- | ---------------------------------------------------------------------- |
| 243+        | [v1.0.7](https://github.com/aemtools/aemtools/releases/tag/v1.0.7)     |
| 223 – 243   | [v1.0.6](https://github.com/aemtools/aemtools/releases/tag/v1.0.6)     |
| 222 – 243   | [v1.0.5](https://github.com/aemtools/aemtools/releases/tag/v1.0.5)     |
| 203 – 222   | [v0.9.4.1](https://github.com/aemtools/aemtools/releases/tag/v0.9.4.1) |

## 💡 Why Use AEM Tools?
* Free: it's an open-source, free feature-rich IntelliJ IDEA plugin available for everyone.
* Boost Productivity: Skip repetitive tasks with smart code insight.
* Reduce Errors: Get real-time validation and suggestions.
* Stay in Flow: Develop AEM components without switching between docs and IDE.
* Support Modern AEM: Covers essential features for both Classic UI and HTL/Sling Models.

## 🧑‍💻 Contribute
We welcome contributions and feedback!
Feel free to open issues or pull requests on GitHub.

<!-- Plugin description end -->
