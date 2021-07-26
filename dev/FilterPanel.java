import javax.swing.*; 
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.ImageIO; 
import java.io.*;
 
public class FilterPanel extends JPanel {  
	private JPanel info_panel;
	private JLabel label_heading;
	private JTextField input_value;
	private JButton btn_edit;
	private MainWindow win_obj;
	
	public FilterPanel(MainWindow mw,String heading, String value){

		heading = heading.substring(8);

		this.label_heading = new JLabel(heading);
		this.win_obj = mw;
		
		this.input_value = new JTextField(value,5);
		
		this.info_panel = new JPanel(new GridLayout(1,3));
		this.info_panel.add(label_heading);
		
		
		this.info_panel.add(input_value);
		this.btn_edit = new JButton("Add Filter");
		this.btn_edit.addActionListener(new ActionListener(){  
			@Override
			public void actionPerformed(ActionEvent e){  
				mw.addFilter("profile_" + label_heading.getText(),input_value.getText());
				mw.showNearbyMatches();
			}
			
		});
		this.info_panel.add(btn_edit);
		this.info_panel.setSize(40,20);
		this.add(info_panel);
		
		this.setLayout(new FlowLayout());
	}
	
	public String[] getAllValues(){
		return new String[]{"profile_" + this.label_heading.getText().substring(8),this.input_value.getText()};
	}
}