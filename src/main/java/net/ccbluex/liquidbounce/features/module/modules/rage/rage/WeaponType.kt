package net.ccbluex.liquidbounce.features.module.modules.rage.rage

import net.minecraft.init.Items
import net.minecraft.item.Item

enum class WeaponType(val items: List<Item>) {
    AWP(listOf(Items.golden_hoe)),
    RIFLE(listOf(Items.iron_hoe, Items.stone_hoe, Items.stone_shovel, Items.diamond_shovel)),
    AK(listOf(Items.stone_hoe)),
    M4(listOf(Items.iron_hoe)),
    MP7(listOf(Items.stone_shovel)),
    P250(listOf(Items.wooden_pickaxe)),
    DEAGLE(listOf(Items.golden_pickaxe)),
    SHOTGUN(listOf(Items.diamond_shovel));
}