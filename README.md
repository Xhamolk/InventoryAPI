InventoryAPI
============

This framework is designed to change the current inventory-manipulation model, and the paradigm that all IInventory instances are static (like chests). 



* An IInventory implementation might want to decide which items are allowed and which are not: IDynamicInventory. 
* An IInventory implementation might want to handle the manipulation of it's contents on a different way: ICustomInventory and IInventoryHandler. 

* An inventory-manipulator no longer need to do the dirty work: InventoryHelper, or InventoryHandler (for more in-depth manipulations).
