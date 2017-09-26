# AEM Tools [![Build Status](https://travis-ci.org/DmytroTroynikov/aemtools.svg?branch=master)](https://travis-ci.org/DmytroTroynikov/aemtools)[![Build status](https://ci.appveyor.com/api/projects/status/i0jamppjexluy5xk/branch/master?svg=true)](https://ci.appveyor.com/project/DmytroTroynikov/aemtools/branch/master)[![codebeat badge](https://codebeat.co/badges/0cc34216-7e9d-4154-8bfc-2561f77f1cbc)](https://codebeat.co/projects/github-com-dmytrotroynikov-aemtools-master)[![codecov](https://codecov.io/gh/DmytroTroynikov/aemtools/branch/master/graph/badge.svg)](https://codecov.io/gh/DmytroTroynikov/aemtools)
Intellij IDEA plugin containing AEM related features. The goal of the plugin is to make the development of AEM projects faster and more convenient by leveraging features which IDE can provide.
The project is in beta now, but it is already usable. 

## Installation 
The plugin is available in official JetBrains repository:

`Settings... -> Plugins -> Browse Repositories... -> type "AEM Tools" -> install`

## Features
1. HTL (Sightly) support
  * Syntax highlighting
  * Completion for standard context objects (e.g. `properties`)
  * Completion for HTL attributes (e.g. `data-sly-use`)
  * Java Use & Sling Models support
  * Code Navigation

2. dialog.xml completion (Classic UI)
  * Provides the list of all available xtypes (taken from official documentation)
  * After the xtype is chosen proposes fields declared in specified xtype
  * "Quick doc" action triggered on specific field or xtype will show corresponding documentation 

More details will be available soon at the project's [WIKI](https://github.com/DmytroTroynikov/aemtools/wiki)
