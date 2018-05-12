package cn.harry12800.client.module.player.handler;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.harry12800.common.core.model.ResultCode;
import cn.harry12800.common.module.chat.response.MsgResponse;
import cn.harry12800.common.module.user.response.PullMsgResponse;
import cn.harry12800.common.module.user.response.ShowAllUserResponse;
import cn.harry12800.common.module.user.response.UserResponse;
import cn.harry12800.lnk.client.ClientExportPanel;
import cn.harry12800.lnk.client.ResultCodeTip;
import cn.harry12800.lnk.client.entity.UserInfo;
import cn.harry12800.tools.Lists;
/**
 * 玩家模块
 * @author -琴兽-
 *
 */
@Component
public class UserHandlerImpl implements UserHandler{
	 
	@Autowired
	private ResultCodeTip resultCodeTip;

	@Override
	public void registerAndLogin(int resultCode, byte[] data) {
		if(resultCode == ResultCode.SUCCESS){
			UserResponse playerResponse = new UserResponse();
			playerResponse.readFromBytes(data);
			
//			swingclient.getTips().setText("注册登录成功");
		}else{
//			swingclient.getTips().setText(resultCodeTip.getTipContent(resultCode));
		}
	}

	@Override
	public void login(int resultCode, byte[] data) {
		if(resultCode == ResultCode.SUCCESS){
			UserResponse playerResponse = new UserResponse();
			playerResponse.readFromBytes(data);
//			ClientExportPanel.instance.loginSuccess("登录成功！");
		}else{
			ClientExportPanel.instance.showLoginMsg(resultCodeTip.getTipContent(resultCode));
		}
	}

	@Override
	public void showAllUser(int resultCode, byte[] data) {
		if(resultCode == ResultCode.SUCCESS) {
			ShowAllUserResponse response = new ShowAllUserResponse();
			response.readFromBytes(data);
			List<UserResponse> players = response.getPlayers();
			List<UserInfo> lists = Lists.newArrayList();
			for (UserResponse playerResponse2 : players) {
				UserInfo c = new UserInfo(playerResponse2.getPlayerName(), playerResponse2.getPlayerId()+"", "");
				lists.add(c);
			}
			ClientExportPanel.instance.showUser(lists);
		}else{
//			swingclient.getTips().setText(resultCodeTip.getTipContent(resultCode));
		}
	}

	@Override
	public void pullMsg(int resultCode, byte[] data) {
		if(resultCode == ResultCode.SUCCESS) {
			PullMsgResponse response = new PullMsgResponse();
			response.readFromBytes(data);
			 List<MsgResponse> msgs = response.getMsgs();
			try {
				ClientExportPanel.instance.showPullMsg(msgs);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("resultCode?");
//			swingclient.getTips().setText(resultCodeTip.getTipContent(resultCode));
		}
	}
}
