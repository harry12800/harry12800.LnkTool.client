package cn.harry12800.lnk.client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;

public class BroadcastReceiveThread extends Thread {

	byte[] buf = new byte[1024];

	@Override
	public void run() {
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		try (DatagramSocket responseSocket = new DatagramSocket(Config.serverListenPort);) {
			System.out.println("Server started, Listen port: " + Config.serverListenPort);
			while (!Config.serverStop) {
				responseSocket.receive(packet);
				String rcvd = "Received " + new String(packet.getData(), 0, packet.getLength()) + " from address: "
						+ packet.getSocketAddress()+ "  port:"+packet.getPort();
				System.out.println(rcvd);
				// Send a response packet to sender
				InetAddress ia1 = InetAddress.getLocalHost();//获取本地IP对象    
				String macAddress = getMACAddress(ia1);
				String backData = ia1.getHostName() + ":" + macAddress;
				byte[] data = backData.getBytes();
				System.out.println("Send " + backData + " to " + packet.getSocketAddress());
				DatagramPacket backPacket = new DatagramPacket(data, 0, data.length, packet.getSocketAddress());
				responseSocket.send(backPacket);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	//获取MAC地址的方法    
	private static String getMACAddress(InetAddress ia) throws Exception {
		//获得网络接口对象（即网卡），并得到mac地址，mac地址存在于一个byte数组中。    
		byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
		//下面代码是把mac地址拼装成String    
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < mac.length; i++) {
			if (i != 0) {
				sb.append("-");
			}
			//mac[i] & 0xFF 是为了把byte转化为正整数    
			String s = Integer.toHexString(mac[i] & 0xFF);
			//            System.out.println("--------------");  
			//            System.err.println(s);  
			sb.append(s.length() == 1 ? 0 + s : s);
		}
		//把字符串所有小写字母改为大写成为正规的mac地址并返回    
		return sb.toString().toUpperCase();
	}
}
