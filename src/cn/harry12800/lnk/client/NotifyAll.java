package cn.harry12800.lnk.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import cn.harry12800.j2se.tip.ItemPanel;
import cn.harry12800.j2se.tip.Letter;

public class NotifyAll extends Thread {
	private ClientExportPanel clientExportPanel;

	public NotifyAll(ClientExportPanel clientExportPanel) {
		this.clientExportPanel = clientExportPanel;
	}

	@Override
	public void run() {
		try {
			DatagramSocket detectSocket = new DatagramSocket(8888);
			byte[] buf = new byte[1024];
			int packetPort = 9999;
			String ba = getBroadcastAddress(getBroadcastAddress());
			InetAddress hostAddress = InetAddress.getByName(ba);
			String outMessage = "broadcast";
			buf = outMessage.getBytes();
			System.out.println("Send " + outMessage + " to " + hostAddress);
			// Send packet to hostAddress:9999, server that listen
			// 9999 would reply this packet
			DatagramPacket out = new DatagramPacket(buf, buf.length, hostAddress, packetPort);
			new Thread(new Runnable() {
				@Override
				public void run() {
					while (true) {
						byte[] buf = new byte[1024];
						DatagramPacket packet = new DatagramPacket(buf, buf.length);
						try {
							detectSocket.receive(packet);
						} catch (IOException e) {
							e.printStackTrace();
						}
						String rcvd = "Received from " + packet.getSocketAddress() + ", Data="
								+ new String(packet.getData(), 0, packet.getLength());
						String string = new String(packet.getData(), 0, packet.getLength());
						String[] split = string.split(":");
						InetSocketAddress socketAddress = (InetSocketAddress) packet.getSocketAddress();
						Letter letter = new Letter(split[0], split[1], socketAddress.getAddress().getHostAddress());
						clientExportPanel.letters.add(letter);
						ItemPanel itemPanel = new ItemPanel<>(letter);
						clientExportPanel.listPanel.addItem(itemPanel);
						System.out.println(rcvd);
					}
				}
			}).start();
			detectSocket.send(out);
			// Receive packet thread.
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static String getBroadcastAddress(String ip) {
		String[] split = ip.split("[.]");
		if (split.length != 4) {
			return "192.168.1.255";
		} else {
			return split[0] + "." + split[1] + "." + split[2] + ".255";
		}
	}

	private static String getBroadcastAddress() {
		Map<String, String> ipSet = getIpSet();
		Set<Entry<String, String>> entrySet2 = ipSet.entrySet();

		Properties p = new Properties();
		try (InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("src/config/mac.properties")) {
			System.err.println(input);
			p.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Set<Entry<Object, Object>> entrySet = p.entrySet();
		for (Entry<String, String> entry1 : entrySet2) {
			for (Entry<Object, Object> entry2 : entrySet) {
				if (entry1.getKey().equals(entry2.getValue())) {
					System.out.println(entry1.getValue());
					return entry1.getValue();
				}
			}
		}
		return "";
	}

	public static void main(String[] args) {
		String broadcastAddress = getBroadcastAddress(getBroadcastAddress());
		System.out.println(broadcastAddress);
	}

	public static Map<String, String> getIpSet() {
		Map<String, String> maps = new HashMap<String, String>();
		try {
			Enumeration<NetworkInterface> el = NetworkInterface.getNetworkInterfaces();
			while (el.hasMoreElements()) {
				NetworkInterface nextElement = el.nextElement();
				byte[] mac = nextElement.getHardwareAddress();
				if (mac == null) {
					continue;
				}
				Enumeration<InetAddress> addrList = nextElement.getInetAddresses();
				if (addrList.hasMoreElements()) {
					InetAddress address = addrList.nextElement();
					if (address instanceof InetAddress) {
						System.err.println(address.getHostAddress());
						// System.err.println(address.getHostName());
						// System.err.println(address.getLocalHost().getHostAddress());
						StringBuilder builder = new StringBuilder();
						if (builder.length() > 0) {
							builder.append(",");
						}
						for (byte b : mac) {
							// convert to hex string.
							String hex = Integer.toHexString(0xff & b).toUpperCase();
							if (hex.length() == 1) {
								hex = "0" + hex;
							}
							builder.append(hex);
							builder.append("-");
						}
						builder.deleteCharAt(builder.length() - 1);
						if (builder.length() == 0) {
							System.err.println("Sorry, can't find your MAC Address.");
						} else {
							System.err.println("Your MAC Address is " + builder.toString());
						}
						if (builder.length() != 0) {
							maps.put(builder.toString(), address.getHostAddress());
						}
					}
				}
			}

		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return maps;
	}
}
