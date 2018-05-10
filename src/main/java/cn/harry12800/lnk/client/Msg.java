package cn.harry12800.lnk.client;

import java.util.Date;

import cn.harry12800.common.module.player.response.MsgResponse;

public class Msg {
	private long id;
	private long fromPlayerId;
	private long toPlayerId;
	private Date sendTime;
	private int dataType;
	private byte[] data;
	public Msg(MsgResponse msgResponse) {
		id = msgResponse.getId();
		fromPlayerId = msgResponse.getFromPlayerId();
		toPlayerId = msgResponse.getToPlayerId();
		sendTime = msgResponse.getSendTime();
		dataType = msgResponse.getDataType();
		data = msgResponse.getData();
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

}
