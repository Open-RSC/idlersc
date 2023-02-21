package scripting.idlescript;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JButton;
//import javax.swing.JCheckBox;
//import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import orsc.ORSCharacter;
//import scripting.idlescript.AIOCooker.FoodObject;

/**
 * Wildy Fire Giant Killer - By Kaila");
 * Start in Edge bank with Armor");
 * Sharks/Laws/Airs/Earths IN BANK REQUIRED");
 * 31 Magic Required for escape tele");
 * 
 * Author - Kaila
 */
public class K_TavBlueDragonPipe extends IdleScript {	
	
	JFrame scriptFrame = null;
	boolean guiSetup = false;
	boolean scriptStarted = false;
	
	int totalDbones = 0;
    int totalRdagger = 0;
    int totalGems = 0;
    int totalLaw = 0;
    int totalNat = 0;
    int totalFire = 0;
    int totalWater = 0;
    int totalAddy = 0;
    int totalLoop = 0;
    int totalTooth = 0;
    int totalLeft = 0;
    int totalSpear = 0;  
    int totalHerb = 0;
    int bankDbones = 0;
    int totalTrips = 0;
	int foodWithdrawAmount = 20;
    
    
   // DRAGON_BONES, 396, 93, LAW_RUNE, 40, WATER_RUNE, 31, 526, 527, 1277

   	int[] attackPot = {488,487,486};
   	int[] strPot = {494,493,492};
	int[] loot = {  814,     //D Bones
					396,     //rune dagger
					40,	 	 // nature rune
					42, 	 // law rune
					32,      //water rune
					31, 	 //fire rune
					526, 	 //tooth half
					527, 	 //loop half
					1277, 	 //shield (left) half
					438, 	 //Grimy ranarr
					439,  	 //Grimy irit
					440,  	 //Grimy ava
					441,	 //Grimy kwu
					442, 	 //Grimy cada
					443, 	 //Grimy dwu
					154,     //Addy Ore
					160, 	 //saph
					159, 	 //emerald
					158, 	 //ruby
					157,	 //diamond
					1092, 	 //rune spear
					795  	//D med
					};

    public boolean isWithinLootzone(int x, int y) { 
    	return controller.distance(361, 3353, x, y) <= 15; //center of lootzone
    }
    //FURTHEST LOOT is 376, 3368, go 361, 3353  (15 tiles)
	
	long startTime;
	long startTimestamp = System.currentTimeMillis() / 1000L;
	
	
	public int start(String parameters[]) {
		if (scriptStarted) {
			controller.displayMessage("@red@Tavelry Blue Dragons (Pipe) - By Kaila");
			controller.displayMessage("@red@Start in Fally west with gear on, or in dragon room!");
			controller.displayMessage("@red@Sharks, Law, Water, Air IN BANK REQUIRED");
			controller.displayMessage("@red@70 Agility required, for the shortcut!");
			if (controller.isInBank() == true) {
				controller.closeBank();
			}
			if (controller.currentY() < 2800) {
				bank();
				BankToDragons();
				controller.sleep(1380);
			}

			scriptStart();
		} else {
			if (parameters[0].equals("")) {
				if (!guiSetup) {
					setupGUI();
					guiSetup = true;
				}
			} else {
				try {
					foodWithdrawAmount = Integer.parseInt(parameters[0]);
				} catch (Exception e) {
					System.out.println("Could not parse parameters!");
					controller.displayMessage("@red@Could not parse parameters!");
					controller.stop();
				}
			}
		}
		return 1000; //start() must return a int value now.
	}
	public void scriptStart() {
			while(controller.isRunning()) {
						
				eat();
				
				if(controller.getInventoryItemCount(465) > 0 && !controller.isInCombat()) {
					controller.dropItem(controller.getInventoryItemSlotIndex(465));
				}
						
				if(controller.getInventoryItemCount(546) > 0) {
					
					if(controller.getInventoryItemCount() < 30) {
				 		boolean lootPickedUp = false; 
					   	for(int lootId : loot) {
					   		int[] coords = controller.getNearestItemById(lootId);
			        		if(coords != null && this.isWithinLootzone(coords[0], coords[1])) {
								controller.setStatus("@yel@Looting..");
								controller.walkTo(coords[0], coords[1]);
					   			controller.pickupItem(coords[0], coords[1], lootId, true, true);
					   			controller.sleep(618);
					   		}
					   	}
					   	if(lootPickedUp) //we don't want to start to pickup loot then immediately attack a npc
					  		continue;

				   	if(controller.getCurrentStat(controller.getStatId("Attack")) == controller.getBaseStat(controller.getStatId("Attack"))) {
				   		if(controller.getInventoryItemCount(attackPot[0]) > 0 || controller.getInventoryItemCount(attackPot[1]) > 0 || controller.getInventoryItemCount(attackPot[2]) > 0 ) {
				   		attackBoost();
						}
				   	}
				   	if(controller.getCurrentStat(controller.getStatId("Strength")) == controller.getBaseStat(controller.getStatId("Strength"))) {
						if(controller.getInventoryItemCount(strPot[0]) > 0 || controller.getInventoryItemCount(strPot[1]) > 0 || controller.getInventoryItemCount(strPot[2]) > 0 ) {
				   		strengthBoost();
						}
				   	}
				   	
				   	if(!controller.isInCombat()) {
		    			controller.setStatus("@yel@Attacking Dragons");
				    	controller.sleepHandler(98, true);
					   	ORSCharacter npc = controller.getNearestNpcById(202, false);
					   	if(npc != null) {
					    	controller.attackNpc(npc.serverIndex);
					    	controller.sleep(1000);
					    } else {
							controller.sleep(1000);
							if (controller.currentX() != 370 || controller.currentY() != 3353){
								controller.walkTo(370,3353);
								controller.sleep(1000);
							}
						}
				    }
			    	controller.sleep(1380);
			    	
			    
				}
				if(controller.getInventoryItemCount() == 30) {
					while(controller.isInCombat()) {
						controller.setStatus("@red@Leaving combat..");
						controller.walkTo(controller.currentX(), controller.currentY(), 0, true);
						controller.sleep(250);
					}
					controller.setStatus("@red@Eating Food to Loot..");

					for(int id : controller.getFoodIds()) {
						if(controller.getInventoryItemCount(id) > 0) {
							controller.itemCommand(id);
							controller.sleep(700);
						}
					}	
				} 
				}
				
				if(controller.getInventoryItemCount(546) == 0) {
					controller.setStatus("@yel@Banking..");
					DragonsToBank();
					bank();
					BankToDragons();
					controller.sleep(618);
					}
				}
			}
	
					
		

	
	public void attackBoost() {
		while(controller.isInCombat()) {
			controller.setStatus("@red@Leaving combat..");
			controller.walkTo(controller.currentX(), controller.currentY(), 0, true);
			controller.sleep(250);
		}
		if(controller.getInventoryItemCount(attackPot[0]) > 0) {
			controller.itemCommand(attackPot[0]);
			controller.sleep(320);
			return;
		}
		if(controller.getInventoryItemCount(attackPot[1]) > 0) {
			controller.itemCommand(attackPot[1]);
			controller.sleep(320);
			return;
		}
		if(controller.getInventoryItemCount(attackPot[2]) > 0) {
			controller.itemCommand(attackPot[2]);
			controller.sleep(320);
			return;
		}
		return;
	}

	public void strengthBoost() {
		while(controller.isInCombat()) {
			controller.setStatus("@red@Leaving combat..");
			controller.walkTo(controller.currentX(), controller.currentY(), 0, true);
			controller.sleep(250);
		}
		if(controller.getInventoryItemCount(strPot[0]) > 0) {
			controller.itemCommand(strPot[0]);
			controller.sleep(320);
			return;
		}
		if(controller.getInventoryItemCount(strPot[1]) > 0) {
			controller.itemCommand(strPot[1]);
			controller.sleep(320);
			return;
		}
		if(controller.getInventoryItemCount(strPot[2]) > 0) {
			controller.itemCommand(strPot[2]);
			controller.sleep(320);
			return;
		}
		return;
	}
	
	public void bank() {

		controller.setStatus("@yel@Banking..");
		controller.openBank();
		controller.sleep(640);

		if (controller.isInBank()) {
			
			totalDbones = totalDbones + controller.getInventoryItemCount(814);
			totalRdagger = totalRdagger + controller.getInventoryItemCount(396);
			totalGems = totalGems 
					+ controller.getInventoryItemCount(160) 
					+ controller.getInventoryItemCount(159)
					+ controller.getInventoryItemCount(158)
					+ controller.getInventoryItemCount(157);
			totalHerb = totalHerb
					+ controller.getInventoryItemCount(438) 
					+ controller.getInventoryItemCount(439) 
					+ controller.getInventoryItemCount(440) 
					+ controller.getInventoryItemCount(441) 
					+ controller.getInventoryItemCount(442) 
					+ controller.getInventoryItemCount(443);
			totalFire = totalFire + controller.getInventoryItemCount(31);
			totalLaw = totalLaw + controller.getInventoryItemCount(42);
			totalNat = totalNat + controller.getInventoryItemCount(40);
			totalWater = totalWater + controller.getInventoryItemCount(32);
			totalAddy = totalAddy + controller.getInventoryItemCount(154);
			totalLoop = totalLoop + controller.getInventoryItemCount(527);
			totalTooth = totalTooth + controller.getInventoryItemCount(526);
			totalLeft = totalLeft + controller.getInventoryItemCount(1277);
			totalSpear = totalSpear + controller.getInventoryItemCount(1092);
			
		
			for (int itemId : controller.getInventoryItemIds()) {
				if (itemId != 486 && itemId != 487 && itemId != 488 && itemId != 492 && itemId != 493 && itemId != 494 ) {
					controller.depositItem(itemId, controller.getInventoryItemCount(itemId));
				}
			}
			controller.sleep(1280);   // increased sleep here to prevent double banking

			if(controller.getInventoryItemCount(33) < 3) {  //3 air
				controller.withdrawItem(33, 3 - controller.getInventoryItemCount(33));
				controller.sleep(340);
			}
			if(controller.getInventoryItemCount(42) < 1) {  //1 law
				controller.withdrawItem(42, 1);
				controller.sleep(340);
			}
			if(controller.getInventoryItemCount(32) < 1) {  //1 water
				controller.withdrawItem(32, 1);
				controller.sleep(340);
			}
			if(controller.getInventoryItemCount(attackPot[0]) < 1 && controller.getInventoryItemCount(attackPot[1]) < 1 && controller.getInventoryItemCount(attackPot[2]) < 1 ) {  //withdraw 10 shark if needed
				controller.withdrawItem(attackPot[2], 1);
				controller.sleep(340);
			}
			if(controller.getInventoryItemCount(strPot[0]) < 1 && controller.getInventoryItemCount(strPot[1]) < 1 && controller.getInventoryItemCount(strPot[2]) < 1 ) {  //withdraw 10 shark if needed
				controller.withdrawItem(strPot[2], 1);
				controller.sleep(340);
			}
			if(controller.getInventoryItemCount(546) < foodWithdrawAmount) {  //withdraw 20 shark
				controller.withdrawItem(546, foodWithdrawAmount - controller.getInventoryItemCount(546));
				controller.sleep(340);
			}
			bankDbones = controller.getBankItemCount(814);
			if(controller.getBankItemCount(546) == 0 || controller.getBankItemCount(33) == 0 || controller.getBankItemCount(42) == 0 || controller.getBankItemCount(32) == 0) {
				controller.setStatus("@red@NO Sharks/Laws/Airs in the bank, Logging Out!.");
				controller.setAutoLogin(false);
				controller.logout();
				if(!controller.isLoggedIn()) {
					controller.stop();
					return;
				}
			}
			if(!controller.isItemIdEquipped(420)) {
				controller.setStatus("@red@Not Wielding Dragonfire Shield!.");
				if(controller.getBankItemCount(420) == 0) {
					controller.setAutoLogin(false);
					controller.logout();
					if(!controller.isLoggedIn()) {
						controller.stop();
						return;
					}
				}
				controller.withdrawItem(420, 1);
				controller.closeBank();
				controller.equipItem(controller.getInventoryItemSlotIndex(420));
				controller.sleep(1320);
			}
			controller.closeBank();
			controller.sleep(640);
		}
	}
	
	public void eat() {
		
		int eatLvl = controller.getBaseStat(controller.getStatId("Hits")) - 20;
		
		
		if(controller.getCurrentStat(controller.getStatId("Hits")) < eatLvl) {
			
			while(controller.isInCombat()) {
				controller.setStatus("@red@Leaving combat..");
				controller.walkTo(controller.currentX(), controller.currentY(), 0, true);
				controller.sleep(250);
			}
			controller.setStatus("@red@Eating..");
			
			boolean ate = false;
			
			for(int id : controller.getFoodIds()) {
				if(controller.getInventoryItemCount(id) > 0) {
					controller.itemCommand(id);
					controller.sleep(700);
					ate = true;
					break;
				}
			}
			if(!ate) {  //only activates if hp goes to -20 again THAT trip, will bank and get new shark usually
				controller.setStatus("@red@We've ran out of Food! Running Away!.");
				controller.sleep(308);
				DragonsToBank();
				bank();
				BankToDragons();
			}
		}
	}
	public void DragonsToBank() {
    	controller.setStatus("@gre@Walking to Bank..");
    	while(controller.currentY() > 3000) {
    		controller.castSpellOnSelf(controller.getSpellIdFromName("Falador Teleport"));
    		controller.sleep(308);
    	}
		totalTrips = totalTrips + 1;
		controller.sleep(308);
		controller.walkTo(327,552);
		controller.sleep(308);
    	controller.setStatus("@gre@Done Walking..");
	}  
	
    public void BankToDragons() {	
    	controller.setStatus("@gre@Walking to Blue Dragons..");
		controller.walkTo(327, 552);
		controller.walkTo(324, 549);
		controller.walkTo(324, 539);
		controller.walkTo(324, 530);
		controller.walkTo(317, 523);
		controller.walkTo(317, 516);
		controller.walkTo(327, 506);
		controller.walkTo(337, 496);
		controller.walkTo(337, 492);
		controller.walkTo(341, 488);
		while(controller.currentX() == 341 && controller.currentY() < 489 && controller.currentY() > 486) {
			controller.atObject(341,487);   //gate wont break if someone else opens it
			controller.sleep(640);
		}
		controller.walkTo(342,493);
		controller.walkTo(350,501);
		controller.walkTo(355,506);
		controller.walkTo(360,511);
		controller.walkTo(362,513);
		controller.walkTo(367,514);
		controller.walkTo(374,521);
		controller.walkTo(376,521);
		controller.atObject(376,520);
		controller.sleep(640);
		controller.walkTo(375,3352);
		controller.atObject(374,3352);
		controller.sleep(640);
		controller.walkTo(372,3352);
		controller.sleep(320);
    	controller.setStatus("@gre@Done Walking..");

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//GUI stuff below (icky)
	
	
	
	public static void centerWindow(Window frame) {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
	}
	public void setupGUI() {
		JLabel header = new JLabel("Tavelry Blue Dragons (Pipe) - By Kaila");
		JLabel label1 = new JLabel("Start in Fally west with gear on, or in Dragon room!");
		JLabel label2 = new JLabel("Sharks, Law, Water, Air IN BANK required");
		JLabel label3 = new JLabel("70 Agility required, for the shortcut!");
		JLabel foodWithdrawAmountLabel = new JLabel("Food Withdraw amount:");
		JTextField foodWithdrawAmountField = new JTextField(String.valueOf(20));
		JButton startScriptButton = new JButton("Start");

		startScriptButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!foodWithdrawAmountField.getText().equals(""))
					foodWithdrawAmount = Integer.parseInt(foodWithdrawAmountField.getText());

				scriptFrame.setVisible(false);
				scriptFrame.dispose();
				startTime = System.currentTimeMillis();
				scriptStarted = true;
			}
		});
		
		scriptFrame = new JFrame("Script Options");

		scriptFrame.setLayout(new GridLayout(0, 1));
		scriptFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		scriptFrame.add(header);
		scriptFrame.add(label1);
		scriptFrame.add(label2);
		scriptFrame.add(label3);
		scriptFrame.add(foodWithdrawAmountLabel);
		scriptFrame.add(foodWithdrawAmountField);
		scriptFrame.add(startScriptButton);
		centerWindow(scriptFrame);
		scriptFrame.setVisible(true);
		scriptFrame.pack();
		scriptFrame.requestFocus();

	}
	public static String msToString(long milliseconds) {
		long sec = milliseconds / 1000;
		long min = sec / 60;
		long hour = min / 60;
		sec %= 60;
		min %= 60;
		DecimalFormat twoDigits = new DecimalFormat("00");

		return new String(twoDigits.format(hour) + ":" + twoDigits.format(min) + ":" + twoDigits.format(sec));
	}
	@Override
	public void paintInterrupt() {
		if (controller != null) {
			String runTime = msToString(System.currentTimeMillis() - startTime);
    		int DbonesSuccessPerHr = 0;
    		int RdaggerSuccessPerHr = 0;
    		int GemsSuccessPerHr = 0;
    		int FireSuccessPerHr = 0;
    		int LawSuccessPerHr = 0;
    		int NatSuccessPerHr = 0;
    		int WaterSuccessPerHr = 0;
    		int AddySuccessPerHr = 0;
    	    int HerbSuccessPerHr = 0;
    		int TripSuccessPerHr = 0;
    		
	    	try {
	    		float timeRan = (System.currentTimeMillis() / 1000L) - startTimestamp;
	    		float scale = (60 * 60) / timeRan;
	    		DbonesSuccessPerHr = (int)(totalDbones * scale);
	    		RdaggerSuccessPerHr = (int)(totalRdagger * scale);
	    		GemsSuccessPerHr = (int)(totalGems * scale);
	    		FireSuccessPerHr = (int)(totalFire * scale);
	    		LawSuccessPerHr = (int)(totalLaw * scale);
	    		NatSuccessPerHr = (int)(totalNat * scale);
	    		WaterSuccessPerHr = (int)(totalWater * scale);
	    		AddySuccessPerHr = (int)(totalAddy * scale);
	    		HerbSuccessPerHr = (int)(totalHerb * scale);
	    		TripSuccessPerHr = (int)(totalTrips * scale);
	    	} catch(Exception e) {
	    		//divide by zero
	    	}
			controller.drawString("@red@Tavelry Blue Dragons @gre@by Kaila", 310, 48, 0xFFFFFF, 1);
			controller.drawString("@whi@Dragon Bones in Bank: @gre@" + String.valueOf(this.bankDbones), 330, 62, 0xFFFFFF, 1); 
			controller.drawString("@whi@Dragon Bones: @gre@" + String.valueOf(this.totalDbones) + "@yel@ (@whi@" + String.format("%,d", DbonesSuccessPerHr) + "@yel@/@whi@hr@yel@)", 330, 76, 0xFFFFFF, 1);  //fix y cords
			controller.drawString("@whi@Rune Dagger: @gre@" + String.valueOf(this.totalRdagger) + "@yel@ (@whi@" + String.format("%,d", RdaggerSuccessPerHr) + "@yel@/@whi@hr@yel@)", 330, 90, 0xFFFFFF, 1);
			controller.drawString("@whi@Law Runes: @gre@" + String.valueOf(this.totalLaw) + "@yel@ (@whi@" + String.format("%,d", LawSuccessPerHr) + "@yel@/@whi@hr@yel@)", 330, 104, 0xFFFFFF, 1);
			controller.drawString("@whi@Nature Runes: @gre@" + String.valueOf(this.totalNat) + "@yel@ (@whi@" + String.format("%,d", NatSuccessPerHr) + "@yel@/@whi@hr@yel@)", 330, 118, 0xFFFFFF, 1);
			controller.drawString("@whi@Fire Runes: @gre@" + String.valueOf(this.totalFire) + "@yel@ (@whi@" + String.format("%,d", FireSuccessPerHr) + "@yel@/@whi@hr@yel@)", 330, 132, 0xFFFFFF, 1);
			controller.drawString("@whi@Water Runes: @gre@" + String.valueOf(this.totalWater) + "@yel@ (@whi@" + String.format("%,d", WaterSuccessPerHr) + "@yel@/@whi@hr@yel@)", 330, 146, 0xFFFFFF, 1);
			controller.drawString("@whi@Adamantite Ore: @gre@" + String.valueOf(this.totalAddy) + "@yel@ (@whi@" + String.format("%,d", AddySuccessPerHr) + "@yel@/@whi@hr@yel@)", 330, 160, 0xFFFFFF, 1);
			controller.drawString("@whi@Total Herbs: @gre@" + String.valueOf(this.totalHerb) + "@yel@ (@whi@" + String.format("%,d", HerbSuccessPerHr) + "@yel@/@whi@hr@yel@)", 330, 174, 0xFFFFFF, 1);
			controller.drawString("@whi@Total Gems: @gre@" + String.valueOf(this.totalGems) + "@yel@ (@whi@" + String.format("%,d", GemsSuccessPerHr) + "@yel@/@whi@hr@yel@)", 330, 188, 0xFFFFFF, 1);
			controller.drawString("@whi@Tooth: @gre@" + String.valueOf(this.totalTooth) + "@yel@ / @whi@Loop: @gre@" + String.valueOf(this.totalLoop), 330, 202, 0xFFFFFF, 1);
			controller.drawString("@whi@Rune Spear: @gre@" + String.valueOf(this.totalSpear), 330, 216, 0xFFFFFF, 1);
			controller.drawString("@whi@Left Half: @gre@"+ String.valueOf(this.totalLeft), 330, 230, 0xFFFFFF, 1);
			controller.drawString("@whi@Total Trips: @gre@" + String.valueOf(this.totalTrips) + "@yel@ (@whi@" + String.format("%,d", TripSuccessPerHr) + "@yel@/@whi@hr@yel@)", 330, 244, 0xFFFFFF, 1);
			controller.drawString("@whi@Runtime: " + runTime, 330, 258, 0xFFFFFF, 1);
		}
	}
}