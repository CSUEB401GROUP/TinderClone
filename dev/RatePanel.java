import javax.swing.*; 
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.ImageIO; 
import java.io.*;
 
public class RatePanel extends JPanel {  
	private JButton btn_left;
	private JButton btn_right;
	private MainWindow parent;
	private ProfilePanel profile_panel;
	private JLabel profile_name;
	private JLabel profile_age;
	private JLabel profile_pic;
	private ProfileList plist;
	
	public RatePanel(MainWindow mw){  
		try{
			this.setLayout(new BorderLayout());
			addLeftButton();
			this.profile_panel = new ProfilePanel(mw);
			this.add(this.profile_panel);
			addRightButton();
			this.setMinimumSize(new Dimension(500,500));
			this.parent = mw;
			this.setVisible(false);
		}catch(Exception e){
			
		}
	}
	
	public void showNextProfile(){
		this.setVisible(true);
		if(this.plist!=null){
			Profile p = this.plist.getNextProfile();
			if(p!=null){
				this.profile_panel.showProfile(p,true);
			}else{
				this.profile_panel.clear();
			}
		}
	}
	
	private void addLeftButton(){
		btn_left=new JButton("<");
		btn_left.setBounds(0,0,70, 20); 
		btn_left.addActionListener(new ActionListener(){  
			@Override
			public void actionPerformed(ActionEvent e){  
				if(plist!=null){
					Profile p = plist.getProfile();
					if(p!=null){
						parent.createMatch(p,"left");
						showNextProfile();
					}
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
				if(plist!=null){
					Profile p = plist.getProfile();
					if(p!=null){
						parent.createMatch(p,"right");
						showNextProfile();
					}
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