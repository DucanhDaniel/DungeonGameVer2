package main;

public class EventHandler {
//    GamePanel gp;
//
//    int previousEventX, previousEventY;
//    boolean canTouch = true;
//
//    EventRect[][] eventRect;
//    boolean isEventHappened = false;
//    public EventHandler(GamePanel gp) {
//        this.gp = gp;
//
//        eventRect = new EventRect[gp.MAX_WORLD_ROW][gp.MAX_WORLD_COL];
//
//        for (int row = 0; row < gp.MAX_WORLD_ROW; row++)
//            for (int col = 0; col < gp.MAX_WORLD_COL; col++){
//                eventRect[row][col] = new EventRect();
//                eventRect[row][col].x = 23;
//                eventRect[row][col].y = 23;
//                eventRect[row][col].width = 2;
//                eventRect[row][col].height = 2;
//                eventRect[row][col].eventRectDefaultX = eventRect[row][col].x;
//                eventRect[row][col].eventRectDefaultY = eventRect[row][col].y;
//            }
//
//    }
//    public void checkEvent() {
//        // Check if player is one tile away from the previous event
//        int xDistance = Math.abs(gp.player.worldX - previousEventX);
//        int yDistance = Math.abs(gp.player.worldY - previousEventY);
//
//        int distance = Math.max(xDistance, yDistance);
//        if (distance > gp.TILE_SIZE) {
//            canTouch = true;
//        }
//
//        if (!canTouch) return;
//
//        if (hit(16, 27, "any")) {
//            damagePit(16, 27, gp.dialogueState);
//        }
//        if (hit(16, 25, "any")) {
//            damagePit(16, 25, gp.dialogueState);
//        }
//        if (hit(12, 23, "up")) {
//            healingPool(12, 23, gp.dialogueState);
//        }
//
//
//    }
//    // Check event collision
//    public boolean hit(int eventRow, int eventCol, String reqDirection) {
//        boolean hit = false;
//        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
//        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
//
//        eventRect[eventRow][eventCol].x = eventCol * gp.TILE_SIZE + eventRect[eventRow][eventCol].x;
//        eventRect[eventRow][eventCol].y = eventRow * gp.TILE_SIZE + eventRect[eventRow][eventCol].y;
//
//        // Checking if player is hitting rectEvent
//        if (gp.player.solidArea.intersects(eventRect[eventRow][eventCol]) && !eventRect[eventRow][eventCol].eventDone) {
//            if (reqDirection.contentEquals(gp.player.direction) || reqDirection.contentEquals("any")) {
//                hit = true;
//
//                previousEventX = gp.player.worldX;
//                previousEventY = gp.player.worldY;
//            }
//        }
//
//        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
//        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
//
//        eventRect[eventRow][eventCol].x = eventRect[eventRow][eventCol].eventRectDefaultX;
//        eventRect[eventRow][eventCol].y = eventRect[eventRow][eventCol].eventRectDefaultY;
//
//        return hit;
//    }
//
//    public void damagePit(int row, int col, int gameState) {
//        // Implement pit damage logic here
//
//        gp.gameState = gameState;
//        gp.ui.currentDialogue = "You fall into a pit!";
//        gp.player.currentLife -= 100;
//
////        eventRect[row][col].eventDone = true;
//        canTouch = false;
//    }
//
//    public void healingPool(int row, int col, int gameState) {
//        if (gp.keyHandler.enterPressed) {
//            gp.gameState = gameState;
//            gp.ui.currentDialogue = "You found a healing pool!";
//            gp.player.currentLife += 100;
//        }
//    }
//
//    public void teleportTile(int row, int col, int gameState) {
//        gp.gameState = gameState;
//        gp.ui.currentDialogue = "You found a hidden door!";
//        gp.player.worldX = 1812;
//        gp.player.worldY = 400;
//    }
}