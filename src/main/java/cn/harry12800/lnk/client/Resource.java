package cn.harry12800.lnk.client;

import cn.harry12800.j2se.tip.Letter;

public class Resource extends Letter {

	private long id;
	private String name;
	private int resourceType;

	public Resource(String title, String content, String date) {
		super(title, content, date);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getResourceType() {
		return resourceType;
	}

	public void setResourceType(int resourceType) {
		this.resourceType = resourceType;
	}

}
