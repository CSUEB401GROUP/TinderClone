import javax.swing.*; 
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.ImageIO; 
import java.io.*;
 
public class ProfilePanel extends JPanel {  
	private JLabel label_img;
	private JLabel label_name;
	private JLabel label_age;
	private JLabel label_gender;
	private JLabel label_city;
	private JLabel label_distance;
	private JPanel panel_info;
	private JButton btn_view;
	private MainWindow win_obj;
	
	public ProfilePanel(MainWindow mw){
		
		this.label_img = new JLabel();
		this.label_name = new JLabel();
		this.label_age = new JLabel();
		this.label_gender = new JLabel();
		this.label_city = new JLabel();
		this.label_distance = new JLabel();
		this.panel_info = new JPanel(new GridLayout(5,1));
		this.win_obj = mw;
		

		this.add(panel_info);
		this.add(label_img);
		this.setLayout(new FlowLayout());
		this.setVisible(false);

	}
	
	public void showProfile(Profile p, Boolean show_distance){
		try{
			this.setVisible(true);
			if(p!=null){
				String str_distance = "";
				
				if(show_distance){
					int d = this.win_obj.getDistance(p);
					str_distance = Integer.toString(d) + " miles away";
				}
				
				String fname = p.getAttribute("profile_fname",false);
				String lname = p.getAttribute("profile_lname",false);
				String age = p.getAttribute("profile_age",false);
				String gender = p.getAttribute("profile_gender",false);
				label_name.setText(fname + " " + lname);
				label_gender.setText(gender);
				label_age.setText(age + " years old");
				label_city.setText(p.getCity());
				label_distance.setText(str_distance);
				panel_info.add(label_name);
				panel_info.add(label_gender);
				panel_info.add(label_age);
				panel_info.add(label_city);
				panel_info.add(label_distance);

				String imgpath = (gender.toUpperCase().compareTo("MALE")==0) ? "male.jpg" : "female.jpg";
					
				String filepath = p.getPicture().getPath();
				BufferedImage image = ImageIO.read(new File(imgpath));
				label_img.setIcon(new ImageIcon(image));
			}else{
				this.clear();
			}
		}catch(IOException ioe){
			
		}
	}
	
	public void clear(){
		label_name.setText("");
		label_gender.setText("");
		label_age.setText("");
		label_city.setText("");
		label_distance.setText("");
		label_img.setIcon(null);
	}
}