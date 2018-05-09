package cn.harry12800.client.module.chat.handler;
import cn.harry12800.common.core.annotion.SocketCommand;
import cn.harry12800.common.core.annotion.SocketModule;
import cn.harry12800.common.module.ModuleId;
import cn.harry12800.common.module.chat.ChatCmd;
import cn.harry12800.common.module.chat.response.ChatResponse;
/**
 * 聊天
 * @author -琴兽-
 *
 */
@SocketModule(module = ModuleId.CHAT)
public interface ChatHandler {
	
	/**
	 * 发送广播消息回包
	 * @param resultCode
	 * @param data {@link null}
	 * @return
	 */
	@SocketCommand(cmd = ChatCmd.PUBLIC_CHAT)
	public void publicChat(int resultCode, byte[] data);
	
	/**
	 * 发送私人消息回包
	 * @param resultCode
	 * @param data {@link null}
	 * @return
	 */
	@SocketCommand(cmd = ChatCmd.PRIVATE_CHAT)
	public void privateChat(int resultCode, byte[] data);
	
	/**
	 * 收到推送聊天信息
	 * @param resultCode
	 * @param data {@link ChatResponse}
	 * @return
	 */
	@SocketCommand(cmd = ChatCmd.PUSHCHAT)
	public void receieveMessage(int resultCode, byte[] data);
}