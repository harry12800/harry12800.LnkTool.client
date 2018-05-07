package cn.harry12800.lnk.client;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import cn.harry12800.Lnk.core.Context;
import cn.harry12800.Lnk.core.CorePanel;
import cn.harry12800.Lnk.core.FunctionPanelConfig;
import cn.harry12800.Lnk.core.FunctionPanelModel;
import cn.harry12800.Lnk.core.util.ImageUtils;
import cn.harry12800.j2se.component.ClickAction;
import cn.harry12800.j2se.component.MButton;
import cn.harry12800.j2se.component.panel.AreaTextPanel;
import cn.harry12800.j2se.style.MyScrollBarUI;
import cn.harry12800.j2se.style.UI;
import cn.harry12800.j2se.tip.ItemPanel;
import cn.harry12800.j2se.tip.Letter;
import cn.harry12800.j2se.tip.ListPanel;
import cn.harry12800.lnk.client.accept.BroadcastReceiveThread;
import cn.harry12800.lnk.client.accept.NotifyAll.NotifyCallback;
import cn.harry12800.lnk.client.entity.ClientInfo;
import cn.harry12800.tools.Lists;

@FunctionPanelModel(configPath = "client", height = 6 * 32 + 200, width = 350, defaultDisplay = true, backgroundImage = "client_back.jpg", headerImage = "teminal.png", desc = "多端操作。")
@FunctionPanelConfig(filename = "client.json")
public class ClientExportPanel extends CorePanel<ClientJsonConfig> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int width = 350;
	List<ClientConnectionParam> exports;
	AreaTextPanel areaTextPanel = new AreaTextPanel();
	public Context context;
	public ListPanel<ClientInfo> listPanel;
	MButton refresh = new MButton("刷新", 80, 25);
	MButton set = new MButton("设置MAC地址", 80, 25);
	MButton udptcp = new MButton("UDP", 80, 25);
	public List<Letter> letters;

	
	public ClientExportPanel(Context context) {
		super(context);
		try {
			this.context = context;
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		setBackground(UI.backColor);
		setLayout(null);
		this.letters = Lists.newArrayList();
		//		new DragListener(this);
		this.listPanel = new ListPanel<ClientInfo>();
		listPanel.setBounds(0, 0, width, 6 * 32 + 200);
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
		a.setBounds(0, 0, width, 6 * 32 + 200);
		add(a);
		refresh.setBounds(5, 6 * 32 + 250 - 30, 80, 25);
		set.setBounds(105, 6 * 32 + 250 - 30, 80, 25);
		udptcp.setBounds(205, 6 * 32 + 250 - 30, 80, 25);
		add(refresh);
		add(udptcp);
		add(set);
		setSize(width, 6 * 32 + 250);
		initBtnListener();
		new BroadcastReceiveThread().start();
		try {
			BroadcastReceiveThread.send();
		} catch (Exception e) {
			e.printStackTrace();
		}
		NotifyCallback callback = new NotifyCallback() {
			@Override
			public void notifyClientinfo(ClientInfo clientinfo) {
				letters.add(clientinfo);
				ItemPanel<ClientInfo> itemPanel = new ItemPanel<ClientInfo>(clientinfo);
				listPanel.addItem(itemPanel);
			}
		};
		new SessionDialog(this);
//		new NotifyAll( callback ).start();
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
	}

	protected void refreshIP() {

	}

	public static void main(String[] args) {
		try {
			//			Main.main(null);
		} catch (Exception e) {
			e.printStackTrace();
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
}
