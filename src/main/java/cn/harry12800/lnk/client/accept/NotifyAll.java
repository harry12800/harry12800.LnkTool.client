package cn.harry12800.lnk.client.accept;

import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import cn.harry12800.lnk.client.Config;
import cn.harry12800.lnk.client.entity.UserInfo;
import cn.harry12800.lnk.client.udp.ClientUtil;

/**
 * 广播我已上线
 * @author harry12800
 */
public class NotifyAll extends Thread {
	/**
	 * 拉取信息回调
	 * @author harry12800
	 */
	public interface NotifyCallback {
		public void notifyClientinfo(UserInfo clientinfo);
	}

	private NotifyCallback callback;
	/**
	 * 局域网广播地址。
	 */
	String broadcastAddr = ClientUtil.getBroadcastAddress();
	String broadcastMsg = "broadcast data";
	private DatagramSocket datagramSocket;
	private DatagramPacket out;

	public NotifyAll(NotifyCallback callback) {
		this.callback = callback;
		try {
			initSokect();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private void initSokect() throws Exception {
		byte[] buf = new byte[1024];
		datagramSocket = new DatagramSocket(Config.clientListenPort);
		InetAddress hostAddress = InetAddress.getByName(broadcastAddr);
		buf = broadcastMsg.getBytes();
		System.err.println("Send " + broadcastMsg + " to " + hostAddress);
		this.out = new DatagramPacket(buf, buf.length, hostAddress, Config.serverListenPort);
	}

	@Override
	public void run() {
		byte[] buf = new byte[1024];
		while (!Config.clientStop) {
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			try {
				datagramSocket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String rcvd = "Received from " + packet.getSocketAddress() + ", "
					+ "Data=" + new String(packet.getData(), 0, packet.getLength());
			String string = new String(packet.getData(), 0, packet.getLength());
			String[] split = string.split(":");
			InetSocketAddress socketAddress = (InetSocketAddress) packet.getSocketAddress();
			UserInfo letter = new UserInfo(split[0], split[1], socketAddress.getAddress().getHostAddress());
			callback.notifyClientinfo(letter);
			System.err.println(rcvd);
		}
	}

	private void sendBroadcast() throws Exception {
		byte[] buf = new byte[1024];
		try (DatagramSocket detectSocket = new DatagramSocket(Config.clientListenPort);) {
			System.err.println(broadcastAddr);
			InetAddress hostAddress = InetAddress.getByName(broadcastAddr);
			buf = broadcastMsg.getBytes();
			System.err.println("Send " + broadcastMsg + " to " + hostAddress);
			DatagramPacket out = new DatagramPacket(buf, buf.length, hostAddress, Config.serverListenPort);
//			this.detectSocket = detectSocket;
		}
	}

	
}
