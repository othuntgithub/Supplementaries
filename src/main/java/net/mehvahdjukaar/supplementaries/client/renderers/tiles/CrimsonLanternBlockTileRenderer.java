package net.mehvahdjukaar.supplementaries.client.renderers.tiles;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.mehvahdjukaar.supplementaries.block.blocks.OilLanternBlock;
import net.mehvahdjukaar.supplementaries.block.tiles.OilLanternBlockTile;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.state.properties.AttachFace;


public class CrimsonLanternBlockTileRenderer extends EnhancedLanternBlockTileRenderer<OilLanternBlockTile> {
    public CrimsonLanternBlockTileRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(OilLanternBlockTile tile, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn,
                       int combinedOverlayIn) {
        AttachFace face = tile.getBlockState().get(OilLanternBlock.FACE);
        if(face==AttachFace.FLOOR)return;
        matrixStackIn.push();
        matrixStackIn.translate(0,-0.0625,0);
        BlockState state = tile.getBlockState().getBlock().getDefaultState();
        this.renderLantern(tile,state,partialTicks,matrixStackIn,bufferIn,combinedLightIn,combinedOverlayIn,face==AttachFace.CEILING);
        matrixStackIn.pop();

    }
}