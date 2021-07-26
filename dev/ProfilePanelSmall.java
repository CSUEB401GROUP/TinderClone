import javax.swing.*; 
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.ImageIO; 
import java.io.*;
 
public class ProfilePanelSmall extends JPanel {  
	private JLabel label_icon;
	private JLabel label_name;
	private JLabel label_age;
	private JLabel label_gender;
	private JPanel panel_info;
	private JButton btn_view;
	private MainWindow win_obj;
	
	public ProfilePanelSmall(MainWindow mw,Profile p){
		try{
			this.label_icon = new JLabel();
			this.label_name = new JLabel();
			this.label_age = new JLabel();
			this.label_gender = new JLabel();
			this.panel_info = new JPanel(new GridLayout(3,1));
			this.win_obj = mw;
			
			String fname = p.getAttribute("profile_fname",false);
			String lname = p.getAttribute("profile_lname",false);
			String age = p.getAttribute("profile_age",false);
			String gender = p.getAttribute("profile_gender",false);
			
			String imgpath = (gender.toUpperCase().compareTo("MALE")==0) ? "male_icon.jpg" : "female_icon.jpg";

			String filepath = p.getPicture().getPath();
			BufferedImage image = ImageIO.read(new File(imgpath));
			label_icon.setIcon(new ImageIcon(image));
			this.add(label_icon);
			

			label_name.setText(fname + " " + lname);
			label_gender.setText(gender);
			label_age.setText(age + " years old");
			panel_info.add(label_name);
			panel_info.add(label_gender);
			panel_info.add(label_age);
			this.add(panel_info);
			
			this.btn_view = new JButton("View Profile");
			this.btn_view.addActionListener(new ActionListener(){  
				@Override
				public void actionPerformed(ActionEvent e){  
					mw.showProfile(p,false);
				}
				
			}); 
			this.add(btn_view);
			
			this.setLayout(new FlowLayout());
		}catch(IOException ioe){
			
		}
	}
	
	
	
}