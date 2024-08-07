package io.github.svegon.capi.mixininterface

import net.minecraft.client.util.SelectionManager

interface IAbstractSignEditScreen {
    fun getBlockEntity(): SignBlockEntity?

    fun setBlockEntity(blockEntity: SignBlockEntity?)

    fun getSignText(): SignText?

    fun setSignText(text: SignText?)

    var text: Array<String?>?

    fun front(): Boolean

    fun front(front: Boolean)

    fun getSignType(): WoodType?

    fun setSignType(signType: WoodType?)

    fun currentRow(): Int

    fun currentRow(row: Int)

    fun getSelectionManager(): SelectionManager?

    fun setSelectionManager(selectionManager: SelectionManager?)
}
