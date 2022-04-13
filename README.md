# AEM Tools - [![Build Status](https://travis-ci.org/aemtools/aemtools.svg?branch=master)](https://travis-ci.org/aemtools/aemtools) | [![Build status](https://ci.appveyor.com/api/projects/status/i0jamppjexluy5xk/branch/master?svg=true)](https://ci.appveyor.com/project/aemtools/aemtools/branch/master) | [![codebeat badge](https://codebeat.co/badges/0cc34216-7e9d-4154-8bfc-2561f77f1cbc)](https://codebeat.co/projects/github-com-dmytrotroynikov-aemtools-master) | [![codecov](https://codecov.io/gh/aemtools/aemtools/branch/master/graph/badge.svg)](https://codecov.io/gh/aemtools/aemtools) | [![downloads](https://img.shields.io/jetbrains/plugin/d/9397-aem-tools.svg)](https://plugins.jetbrains.com/plugin/9397-aem-tools)

<!-- Plugin description -->

Intellij IDEA plugin containing AEM (Adobe Experience Manager) related features. The goal of the plugin is to make the development of AEM projects faster and more convenient by leveraging features which IDE can provide.

## Installation 
The plugin is available in official JetBrains repository:

`Settings... -> Plugins -> Browse Repositories... -> type "AEM Tools" -> install`

## Features
1. HTL (Sightly) support
   * Syntax highlighting
   * Code Completion 
   * Code Navigation
   * Refactoring
   * Documentation
   * Java Use & Sling Models support

2. OSGi
   * Resolve OSGi (Felix) configurations
   * Resolve OSGi Properties

3. dialog.xml completion (Classic UI)
   * Provides the list of all available xtypes (taken from official documentation)
   * After the xtype is chosen proposes fields declared in specified xtype
   * "Quick doc" action triggered on specific field or xtype will show corresponding documentation

<!-- Plugin description end -->
