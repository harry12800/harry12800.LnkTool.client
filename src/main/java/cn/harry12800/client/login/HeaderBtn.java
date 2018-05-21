package cn.harry12800.client.login;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import cn.harry12800.j2se.component.utils.ImageUtils;

public class HeaderBtn extends Component {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String src;

	int w=100;
	int h=100;
	public HeaderBtn(String src) {
		this.src = src;
		setPreferredSize(new Dimension(100, 100));
	}
	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.WHITE);
		g2d.drawRoundRect(1, 1, w-3, h-3, 3, 3);
		g2d.drawImage(ImageUtils.getByName(src), 2, 2, w-4, h-4, null);
		super.paint(g);
	}
}
