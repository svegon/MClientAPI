package io.github.svegon.mclientapi.client.mixinterface

interface IMerchantScreen {
    var selectedIndex: Int

    var isScrolling: Boolean

    var indexStartOffset: Int

    fun syncSelectedRecipe()
}
