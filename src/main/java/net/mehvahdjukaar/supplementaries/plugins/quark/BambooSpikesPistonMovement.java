package net.mehvahdjukaar.supplementaries.plugins.quark;

import net.mehvahdjukaar.supplementaries.block.blocks.BambooSpikesBlock;
import net.mehvahdjukaar.supplementaries.block.tiles.BambooSpikesBlockTile;
import net.mehvahdjukaar.supplementaries.block.util.IBlockHolder;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.ModList;

import java.util.List;

public class BambooSpikesPistonMovement {

    //called by mixin code
    public static void tick(World world, BlockPos pos, AxisAlignedBB pistonBB, boolean sameDir, TileEntity movingTile){
        List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(null, pistonBB);
        for(Entity entity : list){
            if(entity instanceof PlayerEntity && ((PlayerEntity) entity).isCreative())return;
            if(entity instanceof LivingEntity && entity.isAlive()) {
                AxisAlignedBB entityBB = entity.getBoundingBox();
                if (pistonBB.intersects(entityBB)) {
                    //apply potions using quark moving tiles
                    if (ModList.get().isLoaded("quark") && world!=null) {
                        //get tile
                        TileEntity tile = QuarkPistonPlugin.getMovingTile(pos, world);
                        if (tile instanceof BambooSpikesBlockTile) {
                            //apply effects
                            if(((BambooSpikesBlockTile) tile).interactWithEntity(((LivingEntity) entity),world)){
                                if(movingTile instanceof IBlockHolder) {
                                    //remove last charge and set new blockState
                                    IBlockHolder te = ((IBlockHolder) movingTile);
                                    BlockState state = te.getHeldBlock();
                                    if(state.getBlock() instanceof BambooSpikesBlock) {
                                        te.setHeldBlock(state.with(BambooSpikesBlock.TIPPED,false));
                                    }
                                }
                            }
                            //update tile entity in its list
                            QuarkPistonPlugin.updateMovingTIle(pos,world,tile);
                        }
                    }

                    entity.attackEntityFrom(BambooSpikesBlock.SPIKE_DAMAGE, sameDir ? 3 : 1);

                }
            }
        }

    }
}
