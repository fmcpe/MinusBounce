/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.utils.block

import net.minecraft.block.Block
import net.minecraft.block.BlockAir
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.util.*
import net.minusmc.minusbounce.injection.access.StaticStorage
import net.minusmc.minusbounce.utils.MinecraftInstance
import net.minusmc.minusbounce.utils.Rotation
import net.minusmc.minusbounce.utils.RotationUtils
import net.minusmc.minusbounce.utils.extensions.plus
import net.minusmc.minusbounce.utils.extensions.times
import kotlin.math.*


object BlockUtils : MinecraftInstance() {

    /**
     * Get block from [blockPos]
     */
    @JvmStatic
    fun getBlock(blockPos: BlockPos?): Block? = mc.theWorld?.getBlockState(blockPos)?.block

    fun blockRelativeToPlayer(offsetX: Double, offsetY: Double, offsetZ: Double): Block {
        val playerPos = BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ)
        val offsetPos = playerPos.add(offsetX.toInt(), offsetY.toInt(), offsetZ.toInt())
        return mc.theWorld.getBlockState(offsetPos).block
    }

    @JvmStatic
    fun getBlock(vec3: Vec3): Block? = getBlock(BlockPos(vec3.xCoord, vec3.yCoord, vec3.zCoord))

    /**
     * Get material from [blockPos]
     */
    @JvmStatic
    fun getMaterial(blockPos: BlockPos?): Material? = getBlock(blockPos)?.material

    /**
     * Check [blockPos] is replaceable
     */
    @JvmStatic
    fun isReplaceable(blockPos: BlockPos?) = getMaterial(blockPos)?.isReplaceable ?: false

    /**
     * Get state from [blockPos]
     */
    @JvmStatic
    fun getState(blockPos: BlockPos?): IBlockState = mc.theWorld.getBlockState(blockPos)

    /**
     * Check if [blockPos] is clickable
     */
    @JvmStatic
    fun isClickable(blockPos: BlockPos?) = getBlock(blockPos)?.canCollideCheck(getState(blockPos), false) ?: false &&
            mc.theWorld.worldBorder.contains(blockPos)

    fun air(pos: BlockPos) = getBlock(pos) is BlockAir
    fun air(pos: Vec3) = getBlock(pos) is BlockAir
    fun air(x: Double, y: Double, z: Double) = getBlock(BlockPos(x, y, z)) is BlockAir
    /**
     * Get block name by [id]
     */
    @JvmStatic
    fun getBlockName(id: Int): String = Block.getBlockById(id).localizedName

    /**
     * Check if block is full block
     */
    @JvmStatic
    fun isFullBlock(blockPos: BlockPos?): Boolean {
        val axisAlignedBB = getBlock(blockPos)?.getCollisionBoundingBox(mc.theWorld, blockPos, getState(blockPos))
                ?: return false
        return axisAlignedBB.maxX - axisAlignedBB.minX == 1.0 && axisAlignedBB.maxY - axisAlignedBB.minY == 1.0 && axisAlignedBB.maxZ - axisAlignedBB.minZ == 1.0
    }

    /**
     * Get distance to center of [blockPos]
     */
    @JvmStatic
    fun getCenterDistance(blockPos: BlockPos) =
            mc.thePlayer.getDistance(blockPos.x + 0.5, blockPos.y + 0.5, blockPos.z + 0.5)

    /**
     * Check if [axisAlignedBB] has collidable blocks using custom [collide] check
     */
    @JvmStatic
    fun collideBlock(axisAlignedBB: AxisAlignedBB, collide: (Block?) -> Boolean): Boolean {
        for (x in MathHelper.floor_double(mc.thePlayer.entityBoundingBox.minX) until
                MathHelper.floor_double(mc.thePlayer.entityBoundingBox.maxX) + 1) {
            for (z in MathHelper.floor_double(mc.thePlayer.entityBoundingBox.minZ) until
                    MathHelper.floor_double(mc.thePlayer.entityBoundingBox.maxZ) + 1) {
                val block = getBlock(BlockPos(x.toDouble(), axisAlignedBB.minY, z.toDouble()))

                if (!collide(block))
                    return false
            }
        }

        return true
    }

    /**
     * Check if [axisAlignedBB] has collidable blocks using custom [collide] check
     */
    @JvmStatic
    fun collideBlockIntersects(axisAlignedBB: AxisAlignedBB, collide: (Block?) -> Boolean): Boolean {
        for (x in MathHelper.floor_double(mc.thePlayer.entityBoundingBox.minX) until
                MathHelper.floor_double(mc.thePlayer.entityBoundingBox.maxX) + 1) {
            for (z in MathHelper.floor_double(mc.thePlayer.entityBoundingBox.minZ) until
                    MathHelper.floor_double(mc.thePlayer.entityBoundingBox.maxZ) + 1) {
                val blockPos = BlockPos(x.toDouble(), axisAlignedBB.minY, z.toDouble())
                val block = getBlock(blockPos)

                if (collide(block)) {
                    val boundingBox = block?.getCollisionBoundingBox(mc.theWorld, blockPos, getState(blockPos))
                            ?: continue

                    if (mc.thePlayer.entityBoundingBox.intersectsWith(boundingBox))
                        return true
                }
            }
        }
        return false
    }

    @JvmStatic
    fun floorVec3(vec3: Vec3) = Vec3(floor(vec3.xCoord),floor(vec3.yCoord),floor(vec3.zCoord))

    private val blockNames = mutableListOf<Pair<String, Int>>()

    /**
     * Checking if the rotation is correct from blockPos and facing.
     * 
     * @author fmcpe
     */
    @JvmOverloads
    fun rayTrace(
        pos: BlockPos?,
        facing: EnumFacing,
        obj: MovingObjectPosition? = mc.objectMouseOver
    ): Boolean {
        obj ?: return false
        pos ?: return false

        return obj.sideHit == facing && obj.blockPos == pos
    }

    /**
     * Thanks!
     *
     * I don't know ?
     * @author fmcpe
     * @author MWHunter
     */
    fun calculateDirection(rotation: Rotation): Vec3 {
        val rotX = rotation.yaw * Math.PI / 180f
        val rotY = rotation.pitch * Math.PI / 180f

        return Vec3(-cos(rotY) * sin(rotX), -sin(rotY), cos(rotY) * cos(rotX))
    }

    fun getPointAtDistance(direction: Vec3, origin: Vec3, distance: Double): Vec3 {
        val dir = Vec3(direction.xCoord, direction.yCoord, direction.zCoord)
        val orig = Vec3(origin.xCoord, origin.yCoord, origin.zCoord)
        return orig + (dir * distance)
    }

    /**
     * Raytrace from a rotation.
     * 
     * @author fmcpe
     */
    @JvmOverloads
    fun distanceRayTrace(rotation: Rotation?, range: Float = mc.playerController.blockReachDistance): MovingObjectPosition? {
        rotation ?: return mc.objectMouseOver

        val vec = RotationUtils.getVectorForRotation(rotation)
        val vector = eyesPos.addVector(vec.xCoord * range, vec.yCoord * range, vec.zCoord * range)
        return mc.theWorld.rayTraceBlocks(eyesPos, vector, false, false, true)
    }
    
    fun getEnumFacing(position: Vec3): PlaceInfo? {
        var placeRotation: PlaceInfo? = null

        for (side in StaticStorage.facings()) {
            val neighbor = BlockPos(position).offset(side)

            if (isClickable(neighbor)) {
                placeRotation = PlaceInfo(neighbor, side.opposite)
            }
        }
        return placeRotation
    }

    fun block(x: Double, y: Double, z: Double): Block {
        return mc.theWorld.getBlockState(BlockPos(x, y, z)).block
    }

    fun getPlacePossibility(offsetX: Double, offsetY: Double, offsetZ: Double): Vec3? {
        val possibilities = mutableListOf<Vec3>()
        val range = (5 + (abs(offsetX) + abs(offsetZ))).toInt()

        for (x in -range..range) {
            for (y in -range..range) {
                for (z in -range..range) {
                    val block = blockRelativeToPlayer(x, y, z)
                    if (block is BlockAir) continue
                    possibilities.add(Vec3(mc.thePlayer.posX + x - 1, mc.thePlayer.posY + y, mc.thePlayer.posZ + z))
                    possibilities.add(Vec3(mc.thePlayer.posX + x + 1, mc.thePlayer.posY + y, mc.thePlayer.posZ + z))
                    possibilities.add(Vec3(mc.thePlayer.posX + x, mc.thePlayer.posY + y - 1, mc.thePlayer.posZ + z))
                    possibilities.add(Vec3(mc.thePlayer.posX + x, mc.thePlayer.posY + y + 1, mc.thePlayer.posZ + z))
                    possibilities.add(Vec3(mc.thePlayer.posX + x, mc.thePlayer.posY + y, mc.thePlayer.posZ + z - 1))
                    possibilities.add(Vec3(mc.thePlayer.posX + x, mc.thePlayer.posY + y, mc.thePlayer.posZ + z + 1))
                }
            }
        }
        possibilities.removeIf {
            mc.thePlayer.getDistance(it.xCoord, it.yCoord, it.zCoord) > 5 ||
                    mc.theWorld.getBlockState(BlockPos(it.xCoord, it.yCoord, it.zCoord)).block !is BlockAir
        }

        return possibilities.minByOrNull {
            val d0 = mc.thePlayer.posX + offsetX - it.xCoord
            val d1 = mc.thePlayer.posY - 1 + offsetY - it.yCoord
            val d2 = mc.thePlayer.posZ + offsetZ - it.zCoord
            sqrt(d0 * d0 + d1 * d1 + d2 * d2)
        }
    }
    fun blockRelativeToPlayer(offsetX: Int, offsetY: Int, offsetZ: Int) = blockRelativeToPlayer(offsetX.toDouble(), offsetY.toDouble(), offsetZ.toDouble())
}

