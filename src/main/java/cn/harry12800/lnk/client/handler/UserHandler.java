package cn.harry12800.lnk.client.handler;

import cn.harry12800.common.core.annotion.SocketCommand;
import cn.harry12800.common.core.annotion.SocketModule;
import cn.harry12800.common.module.ModuleId;
import cn.harry12800.common.module.UserCmd;

/**
 * 玩家模块
 * @author -琴兽-
 *
 */
@SocketModule(module = ModuleId.USER)
public interface UserHandler {
	/**
	 * 回报所有好友
	 * @param resultCode
	 * @param data {@link null}
	 */
	@SocketCommand(cmd = UserCmd.SHOW_ALL_USER, desc = "回报所有好友")
	public void showAllUser(int resultCode, byte[] data);

	/**
	 * 拉取所有未读消息
	 * @param resultCode
	 * @param data {@link null}
	 */
	@SocketCommand(cmd = UserCmd.PULL_MSG, desc = "拉取所有未读消息")
	public void pullMsg(int resultCode, byte[] data);
}
