package scripting.idlescript;

import models.entities.ItemId;
import models.entities.NpcId;
import models.entities.QuestId;

/*
 * TODO:
 * Make sure atObjectSequence works correctly
 * Make sure the player correctly picks up the milled flour
 * Return to cook and hand in items
 */

public final class QH_CooksAssistant extends QH__QuestHandler {

  // COORDINATES FOR walkPath() PATHS AND pickupTableItem()
  private static final Integer[] WHEAT_FIELD_OUTER = {172, 607};
  private static final Integer[] WHEAT_FIELD_INNER = {172, 606};
  private static final Integer[] COURTYARD_OUTER = {128, 658};
  private static final Integer[] COURTYARD_INNER = {129, 658};
  private static final Integer[] GENERAL_STORE = {130, 641};
  private static final Integer[] CHICKEN_OUTER = {114, 609};
  private static final Integer[] CHICKEN_INNER = {115, 609};
  private static final Integer[] CABBAGE_FIELD = {151, 614};
  private static final Integer[] BRIDGE_NORTH = {125, 625};
  private static final Integer[] BRIDGE_SOUTH = {106, 656};
  private static final Integer[] GOBLIN_ROAD = {106, 625};
  private static final Integer[] MILL_FLOUR = {166, 598};
  private static final Integer[] MILL_INNER = {166, 603};
  private static final Integer[] MILL_OUTER = {166, 604};
  private static final Integer[] COW_OUTER = {105, 619};
  private static final Integer[] COW_INNER = {104, 619};
  private static final Integer[] KITCHEN = {133, 660};
  private static final Integer[] SHEEP = {135, 625};

  // PATHS FOR walkPath()
  private static final Integer[][] MILL_TO_KITCHEN = {
    MILL_INNER, MILL_OUTER, CABBAGE_FIELD, SHEEP, GENERAL_STORE, COURTYARD_OUTER, KITCHEN
  };
  private static final Integer[][] COWS_TO_MILL = {
    COW_INNER, BRIDGE_NORTH, SHEEP, CABBAGE_FIELD, MILL_OUTER
  };
  private static final Integer[][] MILL_TO_WHEAT_FIELD = {WHEAT_FIELD_OUTER, WHEAT_FIELD_INNER};
  private static final Integer[][] KITCHEN_TO_COURTYARD = {COURTYARD_INNER, COURTYARD_OUTER};
  private static final Integer[][] CHICKENS_TO_COWS = {CHICKEN_INNER, COW_OUTER, COW_INNER};
  private static final Integer[][] WHEAT_FIELD_TO_MILL = {WHEAT_FIELD_INNER, MILL_OUTER};
  private static final Integer[][] PENS_TO_CHICKENS = {CHICKEN_OUTER, CHICKEN_INNER};
  private static final Integer[][] COURTYARD_TO_KITCHEN = {COURTYARD_OUTER, KITCHEN};
  private static final Integer[][] COURTYARD_TO_PENS = {BRIDGE_SOUTH, GOBLIN_ROAD};
  private static final Integer[][] PENS_TO_COWS = {COW_OUTER, COW_INNER};
  private static final Integer[][] INTO_MILL = {MILL_OUTER, MILL_INNER};

  // OBJECT COORDINATES
  private static final Integer[] MILL_LADDER_DOWN_SECOND_FLOOR = {165, 1542};
  private static final Integer[] MILL_LADDER_DOWN_THIRD_FLOOR = {166, 2490};
  private static final Integer[] MILL_LADDER_UP_SECOND_FLOOR = {166, 1546};
  private static final Integer[] MILL_LADDER_UP_GROUND_FLOOR = {165, 598};
  private static final Integer[] MILL_HOPPER = {166, 2487};
  private static final Integer[] WHEAT_PLANT = {172, 605};

  // ITEM REQUIREMENTS IDS
  private static final Integer POT_OF_FLOUR_ID = ItemId.POT_OF_FLOUR.getId();
  private static final Integer BUCKET_ID = ItemId.BUCKET.getId();
  private static final Integer GRAIN_ID = ItemId.GRAIN.getId();
  private static final Integer FLOUR_ID = ItemId.FLOUR.getId();
  private static final Integer MILK_ID = ItemId.MILK.getId();
  private static final Integer EGG_ID = ItemId.EGG.getId();
  private static final Integer POT_ID = ItemId.POT.getId();

  // NPC IDS
  private static final int COW_ID = NpcId.COW_ATTACKABLE.getId();
  private static final int COOK_ID = NpcId.COOK.getId();

  public int start(String[] param) {
    QUEST_NAME = "Cook's Assistant";
    CURRENT_QUEST_STEP = "Starting " + QUEST_NAME;
    START_RECTANGLE = LUMBRIDGE_CASTLE_COURTYARD;
    SKILL_REQUIREMENTS = new String[][] {};
    QUEST_REQUIREMENTS = new String[] {};
    QUEST_ID = QuestId.getByName(QUEST_NAME).getId();
    QUEST_STAGE = c.getQuestStage(QUEST_ID);
    doQuestChecks();

    while (c.isRunning()) {
      QUEST_STAGE = c.getQuestStage(QUEST_ID);
      switch (QUEST_STAGE) {
        case 0:
          // Gather pot and bucket if needed
          while ((!hasAtLeastItemAmount(POT_ID, 1) && !hasAtLeastItemAmount(POT_OF_FLOUR_ID, 1))
              || (!hasAtLeastItemAmount(BUCKET_ID, 1) && !hasAtLeastItemAmount(MILK_ID, 1))
                  && c.isRunning()) {
            STEP_ITEMS =
                new Integer[][] {
                  {POT_ID, 1},
                  {BUCKET_ID, 1}
                };
            if (!hasAtLeastItemAmount(POT_ID, 1) && !hasAtLeastItemAmount(POT_OF_FLOUR_ID, 1)) {
              CURRENT_QUEST_STEP = "Getting a pot";
              walkPath(COURTYARD_TO_KITCHEN);
              pickupUnreachableItem(POT_ID, KITCHEN);
              walkPath(KITCHEN_TO_COURTYARD);
            }
            if (!hasAtLeastItemAmount(BUCKET_ID, 1) && !hasAtLeastItemAmount(MILK_ID, 1)) {
              CURRENT_QUEST_STEP = "Getting a bucket";
              walkPath(COURTYARD_TO_PENS);
              walkPath(PENS_TO_CHICKENS);
              pickupGroundItem(BUCKET_ID);
              c.sleep(1280);
            }
          }
          // Gather egg, milk, and flour
          while (!hasAtLeastItemAmount(EGG_ID, 1)
              || !hasAtLeastItemAmount(MILK_ID, 1)
              || !hasAtLeastItemAmount(POT_OF_FLOUR_ID, 1) && c.isRunning()) {
            STEP_ITEMS =
                new Integer[][] {
                  {EGG_ID, 1},
                  {MILK_ID, 1},
                  {POT_OF_FLOUR_ID, 1}
                };
            CURRENT_QUEST_STEP = "Getting ingredients";
            while (!hasAtLeastItemAmount(EGG_ID, 1) && c.isRunning()) {
              CURRENT_QUEST_STEP = "Getting an egg";
              if (isInRectangle(START_RECTANGLE)) {
                walkPath(COURTYARD_TO_PENS);
                walkPath(PENS_TO_CHICKENS);
              }
              pickupGroundItem(EGG_ID);
            }
            while (!hasAtLeastItemAmount(MILK_ID, 1) && c.isRunning()) {
              CURRENT_QUEST_STEP = "Getting milk";
              if (isInRectangle(START_RECTANGLE)) {
                walkPath(COURTYARD_TO_PENS);
                walkPath(PENS_TO_COWS);
              } else {
                walkPath(CHICKENS_TO_COWS);
              }
              useItemOnNearestNpcId(COW_ID, BUCKET_ID);
            }
            while (!hasAtLeastItemAmount(POT_OF_FLOUR_ID, 1) && c.isRunning()) {
              CURRENT_QUEST_STEP = "Getting flour";
              walkPath(COWS_TO_MILL);
              if (!hasAtLeastItemAmount(GRAIN_ID, 1)) {
                walkPath(MILL_TO_WHEAT_FIELD);
                c.atObject2(WHEAT_PLANT[0], WHEAT_PLANT[1]);
                while (!hasAtLeastItemAmount(GRAIN_ID, 1)) c.sleep(640);
                c.walkTo(c.currentX(), c.currentY());
                dropAllButOne(GRAIN_ID);
                walkPath(WHEAT_FIELD_TO_MILL);
              }
              walkPath(INTO_MILL);
              Integer[][] objectSeq = {
                MILL_LADDER_UP_GROUND_FLOOR, MILL_LADDER_UP_SECOND_FLOOR,
              };
              atObjectSequence(objectSeq);
              c.useItemIdOnObject(MILL_HOPPER[0], MILL_HOPPER[1], GRAIN_ID);
              c.sleep(640);
              objectSeq =
                  new Integer[][] {
                    MILL_HOPPER, MILL_LADDER_DOWN_THIRD_FLOOR, MILL_LADDER_DOWN_SECOND_FLOOR
                  };
              atObjectSequence(objectSeq);
              pickupUnreachableItem(FLOUR_ID, MILL_FLOUR);
              c.sleep(1280);
            }
          }
          CURRENT_QUEST_STEP = "Returning to the Cook";
          walkPath(MILL_TO_KITCHEN);
          DIALOG_CHOICES = new String[] {"What's wrong?", "Yes, I'll help you"};
          followNPCDialog(COOK_ID, DIALOG_CHOICES);
          break;
        case 1:
          CURRENT_QUEST_STEP = "Handing in items to the Cook";
          DIALOG_CHOICES = new String[] {};
          followNPCDialog(COOK_ID, DIALOG_CHOICES);
          // TALK TO COOK
          // GIVE ITEMS
          // QUEST COMPLETE
          break;
        case -1:
          quit("Quest Completed");
        default:
          quit("");
      }
    }
    quit("Script stopped");
    return 1000;
  }
}
