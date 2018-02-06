package WayofTime.bloodmagic.apibutnotreally.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class ItemBindEvent extends Event {
    public final EntityPlayer player;
    public String key;
    public ItemStack itemStack;

    /**
     * This event is called whenever a player attempts to bind a
     * {@link WayofTime.bloodmagic.apibutnotreally.iface.IBindable} item.
     *
     * @param player    The player doing the binding
     * @param key       The UUID of the player doing the binding
     * @param itemStack The {@link ItemStack} that the player is binding
     *                  <p>
     *                  This event is {@link Cancelable}.<br>
     */
    public ItemBindEvent(EntityPlayer player, String key, ItemStack itemStack) {
        super();
        this.player = player;
        this.key = key;
        this.itemStack = itemStack;
    }
}
