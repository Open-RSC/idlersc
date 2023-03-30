package scripting.idlescript;

import bot.Main;
import controller.Controller;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import orsc.ORSCharacter;

/**
 * Attack Cape Buyer - By Kaila.
 *
 * <p>
 *
 * <p>Talks to Rovin for capes and Banks.
 *
 * <p>Start by Rovin or varrock west!
 *
 * <p>Need coins in the inventory to buy.
 *
 * <p>@Author - Kaila.
 */
public class K_AttackCapeBuyer extends IdleScript {
  private static final Controller c = Main.getController();
  private static JFrame scriptFrame = null;
  private static boolean guiSetup = false;
  private static boolean scriptStarted = false;
  private static long startTime;
  private static final long startTimestamp = System.currentTimeMillis() / 1000L;
  private static int totalTopz = 0;
  private static int totalTrips = 0;
  private static int TopzInBank = 0;

  public int start(String[] parameters) {
    if (scriptStarted) {
      c.displayMessage("@red@Attack Cape Buyer - By Kaila");
      c.displayMessage("@red@Start by Rovin or varrock west!");
      c.displayMessage("@red@Need coins in the inventory to buy");
      if (c.isInBank()) {
        c.closeBank();
      }
      if (c.currentY() < 1000) {
        bank();
        BankToGrape();
        c.sleep(1380);
      }
      scriptStart();
    }
    if (!guiSetup) {
      setupGUI();
      guiSetup = true;
    }
    return 1000; // start() must return an int value now.
  }

  private void scriptStart() {
    while (c.isRunning()) {

      if (c.getInventoryItemCount() == 30) {
        c.setStatus("@red@Banking..");
        GrapeToBank();
        bank();
        BankToGrape();
        c.sleep(618);
      }
      ORSCharacter npc = c.getNearestNpcById(18, true);

      if (npc != null) {
        c.setStatus("@red@Getting cape from Rovin..");
        c.talkToNpc(npc.serverIndex);
        c.sleep(6000);

        if (!c.isInOptionMenu()) continue;

        c.optionAnswer(2);
        c.sleep(9000);
        c.optionAnswer(0);
        c.sleep(12000);
      }
    }
  }

  private void bank() {

    c.setStatus("@yel@Banking..");
    c.openBank();
    c.sleep(640);

    if (c.isInBank()) {

      totalTopz = totalTopz + c.getInventoryItemCount(1374);

      if (c.getInventoryItemCount(1374) > 0) { // robe top
        c.depositItem(1374, c.getInventoryItemCount(1374));
        c.sleep(1380);
      }

      TopzInBank = c.getBankItemCount(1374);

      c.closeBank();
      c.sleep(640);
    }
  }

  private void GrapeToBank() { // replace

    c.setStatus("@gre@Walking to Bank..");
    c.walkTo(141, 1398);
    c.sleep(340);
    c.atObject(142, 1398); // down ladder
    c.sleep(800);
    c.walkTo(135, 460);
    c.walkTo(135, 470);
    c.walkTo(132, 474);
    c.walkTo(132, 484);
    c.walkTo(132, 494);
    c.walkTo(132, 502);
    c.walkTo(137, 507);
    c.walkTo(150, 507);
    totalTrips = totalTrips + 1;
    c.setStatus("@gre@Done Walking..");
  }

  private void BankToGrape() {

    c.setStatus("@gre@Walking to Rovin..");
    c.walkTo(150, 507);
    c.walkTo(137, 507);
    c.walkTo(132, 502);
    c.walkTo(132, 494);
    c.walkTo(132, 484);
    c.walkTo(132, 474);
    c.walkTo(135, 470);
    c.walkTo(135, 460);
    c.walkTo(141, 454);
    c.atObject(142, 454); // up ladder
    c.sleep(800);
    // next to rovin now
    c.setStatus("@gre@Done Walking..");
  }

  // GUI stuff below
  private void setupGUI() {
    JLabel header = new JLabel("Attack Cape Buyer ~ By Kaila");
    JLabel label1 = new JLabel("Talks to Rovin for capes & Banks");
    JLabel label2 = new JLabel("Start by Rovin or varrock west!");
    JLabel label3 = new JLabel("Need coins in the inventory to buy");
    JButton startScriptButton = new JButton("Start");

    startScriptButton.addActionListener(
        e -> {
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
    scriptFrame.add(label3);
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
      int TopzSuccessPerHr = 0;
      int TripSuccessPerHr = 0;
      long currentTimeInSeconds = System.currentTimeMillis() / 1000L;

      try {
        float timeRan = currentTimeInSeconds - startTimestamp;
        float scale = (60 * 60) / timeRan;
        TopzSuccessPerHr = (int) (totalTopz * scale);
        TripSuccessPerHr = (int) (totalTrips * scale);

      } catch (Exception e) {
        // divide by zero
      }
      int x = 6;
      int y = 21;
      c.drawString("@red@Attack Cape Buyer @mag@~ by Kaila", x, y - 3, 0xFFFFFF, 1);
      c.drawString("@whi@____________________", x, y, 0xFFFFFF, 1);
      c.drawString("@whi@Capes in Bank: @gre@" + TopzInBank, x, y + 14, 0xFFFFFF, 1);
      c.drawString(
          "@whi@Coins Spent: @gre@" + (totalTopz * 99) + " @whi@K", x, y + (14 * 2), 0xFFFFFF, 1);
      c.drawString(
          "@whi@Capes Bought: @gre@"
              + totalTopz
              + "@yel@ (@whi@"
              + String.format("%,d", TopzSuccessPerHr)
              + "@yel@/@whi@hr@yel@)",
          x,
          y + (14 * 3),
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@Total Trips: @gre@"
              + totalTrips
              + "@yel@ (@whi@"
              + String.format("%,d", TripSuccessPerHr)
              + "@yel@/@whi@hr@yel@)",
          x,
          y + (14 * 4),
          0xFFFFFF,
          1);
      c.drawString("@whi@Runtime: " + runTime, x, y + (14 * 5), 0xFFFFFF, 1);
      c.drawString("@whi@____________________", x, y + 3 + (14 * 5), 0xFFFFFF, 1);
    }
  }
}
