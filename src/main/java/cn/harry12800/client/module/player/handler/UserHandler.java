package cn.harry12800.client.module.player.handler;

import cn.harry12800.common.core.annotion.SocketCommand;
import cn.harry12800.common.core.annotion.SocketModule;
import cn.harry12800.common.module.ModuleId;
import cn.harry12800.common.module.user.UserCmd;

/**
 * 玩家模块
 * @author -琴兽-
 *
 */
@SocketModule(module = ModuleId.USER)
public interface UserHandler {

	/**
	 * 创建并登录帐号
	 * @param resultCode
	 * @param data {@link null}
	 */
	@SocketCommand(cmd = UserCmd.REGISTER_AND_LOGIN, desc = "创建并登录帐号")
	public void registerAndLogin(int resultCode, byte[] data);

	/**
	 * 登录帐号
	 * @param resultCode
	 * @param data {@link null}
	 */
	@SocketCommand(cmd = UserCmd.LOGIN, desc = "登录帐号")
	public void login(int resultCode, byte[] data);

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