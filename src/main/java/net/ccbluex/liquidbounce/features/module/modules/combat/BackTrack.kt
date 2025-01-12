//All the code was written by N0ne.
package net.ccbluex.liquidbounce.features.module.modules.combat

import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.network.play.server.S14PacketEntity
import net.minecraft.network.play.server.S32PacketConfirmTransaction
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.Vec3
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.util.concurrent.ConcurrentHashMap

@ModuleInfo(name = "Backtrack", category = ModuleCategory.COMBAT)
object Backtrack : Module() {

    private val delay = IntegerValue("Delay", 100, 1, 1000)
    private val maxClientDistance: FloatValue = FloatValue("MaxClientSideDistance", 3f, 0.0f, 6f)
    private val minClientDistance = FloatValue("MinClientSideDistance", 1.0f, 0.0f, 6f)
    private val maxServerDistance: FloatValue = FloatValue("MaxServerSideDistance", 3f, 0.0f, 6f)
    private val minServerDistance = FloatValue("MinServerSideDistance", 1.0f, 0.0f, 6f)
    private val ignoreSmoothing = BoolValue("IgnoreSmoothing", true)
    private val onlyPlayerMove = BoolValue("OnlyPlayerMove", false)
    private val playerMovementIncludesJumping = BoolValue("PlayerMovementIncludesJumping", false)
    private val onlyTargetMove = BoolValue("OnlyTargetMove", false)
    private val targetMovementIncludesJumping = BoolValue("TargetMovementIncludesJumping", false)
    private val minPlayerHurtTime = IntegerValue("MinPlayerHurtTime", 0, 0, 10)
    private val maxPlayerHurtTime = IntegerValue("MaxPlayerHurtTime", 10, 0, 10)
    private val minTargetHurtTime = IntegerValue("MinTargetHurtTime", 0, 0, 10)
    private val maxTargetHurtTime = IntegerValue("MaxTargetHurtTime", 10, 0, 10)
    private val lastPositions = ConcurrentHashMap<EntityPlayer, Vec3>()
    private val delayTimers = ConcurrentHashMap<EntityPlayer, MSTimer>()
    private val freeze = ConcurrentHashMap<EntityPlayer, Boolean>()
    private var keepPos = false
    private var targetPlayer: EntityPlayer? = null
    override fun onDisable() {
        keepPos = false
        delayTimers.clear()
        freeze.clear()
        lastPositions.clear()
        targetPlayer = null
    }
    @EventTarget
    fun onAttack(event: AttackEvent) {
        val player = mc.thePlayer?:return
        val target = event.targetEntity as EntityPlayer
        val clientDistance = player.getDistanceToEntity(target)
        val serverDistance = player.getDistance(target.serverPosX.toDouble()/32,
            target.serverPosY.toDouble()/32,target.serverPosZ.toDouble()/32)
        if (clientDistance in minClientDistance.get()..maxClientDistance.get()
            && serverDistance in minServerDistance.get()..maxServerDistance.get() && !freeze.getOrDefault(target, false)) {
            lastPositions[target] = Vec3(target.posX, target.posY, target.posZ)
            if (!delayTimers.containsKey(target)) delayTimers[target] = MSTimer()
            delayTimers[target]?.reset()
            freeze[target] = true
            keepPos = true
        }
    }
    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        val player = mc.thePlayer?:return
        val iterator = lastPositions.iterator()
        while (iterator.hasNext()) {
            val (target, lastPos) = iterator.next()
            val distance = player.getDistanceToEntity(target)
            val serverDistance = player.getDistance(target.serverPosX.toDouble()/32,
                target.serverPosY.toDouble()/32,target.serverPosZ.toDouble()/32)
            if (distance !in minClientDistance.get()..maxClientDistance.get() && serverDistance !in minServerDistance.get()..maxServerDistance.get()) {
                freeze.remove(target)
                lastPositions.clear()
                keepPos = false
                continue
            }

            val timer = delayTimers[target]
            if (timer != null && timer.hasTimePassed(delay.get().toLong())) {
                freeze.clear()
                lastPositions.clear()
                targetPlayer = mc.thePlayer
                keepPos = false
                continue
            } else {
                keepPos = true
               if (allow(target)) target.setPositionAndUpdate(lastPos.xCoord, lastPos.yCoord, lastPos.zCoord)
                targetPlayer = target
            }
        }
    }
    @EventTarget
    fun onRender3D(event: Render3DEvent) {
       if (ignoreSmoothing.get()) runSetTargetPos()
    }
    private fun allow(it: EntityPlayer):Boolean {
        return (!onlyPlayerMove.get() || isPlayerMoving()) && (!onlyTargetMove.get() || isTargetMoving(it)) && targetInHurtTime(it) && playerInHurtTime()
    }
    private fun targetInHurtTime(it: EntityPlayer):Boolean{
        return it.hurtTime in minTargetHurtTime.get()..maxTargetHurtTime.get()
    }
    private fun playerInHurtTime():Boolean{
        return mc.thePlayer != null && mc.thePlayer.hurtTime in minPlayerHurtTime.get()..maxPlayerHurtTime.get()
    }
    private fun isPlayerMoving(): Boolean {
        return mc.thePlayer != null && (mc.thePlayer.movementInput.moveForward != 0f || mc.thePlayer.movementInput.moveStrafe != 0f || (!playerMovementIncludesJumping.get() || mc.thePlayer.movementInput.jump))
    }
    private fun isTargetMoving(it:EntityPlayer): Boolean {
        return it.posX - it.prevPosX != 0.0 || it.posZ - it.prevPosZ != 0.0 || (!targetMovementIncludesJumping.get() || it.posY - it.prevPosY != 0.0)
    }
    private fun runSetTargetPos() {
        val iterator = lastPositions.iterator()
        while (iterator.hasNext()) {
            val (target, lastPos) = iterator.next()
            if (keepPos && allow(target)) target.setPositionAndUpdate(lastPos.xCoord, lastPos.yCoord, lastPos.zCoord) else continue
        }
    }
}
