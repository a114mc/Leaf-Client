/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/SkidderMC/FDPClient/
 */
package net.nonemc.leaf.features.command.commands

import net.nonemc.leaf.features.command.Command

class UsernameCommand : Command("username", arrayOf("name")) {
    override fun execute(args: Array<String>) {
        alert("Username: " + mc.thePlayer.name)
    }
}