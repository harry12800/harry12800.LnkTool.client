package cn.harry12800.client.module.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.harry12800.common.core.model.ResultCode;
import cn.harry12800.common.module.chat.dto.ResourceDto;
import cn.harry12800.common.module.user.dto.PullResouceResponse;
import cn.harry12800.lnk.client.ClientExportPanel;
import cn.harry12800.lnk.client.Resource;
import cn.harry12800.lnk.client.ResultCodeTip;
import cn.harry12800.tools.Lists;

/**
 * 玩家模块
 * @author -琴兽-
 *
 */
@Component
public class ResourceShareHandlerImpl implements ResourceShareHandler {

	@Autowired
	private ResultCodeTip resultCodeTip;

	@Override
	public void uploadResourceResult(int resultCode, byte[] data) {
		if (resultCode == ResultCode.SUCCESS) {
			ClientExportPanel.instance.showLoginMsg("上传成功！");
		} else {
			ClientExportPanel.instance.showLoginMsg(resultCodeTip.getTipContent(resultCode));
		}
	}

	@Override
	public void pullAllResouces(int resultCode, byte[] data) {
		if (resultCode == ResultCode.SUCCESS) {
			PullResouceResponse response = new PullResouceResponse();
			response.readFromBytes(data);
			List<ResourceDto> resources = response.getResources();
			List<Resource> lists = Lists.newArrayList();
			for (ResourceDto r : resources) {
				Resource c = new Resource(r.getResourceName(), r.getResourceName(), r.getGrantTime() + "");
				lists.add(c);
			}
			ClientExportPanel.instance.showResources(lists);
		} else {
			ClientExportPanel.instance.showLoginMsg(resultCodeTip.getTipContent(resultCode));
		}
	}

	@Override
	public void downloadResource(int resultCode, byte[] data) {
		if (resultCode == ResultCode.SUCCESS) {
			ResourceDto response = new ResourceDto();
			response.readFromBytes(data);
			ClientExportPanel.instance.downloadResourceCallback(response);
		} else {
			ClientExportPanel.instance.showLoginMsg(resultCodeTip.getTipContent(resultCode));
		}
	}
}
