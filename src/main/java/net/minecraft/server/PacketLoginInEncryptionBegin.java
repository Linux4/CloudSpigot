package net.minecraft.server;

import java.io.IOException;
import java.security.PrivateKey;

import javax.crypto.SecretKey;

public class PacketLoginInEncryptionBegin implements Packet<PacketLoginInListener> {

	private byte[] a = new byte[0];
	private byte[] b = new byte[0];

	public PacketLoginInEncryptionBegin() {
	}

	@Override
	public void a(PacketDataSerializer packetdataserializer) throws IOException {
		// Paper start
		this.a = packetdataserializer.readByteArray(256);
		this.b = packetdataserializer.readByteArray(256);
		// Paper end
	}

	@Override
	public void b(PacketDataSerializer packetdataserializer) throws IOException {
		packetdataserializer.a(this.a);
		packetdataserializer.a(this.b);
	}

	@Override
	public void a(PacketLoginInListener packetlogininlistener) {
		packetlogininlistener.a(this);
	}

	public SecretKey a(PrivateKey privatekey) {
		return MinecraftEncryption.a(privatekey, this.a);
	}

	public byte[] b(PrivateKey privatekey) {
		return privatekey == null ? this.b : MinecraftEncryption.b(privatekey, this.b);
	}
}
