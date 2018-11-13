# NoirXP
A leveling and profession system for SpigotMC 1.13.1

## Default Commands:

- /noir level (check your current level)
- /noir level top (view the top 10 highest level players)
- /noir class <class> e.g. /noir class mining (check your xp/level in a specific class)
- /noir class <class> top e.g. /noir class mining top (view top 10 highest level players in that class)
- /noir show (shows xp gained messages in chat)
- /noir hide (disables xp gained messages in chat)
- /noir convert (adds tags to all items in inventory - used only if an item wasnt correctly tagged)

## Admin Commands:

- /noir disable (disables the leveling of the plugin for self)
- /noir reset (sets your level/xp in all classes to 0)
- /noir reset <player> (sets the level/xp of the player in all classes to 0)
- /noir reset <player> <class> (sets the level/xp in this class to 0)
- /noir set <player> <amount> (sets the overall xp of the player to the amount specified)
- /noir set <player> <amount> <class> (sets the xp of the given class to the amount specified)
  
## Permissions:
- NoirLeveling.default
- NoirLeveling.op

## Professions/classes
Each item in the game is either tagged as "General" or one of the following:
- Alchemy
- Building
- Cooking
- Farming
- Fishing
- Hunting
- Mining
- Smithing

## How to use:
All that needs to be done to allow this plugin to work is to modify the config.yml file in the plugins/NoirLeveling folder and change the credentials to a valid mysql server with create/read/write permissions. The plugin will take care of creating the database itself and all the tables.
