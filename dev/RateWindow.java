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
	
	public RateWindow(MainWindow mw){  
		try{
			String path = "male_small.jpg";
			this.setLayout(new BorderLayout());
			addLeftButton();
			BufferedImage image = ImageIO.read(new File(path));
			JLabel label = new JLabel(new ImageIcon(image));
			this.add(label,BorderLayout.CENTER);
			addRightButton();
			this.setMinimumSize(new Dimension(500,500));
			this.parent = mw;
			this.setVisible(false);
		}catch(IOException ioe){
			
		}
	}
	
	private void addLeftButton(){
		btn_left=new JButton("<");
		btn_left.setBounds(0,0,70, 20); 
		btn_left.addActionListener(new ActionListener(){  
			@Override
			public void actionPerformed(ActionEvent e){  
				//showNearbyMatches();
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
				//showMatchHistory();
			}  
		});  
		this.add(btn_right,BorderLayout.EAST);
	}
	
}