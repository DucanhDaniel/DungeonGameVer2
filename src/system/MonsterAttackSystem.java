package system;

import entities.projectile.Projectile;
import gamestates.Playing;
import java.util.Random;
import java.util.ArrayList;

import enitystates.Attack;
import enitystates.Death;
import utils.Constants;
import entities.monsters.Monster;
public class MonsterAttackSystem {
    Playing playing;
    public MonsterAttackSystem(Playing playing) {
        this.playing = playing;
    }

    private int frameCounter = 0;
    private void monsterAttackLongRange() {
        frameCounter++;
        if (frameCounter < 120) return;
        frameCounter = 0;
        ArrayList<Integer> monsterIds = new ArrayList<>();
        for (int id = 0; id < playing.monsters.size(); id++) {
            monsterIds.add(id);
        }

//        System.out.println(monsterIds.size());

        if (monsterIds.size() != 0) {
            Random random = new Random();
//            int r = random.nextInt(2);
//            if (r != 0) return; // Neu r = 0 thi attack

            int id = random.nextInt(monsterIds.size());
            int monsterId = monsterIds.get(id);
            Monster monster = playing.monsters.get(monsterId);

            monster.getDirectionForAttacking();
            Projectile projectile = new Projectile(playing,"MONSTER", monster);
//            playing.getProjectileManager().addProjectile(projectile);
        }
    }
    public void update() {
        monsterAttackLongRange();
    }
}
