package cn.harry12800.lnk.client.entity;

import cn.harry12800.j2se.tip.Letter;

public class ClientInfo extends Letter {

	private String name;
	private String ip;
	private String mac;

	public ClientInfo(String name, String mac, String ip) {
		super(name, mac, ip);
		this.ip = ip;
		this.name = name;
		this.mac = mac;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}
