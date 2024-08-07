/*
 * Copyright (c) 2021-2021 Svegon and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package io.github.svegon.mclientapi

class MinecraftConstants private constructor() {
    init {
        throw UnsupportedOperationException()
    }

    companion object {
        /**
         * maximum number of pages in a writable/written book
         */
        const val MAX_PAGE_COUNT: Int = 100

        /**
         * maximum number of characters in a page of a writable/written book
         */
        const val MAX_PAGE_LENGTH: Int = 8192

        /**
         * maximum number of characters in a book's title you can write in vanilla
         * if this is exceeded, MC will deny display the title at all
         */
        const val MAX_TITLE_LENGTH_READABLE: Int = 32

        /**
         * maximum number of characters in title accepted by server
         */
        const val MAX_TITLE_LENGTH: Int = 128
        const val CLIENT_REACH_DISTANCE: Float = 4.5f
        const val DEFAULT_REACH_DISTANCE: Double = 4.25

        /**
         * maximum reach distance accepted by vanilla server
         */
        const val SERVER_REACH_DISTANCE: Double = 6.0

        /**
         * maximum vanilla reach distance for full block operations
         */
        const val BLOCK_REACH_DISTANCE: Int = 5
        const val SQUARED_DEFAULT_REACH_DISTANCE: Double = 18.0625
        const val SQUARED_SERVER_REACH_DISTANCE: Double = 36.0
        const val SQUARED_BLOCK_REACH_DISTANCE: Int = 25

        /**
         * maximum number of rows in a sign's text
         */
        const val SIGN_TEXT_HEIGHT: Int = 4

        /**
         * maximum number of characters in a sign's row
         */
        const val SIGN_TEXT_WIDTH: Int = 384

        /**
         * status effect duration applied when none is specified by a command
         */
        const val POTION_EFFECT_DEFAULT_DURATION: Int = 600
        const val POTION_EFFECTS_NBT_KEY: String = "CustomPotionEffects"
        const val POTION_EFFECT_AMPLIFIER_KEY: String = "Amplifier"
        const val POTION_EFFECT_DURATION_KEY: String = "Duration"
        const val POTION_EFFECT_ID_KEY: String = "Id"
        const val FIREWORK_ENTITY_TAG: String = "Fireworks"
        const val FIREWORKS_EXPLOSIONS_TAG: String = "Explosions"
    }
}
