package cn.harry12800.lnk.client;

import java.util.List;

import cn.harry12800.lnk.client.entity.UserInfo;
import cn.harry12800.tools.Lists;

public class ClientJsonConfig {

	private UserInfo self;
	private List<UserInfo> list = Lists.newArrayList();
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

}
