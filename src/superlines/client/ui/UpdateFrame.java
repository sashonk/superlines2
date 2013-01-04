package superlines.client.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import superlines.client.Application;

import superlines.client.FeedBack;
import superlines.client.Messages;

@SuppressWarnings("serial")
public class UpdateFrame extends JFrame implements FeedBack{
	private static Log log = LogFactory.getLog(UpdateFrame.class);
	
	private JProgressBar m_bar ;
	private JLabel m_label;
	private JButton m_closeBtn;

	public UpdateFrame(){
		this.setTitle(Messages.UPDATE.toString());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		
		Toolkit kit =Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		Dimension size = new Dimension(300, 120);
		this.setBounds(screenSize.width/2-size.width/2, screenSize.height/2-size.height/2, size.width, size.height);
		
		this.setLayout(new BorderLayout());
		
		JPanel bottomPanel = new JPanel();
		this.add(bottomPanel, BorderLayout.SOUTH);
		bottomPanel.setLayout(new FlowLayout());
		m_closeBtn = new JButton(Messages.CLOSE.toString());
		m_closeBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				UpdateFrame.this.dispose();
                                Application.exit(0);
			}
		});		
		m_closeBtn.setEnabled(false);
		bottomPanel.add(m_closeBtn);
				
		JPanel centerPanel = new JPanel();
		this.add(centerPanel, BorderLayout.CENTER);
		
		m_label = new JLabel();
		m_label.setAlignmentX(0f);
		m_bar = new JProgressBar();
		m_bar.setAlignmentX(0f);
		m_bar.setMinimum(0);
		m_bar.setMaximum(100);
		m_bar.setValue(0);
		m_bar.setStringPainted(true);
		
		GroupLayout gl = new GroupLayout(centerPanel);
		gl.setAutoCreateContainerGaps(true);
		gl.setAutoCreateGaps(true);
		centerPanel.setLayout(gl);
		
		gl.setHorizontalGroup(gl.createSequentialGroup().
				addGroup(gl.createParallelGroup().
						addComponent(m_label, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).
						addComponent(m_bar, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		gl.setVerticalGroup(gl.createSequentialGroup().
				addComponent(m_label,GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).
				addComponent(m_bar,GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE));
		
		
		this.getRootPane().setDefaultButton(m_closeBtn);
	}
	
	
	@Override
	public void updateTitle(final String value) {

				m_label.setText(value);	
	}

	@Override
	public void updateProgress(final float p) {

				int progress = (int)(100f*p);
				m_bar.setValue(progress);
				

	}
	
	@Override
	public void end(){
				m_bar.setValue(100);
				m_closeBtn.setEnabled(true);				
	}


	@Override
	public void begin() {
	
		
	}




}
