# Arrowhead SysML export-import demo

*was shown at the Porto workshop on 2019-11-13*

## Installation of the plugin

1. Have MagicDraw 19.0 and the MagicDraw SysML plugin (or Cameo Systems Modeler)
2. If you want to see the validation feature, get IncQuery Desktop too
3. Open Resource/Plugin Manager (Help → Resource/Plugin Manager)
4. Select 'Import' and locate the V4MD (com.incquerylabs.v4md.zip) and the IncQuery Desktop (com.incquerylabs.arrowhead.magicdraw.zip) archives.
5. Restart MagicDraw

## Use

### Creating a new project

1. Create a normal SysML project (File -> New Project -> Systems Engineering -> SysML Project)
2. Import the profile (File -> Use Project -> Use Local Project). Either:
    * Use the *From file system* option
      * The connnection may break if the file containing the profile is moved
    * Put said file next to the new project and use the *From predefined location* -> *<project.dir>* option
      * And move the two of the togeter (the .bak created is just a backup file)
3. Press finish
