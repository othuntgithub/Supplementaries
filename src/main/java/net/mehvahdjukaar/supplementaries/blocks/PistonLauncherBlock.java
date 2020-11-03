package net.mehvahdjukaar.supplementaries.blocks;

import net.mehvahdjukaar.supplementaries.setup.Registry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class PistonLauncherBlock extends Block {
    public static final DirectionProperty FACING = DirectionalBlock.FACING;
    public static final BooleanProperty EXTENDED = BlockStateProperties.EXTENDED; // is base only?
    public PistonLauncherBlock(Properties properties){
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(EXTENDED, false));
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return true;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, EXTENDED);
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        if (state.get(EXTENDED)) {
            switch (state.get(FACING)) {
                case SOUTH :
                default :
                    return VoxelShapes.create(1D, 0D, 0.812D, 0D, 1D, 0D);
                case NORTH :
                    return VoxelShapes.create(0D, 0D, 0.188D, 1D, 1D, 1D);
                case WEST :
                    return VoxelShapes.create(0.188D, 0D, 1D, 1D, 1D, 0D);
                case EAST :
                    return VoxelShapes.create(0.812D, 0D, 0D, 0D, 1D, 1D);
                case UP :
                    return VoxelShapes.create(0D, 0.812D, 0D, 1D, 0D, 1D);
                case DOWN :
                    return VoxelShapes.create(0D, 0.188D, 1D, 1D, 1D, 0D);
            }
        } else {
            switch (state.get(FACING)) {
                case SOUTH :
                default :
                    return VoxelShapes.or(VoxelShapes.create(1D, 0D, 0.812D, 0D, 1D, 0D), VoxelShapes.create(1D, 0D, 1D, 0D, 1D, 0.875D),
                            VoxelShapes.create(0.9375D, 0.062D, 0.8125D, 0.0625D, 0.9375D, 0.875D));
                case NORTH :
                    return VoxelShapes.or(VoxelShapes.create(0D, 0D, 0.188D, 1D, 1D, 1D), VoxelShapes.create(0D, 0D, 0D, 1D, 1D, 0.125D),
                            VoxelShapes.create(0.0625D, 0.062D, 0.1875D, 0.9375D, 0.9375D, 0.125D));
                case WEST :
                    return VoxelShapes.or(VoxelShapes.create(0.188D, 0D, 1D, 1D, 1D, 0D), VoxelShapes.create(0D, 0D, 1D, 0.125D, 1D, 0D),
                            VoxelShapes.create(0.1875D, 0.062D, 0.9375D, 0.125D, 0.9375D, 0.0625D));
                case EAST :
                    return VoxelShapes.or(VoxelShapes.create(0.812D, 0D, 0D, 0D, 1D, 1D), VoxelShapes.create(1D, 0D, 0D, 0.875D, 1D, 1D),
                            VoxelShapes.create(0.8125D, 0.062D, 0.0625D, 0.875D, 0.9375D, 0.9375D));
                case UP :
                    return VoxelShapes.or(VoxelShapes.create(0D, 0.812D, 0D, 1D, 0D, 1D), VoxelShapes.create(0D, 1D, 0D, 1D, 0.875D, 1D),
                            VoxelShapes.create(0.0625D, 0.8125D, 0.062D, 0.9375D, 0.875D, 0.9375D));
                case DOWN :
                    return VoxelShapes.or(VoxelShapes.create(0D, 0.188D, 1D, 1D, 1D, 0D), VoxelShapes.create(0D, 0D, 1D, 1D, 0.125D, 0D),
                            VoxelShapes.create(0.0625D, 0.1875D, 0.938D, 0.9375D, 0.125D, 0.0625D));
            }
            // return VoxelShapes.create(0D, 0D, 0D, 1D, 0.5D, 1D);
        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        this.checkForMove(state, worldIn, pos);
    }

    public void checkForMove(BlockState state, World world, BlockPos pos) {
        if (!world.isRemote()) {
            boolean flag = this.shouldBeExtended(world, pos, state.get(FACING));
            BlockPos _bp = pos.add(state.get(FACING).getDirectionVec());
            if (flag && !state.get(EXTENDED)) {
                boolean flag2 = false;
                BlockState targetblock = world.getBlockState(_bp);
                if (targetblock.getPushReaction() == PushReaction.DESTROY || targetblock.isAir()) {
                    TileEntity tileentity = targetblock.hasTileEntity() ? world.getTileEntity(_bp) : null;
                    spawnDrops(targetblock, world, _bp, tileentity);
                    flag2 = true;
                }
                /*
                 * else if (targetblock.getBlock() instanceof FallingBlock &&
                 * world.getBlockState(_bp.add(state.get(FACING).getDirectionVec())).isAir(
                 * world, _bp)){ FallingBlockEntity fallingblockentity = new
                 * FallingBlockEntity(world, (double)_bp.getX() + 0.5D, (double)_bp.getY() ,
                 * (double)_bp.getZ() + 0.5D, world.getBlockState(_bp));
                 *
                 * world.addEntity(fallingblockentity); flag2=true; }
                 */
                if (flag2) {
                    world.setBlockState(_bp,
                            Registry.PISTON_LAUNCHER_ARM.get().getDefaultState().with(PistonLauncherArmBlock.EXTENDING, true).with(FACING, state.get(FACING)),
                            3);
                    world.setBlockState(pos, state.with(EXTENDED, true));
                    world.playSound(null, pos, SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.BLOCKS, 0.53F,
                            world.rand.nextFloat() * 0.25F + 0.45F);
                }
            } else if (!flag && state.get(EXTENDED)) {
                BlockState bs = world.getBlockState(_bp);
                if (bs.getBlock() instanceof PistonLauncherHeadBlock && state.get(FACING) == bs.get(FACING)) {
                    // world.setBlockState(_bp, Blocks.AIR.getDefaultState(), 3);
                    world.setBlockState(_bp,
                            Registry.PISTON_LAUNCHER_ARM.get().getDefaultState().with(PistonLauncherArmBlock.EXTENDING, false).with(FACING, state.get(FACING)),
                            3);
                    world.playSound(null, pos, SoundEvents.BLOCK_PISTON_CONTRACT, SoundCategory.BLOCKS, 0.53F,
                            world.rand.nextFloat() * 0.15F + 0.45F);
                } else if (bs.getBlock() instanceof  PistonLauncherArmBlock
                        && state.get(FACING) == bs.get(FACING)) {
                    if (world.getTileEntity(_bp) instanceof PistonLauncherArmBlockTile) {
                        world.getPendingBlockTicks().scheduleTick(pos, world.getBlockState(pos).getBlock(), 1);
                    }
                }
            }
        }
    }

    // piston code
    private boolean shouldBeExtended(World worldIn, BlockPos pos, Direction facing) {
        for (Direction direction : Direction.values()) {
            if (direction != facing && worldIn.isSidePowered(pos.offset(direction), direction)) {
                return true;
            }
        }
        if (worldIn.isSidePowered(pos, Direction.DOWN)) {
            return true;
        } else {
            BlockPos blockpos = pos.up();
            for (Direction direction1 : Direction.values()) {
                if (direction1 != Direction.DOWN && worldIn.isSidePowered(blockpos.offset(direction1), direction1)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block neighborBlock, BlockPos fromPos, boolean moving) {
        super.neighborChanged(state, world, pos, neighborBlock, fromPos, moving);
        this.checkForMove(state, world, pos);
    }
}