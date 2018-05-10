package cn.harry12800.lnk.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import cn.harry12800.lnk.client.entity.UserInfo;
import cn.harry12800.tools.Lists;

public class ClientJsonConfig {

	private UserInfo self;
	private List<UserInfo> list = Lists.newArrayList();
	private Map<Long,ConcurrentLinkedQueue<Msg>> maps = new HashMap<>();
	public UserInfo getSelf() {
		return self;
	}
	public void setSelf(UserInfo self) {
		this.self = self;
	}
	public List<UserInfo> getList() {
		return list;
	}
	public void setList(List<UserInfo> list) {
		this.list = list;
	}
	public Map<Long, ConcurrentLinkedQueue<Msg>> getMaps() {
		return maps;
	}
	public void setMaps(Map<Long, ConcurrentLinkedQueue<Msg>> maps) {
		this.maps = maps;
	}
}
