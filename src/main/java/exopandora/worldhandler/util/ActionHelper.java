package exopandora.worldhandler.util;

import exopandora.worldhandler.Main;
import exopandora.worldhandler.WorldHandler;
import exopandora.worldhandler.builder.impl.BuilderDifficulty;
import exopandora.worldhandler.builder.impl.BuilderGamemode;
import exopandora.worldhandler.builder.impl.BuilderGamemode.EnumGamemode;
import exopandora.worldhandler.builder.impl.BuilderTime;
import exopandora.worldhandler.builder.impl.BuilderTime.EnumMode;
import exopandora.worldhandler.builder.impl.BuilderWeather;
import exopandora.worldhandler.builder.impl.BuilderWeather.EnumWeather;
import exopandora.worldhandler.config.Config;
import exopandora.worldhandler.gui.container.impl.GuiWorldHandler;
import exopandora.worldhandler.gui.content.Content;
import exopandora.worldhandler.gui.content.Contents;
import exopandora.worldhandler.gui.content.impl.ContentChild;
import exopandora.worldhandler.gui.content.impl.ContentContinue;
import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.NoteBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Difficulty;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ActionHelper
{
	public static void backToGame()
	{
		Minecraft.getInstance().displayGuiScreen(null);
		Minecraft.getInstance().mouseHelper.grabMouse();
	}
	
	public static void back(Content content) throws Exception
	{
		if(content.getBackContent() != null)
		{
			Minecraft.getInstance().displayGuiScreen(new GuiWorldHandler(content.getBackContent()));
		}
	}
	
	public static void changeTab(Content content, int index)
	{
		ActionHelper.tryRun(() -> ActionHelper.open(content.getCategory().getContent(index)));
	}
	
	public static void open(String id) throws Exception
	{
		if(id != null)
		{
			ActionHelper.open(Contents.getRegisteredContent(id));
		}
	}
	
	public static void open(Content content) throws Exception
	{
		if(content != null && !(content instanceof ContentContinue))
		{
			if(content instanceof ContentChild && Minecraft.getInstance().currentScreen != null && Minecraft.getInstance().currentScreen instanceof GuiWorldHandler)
			{
				Minecraft.getInstance().displayGuiScreen(new GuiWorldHandler(((ContentChild) content).withParent(((GuiWorldHandler) Minecraft.getInstance().currentScreen).getContent())));
			}
			else
			{
				Minecraft.getInstance().displayGuiScreen(new GuiWorldHandler(content));
			}
		}
	}
	
	public static void timeDawn()
	{
		CommandHelper.sendCommand(new BuilderTime(EnumMode.SET, Config.getSettings().getDawn()));
	}
	
	public static void timeNoon()
	{
		CommandHelper.sendCommand(new BuilderTime(EnumMode.SET, Config.getSettings().getNoon()));
	}
	
	public static void timeSunset()
	{
		CommandHelper.sendCommand(new BuilderTime(EnumMode.SET, Config.getSettings().getSunset()));
	}
	
	public static void timeMidnight()
	{
		CommandHelper.sendCommand(new BuilderTime(EnumMode.SET, Config.getSettings().getMidnight()));
	}
	
	public static void weatherClear()
	{
		CommandHelper.sendCommand(new BuilderWeather(EnumWeather.CLEAR));
	}
	
	public static void weatherRain()
	{
		CommandHelper.sendCommand(new BuilderWeather(EnumWeather.RAIN));
	}
	
	public static void weatherThunder()
	{
		CommandHelper.sendCommand(new BuilderWeather(EnumWeather.THUNDER));
	}
	
	public static void difficultyPeaceful()
	{
		CommandHelper.sendCommand(new BuilderDifficulty(Difficulty.PEACEFUL));
	}
	
	public static void difficultyEasy()
	{
		CommandHelper.sendCommand(new BuilderDifficulty(Difficulty.EASY));
	}
	
	public static void difficultyNormal()
	{
		CommandHelper.sendCommand(new BuilderDifficulty(Difficulty.NORMAL));
	}
	
	public static void difficultyHard()
	{
		CommandHelper.sendCommand(new BuilderDifficulty(Difficulty.HARD));
	}
	
	public static void gamemodeSurvival()
	{
		CommandHelper.sendCommand(new BuilderGamemode(EnumGamemode.SURVIVAL));
	}
	
	public static void gamemodeCreative()
	{
		CommandHelper.sendCommand(new BuilderGamemode(EnumGamemode.CREATIVE));
	}
	
	public static void gamemodeAdventure()
	{
		CommandHelper.sendCommand(new BuilderGamemode(EnumGamemode.ADVENTURE));
	}
	
	public static void gamemodeSpectator()
	{
		CommandHelper.sendCommand(new BuilderGamemode(EnumGamemode.SPECTATOR));
	}
	
	public static void tryRun(ActionHandler action)
	{
		try
		{
			if(action != null)
			{
				action.run();
			}
		}
		catch(Exception e)
		{
			Minecraft.getInstance().displayGuiScreen(null);
			Minecraft.getInstance().mouseHelper.grabMouse();
			
			Style redColor = new Style().setColor(TextFormatting.RED);
			
			ITextComponent message = new TranslationTextComponent("<" + Main.NAME + "> %s", new TranslationTextComponent("worldhandler.error.gui")).setStyle(redColor);
			ITextComponent cause = new StringTextComponent(" " + e.getClass().getCanonicalName() + ": " + e.getMessage()).setStyle(redColor);
			
			Minecraft.getInstance().ingameGUI.addChatMessage(ChatType.SYSTEM, message);
			Minecraft.getInstance().ingameGUI.addChatMessage(ChatType.SYSTEM, cause);
			
			WorldHandler.LOGGER.throwing(e);
		}
	}
	
	public static void displayGui()
	{
		if(!CommandHelper.canPlayerIssueCommand() && Config.getSettings().permissionQuery())
		{
			Minecraft.getInstance().ingameGUI.addChatMessage(ChatType.SYSTEM, new StringTextComponent(TextFormatting.RED + I18n.format("worldhandler.permission.refused")));
			Minecraft.getInstance().ingameGUI.addChatMessage(ChatType.SYSTEM, new StringTextComponent(TextFormatting.RED + I18n.format("worldhandler.permission.refused.change", I18n.format("gui.worldhandler.config.settings.permission_query"))));
		}
		else
		{
			ActionHelper.tryRun(() ->
			{
				if(BlockHelper.getFocusedBlock() instanceof AbstractSignBlock)
				{
					Minecraft.getInstance().displayGuiScreen(new GuiWorldHandler(Contents.SIGN_EDITOR));
				}
				else if(BlockHelper.getFocusedBlock() instanceof NoteBlock)
				{
					Minecraft.getInstance().displayGuiScreen(new GuiWorldHandler(Contents.NOTE_EDITOR));
				}
				else
				{
					Minecraft.getInstance().displayGuiScreen(new GuiWorldHandler(Contents.MAIN));
				}
			});
		}
	}
}
