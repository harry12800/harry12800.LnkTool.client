package cn.harry12800.client.login;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

public class MinBtn extends Component {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean hover=false;
	public MinBtn() {
		setPreferredSize(new Dimension(25, 25));
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				hover= true;
				repaint();
				super.mouseEntered(e);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				hover= false;
				repaint();
				super.mouseExited(e);
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				LoginFrame.instance.setExtendedState(JFrame.ICONIFIED);
				super.mouseClicked(e);
			}
		});
	}
	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		if(hover)
			g2d.setColor(Color.WHITE);
		else {
			g2d.setColor(Color.GRAY);
		}
		g2d.drawLine(3, 8, 13, 8);
		super.paint(g);
	}
}
