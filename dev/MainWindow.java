import javax.swing.*; 
import java.awt.*;
import java.awt.event.*;
 
public class MainWindow {  
	private JFrame win;
	private JButton btn_logout;
	private JButton btn_nearby;
	private JButton btn_mutual;
	private JButton btn_history;
	private JPanel controlPanel;
	private DefaultListModel data_list;
	private JList list_obj;
	private MatchSystem match_obj;
	private RateWindow rate_win;
	
	public MainWindow(MatchSystem ms){  
		win=new JFrame();
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		match_obj = ms;	  

		addButtons();
		
		data_list = new DefaultListModel();

		list_obj = new JList(data_list);
		list_obj.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list_obj.setSelectedIndex(0);
		      

		JScrollPane dataListScrollPane = new JScrollPane(list_obj);  
		controlPanel = new JPanel();
		controlPanel.setLayout(new GridLayout(3,1));
		controlPanel.add(btn_nearby);
		controlPanel.add(btn_history);
		controlPanel.add(btn_mutual);
		controlPanel.setMaximumSize(new Dimension(100,500));
		dataListScrollPane.setMaximumSize(new Dimension(500, 100));
		
		this.rate_win = new RateWindow(this);
		
		win.add(controlPanel);
		win.add(rate_win);
		win.add(dataListScrollPane);
		
		win.setSize(800,500);
		win.setLayout(new GridLayout(1, 3));
		win.setVisible(true);
	}  
	
	public void addItem(String item){
		data_list.addElement(item);
	}
	
	private void addButtons(){
		btn_nearby=new JButton("Nearby Matches");
		btn_nearby.setBounds(130,100,30,20); 
		btn_nearby.addActionListener(new ActionListener(){  
			@Override
			public void actionPerformed(ActionEvent e){  
				showNearbyMatches();
			} 
		});  
		
		btn_history=new JButton("Match History");
		btn_history.setBounds(150,100,30,20); 
		btn_history.addActionListener(new ActionListener(){  
			@Override
			public void actionPerformed(ActionEvent e){  
				showMatchHistory();
			}  
		});  
		btn_mutual=new JButton("Mutual Matches");
		btn_mutual.setBounds(130,100,30,20); 
		btn_mutual.addActionListener(new ActionListener(){  
			@Override
			public void actionPerformed(ActionEvent e){  
				showMutualMatches();
			} 
		}); 
	}
	
	public void createMatch(){
		//
	}

	public void showNearbyMatches(){
		this.rate_win.setVisible(true);
		this.data_list.clear();				
		ProfileList profiles;
		profiles = match_obj.getMatches(true);
		//If there are any profiles in the results, print them
		if(profiles!=null){
			Profile p = profiles.getNextProfile();
			
			while(p != null){
				addItem(p.getAttribute("profile_fname",false));
				p = profiles.getNextProfile();
			}
		}
	}

	public void showMutualMatches(){
		this.rate_win.setVisible(false);
		this.data_list.clear();				
		ProfileList profiles;
		profiles = match_obj.getMutualMatches();
		//If there are any profiles in the results, print them
		if(profiles!=null){
			Profile p = profiles.getNextProfile();
			
			while(p != null){
				addItem(p.getAttribute("profile_fname",false));
				p = profiles.getNextProfile();
			}
		}
	}
	
	public void showMatchHistory(){
		this.rate_win.setVisible(false);
		this.data_list.clear();
		ProfileList profiles;
		profiles = match_obj.getMatchHistory();
		//If there are any profiles in the results, print them
		if(profiles!=null){
			Profile p = profiles.getNextProfile();
			
			while(p != null){
				addItem(p.getAttribute("profile_fname",false));
				p = profiles.getNextProfile();
			}
		}
	}

}  