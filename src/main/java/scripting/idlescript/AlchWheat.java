package scripting.idlescript;

/**
 * High/Low alches wheat anywhere there's wheat.
 *
 * @author Dvorak
 */
public class AlchWheat extends IdleScript {

  final long startTimestamp = System.currentTimeMillis() / 1000L;
  int success = 0;

  public int start(String[] parameters) {
    controller.displayMessage("@red@Start near wheat with fires and nats!");
    while (controller.isRunning()) {
      controller.sleepHandler(98, true);
      if (controller.getInventoryItemCount(29) > 0) {
        controller.setStatus("@gre@Alchin'!");
        if (controller.getCurrentStat(controller.getStatId("Magic")) >= 55)
          controller.castSpellOnInventoryItem(
              controller.getSpellIdFromName("High level alchemy"),
              controller.getInventoryItemSlotIndex(29));
        else
          controller.castSpellOnInventoryItem(
              controller.getSpellIdFromName("Low level alchemy"),
              controller.getInventoryItemSlotIndex(29));

        controller.sleep(1300);
      } else {
        controller.setStatus("@gre@Pickin' wheat!");
        int[] coords = controller.getNearestObjectById(72);
        if (coords != null) {
          controller.atObject2(coords[0], coords[1]);
        } else {
          controller.displayMessage("@red@No wheat!");
        }

        controller.sleep(618);
      }
    }

    return 1000; // start() must return a int value now.
  }

  @Override
  public void questMessageInterrupt(String message) {
    if (message.contains("successful") || message.contains("make a")) success++;
  }

  @Override
  public void paintInterrupt() {
    if (controller != null) {

      int successPerHr = 0;
      long currentTimeInSeconds = System.currentTimeMillis() / 1000L;
      try {
        float timeRan = currentTimeInSeconds - startTimestamp;
        float scale = (60 * 60) / timeRan;
        successPerHr = (int) (success * scale);
      } catch (Exception e) {
        // divide by zero
      }

      controller.drawBoxAlpha(7, 7, 160, 21 + 14, 0x00FF00, 128);
      controller.drawString("@whi@AlchWheat @gre@by @whi@Dvorak", 10, 21, 0xFFFFFF, 1);
      controller.drawString(
          "@whi@Alches: @gre@"
              + String.format("%,d", success)
              + " @whi@(@gre@"
              + String.format("%,d", successPerHr)
              + "@whi@/@gre@hr@whi@)",
          10,
          21 + 14,
          0xFFFFFF,
          1);
    }
  }
}
