package cn.harry12800.lnk.client;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import cn.harry12800.Lnk.core.util.ImageUtils;
import cn.harry12800.j2se.component.MButton;
import cn.harry12800.j2se.component.btn.ImageBtn;
import cn.harry12800.j2se.component.panel.AreaTextPanel;
import cn.harry12800.j2se.style.MyScrollBarUI;
import cn.harry12800.j2se.style.UI;
import cn.harry12800.j2se.tip.ListPanel;

@SuppressWarnings("serial")
public class SessionPanel extends JPanel implements KeyListener {

	ImageBtn closeButton;
	  JLabel titleLabel = new JLabel("语言翻译");
	  JLabel notifyLabel = new JLabel("");
	AreaTextPanel areaTextPanel = new AreaTextPanel();
	AreaTextPanel areaTextPanel1 = new AreaTextPanel();
	String btnText = "发送";
	String clearText = "清空";
	String shareText = "共享";
	MButton sendBtn = new MButton(btnText, 50, 30);
	MButton clearBtn = new MButton(clearText, 50, 30);
	MButton shareBtn = new MButton(shareText, 50, 30);
	private SessionDialog dialog;
	SendEvent e;
	public ListPanel<Resource> listPanel;

	public static interface SendEvent {
		void send(String conent);
	}

	void addSendEvent(SendEvent e) {
		this.e = e;
	}

	public SessionPanel(SessionDialog dialog) {
		this.dialog = dialog;
		setOpaque(false);
		closeButton = new ImageBtn(ImageUtils.getByName("close24.png"));
		setProps();
		setLayout(null);
		initComponent();
		addComponent();
		initCompBounds();
		initCompListener();
		addKeyListener(this);
	}

	private void initComponent() {
		this.listPanel = new ListPanel<Resource>();
		listPanel.setBounds(502, 30, 120, 345);
		JScrollPane scrollPane = new JScrollPane(listPanel);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.getVerticalScrollBar().setBackground(UI.backColor);
		MyScrollBarUI myScrollBarUI = new MyScrollBarUI();
		scrollPane.getVerticalScrollBar().setUnitIncrement(20);
		scrollPane.getVerticalScrollBar().setUI(myScrollBarUI);
		// 屏蔽横向滚动条
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(502, 30, 120, 345);
		add(scrollPane);
	}

	private void setProps() {
		titleLabel.setFont(UI.微软雅黑Font);
		titleLabel.setForeground(UI.fontColor);
		areaTextPanel.setSize(500, 250);
		areaTextPanel.setPreferredSize(new Dimension(500, 250));
		areaTextPanel1.setSize(500, 50);
		areaTextPanel1.setPreferredSize(new Dimension(500, 50));
	}

	private void initCompListener() {
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});
		sendBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e1) {
				if(!"".equals(areaTextPanel1.getText().trim()))
					e.send(areaTextPanel1.getText());
				areaTextPanel1.setText("");
			}
		});
		clearBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e1) {
				areaTextPanel1.setText("");
				areaTextPanel.setText("");
				dialog.clearChatMsg();
			}
		});
		shareBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e1) {
				JFileChooser jFileChooser = new JFileChooser();
				jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				jFileChooser.setCurrentDirectory(new File("D:/"));
				jFileChooser.setSelectedFile(new File("D:/a.text"));
				jFileChooser.setName("abc");
//				jFileChooser.setFileFilter(new FileTypeFilter());
				int i = jFileChooser.showOpenDialog(null);
				if (i == JFileChooser.APPROVE_OPTION) { //打开文件
					String path = jFileChooser.getSelectedFile().getAbsolutePath();
					String name = jFileChooser.getSelectedFile().getName();
					System.out.println(path); System.out.println(name);
					shareFile(path,name);
				}
			}
		});
		areaTextPanel1.addKeyListener(this);
		areaTextPanel.addKeyListener(this);
	}
	private void  shareFile(String path ,String name) {
		dialog.shareFile( path , name) ;
	}
	private void initCompBounds() {
		titleLabel.setBounds(2, 0, 200, 25);
		closeButton.setBounds(585, 0, 25, 25);
		areaTextPanel.setBounds(5, 30, 500, 250);
		areaTextPanel1.setBounds(5, 285, 500, 50);
		sendBtn.setBounds(430, 340, 50, 30);
		clearBtn.setBounds(380, 340, 50, 30);
		shareBtn.setBounds(300, 340, 50, 30);
		notifyLabel.setBounds(5, 340, 200, 30);
	}

	private void addComponent() {
		add(closeButton);
		add(areaTextPanel);
		add(areaTextPanel1);
		add(titleLabel);
		add(sendBtn);
		add(clearBtn);
		add(shareBtn);
		add(notifyLabel);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e1) {
		if (e1.getKeyCode() == 27) {
			dialog.setVisible(false);
		} else if (e1.getKeyCode() == 10) {
			if(!"".equals(areaTextPanel1.getText().trim()))
				e.send(areaTextPanel1.getText());
			areaTextPanel1.setText("");
		}
	}

	public void setTitle(String title) {
		titleLabel.setText(title);
	}
}
