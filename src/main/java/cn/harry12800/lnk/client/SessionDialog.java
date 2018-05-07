package cn.harry12800.lnk.client;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import cn.harry12800.j2se.action.DragListener;
import cn.harry12800.j2se.component.BaseWindow;

public class SessionDialog  extends BaseWindow {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3402650173117339424L;
	public static SessionDialog instance;
	public SessionDialog(ClientExportPanel clientExportPanel ) {
		instance = this;
		setType(JFrame.Type.UTILITY);
		setSize(350,420);
		setContentPane(new JPanel());
		new DragListener(this);
		setCenterScreen();
		clientExportPanel.addWindow(this);
	}
	public void setCenterScreen() {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		int w = (int) d.getWidth();
		int h = (int) d.getHeight();
		this.setLocation((w - this.getWidth()) / 2, (h - this.getHeight()) / 2);
	}
}
