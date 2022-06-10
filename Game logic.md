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


Lore: ["TT", "This consumable heart will give you %s% heart points." , "PP"]

Lore -> Component
Component -> Minimessage and TagResolver -> Component
Component -> Lore(List<String>)