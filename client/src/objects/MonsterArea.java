package objects;

import java.util.ArrayList;

public class MonsterArea {
    public String name;
    public ArrayList<Integer> monsterIDs;
    public ArrayList<Integer> doorIDs;
    public boolean isLocked = false;

    public MonsterArea() {
        monsterIDs = new ArrayList<>();
        doorIDs = new ArrayList<>();
    }

//    public MonsterArea(String name) {
//        this.name = name;
//        monsterIDs = new ArrayList<>();
//        doorIDs = new ArrayList<>();
//    }

    public void addMonster(int monsterID) {
        monsterIDs.add(monsterID);
    }

//    public void addDoor(Door door) {
//        doors.add(door);
//    }
    public void addDoor(int doorID) {
        doorIDs.add(doorID);
    }

//    public void update() {
////        if (isLocked) {
////            monsters.removeIf(monster -> monster.currentState == DEATH);
////            if (monsters.isEmpty()) {
////                unlockArea();
////            }
////        } else {
////            if (!monsters.isEmpty()) {
////                for (Door door : doors) {
////                    if (door.playerPassed) {
////                        lockArea();
////                        return;
////                    }
////                }
////            }
////        }
////        if (isLocked) {
////            monsterIDs.removeIf(monsterID -> monsters[monsterID].currentState == DEATH);
////            if (monsters.isEmpty()) {
////                unlockArea();
////            }
////        } else {
////            if (!monsters.isEmpty()) {
////                for (Door door : doors) {
////                    if (door.playerPassed) {
////                        lockArea();
////                        return;
////                    }
////                }
////            }
////        }
//    }

//    public void lockArea() {
//        isLocked = true;
//        for (Integer id : doorIDs) {
//            door.isLocked = true;
//        }
//    }
//    public void unlockArea() {
//        isLocked = false;
//        for (Door door : doors) {
//            door.isLocked = false;
//        }
//    }
}
