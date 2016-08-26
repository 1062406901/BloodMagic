package WayofTime.bloodmagic.client;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.item.sigil.ItemSigilHolding;
import WayofTime.bloodmagic.network.BloodMagicPacketHandler;
import WayofTime.bloodmagic.network.KeyProcessor;
import WayofTime.bloodmagic.util.handler.event.ClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.Locale;

public class KeyBindingBloodMagic extends KeyBinding
{
    @SideOnly(Side.CLIENT)
    public KeyBindingBloodMagic(KeyBindings key)
    {
        super(key.getDescription(), key.getKeyConflictContext(), key.getKeyModifier(), key.getKeyCode(), Constants.Mod.NAME);

        ClientRegistry.registerKeyBinding(this);
    }

    // @formatter:off
    public enum KeyBindings
    {
        OPEN_HOLDING(KeyConflictContext.IN_GAME, KeyModifier.NONE, Keyboard.KEY_H)
        {
            @SideOnly(Side.CLIENT)
            @Override
            public void handleKeybind()
            {
                ItemStack itemStack = ClientHandler.minecraft.thePlayer.getHeldItemMainhand();
                if (itemStack != null && itemStack.getItem() instanceof IKeybindable)
                    BloodMagicPacketHandler.INSTANCE.sendToServer(new KeyProcessor(this, false));
            }
        },
        CYCLE_HOLDING_POS(KeyConflictContext.IN_GAME, KeyModifier.SHIFT, Keyboard.KEY_EQUALS)
        {
            @SideOnly(Side.CLIENT)
            @Override
            public void handleKeybind()
            {
                EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
                if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() instanceof ItemSigilHolding)
                    ClientHandler.cycleSigil(player.getHeldItemMainhand(), player, -1);
            }
        },
        CYCLE_HOLDING_NEG(KeyConflictContext.IN_GAME, KeyModifier.SHIFT, Keyboard.KEY_MINUS)
        {
            @SideOnly(Side.CLIENT)
            @Override
            public void handleKeybind()
            {
                EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
                if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() instanceof ItemSigilHolding)
                    ClientHandler.cycleSigil(player.getHeldItemMainhand(), player, 1);
            }
        },
        ;
        // @formatter:on

        private final IKeyConflictContext keyConflictContext;
        private final KeyModifier keyModifier;
        private final int keyCode;

        private KeyBinding key;

        KeyBindings(IKeyConflictContext keyConflictContext, KeyModifier keyModifier, int keyCode)
        {
            this.keyConflictContext = keyConflictContext;
            this.keyModifier = keyModifier;
            this.keyCode = keyCode;
        }

        @SideOnly(Side.CLIENT)
        public abstract void handleKeybind();

        public IKeyConflictContext getKeyConflictContext()
        {
            return keyConflictContext;
        }

        public KeyModifier getKeyModifier()
        {
            return keyModifier;
        }

        public int getKeyCode()
        {
            return keyCode;
        }

        public KeyBinding getKey()
        {
            if (key == null)
                key = new KeyBindingBloodMagic(this);

            return key;
        }

        public void setKey(KeyBinding key)
        {
            this.key = key;
        }

        public String getDescription()
        {
            return Constants.Mod.MODID + ".keybind." + name().toLowerCase(Locale.ENGLISH);
        }
    }
}
