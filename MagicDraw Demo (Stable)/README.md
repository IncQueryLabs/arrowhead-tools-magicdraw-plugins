# Arrowhead SysML export-import demo

*was shown at the Porto workshop on 2019-11-13*

## Installation of the plugin

1. Have MagicDraw 19.0 and the MagicDraw SysML plugin (or, equivalently, Cameo Systems Modeler 19.0) 
2. If you want to see the validation feature, get IncQuery Desktop too (http://static.incquerylabs.com/projects/incquery-desktop/latest/)
3. Open Resource/Plugin Manager (Help → Resource/Plugin Manager)
4. Select 'Import' and locate the V4MD (com.incquerylabs.v4md.zip) and the IncQuery Desktop (com.incquerylabs.arrowhead.magicdraw.zip) archives.
5. Restart MagicDraw

## Use

### Creating a new project using the Arrowhead profile

1. Create a normal SysML project (File -> New Project -> Systems Engineering -> SysML Project)
2. Import the profile (File -> Use Project -> Use Local Project). The profile file is Arrowhead Profile.mdzip (as in this folder) by default. There are two import methods:
    * Use the *From file system* option
      * In this case, MagicDraw stores an absolute path to the file containing the profile (Arrowhead Profile.mdzip in this folder), thus, moving it would result in a missing profile error
    * Copy the file next to the new project (i.e., same folder) and use the *From predefined location* -> *<project.dir>* option
      * Here, a relative path to the profile file is stored, thus, the project remains intact as long as the project and the profile are stored in the same folder (the .bak created is just a backup file)
3. Press finish
