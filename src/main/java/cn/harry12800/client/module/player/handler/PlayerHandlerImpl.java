package cn.harry12800.client.module.player.handler;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.harry12800.client.swing.ResultCodeTip;
import cn.harry12800.client.swing.Swingclient;
import cn.harry12800.common.core.model.ResultCode;
import cn.harry12800.common.module.player.response.PlayerResponse;
import cn.harry12800.common.module.player.response.ShowAllPlayerResponse;
import cn.harry12800.lnk.client.ClientExportPanel;
import cn.harry12800.lnk.client.entity.UserInfo;
import cn.harry12800.tools.Lists;
/**
 * 玩家模块
 * @author -琴兽-
 *
 */
@Component
public class PlayerHandlerImpl implements PlayerHandler{
	
	@Autowired
	private Swingclient swingclient;
	@Autowired
	private ResultCodeTip resultCodeTip;

	@Override
	public void registerAndLogin(int resultCode, byte[] data) {
		if(resultCode == ResultCode.SUCCESS){
			PlayerResponse playerResponse = new PlayerResponse();
			playerResponse.readFromBytes(data);
			
			swingclient.setPlayerResponse(playerResponse);
			swingclient.getTips().setText("注册登录成功");
		}else{
			swingclient.getTips().setText(resultCodeTip.getTipContent(resultCode));
		}
	}

	@Override
	public void login(int resultCode, byte[] data) {
		if(resultCode == ResultCode.SUCCESS){
			PlayerResponse playerResponse = new PlayerResponse();
			playerResponse.readFromBytes(data);
			swingclient.setPlayerResponse(playerResponse);
			swingclient.getTips().setText("登录成功");
		}else{
			swingclient.getTips().setText(resultCodeTip.getTipContent(resultCode));
			ClientExportPanel.instance.showLoginMsg(resultCodeTip.getTipContent(resultCode));
		}
	}

	@Override
	public void showAllUser(int resultCode, byte[] data) {
		if(resultCode == ResultCode.SUCCESS) {
			ShowAllPlayerResponse response = new ShowAllPlayerResponse();
			response.readFromBytes(data);
			List<PlayerResponse> players = response.getPlayers();
			List<UserInfo> lists = Lists.newArrayList();
			System.out.println(players.size());
			for (PlayerResponse playerResponse2 : players) {
				UserInfo c = new UserInfo(playerResponse2.getPlayerName(), playerResponse2.getPlayerId()+"", "");
				lists.add(c);
			}
			ClientExportPanel.instance.showUser(lists);
		}else{
			System.out.println("resultCode?");
			swingclient.getTips().setText(resultCodeTip.getTipContent(resultCode));
		}
	}
}
