package cn.harry12800.lnk.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;

import cn.harry12800.Lnk.core.util.ImageUtils;
import cn.harry12800.common.module.player.response.MsgResponse;
import cn.harry12800.j2se.action.DragListener;
import cn.harry12800.lnk.client.SessionPanel.SendEvent;
import cn.harry12800.lnk.client.entity.UserInfo;
import cn.harry12800.tools.DateUtils;

public class SessionDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3402650173117339424L;
	public static SessionDialog instance;
	private UserInfo toUser;
	private UserInfo formUser;
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

	public void setClientInfo(UserInfo letter) {
		this.toUser = letter;
		this.formUser = ClientExportPanel.instance.getData().getSelf();
		List<Msg> list = ClientExportPanel.instance.getData().getMaps().get(letter.getId());
		String info = appendChatMsg(list);
		sessionPanel.setTitle(letter.getTitle());
		sessionPanel.areaTextPanel.setText(info);
		sessionPanel.addSendEvent(new SendEvent() {
			public void send(String content) {
				ClientExportPanel.instance.sendMsg(letter, content);
			}
		});
	}

	private String appendChatMsg(List<Msg> list) {
		StringBuilder builder = new StringBuilder();

		if (list == null)
			return "";
		for (Msg m : list) {
			boolean isTo = false;
			if (m.getFromPlayerId() == toUser.getId())
				isTo = true;
			if (isTo) {
				builder.append(toUser.getName());
				builder.append("[");
				builder.append(toUser.getId());
				builder.append("]");
				builder.append(" 悄悄对你说:\n（" + DateUtils.getTimeByFormat(m.getSendTime(), "yyyy-MM-dd HH:mm") + "）\t");
				builder.append(new String(m.getData()));
				builder.append("\n\n");
			} else {
				builder.append("你悄悄对[" + toUser.getName() + "]说:\n（" + DateUtils.getTimeByFormat(m.getSendTime(), "yyyy-MM-dd HH:mm") + "）\t");
				builder.append(new String(m.getData()));
				builder.append("\n\n");
			}
		}
		return builder.toString();
	}

	public void showReceiveMsg(MsgResponse m) {
		StringBuilder builder = new StringBuilder();
		String text = sessionPanel.areaTextPanel.getText();
		boolean isTo = true;
		if (m.getFromPlayerId() == toUser.getId())
			isTo = false;
		if (isTo) {
			builder.append(toUser.getName());
			builder.append("[");
			builder.append(toUser.getId());
			builder.append("]");
			builder.append(" 悄悄对你说:\n（" + DateUtils.getTimeByFormat(m.getSendTime(), "yyyy-MM-dd HH:mm") + "）\t");
			builder.append(new String(m.getData()));
			builder.append("\n\n");
		} else {
			builder.append("你悄悄对[" + toUser.getName() + "]说:\n（" + DateUtils.getTimeByFormat(m.getSendTime(), "yyyy-MM-dd HH:mm") + "）\t");
			builder.append(new String(m.getData()));
			builder.append("\n\n");
		}
		int online = m.getOnline();
		if(online==2) {
			sessionPanel.areaTextPanel.setText(text +"\r\n\r\n(对方离线消息)" +builder.toString());
		}else {
			sessionPanel.areaTextPanel.setText(text +"\r\n\r\n" +builder.toString());
		}
	}

	public void showNotify(String tipContent) {
		sessionPanel.notifyLabel.setText(tipContent);
	}

	@Override
	public void requestFocus() {
		super.requestFocus();
		sessionPanel.areaTextPanel1.requestFocus();
	}
}
