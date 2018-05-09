package cn.harry12800.lnk.client.entity;

import cn.harry12800.j2se.tip.Letter;

/**
 * 客户端客户信息
 * @author zr0014
 *
 */
public class UserInfo extends Letter {

	private String id;
	private String name;
	private String ip;
	private String clientType;
	private String mac;
	private String token;

	
	public void setToken(String token) {
		this.token = token;
	}

	public UserInfo(String name, String mac, String ip) {
		super(name, mac, ip);
		this.ip = ip;
		this.name = name;
		this.mac = mac;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getToken() {
		return token;
	}
	
}
