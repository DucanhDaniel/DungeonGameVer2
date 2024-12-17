package entities.projectile;

import static enitystates.EntityState.*;

import enitystates.EntityState;
import entities.Entity;
import gamestates.Playing;
import utils.Constants;
import utils.HelpMethods;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

public class ProjectileManager {
    public ArrayList<Projectile> projectileList;
    private Playing playing;

    public ProjectileManager(Playing playing) {
        projectileList = new ArrayList<>();
        this.playing = playing;
    }

    public void update() {
        Iterator<Projectile> iterator = projectileList.iterator();

        while (iterator.hasNext()) {
                Projectile projectile = iterator.next();
                if (projectile.getState().equals("ATTACK")) {
                    playing.getGame().getCollisionChecker().checkPlayer(projectile);
                    if (projectile.collisionOn) {
                        playing.getPlayer().getHurt(projectile.attackPoints);
                        projectile.setState("EXPLOSION");
                    } else {
                        playing.getGame().getCollisionChecker().checkTile(projectile);
                        if (projectile.collisionOn) {
                            projectile.setState("EXPLOSION");
                        } else {
                            projectile.update();
                        }
                    }
                } else {
                    if (projectile.checkCompleteOneAnimation()) {
                        iterator.remove();
                    } else {
                        projectile.update();
                    }
                }

        }
    }

    public void addProjectile(Projectile projectile) {
        projectileList.add(projectile);
//        System.out.println(projectileList.size());
    }

    public void draw(Graphics2D g2) {
        for (Projectile projectile : projectileList) {
            projectile.draw(g2);
        }
    }
}
