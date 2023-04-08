package scripting.idlescript;

import bot.Main;
import controller.Controller;
import java.awt.GridLayout;
import javax.swing.*;
import orsc.ORSCharacter;

/**
 * Ice Dungeon Ice Giant/Warrior Killer - By Kaila
 *
 * <p>This bot supports the "autostart" Parameter"); Usage: foodname numberOfFood potUp? example:
 * "shark,5,true". "autostart": uses lobsters,5,true.
 *
 * <p>Start in Fally East bank or In Ice Cave. Food in bank REQUIRED. Use regular Atk/Str Pots
 * Selector. Food Withdraw amount Selector Type of Food Array Selector
 *
 * <p>Author - Kaila
 */
public class K_AsgarnianIce extends IdleScript {
  private static final Controller c = Main.getController();
  private static JFrame scriptFrame = null;
  private static boolean guiSetup = false;
  private static boolean scriptStarted = false;
  private static boolean potUp = true;
  private static boolean timeToBank = false;

  private static boolean isWithinLootzone(int x, int y) {
    return c.distance(308, 3520, x, y) <= 15; // center of lootzone
  }

  private static long startTime;
  private static final long startTimestamp = System.currentTimeMillis() / 1000L;
  private static int totalGuam = 0;
  private static int totalMar = 0;
  private static int totalTar = 0;
  private static int totalHar = 0;
  private static int totalRan = 0;
  private static int totalIrit = 0;
  private static int totalAva = 0;
  private static int totalKwuarm = 0;
  private static int totalCada = 0;
  private static int totalDwarf = 0;
  private static int totalLaw = 0;
  private static int totalNat = 0;
  private static int totalDeath = 0;
  private static int totalBlood = 0;
  private static int totalLoop = 0;
  private static int totalTooth = 0;
  private static int totalLeft = 0;
  private static int totalSpear = 0;
  private static int totalGems = 0;
  private static int totalTrips = 0;
  private static int fightMode = 3;
  private static int foodId = -1;
  private static int foodWithdrawAmount = 0;
  private static final int[] bones = {
    20, // regular
    413, // big
    604, // bat?
    814
  }; // dragon
  private static final int[] attackPot = {
    476, // reg attack pot (1)
    475, // reg attack pot (2)
    474 // reg attack pot (3)
  };
  private static final int[] strPot = {
    224, // reg str pot (1)
    223, // reg str pot (2)
    222 // reg str pot (3)
  };
  private static final int[] loot = {
    526, // tooth half
    527, // loop half
    1277, // shield (left) half
    1092, // rune spear
    160, // saph
    159, // emerald
    158, // ruby
    157, // diamond
    33, // air rune
    34, // Earth rune
    31, // fire rune
    32, // water rune
    36, // body runes
    46, // cosmic
    40, // nature rune
    42, // law rune
    35, // mind rune
    41, // chaos rune
    38, // death rune
    619, // blood rune
    165, // Grimy Guam
    435, // Grimy mar
    436, // Grimy tar
    437, // Grimy har
    438, // Grimy ranarr
    439, // Grimy irit
    440, // Grimy ava
    441, // Grimy kwu
    442, // Grimy cada
    443, // Grimy dwu
    10, // coins
    153, // mithril ore
    11, // bronze arrow
    433, // Black Kite
    126, // Mithril Square
    413, // Big bones
    20 // bones
  };
  private static final int[] foodIds = {
    1191, // cooked Manta Ray
    1193, // cooked Sea Turtle
    546, // cooked shark
    370, // cooked swordfish
    367, // cooked tuna
    373, // cooked lobster
    555, // cooked Bass
    553, // cooked Mackerel
    551, // cooked Cod
    364, // cooked Pike
    362, // cooked Herring
    357, // cooked Salmon
    352, // cooked Trout
    350, // cooked Anchovies
    132 // cooked Chicken
  };
  private static final String[] foodTypes =
      new String[] {
        "Manta Ray",
        "Sea Turtle",
        "Shark",
        "Swordfish",
        "Tuna",
        "Lobster",
        "Bass",
        "Mackerel",
        "Cod",
        "Pike",
        "Herring",
        "Salmon",
        "Trout",
        "Anchovies",
        "Shrimp",
        "Chicken"
      };

  private void startSequence() {
    c.displayMessage("@red@Asgarnian Pirate Hobs - By Kaila");
    c.displayMessage("@red@Start in Fally East bank with Armor");
    c.displayMessage("@red@Sharks IN BANK REQUIRED");
    startTime = System.currentTimeMillis();
    if (c.isInBank()) {
      c.closeBank();
    }
    if (c.currentY() < 3000) {
      bank();
      BankToIce();
      c.sleep(1380);
    }
  } // param 0 - type of food, param 1 - number of food, param 2 - potUp

  public int start(String[] parameters) {
    if (parameters[0].toLowerCase().startsWith("auto")) {
      c.displayMessage("Got Autostart, using 2 Lobs, yes pots", 0);
      System.out.println("Got Autostart, using 2 Lobs, yes pots");
      foodId = 373;
      foodWithdrawAmount = 2;
      potUp = true;
      startSequence();
      scriptStart();
    }
    if (scriptStarted) {
      startSequence();
      scriptStart();
    }
    if (!scriptStarted && !guiSetup) {
      setupGUI();
      guiSetup = true;
    }
    return 1000; // start() must return an int value now.
  }

  private void scriptStart() {
    while (c.isRunning()) {

      eat();
      buryBones();
      if (c.getFightMode() != fightMode) {
        c.log("@red@Changing fightmode to " + fightMode);
        c.setFightMode(fightMode);
      }
      if (c.getInventoryItemCount() < 30) {
        lootScript();
        if (potUp) {
          if (c.getCurrentStat(c.getStatId("Attack")) == c.getBaseStat(c.getStatId("Attack"))) {
            if (c.getInventoryItemCount(attackPot[0]) > 0
                || c.getInventoryItemCount(attackPot[1]) > 0
                || c.getInventoryItemCount(attackPot[2]) > 0) {
              attackBoost();
            }
          }
          if (c.getCurrentStat(c.getStatId("Strength")) == c.getBaseStat(c.getStatId("Strength"))) {
            if (c.getInventoryItemCount(strPot[0]) > 0
                || c.getInventoryItemCount(strPot[1]) > 0
                || c.getInventoryItemCount(strPot[2]) > 0) {
              strengthBoost();
            }
          }
        }
        if (!c.isInCombat()) {
          int[] npcIds = {135, 158};
          ORSCharacter npc = c.getNearestNpcByIds(npcIds, false);
          if (npc != null) {
            c.setStatus("@yel@Attacking..");
            // c.walktoNPC(npc.serverIndex,1);
            c.attackNpc(npc.serverIndex);
            c.sleep(1000);
          } else {
            c.sleep(1000);
            if (c.currentX() != 305 || c.currentY() != 3522) {
              c.walkTo(305, 3522);
              c.sleep(1000);
            }
          }
        }
        c.sleep(320);
      }
      if (c.getInventoryItemCount() > 29 || c.getInventoryItemCount() == 0 || timeToBank) {
        c.setStatus("@yel@Banking..");
        IceToBank();
        bank();
        BankToIce();
        c.sleep(618);
      }
    }
  }

  private void lootScript() {
    for (int lootId : loot) {
      int[] coords = c.getNearestItemById(lootId);
      if (coords != null && isWithinLootzone(coords[0], coords[1])) {
        c.setStatus("@yel@Looting..");
        c.walkTo(coords[0], coords[1]);
        c.pickupItem(coords[0], coords[1], lootId, true, true);
        c.sleep(618);
      }
    }
  }

  private void bank() {

    c.setStatus("@yel@Banking..");
    c.openBank();
    c.sleep(640);

    if (c.isInBank()) {

      totalGuam = totalGuam + c.getInventoryItemCount(165);
      totalMar = totalMar + c.getInventoryItemCount(435);
      totalTar = totalTar + c.getInventoryItemCount(436);
      totalHar = totalHar + c.getInventoryItemCount(437);
      totalRan = totalRan + c.getInventoryItemCount(438);
      totalIrit = totalIrit + c.getInventoryItemCount(439);
      totalAva = totalAva + c.getInventoryItemCount(440);
      totalKwuarm = totalKwuarm + c.getInventoryItemCount(441);
      totalCada = totalCada + c.getInventoryItemCount(442);
      totalDwarf = totalDwarf + c.getInventoryItemCount(443);
      totalLaw = totalLaw + c.getInventoryItemCount(42);
      totalNat = totalNat + c.getInventoryItemCount(40);
      totalDeath = totalDeath + c.getInventoryItemCount(38);
      totalBlood = totalBlood + c.getInventoryItemCount(619);
      totalLoop = totalLoop + c.getInventoryItemCount(527);
      totalTooth = totalTooth + c.getInventoryItemCount(526);
      totalLeft = totalLeft + c.getInventoryItemCount(1277);
      totalGems =
          totalGems
              + c.getInventoryItemCount(160)
              + c.getInventoryItemCount(159)
              + c.getInventoryItemCount(158)
              + c.getInventoryItemCount(157);
      totalSpear = totalSpear + c.getInventoryItemCount(1092);
      if (c.getInventoryItemCount() > 2) {
        for (int itemId : c.getInventoryItemIds()) {
          if (itemId != 476
              && itemId != 475
              && itemId != 224
              && itemId != 223) { // dont deposit partial potions!
            c.depositItem(itemId, c.getInventoryItemCount(itemId));
          }
        }
      }
      c.sleep(640);
      if (potUp) {
        if (c.getInventoryItemCount(attackPot[0]) < 1
            && c.getInventoryItemCount(attackPot[1]) < 1
            && c.getInventoryItemCount(attackPot[2]) < 1) { // withdraw 10 shark if needed
          c.withdrawItem(attackPot[2], 1);
          c.sleep(340);
        }
        if (c.getInventoryItemCount(strPot[0]) < 1
            && c.getInventoryItemCount(strPot[1]) < 1
            && c.getInventoryItemCount(strPot[2]) < 1) { // withdraw 10 shark if needed
          c.withdrawItem(strPot[2], 1);
          c.sleep(340);
        }
      }
      if (c.getInventoryItemCount(foodId) < foodWithdrawAmount) { // withdraw 20 shark
        c.withdrawItem(foodId, foodWithdrawAmount);
        c.sleep(340);
      }
      if (c.getBankItemCount(foodId) == 0) {
        c.setStatus("@red@NO Food in the bank, Logging Out!.");
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

  private void buryBones() {
    if (!c.isInCombat()) {
      for (int id : bones) {
        if (c.getInventoryItemCount(id) > 0) {
          c.setStatus("@yel@Burying..");
          c.itemCommand(id);

          c.sleep(618);
          buryBones();
        }
      }
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
        // c.setStatus("@yel@Banking..");
        IceToBank();
        bank();
        BankToIce();
        c.sleep(618);
      }
    }
  }

  private void attackBoost() {
    leaveCombat();
    if (c.getInventoryItemCount(attackPot[0]) > 0) {
      c.itemCommand(attackPot[0]);
      c.sleep(320);
    } else if (c.getInventoryItemCount(attackPot[1]) > 0) {
      c.itemCommand(attackPot[1]);
      c.sleep(320);
    } else if (c.getInventoryItemCount(attackPot[2]) > 0) {
      c.itemCommand(attackPot[2]);
      c.sleep(320);
    }
  }

  private void strengthBoost() {
    leaveCombat();
    if (c.getInventoryItemCount(strPot[0]) > 0) {
      c.itemCommand(strPot[0]);
      c.sleep(320);
    } else if (c.getInventoryItemCount(strPot[1]) > 0) {
      c.itemCommand(strPot[1]);
      c.sleep(320);
    } else if (c.getInventoryItemCount(strPot[2]) > 0) {
      c.itemCommand(strPot[2]);
      c.sleep(320);
    }
  }

  private void IceToBank() {
    c.setStatus("@gre@Walking to Bank..");
    c.walkTo(302, 3519);
    c.walkTo(293, 3519);
    c.walkTo(291, 3521);
    c.walkTo(284, 3522);
    c.walkTo(279, 3531);
    c.walkTo(279, 3540);
    c.walkTo(285, 3544);
    c.atObject(285, 3543); // go up ladder
    c.sleep(1000);
    c.walkTo(287, 711);
    c.walkTo(287, 694);
    c.walkTo(287, 680);
    c.walkTo(287, 673);
    c.walkTo(287, 665);
    c.walkTo(287, 652);
    c.walkTo(289, 650);
    c.walkTo(288, 649);
    c.walkTo(288, 639);
    c.walkTo(288, 629);
    c.walkTo(288, 619);
    c.walkTo(288, 609);
    c.walkTo(290, 607);
    c.walkTo(290, 597);
    c.walkTo(290, 587);
    c.walkTo(290, 577);
    c.walkTo(290, 575);
    c.walkTo(286, 571);
    c.sleep(640);
    totalTrips = totalTrips + 1;
    c.setStatus("@gre@Done Walking..");
  }

  private void BankToIce() {
    c.setStatus("@gre@Walking to Ice Dungeon..");
    c.walkTo(287, 571);
    c.walkTo(290, 575);
    c.walkTo(290, 577);
    c.walkTo(290, 587);
    c.walkTo(290, 597);
    c.walkTo(290, 607);
    c.walkTo(288, 609);
    c.walkTo(288, 619);
    c.walkTo(288, 629);
    c.walkTo(288, 639);
    c.walkTo(288, 649);
    c.walkTo(289, 650);
    c.walkTo(287, 652);
    c.walkTo(287, 665);
    // add pathing??
    c.walkTo(287, 673);
    c.walkTo(287, 680);
    c.walkTo(287, 694);
    c.walkTo(287, 711);
    c.walkTo(285, 712);
    c.atObject(285, 711); // go down ladder
    c.sleep(1000);
    // c.walkTo(282,3543);
    c.walkTo(285, 3544);
    c.walkTo(279, 3540);
    c.walkTo(279, 3531);
    c.walkTo(284, 3522);
    c.walkTo(291, 3521);
    c.walkTo(293, 3519);
    c.walkTo(302, 3519);
    c.walkTo(305, 3522);
    c.setStatus("@gre@Done Walking..");
  }

  private void leaveCombat() {
    for (int i = 1; i <= 15; i++) {
      if (c.isInCombat()) {
        c.setStatus("@red@Leaving combat..");
        c.walkTo(c.currentX(), c.currentY(), 0, true);
        c.sleep(600);
      } else {
        c.setStatus("@red@Done Leaving combat..");
        break;
      }
      c.sleep(10);
    }
  }

  // GUI stuff below (icky)

  private void setupGUI() {

    JLabel header = new JLabel("Ice Dungeon Ice Giant/Warrior Killer - by Kaila");
    JLabel label1 = new JLabel("Start in Fally East bank or In Ice Cave");
    JLabel spacer = new JLabel("             ");
    JLabel label2 = new JLabel("Chat commands can be used to direct the bot");
    JLabel label3 = new JLabel("Example ::bank ");
    JLabel label4 = new JLabel("Combat Styles ::attack :strength ::defense ::controller");
    JLabel label5 = new JLabel("This bot supports the \"autostart\" Parameter");
    JLabel label6 = new JLabel("Usage: foodname numberOfFood potUp?");
    JLabel label7 = new JLabel("example: \"shark,5,true\"");
    JLabel label8 = new JLabel("\"autostart\": uses lobsters,5,true");
    JCheckBox potUpCheckbox = new JCheckBox("Use regular Atk/Str Pots?", true);
    JLabel foodWithdrawAmountLabel = new JLabel("Food Withdraw amount:");
    JTextField foodWithdrawAmountField = new JTextField(String.valueOf(6));
    JLabel foodLabel = new JLabel("Type of Food:");
    JComboBox<String> foodField = new JComboBox<>(foodTypes);
    JLabel fightModeLabel = new JLabel("Fight Mode:");
    JComboBox<String> fightModeField =
        new JComboBox<>(new String[] {"Controlled", "Aggressive", "Accurate", "Defensive"});
    JLabel blankLabel = new JLabel("          ");
    JButton startScriptButton = new JButton("Start");

    startScriptButton.addActionListener(
        e -> {
          foodId = foodIds[foodField.getSelectedIndex()];
          if (!foodWithdrawAmountField.getText().equals("")) {
            foodWithdrawAmount = Integer.parseInt(foodWithdrawAmountField.getText());
          } else if (foodWithdrawAmountField.getText().equals("")) {
            foodWithdrawAmount = 1;
          }
          potUp = potUpCheckbox.isSelected();
          scriptFrame.setVisible(false);
          scriptFrame.dispose();
          scriptStarted = true;
        });

    scriptFrame = new JFrame(c.getPlayerName() + " - options");

    scriptFrame.setLayout(new GridLayout(0, 1));
    scriptFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    scriptFrame.add(header);
    scriptFrame.add(label1);
    scriptFrame.add(label2);
    scriptFrame.add(spacer);
    scriptFrame.add(label3);
    scriptFrame.add(label4);
    scriptFrame.add(spacer);
    scriptFrame.add(label5);
    scriptFrame.add(label6);
    scriptFrame.add(label7);
    scriptFrame.add(label8);
    scriptFrame.add(spacer);
    scriptFrame.add(potUpCheckbox);
    scriptFrame.add(foodWithdrawAmountLabel);
    scriptFrame.add(foodWithdrawAmountField);
    scriptFrame.add(foodLabel);
    scriptFrame.add(foodField);
    scriptFrame.add(fightModeLabel);
    scriptFrame.add(fightModeField);
    scriptFrame.add(blankLabel);
    scriptFrame.add(startScriptButton);

    scriptFrame.pack();
    scriptFrame.setLocationRelativeTo(null);
    scriptFrame.setVisible(true);
    scriptFrame.requestFocusInWindow();
  }

  @Override
  public void chatCommandInterrupt(String commandText) {
    if (commandText.contains("bank")) {
      c.displayMessage("@red@Got banking command! Going to the Bank!");
      timeToBank = true;
      c.sleep(100);
    } else if (commandText.contains(
        "attack")) { // field is "Controlled", "Aggressive", "Accurate", "Defensive"}
      c.displayMessage("@red@Got Combat Style Command! - Attack Xp");
      c.displayMessage("@red@Switching to \"Accurate\" combat style!");
      fightMode = 2;
      c.sleep(100);
    } else if (commandText.contains("strength")) {
      c.displayMessage("@red@Got Combat Style Command! - Strength Xp");
      c.displayMessage("@red@Switching to \"Aggressive\" combat style!");
      fightMode = 1;
      c.sleep(100);
    } else if (commandText.contains("defense")) {
      c.displayMessage("@red@Got Combat Style Command! - Defense Xp");
      c.displayMessage("@red@Switching to \"Defensive\" combat style!");
      fightMode = 3;
      c.sleep(100);
    } else if (commandText.contains("controlled")) {
      c.displayMessage("@red@Got Combat Style Command! - Controlled Xp");
      c.displayMessage("@red@Switching to \"Controlled\" combat style!");
      fightMode = 0;
      c.sleep(100);
    }
  }

  @Override
  public void paintInterrupt() {
    if (c != null) {
      String runTime = controller.msToString(System.currentTimeMillis() - startTime);
      int guamSuccessPerHr = 0;
      int marSuccessPerHr = 0;
      int tarSuccessPerHr = 0;
      int harSuccessPerHr = 0;
      int ranSuccessPerHr = 0;
      int iritSuccessPerHr = 0;
      int avaSuccessPerHr = 0;
      int kwuSuccessPerHr = 0;
      int cadaSuccessPerHr = 0;
      int dwarSuccessPerHr = 0;
      int lawSuccessPerHr = 0;
      int natSuccessPerHr = 0;
      int deathSuccessPerHr = 0;
      int bloodSuccessPerHr = 0;
      int GemsSuccessPerHr = 0;
      int TripSuccessPerHr = 0;
      long currentTimeInSeconds = System.currentTimeMillis() / 1000L;

      try {
        float timeRan = currentTimeInSeconds - startTimestamp;
        float scale = (60 * 60) / timeRan;
        guamSuccessPerHr = (int) (totalGuam * scale);
        marSuccessPerHr = (int) (totalMar * scale);
        tarSuccessPerHr = (int) (totalTar * scale);
        harSuccessPerHr = (int) (totalHar * scale);
        ranSuccessPerHr = (int) (totalRan * scale);
        iritSuccessPerHr = (int) (totalIrit * scale);
        avaSuccessPerHr = (int) (totalAva * scale);
        kwuSuccessPerHr = (int) (totalKwuarm * scale);
        cadaSuccessPerHr = (int) (totalCada * scale);
        dwarSuccessPerHr = (int) (totalDwarf * scale);
        lawSuccessPerHr = (int) (totalLaw * scale);
        natSuccessPerHr = (int) (totalNat * scale);
        deathSuccessPerHr = (int) (totalDeath * scale);
        bloodSuccessPerHr = (int) (totalBlood * scale);
        GemsSuccessPerHr = (int) (totalGems * scale);
        TripSuccessPerHr = (int) (totalTrips * scale);

      } catch (Exception e) {
        // divide by zero
      }

      c.drawString("@red@Asgarnian Ice Slayer @mag@~ by Kaila", 330, 48, 0xFFFFFF, 1);
      c.drawString(
          "@whi@Guams: @gre@"
              + totalGuam
              + "@yel@ (@whi@"
              + String.format("%,d", guamSuccessPerHr)
              + "@yel@/@whi@hr@yel@)",
          350,
          62,
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@Marrentills: @gre@"
              + totalMar
              + "@yel@ (@whi@"
              + String.format("%,d", marSuccessPerHr)
              + "@yel@/@whi@hr@yel@)",
          350,
          76,
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@Tarromins: @gre@"
              + totalTar
              + "@yel@ (@whi@"
              + String.format("%,d", tarSuccessPerHr)
              + "@yel@/@whi@hr@yel@)",
          350,
          90,
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@Harralanders: @gre@"
              + totalHar
              + "@yel@ (@whi@"
              + String.format("%,d", harSuccessPerHr)
              + "@yel@/@whi@hr@yel@)",
          350,
          104,
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@Ranarrs: @gre@"
              + totalRan
              + "@yel@ (@whi@"
              + String.format("%,d", ranSuccessPerHr)
              + "@yel@/@whi@hr@yel@)",
          350,
          118,
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@Irit Herbs: @gre@"
              + totalIrit
              + "@yel@ (@whi@"
              + String.format("%,d", iritSuccessPerHr)
              + "@yel@/@whi@hr@yel@)",
          350,
          132,
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@Avantoes: @gre@"
              + totalAva
              + "@yel@ (@whi@"
              + String.format("%,d", avaSuccessPerHr)
              + "@yel@/@whi@hr@yel@)",
          350,
          146,
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@Kwuarms: @gre@"
              + totalKwuarm
              + "@yel@ (@whi@"
              + String.format("%,d", kwuSuccessPerHr)
              + "@yel@/@whi@hr@yel@)",
          350,
          160,
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@Cadantines: @gre@"
              + totalCada
              + "@yel@ (@whi@"
              + String.format("%,d", cadaSuccessPerHr)
              + "@yel@/@whi@hr@yel@)",
          350,
          174,
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@Dwarfs: @gre@"
              + totalDwarf
              + "@yel@ (@whi@"
              + String.format("%,d", dwarSuccessPerHr)
              + "@yel@/@whi@hr@yel@)",
          350,
          188,
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@Laws: @gre@"
              + totalLaw
              + "@yel@ (@whi@"
              + String.format("%,d", lawSuccessPerHr)
              + "@yel@/@whi@hr@yel@)",
          350,
          202,
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@Nats: @gre@"
              + totalNat
              + "@yel@ (@whi@"
              + String.format("%,d", natSuccessPerHr)
              + "@yel@/@whi@hr@yel@)",
          350,
          216,
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@Deaths: @gre@"
              + totalDeath
              + "@yel@ (@whi@"
              + String.format("%,d", deathSuccessPerHr)
              + "@yel@/@whi@hr@yel@)",
          350,
          230,
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@Bloods: @gre@"
              + totalBlood
              + "@yel@ (@whi@"
              + String.format("%,d", bloodSuccessPerHr)
              + "@yel@/@whi@hr@yel@)",
          350,
          244,
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@Total Gems: @gre@"
              + totalGems
              + "@yel@ (@whi@"
              + String.format("%,d", GemsSuccessPerHr)
              + "@yel@/@whi@hr@yel@)",
          350,
          258,
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@Tooth: @gre@" + totalTooth + "@yel@ / @whi@Loop: @gre@" + totalLoop,
          350,
          272,
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@R.Spear: @gre@" + totalSpear + "@yel@ / @whi@Shield Half: @gre@" + totalLeft,
          350,
          286,
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@Total Trips: @gre@"
              + totalTrips
              + "@yel@ (@whi@"
              + String.format("%,d", TripSuccessPerHr)
              + "@yel@/@whi@hr@yel@)",
          350,
          300,
          0xFFFFFF,
          1);
      c.drawString("@whi@Runtime: " + runTime, 350, 314, 0xFFFFFF, 1);
    }
  }
}