package cn.harry12800.client.login;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import cn.harry12800.j2se.component.btn.DIYButton;
import cn.harry12800.j2se.component.btn.ImageButton;
import cn.harry12800.j2se.component.btn.LabelButton;
import cn.harry12800.j2se.component.btn.LabelButton.ChangeListener;
import cn.harry12800.j2se.style.UI;

class LoginPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		boolean hasRemember = false;
		boolean hasAutoLogin = false;
		String username="";
		JTextField userNameField = new InputText(300);
		JLabel errorLabel = new JLabel();
		ImageButton logoImage = new ImageButton("logo.png", 20, 20);
		LabelButton logoNameLabel = new LabelButton("VChat__Tools", 250, 20,LabelButton.createLabelBuilder());
		JPasswordField passwordField = new PassText(300);
		LabelButton loginBtn = new LabelButton("登陆", 160, 30,LabelButton.createBgColorBuilder(new Color(58, 177, 255)));
		LabelButton rememberPass;
		LabelButton autoLogin;
		private LoginCallback action;
		private LoginFrame loginFrame;
		protected boolean reLogin;

		public LoginPanel(final LoginFrame loginFrame, final LoginCallback a) {
			this.action = a;
			this.loginFrame = loginFrame;
			setLayout(null);
			rememberPass = new LabelButton("记住密码", 100, 25,
					LabelButton.createCheckBuilder(hasRemember));
			autoLogin = new LabelButton("自动登陆", 100, 25,
					LabelButton.createCheckBuilder(hasAutoLogin));
			errorLabel.setForeground(Color.RED);
			logoImage.setBounds(2, 2, 25, 25);
			logoNameLabel.setBounds(25, 2, 150, 20);
			userNameField.setBounds(200, 50, 160, 25);
			passwordField.setBounds(200, 80, 160, 25);
			errorLabel.setBounds(80, 160, 120, 25);
			logoNameLabel.setBackground(new Color(255, 255, 255, 0));
			loginBtn.setBounds(200, 140, 200, 30);
			rememberPass.setBounds(200, 110, 80, 25);
			autoLogin.setBounds(280, 110, 80, 25);
			rememberPass.setBackground(new Color(255, 255, 255, 0));
			rememberPass.addChangeListener(new ChangeListener() {
				@Override
				public void changed(boolean checked) {
					if(!checked) {
						autoLogin.builder.checked=false;
						autoLogin.repaint();
					}
				}
			});
			autoLogin.addChangeListener(new ChangeListener() {
				@Override
				public void changed(boolean checked) {
					if(checked) {
						rememberPass.builder.checked=true;
						rememberPass.repaint();
					}
				}
			});
			CloseBtn closeBtn = new CloseBtn();
			MinBtn minBtn = new MinBtn();
			HeaderBtn headerBtn = new HeaderBtn("header.jpg");
			autoLogin.setBackground(new Color(255, 255, 255, 0));
			add(userNameField);
			userNameField.setOpaque(false);
			add(passwordField);
			add(rememberPass);
			add(autoLogin);
			closeBtn.setBounds(375, 1, 25, 25);
			minBtn.setBounds(350, 1, 25, 25);
			headerBtn.setBounds(50, 50, 100, 100);
			add(closeBtn);
			add(minBtn);
			passwordField.setOpaque(false);
			add(errorLabel);
			add(loginBtn);
			add(headerBtn);
			add(logoImage);
			add(logoNameLabel);
			username  = getLastUser();
//			updateCurrUserState(username);
			
			passwordField.addKeyListener(new KeyAdapter() {
				@SuppressWarnings("deprecation")
				@Override
				public void keyReleased(KeyEvent arg0) {
					if (arg0.getKeyCode() == 10) {
						doLogin();
					}
					else if (arg0.getKeyCode() == 8) {
						if(passwordField.getText().equals("111111111")){
							passwordField.setText("");
						}
						reLogin=false;
					}
				}
			});
			userNameField.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					if (e.getKeyCode() == 10) {
						doLogin();
					}else{
//						if(AllUserConfig.map.keySet().contains( userNameField.getText())){
//							updateCurrUserState(userNameField.getText());
//						}else{
//							hasAutoLogin=false;
//							hasRemember=false;
//							passwordField.setText("");
//							autoLogin.builder.checked=false;
//							autoLogin.repaint();
//							rememberPass.builder.checked=false;
//							rememberPass.repaint();
//						}
					}
				}
			});
			loginBtn.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					doLogin();
				}
			});
			if(hasAutoLogin) doAutoLogin();
		}

		private void doAutoLogin() {
//			UserService bean = SysConfig.instance.getBean(UserService.class);
//			User user = bean.getUserByUserName(userNameField.getText());
//			System.out.println(userNameField.getText());
//			if (user != null) {
//				loginFrame.dispose();
//				LoginFrame.showflag=true;
//			
//				System.out.println("-----------");
//				SysConfig.map.put("user", user.getId());
//				LoginPanel.this.action.success();
//				AllUserConfig.setProp(user.getUserName(),System.currentTimeMillis()+"");
//				UserConfig.getInstance(user.getUserName()).setProp(LoginPanel.class, "hasRemember", rememberPass.builder.checked+"");
//				UserConfig.getInstance(user.getUserName()).setProp(LoginPanel.class, "hasAutoLogin", autoLogin.builder.checked+"");
//			} else {
//				errorLabel.setText("用户名或密码错误！");
//				action.fail();
//			}
		}

//		protected void updateCurrUserState(String username) {
//			if(username==null)return ;
//			userNameField.setText(username);
//			String hasRemember =UserConfig.getInstance(username).getProp(LoginPanel.class, "hasRemember");
//			String hasAutoLogin =UserConfig.getInstance(username).getProp(LoginPanel.class, "hasAutoLogin");
//			if("true".equals(hasAutoLogin)){
//				this.hasAutoLogin=true;
//				autoLogin.builder.checked=true;
//				passwordField.setText("1111111111");
//				reLogin=true;
//				autoLogin.repaint();
//			}else{
//				this.hasAutoLogin=false;
//				autoLogin.builder.checked=false;
//				autoLogin.repaint();
//			}
//			if("true".equals(hasRemember)){
//				this.hasRemember=true;
//				rememberPass.builder.checked=true;
//				passwordField.setText("1111111111");
//				this.reLogin=true;
//				rememberPass.repaint();
//			}else{
//				this.hasRemember=false;
//				passwordField.setText("");
//				rememberPass.builder.checked=false;
//				rememberPass.repaint();
//			}
//			errorLabel.setText("");
//		}

		/**
		 * 得到最近的登录用户
		 * @return
		 */
		private String getLastUser() {
//			Set<Entry<String, String>> entrySet = AllUserConfig.map.entrySet();
//			long time =0;
//			String username="";
//			for (Entry<String, String> entry : entrySet) {
//				Long valueOf = Long.valueOf(entry.getValue());
//				if(valueOf>time){
//					time = valueOf;
//					username = entry.getKey();
//				}
//			}
			return username;
		}

		private void doLogin() {
//			if(reLogin){
//				doAutoLogin();
//				return ;
//			}
//			UserService bean = SysConfig.instance.getBean(UserService.class);
//			User user = bean.getUser(userNameField.getText(), new String(
//					passwordField.getPassword()));
//			if (user != null) {
//				loginFrame.dispose();
//				SysConfig.map.put("user", user.getId());
//				LoginPanel.this.action.success();
//				AllUserConfig.setProp(user.getUserName(),System.currentTimeMillis()+"");
//				UserConfig.getInstance(user.getUserName()).setProp(LoginPanel.class, "hasRemember", rememberPass.builder.checked+"");
//				UserConfig.getInstance(user.getUserName()).setProp(LoginPanel.class, "hasAutoLogin", autoLogin.builder.checked+"");
//			} else {
//				errorLabel.setText("用户名或密码错误！");
//				action.fail();
//			}
		}

		@Override
		protected void paintComponent(Graphics g) {
			int w = getWidth();
			int h = getHeight();
			setFont(UI.normalFont);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setStroke(new BasicStroke(1));
			g2d.setColor(Color.GRAY);
			GradientPaint p1;
			p1 = new GradientPaint(0, 0, new Color(13, 84, 162), 0, h - 200,
					new Color(0, 255, 245));
			Image image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

			RoundRectangle2D.Float r2d = new RoundRectangle2D.Float(0, 0, w, h,
					0, 0);
			g2d.clip(r2d);
			int x = 0, y = 0;
			java.net.URL imgURL = getClass().getResource("/image/desk.jpg");
			// test.jpg是测试图片，与Demo.java放在同一目录下
			ImageIcon ic = new ImageIcon(imgURL);
			InputStream in = DIYButton.class
					.getResourceAsStream("/image/desk.jpg");
			try {
				image = ImageIO.read(in);
			} catch (IOException e) {
				e.printStackTrace();
			}
			g2d.drawImage(image, 0, 0, w, h, null);
			g.drawImage(ic.getImage(), x, y, getSize().width, getSize().height,
					this);
			while (true) {
				g2d.drawImage(image, 0, 0, w, h, null);
				if (x > getSize().width && y > getSize().height)
					break;
				// 这段代码是为了保证在窗口大于图片时，图片仍能覆盖整个窗口
				if (x > getSize().width) {
					x = 0;
					y += ic.getIconHeight();
				} else
					x += ic.getIconWidth();
			}
			// g2d.fillRect(0, 0, w, h);
			// g2d.setClip(clip);
			g2d.setPaint(p1);
			g2d.setStroke(new BasicStroke(1));
			// g2d.drawRoundRect(0, 0,w , h, 0, 0);
		}

		public void requestFocus() {
			super.requestFocus();
			userNameField.requestFocus();
		}
		public void updateLastUser(){
			String lastUser = getLastUser();
//			updateCurrUserState(lastUser);
		}
	}
