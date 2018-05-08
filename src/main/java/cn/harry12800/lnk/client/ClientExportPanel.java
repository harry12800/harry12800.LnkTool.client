package cn.harry12800.lnk.client;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.List;

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
import cn.harry12800.common.module.player.request.ShowAllPlayerRequest;
import cn.harry12800.j2se.component.ClickAction;
import cn.harry12800.j2se.component.InputText;
import cn.harry12800.j2se.component.MButton;
import cn.harry12800.j2se.component.TextLabel;
import cn.harry12800.j2se.component.TextLabel.Builder;
import cn.harry12800.j2se.component.panel.AreaTextPanel;
import cn.harry12800.j2se.style.MyScrollBarUI;
import cn.harry12800.j2se.style.UI;
import cn.harry12800.j2se.tip.ItemPanel;
import cn.harry12800.j2se.tip.Letter;
import cn.harry12800.j2se.tip.ListPanel;
import cn.harry12800.j2se.tip.ListPanel.ListCallBack;
import cn.harry12800.lnk.client.entity.ClientInfo;
import cn.harry12800.tools.Lists;

@FunctionPanelModel(configPath = "client", height = 6 * 32 + 250, width = 350, defaultDisplay = true, backgroundImage = "client_back.jpg", headerImage = "teminal.png", desc = "多端操作。")
@FunctionPanelConfig(filename = "client.json")
public class ClientExportPanel extends CorePanel<ClientJsonConfig> implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int width = 350;
	List<ClientConnectionParam> exports;
	public static ClientExportPanel instance;
	AreaTextPanel areaTextPanel = new AreaTextPanel();
	public Context context;
	public ListPanel<ClientInfo> listPanel;
	MButton refresh = new MButton("登录", 80, 25);
	MButton set = new MButton("设置MAC地址", 80, 25);
	MButton udptcp = new MButton("UDP", 80, 25);
	public List<Letter> letters;
	InputText userNameInput;
	InputText passInput;
	ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
	Client client = null;
	private SessionDialog sessionDialog;
	private List<ClientInfo> userList;

	public ClientExportPanel(Context context) {
		super(context);
		client = applicationContext.getBean(Client.class);
		try {
			this.context = context;
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		instance = this;
		setBackground(UI.backColor);
		setLayout(null);
		this.letters = Lists.newArrayList();
		//		new DragListener(this);
		this.listPanel = new ListPanel<ClientInfo>();
		listPanel.setBounds(0, 0, width, 6 * 32 + 170);
		addText();
		JScrollPane a = new JScrollPane(listPanel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2d = (Graphics2D) g.create();
				g2d.drawImage(ImageUtils.getByName("music1.jpg"), 0, 0, getWidth() - 1, getHeight() - 1, null);
				g2d.dispose();
			}
		};
		a.setOpaque(false);
		a.getViewport().setOpaque(false);
		a.getVerticalScrollBar().setBackground(UI.backColor);
		MyScrollBarUI myScrollBarUI = new MyScrollBarUI();
		a.getVerticalScrollBar().setUnitIncrement(20);
		a.getVerticalScrollBar().setUI(myScrollBarUI);
		// 屏蔽横向滚动条
		a.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		a.setBounds(0, 0, width, 6 * 32 + 170);
		add(a);
		refresh.setBounds(270, 6 * 32 + 250 - 70, 80, 25);
		set.setBounds(105, 6 * 32 + 250 - 30, 80, 25);
		udptcp.setBounds(205, 6 * 32 + 250 - 30, 80, 25);
		add(refresh);
		add(udptcp);
		add(set);
		setSize(width, 6 * 32 + 250);
		initBtnListener();
		this.sessionDialog = new SessionDialog();
		this.addWindow(sessionDialog);
	}

	private void addText() {
		userNameInput = new InputText(30);
		userNameInput.setText("周国柱");
		Builder a = new Builder();
		TextLabel userName = new TextLabel("用户名", 50, 30, a);
		TextLabel pass = new TextLabel("密  码", 50, 30, a);
		passInput = new InputText(30);
		passInput.setText("123456");
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
		refresh.addMouseListener(new ClickAction(refresh) {
			public void leftClick(MouseEvent e) {
				refreshIP();
			}
		});
		udptcp.addMouseListener(new ClickAction(udptcp) {
			public void leftClick(MouseEvent e) {

			}
		});
		set.addMouseListener(new ClickAction(set) {
			public void leftClick(MouseEvent e) {

			}
		});
		listPanel.addCallBack(new ListCallBack<ClientInfo>() {
			@Override
			public void item(ItemPanel<ClientInfo> itemPanel, ClientInfo letter) {
				ClientExportPanel.this.sessionDialog.setClientInfo(letter);
				ClientExportPanel.this.sessionDialog.setVisible(true);
			}
		});

	}

	private void sendLogin() {
		try {
			LoginRequest loginRequest = new LoginRequest();
			loginRequest.setPlayerName(userNameInput.getText());
			loginRequest.setPassward(passInput.getText());
			//构建请求
			Request request = Request.valueOf(ModuleId.PLAYER, PlayerCmd.LOGIN, loginRequest.getBytes());
			client.sendRequest(request);
		} catch (Exception e) {
			//			tips.setText("无法连接服务器");
		}
	}

	protected void refreshIP() {
		sendLogin();
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

	public void showUser(List<ClientInfo> lists) {
		this.userList = lists;
		for (ClientInfo clientInfo : lists) {
			System.out.println(clientInfo);
			ItemPanel<ClientInfo> itemPanel = new ItemPanel<ClientInfo>(clientInfo);
			itemPanel.setListPanel(listPanel);
			listPanel.addItem(itemPanel);
		}
		revalidate();
	}

	public void sendMsg(ClientInfo letter, String content) {
		try {
			PrivateChatRequest request = new PrivateChatRequest();
			request.setContext("asdfa");
			request.setTargetPlayerId(Long.valueOf(letter.getContent()));
			//构建请求
			Request request1 = Request.valueOf(ModuleId.CHAT, ChatCmd.PRIVATE_CHAT, request.getBytes());
			client.sendRequest(request1);
		} catch (Exception e) {
			//			tips.setText("无法连接服务器");
		}
	}

	public void showMsg(long sendPlayerId, String msg) {
		for (ClientInfo clientInfo : userList) {
			if(clientInfo.getContent().equals(""+sendPlayerId)){
				ClientExportPanel.this.sessionDialog.setClientInfo(clientInfo);
				ClientExportPanel.this.sessionDialog.setVisible(true);
				ClientExportPanel.this.sessionDialog.showMsg(msg);
			}
		}
	}
}
