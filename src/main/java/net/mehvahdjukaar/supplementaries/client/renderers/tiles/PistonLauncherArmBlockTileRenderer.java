package net.mehvahdjukaar.supplementaries.client.renderers.tiles;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.mehvahdjukaar.supplementaries.block.blocks.PistonLauncherHeadBlock;
import net.mehvahdjukaar.supplementaries.block.tiles.PistonLauncherArmBlockTile;
import net.mehvahdjukaar.supplementaries.client.renderers.Const;
import net.mehvahdjukaar.supplementaries.setup.Registry;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.model.data.EmptyModelData;


public class PistonLauncherArmBlockTileRenderer extends TileEntityRenderer<PistonLauncherArmBlockTile> {


    public PistonLauncherArmBlockTileRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(PistonLauncherArmBlockTile tile, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn,
                       int combinedOverlayIn) {
        matrixStackIn.push();
        matrixStackIn.translate(0.5, 0.5, 0.5);
        matrixStackIn.rotate(tile.getDirection().getOpposite().getRotation());
        matrixStackIn.rotate(Const.X180);
        matrixStackIn.translate(-0.5, -0.5, -0.5);
        matrixStackIn.translate(0, MathHelper.lerp(partialTicks, tile.prevOffset, tile.offset), 0);
        BlockRendererDispatcher blockRenderer = Minecraft.getInstance().getBlockRendererDispatcher();
        boolean flag1 = tile.getExtending() == tile.age < 2;
        BlockState state = Registry.PISTON_LAUNCHER_HEAD.get().getDefaultState().with(PistonLauncherHeadBlock.FACING, Direction.UP).with(BlockStateProperties.SHORT, flag1);
        //RendererUtil.renderBlockPlus(state, matrixStackIn, bufferIn, blockRenderer, tile.getWorld(), tile.getPos());
        blockRenderer.renderBlock(state, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, EmptyModelData.INSTANCE);
        matrixStackIn.pop();
    }
}