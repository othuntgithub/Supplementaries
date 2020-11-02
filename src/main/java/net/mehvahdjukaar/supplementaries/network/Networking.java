package net.mehvahdjukaar.supplementaries.network;

import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;


public class Networking{
    public static SimpleChannel INSTANCE;
    private static int ID = 0;
    private static final String PROTOCOL_VERSION = "1";
    public static int nextID() { return ID++; }

    public static void registerMessages() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(Supplementaries.MOD_ID, "splmchannel"), () -> PROTOCOL_VERSION,
                PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

        INSTANCE.registerMessage(nextID(), SendSpeakerBlockMessagePacket.class, SendSpeakerBlockMessagePacket::buffer,
                SendSpeakerBlockMessagePacket::new, SendSpeakerBlockMessagePacket::handler);

        INSTANCE.registerMessage(nextID(), UpdateServerSpeakerBlockPacket.class, UpdateServerSpeakerBlockPacket::buffer,
                UpdateServerSpeakerBlockPacket::new, UpdateServerSpeakerBlockPacket::handler);

        INSTANCE.registerMessage(nextID(), UpdateServerSignPostPacket.class, UpdateServerSignPostPacket::buffer,
                UpdateServerSignPostPacket::new, UpdateServerSignPostPacket::handler);

        INSTANCE.registerMessage(nextID(), UpdateServerHangingSignPacket.class, UpdateServerHangingSignPacket::buffer,
                UpdateServerHangingSignPacket::new, UpdateServerHangingSignPacket::handler);



    }
}