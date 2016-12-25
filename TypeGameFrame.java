package test;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Vector;

public class TypeGameFrame extends JFrame implements Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 112233445566L;
	JPanel contentPane;
	JPanel jMainPanel = new JPanel();
	// ��ʼ������ ��ť
	JButton jBtnStart = new JButton();
	JButton jBtnStop = new JButton();
	JButton jBtnPause1 = new JButton(); // ��ͣ��ť
	JButton jBtnPause2 = new JButton(); // ��ͣ��ť

	JSlider jSlider1 = new JSlider(); // �ٶ���
	JSlider jSlider2 = new JSlider(); // ʱ����
	JSlider jSlider3 = new JSlider(); // �ַ�����

	JLabel jLblRate1 = new JLabel();
	JLabel jLblRate2 = new JLabel();
	JLabel jLblRate3 = new JLabel();	
	JLabel jLblTypedResult = new JLabel();
	
	Thread t ;
	MyListener mylister;
	boolean pause = true;
	boolean fo = true;
	int count = 1; // count ��ǰ���еĸ���
	int count_geshu = 20;
	int time = 0;
	long timeNum =0; //�����ͣ
	long t1 = 0; // ��ʼ
	long t2 = 0; // ��ͣ
	long t3 = 0; // ��ͣ����
	long t4 = 0; // ����
	int rapidity = 80; // rapidity �α��λ��
	int rapidity2 = 91;
	int rapidity3 = 81;
	// ���ֺ�ĳɹ�������ʧ�ܸ���
	int nTypedCorrectNum = 0;
	int nTypedErrorNum = 0;
	int rush[] = { 10, 20, 30 }; // ��Ϸÿ�صĸ��� �����������.�� { 10 ,20 ,30 ,40,50}
	// ��¼����
	int rush_count = 0;
	// ������ֵ������б������������
	char chrList[] = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
			'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	Vector number = new Vector();
	Vector x = new Vector();
	Vector y = new Vector();
	Vector value = new Vector();
	String paiduan = "true";
	AudioClip Musci_anjian, Music_shibai, Music_chenggong;

	public TypeGameFrame() {
		try {
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			// -----------------�����ļ�---------------------
			Musci_anjian = Applet.newAudioClip(new File("sounds//anjian.wav").toURL());
			Music_shibai = Applet.newAudioClip(new File("sounds//shibai.wav").toURL());
			Music_chenggong = Applet.newAudioClip(new File("sounds//chenggong.wav").toURL());
			// ---------------------------------------
			jbInit();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * Component initialization.
	 * 
	 * @throws java.lang.Exception
	 */
	private void jbInit() throws Exception {
		contentPane = (JPanel) getContentPane();
		contentPane.setLayout(null);
		setSize(new Dimension(800, 490));
		setTitle("������ϰ");
		ImageIcon icon = new ImageIcon("resource/images/poinfoedit.gif");
		setIconImage(icon.getImage());

		jMainPanel.setBorder(BorderFactory.createEtchedBorder());
		jMainPanel.setBounds(new Rectangle(4, 4, 573, 419));
		jMainPanel.setLayout(null);

		// ��ʼ
		jBtnStart.setBounds(new Rectangle(580, 154, 164, 31));
		jBtnStart.setText("��ʼ/���¿�ʼ");
		jBtnStart.addActionListener(new TypeGameFrame_jBtnStart_actionAdapter(this));
		mylister=new MyListener();
		jBtnStart.addKeyListener(mylister);

		// ��ͣ
		/**
		 * TODO
		 */
		jBtnPause1.setBounds(new Rectangle(580, 189, 164, 31));
		jBtnPause1.setText("��ͣ");
		jBtnPause1.addActionListener(new TypeGameFrame_jBtnPause1_actionAdapter(this));
		jBtnPause1.addMouseListener(new MouseLister());
		// ��ͣ2��ʼ
		/**
		 * TODO
		 */
		jBtnPause2.setBounds(new Rectangle(580, 189, 164, 31));
		jBtnPause2.setText("ȡ����ͣ");
		jBtnPause2.setVisible(false);
		jBtnPause2.addActionListener(new TypeGameFrame_jBtnPause2_actionAdapter(this));
		jBtnPause2.addMouseListener(new MouseLister());
		
		// ����
		jBtnStop.setBounds(new Rectangle(580, 224, 164, 31));
		jBtnStop.setText("����(�˳�)");
		jBtnStop.addActionListener(new TypeGameFrame_jBtnStop_actionAdapter(this));

		// �ٶ�
		jSlider1.setBounds(new Rectangle(580, 22, 164, 21));
		jSlider1.setMaximum(100);
		jSlider1.setMinimum(1);
		jSlider1.setValue(50);
		jSlider1.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				rapidity = jSlider1.getValue();
				byte nTmpRapidity = (byte) (101 - rapidity);
				jLblRate1.setText("�����ٶ�(1-100):" + nTmpRapidity);
			}
		});
		jLblRate1.setText("�����ٶ�(1-100):50");
		jLblRate1.setBounds(new Rectangle(580, 4, 149, 18));

		// ����
		jSlider3.setBounds(new Rectangle(580, 71, 164, 21));
		
		jSlider3.setMaximum(100);
		jSlider3.setMinimum(1);
		jSlider3.setValue(80);
		jSlider3.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				rapidity3 = jSlider3.getValue();
				byte nTmpRapidity = (byte) (101 - rapidity3);
				count_geshu = 101 - rapidity3;
				jLblRate3.setText("��������(10-100):" + nTmpRapidity + " ��");
			}
		});
		jLblRate3.setText("��������(10-100):20 ��");
		jLblRate3.setBounds(new Rectangle(580, 53, 149, 18));


		// ʱ��
		jSlider2.setBounds(new Rectangle(580, 120, 164, 21));
		jSlider2.setMaximum(100);
		jSlider2.setMinimum(1);
		jSlider2.setValue(90);
		rapidity2 = jSlider2.getValue();
		time = 101 - rapidity2;
		jSlider2.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				rapidity2 = jSlider2.getValue();
				byte nTmpRapidity = (byte) (101 - rapidity2);
				time = 101 - rapidity2;
				jLblTypedResult.setText("��ȷ:0��,����:0��,ʣ��ʱ��:"+time+"s");
				jLblRate2.setText("����ʱ��(5-100):" + nTmpRapidity + " ��");
			}
		});
		jLblRate2.setText("����ʱ��(5-100):10 ��");
		
		jLblRate2.setBounds(new Rectangle(580, 102, 149, 18));
		// ���ֽ��
		jLblTypedResult.setText("��ȷ:0��,����:0��,ʣ��ʱ��:10s");
		jLblTypedResult.setBounds(new Rectangle(256, 423, 211, 21));

		contentPane.add(jMainPanel);
		contentPane.add(jBtnStop);
		contentPane.add(jBtnPause1);
		contentPane.add(jBtnPause2);
		contentPane.add(jBtnStart);
		contentPane.add(jSlider1);		
		contentPane.add(jSlider3);
		contentPane.add(jSlider2);
		contentPane.add(jLblRate1);
		contentPane.add(jLblRate2);
		contentPane.add(jLblRate3);
		contentPane.add(jLblTypedResult);
		this.addKeyListener(mylister);
	}

	public void run() {
		number.clear();
		jLblTypedResult.setText("��ȷ:0��,����:0��,ʣ��ʱ��:10s");
		nTypedCorrectNum = 0;
		nTypedErrorNum = 0;
		paiduan = "true";
		int tempTime=0;
		t4 = System.currentTimeMillis();
		while ( t4- t1 - timeNum <= time * 1000 ) {				
		//	if (pause) {
				if (count < count_geshu) {
					try {	
						t4 = System.currentTimeMillis();
						if(t4- t1 - timeNum > time * 1000 ){
							break;
						}else{
							;
						}
						if(pause){
						Thread t = new Thread(new Tthread());
						t.start();
						count += 1;	
						tempTime= time-1-(int) (t4-t1-timeNum)/1000;
						jLblTypedResult.setText("��ȷ:" + nTypedCorrectNum + "��,����:" + nTypedErrorNum + "��"+",ʣ��ʱ��:"+tempTime+"s");	
						Thread.sleep(500);
						}else{
							jLblTypedResult.setText("��ȷ:" + nTypedCorrectNum + "��,����:" + nTypedErrorNum + "��"+",ʣ��ʱ��:"+tempTime+"s");
							Thread.sleep(100);	
						}
						// Thread.sleep(500 + (int) (Math.random() * 1000)); //
						// ��������ͣ��ʱ��
						// ���0.5��.����1��
					} catch (InterruptedException e) {
						e.printStackTrace();
					}					
				}else{					
					try {	
						t4 = System.currentTimeMillis();
						if(t4- t1 - timeNum > time * 1000 ){
							break;
						}else{
							tempTime= time-1-(int) (t4-t1-timeNum)/1000;
							jLblTypedResult.setText("��ȷ:" + nTypedCorrectNum + "��,����:" + nTypedErrorNum + "��"+",ʣ��ʱ��:"+tempTime+"s");
					
						}						
					   Thread.sleep(100);												
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
//			}else{
//				continue;
//			}		
		}				
		
		fo = false;
		int falseNum=count_geshu -nTypedCorrectNum;
		JOptionPane.showMessageDialog(null, "��ȷ:" + nTypedCorrectNum + "��,����:" + falseNum + "��");
	}

	public void jBtnStart_actionPerformed(ActionEvent e) {
		// jMainPanel.removeAll();
		for (int i = 0; i < number.size(); i++) {
			Bean bean = ((Bean) number.get(i));
			bean.getShow().setVisible(false);
		}
		t1 = System.currentTimeMillis();
		jLblTypedResult.setText("��ȷ:0��,����:0��,ʣ��ʱ��:10s");
		number.clear();
		nTypedCorrectNum = 0;
		nTypedErrorNum = 0;
		count = 0;
		fo = true;
		t2 = 0;
		t3 = 0;
		pause = true;
		timeNum = 0;
		Thread t = new Thread(this);
		t.start();
	}

	// ��ͣ
	public void jBtnPause1_actionPerformed(ActionEvent e1) {
		t2 = System.currentTimeMillis();
		t3 = t2;
		// fo =false;
		pause = false;
		jBtnPause1.setVisible(false);
		jBtnPause2.setVisible(true);
		JOptionPane.showMessageDialog(null, "��ͣ��,�޷�����");
	//	jBtnStart.removeKeyListener(mylister);
	}

	// ��ͣ2��ʼ
	public void jBtnPause2_actionPerformed(ActionEvent e2) {
		t3 = System.currentTimeMillis();
		timeNum +=t3 -t2;
		// fo =true;
		pause = true;
		jBtnPause1.setVisible(true);
		jBtnPause2.setVisible(false);		
	//	jBtnStart.addKeyListener(mylister);
	//	contentPane.requestFocusInWindow();
		jBtnStart.requestFocusInWindow();
		
	}

	public void jBtnStop_actionPerformed(ActionEvent e3) {
		// count = rush[rush_count] + 1;
		// paiduan = "flase";
		System.exit(0);
		contentPane.setVisible(false);
		;
	}

	/**
	 * 
	 * */
	class Tthread implements Runnable {
		public void run() {
			// boolean fo = true;			
		//	int tempTime= time-2-(int) (t4-t1-timeNum)/1000;
		//	jLblTypedResult.setText("��ȷ:" + nTypedCorrectNum + "��,����:" + nTypedErrorNum + "��"+",ʣ��ʱ��:"+tempTime);				
			int Y = 0, X = 0;
			// int temp_y=0;
			JLabel show = new JLabel();
			show.setFont(new java.awt.Font("����", Font.PLAIN, 43));
			jMainPanel.add(show);
			X = 10 + (int) (Math.random() * 400);
			String parameter = chrList[(int) (Math.random() * chrList.length)] + "";
			Bean bean = new Bean();
			bean.setParameter(parameter);
			bean.setShow(show);
			number.add(bean);
			show.setText(parameter);
			while (fo) {
				// ---------------------��������--------------------
				if (pause) {
					show.setBounds(new Rectangle(X, Y += 2, 33, 33));
				} else {
					show.setBounds(new Rectangle(X, Y += 0, 0, 0));
				}
				try {
					Thread.sleep(rapidity);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (Y >= 419) {
					for (int i = number.size() - 1; i >= 0; i--) {
						Bean bn = ((Bean) number.get(i));
						if (parameter.equalsIgnoreCase(bn.getParameter())) {
							nTypedErrorNum += 1;
					//	int	tempTime= time-1-(int) (t4-t1-timeNum)/1000;
					//		jLblTypedResult.setText("��ȷ:" + nTypedCorrectNum + "��,����:" + nTypedErrorNum + "��"+",ʣ��ʱ��:"+tempTime);
							number.removeElementAt(i);
							Music_shibai.play();
							break;
						}
					}
				}
			}

		}
	}

	/**
	 * �������̣�ͳ����ȷ�ĸ���
	 *
	 */
	class MyListener extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			String uu = e.getKeyChar() + "";
			for (int i = 0; i < number.size(); i++) {
				Bean bean = ((Bean) number.get(i));
				if (uu.equalsIgnoreCase(bean.getParameter())) {
					nTypedCorrectNum += 1;
					number.removeElementAt(i);
					bean.getShow().setVisible(false);
					Music_chenggong.play();
					break;
				}
			}
			Musci_anjian.play();
		}
	}
	/**
	 * 
	 * @author fan
	 *
	 */
	class MouseLister extends MouseAdapter{
		public void mouseClieced(MouseEvent e){
			
		}
	}
	
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		// ��������
		TypeGameFrame gameFrame = new TypeGameFrame();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = gameFrame.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		gameFrame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		gameFrame.setVisible(true);
	}

}

/**
 * ֹͣ��ť������
 */
class TypeGameFrame_jBtnStop_actionAdapter implements ActionListener {
	private TypeGameFrame adaptee;

	TypeGameFrame_jBtnStop_actionAdapter(TypeGameFrame adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jBtnStop_actionPerformed(e);
	}
}

/**
 * ��ʼ��ť������
 */
class TypeGameFrame_jBtnStart_actionAdapter implements ActionListener {
	private TypeGameFrame adaptee;

	TypeGameFrame_jBtnStart_actionAdapter(TypeGameFrame adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jBtnStart_actionPerformed(e);
	}
}

/**
 * ��ͣ��ť������
 */
class TypeGameFrame_jBtnPause1_actionAdapter implements ActionListener {
	private TypeGameFrame adaptee;

	TypeGameFrame_jBtnPause1_actionAdapter(TypeGameFrame adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jBtnPause1_actionPerformed(e);
	}
}

/**
 * ��ͣ2��ʼ��ť������
 */
class TypeGameFrame_jBtnPause2_actionAdapter implements ActionListener {
	private TypeGameFrame adaptee;

	TypeGameFrame_jBtnPause2_actionAdapter(TypeGameFrame adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jBtnPause2_actionPerformed(e);
	}
}

class Bean {
	String parameter = null;
	JLabel show = null;

	public JLabel getShow() {
		return show;
	}

	public void setShow(JLabel show) {
		this.show = show;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
}
