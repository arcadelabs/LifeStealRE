Player joins server:
Sets HP attrib to value from config.

Player gets killed:
Transfers 2 hearts to killer.
If HP attrib is 0:
Player gets put in spectator mode.
Killer gains no hearts.

Player dies from something else:
Drops consumable heart item:
Heart item is immune to everything.

Player eliminated:
If enabled bans:
Player gets configured ban, tenp or perm
Else:
Player goes into permanent spectator mode
Sleeping(dead) player skinned npc spawns at the spot

Revive beacon:
Configurable 'comeback' hearts
When placed around a player's dead body, they gets revived
If there are multiple bodies while placing, beacon will choose the closest one

LifeState enums:
Living
Dead
Banned

Anti-exploits:
Revive cooldown
Revive x times limit
Anti spoof hearts

Considering these:
Teams
Curses
Blessings
Decaying hearts

Player dies
-> Random cursed heart drop
-> Random blessed heart drop
-> Fixed cursed heart drop
-> Fixed blessed heart drop
-> Random cursed/blessed/normal heart drop
-> Random cursed/blessed heart drop
-> Normal heart drop

Heart types:

    Cursed,
    Blessed,
    Normal

A customizable recipe, returns a placeholder item
On item craft event, we change the item to whatever configured.

# Profile Revamp: Implementation of ORMLite

ORMLite an very efficient, fast and light-weight Object Relational Mapper for mysql & sqlite;
This implementation has the following features:

## Features

> Easy methods for editing, updating, retrieve & deleting data.
This implementation makes easy to edit, update or retrieve & delete data without even using the weird sql query syntax!.

- Creating a Profile
```java
Profile profile = new Profile(uuid);
 LifeStealPlugin.getLifeSteal()
  .getDatabaseHandler()
  .getProfileDao()
  .create(profile);
```

- Updating & Refreshing Profile
```java
Profile profile = new Profile(uuid);

// Updating data after making changes.
 LifeStealPlugin.getLifeSteal()
  .getDatabaseHandler().getProfileDao()
  .update(profile);

// Refeshing data after updating.
LifeStealPlugin.getLifeSteal()
  .getDatabaseHandler().getProfileDao().refresh(profile);
```

### OR ELSE

```java
// This method will execute update() and refresh() method
Profile profile = new Profile(uuid);
LifeStealPlugin.getLifeSteal()
  .getProfileManager()
  .saveProfile(profile);
```

- Retrieving player profile

> Retrieving profile directly from database

This method of retrieving profile from database will make a new query to fetch the data;
It is very inconvenient cause it can cause lag between database and plugin if frequently used.
To retrieve data of an online player, use the second method..
```java
Profile profile = LifeStealPlugin
  .getLifeSteal()
  .getProfileManager()
  .getProfile(uuid);
``` 

> Retrieving profile from cache

Cache is a Map<UUID, Profile> in which the profiles are stored with the player uuid as key;
LifeSteal provides an runnable task which saves the cache profile to the actual database every
10 seconds which doesn't cause lag; to retrieve data from cache follow the code below.
```java
Profile profile = LifeStealPlugin
  .getLifeSteal()
  .getProfileManager()
  .getProfileCache()
  .get(uuid);
```

> Deleting Profile from database
```java
Profile profile = new Profile(uuid);
LifeStealPlugin.getLifeSteal()
  .getDatabaseHandler()
  .getProfileDao()
  .delete(profile);
```