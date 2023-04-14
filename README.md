# AEM Tools - [![Build Status](https://travis-ci.org/aemtools/aemtools.svg?branch=master)](https://travis-ci.org/aemtools/aemtools) | [![Build status](https://ci.appveyor.com/api/projects/status/i0jamppjexluy5xk/branch/master?svg=true)](https://ci.appveyor.com/project/aemtools/aemtools/branch/master) | [![codebeat badge](https://codebeat.co/badges/0cc34216-7e9d-4154-8bfc-2561f77f1cbc)](https://codebeat.co/projects/github-com-dmytrotroynikov-aemtools-master) | [![codecov](https://codecov.io/gh/aemtools/aemtools/branch/master/graph/badge.svg)](https://codecov.io/gh/aemtools/aemtools) | [![downloads](https://img.shields.io/jetbrains/plugin/d/9397-aem-tools.svg)](https://plugins.jetbrains.com/plugin/9397-aem-tools)

<!-- Plugin description -->

Intellij IDEA plugin containing AEM (Adobe Experience Manager) related features. The goal of the plugin is to make the development of AEM projects faster and more convenient by leveraging features which IDE can provide.

## Compatibility

It is compatible with Intellij IDEA **2020.3+** versions. [IntelliJ Platform Based Products of Recent IDE Versions.](https://plugins.jetbrains.com/docs/intellij/build-number-ranges.html#intellij-platform-based-products-of-recent-ide-versions)

Latest versions:
* 203-222 branches - [0.9.4.1](https://github.com/aemtools/aemtools/releases/tag/v0.9.4.1)
* 222+ branches - [0.9.5](https://github.com/aemtools/aemtools/releases/tag/v0.9.5)

## Installation 
The plugin is available in official JetBrains repository:

`Settings... -> Plugins -> Browse Repositories... -> type "AEM Tools" -> install`

## Features
1. HTL (Sightly) support
   * Syntax highlighting
   * Versioning (supports v1.3-v1.4)
   * Code Completion 
   * Code Navigation
   * Refactoring
   * Documentation
   * Java Use & Sling Models support

2. OSGi
   * Resolve OSGi (Felix, OSGi R6-R7) configurations
   * Resolve OSGi Properties (Felix, OSGi R6-R7)
   * Metadata property validation 

3. Jackrabbit FileVault xml-content files
   1. Syntax highlighting
   2. dialog.xml completion (Classic UI)
      * Provides the list of all available xtypes (taken from official documentation)
      * After the xtype is chosen proposes fields declared in specified xtype
      * "Quick doc" action triggered on specific field or xtype will show corresponding documentation
   3. cq:Component, cq:editConfig completion and highlighting support

4. Clientlibs
   * Provides completion for project category name in the clientlibs declaration
   * Provides completion for project category name in the HTL
   * Provides completion/reference provider for clientlibs js.txt and css.txt files 
   * Provides short documentation about clientlibs category
   * Provides HTL template support
   

<!-- Plugin description end -->

## Sponsoring

[![Support this reposiroty with issuehunt](https://issuehunt.io/static/embed/issuehunt-button-v1.svg)](https://issuehunt.io/r/aemtools/aemtools)
