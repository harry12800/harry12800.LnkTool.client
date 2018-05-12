package cn.harry12800.lnk.client.udp;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

public class ClientUtil {

	/**
	 * 获取网卡对应的IP地址
	 * @return
	 */
	public static String getIpAddress() {
		Map<String, String> ipSet = getIpSet();
		Set<Entry<String, String>> entrySet2 = ipSet.entrySet();

		Properties p = new Properties();
		try (InputStream input = NotifyAll.class.getResourceAsStream("/config/mac.properties")) {
			p.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Set<Entry<Object, Object>> entrySet = p.entrySet();
		for (Entry<String, String> entry1 : entrySet2) {
			for (Entry<Object, Object> entry2 : entrySet) {
				if (entry1.getKey().equals(entry2.getValue())) {
					//					System.out.println(entry1.getValue());
					return entry1.getValue();
				}
			}
		}
		return "";
	}

	public static String getBroadcastAddress() {
		String ip = getIpAddress();
		String[] split = ip.split("[.]");
		if (split.length != 4) {
			return "192.168.1.255";
		} else {
			return split[0] + "." + split[1] + "." + split[2] + ".255";
		}
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
						//						System.err.println(address.getHostAddress());
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
