package net.minecraft.server;

public class PacketStatusListener implements PacketStatusInListener {

    private static final IChatBaseComponent a = new ChatComponentText("Status request has been handled.");
    private final MinecraftServer minecraftServer;
    private final NetworkManager networkManager;
    private boolean d;

    public PacketStatusListener(MinecraftServer minecraftserver, NetworkManager networkmanager) {
        this.minecraftServer = minecraftserver;
        this.networkManager = networkmanager;
    }

    public void a(IChatBaseComponent ichatbasecomponent) {}

    public void a(PacketStatusInStart packetstatusinstart) {
        if (this.d) {
            this.networkManager.close(PacketStatusListener.a);
        } else {
            this.d = true;
            this.networkManager.handle(new PacketStatusOutServerInfo(this.minecraftServer.aG()));
        }
    }

    public void a(PacketStatusInPing packetstatusinping) {
        this.networkManager.handle(new PacketStatusOutPong(packetstatusinping.a()));
        this.networkManager.close(PacketStatusListener.a);
    }
}
