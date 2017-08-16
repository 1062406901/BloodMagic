package WayofTime.bloodmagic.client.hud;

import WayofTime.bloodmagic.util.handler.event.ClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public abstract class HUDElement {
    private final int xOffsetDefault;
    private final int yOffsetDefault;
    private final RenderGameOverlayEvent.ElementType elementType;
    private int xOffset;
    private int yOffset;

    public HUDElement(int xOffset, int yOffset, RenderGameOverlayEvent.ElementType elementType) {
        this.xOffset = xOffset;
        this.xOffsetDefault = xOffset;
        this.yOffset = yOffset;
        this.yOffsetDefault = yOffset;
        this.elementType = elementType;

        ClientHandler.hudElements.add(this);
    }

    public abstract void render(Minecraft minecraft, ScaledResolution resolution, float partialTicks);

    public abstract boolean shouldRender(Minecraft minecraft);

    public void onPositionChanged() {

    }

    public void resetToDefault() {
        this.xOffset = xOffsetDefault;
        this.yOffset = yOffsetDefault;
    }

    public void drawTexturedModalRect(double x, double y, double textureX, double textureY, double width, double height) {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(x + 0, y + height, 0).tex((double) ((float) (textureX + 0) * f), (double) ((float) (textureY + height) * f1)).endVertex();
        buffer.pos(x + width, y + height, 0).tex((double) ((float) (textureX + width) * f), (double) ((float) (textureY + height) * f1)).endVertex();
        buffer.pos(x + width, y + 0, 0).tex((double) ((float) (textureX + width) * f), (double) ((float) (textureY + 0) * f1)).endVertex();
        buffer.pos(x + 0, y + 0, 0).tex((double) ((float) (textureX + 0) * f), (double) ((float) (textureY + 0) * f1)).endVertex();
        tessellator.draw();
    }

    public int getXOffset() {
        return xOffset;
    }

    public int getYOffset() {
        return yOffset;
    }

    public int getXOffsetDefault() {
        return xOffsetDefault;
    }

    public int getYOffsetDefault() {
        return yOffsetDefault;
    }

    public RenderGameOverlayEvent.ElementType getElementType() {
        return elementType;
    }
}
