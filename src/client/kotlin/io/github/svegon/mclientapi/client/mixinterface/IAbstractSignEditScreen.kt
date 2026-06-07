package io.github.svegon.mclientapi.client.mixinterface

import net.minecraft.client.gui.font.TextFieldHelper
import net.minecraft.world.level.block.entity.SignBlockEntity
import net.minecraft.world.level.block.entity.SignText
import net.minecraft.world.level.block.state.properties.WoodType

interface IAbstractSignEditScreen {
    var blockEntity: SignBlockEntity

    var signText: SignText

    var text: Array<String>

    var front: Boolean

    var signType: WoodType

    var currentRow: Int

    var selectionManager: TextFieldHelper?
}
