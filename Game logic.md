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