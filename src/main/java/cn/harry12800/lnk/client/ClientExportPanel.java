package cn.harry12800.lnk.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import cn.harry12800.common.core.model.Request;
import cn.harry12800.common.module.ChatCmd;
import cn.harry12800.common.module.ModuleId;
import cn.harry12800.common.module.ResourceShareCmd;
import cn.harry12800.common.module.UserCmd;
import cn.harry12800.common.module.chat.dto.MsgResponse;
import cn.harry12800.common.module.chat.dto.PrivateChatRequest;
import cn.harry12800.common.module.chat.dto.ResourceDto;
import cn.harry12800.common.module.chat.dto.SourceShareRequest;
import cn.harry12800.common.module.user.dto.PullMsgRequest;
import cn.harry12800.common.module.user.dto.ShowAllUserRequest;
import cn.harry12800.common.module.user.dto.UserResponse;
import cn.harry12800.j2se.component.ClickAction;
import cn.harry12800.j2se.component.InputText;
import cn.harry12800.j2se.component.MButton;
import cn.harry12800.j2se.component.btn.ImageBtn;
import cn.harry12800.j2se.component.panel.AreaTextPanel;
import cn.harry12800.j2se.style.MyScrollBarUI;
import cn.harry12800.j2se.style.UI;
import cn.harry12800.j2se.tip.ItemPanel;
import cn.harry12800.j2se.tip.ListPanel;
import cn.harry12800.j2se.tip.ListPanel.ListCallBack;
import cn.harry12800.j2se.utils.Clip;
import cn.harry12800.lnk.core.Context;
import cn.harry12800.lnk.core.CorePanel;
import cn.harry12800.lnk.core.FunctionPanelConfig;
import cn.harry12800.lnk.core.FunctionPanelModel;
import cn.harry12800.lnk.core.entity.UserInfo;
import cn.harry12800.lnk.core.util.ImageUtils;
import cn.harry12800.tools.DateUtils;
import cn.harry12800.tools.FileUtils;
import cn.harry12800.tools.Lists;

@FunctionPanelModel(configPath = "client", height = 450, width = 450, defaultDisplay = true, backgroundImage = "client_back.jpg", headerImage = "teminal.png", desc = "多端操作。")
@FunctionPanelConfig(filename = "client.json")
public class ClientExportPanel extends CorePanel<ClientJsonConfig> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int width = 600;
	public static ClientExportPanel instance;
	public ListPanel<UserInfo> listPanel;
	//	MButton udptcp = new MButton("局域网方式", 80, 25);
	JLabel msgLabel = new JLabel("");
	ImageBtn setBtn = new ImageBtn(ImageUtils.getByName("post.png"));
	ImageBtn closeButton;
	InputText userNameInput;
	JLabel userNameLabel = new JLabel("dsa");
	JLabel fileLabel = new JLabel("...");
	InputText passInput;
	JTextPane areaTextPanel = new JTextPane();
	AreaTextPanel areaTextPanel1 = new AreaTextPanel();
	String btnText = "发送";
	String clearText = "清空";
	String shareText = "共享";
	String imageText = "截图发送";
	MButton sendBtn = new MButton(btnText, 50, 30);
	MButton clearBtn = new MButton(clearText, 50, 30);
	MButton shareBtn = new MButton(shareText, 50, 30);
	MButton imageBtn = new MButton(imageText, 50, 30);
	private List<UserInfo> userList;
	static Map<UserInfo, SessionDialog> mapsDialogByUser = new HashMap<UserInfo, SessionDialog>(0);
	static Map<Long, UserInfo> mapsUserByUserid = new HashMap<Long, UserInfo>(0);
	JScrollPane scrollPane;
	JScrollPane areaTextScrollPane;
	private UserInfo toUser;

	public ClientExportPanel(Context context) {
		super(context);
		instance = this;
		this.userList = Lists.newArrayList();
		this.listPanel = new ListPanel<UserInfo>();
		scrollPane = new JScrollPane(listPanel);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.getVerticalScrollBar().setBackground(UI.backColor);
		MyScrollBarUI myScrollBarUI = new MyScrollBarUI();
		scrollPane.getVerticalScrollBar().setUnitIncrement(20);
		scrollPane.getVerticalScrollBar().setUI(myScrollBarUI);
		// 屏蔽横向滚动条
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		closeButton = new ImageBtn(ImageUtils.getByName("close24.png"));
		areaTextScrollPane = new JScrollPane(areaTextPanel);
		areaTextScrollPane.setOpaque(false);
		areaTextScrollPane.getViewport().setOpaque(false);
		areaTextScrollPane.getVerticalScrollBar().setBackground(UI.backColor(100));
		MyScrollBarUI myScrollBarUI1 = new MyScrollBarUI();
		areaTextScrollPane.getVerticalScrollBar().setUnitIncrement(20);
		areaTextScrollPane.getVerticalScrollBar().setUI(myScrollBarUI1);
		// 屏蔽横向滚动条
		areaTextScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		setProps();
		setCompBounds();
		addComps();
		addListener();
	}

	private void addComps() {
		add(scrollPane);
		add(closeButton);
		add(userNameLabel);
		add(fileLabel);
		add(areaTextScrollPane);
		add(areaTextPanel1);
		add(imageBtn);
		add(clearBtn);
		add(sendBtn);
		add(shareBtn);
	}

	private void setCompBounds() {
		listPanel.setOpaque(false);
		listPanel.setBounds(0, 0, 200, width);
		scrollPane.setBounds(0, 0, 200, width);
		areaTextScrollPane.setBounds(205, 30, 440, 320);
		closeButton.setBounds(575, 0, 25, 25);
		userNameLabel.setBounds(210, 0, 100, 25);
		fileLabel.setBounds(300, 0, 100, 25);
		areaTextPanel1.setBounds(205, 355, 440, 50);
		shareBtn.setBounds(400, 400, 50, 30);
		imageBtn.setBounds(450, 400, 50, 30);
		clearBtn.setBounds(500, 400, 50, 30);
		sendBtn.setBounds(550, 400, 50, 30);
		//		loginBtn.setBounds(270, 6 * 32 + 250 - 70, 80, 25);
		//		setBtn.setBounds(300, 6 * 32 + 250 - 30, 80, 25);
		//		udptcp.setBounds(205, 6 * 32 + 250 - 30, 80, 25);
		//		msgLabel.setBounds(5, 6 * 32 + 250 - 30, 200, 25);
	}

	private void setProps() {
		setBackground(UI.backColor);
		setLayout(null);
		setSize(width, 6 * 32 + 250);
		areaTextPanel.setBackground(UI.backColor);
		areaTextPanel.setAutoscrolls(true);
		areaTextPanel1.setSize(440, 50);
		areaTextPanel1.setPreferredSize(new Dimension(440, 50));
	}

	private void addListener() {
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ClientExportPanel.super.getContext().getFrame().dispose();
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
					setClientInfo(letter);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		sendBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e1) {
				if (!"".equals(areaTextPanel1.getText().trim()))
					sendMsg(toUser,areaTextPanel1.getText());
				areaTextPanel1.setText("");
			}
		});
		clearBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e1) {
				areaTextPanel1.setText("");
				areaTextPanel.setText("");
			}
		});
		shareBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e1) {
				JFileChooser jFileChooser = new JFileChooser();
				jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				jFileChooser.setCurrentDirectory(new File("D:/"));
				//				jFileChooser.setSelectedFile(new File("D:/a.text"));
				jFileChooser.setName("abc");
				//				jFileChooser.setFileFilter(new FileTypeFilter());
				int i = jFileChooser.showOpenDialog(null);
				if (i == JFileChooser.APPROVE_OPTION) { //打开文件
					String path = jFileChooser.getSelectedFile().getAbsolutePath();
					String name = jFileChooser.getSelectedFile().getName();
					System.out.println(path);
					System.out.println(name);
					//					shareFile(path, name);
				}
			}
		});
	}

	public void setClientInfo(UserInfo letter) {
		this.toUser = letter;
		ConcurrentLinkedQueue<Msg> linkedHashSet = data.getMapsSessionData().get(1).getMaps().get(letter.getId());

		//		String info = appendAllChatMsg(linkedHashSet);
		if (linkedHashSet == null)
			System.out.println("本地数据条数。" + 0);
		else
			System.out.println("本地数据条数。" + linkedHashSet.size());
		userNameLabel.setText(letter.getTitle());
		if (linkedHashSet != null) {
			linkedHashSet.stream().forEach(msg -> {
				showReceiveNewMsg(msg);
			});
		}
		//		sessionPanel.areaTextPanel.setText(info);

		//		listPanel.addCallBack(new ListCallBack<Resource>() {
		//			@Override
		//			public void item(ItemPanel<Resource> itemPanel, Resource letter) {
		//				ClientExportPanel.instance.downloadResource(letter);
		//			}
		//		});
	}

	public void sendLoginRequest() {
		//		try {
		//			LoginRequest loginRequest = new LoginRequest();
		//			loginRequest.setPlayerName(userNameInput.getText());
		//			loginRequest.setPassward(passInput.getText());
		//			//构建请求
		//			Request request = Request.valueOf(ModuleId.USER, UserCmd.LOGIN, loginRequest.getBytes());
		//			client.sendRequest(request);
		//		} catch (Exception e) {
		//			msgLabel.setText("无法连接服务器");
		//		}
	}

	protected void pullUserList() {
		try {
			ShowAllUserRequest request = new ShowAllUserRequest();
//			request.setUserId(self);
			//构建请求
			Request request1 = Request.valueOf(ModuleId.USER, UserCmd.SHOW_ALL_USER, request.getBytes());
			client.sendRequest(request1);
		} catch (Exception e) {
			e.printStackTrace();
			msgLabel.setText("无法连接服务器");
		}
	}

	public void showUser(List<UserInfo> lists) {
		this.userList = lists;
		listPanel.removeAll();
		for (UserInfo clientInfo : lists) {
			ItemPanel<UserInfo> itemPanel = new ItemPanel<UserInfo>(clientInfo);
			itemPanel.setListPanel(listPanel);
			listPanel.addItem(itemPanel);
		}
		listPanel.updateUI();
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
//		 showNotify(tipContent);
	}

	public void showLoginMsg(String tipContent) {
		msgLabel.setText(tipContent);
	}

	public void showPullMsg(List<MsgResponse> msgs) throws Exception {
		System.out.println("离线消息：" + msgs.size());
		for (MsgResponse msg : msgs) {
			String string = new String(msg.getData());
			System.err.println("-------------:" + string);
			System.out.println(msg);
			//			System.err.println(msgResponse);
			for (UserInfo userInfo : userList) {
				//				System.out.println(userInfo);
				if (msg.getFromId() == userInfo.getId()) {
					ConcurrentLinkedQueue<Msg> linkedHashSet = getData().getMaps().get(userInfo.getId());
					if (linkedHashSet == null) {
						ConcurrentLinkedQueue<Msg> newArrayList = new ConcurrentLinkedQueue<>();
						newArrayList.add(new Msg(msg));
						getData().getMaps().put(userInfo.getId(), newArrayList);
					} else {
						getData().getMaps().get(userInfo.getId()).add(new Msg(msg));
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
		//		try {
		//			PullResourceRequest request = new PullResourceRequest();
		//			request.setUserid(data.getSelf().getId());
		//			System.err.println(data.getSelf().getId());
		//			Request request1 = Request.valueOf(ModuleId.RESOURCE, ResourceShareCmd.pullAllResouces, request.getBytes());
		//			client.sendRequest(request1);
		//		} catch (Exception e) {
		//			msgLabel.setText("无法连接服务器");
		//		}
	}

	public void showPrivateChatSuccessNotify(String string, MsgResponse msg) {
		for (UserInfo clientInfo : userList) {
			if (clientInfo.getId() == msg.getToId()) {
				requestFocus();
				Msg m = new Msg(msg);
				showReceiveNewMsg(m);
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

	public void showReceiveMsg(MsgResponse msg) {
		for (UserInfo clientInfo : userList) {
			if (clientInfo.getId() == msg.getFromId()) {
				Msg m = new Msg(msg);
				showReceiveNewMsg(m);
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
//		sessionDialog.showResources(lists);
	}

	public void downloadResource(Resource letter) {
		//		try {
		//			DownLoadResourceRequest request = new DownLoadResourceRequest();
		//			request.setResourceId(letter.getId());
		//			Request r = Request.valueOf(ModuleId.RESOURCE, ResourceShareCmd.pullResouces, request.getBytes());
		//			client.sendRequest(r);
		//		} catch (Exception e) {
		//			msgLabel.setText("无法连接服务器");
		//		}
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
//		sessionDialog.addResources(response);
	}

	@Override
	public void initLoadData() {
		pullUserList();
	}

	public void showReceiveNewMsg(Msg m) {
		StringBuilder builderHeader = new StringBuilder();
		StringBuilder builderBody = new StringBuilder();
		boolean isTo = false;
		if (m.getFromPlayerId() == toUser.getId())
			isTo = true;
		if (isTo) {
			if (m.getOnline() == 2)
				builderHeader.append("（收到离线消息）");
			builderHeader.append(toUser.getName());
			builderHeader.append("[");
			builderHeader.append(toUser.getId());
			builderHeader.append("]");
			builderHeader.append(" 悄悄对你说:（" + DateUtils.getTimeByFormat(m.getSendTime(), "MM-dd HH:mm:ss") + "）\n");
			builderBody.append(new String(m.getData()));
			builderBody.append("\n\n");
			insertHeader(builderHeader.toString(), false, 12, StyleConstants.ALIGN_LEFT);
			insertBody(builderBody.toString(), false, 12, StyleConstants.ALIGN_LEFT);
		} else {
			if (m.getOnline() == 2)
				builderHeader.append("（对方离线消息）");
			builderHeader.append("你悄悄对[" + toUser.getName() + "]说:（" + DateUtils.getTimeByFormat(m.getSendTime(), "MM-dd HH:mm:ss") + "）\n");
			builderBody.append(new String(m.getData()));
			builderBody.append("\n\n");
			insertHeader(builderHeader.toString(), false, 12, StyleConstants.ALIGN_RIGHT);
			System.out.println(34);
			insertBody(builderBody.toString(), false, 12, StyleConstants.ALIGN_RIGHT);
		}
	}

	private void insertHeader(String str, boolean bold, int fontSize, int align) {
		SimpleAttributeSet attrSet = new SimpleAttributeSet();
		StyleConstants.setAlignment(attrSet, align);
		StyleConstants.setForeground(attrSet, Color.red);
		//Component c=new JLabel("asd");
		//StyleConstants.setComponent(attrSet, c);
		// 颜色
		if (bold == true) {
			StyleConstants.setBold(attrSet, true);
		} // 字体类型
		StyleConstants.setFontSize(attrSet, fontSize);
		// 字体大小
		// StyleConstants.setFontFamily(attrSet, "黑体");
		// 设置字体
		insert(str, attrSet);
	}

	private void insertBody(String str, boolean bold, int fontSize, int align) {
		SimpleAttributeSet attrSet = new SimpleAttributeSet();
		StyleConstants.setForeground(attrSet, Color.WHITE);
		StyleConstants.setAlignment(attrSet, align);
		//Component c=new JLabel("asd");
		//StyleConstants.setComponent(attrSet, c);
		// 颜色
		if (bold) {
			StyleConstants.setBold(attrSet, true);
		} // 字体类型
		StyleConstants.setFontSize(attrSet, fontSize);
		// 字体大小
		// StyleConstants.setFontFamily(attrSet, "黑体");
		// 设置字体

		insert(str, attrSet);
	}

	private void insert(String str, AttributeSet attrSet) {
		JTextPane textPane = areaTextPanel;
		//		StyledDocument styledDocument = textPane.getStyledDocument();
		Document doc = textPane.getDocument();
		//		int length = doc.getLength();
		try {
			doc.insertString(doc.getLength(), str, attrSet);
			//			styledDocument.setCharacterAttributes(length, doc.getLength(), attrSet, false);
			textPane.setCaretPosition(doc.getLength() - 1);
			textPane.setEditable(true);
			textPane.setEnabled(true);
		} catch (BadLocationException e) {
			System.out.println("BadLocationException: " + e);
		}
	}
}
