package net.minecraft.server;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class ServerConnection {

	private static final Logger e = LogManager.getLogger();
	public static final LazyInitVar<NioEventLoopGroup> a = new LazyInitVar<NioEventLoopGroup>() {
		protected NioEventLoopGroup a() {
			return new NioEventLoopGroup();
		}

		@Override
		protected NioEventLoopGroup init() {
			return this.a();
		}
	};
	public static final LazyInitVar<NioEventLoopGroup> b = new LazyInitVar<NioEventLoopGroup>() {
		protected NioEventLoopGroup a() {
			return new NioEventLoopGroup();
		}

		@Override
		protected NioEventLoopGroup init() {
			return this.a();
		}
	};
	public static final LazyInitVar<NioEventLoopGroup> c = new LazyInitVar<NioEventLoopGroup>() {
		protected NioEventLoopGroup a() {
			return new NioEventLoopGroup();
		}

		@Override
		protected NioEventLoopGroup init() {
			return this.a();
		}
	};
	private final MinecraftServer f;
	public volatile boolean d;
	private final List<ChannelFuture> g = Collections.synchronizedList(Lists.<ChannelFuture>newArrayList());
	private final List<NetworkManager> h = Collections.synchronizedList(Lists.<NetworkManager>newArrayList());

	public ServerConnection(MinecraftServer minecraftserver) {
		this.f = minecraftserver;
		this.d = true;
	}

	@SuppressWarnings({ "rawtypes" })
	public void a(InetAddress inetaddress, int i) throws IOException {
		// List<ChannelFuture> list = this.g; // CloudSpigot

		synchronized (this.g) {

			this.g.add((new ServerBootstrap()).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer() {
				@Override
				protected void initChannel(Channel channel) throws Exception {
					try {
						channel.config().setOption(ChannelOption.TCP_NODELAY, true);
					} catch (ChannelException channelexception) {
						;
					}

					channel.pipeline().addLast("timeout", new ReadTimeoutHandler(30))
							.addLast("legacy_query", new LegacyPingHandler(ServerConnection.this))
							.addLast("splitter", new PacketSplitter())
							.addLast("decoder", new PacketDecoder(EnumProtocolDirection.SERVERBOUND))
							.addLast("prepender", new PacketPrepender())
							.addLast("encoder", new PacketEncoder(EnumProtocolDirection.CLIENTBOUND));
					NetworkManager networkmanager = new NetworkManager(EnumProtocolDirection.SERVERBOUND);

					ServerConnection.this.h.add(networkmanager);
					channel.pipeline().addLast("packet_handler", networkmanager);
					networkmanager.a((new HandshakeListener(ServerConnection.this.f, networkmanager)));
				}
			}).group(a.c()).localAddress(inetaddress, i).bind().syncUninterruptibly());
		}
	}

	public void b() {
		this.d = false;
		Iterator<ChannelFuture> iterator = this.g.iterator();

		while (iterator.hasNext()) {
			ChannelFuture channelfuture = iterator.next();

			try {
				channelfuture.channel().close().sync();
			} catch (InterruptedException interruptedexception) {
				ServerConnection.e.error("Interrupted whilst closing channel");
			}
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void c() {
		// List<NetworkManager> list = this.h; // CloudSpigot

		synchronized (this.h) {
			// Spigot Start
			// This prevents players from 'gaming' the server, and strategically relogging
			// to increase their position in the tick order
			if (org.spigotmc.SpigotConfig.playerShuffle > 0
					&& MinecraftServer.currentTick % org.spigotmc.SpigotConfig.playerShuffle == 0) {
				Collections.shuffle(this.h);
			}
			// Spigot End
			Iterator<NetworkManager> iterator = this.h.iterator();

			while (iterator.hasNext()) {
				final NetworkManager networkmanager = iterator.next();

				if (!networkmanager.h()) {
					if (!networkmanager.g()) {
						// Spigot Start
						// Fix a race condition where a NetworkManager could be unregistered just before
						// connection.
						if (networkmanager.preparing)
							continue;
						// Spigot End
						iterator.remove();
						networkmanager.l();
					} else {
						try {
							networkmanager.a();
						} catch (Exception exception) {
							if (networkmanager.c()) {
								CrashReport crashreport = CrashReport.a(exception, "Ticking memory connection");
								CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Ticking connection");

								crashreportsystemdetails.a("Connection", new Callable<String>() {
									public String a() throws Exception {
										return networkmanager.toString();
									}

									@Override
									public String call() throws Exception {
										return this.a();
									}
								});
								throw new ReportedException(crashreport);
							}

							ServerConnection.e.warn("Failed to handle packet for " + networkmanager.getSocketAddress(),
									exception);
							final ChatComponentText chatcomponenttext = new ChatComponentText("Internal server error");

							networkmanager.a(new PacketPlayOutKickDisconnect(chatcomponenttext),
									new GenericFutureListener() {
										@Override
										public void operationComplete(Future future) throws Exception {
											networkmanager.close(chatcomponenttext);
										}
									}, new GenericFutureListener[0]);
							networkmanager.k();
						}
					}
				}
			}

		}
	}

	public MinecraftServer d() {
		return this.f;
	}
}