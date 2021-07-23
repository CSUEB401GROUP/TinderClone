import javax.swing.*; 
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.ImageIO; 
import java.io.*;
 
public class RateWindow extends JPanel {  
	private JButton btn_left;
	private JButton btn_right;
	private MainWindow parent;
	private JPanel panel_profile;
	private JLabel profile_name;
	private JLabel profile_age;
	private JLabel profile_pic;
	private ProfileList plist;
	
	public RateWindow(MainWindow mw){  
		try{
			this.setLayout(new BorderLayout());
			addLeftButton();
			panel_profile = new JPanel(new GridLayout(3,1));
			profile_name = new JLabel();
			profile_age = new JLabel();
			profile_pic = new JLabel();
			panel_profile.add(profile_name);
			panel_profile.add(profile_age);
			panel_profile.add(profile_pic);
			this.add(panel_profile);
			addRightButton();
			this.setMinimumSize(new Dimension(500,500));
			this.parent = mw;
			this.setVisible(false);
		}catch(Exception e){
			
		}
	}
	
	public void showNextProfile(){
		try{
			this.setVisible(true);
			if(this.plist!=null){
				Profile p = plist.getNextProfile();
				if(p!=null){
					profile_pic.setText("");
					BufferedImage image = ImageIO.read(new File(p.getPicture().getPath()));
					String fname = p.getAttribute("profile_fname",false);
					String lname = p.getAttribute("profile_lname",false);
					String age = p.getAttribute("profile_age",false);
					String gender = p.getAttribute("profile_gender",false);
					profile_name.setText(fname + " " + lname);
					profile_pic.setIcon(new ImageIcon(image));
					profile_age.setText(gender + ", " + age + " years old");
				}else{
					profile_name.setText("");
					profile_age.setText("");
					profile_pic.setText("No more profiles nearby");
					profile_pic.setIcon(null);
					//this.setVisible(false);
				}
			}
		}catch(IOException ioe){
			
		}
	}
	
	private void addLeftButton(){
		btn_left=new JButton("<");
		btn_left.setBounds(0,0,70, 20); 
		btn_left.addActionListener(new ActionListener(){  
			@Override
			public void actionPerformed(ActionEvent e){  
				Profile p = plist.getProfile();
				if(p!=null){
					parent.createMatch(p,"left");
					showNextProfile();
				}
			} 
		});  
		this.add(btn_left,BorderLayout.WEST);
	}
		
	private void addRightButton(){	
		btn_right=new JButton(">");
		btn_right.setBounds(0,0,70, 20); 
		btn_right.addActionListener(new ActionListener(){  
			@Override
			public void actionPerformed(ActionEvent e){  
				Profile p = plist.getProfile();
				if(p!=null){
					parent.createMatch(p,"right");
					showNextProfile();
				}
			}  
		});  
		this.add(btn_right,BorderLayout.EAST);
	}
	
	public void setProfileList(ProfileList pl){
		this.plist = pl;
		this.plist.resetIndex();
	}

}