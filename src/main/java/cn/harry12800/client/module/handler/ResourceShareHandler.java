package cn.harry12800.client.module.handler;

import cn.harry12800.common.core.annotion.SocketCommand;
import cn.harry12800.common.core.annotion.SocketModule;
import cn.harry12800.common.module.ModuleId;
import cn.harry12800.common.module.ResourceShareCmd;

/**
 * 玩家模块
 * @author -琴兽-
 *
 */
@SocketModule(module = ModuleId.RESOURCE)
public interface ResourceShareHandler {

	/**
	 * 创建并登录帐号
	 * @param resultCode
	 * @param data {@link null}
	 */
	@SocketCommand(cmd = ResourceShareCmd.upload_source, desc = "上传回调")
	public void uploadResourceResult(int resultCode, byte[] data);
	/**
	 * 创建并登录帐号
	 * @param resultCode
	 * @param data {@link null}
	 */
	@SocketCommand(cmd = ResourceShareCmd.pullAllResouces, desc = "回调资源")
	public void pullAllResouces(int resultCode, byte[] data);
}
