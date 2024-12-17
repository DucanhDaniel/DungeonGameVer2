package effect;

import entities.Player;
import entities.Sprite;
import entities.monsters.Mage;
import entities.monsters.Monster;
import entities.monsters.Sickle;
import entities.monsters.SwordKnight;
import entities.monsters.bosses.SkeletonReaper;
import gamestates.Playing;
import main.CollisionChecker;
import utils.Constants;

import java.awt.*;
import java.util.Random;

import static utils.Constants.Screen.TILE_SIZE;

public class Portal extends EffectMethod {
    SkeletonReaper entity;
    public Portal(Monster entity, int worldX, int worldY, int index) {
        super("Portal", 20, entity,
                entity.getPlaying().getPlayer()
                , worldX, worldY, 6 * TILE_SIZE, 5 * TILE_SIZE, index);

        this.entity = (SkeletonReaper) entity;
        effectRect = new Rectangle(0, 0, width, height);

        // Make the center of effectRect has position (worldX, worldY)
        this.worldX -= effectRect.x + effectRect.width / 2;
        this.worldY -= effectRect.y + effectRect.height / 2;
        effectRect.x += this.worldX;
        effectRect.y += this.worldY;
        createNewMonster();
    }

    public void draw(Graphics2D g2) {
        super.draw(g2, 1);
    }
    Random random = new Random();

    public void createNewMonster() {
//        System.out.println("Yeahhhh");
        Playing playing = entity.getPlaying();
        int x = random.nextInt(3);
        Monster monster = null;
        switch (x) {
            case 0:
                monster = new Mage(playing, worldX - 3 * TILE_SIZE, worldY + TILE_SIZE / 2);
                break;
            case 1:
                monster = new SwordKnight(playing, worldX - 3 * TILE_SIZE, worldY + TILE_SIZE / 2);
                break;
            case 2:
                monster = new Sickle(playing, worldX - TILE_SIZE, worldY + TILE_SIZE / 2);
                break;
        }
        CollisionChecker cc = playing.getGame().getCollisionChecker();
        if (!cc.checkTile(monster) && cc.checkEntity(monster, playing.entityList) == -1) {
            playing.monsters.add(monster);
            playing.entityList.add(monster);
        }
    }

    public void update() {
        frameCounter++;
        if (frameCounter >= frameDuration * totalAnimationFrames) {
            // Summon monster
//            Playing playing = entity.getPlaying();
//
//            for (int i = 0; i < 3; i++) {
//                int x = random.nextInt(3);
//                Monster monster = null;
//                switch (x) {
//                    case 0:
//                        monster = new Mage(playing, worldX - 3 * TILE_SIZE, worldY + TILE_SIZE / 2);
//                        break;
//                    case 1:
//                        monster = new SwordKnight(playing, worldX - 3 * TILE_SIZE, worldY + TILE_SIZE / 2);
//                        break;
//                    case 2:
//                        monster = new Sickle(playing, worldX - TILE_SIZE, worldY + TILE_SIZE / 2);
//                        break;
//                }
//                CollisionChecker cc = playing.getGame().getCollisionChecker();
//                if (!cc.checkTile(monster) && cc.checkEntity(monster, playing.entityList) == -1) {
//                    playing.monsters.add(monster);
//                    playing.entityList.add(monster);
//                }
//            }
//            for (int i = 0; i < playing.entityArray.length; i++) {
//                if (playing.entityArray[i] == null) {
//                    int x = random.nextInt(3);
//                    switch (x) {
//                        case 0:
//                            playing.entityArray[i] = new Mage(playing, worldX - 3 * TILE_SIZE, worldY + TILE_SIZE / 2);
//                            break;
//                        case 1:
//                            playing.entityArray[i] = new SwordKnight(playing, worldX - 3 * TILE_SIZE, worldY + TILE_SIZE / 2);
//                            break;
//                        case 2:
//                            playing.entityArray[i] = new Sickle(playing, worldX - TILE_SIZE, worldY + TILE_SIZE / 2);
//                            break;
//                    }
//                    CollisionChecker cc = playing.getGame().getCollisionChecker();
//                    if (cc.checkTile((Sprite) playing.entityArray[i])
//                            || cc.checkEntity((Sprite) playing.entityArray[i], playing.entityList) != -1) {
//                        playing.entityArray[i] = null;
//                        break;
//                    }
//                    else {
//                        playing.entityList.add((Sprite) playing.entityArray[i]);
//                        for (int j = 0; j < playing.monsters.length; j++) {
//                            if (playing.monsters[j] == null) {
//                                playing.monsters[j] = (Monster) playing.entityArray[i];
//                                break;
//                            }
//                        }
//                        break;
//                    }
//                }
//            }
            removeEffect(index);
        }
    }

    @Override
    public void removeEffect(int index) {
        entity.removePortal(index);
    }
}
