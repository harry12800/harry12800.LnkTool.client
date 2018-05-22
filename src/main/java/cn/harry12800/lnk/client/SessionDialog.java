package cn.harry12800.lnk.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import cn.harry12800.lnk.core.util.ImageUtils;
import cn.harry12800.j2se.action.DragListener;
import cn.harry12800.j2se.tip.ItemPanel;
import cn.harry12800.j2se.tip.ListPanel.ListCallBack;
import cn.harry12800.lnk.core.entity.UserInfo;
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
		setSize(610, 370);
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

		//		String info = appendAllChatMsg(linkedHashSet);
		if (linkedHashSet == null)
			System.out.println("本地数据条数。" + 0);
		else
			System.out.println("本地数据条数。" + linkedHashSet.size());
		sessionPanel.setTitle(letter.getTitle());
		if (linkedHashSet != null) {
			linkedHashSet.stream().forEach(msg -> {
				showReceiveNewMsg(msg);
			});
		}
		//		sessionPanel.areaTextPanel.setText(info);
		sessionPanel.addSendEvent(
				content -> ClientExportPanel.instance.sendMsg(letter, content));
		sessionPanel.listPanel.addCallBack(new ListCallBack<Resource>() {
			@Override
			public void item(ItemPanel<Resource> itemPanel, Resource letter) {
				ClientExportPanel.instance.downloadResource(letter);
			}
		});
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

	public void showNotify(String tipContent) {
		sessionPanel.notifyLabel.setText(tipContent);
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
		JTextPane textPane = sessionPanel.areaTextPanel;
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

	@Override
	public void requestFocus() {
		super.requestFocus();
		sessionPanel.areaTextPanel1.requestFocus();
		sessionPanel.areaTextPanel1.area.setCaretPosition(sessionPanel.areaTextPanel1.area.getDocument().getLength());
	}

	/**
	 * 清空本回话好友的本地数据。
	 */
	public void clearChatMsg() {
		ClientExportPanel.instance.getData().getMaps().remove(toUser.getId());
		ClientExportPanel.instance.saveConfigObject();
	}

	public void shareFile(String path, String name) {
		ClientExportPanel.instance.shareFile(toUser, path, name);
	}

	public void showResources(List<Resource> lists) {
		sessionPanel.listPanel.removeAll();
		for (Resource r : lists) {
			ItemPanel<Resource> itemPanel = new ItemPanel<Resource>(r);
			itemPanel.setListPanel(sessionPanel.listPanel);
			sessionPanel.listPanel.addItem(itemPanel);
		}
		sessionPanel.listPanel.revalidate();
	}

	public void addResources(Resource r) {
		ItemPanel<Resource> itemPanel = new ItemPanel<Resource>(r);
		itemPanel.setListPanel(sessionPanel.listPanel);
		sessionPanel.listPanel.addItem(itemPanel);
		sessionPanel.listPanel.revalidate();
		this.setVisible(true);
		this.requestFocus();
	}
}
