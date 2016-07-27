package WayofTime.bloodmagic.client.hud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import WayofTime.bloodmagic.proxy.ClientProxy;
import WayofTime.bloodmagic.util.Utils;

public class HUDElementDemonWillAura extends HUDElement
{
    protected Map<EnumDemonWillType, ResourceLocation> crystalTextures = new HashMap<EnumDemonWillType, ResourceLocation>();
    protected List<EnumDemonWillType> barOrder = new ArrayList<EnumDemonWillType>();

//    private double maxBarSize = 54;

    public HUDElementDemonWillAura()
    {
        super(5, 5, RenderGameOverlayEvent.ElementType.HOTBAR);
        crystalTextures.put(EnumDemonWillType.DEFAULT, new ResourceLocation(Constants.Mod.MODID, "textures/models/DefaultCrystal.png"));
        crystalTextures.put(EnumDemonWillType.CORROSIVE, new ResourceLocation(Constants.Mod.MODID, "textures/models/CorrosiveCrystal.png"));
        crystalTextures.put(EnumDemonWillType.DESTRUCTIVE, new ResourceLocation(Constants.Mod.MODID, "textures/models/DestructiveCrystal.png"));
        crystalTextures.put(EnumDemonWillType.VENGEFUL, new ResourceLocation(Constants.Mod.MODID, "textures/models/VengefulCrystal.png"));
        crystalTextures.put(EnumDemonWillType.STEADFAST, new ResourceLocation(Constants.Mod.MODID, "textures/models/SteadfastCrystal.png"));
        barOrder.add(EnumDemonWillType.DEFAULT);
        barOrder.add(EnumDemonWillType.CORROSIVE);
        barOrder.add(EnumDemonWillType.STEADFAST);
        barOrder.add(EnumDemonWillType.DESTRUCTIVE);
        barOrder.add(EnumDemonWillType.VENGEFUL);
    }

    @Override
    public void render(Minecraft minecraft, ScaledResolution resolution, float partialTicks)
    {
        EntityPlayer player = minecraft.thePlayer;

        if (!Utils.canPlayerSeeDemonWill(player))
        {
            return;
        }

        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexBuffer = tessellator.getBuffer();

        minecraft.getTextureManager().bindTexture(new ResourceLocation(Constants.Mod.MODID, "textures/hud/bars.png"));
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        this.drawTexturedModalRect(getXOffset(), getYOffset(), 0, 105 * 2, 80, 46);

        double maxAmount = Utils.getDemonWillResolution(player);

        for (EnumDemonWillType type : barOrder)
        {
            GlStateManager.color(1.0F, 1.0F, 1.0F);
//            minecraft.getTextureManager().bindTexture(crystalTextures.get(type));
            double maxBarSize = 26;

            double amount = ClientProxy.currentAura == null ? 0 : ClientProxy.currentAura.getWill(type);
            double ratio = Math.max(Math.min(amount / maxAmount, 1), 0);

            double x = getXOffset() + 8 + type.ordinal() * 6;
            double y = getYOffset() + 5 + (1 - ratio) * maxBarSize;
            double height = maxBarSize * ratio;
            double width = 5;

//            vertexBuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
//            vertexBuffer.pos((double) (x), (double) (y + height), 0).tex(0, 1).endVertex();
//            vertexBuffer.pos((double) (x + width), (double) (y + height), 0).tex(5d / 16d, 1).endVertex();
//            vertexBuffer.pos((double) (x + width), (double) (y), 0).tex(5d / 16d, 1 - ratio).endVertex();
//            vertexBuffer.pos((double) (x), (double) (y), 0).tex(0, 1 - ratio).endVertex();
//            tessellator.draw();

//            if (player.isSneaking())
//            {
//                GlStateManager.pushMatrix();
//                String value = "" + (int) amount;
//                GlStateManager.translate(x, (y + height + 4 + value.length() * 3), 0);
//                GlStateManager.scale(0.5, 0.5, 1);
//                GlStateManager.rotate(-90, 0, 0, 1);
//                minecraft.fontRendererObj.drawStringWithShadow("" + (int) amount, 0, 2, 0xffffff);
//                GlStateManager.popMatrix();
//            }
        }

//        minecraft.getTextureManager().bindTexture(new ResourceLocation(Constants.Mod.MODID, "textures/gui/demonWillBar.png"));
        minecraft.getTextureManager().bindTexture(new ResourceLocation(Constants.Mod.MODID, "textures/hud/bars.png"));
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1f);
        this.drawTexturedModalRect(getXOffset() + 10, getYOffset() + 14, 42 * 2, 112 * 2, 60, 20);
    }

    @Override
    public boolean shouldRender(Minecraft minecraft)
    {
        return true;
    }
}
