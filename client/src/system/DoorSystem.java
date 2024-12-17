package system;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectWriter;
import components.PositionComponent;
import com.fasterxml.jackson.annotation.JsonProperty;
import data.DataStorage;
import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Player;
import gamestates.Playing;
import objects.Door;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.*;

import static utils.HelpMethods.scaleImage;

public class DoorSystem {

    @JsonIgnore
    public Playing playing;

    public int enteredDoorID; // The door that player has just gone in
    public int enteredDirection;

    public int exitDirection;

    public ArrayList<Door> doors;

    public DoorSystem() {
        doors = new ArrayList<>();
        enteredDoorID = -1;
        enteredDirection = exitDirection = 0;
    }
//    public DoorSystem(Playing playing) {
//        this.playing = playing;
//        doors = new ArrayList<>();
//        enteredDoorID = -1;
//        enteredDirection = exitDirection = 0;
//        InitSystem.initDoors(doors);
//    }


//    public void saveDoors(DataStorage ds) {
//        ds.doors = doors;
//    }
//    public void loadDoors(DataStorage ds) {
//        doors = ds.doors;
//        System.out.println("Doors size = " + doors.size());
//    }
    public void update() {
        for (Door door : doors) {
            if (door.isOpen) {
                door.animation.playAnAnimation();
            } else {
                door.animation.playAnAnimationReverse();
            }

            String key = door.name;
            int numAnimationFrame = door.animation.numAnimationFrame;
            int totalAnimationFrame = door.animation.totalAnimationFrame;
            BufferedImage image = playing.getImageManager().getObjectImage(key, numAnimationFrame - 1, totalAnimationFrame);
            door.render.image = scaleImage(image, door.render.width, door.render.height);
        }
    }
    public void draw(Graphics2D g2) {
        for (Door door : doors) {
            playing.getRenderSystem().draw(g2, door.position, door.render, door.hitbox);
//            playing.getRenderSystem().draw(g2, door.position, door.render);
        }
    }

    public void checkPlayerNextMove(Player player) {
        PositionComponent playerCurrent = new PositionComponent(player.worldX + player.width / 2, player.worldY + player.height / 2);
        player.goAlongDirection();
        PositionComponent playerNext = new PositionComponent(player.worldX + player.width / 2, player.worldY + player.height / 2);
        player.goOppositeDirection();

        if (enteredDoorID != -1) {
            Door enteredDoor = doors.get(enteredDoorID);
//            System.out.println(enteredDoor.position.worldX + " " + enteredDoor.position.worldY);
            exitDirection = getExitDirection(playerCurrent, playerNext, enteredDoor);
//            System.out.println("EnteredDiretion: " + enteredDirection + ", ExitDirection: " + exitDirection);

            if (exitDirection != 0) {
                enteredDoor.isOpen = false;
                if (exitDirection + enteredDirection == 0 && enteredDirection == enteredDoor.direction) {
                    playing.getMonsterAreaSystem().playerEnteredDoor(enteredDoorID);
                }
                exitDirection = enteredDirection = 0;
                enteredDoorID = -1;
            }
        } else {
            for (int i = 0; i < doors.size(); i++) {
                Door door = doors.get(i);
                enteredDirection = getEnterDirection(playerCurrent, playerNext, door);
                if (enteredDirection == 0) continue;
                if (door.isLocked) {
                    player.collisionOn = true;
                    return;
                }

                enteredDoorID = i;
                doors.get(enteredDoorID).isOpen = true;
                break;
            }
        }
    }

    private int getEnterDirection(PositionComponent current, PositionComponent next, Door door) {
//        System.out.println("(" + door.position.worldX + "," + door.position.worldY + ")");
        int left = door.position.worldX - door.hitbox.width / 2;
        int right = door.position.worldX + door.hitbox.width / 2;
        int up = door.position.worldY - door.hitbox.height / 2;
        int down = door.position.worldY + door.hitbox.height / 2;
        if (left > next.worldX || next.worldX > right || up > next.worldY || next.worldY > down) return 0;
        if (current.worldX < left && left <= next.worldX) return -2;
        if (current.worldY < up && up <= next.worldY) return -1;
        if (current.worldX > right && right >= next.worldX) return 2;
        if (current.worldY > down && down >= next.worldY) return 1;
        return 0;
    }

    private int getExitDirection(PositionComponent current, PositionComponent next, Door door) {
        int left = door.position.worldX - door.hitbox.width / 2;
        int right = door.position.worldX + door.hitbox.width / 2;
        int up = door.position.worldY - door.hitbox.height / 2;
        int down = door.position.worldY + door.hitbox.height / 2;
        if (left <= next.worldX && next.worldX <= right && up <= next.worldY && next.worldY <= down) return 0;
        if (current.worldX >= left && left >= next.worldX) return -2;
        if (current.worldY >= up && up >= next.worldY) return -1;
        if (current.worldX <= right && right <= next.worldX) return 2;
        if (current.worldY <= down && down <= next.worldY) return 1;
        return 0;
    }
    public void lockDoor(int id) {
        doors.get(id).isLocked = true;
    }
    public void unlockDoor(int id) {
        doors.get(id).isLocked = false;
    }
}
