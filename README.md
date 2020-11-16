# DurabilityAlert
Alerts the user of low durability items. Percent and toggle defined by user.

DurabilityAlert is a plugin that notifies the player when their items are low on durability.
Works for: All armour, Pickaxes, Axes, Shovels, Swords, Hoes, Fishing Rod, Shears.

The warning can be toggled (with the command /durabilityalert toggle) by each player so that it's not forced on everybody.

/durabilityalert (/da) - the base command
/durabilityalert toggle - toggles the alert per player (set by user)
/durabilityalert armour <number> - sets the percent at which the armour durability warning will start to show (set by user)
/durabilityalert tools <number> - sets the percent at which the tools durability warning will show (set by user)
/durabilityalert type [percent/durability] - sets the warning values to depend on either percent left, or durability left (set by user)
/durability enchant - Toggle whether or not alerts hould only be shown for enchanted items

durabilityalert.command:
description: default durability alert command permission
default: op
durabilityalert.alert:
description: allows the user to get durability alerts
default: op

Spoiler: config.yml
Join my discord server for help/suggestions. I'm active there every day.
https://discord.gg/AET9mWj
