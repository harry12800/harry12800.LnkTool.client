package cn.harry12800.lnk.client;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;

import cn.harry12800.Lnk.core.Context;
import cn.harry12800.Lnk.core.CorePanel;
import cn.harry12800.Lnk.core.FunctionPanelConfig;
import cn.harry12800.Lnk.core.FunctionPanelModel;
import cn.harry12800.Lnk.core.util.ImageUtils;
import cn.harry12800.client.Client;
import cn.harry12800.common.core.model.Request;
import cn.harry12800.common.module.ChatCmd;
import cn.harry12800.common.module.ModuleId;
import cn.harry12800.common.module.ResourceShareCmd;
import cn.harry12800.common.module.UserCmd;
import cn.harry12800.common.module.chat.dto.MsgResponse;
import cn.harry12800.common.module.chat.dto.PrivateChatRequest;
import cn.harry12800.common.module.chat.dto.ResourceDto;
import cn.harry12800.common.module.chat.dto.SourceShareRequest;
import cn.harry12800.common.module.user.dto.DownLoadResourceRequest;
import cn.harry12800.common.module.user.dto.LoginRequest;
import cn.harry12800.common.module.user.dto.PullMsgRequest;
import cn.harry12800.common.module.user.dto.PullResourceRequest;
import cn.harry12800.common.module.user.dto.ShowAllUserRequest;
import cn.harry12800.common.module.user.dto.UserResponse;
import cn.harry12800.j2se.component.ClickAction;
import cn.harry12800.j2se.component.InputText;
import cn.harry12800.j2se.component.MButton;
import cn.harry12800.j2se.component.TextLabel;
import cn.harry12800.j2se.component.TextLabel.Builder;
import cn.harry12800.j2se.component.btn.ImageBtn;
import cn.harry12800.j2se.component.panel.AreaTextPanel;
import cn.harry12800.j2se.style.MyScrollBarUI;
import cn.harry12800.j2se.style.UI;
import cn.harry12800.j2se.tip.ItemPanel;
import cn.harry12800.j2se.tip.ListPanel;
import cn.harry12800.j2se.tip.ListPanel.ListCallBack;
import cn.harry12800.j2se.utils.Clip;
import cn.harry12800.lnk.client.entity.UserInfo;
import cn.harry12800.tools.FileUtils;
import cn.harry12800.tools.Lists;

@FunctionPanelModel(configPath = "client", height = 6 * 32 + 250, width = 350, defaultDisplay = true, backgroundImage = "client_back.jpg", headerImage = "teminal.png", desc = "多端操作。")
@FunctionPanelConfig(filename = "client.json")
public class ClientExportPanel extends CorePanel<ClientJsonConfig> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int width = 350;
	public static ClientExportPanel instance;
	AreaTextPanel areaTextPanel = new AreaTextPanel();
	public ListPanel<UserInfo> listPanel;
	MButton loginBtn = new MButton("登录", 80, 25);
	MButton udptcp = new MButton("局域网方式", 80, 25);
	JLabel msgLabel = new JLabel("");
	ImageBtn setBtn = new ImageBtn(ImageUtils.getByName("post.png"));
	InputText userNameInput;
	InputText passInput;
	ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
	private SessionDialog sessionDialog;
	Client client = null;
	private List<UserInfo> userList;
	static Map<UserInfo, SessionDialog> mapsDialogByUser = new HashMap<UserInfo, SessionDialog>(0);
	static Map<Long, UserInfo> mapsUserByUserid = new HashMap<Long, UserInfo>(0);

	public ClientExportPanel(Context context) throws Exception {
		super(context);
		client = applicationContext.getBean(Client.class);
		instance = this;
		setBackground(UI.backColor);
		setLayout(null);
		this.userList = Lists.newArrayList();
		//		new DragListener(this);
		this.listPanel = new ListPanel<UserInfo>();
		listPanel.setBounds(0, 0, width, 6 * 32 + 170);
		addText();
		JScrollPane scrollPane = new JScrollPane(listPanel);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.getVerticalScrollBar().setBackground(UI.backColor);
		MyScrollBarUI myScrollBarUI = new MyScrollBarUI();
		scrollPane.getVerticalScrollBar().setUnitIncrement(20);
		scrollPane.getVerticalScrollBar().setUI(myScrollBarUI);
		// 屏蔽横向滚动条
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(0, 0, width, 6 * 32 + 170);
		add(scrollPane);
		loginBtn.setBounds(270, 6 * 32 + 250 - 70, 80, 25);
		setBtn.setBounds(300, 6 * 32 + 250 - 30, 80, 25);
		udptcp.setBounds(205, 6 * 32 + 250 - 30, 80, 25);
		msgLabel.setBounds(5, 6 * 32 + 250 - 30, 200, 25);
		add(loginBtn);
		add(udptcp);
		add(setBtn);
		add(msgLabel);
		setSize(width, 6 * 32 + 250);
		initBtnListener();
		this.sessionDialog = new SessionDialog();
		this.addWindow(sessionDialog);
		hotKey();
	}

	private void addText() throws Exception {
		userNameInput = new InputText(30);

		Builder a = new Builder();
		TextLabel userName = new TextLabel("用户名", 50, 30, a);
		TextLabel pass = new TextLabel("密  码", 50, 30, a);
		passInput = new InputText(30);
		UserInfo self2 = getConfigObject().getSelf();
		if (self2 != null) {
			userNameInput.setText(self2.getName());
			passInput.setText(self2.getToken());
		}
		add(userNameInput);
		add(passInput);
		add(userName);
		add(pass);
		userName.setBounds(5, 6 * 32 + 250 - 80, 80, 25);
		pass.setBounds(5, 6 * 32 + 250 - 60, 80, 25);
		userNameInput.setBounds(70, 6 * 32 + 250 - 80, 180, 25);
		passInput.setBounds(70, 6 * 32 + 250 - 60, 180, 25);
	}

	private void initBtnListener() {
		loginBtn.addMouseListener(new ClickAction(loginBtn) {
			public void leftClick(MouseEvent e) {
				sendLoginRequest();
			}
		});
		udptcp.addMouseListener(new ClickAction(udptcp) {
			public void leftClick(MouseEvent e) {

			}
		});
		setBtn.addMouseListener(new ClickAction(setBtn) {
			public void leftClick(MouseEvent e) {

			}
		});
		listPanel.addCallBack(new ListCallBack<UserInfo>() {
			@Override
			public void item(ItemPanel<UserInfo> itemPanel, UserInfo letter) {
				try {
					ClientExportPanel.this.sessionDialog.setClientInfo(letter);
				} catch (Exception e) {
					e.printStackTrace();
				}
				ClientExportPanel.this.sessionDialog.setVisible(true);
			}
		});
		userNameInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					sendLoginRequest();
				}
			}
		});
		passInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					sendLoginRequest();
				}
			}
		});
	}

	private void sendLoginRequest() {
		try {
			LoginRequest loginRequest = new LoginRequest();
			loginRequest.setPlayerName(userNameInput.getText());
			loginRequest.setPassward(passInput.getText());
			//构建请求
			Request request = Request.valueOf(ModuleId.USER, UserCmd.LOGIN, loginRequest.getBytes());
			client.sendRequest(request);
		} catch (Exception e) {
			msgLabel.setText("无法连接服务器");
		}
	}

	protected void pullUserList() {
		try {
			ShowAllUserRequest request = new ShowAllUserRequest();
			//构建请求
			Request request1 = Request.valueOf(ModuleId.USER, UserCmd.SHOW_ALL_USER, request.getBytes());
			client.sendRequest(request1);
		} catch (Exception e) {
			msgLabel.setText("无法连接服务器");
		}
	}

	public void showUser(List<UserInfo> lists) {
		this.userList = lists;
		listPanel.removeAll();
		for (UserInfo clientInfo : lists) {
			if (clientInfo.getName().equals(data.getSelf().getName())) {
				data.getSelf().setId(clientInfo.getId());
				continue;
			}
			ItemPanel<UserInfo> itemPanel = new ItemPanel<UserInfo>(clientInfo);
			itemPanel.setListPanel(listPanel);
			listPanel.addItem(itemPanel);
		}
		revalidate();
		pullMsg();
	}

	private void pullMsg() {
		try {
			PullMsgRequest request = new PullMsgRequest();
			request.setUserid(data.getSelf().getId());
			//构建请求
			Request request1 = Request.valueOf(ModuleId.USER, UserCmd.PULL_MSG, request.getBytes());
			client.sendRequest(request1);
			System.out.println("主动拉取信息");
		} catch (Exception e) {
			e.printStackTrace();
			msgLabel.setText("无法连接服务器");
		}
	}

	public void sendMsg(UserInfo letter, String content) {
		try {
			PrivateChatRequest request = new PrivateChatRequest();
			request.setContext(content);
			request.setTargetPlayerId(Long.valueOf(letter.getContent()));
			//构建请求
			Request req = Request.valueOf(ModuleId.CHAT, ChatCmd.PRIVATE_CHAT, request.getBytes());
			client.sendRequest(req);
		} catch (Exception e) {
			msgLabel.setText("无法连接服务器");
		}
	}

	public void showNotify(String tipContent) {
		ClientExportPanel.this.sessionDialog.showNotify(tipContent);
	}

	public void showLoginMsg(String tipContent) {
		msgLabel.setText(tipContent);
	}

	public void showPullMsg(List<MsgResponse> msgs) throws Exception {
		System.out.println("离线消息：" + msgs.size());
		for (MsgResponse msgResponse : msgs) {
			//			System.err.println(msgResponse);
			for (UserInfo userInfo : userList) {
				//				System.out.println(userInfo);
				if (msgResponse.getFromId() == userInfo.getId()) {
					ConcurrentLinkedQueue<Msg> linkedHashSet = getData().getMaps().get(userInfo.getId());
					if (linkedHashSet == null) {
						ConcurrentLinkedQueue<Msg> newArrayList = new ConcurrentLinkedQueue<>();
						newArrayList.add(new Msg(msgResponse));
						getData().getMaps().put(userInfo.getId(), newArrayList);
					} else {
						getData().getMaps().get(userInfo.getId()).add(new Msg(msgResponse));
					}
				}
			}
		}
		saveConfigObject();
	}

	public void loginSuccess(UserResponse user) {
		msgLabel.setText("登录成功！");
		UserInfo self = new UserInfo(user.getUserName(), user.getId() + "", "");
		self.setToken(passInput.getText());
		data.setSelf(self);
		pullUserList();
		pullResourceShare();
	}

	private void pullResourceShare() {
		try {
			PullResourceRequest request = new PullResourceRequest();
			request.setUserid(data.getSelf().getId());
			System.err.println(data.getSelf().getId());
			Request request1 = Request.valueOf(ModuleId.RESOURCE, ResourceShareCmd.pullAllResouces, request.getBytes());
			client.sendRequest(request1);
		} catch (Exception e) {
			msgLabel.setText("无法连接服务器");
		}
	}

	public void showPrivateChatSuccessNotify(String string, MsgResponse msg) {
		for (UserInfo clientInfo : userList) {
			if (clientInfo.getId() == msg.getToId()) {
				ClientExportPanel.this.sessionDialog.setClientInfo(clientInfo);
				ClientExportPanel.this.sessionDialog.setVisible(true);
				ClientExportPanel.this.sessionDialog.requestFocus();
				ClientExportPanel.this.sessionDialog.showReceiveNewMsg(msg);
			}
		}
		Msg e = new Msg(msg);
		ConcurrentLinkedQueue<Msg> linkedHashSet = data.getMaps().get(msg.getToId());
		if (linkedHashSet == null) {
			ConcurrentLinkedQueue<Msg> value = new ConcurrentLinkedQueue<>();
			value.add(e);
			data.getMaps().put(msg.getToId(), value);
		} else {
			data.getMaps().get(msg.getToId()).add(e);
		}
		saveConfigObject();
	}

	private void hotKey() {
		try {
			JIntellitype.getInstance().registerHotKey(105, JIntellitype.MOD_ALT, (int) '2');
			JIntellitype.getInstance().addHotKeyListener(new HotkeyListener() {
				public void onHotKey(int key) {
					if (key == 105) { // 你要做的事
						if (ClientExportPanel.instance.sessionDialog.isVisible())
							ClientExportPanel.instance.sessionDialog.dispose();
						else {
							ClientExportPanel.instance.sessionDialog.setVisible(true);
							ClientExportPanel.instance.sessionDialog.requestFocus();
						}
					}
				}
			});
		} catch (Exception e) {
			System.out.println("ALT+N 热键失败！");
		}
	}

	public void showReceiveMsg(MsgResponse msg) {
		for (UserInfo clientInfo : userList) {
			if (clientInfo.getId() == msg.getFromId()) {
				ClientExportPanel.this.sessionDialog.setClientInfo(clientInfo);
				ClientExportPanel.this.sessionDialog.setVisible(true);
				ClientExportPanel.this.sessionDialog.requestFocus();
				ClientExportPanel.this.sessionDialog.showReceiveNewMsg(msg);
				System.out.println("接收消息");
			}
		}
		Msg e = new Msg(msg);
		ConcurrentLinkedQueue<Msg> linkedHashSet = data.getMaps().get(msg.getToId());
		if (linkedHashSet == null) {
			ConcurrentLinkedQueue<Msg> value = new ConcurrentLinkedQueue<>();
			value.add(e);
			data.getMaps().put(msg.getToId(), value);
		} else {
			data.getMaps().get(msg.getToId()).add(e);
		}
		saveConfigObject();
	}

	public void shareFile(UserInfo toUser, String path, String name) {
		try {
			File file = new File(path);
			SourceShareRequest request = new SourceShareRequest();
			request.setPath(path);
			request.setResourceName(name);
			request.setResourceType(file.isFile() ? 1 : 2);
			request.setProviderId(data.getSelf().getId());
			request.setRecipientId(toUser.getId());
			if (file.isFile()) {
				byte[] file2byte = FileUtils.file2byte1(path);
				request.setData(file2byte);
			}
			//构建请求
			Request req = Request.valueOf(ModuleId.RESOURCE, ResourceShareCmd.upload_source, request.getBytes());
			client.sendRequest(req);
		} catch (Exception e) {
			e.printStackTrace();
			msgLabel.setText("无法连接服务器");
		}
	}

	public void showResources(List<Resource> lists) {
		sessionDialog.showResources(lists);
	}

	public void downloadResource(Resource letter) {
		try {
			DownLoadResourceRequest request = new DownLoadResourceRequest();
			request.setResourceId(letter.getId());
			Request r = Request.valueOf(ModuleId.RESOURCE, ResourceShareCmd.pullResouces, request.getBytes());
			client.sendRequest(r);
		} catch (Exception e) {
			msgLabel.setText("无法连接服务器");
		}
	}

	public void downloadResourceCallback(ResourceDto response) {
		FileUtils.byte2File(response.getData(), dirPath, response.getResourceName());
		try {
			Clip.openFile(dirPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void pushResourceCallback(Resource response) {
		sessionDialog.addResources(response);
	}
}
