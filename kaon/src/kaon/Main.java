package kaon;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Main {

	public static int size[] = {300, 400, 0, 0};
	public static int state[] = {1, 1, 1, 3, 100, 100, 0, 100, 1, 0};
//	str, agi, con, 스텟, 현재 체력, 최대 체력, 현재 경험치, 최대 경험치, 레벨, 소지금
	public static int it[] = {0};
//	슬라임젤리 개수,
	public static int sell[] = {3};
//	슬라임젤리 가격
	public static String id = null;
	
	public static boolean ingame = false; 

	public static void main(String[] args) {

		Mythread ms = new Mythread();
		ms.start();
		new Play();

	}

}

class Mythread extends Thread {

	Main m = new Main();

	@Override
	public void run() {
		while (true) {
			try {
				this.sleep(0);
				if (m.state[6] >= m.state[7] && m.ingame) {
					m.state[6] = m.state[6] - m.state[7]; // 현재 경험치 초기화;
					m.state[8]++; // 레벨 1 상승;
					m.state[7] = 100 * m.state[8]; // 최대 경험치 상승;
					m.state[4] = m.state[5]; // 현제 채력 초기화;
					m.state[3]++;
				}
			} catch (InterruptedException e) {
				break;
			}
		}

	}
}

class Title {
	
	Main m = new Main();
	ImageIcon t = new ImageIcon("../kaon/src/title.png");
	ImageIcon x = new ImageIcon("../kaon/src/x.png");
	ImageIcon o = new ImageIcon("../kaon/src/O.png");
	private Point initialClick;
	
	public Title(JFrame jf, JPanel jp) {
		jf.setResizable(false);
		jf.setUndecorated(true);
		jf.setBounds(m.size[2], m.size[3], m.size[0], m.size[1]);
		jf.setVisible(true);
		
		JLabel tt = new JLabel(t);
		tt.setBounds(0, 0, 300, 30);
		tt.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) 
			{ 
				initialClick = e.getPoint();
				jf.getComponentAt(initialClick);
			}
		});
		tt.addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent e) 
			{
	            
				int thisX = jf.getLocation().x;
				int thisY = jf.getLocation().y;
	            
				int xMoved = e.getX() - initialClick.x;
				int yMoved = e.getY() - initialClick.y;
	            
				int X = thisX + xMoved;
				int Y = thisY + yMoved;
				jf.setLocation(X, Y);
			}
		});
		
		JButton jb = new JButton(x);
		jb.setRolloverIcon(o);
		jb.setBounds(270, 0, 30, 30);
		jb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(m.id != null) {
					try {
						var rs = db.Datebase.stmt.executeQuery("select * from p");
						while(rs.next()) {
							if(m.id.equals(rs.getString("id"))) {
								execute("update p set str = "+m.state[0]+", agi = "+m.state[1]+", con = "+m.state[2]+", sp = "+m.state[3]
										+", nhp = "+m.state[4]+", mhp = "+m.state[5]+", nexp = "+m.state[6]+", mexp = "+m.state[7]
												+", lv = "+m.state[8]+", mon = "+m.state[9]+ " where id = '" + m.id + "'");
								JOptionPane.showMessageDialog(null, "저장되었습니다.");
								break;
							}
						}
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
				System.exit(jf.EXIT_ON_CLOSE);
			}
		});
		if(jp == null) {
			jf.add(jb);
			jf.add(tt);	
		}
		else {
			jp.add(jb);
			jp.add(tt);	
		}
		
		
		
		jf.repaint();
		
		
	}
	
	void execute(String sql) {
		try {
			db.Datebase.stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(sql);
		}
	}

}


class Play {

	Main m = new Main();
	Font font = new Font("바탕", Font.BOLD, 30);

	public Play() {
		
		JFrame jf = new JFrame();

		
		new Title(jf, null);

		JLabel gameName = new JLabel("Speon");
		gameName.setBounds(m.size[0] / 3, m.size[1] / 3, 100, 50);
		gameName.setFont(font);
		jf.add(gameName);

		JButton regi = new JButton("등록");
		regi.setBounds(m.size[0] / 4 - 35, m.size[1] / 2 + 100, 100, 30);
		regi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m.size[2] = jf.getX();
				m.size[3] = jf.getY();
				new Regi();
			}
		});
		
		JButton rogi = new JButton("로그인");
		rogi.setBounds(m.size[0] / 4 + 85, m.size[1] / 2 + 100, 100, 30);
		rogi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(m.id == null) {
					m.state[0] = 1;
					m.state[1] = 1;
					m.state[2] = 1;
					m.state[3] = 3;
					m.state[4] = 100;
					m.state[5] = 100;
					m.state[6] = 0;
					m.state[7] = 100;
					m.state[8] = 1;
					m.state[9] = 0;
				}
				m.size[2] = jf.getX();
				m.size[3] = jf.getY();
				new Rogi();
			}
		});

		JButton start = new JButton("시작하기");
		start.setBounds(m.size[0] / 4 + 25, m.size[1] / 2 + 140, 100, 30);
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (m.id == null) {
					JOptionPane.showMessageDialog(null, "게스트로 로그인 합니다.");
				}else {
					JOptionPane.showMessageDialog(null, m.id+"으로 로그인 합니다.");
				}
				m.ingame = true;
				m.size[2] = jf.getX();
				m.size[3] = jf.getY();
				jf.dispose();
				new MainPage();
			}
		});

		jf.add(start);
		jf.add(regi);
		jf.add(rogi);

	}
}

class Rogi extends JFrame{
	
	Main m = new Main();
	boolean idCk = false;
	boolean pwCk = false;
	
	public Rogi() {
		
		super("RogIn");
		setBounds(m.size[2] + 50, m.size[3] + 50, m.size[0] - 50, m.size[1] - 190);
		setLayout(null);
		setVisible(true);
		
		execute("use list");
		
		JTextField id = new JTextField("아이디를 입력하세요.");
		id.setBounds(15, 30, 200, 30);
		
		JTextField pw = new JTextField("비밀번호를 입력하세요.");
		pw.setBounds(15, 80, 200, 30);
		
		JButton rogin = new JButton("로그인");
		rogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				idCk = false;
				pwCk = false;
				try {
					var rs = db.Datebase.stmt.executeQuery("select * from p");
					while(rs.next()) {
						if(id.getText().equals(rs.getString("id"))) {
							idCk = true;
							if(pw.getText().equals(rs.getString("pw"))) {
								JOptionPane.showMessageDialog(null, "로그인 되었습니다.");
								m.id = id.getText();
								pwCk = true;
								if(rs.getInt("visit") == 0) {
									execute("update p set visit = visit + 1 where id = '" + m.id + "'");
								}
								else {
									m.state[0] = rs.getInt("str");
									m.state[1] = rs.getInt("agi");
									m.state[2] = rs.getInt("con");
									m.state[3] = rs.getInt("sp");
									m.state[4] = rs.getInt("nhp");
									m.state[5] = rs.getInt("mhp");
									m.state[6] = rs.getInt("nexp");
									m.state[7] = rs.getInt("mexp");
									m.state[8] = rs.getInt("lv");
									m.state[9] = rs.getInt("mon");
								}
								dispose();
								break;
							}
						}
					}
					if(!idCk) {
						JOptionPane.showMessageDialog(null, "아이디를 확인해 주세요.");
					}
					else {
						if(!pwCk) {
							JOptionPane.showMessageDialog(null, "비밀번호를 확인해 주세요.");
						}
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		rogin.setBounds(15, 130, 200, 30);
		
		add(id);
		add(pw);
		add(rogin);
		
	}
	
	void execute(String sql) {
		try {
			db.Datebase.stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(sql);
		}
	}
	
}

class Regi extends JFrame {
	
	Main m = new Main();
	boolean ischeck = false;

	public Regi() {

		super("Registration");
		setBounds(m.size[2] + 50, m.size[3] + 50, m.size[0] - 50, m.size[1] - 190);
		setLayout(null);
		setVisible(true);

		execute("use list");

		JTextField id = new JTextField("아이디를 입력하세요.");
		id.setBounds(15, 10, 200, 30);

		JButton check = new JButton("중복확인");
		check.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					var rs = db.Datebase.stmt.executeQuery("select p.id from p");
					while(rs.next()) {
						if(!id.getText().equals(rs.getString(1))) {
							ischeck = true;
						}
						else {
							JOptionPane.showMessageDialog(null, "이름이 중복되었습니다.");
							ischeck = false;
							break;
						}
					}
					if(ischeck) {
						JOptionPane.showMessageDialog(null, "이름을 사용할 수 있습니다.");
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
			}
		});
		check.setBounds(15, 50, 90, 30);

		JTextField pw = new JTextField("비밀번호를 입력하세요.");
		pw.setBounds(15, 90, 200, 30);

		JButton confirm = new JButton("등록하기");
		confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (ischeck) {
					execute("insert into p (no, id, pw, visit) values (0, '" + id.getText() + "','" + pw.getText() + "', 0)");
					JOptionPane.showMessageDialog(null, "등록되었습니다.");
					dispose();
				} else {
					JOptionPane.showMessageDialog(null, "중복확인을 해주세요");
				}
			}
		});
		confirm.setBounds(15, 130, 200, 30);

		add(id);
		add(check);
		add(pw);
		add(confirm);

	}

	void execute(String sql) {
		try {
			db.Datebase.stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(sql);
		}
	}

}

class MainPage {

	Main m = new Main();

	public MainPage() {
		
		JFrame jf = new JFrame();

		JPanel jp = new JPanel();
		jp.setSize(m.size[0], m.size[1]);
		jp.setLayout(null);
		jp.setVisible(true);

		new Title(jf, jp);
		
		JButton state = new JButton("플레이어 정보");
		state.setBounds(m.size[0] / 25, 50, 270, 30);
		state.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m.size[2] = jf.getX();
				m.size[3] = jf.getY();
				jf.dispose();
				new PlayerState();
			}
		});

		JButton bag = new JButton("가방");
		bag.setBounds(m.size[0] / 25, 90, 270, 30);
		bag.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m.size[2] = jf.getX();
				m.size[3] = jf.getY();
				jf.dispose();
				new Bag();
			}
		});

		JButton dungeon = new JButton("사냥터");
		dungeon.setBounds(m.size[0] / 25, 130, 270, 30);
		dungeon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m.size[2] = jf.getX();
				m.size[3] = jf.getY();
				jf.dispose();
				new Dungeon();
			}
		});

		JButton shop = new JButton("상점");
		shop.setBounds(m.size[0] / 25, 170, 270, 30);
		shop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m.size[2] = jf.getX();
				m.size[3] = jf.getY();
				jf.dispose();
				new Shop();
			}
		});
		
		JButton home = new JButton("시작 화면으로");
		home.setBounds(m.size[0] / 25, 350, 270, 30);
		home.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m.size[2] = jf.getX();
				m.size[3] = jf.getY();
				jf.dispose();
				new Play();
			}
		});

		jp.add(state);
		jp.add(bag);
		jp.add(dungeon);
		jp.add(shop);
		jp.add(home);
		jf.add(jp);

	}

}

class PlayerState{

	Main m = new Main();
	Font font = new Font("바탕", Font.BOLD, 13);

	ImageIcon btU = new ImageIcon("C:\\Users\\user\\eclipse-workspace\\kaon\\src\\+btu.png");
	ImageIcon btD = new ImageIcon("C:\\Users\\user\\eclipse-workspace\\kaon\\src\\+btd.png");

	public PlayerState() {
		
		JFrame jf = new JFrame();

		JPanel jp = new JPanel();
		jp.setSize(m.size[0], m.size[1]);
		jp.setLayout(null);
		jp.setVisible(true);

		new Title(jf, jp);
		
		JLabel stateL = new JLabel();
		stateL.setFont(font);
		stateL.setBounds(20, 25, 300, 200);
		Reload(stateL);

		JButton exit = new JButton("돌아가기");
		exit.setBounds(m.size[0] / 25, 350, 270, 30);
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m.size[2] = jf.getX();
				m.size[3] = jf.getY();
				jf.dispose();
				new MainPage();
			}
		});

		JButton str = new JButton(btU);
		str.setRolloverIcon(btD);
		str.setBounds(75, 120, 15, 15);
		str.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (m.state[3] > 0) {
					m.state[0]++;
					m.state[3]--;
				}
				Reload(stateL);
			}
		});

		JButton con = new JButton(btU);
		con.setRolloverIcon(btD);
		con.setBounds(75, 135, 15, 15);
		con.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (m.state[3] > 0) {
					m.state[2]++;
					m.state[4] += 2;
					m.state[5] += 2;
					m.state[3]--;
				}
				Reload(stateL);
			}
		});

		JButton agi = new JButton(btU);
		agi.setRolloverIcon(btD);
		agi.setBounds(75, 150, 15, 15);
		agi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (m.state[3] > 0) {
					m.state[1]++;
					m.state[3]--;
				}
				Reload(stateL);
			}
		});

		jp.add(exit);
		jp.add(str);
		jp.add(con);
		jp.add(agi);
		jp.add(stateL);
		jf.add(jp);

	}

	public void Reload(JLabel jl) {
		try {
			jl.setText("<html><body>플레이어 정보" + "<br />레벨 : " + m.state[8] + "<br />경험치 : " + m.state[6] + "/"
					+ m.state[7] + "<br />체력 : " + m.state[4] + "/" + m.state[5] + "<br /><br /> STR : " + m.state[0]
					+ "<br />CON : " + m.state[2] + "<br />AGI : " + m.state[1] + "<br />미분배 포인트 : " + m.state[3]
					+ "<br /><br /> 소지금 : " + m.state[9] + "</body></html>");
		} catch (Exception e) {
			return;
		}
	}
}

class Bag {

	Main m = new Main();

	public Bag() {

		JFrame jf = new JFrame();
		

		JPanel jp = new JPanel();
		jp.setSize(m.size[0], m.size[1]);
		jp.setLayout(null);
		jp.setVisible(true);

		new Title(jf, jp);

		JLabel it = new JLabel("<html><body>몬스터 부산물 목록" + "<br /><br />슬라임 젤리 : " + m.it[0] + " 개" + "</body></html>");
		it.setBounds(10, 10, 200, 300);

		JButton exit = new JButton("돌아가기");
		exit.setBounds(m.size[0] / 25, 350, 270, 30);
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m.size[2] = jf.getX();
				m.size[3] = jf.getY();
				jf.dispose();
				new MainPage();
			}
		});

		jp.add(exit);
		jp.add(it);

		jf.add(jp);

	}
}

class Dungeon {

	Main m = new Main();

	public Dungeon() {

		JFrame jf = new JFrame();

		JPanel jp = new JPanel();
		jp.setSize(m.size[0], m.size[1]);
		jp.setLayout(null);
		jp.setVisible(true);

		new Title(jf, jp);

		JButton veld = new JButton("초원");
		veld.setBounds(m.size[0] / 25, 100, 270, 30);
		veld.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m.size[2] = jf.getX();
				m.size[3] = jf.getY();
				jf.dispose();
				new Veld();
			}
		});

		JButton exit = new JButton("돌아가기");
		exit.setBounds(m.size[0] / 25, 350, 270, 30);
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m.size[2] = jf.getX();
				m.size[3] = jf.getY();
				jf.dispose();
				new MainPage();
			}
		});

		jp.add(veld);
		jp.add(exit);

		jf.add(jp);

	}
}

class Shop {

	Main m = new Main();
	public static int price[] = { 5 };
//	회복약 가격,
	public int reco = 20; // 회복약 회복

	JLabel state2;

	public Shop() {

		JFrame jf = new JFrame();

		JPanel jp = new JPanel();
		jp.setSize(m.size[0], m.size[1]);
		jp.setLayout(null);
		jp.setVisible(true);

		new Title(jf, jp);

		JPanel jp2 = new JPanel();
		jp2.setSize(m.size[0], m.size[1]);
		jp2.setLayout(null);
		jp2.setVisible(false);

		JLabel state = new JLabel("<html><body>체력 : " + m.state[4] + "/" + m.state[5] + "<br />경험치 : " + m.state[6]
				+ " /" + m.state[7] + "<br />소지금 : " + m.state[9] + "</body></html>");
		state.setBounds(10, 40, 200, 50);

		JLabel check = new JLabel();
		check.setBounds(10, 80, 200, 30);

		JButton slie = new JButton("슬라임 젤리 팔기 : " + m.sell[0] + "원");
		slie.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (m.it[0] > 0) {
					m.it[0]--;
					m.state[9] += m.sell[0];
					Reload(state);
					check.setText("슬라임 젤리를 팔았어");
				} else {
					check.setText("슬라임 젤리가 없어");
				}
			}
		});
		slie.setBounds(10, 120, 160, 20);

		JButton purc = new JButton("구매");
		purc.setBounds(m.size[0] / 25, 290, 80, 20);
		purc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jp.setVisible(false);
				jp2.setVisible(true);
				Reload(state2);
			}
		});

		JButton exit = new JButton("돌아가기");
		exit.setBounds(m.size[0] / 25, 350, 270, 30);
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m.size[2] = jf.getX();
				m.size[3] = jf.getY();
				jf.dispose();
				new MainPage();
			}
		});

		state2 = new JLabel("<html><body>체력 : " + m.state[4] + "/" + m.state[5] + "<br />경험치 : " + m.state[6] + " /"
				+ m.state[7] + "<br />소지금 : " + m.state[9] + "</body></html>");
		state2.setBounds(10, 30, 200, 50);

		JLabel check2 = new JLabel();
		check2.setBounds(10, 80, 200, 30);

		JButton heal = new JButton("회복약 : " + price[0] + " 원");
		heal.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (m.state[9] >= price[0]) {
					m.state[9] -= price[0];
					if (m.state[4] + reco >= m.state[5]) {
						m.state[4] = m.state[5];
					} else {
						m.state[4] += reco;
					}
					Reload(state2);
					check2.setText("회복약을 샀어");
				} else {
					check2.setText("회복약을 살 돈이 없어");
				}
			}
		});
		heal.setBounds(10, 110, 130, 20);

		JButton sell = new JButton("판매");
		sell.setBounds(m.size[0] - 100, 290, 80, 20);
		sell.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jp2.setVisible(false);
				jp.setVisible(true);
				Reload(state);
			}
		});

		JButton exit2 = new JButton("돌아가기");
		exit2.setBounds(m.size[0] / 25, 350, 270, 30);
		exit2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m.size[2] = jf.getX();
				m.size[3] = jf.getY();
				jf.dispose();
				new MainPage();
			}
		});

		jp.add(exit);
		jp.add(purc);
		jp.add(state);
		jp.add(check);
		jp.add(slie);

		jp2.add(exit2);
		jp2.add(state2);
		jp2.add(check2);
		jp2.add(sell);
		jp2.add(heal);

		jf.add(jp);
		jf.add(jp2);

	}

	public void Reload(JLabel jl) {
		try {
			jl.setText("<html><body>체력 : " + m.state[4] + "/" + m.state[5] + "<br />경험치 : " + m.state[6] + " /"
					+ m.state[7] + "<br />소지금 : " + m.state[9] + "</body></html>");
		} catch (Exception e) {
			return;
		}
	}
}

class Veld {

	Main m = new Main();
	Thread fp, fs, f, reload;

	public static int kill = 0;
	public static int sstate[] = { 1, 1, 1, 50, 50, 1, 10, 2 };
//	str, agi, con, 현재 체력, 최대 체력, 레벨, 주는 경험치, 주는 돈

	public Veld() {

		JFrame jf = new JFrame();

		JPanel jp = new JPanel();
		jp.setSize(m.size[0], m.size[1]);
		jp.setLayout(null);
		jp.setVisible(true);

		new Title(jf, jp);

		Random rand = new Random();

		JLabel state = new JLabel("<html><body>플레이어" + "<br />레벨 : " + m.state[8] + "<br />경험치 : " + m.state[6] + "/"
				+ m.state[7] + "<br />체력 : " + m.state[4] + "/" + m.state[5] + "</body></html>");
		state.setVisible(true);
		state.setBounds(10, 50, 100, 100);

		JLabel find = new JLabel("찾는중입니다");
		find.setBounds(10, 150, 300, 100);

		reload = new Thread() {
			@Override
			public void run() {
				int i = 1;
				while (i == 1) {
					if (m.state[4] <= 0) {
						m.state[4] = 0;
						f.interrupt();
						fp.interrupt();
						fs.interrupt();
						i = 0;
					}
					if (sstate[3] <= 0) {
						sstate[3] = 0;
						m.state[6] += sstate[6];
						m.state[9] += sstate[7];
						m.it[0]++;
						kill++;
						f.interrupt();
						fp.interrupt();
						fs.interrupt();
						i = 0;
					}
					state.setText("<html><body>플레이어" + "<br />레벨 : " + m.state[8] + "<br />경험치 : " + m.state[6] + "/"
							+ m.state[7] + "<br />체력 : " + m.state[4] + "/" + m.state[5] + "</body></html>");
					find.setText("<html><body>슬라임" + "<br />레벨 : " + sstate[5] + "<br />체력 : " + sstate[3] + "/"
							+ sstate[4] + "</body></html>");
					if (i == 0) {
						try {
							m.size[2] = jf.getX();
							m.size[3] = jf.getY();
							sleep(5000);
							jf.dispose();
							new Veld();
							return;
						} catch (InterruptedException e) {
							return;
						}
					}
				}
			}
		};

		fp = new Thread() {

			@Override
			public void run() {
				try {
					while (m.state[4] > 0 && sstate[3] > 0) {
						sleep(2000 - m.state[1] * 10);
						if (Math.random() * 100 < 50 - sstate[1] + m.state[1]) {
							if (5 + rand.nextInt(m.state[0] - 2, m.state[0]) - sstate[2] > 0) {
								sstate[3] -= 5 + rand.nextInt(m.state[0] - 2, m.state[0]) - sstate[2];
							}
						}
					}
				} catch (InterruptedException e) {
					return;
				}
			}

		};

		fs = new Thread() {
			@Override
			public void run() {
				find.setText("<html><body>슬라임" + "<br />레벨 : " + sstate[5] + "<br />체력 : " + sstate[3] + "/" + sstate[4]
						+ "</body></html>");
				try {
					while (m.state[4] > 0 && sstate[3] > 0) {
						sleep(2000 - sstate[1] * 10);
						if (Math.random() * 100 < 50 - m.state[1] + sstate[1]) {
							if (5 + rand.nextInt(sstate[0] - 2, sstate[0]) - m.state[2] > 0) {
								m.state[4] -= 5 + rand.nextInt(sstate[0] - 2, sstate[0]) - m.state[2];
							}
						}
					}
				} catch (InterruptedException e) {
					return;
				}

			}
		};

		f = new Thread() {
			public void run() {
				for (int i = 0; i == 0;) {
					try {
						sleep(1000);
						if (m.state[4] > 0) {
							state.setText("<html><body>플레이어" + "<br />레벨 : " + m.state[8] + "<br />경험치 : " + m.state[6]
									+ "/" + m.state[7] + "<br />체력 : " + m.state[4] + "/" + m.state[5]
									+ "</body></html>");
							jp.repaint();
							find.setText("찾는중입니다");
							sleep(1000);
							find.setText("찾는중입니다.");
							sleep(1000);
							find.setText("찾는중입니다..");
							sleep(1000);
							find.setText("찾는중입니다...");
							sleep(1000);
							if (Math.random() * 100 < 50 + m.state[2]) {
								if (kill % 100 == 0 && kill != 0) {
									sstate[0]++;
									sstate[1]++;
									sstate[2]++;
									sstate[3] = sstate[4] += 2;
									sstate[5] += 3;
									sstate[6] += 10;
									sstate[7] += 2;
								}
								find.setText("찾았습니다.");
								sleep(1000);
								i++;
								reload.start();
								fp.start();
								fs.start();
								f.interrupt();
							} else {
								find.setText("못찾았습니다.");
								sleep(2500);
							}
						} else {
							find.setText("체력이 없어 더이상의 사냥은 위험해");
							jf.repaint();
						}
					} catch (InterruptedException e) {
						return;
					}
				}
			}
		};

		f.start();

		JButton exit = new JButton("돌아가기");
		exit.setBounds(m.size[0] / 25, 350, 270, 30);
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m.size[2] = jf.getX();
				m.size[3] = jf.getY();
				f.interrupt();
				fs.interrupt();
				fp.interrupt();
				reload.interrupt();
				jf.dispose();
				new Dungeon();
			}
		});

		jp.add(state);
		jp.add(find);
		jp.add(exit);

		jf.add(jp);

	}
}