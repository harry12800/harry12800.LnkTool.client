package cn.harry12800.client.login;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import cn.harry12800.j2se.component.utils.ImageUtils;

public class LoginFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private int xx;
	private int yy;
	private boolean isDraging;
	private boolean flag = false;
	private BufferedImage image;
	private LoginPanel loginPanel;
	static boolean showflag;
	public static LoginFrame instance = null;
	public static LoginCallback a = null;

	@Override
	public void dispose() {
		if(showflag)
			super.dispose();
		else{
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					LoginFrame.this.dispose();
				}
			}, 100);
		}
		
	}
	private LoginFrame(LoginCallback a) {
		LoginFrame.a = a;
		setUndecorated(true);
		image = ImageUtils.getByName("logo_lan.png");
		setIconImage(image);
		loginPanel = new LoginPanel(this, LoginFrame.a);
		setContentPane(loginPanel);
		setSize(400, 200);
		setLocationRelativeTo(null);
		initListener();
		if(!showflag)
			setVisible(true);
		requestFocus();
		loginPanel.requestFocus();
		requestFocusInWindow();
		showflag = true;
	}

	public static void main(String[] args) {
		new LoginFrame(null);
	}

	/**
	 * 可拖拽
	 */
	private void initListener() {
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				requestFocus();
				isDraging = true;
				xx = e.getX();
				yy = e.getY();
			}

			public void mouseReleased(MouseEvent e) {
				isDraging = false;
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				setFlag(true);// 窗体内部暂时设置为不能取色
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				if (isDraging) {
					int left = getLocation().x;
					int top = getLocation().y;
					setLocation(left + e.getX() - xx, top + e.getY() - yy);
				}
			}
		});
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public static void display() {
		getInstance(LoginFrame.a);
	}

	public static LoginFrame display(LoginCallback a) {
		return getInstance(a);
	}

	private static LoginFrame getInstance(LoginCallback a) {
		if(instance==null)
			instance = new LoginFrame(a);
		else{
			instance.setVisible(true);
			instance.updateLastUser();
		}
		return instance;
	}

	private void updateLastUser() {
		loginPanel.updateLastUser();
	}
	@Override
	public void requestFocus() {
		super.requestFocus();
		loginPanel.requestFocus();
	}
}
