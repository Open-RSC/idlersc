package scripting.idlescript;

import bot.Main;
import controller.Controller;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Grabs red spider eggs in edge dungeon, recommend very high stats ~90+ and good defensive armor.
 *
 * <p>Start in Edge bank with Armor. Sharks in bank REQUIRED.
 *
 * <p>Should bot Teleport if Pkers Attack?. 31 Magic, Laws, Airs, and Earths required for Escape
 * Tele. Unselected, bot WALKS to Edge when Attacked. Selected, bot teleports, then walks to edge.
 *
 * <p>Should bot Return to Eggz after Escaping?. Unselected, bot will log out after escaping Pkers.
 * Selected, bot will grab more food and return.
 *
 * <p>@Author - Kaila
 */
public class K_RedSpiderEggz extends IdleScript {
  private static final Controller c = Main.getController();
  private static JFrame scriptFrame = null;
  private static boolean guiSetup = false;
  private static boolean scriptStarted = false;
  private static boolean teleportOut = false;
  private static boolean returnEscape = true;
  private static int eggzInBank = 0;
  private static int totalEggz = 0;
  private static int totalTrips = 0;

  private static long startTime;
  private static final long startTimestamp = System.currentTimeMillis() / 1000L;

  public int start(String[] parameters) {
    if (!guiSetup) {
      setupGUI();
      guiSetup = true;
    }
    if (scriptStarted) {
      c.displayMessage("@red@Red Spider Egg Picker - By Kaila");
      c.displayMessage("@red@Start in Edge bank with Armor");
      c.displayMessage("@red@Sharks/Laws/Airs/Earths IN BANK REQUIRED");
      c.displayMessage("@red@31 Magic Required for escape tele");
      if (c.isInBank()) {
        c.closeBank();
      }
      if (c.currentY() > 340 && c.currentY() < 500) { // fixed start area bug
        bank();
        eat();
        BankToEgg();
        c.sleep(100);
      }
      scriptStart();
    }
    return 1000; // start() must return an int value now.
  }

  private void scriptStart() {
    while (c.isRunning()) {

      eat();
      leaveCombat();
      c.setStatus("@yel@Picking Eggs..");

      if (c.getInventoryItemCount() > 29 || c.getInventoryItemCount(546) == 0) {
        c.setStatus("@red@Banking..");
        EggToBank();
        bank();
        BankToEgg();
        c.sleep(618);
      }
      if (c.getNearestItemById(219) != null) {
        int[] coords = c.getNearestItemById(219);
        c.pickupItem(coords[0], coords[1], 219, true, true);
        c.sleep(1000);
      } else { // fixed cpu overrun issue
        c.sleep(1000); // fixed cpu overrun issue
      }
    }
  }

  private void bank() {

    c.setStatus("@yel@Banking..");
    c.openBank();
    c.sleep(1200);

    if (c.isInBank()) {

      totalEggz = totalEggz + c.getInventoryItemCount(219);

      for (int itemId : c.getInventoryItemIds()) {
        if (itemId != 546) {
          c.depositItem(itemId, c.getInventoryItemCount(itemId));
        }
      }
      c.sleep(1280);
      eggzInBank = c.getBankItemCount(219);

      if (c.getInventoryItemCount(546) > 1) { // deposit extra shark
        c.depositItem(546, c.getInventoryItemCount(546) - 1);
        c.sleep(340);
      }
      if (c.getInventoryItemCount(546) < 1) { // withdraw 1 shark
        c.withdrawItem(546, 1);
        c.sleep(340);
      }
      if (teleportOut) {
        if (c.getInventoryItemCount(33) < 3) { // withdraw 3 air
          c.withdrawItem(33, 3);
          c.sleep(640);
        }
        if (c.getInventoryItemCount(34) < 1) { // withdraw 1 earth
          c.withdrawItem(34, 1);
          c.sleep(640);
        }
        if (c.getInventoryItemCount(42) < 1) { // withdraw 1 law
          c.withdrawItem(42, 1);
          c.sleep(640);
        }
      }
      if (c.getBankItemCount(546) == 0) {
        c.setStatus("@red@NO Sharks in the bank, Logging Out!.");
        c.setAutoLogin(false);
        c.logout();
        if (!c.isLoggedIn()) {
          c.stop();
        }
      }
      c.closeBank();
      c.sleep(640);
    }
  }

  private void eat() {

    int eatLvl = c.getBaseStat(c.getStatId("Hits")) - 20;

    if (c.getCurrentStat(c.getStatId("Hits")) < eatLvl) {

      leaveCombat();
      c.setStatus("@red@Eating..");

      boolean ate = false;

      for (int id : c.getFoodIds()) {
        if (c.getInventoryItemCount(id) > 0) {
          c.itemCommand(id);
          c.sleep(700);
          ate = true;
          break;
        }
      }
      if (!ate) { // only activates if hp goes to -20 again THAT trip, will bank and get new shark
        // usually
        c.setStatus("@red@We've ran out of Food! Running Away!.");
        if (!teleportOut
            || c.getInventoryItemCount(42) < 1
            || c.getInventoryItemCount(33) < 3
            || c.getInventoryItemCount(34) < 1) { // or no earths/airs/laws
          EggToBank();
          bank();
        }
        if (teleportOut) {
          c.castSpellOnSelf(c.getSpellIdFromName("Lumbridge Teleport(1)"));
          c.sleep(800);
          if (c.currentY() > 3000) {
            c.castSpellOnSelf(c.getSpellIdFromName("Lumbridge Teleport(2)"));
            c.sleep(800);
          }
          if (c.currentY() > 3000) {
            c.castSpellOnSelf(c.getSpellIdFromName("Lumbridge Teleport(3)"));
            c.sleep(800);
          }
          c.walkTo(120, 644);
          c.atObject(119, 642);
          c.walkTo(217, 447);
        }
        if (!returnEscape) {
          c.setAutoLogin(false);
          c.logout();
          c.sleep(1000);

          if (!c.isLoggedIn()) {
            c.stop();
            c.logout();
          }
        }
        if (returnEscape) {
          bank();
          BankToEgg();
          c.sleep(618);
        }
      }
    }
  }

  private void EggToBank() {
    c.setStatus("@gre@Walking to Bank..");
    c.walkTo(197, 3244);
    c.walkTo(197, 3255);
    c.walkTo(196, 3265);
    c.setStatus("@gre@Opening Wildy Gate North to South(1)..");
    c.atObject(196, 3266);
    c.sleep(640);
    openGateNorthToSouth();
    c.walkTo(197, 3266);
    c.walkTo(204, 3272);
    c.walkTo(210, 3273);
    if (c.getObjectAtCoord(211, 3272) == 57) {
      c.setStatus("@gre@Opening Edge Gate..");
      c.walkTo(210, 3273);
      c.atObject(211, 3272);
      c.sleep(340);
    }
    c.setStatus("@gre@Walking to Bank..");
    c.walkTo(217, 3283);
    c.walkTo(215, 3294);
    c.walkTo(215, 3299);
    c.atObject(215, 3300);
    c.sleep(640);
    c.walkTo(217, 458);
    c.walkTo(221, 447);
    c.walkTo(217, 448);
    c.sleep(640);
    totalTrips = totalTrips + 1;
    c.setStatus("@gre@Done Walking..");
  }

  private void BankToEgg() {
    c.setStatus("@gre@Walking to Eggs..");
    c.walkTo(221, 447);
    c.walkTo(217, 458);
    c.walkTo(215, 467);
    c.atObject(215, 468);
    c.sleep(640);
    c.walkTo(217, 3283);
    c.walkTo(211, 3273);
    if (c.getObjectAtCoord(211, 3272) == 57) {
      c.setStatus("@gre@Opening Edge Gate..");
      c.walkTo(211, 3273);
      c.atObject(211, 3272);
      c.sleep(340);
    }
    c.setStatus("@gre@Walking to Bank..");
    c.walkTo(204, 3272);
    c.walkTo(199, 3272);
    c.walkTo(197, 3266);
    c.setStatus("@gre@Opening Wildy Gate, South to North(1)..");
    c.atObject(196, 3266);
    c.sleep(640);
    openGateSouthToNorth();
    c.walkTo(197, 3244);
    c.walkTo(208, 3240);
    c.setStatus("@gre@Done Walking..");
  }

  private void leaveCombat() {
    for (int i = 1; i <= 15; i++) {
      if (c.isInCombat()) {
        c.setStatus("@red@Leaving combat (n)..");
        c.walkTo(c.currentX(), c.currentY(), 0, true);
        c.sleep(600);
      } else {
        c.setStatus("@red@Done Leaving combat..");
        break;
      }
      c.sleep(10);
    }
  }

  private void openGateNorthToSouth() {
    for (int i = 1; i <= 25; i++) {
      if (c.currentY() == 3265) {
        c.setStatus("@gre@Opening Wildy Gate..");
        c.atObject(196, 3266);
        c.sleep(640);
      } else {
        c.setStatus("@red@Done Opening Wildy Gate..");
        break;
      }
      c.sleep(10);
    }
  }

  private void openGateSouthToNorth() {
    for (int i = 1; i <= 25; i++) {
      if (c.currentY() == 3266) {
        c.setStatus("@gre@Opening Wildy Gate..");
        c.atObject(196, 3266);
        c.sleep(640);
      } else {
        c.setStatus("@red@Done Opening Wildy Gate..");
        break;
      }
      c.sleep(10);
    }
  }

  // GUI stuff below (icky)
  private void setupGUI() {
    JLabel header = new JLabel("Red Spider Egg Picker @mag@~ by Kaila");
    JLabel label1 = new JLabel("Start in Edge bank with Armor");
    JLabel label2 = new JLabel("Sharks in bank REQUIRED");
    JCheckBox teleportCheckbox = new JCheckBox("Teleport if Pkers Attack?", false);
    JLabel label3 = new JLabel("31 Magic, Laws, Airs, and Earths required for Escape Tele");
    JLabel label4 = new JLabel("Unselected, bot WALKS to Edge when Attacked");
    JLabel label5 = new JLabel("Selected, bot teleports, then walks to edge");
    JCheckBox escapeCheckbox = new JCheckBox("Return to Eggz after Escaping?", true);
    JLabel label6 = new JLabel("Unselected, bot will log out after escaping Pkers");
    JLabel label7 = new JLabel("Selected, bot will grab more food and return");
    JButton startScriptButton = new JButton("Start");

    startScriptButton.addActionListener(
        e -> {
          teleportOut = teleportCheckbox.isSelected();
          returnEscape = escapeCheckbox.isSelected();
          scriptFrame.setVisible(false);
          scriptFrame.dispose();
          startTime = System.currentTimeMillis();
          scriptStarted = true;
        });

    scriptFrame = new JFrame(c.getPlayerName() + " - options");

    scriptFrame.setLayout(new GridLayout(0, 1));
    scriptFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    scriptFrame.add(header);
    scriptFrame.add(label1);
    scriptFrame.add(label2);
    scriptFrame.add(teleportCheckbox);
    scriptFrame.add(label3);
    scriptFrame.add(label4);
    scriptFrame.add(label5);
    scriptFrame.add(escapeCheckbox);
    scriptFrame.add(label6);
    scriptFrame.add(label7);
    scriptFrame.add(startScriptButton);

    scriptFrame.pack();
    scriptFrame.setLocationRelativeTo(null);
    scriptFrame.setVisible(true);
    scriptFrame.requestFocusInWindow();
  }

  @Override
  public void paintInterrupt() {
    if (c != null) {

      String runTime = c.msToString(System.currentTimeMillis() - startTime);
      int successPerHr = 0;
      int TripSuccessPerHr = 0;
      long currentTimeInSeconds = System.currentTimeMillis() / 1000L;

      try {
        float timeRan = currentTimeInSeconds - startTimestamp;
        float scale = (60 * 60) / timeRan;
        successPerHr = (int) (totalEggz * scale);
        TripSuccessPerHr = (int) (totalTrips * scale);

      } catch (Exception e) {
        // divide by zero
      }
      int x = 6;
      int y = 21;
      c.drawString("@red@RedSpiderEggz @gre@by Kaila", x, y - 3, 0xFFFFFF, 1);
      c.drawString("@whi@____________________", x, y, 0xFFFFFF, 1);
      c.drawString("@whi@Eggs in Bank: @gre@" + eggzInBank, x, y + 14, 0xFFFFFF, 1);
      c.drawString(
          "@whi@Eggs Picked: @gre@"
              + totalEggz
              + "@yel@ (@whi@"
              + String.format("%,d", successPerHr)
              + "@yel@/@whi@hr@yel@)",
          x,
          y + (14 * 2),
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@Total Trips: @gre@"
              + totalTrips
              + "@yel@ (@whi@"
              + String.format("%,d", TripSuccessPerHr)
              + "@yel@/@whi@hr@yel@)",
          x,
          y + (14 * 3),
          0xFFFFFF,
          1);
      c.drawString("@whi@Runtime: " + runTime, x, y + (14 * 4), 0xFFFFFF, 1);
      c.drawString("@whi@____________________", x, y + 3 + (14 * 4), 0xFFFFFF, 1);
    }
  }
}