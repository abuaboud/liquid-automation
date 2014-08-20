package org.liquidbot.bot.script.randevent.impl;

import java.awt.Graphics2D;
import java.util.HashMap;

import org.liquidbot.bot.script.api.interfaces.Filter;
import org.liquidbot.bot.script.api.interfaces.PaintListener;
import org.liquidbot.bot.script.api.methods.data.Inventory;
import org.liquidbot.bot.script.api.methods.data.Menu;
import org.liquidbot.bot.script.api.methods.data.movement.Walking;
import org.liquidbot.bot.script.api.methods.input.Mouse;
import org.liquidbot.bot.script.api.methods.interactive.GameEntities;
import org.liquidbot.bot.script.api.methods.interactive.GroundItems;
import org.liquidbot.bot.script.api.methods.interactive.NPCs;
import org.liquidbot.bot.script.api.methods.interactive.Players;
import org.liquidbot.bot.script.api.methods.interactive.Widgets;
import org.liquidbot.bot.script.api.util.Random;
import org.liquidbot.bot.script.api.util.Time;
import org.liquidbot.bot.script.api.wrappers.GameObject;
import org.liquidbot.bot.script.api.wrappers.Item;
import org.liquidbot.bot.script.api.wrappers.NPC;
import org.liquidbot.bot.script.api.wrappers.Tile;
import org.liquidbot.bot.script.api.wrappers.WidgetChild;
import org.liquidbot.bot.script.randevent.RandomEvent;

public class GraveRandom extends RandomEvent implements PaintListener {
	
	private final String ITEM_NAME = "Coffin",
			GRAVE_NAME = "Grave",
			GRAVESTONE_NAME = "Gravestone";
	
	private final int GRAVESTONE_MINING = 7616,
			GRAVESTONE_WOODCUTTING = 7614,
			GRAVESTONE_COOKING = 7615,
			GRAVESTONE_FARMING = 7617,
			GRAVESTONE_CRAFTING = 7618;
	private final int[] COFFIN_WOODCUTTING = new int[] {7603, 7605, 7612},
			COFFIN_FARMING = new int[] {7610, 7602, 7609},
			COFFIN_CRAFTING = new int[] {7608, 7613, 7599},
			COFFIN_COOKING = new int[] {7600, 7601, 7604},
			COFFIN_MINING= new int[] {7597, 7606, 7607};
			// COFFIN_OTHER = new int[] {7598, 7611};
	
	private HashMap<Integer, Integer> gravestoneCoffins = new HashMap<Integer, Integer>();

	private WidgetChild widget;

	private boolean hasCoffins = false;

	private int curCoffinCount;

	private GameObject tmpGameObj, grave;

	private int widgetId;

	private NPC leo;

	private int curFreeSpace;

	@Override
	public boolean active() {
		return GameEntities.getNearest("Mausoleum").isValid() && NPCs.getNearest("Leo").isValid();
	}

	@Override
	public String getAuthor() {
		return "Lemons";
	}

	@Override
	public String getName() {
		return "Grave Shuffle";
	}

	@Override
	public void solve() {
		if (active()) {
			if (!hasCoffins && Inventory.getFreeSpace() - (5 - Inventory.getCount(ITEM_NAME)) < 0) {
				if (GameEntities.getNearest("Mausoleum").distanceTo() > 3) {
					Walking.walkTo(GameEntities.getNearest("Mausoleum"));
				}
				
				GameEntities.getNearest("Mausoleum").interact("Deposit");
				
				for (int i = 0; i < 50 && !Widgets.canContinue(); i++)
					Time.sleep(100, 200);
				
				Widgets.clickContinue();
				
				Time.sleep(1500, 3000);
				
				for (int y = 0; y < 4; y++) {
					for (int x = 0; x < 7; x++) {
						if (Inventory.getFreeSpace() - (5 - Inventory.getCount(ITEM_NAME)) < 0) {
							Mouse.move(131 + (40 * x), 75 + (42 * y));
							Time.sleep(250, 500);
							Menu.interact("Deposit All");
							Time.sleep(250, 500);
						}
					}
				}
			} else if (Widgets.get(220, 16).isVisible()) {
				log.info("Exiting info screen");
				Widgets.get(220, 16).click();
				Time.sleep(1500, 3000);
			} else if (!hasCoffins && Inventory.getCount(ITEM_NAME) < 5) {
				// Get all coffins
				for (GameObject o : getGraves(true)) {
					if (o.distanceTo() > 2) {
						log.info("Walking to grave...");
						Walking.walkTo(o);
						for (int i = 0; i < 50 && !Players.getLocal().isMoving(); i++)
							Time.sleep(100, 200);
						for (int i = 0; i < 50 && Players.getLocal().isMoving(); i++)
							Time.sleep(100, 200);
					}
					
					curCoffinCount = Inventory.getCount(ITEM_NAME);
					
					if (o.interact("Take-Coffin")) {
						for (int i = 0; i < 100 && curCoffinCount == Inventory.getCount(ITEM_NAME); i++)
							Time.sleep(100, 200);
						
						log.info("Got the coffin!");
						
						Time.sleep(1000, 2000);
						if (Inventory.getCount(ITEM_NAME) == 5) {
							log.info("All coffins aquired!");
							hasCoffins = true;
						}
					}
				}
			} else if (gravestoneCoffins.size() < 5) {
				hasCoffins = true;
				for (Item coffin : Inventory.getAllItems()) {
					if (coffin == null || !coffin.isValid() || !coffin.getName().equalsIgnoreCase(ITEM_NAME))
						continue;
					
					for (int gi : gravestoneCoffins.keySet()) {
						if (gravestoneCoffins.get(gi) == coffin.getId()) {
							log.info("Already know gravestone "+gi+" goes with "+coffin.getId());
							continue;
						}
					}
					
					if (coffin.interact("Check")) {
						for (int i = 0; i < 50 && !Widgets.get(141, 3).isVisible(); i++)
							Time.sleep(100, 200);
						
						Time.sleep(3000, 5000);
						
						getGravestone(coffin.getId());
						
						if (Random.nextInt(0, 5) == 1 && Widgets.get(141, 12).isVisible()) {
							Widgets.get(141, 12).click();
							Time.sleep(500, 1000);
						}
					}
				}
			} else if (Inventory.contains(ITEM_NAME)) {
				hasCoffins = true;
				log.info("Placing in graves!");
				
				if (Widgets.get(141, 12).isVisible()) {
					Widgets.get(141, 12).click();
					Time.sleep(500, 1000);
				}
				
				for (GameObject o : getGravestones()){
					if (actionCount(closestGrave(o.getLocation())) == 1 || Inventory.getCount(ITEM_NAME) == 0)
						continue;
					if (o.distanceTo() > 3) {
						log.info("Walking to gravestone");
						Walking.walkTo(o);
						for (int i = 0; i < 50 && !Players.getLocal().isMoving(); i++)
							Time.sleep(100, 200);
						for (int i = 0; i < 50 && Players.getLocal().isMoving(); i++)
							Time.sleep(100, 200);
					}
					
					if (o.interact("Read")) {
						for (int i = 0; i < 100 && !Widgets.get(143, 2).isVisible(); i++)
							Time.sleep(100, 200);
						
						Time.sleep(300, 1000);
						
						if (Widgets.get(143, 2).isVisible()) {
							graveMatch(Widgets.get(143, 2), o.getLocation());
						}
					}
					
				}
			} else if (GroundItems.getAll().length > 0) {
				log.info("Picking up ground item!");
				GroundItems.getAll()[0].interact("Take");
				curFreeSpace = Inventory.getFreeSpace();
				for (int i = 0; i < 100 && curFreeSpace == Inventory.getFreeSpace(); i++)
					Time.sleep(100, 200);
						
				Time.sleep(500, 1000);
			} else {
				leo = NPCs.getNearest("Leo");
				
				if (leo.distanceTo() > 3) {
					log.info("Walking to Leo");
					Walking.walkTo(leo);
					for (int i = 0; i < 50 && !Players.getLocal().isMoving(); i++)
						Time.sleep(100, 200);
					for (int i = 0; i < 50 && Players.getLocal().isMoving(); i++)
						Time.sleep(100, 200);
				} else if (Widgets.canContinue()) {
					log.info("Continuing...");
					if (Widgets.get(241, 2).isVisible() && Widgets.get(241, 2).getText() != null
							&& Widgets.get(241, 2).getText().equalsIgnoreCase("Ok, let's have a look.")) {
						Widgets.clickContinue();
						Time.sleep(7500, 10000);
						reset();
						log.info("Random finished.");
					} else {
						Widgets.clickContinue();
						Time.sleep(1500, 3000);
					}
				} else {
					log.info("Talking to leo");
					leo.interact("Talk-to");
					for (int i = 0; i < 50 && !Widgets.canContinue(); i++)
						Time.sleep(100, 200);
				}
			}
		}
	}
	
	@Override
	public void reset() {
		hasCoffins = false;
		gravestoneCoffins = new HashMap<Integer, Integer>();
	}
	
	public void getGravestone(int coffinId) {
		log.info("Getting gravestone...");
		for (int i = 3; i < 12; i++) {
			log.info("Cell #"+(i-2));
			widget = Widgets.get(141, i);

			if (widget != null && hasCoffinWidget(widget, COFFIN_MINING)) {
				log.info("Detected Mining for coffin #"+coffinId);
				gravestoneCoffins.put(GRAVESTONE_MINING, coffinId);
				break;
			} else if (widget != null && hasCoffinWidget(widget, COFFIN_COOKING)) {
				log.info("Detected Cooking for coffin #"+coffinId);
				gravestoneCoffins.put(GRAVESTONE_COOKING, coffinId);
				break;
			} else if (widget != null && hasCoffinWidget(widget, COFFIN_CRAFTING)) {
				log.info("Detected Crafting for coffin #"+coffinId);
				gravestoneCoffins.put(GRAVESTONE_CRAFTING, coffinId);
				break;
			} else if (widget != null && hasCoffinWidget(widget, COFFIN_FARMING)) {
				log.info("Detected Farming for coffin #"+coffinId);
				gravestoneCoffins.put(GRAVESTONE_FARMING, coffinId);
				break;
			} else if (widget != null && hasCoffinWidget(widget, COFFIN_WOODCUTTING)) {
				log.info("Detected Woodcutting for coffin #"+coffinId);
				gravestoneCoffins.put(GRAVESTONE_WOODCUTTING, coffinId);
				break;
			}
		}
	}

	private boolean hasCoffinWidget(WidgetChild widget, int[] ids) {
		for (int i : ids)
			if (widget.getModelId() == i)
				return true;
		return false;
	}

	private void graveMatch(WidgetChild widget, Tile l) {
		if (!widget.isVisible())
			return;
		
		widgetId = widget.getModelId();
		
		Widgets.get(143, 3).click();
		
		Time.sleep(1000, 1500);
		
		grave = closestGrave(l);
		
		Inventory.getItem(gravestoneCoffins.get(widgetId)).interact("Use");
		
		Time.sleep(500, 1000);
		
		if (grave.interact("Use", "Coffin -> Grave")) {
			curCoffinCount = Inventory.getCount(ITEM_NAME);
			for (int i = 0; i < 100 && curCoffinCount == Inventory.getCount(ITEM_NAME); i++)
				Time.sleep(100, 200);
			log.info("Grave fixed!");
			
			Time.sleep(1000, 1500);
		}
	}

	private GameObject closestGrave(Tile l) {
		tmpGameObj = null;
		for (GameObject o : GameEntities.getAll(GRAVE_NAME)) {
			if (o.isValid() && actionCount(o) == 0 && (tmpGameObj == null || tmpGameObj.distanceTo(l) > o.distanceTo(l))) {
				tmpGameObj = o;
			}
		}
		return tmpGameObj;
	}

	private GameObject[] getGraves(final boolean b) {
		return GameEntities.getAll(new Filter<GameObject>() {

			@Override
			public boolean accept(GameObject o) {
				return o.isValid() && o.getName().equalsIgnoreCase(GRAVE_NAME) && actionCount(o) == (b ? 1 : 0);
			}
			
		});
	}

	private GameObject[] getGravestones() {
		return GameEntities.getAll(new Filter<GameObject>() {

			@Override
			public boolean accept(GameObject o) {
				return o.isValid() && o.getName().equalsIgnoreCase(GRAVESTONE_NAME)
						&& actionCount(closestGrave(o.getLocation())) == 0;
			}
			
		});
	}

	@Override
	public void render(Graphics2D g) {
		g.drawString("Widget 143,2: "+Widgets.get(143, 2).getModelId(), 10, 100);
	}

	private int actionCount(GameObject o) {
		int i = 0;
		for (String s : o.getActions()) {
			if (s != null && !s.isEmpty()) {
				i++;
			}
		}
		log.info(o.getName()+" has "+i+" actions!");
		return i;
	}

}