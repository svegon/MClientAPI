package io.github.svegon.mclientapi.client.mixinterface

interface IMerchantScreen {
    var `mClientAPI$selectedOffer`: Int

    var isScrolling: Boolean

    var indexStartOffset: Int

    fun syncSelectedRecipe()
}
