import javax.swing.*; 
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.ImageIO; 
import java.io.*;
 
public class LoginWindow extends JFrame {  
	private JButton btn_submit;
	private JPanel panel_header;
	private JTextField input_user;
	private JPasswordField input_pass;
	private JLabel text_user;
	private JLabel text_pass;
	private JPanel field_user;
	private JPanel field_pass;
	private Authentication auth_obj;
	private User user_obj;
	private MatchSystem match_obj;
	
	public LoginWindow(Authentication auth, User u, MatchSystem ms){ 
		this.auth_obj = auth;
		this.user_obj = u;
		this.match_obj = ms;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//this.setUndecorated(true);
		
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(d.width/2-this.getSize().width/2, d.height/2-this.getSize().height/2);
		
		this.panel_header = new JPanel(new FlowLayout());
		this.panel_header.setMinimumSize(new Dimension(100,120));
		this.panel_header.setBackground(Color.pink);
		JLabel label_title = new JLabel("Login Page");
		label_title.setForeground(Color.white);
		this.panel_header.add(label_title,BorderLayout.CENTER);
		this.add(panel_header);
		
		this.field_user = new JPanel(new GridLayout(1,2));
		this.text_user = new JLabel("Username");
		this.input_user = new JTextField(20);
		this.input_user.setColumns(20);
		this.input_user.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                input_user.setText("");
            }
        });
		this.field_user.add(this.text_user);
		this.field_user.add(this.input_user);
		this.add(this.field_user);
		
		this.field_pass = new JPanel(new GridLayout(1,2));
		this.text_pass = new JLabel("Password");
		this.input_pass = new JPasswordField(20);
		this.input_pass.setColumns(20);
		LoginWindow ref = this;
		this.input_pass.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					openMainWindow();            
				}
			}
        });
		this.input_pass.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                input_pass.setText("");
            }
        });
		this.field_pass.add(this.text_pass);
		this.field_pass.add(this.input_pass);
		this.add(this.field_pass);
		
		this.btn_submit = new JButton("SUBMIT");
		btn_submit.setBounds(0,0,70, 20); 
		ref = this;
		btn_submit.addActionListener(new ActionListener(){  
			@Override
			public void actionPerformed(ActionEvent e){  
				openMainWindow();
			} 
		});  
		this.add(btn_submit);
		
		this.setSize(100,270);
		this.setVisible(true);
		this.setLayout(new GridLayout(4,1));
		this.pack();
	}
	
	public void openMainWindow(){
		auth_obj.login(input_user.getText(),input_pass.getText());
		new MainWindow(auth_obj,user_obj,match_obj);
		this.dispose(); 
	}
	
}