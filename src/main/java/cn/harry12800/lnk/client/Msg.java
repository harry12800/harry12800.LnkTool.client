package cn.harry12800.lnk.client;

import java.util.Date;

import cn.harry12800.common.module.chat.dto.MsgResponse;
import cn.harry12800.common.module.chat.dto.PrivateChatRequest;

public class Msg {
	private long id;
	private long fromPlayerId;
	private int online;
	private long toPlayerId;
	private Date sendTime;
	private int dataType;
	private byte[] data;

	public Msg(MsgResponse m) {
		id = m.getId();
		fromPlayerId = m.getFromId();
		online = m.getOnline();
		toPlayerId = m.getToId();
		sendTime = m.getSendTime();
		dataType = m.getDataType();
		data = m.getData();
	}

	public Msg(PrivateChatRequest req) {

	}

	public int getOnline() {
		return online;
	}

	public void setOnline(int online) {
		this.online = online;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getFromPlayerId() {
		return fromPlayerId;
	}

	public void setFromPlayerId(long fromPlayerId) {
		this.fromPlayerId = fromPlayerId;
	}

	public long getToPlayerId() {
		return toPlayerId;
	}

	public void setToPlayerId(long toPlayerId) {
		this.toPlayerId = toPlayerId;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Msg other = (Msg) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
