package main;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Counter extends JPanel{
	public static Point mouse = new Point(0,0);
	private static boolean isAOT = true;
	
	private static JSlider AlphaChooser = new JSlider(JSlider.HORIZONTAL, 3, 100, 60);
	private SimpleDateFormat debugFormat = new SimpleDateFormat("yyyy/MM/dd a h:mm:ss", Locale.KOREA);
	private SimpleDateFormat curTimeFormat = new SimpleDateFormat("a h:mm", Locale.KOREA);
	public static MyDialog settingDialog = null;
	public static EverytimeDialog everytimeDialog = null;
	public static long endFlag = 0;
	public static long registerFlag = 0;
	private static double alpha = 0.8;
	
	public void paintComponent(Graphics gp) {
		Graphics2D g = (Graphics2D)gp;
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON); //text smooth
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //roundRect smooth
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setFont(Global.CUSTOM_FONT);
		
		double adjusted = 1-(1-alpha)*0.8;
		Composite c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)adjusted);
		g.setComposite(c);
		
		//setting area
		g.drawImage(Global.SettingIcon, null, Global.SETTING_AREA.x, Global.SETTING_AREA.y);
		
		//everytime icon area
		g.drawImage(Global.EverytimeIcon, null, Global.EVERYTIME_AREA.x, Global.EVERYTIME_AREA.y);
		
		//draggable area
		g.setColor(new Color(50, 50, 255, (int)(255*alpha)));
		g.fill(Global.DRAGGABLE_AREA);
		
		//Pinning area
		if(isAOT) g.drawImage(Global.PinnedIcon, null, Global.PIN_AREA.x, Global.PIN_AREA.y);
		else g.drawImage(Global.UnpinnedIcon, null, Global.PIN_AREA.x, Global.PIN_AREA.y);
		
		//exit button
		g.drawImage(Global.ExitIcon, null, Global.EXIT_AREA.x, Global.EXIT_AREA.y);
		
		
		g.setColor(Color.BLACK);
		
		String curTimeU = curTimeFormat.format(System.currentTimeMillis());
		
		long diff = endFlag-System.currentTimeMillis();
		long total = endFlag-registerFlag;
		double prate = (double)(total-diff)/(double)total;
		double arate = 0;
		if(total>(diff+10))
			arate = (double)diff/(double)(total-diff);
		boolean activate = true;
		if(diff < 0) {
			diff = 0;
			activate = false;
			prate = 0;
		}
		
		
		long msec = diff%1000;
		diff /= 1000;
		
		long hour = diff/3600;
		
		diff -= hour*3600;
		long min = diff/60;
		long sec = diff%60;
		
		String diffStr = String.format("%d:%02d:%02d", hour, min,sec);
		
		setFontSize(g, 12F);
		g.drawString("현재 시각", 15, 45);
		setFontSize(g, 20F);
		g.drawString(curTimeU, 75, 45);
		setFontSize(g, 10F);
		g.drawString(String.format("%02d", (System.currentTimeMillis()/1000)%60), 160+(curTimeU.length()-7)*12, 45);
		setFontSize(g, 12F);
		g.drawString("남은 시간", 15, 85);
		setFontSize(g, 30F);
		g.drawString(diffStr, 75, 85);
		setFontSize(g, 15F);
		g.drawString("."+String.format("%03d", msec), 185, 85);
		setFontSize(g, 12F);
		g.drawString("진행도", 15, 115);
		
		if(activate) {
			String dds = String.format("%.5f", arate);
			
			setFontSize(g, 12F);
			g.drawString("x"+dds, 335, 55);
			
			g.setColor(new Color((int)(220*prate), 0, 0));
			setFontSize(g, 70F);
			double pr = prate*100;
			g.drawString(String.format("%02d",(int)pr), 240, 85);
			setFontSize(g, 20F);
			String ff = String.format("%.5f%%", pr-Math.floor(pr));
			ff = ff.substring(1);
			g.drawString(ff, 330, 85);
		}
		
		g.setColor(new Color(50,50,50));
		g.fillRoundRect(75, 103, 345, 15, 8, 8);
		g.setColor(new Color(70,255,70));
		g.fillRoundRect(75, 103, (int)((double)345*prate), 15, 8, 8);
	}
	
	public static void init() {
		try {
		     GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		     Global.CUSTOM_FONT = Font.createFont(Font.TRUETYPE_FONT, new File("static\\RIXGOM.TTF")).deriveFont(30F);
		     ge.registerFont(Global.CUSTOM_FONT);
		} catch (IOException e) {
		     //Handle exception
			System.out.println("FONT FILE LOAD ERROR");
		}catch(FontFormatException e) {
			System.out.println("FONT FILE FORMAT ERROR");
		}
		try {
			Global.SettingIcon = ImageIO.read(new File("static\\SETTING_ICON.png"));
			Global.UnpinnedIcon = ImageIO.read(new File("static\\PINNING_ICON.png"));
			Global.PinnedIcon = ImageIO.read(new File("static\\UNPINNING_ICON.png"));
			Global.ExitIcon = ImageIO.read(new File("static\\EXIT_ICON.png"));
			Global.IncreaseIcon = ImageIO.read(new File("static\\up.png"));
			Global.DecreaseIcon = ImageIO.read(new File("static\\down.png"));
			Global.EverytimeIcon = ImageIO.read(new File("static\\logo.png"));
			Global.HighResEverytimeIcon = ImageIO.read(new File("static\\HIGH_RESOL_LOGO.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("InClassRemainCounter");
		frame.setBounds(Global.FRAME_BOUNDARY);
		frame.setUndecorated(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setBackground(new Color(255, 255, 255, (int)((double)255*0.6)));
		
		init();
		
		Counter counter = new Counter();
		counter.setVisible(true);
		counter.setSize(Global.FRAME_DIMEN);
		counter.setLayout(null);
		
		AlphaChooser.setBounds(Global.ALPHA_CHOOSER_AREA);
		AlphaChooser.setBackground(new Color(0,0,0,0));
		AlphaChooser.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				int val = ((JSlider)e.getSource()).getValue();
				double rate = (double)val/100;
				frame.setBackground(new Color(255, 255, 255, (int)((double)255*rate)));
				
				alpha = rate;
			}
		});
		counter.add(AlphaChooser);
		
		settingDialog = new MyDialog(frame, "종료 날짜/시간");
		everytimeDialog = new EverytimeDialog(frame, "에브리타임 계정");
		frame.add(counter);
		frame.setAlwaysOnTop(isAOT);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					counter.repaint();
					frame.repaint();
					try {
						Thread.sleep(1000/144);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
		
		counter.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
            	if(Global.DRAGGABLE_AREA.contains(mouse)) {
					int dx = e.getX() - mouse.x;
					int dy = e.getY() - mouse.y;
					Point cur = frame.getLocation();
					frame.setLocation(cur.x + dx, cur.y + dy);
				}
            }

            @Override
            public void mouseMoved(MouseEvent e) {
               mouse.x = e.getX();
               mouse.y = e.getY();
            }
            
		});
		
		counter.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if(Global.SETTING_AREA.contains(mouse)) {
					settingDialog.setVisible(!settingDialog.isVisible());
					if(settingDialog.isVisible())
						settingDialog.init(frame);
				}else if(Global.EXIT_AREA.contains(mouse)) {
					System.exit(0);
				}else if(Global.PIN_AREA.contains(mouse)) {
					isAOT = !isAOT;
					frame.setAlwaysOnTop(isAOT);
				}else if(Global.EVERYTIME_AREA.contains(mouse)) {
					everytimeDialog.setVisible(!everytimeDialog.isVisible());
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	private void setFontSize(Graphics2D g, float size) {
		g.setFont(Global.CUSTOM_FONT.deriveFont(size));
	}
	
	private static class MyDialog extends JDialog{
		public JTextField textField = new JTextField("");
		public JButton okButton = new JButton("설정");
		public JLabel afterStr = new JLabel("오후");
		public JLabel fixedStr = new JLabel("20--/--/--");
		public JLabel timeStr = new JLabel("--:--");
		public JButton upHour = new JButton();
		public JButton upMin = new JButton();
		public JButton downHour = new JButton();
		public JButton downMin = new JButton();
		public boolean isAfter = true;
		
		private int hour = 0, min = 0;
		Calendar cal = Calendar.getInstance();
		
		private SimpleDateFormat sdfa = new SimpleDateFormat("yyyy/MM/dd");
		
		void init(JFrame frame) {
			fixedStr.setText(sdfa.format(System.currentTimeMillis()));
			
			Date d = new Date();
			d.setTime(System.currentTimeMillis());
			cal.setTime(d);
			isAfter = cal.get(Calendar.HOUR_OF_DAY)>=12;
			afterStr.setText(isAfter?"오후":"오전");
			hour = cal.get(Calendar.HOUR);
			min = cal.get(Calendar.MINUTE);
			timeStr.setText(String.format("%02d:%02d",hour,min));
			
			setLocation(frame.getLocation());
		}
		
		public MyDialog(JFrame frame, String title) {
			super(frame, title);
			setLayout(null);
			init(frame);
			
			upHour.setIcon(new ImageIcon(Global.IncreaseIcon));
			upMin.setIcon(new ImageIcon(Global.IncreaseIcon));
			downHour.setIcon(new ImageIcon(Global.DecreaseIcon));
			downMin.setIcon(new ImageIcon(Global.DecreaseIcon));
			
			upHour.setBounds(190, 15, 15, 15);
			downHour.setBounds(190, 30, 15, 15);
			upMin.setBounds(270, 15, 15, 15);
			downMin.setBounds(270, 30, 15, 15);
			fixedStr.setFont(Global.CUSTOM_FONT.deriveFont(20F));
			afterStr.setFont(Global.CUSTOM_FONT.deriveFont(20F));
			afterStr.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
			afterStr.setBackground(new Color(220,220,255));
			afterStr.setOpaque(true);
			timeStr.setFont(Global.CUSTOM_FONT.deriveFont(20F));
			
			fixedStr.setBounds(15,15, 120, 30);
			afterStr.setBounds(140, 15, 40, 30);
			timeStr.setBounds(210,15,60,30);
			okButton.setBounds(35, 60, 230, 30);
			add(afterStr);
			add(fixedStr);
			add(timeStr);
			add(okButton);
			add(upHour);
			add(upMin);
			add(downHour);
			add(downMin);
			
			setLocation(1920 - Global.FRAME_DIMEN.width, 1080-Global.FRAME_DIMEN.height-30);
			setSize(315, 150);
			okButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					Calendar c = Calendar.getInstance();
					c.set(Calendar.YEAR, cal.get(Calendar.YEAR));
					c.set(Calendar.MONTH, cal.get(Calendar.MONTH));
					c.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
					c.set(Calendar.AM_PM, isAfter?1:0);
					c.set(Calendar.HOUR, hour);
					c.set(Calendar.MINUTE, min);
					c.set(Calendar.SECOND, 0);
					endFlag = c.getTimeInMillis();
					registerFlag = System.currentTimeMillis();
					setVisible(false);
				}
			});
			afterStr.addMouseListener(new MouseListener() {

				@Override
				public void mouseClicked(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseEntered(MouseEvent arg0) {
					// TODO Auto-generated method stub
					afterStr.setBackground(new Color(190,190,230));
				}

				@Override
				public void mouseExited(MouseEvent arg0) {
					// TODO Auto-generated method stub
					afterStr.setBackground(new Color(220,220,255));
				}

				@Override
				public void mousePressed(MouseEvent arg0) {
					// TODO Auto-generated method stub
					afterStr.setBackground(new Color(120,120,150));
				}

				@Override
				public void mouseReleased(MouseEvent arg0) {
					// TODO Auto-generated method stub
					afterStr.setBackground(new Color(190,190,230));
					isAfter = !isAfter;
					afterStr.setText(isAfter?"오후":"오전");
				}
				
			});
			upHour.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					hour += 1;
					if(hour>=13)
						hour = 1;
					updateTime();
				}
			});
			downHour.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					hour -= 1;
					if(hour<=0)
						hour = 12;
					updateTime();
				}
			});
			upMin.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					if(min%15==0)
						min+=15;
					else
						min += 15-min%15;
					if(min>=60)
						min = 0;
					updateTime();
				}
			});
			downMin.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					if(min%15==0) {
						min-=15;
						if(min<=-15)
							min = 45;
					}else
						min -= min%15;
					updateTime();
				}
			});
		}
		
		private void updateTime() {
			timeStr.setText(String.format("%02d:%02d",hour,min));
		}
	}
	private static class EverytimeDialog extends JDialog{
		public JTextField idField = new JTextField();
		public JPasswordField pwField = new JPasswordField();
		public JButton okButton = new JButton("로그인");
		public void init(JFrame frame) {
			Point p = frame.getLocation();
			setLocation(p.x, p.y - 50);
		}
		
		public EverytimeDialog(JFrame frame, String title) {
			super(frame, title);
			setLayout(null);
			setSize(435, 200);
			init(frame);
			
			this.setBackground(Color.WHITE);
			this.getContentPane().setBackground(Color.WHITE);
			
			JLabel a = new JLabel("계정 ID");
			JLabel b = new JLabel("계정 PW");
			JLabel everytimeLogo;
			ImageIcon icon = new ImageIcon(Global.HighResEverytimeIcon);
			
			a.setBounds(45, 75, 80, 25);
			b.setBounds(45, 110, 80, 25);
			everytimeLogo = new JLabel(icon);
			everytimeLogo.setBounds(this.getWidth()/2-icon.getIconWidth()/2, 15, icon.getIconWidth(), icon.getIconHeight());
			idField.setBounds(130, 75, 150, 25);
			pwField.setBounds(130, 110, 150, 25);
			okButton.setBounds(300, 75, 70, 60);
			
			a.setFont(Global.CUSTOM_FONT.deriveFont(18F));
			b.setFont(Global.CUSTOM_FONT.deriveFont(18F));
			idField.setFont(Global.CUSTOM_FONT.deriveFont(18F));
			okButton.setFont(Global.CUSTOM_FONT.deriveFont(13F));
			
			add(a);
			add(b);
			add(everytimeLogo);
			add(idField);
			add(pwField);
			add(okButton);
			
			okButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					setVisible(false);
				}
			});
		}
	}
}
