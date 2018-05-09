package cn.harry12800.lnk.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;

import cn.harry12800.Lnk.core.util.ImageUtils;
import cn.harry12800.j2se.action.DragListener;
import cn.harry12800.lnk.client.SessionPanel.SendEvent;
import cn.harry12800.lnk.client.entity.UserInfo;

public class SessionDialog extends JDialog{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3402650173117339424L;
	public static SessionDialog instance;
	private UserInfo letter;
	private SessionPanel sessionPanel;

	public SessionDialog() {
		setUndecorated(true);
		new DragListener(this);
		setLayout(new BorderLayout(0, 0));
		getLayeredPane().setOpaque(false);
		getRootPane().setOpaque(false);
		this.sessionPanel = new SessionPanel(this);
		setContentPane(sessionPanel);
		setSize(510, 370);
		setBackground();
		setLocationRelativeTo(null);
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == 27) {
					setVisible(false);
				}
			}
		});
	}

	JLabel picture;
	static final String default_back = "default_back.jpg";
	public void setBackground() {
		ImageIcon image = new ImageIcon(ImageUtils.getByName(default_back));
		getLayeredPane().setLayout(null);
		image.setImage(image.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_DEFAULT));
		picture = new JLabel(image);
		picture.setBounds(0, 0, getWidth(), getHeight());
		getLayeredPane().add(picture, new Integer(Integer.MIN_VALUE));
	}

	public void setCenterScreen() {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		int w = (int) d.getWidth();
		int h = (int) d.getHeight();
		this.setLocation((w - this.getWidth()) / 2, (h - this.getHeight()) / 2);
	}

	public static void main(String[] args) {
		new SessionDialog();
	}

	public void setClientInfo(UserInfo letter) {
		this.letter = letter;
		sessionPanel.setTitle(letter.getTitle());
		sessionPanel.addSendEvent(new SendEvent() {
			public void send(String content) {
				ClientExportPanel.instance.sendMsg(letter,content);
			}
		});
	}

	public void showMsg(String msg) {
		String text = sessionPanel.areaTextPanel.getText();
		sessionPanel.areaTextPanel.setText(text+"\r\n"+msg);
	}
}
