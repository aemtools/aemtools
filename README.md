# AEM Tools

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/3f9068782fb9457b9c4c7e535710b15e)](https://www.codacy.com/app/DmytroTroynikov/aemtools?utm_source=github.com&utm_medium=referral&utm_content=DmytroTroynikov/aemtools&utm_campaign=badger)

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

2. dialog.xml completion (Classic UI)
  * Provides the list of all available xtypes (taken from official documentation)
  * After the xtype is chosen proposes fields declared in specified xtype
  * "Quick doc" action triggered on specific field or xtype will show corresponding documentation 

More details will be available soon at the project's [WIKI](https://github.com/DmytroTroynikov/aemtools/wiki)
