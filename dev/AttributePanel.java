import javax.swing.*; 
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.ImageIO; 
import java.io.*;
 
public class AttributePanel extends JPanel {  
	private JPanel info_panel;
	private JLabel label_heading;
	private JTextField input_user;
	private JTextField input_pref;
	private MainWindow win_obj;
	
	public AttributePanel(MainWindow mw,String heading, String user_value, String pref_value){
		heading = heading.substring(8);
		this.label_heading = new JLabel(heading);
		this.win_obj = mw;
		
		this.input_user = new JTextField(user_value,5);
		this.input_pref = new JTextField(pref_value,5);
		
		if(heading.compareTo("fname")==0 || heading.compareTo("lname")==0){
			this.input_pref.setEnabled(false);
		}else if(heading.compareTo("distance")==0){
			this.input_user.setEnabled(false);
		}
		
		this.info_panel = new JPanel(new GridLayout(1,3));
		this.info_panel.add(label_heading);
		this.info_panel.add(input_user);
		this.info_panel.add(input_pref);
		
		this.info_panel.setSize(40,20);
		this.add(info_panel);
		
		this.setLayout(new FlowLayout());
	}
	
	public String[] getAllValues(){
		return new String[]{this.label_heading.getText(),this.input_user.getText(),this.input_pref.getText()};
	}
}