package net.denryu.minecraft.neitoggle;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import codechicken.nei.NEIServerConfig;
import codechicken.nei.NEISPH;
import codechicken.core.ServerUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class NEIToggleCommand implements ICommand
{
  private List aliases;
  public NEIToggleCommand()
  {
    this.aliases = new ArrayList();
    this.aliases.add("neitoggle");
    this.aliases.add("nei");
    this.aliases.add("neit");
  }

  @Override
  public String getCommandName()
  {
    return "neitoggle";
  }

  @Override
  public String getCommandUsage(ICommandSender icommandsender)
  {
    return "Usage: neitoggle (on|off|status|list) [playername]";
  }

  @Override
  public List getCommandAliases()
  {
    return this.aliases;
  }

  @Override
  public void processCommand(ICommandSender icommandsender, String[] astring)
  {
  	// if player is nethier CONSOLE nor OP
    if( !(icommandsender.getCommandSenderName() == "CONSOLE" || icommandsender.getCommandSenderName() == "Server" || ServerUtils.isPlayerOP( icommandsender.getCommandSenderName()) ))
    {
    	icommandsender.addChatMessage(new ChatComponentText("Only OPs are allowed to use /neitoggle! (your name is " +icommandsender.getCommandSenderName()+ ")"));
    	return;
    }
    
    if(astring.length == 1)
    {
      //icommandsender.addChatMessage("Invalid arguments");
      switch(astring[0])
      {
		
      }
      return;
    }
   
    //icommandsender.addChatMessage("Sample: [" + astring[0] + "]");
    
    else if(astring.length == 2)
    {
	    switch(astring[0])
	    {
	    	case "on":
	    		NEIServerConfig.addPlayerToList(astring[1], "permissions.item");
			icommandsender.addChatMessage(new ChatComponentText("NEI Itemspawn Enabled for " + astring[1]));
		
			try
			{
				if(refreshNEIForPlayer(astring[1])) icommandsender.addChatMessage(new ChatComponentText("Client NEI Config Resent Successfully!"));
			}
			catch(Exception ex)
			{
				icommandsender.addChatMessage(new ChatComponentText("Exception [" + ex.getClass().getSimpleName() + "] while resending NEI config"));
			}
	    		break;
	    	case "off":
	    		NEIServerConfig.remPlayerFromList(astring[1], "permissions.item");
			icommandsender.addChatMessage(new ChatComponentText("NEI Itemspawn Disabled for " + astring[1]));
		
			try
			{
				if(refreshNEIForPlayer(astring[1])) icommandsender.addChatMessage(new ChatComponentText("Client NEI Config Resent Successfully!"));
			}
			catch(Exception ex)
			{
				icommandsender.addChatMessage(new ChatComponentText("Exception [" + ex.getClass().getSimpleName() + "] while resending NEI config"));
			}
	    		break;
	    	default:
	    		icommandsender.addChatMessage(new ChatComponentText("Invalid usage of NEITOGGLE"));
	    		break;
	    }
    }
    else
    {
    	icommandsender.addChatMessage(new ChatComponentText("Invalid usage of NEITOGGLE"));
    }
  }
  
  private boolean refreshNEIForPlayer(String playername) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException
  {
  	// func_152612_a is EntityPlayer getPlayerByName(string username)
  	//
  	// seems to return EntityPlayer for online players, null if offline
  	EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(playername);
  	if(player == null) return false;
  	
  	// NEISPH doesn't do anything until registered.
  	// If we use it to send packets for us, but don't register it, it should transmit them without further side effects.
  	NEISPH sphSlave = new NEISPH();
  	Method meth = NEISPH.class.getDeclaredMethod("sendLoginState", new Class[] {EntityPlayerMP.class} );
  	meth.setAccessible(true);
  	meth.invoke(sphSlave, new Object[] {(EntityPlayerMP) player} );
  	
  	return true;
  }

  @Override
  public boolean canCommandSenderUseCommand(ICommandSender icommandsender)
  {
    return true;
  }

  @Override
  public List addTabCompletionOptions(ICommandSender icommandsender,
      String[] astring)
  {
    return null;
  }

  @Override
  public boolean isUsernameIndex(String[] astring, int i)
  {
    return false;
  }

  @Override
  public int compareTo(Object o)
  {
    return 0;
  }
}
