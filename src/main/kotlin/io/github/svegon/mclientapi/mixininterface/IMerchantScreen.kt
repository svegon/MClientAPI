package io.github.svegon.capi.mixininterface

interface IMerchantScreen {
    var selectedIndex: Int

    var isScrolling: Boolean

    var indexStartOffset: Int

    fun syncSelectedRecipe()
}
