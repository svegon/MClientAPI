package io.github.svegon.mclientapi.client.mixinterface

import net.minecraft.block.WoodType
import net.minecraft.block.entity.SignBlockEntity
import net.minecraft.block.entity.SignText
import net.minecraft.client.util.SelectionManager

interface IAbstractSignEditScreen {
    var blockEntity: SignBlockEntity

    var signText: SignText

    var text: Array<String>

    var front: Boolean

    var signType: WoodType

    var currentRow: Int

    var selectionManager: SelectionManager?
}
