package cn.harry12800.lnk.client;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.harry12800.Lnk.core.Context;
import cn.harry12800.Lnk.core.CorePanel;
import cn.harry12800.Lnk.core.FunctionPanelConfig;
import cn.harry12800.Lnk.core.FunctionPanelModel;
import cn.harry12800.Lnk.core.util.ImageUtils;
import cn.harry12800.client.Client;
import cn.harry12800.common.core.model.Request;
import cn.harry12800.common.module.ModuleId;
import cn.harry12800.common.module.chat.ChatCmd;
import cn.harry12800.common.module.chat.request.PrivateChatRequest;
import cn.harry12800.common.module.player.PlayerCmd;
import cn.harry12800.common.module.player.request.LoginRequest;
import cn.harry12800.common.module.player.request.PullMsgRequest;
import cn.harry12800.common.module.player.request.ShowAllPlayerRequest;
import cn.harry12800.common.module.player.response.MsgResponse;
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
import cn.harry12800.lnk.client.entity.UserInfo;
import cn.harry12800.tools.Lists;

@FunctionPanelModel(configPath = "client", height = 6 * 32 + 250, width = 350, defaultDisplay = true, backgroundImage = "client_back.jpg", headerImage = "teminal.png", desc = "多端操作。")
@FunctionPanelConfig(filename = "client.json")
public class ClientExportPanel extends CorePanel<ClientJsonConfig> implements ActionListener {
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
	UserInfo self = null;
	private List<UserInfo> userList;
	static Map<UserInfo, SessionDialog> mapsDialogByUser = new HashMap<UserInfo, SessionDialog>(0);

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
				sendLogin();
				refreshIP();
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
					sendLogin();
					refreshIP();
				}
			}
		});
		passInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					sendLogin();
					refreshIP();
				}
			}
		});
	}

	private void sendLogin() {
		try {
			LoginRequest loginRequest = new LoginRequest();
			loginRequest.setPlayerName(userNameInput.getText());
			loginRequest.setPassward(passInput.getText());
			self = new UserInfo(userNameInput.getText(), "", "");
			self.setToken(passInput.getText());
			//构建请求
			Request request = Request.valueOf(ModuleId.PLAYER, PlayerCmd.LOGIN, loginRequest.getBytes());
			client.sendRequest(request);
			getConfigObject().setSelf(self);
			saveConfigObject();
		} catch (Exception e) {
			//			tips.setText("无法连接服务器");
		}
	}

	protected void refreshIP() {
		try {
			ShowAllPlayerRequest request = new ShowAllPlayerRequest();
			//构建请求
			Request request1 = Request.valueOf(ModuleId.PLAYER, PlayerCmd.SHOW_ALL_USER, request.getBytes());
			client.sendRequest(request1);
		} catch (Exception e) {
			//			tips.setText("无法连接服务器");
		}
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();
		GradientPaint p2 = new GradientPaint(0, 1,
				new Color(186, 131, 164, 200), 0, 20, new Color(255, 255, 255,
						255));
		g2d.setPaint(p2);
		g2d.drawRoundRect(1, 20, getWidth() - 5, 6 * 32, 5, 5);
		g2d.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_SQUARE,
				BasicStroke.JOIN_ROUND)); // 设置新的画刷
		g2d.setFont(new Font("宋体", Font.PLAIN, 12));
		g2d.drawString("数据库", 5, 15);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}

	public void showUser(List<UserInfo> lists) {
		this.userList = lists;
		listPanel.removeAll();
		for (UserInfo clientInfo : lists) {
			System.out.println(clientInfo);
			if (self.getTitle().equals(clientInfo.getTitle())) {
				self.setId(clientInfo.getId());
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
			request.setUserid(Long.valueOf(self.getId()));
			//构建请求
			Request request1 = Request.valueOf(ModuleId.PLAYER, PlayerCmd.PULL_MSG, request.getBytes());
			client.sendRequest(request1);
			System.out.println("主动拉取信息");
		} catch (Exception e) {
			e.printStackTrace();
			//			tips.setText("无法连接服务器");
		}
	}

	public void sendMsg(UserInfo letter, String content) {
		try {
			PrivateChatRequest request = new PrivateChatRequest();
			request.setContext(content);
			request.setTargetPlayerId(Long.valueOf(letter.getContent()));
			//构建请求
			Request request1 = Request.valueOf(ModuleId.CHAT, ChatCmd.PRIVATE_CHAT, request.getBytes());
			client.sendRequest(request1);
		} catch (Exception e) {
			//			tips.setText("无法连接服务器");
		}
	}

	public void showMsg(long sendPlayerId, String msg) throws Exception {
		for (UserInfo clientInfo : userList) {
			if (clientInfo.getContent().equals("" + sendPlayerId)) {
				ClientExportPanel.this.sessionDialog.setClientInfo(clientInfo);
				ClientExportPanel.this.sessionDialog.setVisible(true);
				ClientExportPanel.this.sessionDialog.requestFocus();
				ClientExportPanel.this.sessionDialog.showMsg(msg);
			}
		}
	}

	public void showNotify(String tipContent) {
		ClientExportPanel.this.sessionDialog.showNotify(tipContent);
	}

	public void showLoginMsg(String tipContent) {
		msgLabel.setText(tipContent);
	}

	
	public void showPullMsg(List<MsgResponse> msgs) throws Exception {
		for (MsgResponse msgResponse : msgs) {
			System.err.println(msgResponse);
			for (UserInfo userInfo : userList) {
				if((msgResponse.getFromPlayerId()+"").equals( userInfo.getId())) {
					List<Msg> list = data.getMaps().get(userInfo.getId());
					if(list == null) {
						List<Msg> newArrayList = Lists.newArrayList();
						newArrayList.add(new Msg(msgResponse));
						data.getMaps().put(userInfo.getId(), newArrayList);
					}else {
						data.getMaps().get(userInfo.getId()).add(new Msg(msgResponse));
					}
				}
			}
		}
		saveConfigObject();
	}
}
