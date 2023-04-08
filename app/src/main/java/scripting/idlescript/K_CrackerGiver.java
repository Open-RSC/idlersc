package scripting.idlescript;

import bot.Main;
import controller.Controller;
import orsc.ORSCharacter;

/**
 * Christmas Cracker Opener.
 *
 * <p>Opens Christmas Crackers by using them on a 2nd account. Used in conjunction with K_GiftTaker
 * Script!
 *
 * <p>Should work in any bank, Ideal bank is Draynor. requires 2 accounts. This bot is the present
 * "taker", it will bank when you have 29 items.
 *
 * <p>To setup start both accounts near each other with NO items in either inventory. start the
 * taker bot FIRST before even starting giver bot. the bots will need to be synced up similar to
 * trader bots. ideally monitor them, if something goes wrong present stuff will drop to the floor
 * and despawn!!!!! you have been warned!
 *
 * <p>WARNING: while within 1 tile of the giver, you will continue to recieve presents. WARNING:
 * regardless of how full your inventory is. items WILL continue to be recieved and extras will drop
 * to the floor and despawn. Always monitor this bot!!! @Author ~ Kaila
 */
public class K_CrackerGiver extends IdleScript {
  private static final Controller c = Main.getController();

  public int start(String[] parameters) {
    c.displayMessage("@red@present GIVER! Let's party like it's 2004!");
    c.displayMessage("@red@Directions inside K_CrackerGiver.java file");
    c.displayMessage("@red@Ideal location is Draynor Bank!");

    while (c.isRunning()) {
      if (c.getInventoryItemCount() > 29) {
        c.setStatus("@gre@Banking.");
        if (!c.isInBank()) {
          int[] bankerIds = {95, 224, 268, 540, 617, 792};
          ORSCharacter npc = c.getNearestNpcByIds(bankerIds, false);
          if (npc != null) {
            c.setStatus("@yel@Walking to Banker..");
            c.displayMessage("@yel@Walking to Banker..");
            c.walktoNPCAsync(npc.serverIndex);
            c.sleep(200);
          } else {
            c.log("@red@Error..");
            c.sleep(1000);
          }
        }
        bank();
      }
      if (c.getInventoryItemCount(575) < 2) {
        c.setStatus("@gre@Banking.");
        if (!c.isInBank()) {
          int[] bankerIds = {95, 224, 268, 540, 617, 792};
          ORSCharacter npc = c.getNearestNpcByIds(bankerIds, false);
          if (npc != null) {
            c.setStatus("@yel@Walking to Banker..");
            c.displayMessage("@yel@Walking to Banker..");
            c.walktoNPCAsync(npc.serverIndex);
            c.sleep(200);
          } else {
            c.log("@red@Error..");
            c.sleep(1000);
          }
        }
        bank();
      }
      if (c.getInventoryItemCount() < 30 && c.getInventoryItemCount(575) > 1) {
        c.setStatus("@gre@Opening.");
        c.useItemOnPlayer(1, c.getPlayerServerIndexByName("kailashu")); // replace the player name
        c.sleep(640);
      }
    }

    return 1000; // start() must return an int value now.
  }

  private void bank() {

    c.setStatus("@yel@Banking..");
    c.openBank();
    c.sleep(640);

    if (c.isInBank()) {
      if (c.getInventoryItemCount() > 0) {
        for (int itemId : c.getInventoryItemIds()) {
          c.depositItem(itemId, c.getInventoryItemCount(itemId));
        }
        c.sleep(1280);
      }
      if (c.getInventoryItemCount(575) < 23) {
        c.withdrawItem(575, 23 - c.getInventoryItemCount(575));
        c.sleep(1280);
        c.closeBank();
        c.sleep(1280);
      }
    }
  }
}