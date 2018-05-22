package cn.harry12800.client.login;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CloseBtn extends Component {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean hover=false;
	public CloseBtn() {
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
				System.exit(1);
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
		g2d.drawLine(3, 3, 13, 13);
		g2d.drawLine(13, 3, 3, 13);
		super.paint(g);
	}
}
