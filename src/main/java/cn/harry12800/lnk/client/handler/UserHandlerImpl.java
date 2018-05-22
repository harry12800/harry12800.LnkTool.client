package cn.harry12800.lnk.client.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.harry12800.common.core.model.ResultCode;
import cn.harry12800.common.module.chat.dto.MsgResponse;
import cn.harry12800.common.module.user.dto.PullMsgResponse;
import cn.harry12800.common.module.user.dto.ShowAllUserResponse;
import cn.harry12800.common.module.user.dto.UserResponse;
import cn.harry12800.lnk.client.ClientExportPanel;
import cn.harry12800.lnk.client.ResultCodeTip;
import cn.harry12800.lnk.core.entity.UserInfo;
import cn.harry12800.tools.Lists;

/**
 * 玩家模块
 * @author -琴兽-
 *
 */
@Component
public class UserHandlerImpl implements UserHandler {

	@Autowired
	private ResultCodeTip resultCodeTip;
 
	@Override
	public void showAllUser(int resultCode, byte[] data) {
		
		if (resultCode == ResultCode.SUCCESS) {
			ShowAllUserResponse response = new ShowAllUserResponse();
			response.readFromBytes(data);
			List<UserResponse> players = response.getPlayers();
			List<UserInfo> lists = Lists.newArrayList();
			for (UserResponse playerResponse2 : players) {
				UserInfo c = new UserInfo(playerResponse2.getUserName(), playerResponse2.getId() + "", "");
				lists.add(c);
			}
			ClientExportPanel.instance.showUser(lists);
		} else {
			System.out.println("aaa");
		}
	}

	@Override
	public void pullMsg(int resultCode, byte[] data) {
		if (resultCode == ResultCode.SUCCESS) {
			PullMsgResponse response = new PullMsgResponse();
			response.readFromBytes(data);
			List<MsgResponse> msgs = response.getMsgs();
			try {
				ClientExportPanel.instance.showPullMsg(msgs);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("resultCode?");
			//			swingclient.getTips().setText(resultCodeTip.getTipContent(resultCode));
		}
	}
}
