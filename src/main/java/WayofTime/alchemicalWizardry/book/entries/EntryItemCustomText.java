package WayofTime.alchemicalWizardry.book.entries;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import WayofTime.alchemicalWizardry.book.classes.guide.GuiEntry;

public class EntryItemCustomText extends EntryItemText implements IEntryCustomText
{

	public EntryItemCustomText(ItemStack stack)
	{
		super(stack);
	}
	
	public EntryItemCustomText(ItemStack stack, String title)
	{
		super(stack, title);
	}

	public String str = "";
	
	@Override
	public String getText() 
	{
		return str;
	}

	@Override
	public void setText(String str) 
	{
		this.str = str;
	}
	
	@Override
	public void drawText(GuiEntry entry, int width, int height, int left, int top, EntityPlayer player, String key, int page, int mX, int mY)
	{
		int x, y;
		
		if(this.entryName == null)
			this.entryName = key;
		
		String s = this.str;
		x = left + width / 2 - 58;
		y = (top + 15);

		Minecraft.getMinecraft().fontRenderer.setUnicodeFlag(true);
		Minecraft.getMinecraft().fontRenderer.drawSplitString(s, x, y, 110, 0);	
		Minecraft.getMinecraft().fontRenderer.setUnicodeFlag(false);

	}
}
