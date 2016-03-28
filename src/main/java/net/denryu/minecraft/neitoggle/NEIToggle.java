package net.denryu.minecraft.neitoggle;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import codechicken.nei.NEISPH;
import java.lang.reflect.Method;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.player.EntityPlayerMP;

//
// This is a minimal (but complete) mod entry point.
//
@Mod(modid = NEIToggle.ID, name = NEIToggle.NAME, version = NEIToggle.VERSION, acceptableRemoteVersions="*", dependencies ="required-after:NotEnoughItems")
//@NetworkMod(clientSideRequired = false, serverSideRequired = true)
public class NEIToggle
{
  public final static String ID = "NEIToggle";
  public final static String NAME = "NEIToggle";
  public final static String VERSION = "1.0";
  private static final Logger logger = LogManager.getLogger("NEIToggle");
   
  // The instance of your mod that Forge uses.
  @Instance(ID)
  public static NEIToggle  instance;
 
  //
  // Mod entry points
  //  
  
  @EventHandler
  public void serverStarting(FMLServerStartingEvent event)
  {
  	// set the NEI sendLoginState to accessable so we can use it to our own end
  	// (retransmit NEI config AFTER the plaer logs in)
  	try
  	{
		Method method = NEISPH.class.getDeclaredMethod("sendLoginState", new Class[] {EntityPlayerMP.class} );
		method.setAccessible(true);
		logger.log(Level.FATAL, "NEI sendLoginState is now globally accessable!");
	}
	catch(NoSuchMethodException ex)
	{
		logger.log(Level.FATAL, "Couldn't change visibilty for NEI sendLoginState! This mod will certainly crash-on-use!!!!");
	}
	event.registerServerCommand(new NEIToggleCommand());
  }
}
