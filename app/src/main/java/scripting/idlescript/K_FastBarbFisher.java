package scripting.idlescript;

import bot.Main;
import controller.Controller;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Power fishes trout/salmon in barb village using Batching.
 *
 * <p>
 *
 * <p>Batch bars MUST be toggles on to function properly. Bot will Autotoggle them On.
 *
 * <p>@Author - Kaila.
 */
public class K_FastBarbFisher extends IdleScript {
  private static final Controller c = Main.getController();
  private static JFrame scriptFrame = null;
  private static boolean guiSetup = false;
  private static boolean scriptStarted = false;
  private static int troutSuccess = 0;
  private static int salmonSuccess = 0;
  private static int failure = 0;
  private static int invFeathers = 0;
  private static long next_attempt = -1;
  private static long startTime;
  private static final long nineMinutesInMillis = 540000L;
  private static final long startTimestamp = System.currentTimeMillis() / 1000L;

  public int start(String[] parameters) {
    if (!parameters[0].equals("")) {
      if (parameters[0].toLowerCase().startsWith("auto")) {
        c.log("Got Autostart, Fishing", "@red@");
        scriptStarted = true;
      }
    }
    if (scriptStarted) {
      startTime = System.currentTimeMillis();
      next_attempt = System.currentTimeMillis() + 5000L;
      c.displayMessage("@red@Power fishes trout/salmon in barb village using Batching");

      c.quitIfAuthentic();
      if (!c.isAuthentic() && !orsc.Config.C_BATCH_PROGRESS_BAR) c.toggleBatchBars();
      if (c.isInBank()) c.closeBank();
      if (c.currentX() < 195) {
        bank();
        bankToFish();
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
      int[] spot = c.getNearestObjectById(192);
      c.walkTo(spot[0] + 1, spot[1]);
      c.sleep(1240);
      c.atObject(spot[0], spot[1]);
      c.sleep(2000);
      waitForBatchingScript();
      if (System.currentTimeMillis() > next_attempt) {
        c.log("@red@Walking to Avoid Logging!");
        c.walkTo(c.currentX() + 1, c.currentY(), 0, true);
        c.sleep(640);
        next_attempt = System.currentTimeMillis() + nineMinutesInMillis;
        long nextAttemptInSeconds = (next_attempt - System.currentTimeMillis()) / 1000L;
        c.log("Done Walking to not Log, Next attempt in " + nextAttemptInSeconds + " seconds!");
      }
      if (c.getInventoryItemCount(381) == 0 || c.getInventoryItemCount(378) == 0) {
        fishToBank();
        bank();
        bankToFish();
      }
    }
  }

  private void waitForBatchingScript() {
    while (c.isBatching() && System.currentTimeMillis() < next_attempt) {
      c.sleep(1000);
    }
  }

  private void bank() {

    c.setStatus("@yel@Banking..");
    c.openBank();
    c.sleep(800);

    if (c.isInBank()) {
      int featherId = 381;
      int rodId = 378;

      for (int itemId : c.getInventoryItemIds()) {
        c.depositItem(itemId, c.getInventoryItemCount(itemId));
        c.sleep(100);
      }
      c.sleep(1280); // Important, leave in
      if (c.getInventoryItemCount(rodId) < 1) { // withdraw 1 fly fish rod if needed
        c.withdrawItem(rodId, 1);
        c.sleep(640);
      }
      if (c.getInventoryItemCount(featherId) < 1000000) { // withdraw 1m feathers if needed
        if (c.getBankItemCount(featherId) > 1000000) {
          c.withdrawItem(featherId, 1000000);
          c.sleep(640);
        } else if (c.getBankItemCount(featherId) < 1000000) {
          c.withdrawItem(featherId, c.getBankItemCount(featherId) - 1);
          c.sleep(640);
        }
      }
      if (c.getBankItemCount(featherId) == 0
          || c.getBankItemCount(rodId) == 0) { // no feathers or fly rod
        c.setStatus("@red@NO Feathers/Fly Fishing Rod in the bank, Logging Out!.");
        c.log("@red@NO Feathers/Fly Fishing Rod in the bank, Logging Out!.");
        endSession();
      }
      c.closeBank();
      c.sleep(1320);
    }
  }

  private void endSession() {
    c.setAutoLogin(false);
    c.logout();
    if (!c.isLoggedIn()) {
      c.stop();
    }
  }

  private void bankToFish() {
    c.walkTo(151, 507);
    c.walkTo(161, 507);
    c.walkTo(171, 507);
    c.walkTo(179, 515);
    c.walkTo(189, 515);
    c.walkTo(199, 515);
    c.walkTo(202, 512);
    c.walkTo(212, 512);
    c.walkTo(216, 512);
    c.walkTo(216, 510);
    c.walkTo(213, 507);
  }

  private void fishToBank() {
    c.walkTo(216, 510);
    c.walkTo(216, 512);
    c.walkTo(212, 512);
    c.walkTo(202, 512);
    c.walkTo(199, 515);
    c.walkTo(189, 515);
    c.walkTo(179, 515);
    c.walkTo(171, 507);
    c.walkTo(161, 507);
    c.walkTo(151, 507);
  }
  // GUI stuff below (icky)
  private void setupGUI() {
    JLabel header = new JLabel("Fast Barb Fisher - Kaila");
    JLabel label5 = new JLabel("* REQUIRES Batch Bars, bot will autotoggle them!");
    JLabel label1 = new JLabel("* Start near Barb Fish spots or Var West!");
    JLabel label2 = new JLabel("* Have Feathers and Fly Fishing Rod");
    JLabel label3 = new JLabel("* Uses Batching Bars to get better xp Rates");
    JLabel label4 = new JLabel("* Only works on Coleslaw currently!");
    JButton startScriptButton = new JButton("Start");

    startScriptButton.addActionListener(
        e -> {
          scriptFrame.setVisible(false);
          scriptFrame.dispose();
          scriptStarted = true;
        });

    scriptFrame = new JFrame(c.getPlayerName() + " - options");

    scriptFrame.setLayout(new GridLayout(0, 1));
    scriptFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    scriptFrame.add(header);
    scriptFrame.add(label5);
    scriptFrame.add(label1);
    scriptFrame.add(label2);
    scriptFrame.add(label3);
    scriptFrame.add(label4);
    scriptFrame.add(label4);
    scriptFrame.add(startScriptButton);
    scriptFrame.pack();
    scriptFrame.setLocationRelativeTo(null);
    scriptFrame.setVisible(true);
    scriptFrame.requestFocusInWindow();
  }

  @Override
  public void questMessageInterrupt(String message) {
    if (message.contains("You catch a trout")) {
      troutSuccess++;
      invFeathers = c.getInventoryItemCount(381);
    } else if (message.contains("You catch a salmon")) {
      salmonSuccess++;
      invFeathers = c.getInventoryItemCount(381);
    } else if (message.contains("You fail")) {
      failure++;
      invFeathers = c.getInventoryItemCount(381);
    }
  }

  @Override
  public void paintInterrupt() {
    if (c != null) {
      String runTime = c.msToString(System.currentTimeMillis() - startTime);
      int troutSuccessPerHr = 0;
      int salmonSuccessPerHr = 0;
      int failurePerHr = 0;
      long currentTimeInSeconds = System.currentTimeMillis() / 1000L;

      try {
        float timeRan = currentTimeInSeconds - startTimestamp;
        float scale = (60 * 60) / timeRan;
        troutSuccessPerHr = (int) (troutSuccess * scale);
        salmonSuccessPerHr = (int) (salmonSuccess * scale);
        failurePerHr = (int) (failure * scale);
      } catch (Exception e) {
        // divide by zero
      }
      int x = 6;
      int y = 15;
      int totalFish = troutSuccess + salmonSuccess;
      c.drawString("@red@Fast Barb Fisher @mag@~ by Kaila", x, y - 3, 0xFFFFFF, 1);
      c.drawString("@whi@________________________", x, y, 0xFFFFFF, 1);
      c.drawString(
          "@whi@Trout Caught: @gre@"
              + troutSuccess
              + "@yel@ (@whi@"
              + String.format("%,d", troutSuccessPerHr)
              + "@yel@/@whi@hr@yel@)",
          x,
          y + 14,
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@Salmon Caught: @gre@"
              + salmonSuccess
              + "@yel@ (@whi@"
              + salmonSuccessPerHr
              + "@yel@/@whi@hr@yel@)",
          x,
          y + (14 * 2),
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@Failure to Catch: @gre@"
              + failure
              + "@yel@ (@whi@"
              + failurePerHr
              + "@yel@/@whi@hr@yel@)",
          x,
          y + (14 * 3),
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@Feathers Used: @gre@"
              + totalFish
              + "@yel@ (@whi@"
              + String.format("%,d", (troutSuccessPerHr + salmonSuccessPerHr))
              + "@yel@/@whi@hr@yel@)",
          x,
          y + (14 * 4),
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@Time Remaining: "
              + c.shortTimeToCompletion(totalFish, invFeathers, startTime)
              + " hours",
          x,
          y + (14 * 5),
          0xFFFFFF,
          1);
      long timeRemainingTillAutoWalkAttempt = next_attempt - System.currentTimeMillis();
      c.drawString(
          "@whi@Time till AutoWalk: " + c.msToShortString(timeRemainingTillAutoWalkAttempt),
          x,
          y + (14 * 6),
          0xFFFFFF,
          1);
      c.drawString("@whi@Runtime: " + runTime, x, y + (14 * 7), 0xFFFFFF, 1);
      c.drawString("@whi@__________________", x, y + 3 + (14 * 7), 0xFFFFFF, 1);
    }
  }
}