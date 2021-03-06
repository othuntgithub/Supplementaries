package net.mehvahdjukaar.supplementaries.network;



import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.configs.ServerConfigs;
import net.mehvahdjukaar.supplementaries.plugins.configured.CustomConfiguredScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenConfigsPacket {
    public OpenConfigsPacket(PacketBuffer buffer) {}
    public OpenConfigsPacket() {}

    public static void buffer(OpenConfigsPacket message, PacketBuffer buf) {}

    public static void handler(OpenConfigsPacket message, Supplier<NetworkEvent.Context> ctx) {
        // client world
        ctx.get().enqueueWork(() -> {

            Minecraft mc = Minecraft.getInstance();

            //FileConfig f = FileConfig.of(ConfigHandler.getServerConfigPath());
            //ServerConfigs.SERVER_CONFIG.getSpec().apply(ConfigHandler.getServerConfigPath().toString());
            //ServerConfigs.SERVER_CONFIG.getSpec().apply(ConfigHandler.getServerConfigPath().toString());
            //ServerConfigs.SERVER_CONFIG.save();

            ServerConfigs.loadLocal();


            if(ModList.get().isLoaded("configured"))
                CustomConfiguredScreen.openScreen(mc);
            else{
                mc.displayGuiScreen(ModList.get().getModContainerById(Supplementaries.MOD_ID).get()
                        .getCustomExtension(ExtensionPoint.CONFIGGUIFACTORY).get()
                        .apply(mc,mc.currentScreen));
            }

        });
        ctx.get().setPacketHandled(true);
    }
}