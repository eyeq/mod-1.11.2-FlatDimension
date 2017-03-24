package eyeq.flatdimension.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSlimeWatery extends Block {
    protected static final AxisAlignedBB CARPET_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.0625, 1.0);

    public BlockSlimeWatery() {
        super(Material.CARPET);
        this.setTickRandomly(true);
        this.setSoundType(SoundType.SLIME);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return CARPET_AABB;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    private boolean canBlockStay(World world, BlockPos pos) {
        return !world.isAirBlock(pos.down());
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        return super.canPlaceBlockAt(world, pos) && this.canBlockStay(world, pos);
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
        if(this.canBlockStay(world, pos)) {
            return;
        }
        this.dropBlockAsItem(world, pos, state, 0);
        world.setBlockToAir(pos);
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        entityIn.motionX *= 0.2;
        entityIn.motionZ *= 0.2;
        if(entityIn.motionY > 0.0) {
            entityIn.motionY = 0.0;
        }
    }
}
