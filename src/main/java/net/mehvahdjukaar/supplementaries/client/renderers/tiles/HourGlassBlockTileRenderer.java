package net.mehvahdjukaar.supplementaries.client.renderers.tiles;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.mehvahdjukaar.supplementaries.block.blocks.HourGlassBlock;
import net.mehvahdjukaar.supplementaries.block.tiles.HourGlassBlockTile;
import net.mehvahdjukaar.supplementaries.client.renderers.RendererUtil;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;


public class HourGlassBlockTileRenderer extends TileEntityRenderer<HourGlassBlockTile> {
    public HourGlassBlockTileRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }


    public static void renderSand(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn,
                                   int combinedOverlayIn, TextureAtlasSprite sprite, float height, Direction dir){


        IVertexBuilder builder = bufferIn.getBuffer(RenderType.getSolid());
        int color = 0xffffff;
        matrixStackIn.push();
        matrixStackIn.translate(0.5,0.5,0.5);
        Quaternion q = dir.getRotation();
        matrixStackIn.rotate(q);

        q.conjugate();

        if(height!=0) {
            matrixStackIn.push();
            matrixStackIn.translate(0,-0.25,0);
            matrixStackIn.rotate(q);
            matrixStackIn.translate(0,-0.125,0);
            float h1 = height * 0.25f;
            RendererUtil.addCube(builder, matrixStackIn, 0.375f,0.3125f, 0.25f, h1, sprite, combinedLightIn, color, 1, combinedOverlayIn, true,
                    true, true, true);
            if(dir==Direction.DOWN) {
                matrixStackIn.translate(0, -h1 - 0.25f, 0);
                RendererUtil.addCube(builder, matrixStackIn,0.375f,0.3125f, 0.0625f, h1 + 0.25f, sprite, combinedLightIn, color, 1, combinedOverlayIn, true,
                        true, true, false);
            }
            matrixStackIn.pop();
        }
        if(height!=1) {
            matrixStackIn.push();
            matrixStackIn.translate(0,0.25,0);
            matrixStackIn.rotate(q);
            matrixStackIn.translate(0,-0.125,0);
            float h2 = (1 - height) * 0.25f;
            RendererUtil.addCube(builder, matrixStackIn,0.375f,0.3125f, 0.25f, h2 , sprite, combinedLightIn, color, 1, combinedOverlayIn, true,
                    true, true, true);
            if(dir==Direction.UP) {
                matrixStackIn.translate(0, -h2 -0.25, 0);
                RendererUtil.addCube(builder, matrixStackIn,0.375f,0.3125f, 0.0625f, h2 + 0.25f, sprite, combinedLightIn, color, 1f, combinedOverlayIn, true,
                        true, true, false);
            }
            matrixStackIn.pop();
        }
        matrixStackIn.pop();

    }

    @Override
    public void render(HourGlassBlockTile tile, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn,
                       int combinedOverlayIn) {
        if(tile.sandType.isEmpty())return;
        TextureAtlasSprite sprite = tile.getOrCreateSprite();

        float h = MathHelper.lerp(partialTicks, tile.prevProgress, tile.progress);
        Direction dir = tile.getBlockState().get(HourGlassBlock.FACING);

        renderSand(matrixStackIn,bufferIn,combinedLightIn,combinedOverlayIn,sprite,h,dir);
    }
}