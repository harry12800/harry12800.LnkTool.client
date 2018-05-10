package cn.harry12800.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Properties;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.harry12800.client.swing.Swingclient;
import cn.harry12800.common.core.codc.RequestEncoder;
import cn.harry12800.common.core.codc.ResponseDecoder;
import cn.harry12800.common.core.model.Request;

/**
 * netty客户端入门
 * 
 * @author -琴兽-
 * 
 */
@Component
public class Client {
	
	/**
	 * 界面
	 */
	@Autowired
	private Swingclient swingclient;

	/**
	 * 服务类
	 */
	Bootstrap bootstrap = new Bootstrap();

	Properties p  = new Properties();
	
	/**
	 * 会话
	 */
	private Channel channel;

	/**
	 * 线程池
	 */
	private EventLoopGroup workerGroup = new NioEventLoopGroup();

	/**
	 * 初始化
	 */
	@PostConstruct
	public void init() {
		try(InputStream stream = Client.class.getResourceAsStream("/client.properties");){
			p.load(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 设置循环线程组事例
		bootstrap.group(workerGroup);
		// 设置channel工厂
		bootstrap.channel(NioSocketChannel.class);

		// 设置管道
		bootstrap.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast(new ResponseDecoder());
				ch.pipeline().addLast(new RequestEncoder());
				ch.pipeline().addLast(new ClientHandler(swingclient));
			}
		});
	}

	/**
	 * 连接
	 * 
	 * @param ip
	 * @param port
	 * @throws InterruptedException
	 */
	public void connect() throws InterruptedException {
		// 连接服务端
		ChannelFuture connect = bootstrap.connect(new InetSocketAddress("127.0.0.1", 10000));
		connect.sync();
		channel = connect.channel();
	}

	/**
	 * 关闭
	 */
	public void shutdown() {
		workerGroup.shutdownGracefully();
	}

	/**
	 * 获取会话
	 * @return
	 */
	public Channel getChannel() {
		return channel;
	}
	
	/**
	 * 发送消息
	 * @param request
	 * @throws InterruptedException 
	 */
	public void sendRequest(Request request) throws InterruptedException{
		if(channel == null || !channel.isActive()){
			connect();
		}
		channel.writeAndFlush(request);
	}
}
