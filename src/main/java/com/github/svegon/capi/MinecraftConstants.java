/*
 * Copyright (c) 2021-2021 Svegon and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package com.github.svegon.capi;

public final class MinecraftConstants {
    /**
     * maximum number of pages in a writable/written book
     */
    public static final int MAX_PAGE_COUNT = 100;
    /**
     * maximum number of characters in a page of a writable/written book
     */
    public static final int MAX_PAGE_LENGTH = 8192;
    /**
     * maximum number of characters in a book's title you can write in vanilla
     * if this is exceeded, MC will deny display the title at all
     */
    public static final int MAX_TITLE_LENGTH_READABLE = 32;
    /**
     * maximum number of characters in title accepted by server
     */
    public static final int MAX_TITLE_LENGTH = 128;
    public static final float CLIENT_REACH_DISTANCE = 4.5F;
    public static final double DEFAULT_REACH_DISTANCE = 4.25;
    /**
     * maximum reach distance accepted by vanilla server
     */
    public static final double SERVER_REACH_DISTANCE = 6;
    /**
     * maximum vanilla reach distance for full block operations
     */
    public static final int BLOCK_REACH_DISTANCE = 5;
    public static final double SQUARED_DEFAULT_REACH_DISTANCE = 18.0625;
    public static final double SQUARED_SERVER_REACH_DISTANCE = 36;
    public static final int SQUARED_BLOCK_REACH_DISTANCE = 25;
    /**
     * maximum number of rows in a sign's text
     */
    public static final int SIGN_TEXT_HEIGHT = 4;
    /**
     * maximum number of characters in a sign's row
     */
    public static final int SIGN_TEXT_WIDTH = 384;
    /**
     * status effect duration applied when none is specified by a command
     */
    public static final int POTION_EFFECT_DEFAULT_DURATION = 600;
    public static final String POTION_EFFECTS_NBT_KEY = "CustomPotionEffects";
    public static final String POTION_EFFECT_AMPLIFIER_KEY = "Amplifier";
    public static final String POTION_EFFECT_DURATION_KEY = "Duration";
    public static final String POTION_EFFECT_ID_KEY = "Id";
    public static final String FIREWORK_ENTITY_TAG = "Fireworks";
    public static final String FIREWORKS_EXPLOSIONS_TAG = "Explosions";

    private MinecraftConstants() {
        throw new UnsupportedOperationException();
    }
}
