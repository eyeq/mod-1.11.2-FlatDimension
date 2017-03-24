package eyeq.flatdimension.block;

import java.util.Random;

import eyeq.util.block.portal.UPortalSize;
import eyeq.util.entity.EntityUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import eyeq.flatdimension.FlatDimension;

public class BlockFlatPortal extends BlockPortal {
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
        UPortalSize size = new UPortalSize(world, pos, Axis.X, FlatDimension.flatPortalGate);
        if(size.isValid() && size.isFull()) {
            return;
        }
        size = new UPortalSize(world, pos, Axis.Z, FlatDimension.flatPortalGate);
        if(size.isValid() && size.isFull()) {
            return;
        }
        world.setBlockToAir(pos);
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        if(entity.isRiding() || entity.isBeingRidden() || !entity.isNonBoss()) {
            return;
        }
        int dimension = FlatDimension.dimensionFlat.getId();
        double scale = 1 / 64.0;
        if(entity.dimension == dimension) {
            dimension = 0;
            scale = 64.0;
        }
        EntityUtils.travel(entity, pos, dimension, FlatDimension.flatPortalGate, scale);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        double xCoord = pos.getX() + rand.nextFloat();
        double yCoord = pos.getY() + rand.nextFloat();
        double zCoord = pos.getZ() + rand.nextFloat();
        double xSpeed = (rand.nextFloat() - 0.5) * 0.5;
        double ySpeed = (rand.nextFloat() - 0.5) * 0.5;
        double zSpeed = (rand.nextFloat() - 0.5) * 0.5;
        int j = rand.nextInt(2) * 2 - 1;
        if(world.getBlockState(pos.west()).getBlock() != this && world.getBlockState(pos.east()).getBlock() != this) {
            xCoord = pos.getX() + 0.5 + 0.25 * j;
            xSpeed = rand.nextFloat() * 2.0 * j;
        } else {
            zCoord = pos.getZ() + 0.5 + 0.25 * j;
            zSpeed = rand.nextFloat() * 2.0 * j;
        }
        world.spawnParticle(EnumParticleTypes.CLOUD, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed);
    }
}
