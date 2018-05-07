package cn.harry12800.lnk.client.udp;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

/**
 * @author 作者 YYD
 * @version 创建时间：2016年11月18日 下午9:00:11
 * @function 未添加
 */
public class ChineseProverbClient {
	public void run(int port) throws Exception {

		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioDatagramChannel.class)
					.option(ChannelOption.SO_BROADCAST, true)//允许广播
					.handler(new ChineseProverClientHandler());//设置消息处理器
			Channel ch = b.bind(0).sync().channel();
			//向网段内的所有机器广播UDP消息。
			ch.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer("谚语字典查询？", CharsetUtil.UTF_8), new InetSocketAddress("192.168.3.255", port))).sync();
			if (!ch.closeFuture().await(15000)) {
				System.out.println("查询超时！");
			}
		} catch (Exception e) {
			group.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		int port = 8080;

		new ChineseProverbClient().run(port);
	}
}