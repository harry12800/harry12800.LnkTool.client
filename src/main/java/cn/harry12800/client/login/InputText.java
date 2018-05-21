package cn.harry12800.client.login;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

class InputText extends JTextField {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public InputText(int x) {
			super(x);
			EmptyBorder emptyBorder = new EmptyBorder(0, 5,0, 1);
			setBorder(emptyBorder);
			setSelectionColor(new Color(36,106,204));
			setSelectedTextColor(Color.WHITE);
			setForeground(Color.WHITE);
			setFont(new Font("楷体", 12, 18));
			setCaretColor(Color.WHITE);
		}

		@Override
		protected void paintComponent(Graphics g) {
			int h = getHeight();
			int w = getWidth();
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.drawRoundRect(0, 0, w - 2, h - 2, 4, 4);
			g2d.setColor(new Color(70, 70, 70));
			g2d.fillRoundRect(1, 1, w - 3, h - 3, 5, 5);
			g2d.dispose();
			super.paintComponent(g);
		}
	}

	