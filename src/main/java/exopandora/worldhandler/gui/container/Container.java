package exopandora.worldhandler.gui.container;

import java.util.ArrayList;
import java.util.List;

import exopandora.worldhandler.gui.menu.Menu;
import exopandora.worldhandler.gui.menu.IMenu;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class Container extends Screen implements IContainer
{
	protected Container(ITextComponent title)
	{
		super(title);
	}
	
	protected final List<IMenu> menus = new ArrayList<IMenu>();
	
	@Override
	public <T extends Widget> T add(T button)
	{
		return super.addButton(button);
	}
	
	public <T extends TextFieldWidget> T add(T textfield)
	{
		this.children.add(textfield);
		return textfield;
	}
	
	@Override
	public void init()
	{
		super.init();
	}
	
	@Override
	public void add(Menu menu)
	{
		this.menus.add(menu);
	}
}
