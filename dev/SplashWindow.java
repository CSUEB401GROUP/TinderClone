import javax.swing.*; 
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.ImageIO; 
import java.io.*;
 
public class SplashWindow extends JFrame {  
	private JButton btn_login;
	private JButton btn_register;
	private Authentication auth_obj;
	
	public SplashWindow(Authentication auth,User u,MatchSystem ms){ 
		this.auth_obj = auth;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(d.width/2-this.getSize().width/2, d.height/2-this.getSize().height/2);
		
		this.btn_login = new JButton("LOGIN");
		btn_login.setBounds(0,0,70, 20); 
		SplashWindow ref = this;
		btn_login.addActionListener(new ActionListener(){  
			@Override
			public void actionPerformed(ActionEvent e){  
				new LoginWindow(auth_obj,u,ms);
				ref.dispose();
			} 
		});  
		this.add(btn_login);
		
		this.btn_register = new JButton("REGISTER");
		btn_register.setBounds(0,0,70, 20); 
		btn_register.addActionListener(new ActionListener(){  
			@Override
			public void actionPerformed(ActionEvent e){  
			System.out.println("register pressed");
				new RegisterWindow(auth_obj,u,ms);
				ref.dispose();
			} 
		});  
		this.add(btn_register);
		
		this.setSize(500,300);
		this.setVisible(true);
		this.setLayout(new GridLayout(2,1));
	}
	
}