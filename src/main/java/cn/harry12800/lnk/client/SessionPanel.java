package cn.harry12800.lnk.client;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import cn.harry12800.Lnk.core.util.ImageUtils;
import cn.harry12800.j2se.component.MButton;
import cn.harry12800.j2se.component.btn.ImageBtn;
import cn.harry12800.j2se.component.panel.AreaTextPanel;
import cn.harry12800.j2se.style.UI;

@SuppressWarnings("serial")
public class SessionPanel extends JPanel implements KeyListener {

	ImageBtn closeButton;
	private JLabel titleLabel = new JLabel("语言翻译");
	AreaTextPanel areaTextPanel = new AreaTextPanel();
	AreaTextPanel areaTextPanel1 = new AreaTextPanel();
	String btnText = "发送";
	MButton OkBtn = new MButton(btnText, 50, 30);
	private SessionDialog dialog;
	SendEvent e;
	public static interface SendEvent{
		void send(String conent);
	}
	void addSendEvent(SendEvent e){
		this.e  = e;
	}
	public SessionPanel( SessionDialog dialog  ) {
		this.dialog = dialog;
		setOpaque(false);
		closeButton = new ImageBtn(ImageUtils.getByName("close24.png"));
		setProps();
		setLayout(null);
		addComponent();
		initCompBounds();
		initCompListener();
		addKeyListener(this);
	}

	private void setProps() {
		titleLabel.setFont(UI.微软雅黑Font);
		titleLabel.setForeground(UI.fontColor);
		areaTextPanel.setSize(500, 250);
		areaTextPanel.setPreferredSize(new Dimension(500, 250));
		areaTextPanel1.setSize(500, 50);
		areaTextPanel1.setPreferredSize(new Dimension(500, 50));
		//		OkBtn.bgcolor = UI.foreColor;
		//		OkBtn1.bgcolor = UI.foreColor;
	}

	private void initCompListener() {
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});
		OkBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e1) {
				e.send(areaTextPanel1.getText());
			}
		});
		areaTextPanel1.addKeyListener(this);
		areaTextPanel.addKeyListener(this);
	}

	private void initCompBounds() {
		titleLabel.setBounds(2, 0, 200, 25);
		closeButton.setBounds(485, 0, 25, 25);
		areaTextPanel.setBounds(5, 30, 500, 250);
		areaTextPanel1.setBounds(5, 285, 500, 50);
		OkBtn.setBounds(420, 340, 50, 30);
	}

	private void addComponent() {
		add(closeButton);
		add(areaTextPanel);
		add(areaTextPanel1);
		add(titleLabel);
		add(OkBtn);
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
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == 27) {
			dialog.setVisible(false);
		}
	}

	public void setTitle(String title) {
		titleLabel.setText(title);
	}
}
