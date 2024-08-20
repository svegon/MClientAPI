package io.github.svegon.mclientapi

import net.minecraft.network.PacketByteBuf

object MinecraftConstants {
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
     * if this is exceeded, any written book component with this title cannot be encoded/decoded
     */
    const val MAX_TITLE_LENGTH_ENCODABLE: Int = 32

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
     * maximum number of characters in a single chat message (not command)
     */
    const val MAX_CHAT_MESSAGE_LENGTH = 256

    /**
     * maximum number of characters in a single command string (not chat message)
     */
    const val MAX_COMMAND_LENGTH = PacketByteBuf.MAX_TEXT_LENGTH

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
}
