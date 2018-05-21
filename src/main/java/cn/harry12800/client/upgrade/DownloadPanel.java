package cn.harry12800.client.upgrade;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import cn.harry12800.j2se.component.Progressar;
import cn.harry12800.j2se.component.panel.TitlePanel;
import cn.harry12800.j2se.component.panel.TitlePanel.Builder;
import cn.harry12800.j2se.component.panel.TitlePanel.TitleHeight;
import cn.harry12800.j2se.component.utils.GBC;
import cn.harry12800.j2se.style.J2seColor;

public class DownloadPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JLabel label= new JLabel("正在更新……");
	/**
	 * 获取label
	 *	@return the label
	 */
	public JLabel getLabel() {
		return label;
	}

	/**
	 * 设置label
	 * @param label the label to set
	 */
	public void setLabel(JLabel label) {
		this.label = label;
	}

	public DownloadPanel(JFrame mainFrame, Configuration parse, Map<Resource, Progressar> maps) {
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(1, 1, 1, 1));
//		setBorder(
//				BorderFactory.createCompoundBorder(
//						ShadowBorder.newBuilder().shadowSize(3).center().build(), 
//				BorderFactory.createLineBorder(Color.GRAY))
//				);
		setOpaque(false);
		Builder builder = TitlePanel.createBuilder(mainFrame);
		builder.hasTitle=true;
		builder.titleHeight= TitleHeight.middle;
		add(new TitlePanel(builder), BorderLayout.NORTH);
		ContainerPanel containerPanel = new ContainerPanel();
		int i=0;
		List<Properties> description = parse.description;
		for (Properties string : description) {
			containerPanel.add(new JLabel(string.getValue()), new GBC(0, i++, GBC.WEST, new Insets(1, 1, 1, 1), 1, 1));
		}
		for (Resource resource : parse.resources) {
			containerPanel.add(new JLabel(resource.getRealname()), new GBC(0, i++, GBC.WEST, new Insets(1, 1, 1, 1), 1, 1));
			containerPanel.add(maps.get(resource), new GBC(0, i++, GBC.WEST, new Insets(1, 1, 1, 1), 1, 1));
		}
		
		JScrollPane scroll = new JScrollPane(containerPanel);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
		scroll.setOpaque(false);
		scroll.setBackground(new Color(0,0,0,0));
		add(scroll,BorderLayout.CENTER);
		add(label,BorderLayout.SOUTH);
	}

	@Override
	protected void paintComponent(Graphics g) {
		int w = getWidth();
		int h = getHeight();
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setStroke(new BasicStroke(3));
		g2d.setColor(J2seColor.getBorderColor());
		GradientPaint p1;
		p1 = new GradientPaint(0, 0,J2seColor.getBackgroundColor(), 0, h - 200,
				J2seColor.getBackgroundColor());
		RoundRectangle2D.Float r2d = new RoundRectangle2D.Float(0, 0, w - 1,
				h - 1, 0, 0);
		g2d.clip(r2d);
		g2d.setPaint(p1);
		g2d.fillRect(0, 0, w-1,h-1);
		/*
		 * int x = 0, y = 0; test.jpg是测试图片，与Demo.java放在同一目录下
		 */

//		g2d.drawImage(ImageUtils.getByName("desk.jpg"), 3, 3, w-6, h-6, null);
		/*
		 * g.drawImage(image, x, y, getSize().width, getSize().height, this);
		 * while (true) { g2d.drawImage(image, 0, 0, w, h, null); if (x >
		 * getSize().width && y > getSize().height) break; //
		 * 这段代码是为了保证在窗口大于图片时，图片仍能覆盖整个窗口 if (x > getSize().width) { x = 0;
		 * image.getw //y += ic.getIconHeight(); } else ;//x +=
		 * ic.getIconWidth(); } g2d.fillRect(0, 0, w, h); g2d.setClip(clip);
		 */
		g2d.setStroke(new BasicStroke(3));
		g2d.setColor(J2seColor.getBorderColor());
		g2d.setPaint(p1);
		g2d.drawRoundRect(0, 0, w - 1, h - 1, 0, 0);
		g2d.setStroke(new BasicStroke(1));
		/*
		 * g2d.setPaint(p2); g2d.drawRoundRect(0, 0, w - 1, h - 1, 0, 0);
		 */
	}
}
