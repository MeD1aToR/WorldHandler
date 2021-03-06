package exopandora.worldhandler.usercontent.factory;

import exopandora.worldhandler.WorldHandler;
import exopandora.worldhandler.gui.content.Content;
import exopandora.worldhandler.gui.menu.impl.ILogicMapped;
import exopandora.worldhandler.usercontent.UsercontentAPI;
import exopandora.worldhandler.usercontent.model.JsonItem;
import exopandora.worldhandler.usercontent.model.JsonWidget;
import exopandora.worldhandler.util.ActionHandler;
import exopandora.worldhandler.util.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class WidgetFactory
{
	private final ActionHandlerFactory actionHandlerFactory;
	private final UsercontentAPI api;
	
	public WidgetFactory(UsercontentAPI api, ActionHandlerFactory actionHandlerFactory)
	{
		this.api = api;
		this.actionHandlerFactory = actionHandlerFactory;
	}
	
	public ActionHandlerFactory getActionHandlerFactory()
	{
		return this.actionHandlerFactory;
	}
	
	public UsercontentAPI getApi()
	{
		return this.api;
	}
	
	@OnlyIn(Dist.CLIENT)
	public static class UsercontentLogicMapped<T extends Enum<T>> implements ILogicMapped<JsonItem>
	{
		private final ActionHandlerFactory actionHandlerFactory;
		private final UsercontentAPI api;
		private final Content content;
		private final JsonWidget<T> widget;
		
		public UsercontentLogicMapped(UsercontentAPI api, ActionHandlerFactory actionHandlerFactory, Content content, JsonWidget<T> widget)
		{
			this.api = api;
			this.actionHandlerFactory = actionHandlerFactory;
			this.content = content;
			this.widget = widget;
		}
		
		@Override
		public String translate(JsonItem item)
		{
			String translation = TextFormatting.formatNullable(item.getTranslation());
			return translation == null ? item.getId() : translation;
		}
		
		@Override
		public String toTooltip(JsonItem item)
		{
			return item.getId();
		}
		
		@Override
		public void onClick(JsonItem item)
		{
			try
			{
				this.api.updateValue(this.widget.getAttributes().getId(), item.getId());
				ActionHandler action = this.actionHandlerFactory.createActionHandler(this.content, this.widget.getAction(), item.getId());
				
				if(action != null)
				{
					action.run();
				}
			}
			catch(Exception e)
			{
				WorldHandler.LOGGER.error("Error executing action for widget");
			}
		}
		
		@Override
		public String getId()
		{
			return this.widget.getAttributes().getId();
		}
		
		@Override
		public void onInit(JsonItem item)
		{
			this.onClick(item);
		}
	}
}
