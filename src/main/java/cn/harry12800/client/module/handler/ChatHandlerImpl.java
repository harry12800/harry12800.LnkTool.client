package cn.harry12800.client.module.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.harry12800.common.core.model.ResultCode;
import cn.harry12800.common.module.chat.dto.MsgResponse;
import cn.harry12800.lnk.client.ClientExportPanel;
import cn.harry12800.lnk.client.ResultCodeTip;

@Component
public class ChatHandlerImpl implements ChatHandler {

	@Autowired
	private  ResultCodeTip resultCodeTip;

	@Override
	public void publicChat(int resultCode, byte[] data) {
		if (resultCode == ResultCode.SUCCESS) {
//			swingclient.getTips().setText("发送成功");
		} else {
//			swingclient.getTips().setText(resultCodeTip.getTipContent(resultCode));
		}
	}

	@Override
	public void privateChat(int resultCode, byte[] data) {
		if (resultCode == ResultCode.SUCCESS) {
			MsgResponse msg = new MsgResponse();
			msg.readFromBytes(data);
			ClientExportPanel.instance.showPrivateChatSuccessNotify("发送成功", msg);
		} else {
			ClientExportPanel.instance.showNotify(resultCodeTip.getTipContent(resultCode));
		}
	}

	@Override
	public void receieveMessage(int resultCode, byte[] data) {

		MsgResponse chatResponse = new MsgResponse();
		chatResponse.readFromBytes(data);
		try {
			ClientExportPanel.instance.showReceiveMsg(chatResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
