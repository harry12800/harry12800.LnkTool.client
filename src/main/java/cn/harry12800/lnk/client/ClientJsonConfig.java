package cn.harry12800.lnk.client;

import java.util.List;

import cn.harry12800.tools.Lists;

public class ClientJsonConfig {

	private List<ClientConnectionParam> list = Lists.newArrayList();

	/**
	 * 获取list
	 *	@return the list
	 */
	public List<ClientConnectionParam> getList() {
		return list;
	}

	/**
	 * 设置list
	 * @param list the list to set
	 */
	public void setList(List<ClientConnectionParam> list) {
		this.list = list;
	}

}
