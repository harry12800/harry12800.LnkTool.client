package cn.harry12800.lnk.client;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class Demo01 {
	public static void main(String[] args) {
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
						System.err.println(address.getHostName());
					}
				}
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
			}

		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}