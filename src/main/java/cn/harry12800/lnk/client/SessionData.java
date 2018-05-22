package cn.harry12800.lnk.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import cn.harry12800.lnk.core.entity.UserInfo;
import cn.harry12800.tools.Lists;

public class SessionData {

	private List<UserInfo> list = Lists.newArrayList();
	private Map<UserInfo, ConcurrentLinkedQueue<Msg>> maps = new HashMap<>();
	 
	public List<UserInfo> getList() {
		return list;
	}
	public void setList(List<UserInfo> list) {
		this.list = list;
	}
	public Map<UserInfo, ConcurrentLinkedQueue<Msg>> getMaps() {
		return maps;
	}
	public void setMaps(Map<UserInfo, ConcurrentLinkedQueue<Msg>> maps) {
		this.maps = maps;
	}
	
	
}
