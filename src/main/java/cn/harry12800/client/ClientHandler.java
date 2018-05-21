package cn.harry12800.client;

import cn.harry12800.client.scanner.Invoker;
import cn.harry12800.client.scanner.InvokerHoler;
import cn.harry12800.common.core.model.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 消息接受处理类
 * @author -琴兽-
 *
 */
public class ClientHandler extends SimpleChannelInboundHandler<Response> {

	/**
	 * 接收消息
	 */
	@Override
	public void channelRead0(ChannelHandlerContext ctx, Response response) throws Exception {

		handlerResponse(response);
	}

	/**
	 * 消息处理
	 * @param channelId
	 * @param request
	 */
	private void handlerResponse(Response response) {

		//获取命令执行器
		System.out.println("module："+response.getModule()+"  cmd："+response.getCmd());
		Invoker invoker = InvokerHoler.getInvoker(response.getModule(), response.getCmd());
		if (invoker != null) {
			try {
				invoker.invoke(response.getStateCode(), response.getData());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			//找不到执行器
			System.out.println(String.format("module:%s  cmd:%s 找不到命令执行器", response.getModule(), response.getCmd()));
		}
	}

	/**
	 * 断开链接
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		//		swingclient.getTips().setText("与服务器断开连接~~~");
	}
}
