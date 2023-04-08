package callbacks;

import bot.Main;
import controller.Controller;
import scripting.idlescript.IdleScript;

public class CommandCallback {

  private static final String helpMessageText =
      "@cya@IdleRSC Help Menu:"
          + " %@red@::bothelp - @yel@Shows this help menu"
          + " %@red@::show - @yel@Unhides the bot sidepane"
          + " %@red@::gfx - @yel@toggle graphic rendering"
          + " %@red@::screenshot - @yel@Take a Screenshot"
          + " %@red@::hidepaint or ::showpaint - @yel@Toggle Paint Left-Side Menu"
          + " %@red@::toggleid - @yel@Toggle Item/Object/NPC right click Id's"
          + " %@red@::interlace - @yel@Toggle Interlacing Mode"
          + " %@red@F1  - @yel@Toggle Interlacing Mode"
          + " %@red@F2  - @yel@Toggle Openrsc Left-Side Sub Menu"
          + " %@red@F3  - @yel@Returns Camera Zoom to Default level"
          + " %@red@F4  - @yel@Toggles 1st/3rd Person Perspective"
          + " %@red@F5/F6/F7 - @yel@Attack, Strength, Defense Item Swapping. See Readme."
          + " %@red@F8 - @yel@Spell ID casting. See Readme for Instructions"
          + " %@red@F9 - @yel@Take a Screenshot, saved to ./IdleRSC/Screenshots/accountName"
          + " %@red@F10 - @yel@Lock the client's camera position till F10 or mouse click."
          + " %@red@F11 - @yel@Stop the current script and load a new one"
          + " %@red@F12 - @yel@Show this help menu";

  public static void commandHook(String command) {
    Controller c = Main.getController();
    command = command.toLowerCase();

    switch (command) {
      case "show":
        Main.showBot();
        break;
      case "hidepaint":
        if (c != null) {
          c.setBotPaint(false);
          c.displayMessage("@red@IdleRSC@yel@: Paint hidden.");
        }
        break;
      case "showpaint":
        if (c != null) {
          c.setBotPaint(true);
          c.displayMessage("@red@IdleRSC@yel@: Paint unhidden.");
        }
        break;
      case "interlace":
        if (c != null) {
          if (c.isInterlacing()) {
            c.setInterlacer(false);
          } else if (!c.isInterlacing()) {
            c.setInterlacer(true);
          }
          c.displayMessage("@red@Interlacer@yel@: toggled.");
        }
        break;
      case "gfx":
        if (c != null) {
          c.setDrawing(!c.isDrawEnabled());
        }
        break;
      case "bothelp":
        if (c != null) {
          c.setServerMessage(helpMessageText, true, true);
        }
        break;
      case "toggleid":
        if (c != null) {
          c.toggleViewId();
        }
        break;
      case "screenshot":
        if (c != null) {
          c.takeScreenshot("");
        }
        break;
      default:
        // pass to script
        if (c != null
            && c.getShowBotPaint()
            && c.isRunning()
            && Main.getCurrentRunningScript() != null) {
          if (Main.getCurrentRunningScript() instanceof IdleScript) {
            ((IdleScript) Main.getCurrentRunningScript()).chatCommandInterrupt(command);
          }
        }
        break;
    }
  }
}