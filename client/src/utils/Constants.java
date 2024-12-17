package utils;

import entities.projectile.Projectile;

public class Constants {
    public static class Screen {
        public static final int ORIGINAL_TILE_SIZE = 16; // 16x16 pixels
        public static int SCALE = 3;
        public static int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE; // 48x48 pixels
        public static final int MAX_SCREEN_COL = 16; // 16 tiles
        public static final int MAX_SCREEN_ROW = 12;
        public static final int SCREEN_WIDTH = TILE_SIZE * MAX_SCREEN_COL; // 768 pixels
        public static final int SCREEN_HEIGHT = TILE_SIZE * MAX_SCREEN_ROW; // 576 pixels

        public static final int FPS_SET = 60;
        public static final int UPS_SET = 60;

        public static int MAX_WORLD_COL = 50;
        public static int MAX_WORLD_ROW = 50;
        public static final int WORLD_WIDTH = TILE_SIZE * MAX_WORLD_COL;
        public static final int WORLD_HEIGHT = TILE_SIZE * MAX_WORLD_ROW;

        public static final int SCREEN_X = SCREEN_WIDTH/2 - (TILE_SIZE/2);
        public static final int SCREEN_Y = SCREEN_HEIGHT/2- (TILE_SIZE/2);
    }
    public static class Player {
        public static int PLAYER_SCREEN_X = Screen.SCREEN_WIDTH/2 - Screen.TILE_SIZE/2 - 16 * Screen.SCALE;
        public static int PLAYER_SCREEN_Y = Screen.SCREEN_HEIGHT/2 - Screen.TILE_SIZE/2 - 16 * Screen.SCALE;
        public static final int PLAYER_IMAGE_WIDTH = Screen.TILE_SIZE*3;
        public static final int PLAYER_IMAGE_HEIGHT = Screen.TILE_SIZE*4;
    }

    public class Monster {
        // --------------------- Projectile ----------------------------
        public class Projectile {
            public static final int WIDTH = Screen.TILE_SIZE;
            public static final int HEIGHT = Screen.TILE_SIZE;
            public static final int TOTAL_FRAME = 5;
            public static final int FRAME_DURATION = 3;
            public static final int SPEED = 5;
            public static final int ATTACK_POINTS = 1;
            public static final int EXPLOSION_TOTAL_FRAME = 3;
            public static final int EXPLOSION_FRAME_DURATION = 5;

        }

        // --------------------------------------------------------------
    }

    public class Object {
        public class ObjHeart {
            public static final int WIDTH = Screen.TILE_SIZE;
            public static final int HEIGHT = Screen.TILE_SIZE * 2;
        }

        public class ObjMana {
            public static final int WIDTH = Screen.TILE_SIZE;
            public static final int HEIGHT = Screen.TILE_SIZE * 2;
        }
    }

}
