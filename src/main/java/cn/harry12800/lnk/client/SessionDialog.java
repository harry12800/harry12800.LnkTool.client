package cn.harry12800.lnk.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.ConcurrentLinkedQueue;

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
		ConcurrentLinkedQueue<Msg> linkedHashSet = ClientExportPanel.instance.getData().getMaps().get(letter.getId());
		String info = appendAllChatMsg(linkedHashSet);
		if(linkedHashSet == null)
		System.out.println("本地数据条数。"+0);
		else System.out.println("本地数据条数。"+linkedHashSet.size());
		sessionPanel.setTitle(letter.getTitle());
		sessionPanel.areaTextPanel.setText(info);
		sessionPanel.addSendEvent(new SendEvent() {
			public void send(String content) {
				ClientExportPanel.instance.sendMsg(letter, content);
			}
		});
	}

	private String appendAllChatMsg(ConcurrentLinkedQueue<Msg> linkedHashSet) {
		if (linkedHashSet == null)
			return "";
		StringBuilder builder = new StringBuilder();
		for (Msg m : linkedHashSet) {
			boolean isTo = false;
			if (m.getFromPlayerId() == toUser.getId())
				isTo = true;
			if (isTo) {
				if (m.getOnline() == 2)
					builder.append("（收到离线消息）");
				builder.append(toUser.getName());
				builder.append("[");
				builder.append(toUser.getId());
				builder.append("]");
				builder.append(" 悄悄对你说:（" + DateUtils.getTimeByFormat(m.getSendTime(), "MM-dd HH:mm:ss") + "）\n");
				builder.append(new String(m.getData()));
				builder.append("\n\n");
			} else {
				if (m.getOnline() == 2)
					builder.append("（对方离线消息）");
				builder.append("你悄悄对[" + toUser.getName() + "]说:（" + DateUtils.getTimeByFormat(m.getSendTime(), "MM-dd HH:mm:ss") + "）\n");
				builder.append(new String(m.getData()));
				builder.append("\n\n");
			}
		}
		return builder.toString();
	}

	public void showReceiveNewMsg(MsgResponse m) {
		StringBuilder builder = new StringBuilder();
		String text = sessionPanel.areaTextPanel.getText().trim();
		boolean isTo = false;
		if (m.getId() == toUser.getId())
			isTo = true;
		if (isTo) {
			if (m.getOnline() == 2)
				builder.append("（收到离线消息）");
			builder.append(toUser.getName());
			builder.append("[");
			builder.append(toUser.getId());
			builder.append("]");
			builder.append(" 悄悄对你说:（" + DateUtils.getTimeByFormat(m.getSendTime(), "MM-dd HH:mm:ss") + "）\n");
			builder.append(new String(m.getData()));
			builder.append("\n\n");
		} else {
			if (m.getOnline() == 2)
				builder.append("（对方离线消息）");
			builder.append("你悄悄对[" + toUser.getName() + "]说:（" + DateUtils.getTimeByFormat(m.getSendTime(), "MM-dd HH:mm:ss") + "）\n");
			builder.append(new String(m.getData()));
			builder.append("\n\n");
		}
		if(!text.isEmpty())
			sessionPanel.areaTextPanel.setText(text + "\n\n"+builder.toString());
		else sessionPanel.areaTextPanel.setText(builder.toString());
	}

	public void showNotify(String tipContent) {
		sessionPanel.notifyLabel.setText(tipContent);
	}

	@Override
	public void requestFocus() {
		super.requestFocus();
		sessionPanel.areaTextPanel1.requestFocus();
		sessionPanel.areaTextPanel1.area.setCaretPosition( sessionPanel.areaTextPanel1.area.getDocument().getLength());
	}

	/**
	 * 清空本回话好友的本地数据。
	 */
	public void clearChatMsg() {
		ClientExportPanel.instance.getData().getMaps().remove(toUser.getId());
		ClientExportPanel.instance.saveConfigObject();
	}

	public void shareFile(String path, String name) {
		ClientExportPanel.instance.shareFile(toUser,path,name);
	}
}
