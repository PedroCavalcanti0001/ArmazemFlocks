package inventory.opener;

import com.google.common.base.Preconditions;
import inventory.InventoryManager;
import inventory.SmartInventory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class ChestInventoryOpener implements InventoryOpener {

    @Override
    public Inventory open(SmartInventory inv, Player player) {
        Preconditions.checkArgument(inv.getColumns() == 9, "Erro nas colunas do inventario.");
        Preconditions.checkArgument(inv.getRows() >= 1 && inv.getRows() <= 6, "As linhas do inventario tem que ser entre 1 e 6.");
        InventoryManager manager = inv.getManager();
        Inventory handle = Bukkit.createInventory(player, inv.getRows() * inv.getColumns(), inv.getTitle());
        fill(handle, manager.getContents(player).get());
        player.openInventory(handle);
        return handle;
    }

    @Override
    public boolean supports(InventoryType type) {
        return type == InventoryType.CHEST || type == InventoryType.ENDER_CHEST;
    }


}