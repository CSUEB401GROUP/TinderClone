import javax.swing.*; 
import java.awt.*;
import java.awt.event.*;
import java.util.*;
 
public class MainWindow extends JFrame{  
	private JButton btn_logout;
	private JButton btn_nearby;
	private JButton btn_mutual;
	private JButton btn_history;
	private JButton btn_home;
	private JButton btn_edit;
	private JPanel menu_panel;
	private DefaultListModel data_list;
	private JList list_obj;
	private MatchSystem match_obj;
	private RatePanel rate_panel;
	private ProfilePanel profile_panel;
	private Authentication auth_obj;
	private User user_obj;
	private Box box_profiles;
	private Boolean profile_mode;
	private Boolean rate_mode;
	
	public MainWindow(Authentication auth, User u, MatchSystem ms){  

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(d.width/2-this.getSize().width/2, d.height/2-this.getSize().height/2);
		auth_obj = auth;
		user_obj = u;
		match_obj = ms;	  

		addButtons();
		
		data_list = new DefaultListModel();

		list_obj = new JList(data_list);
		list_obj.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list_obj.setSelectedIndex(0);
		      

		JScrollPane dataListScrollPane = new JScrollPane(list_obj);  
		menu_panel = new JPanel();
		menu_panel.setLayout(new GridLayout(5,1));
		menu_panel.add(btn_home);
		menu_panel.add(btn_edit);
		menu_panel.add(btn_nearby);
		menu_panel.add(btn_history);
		menu_panel.add(btn_logout);
		menu_panel.setMaximumSize(new Dimension(100,500));
		dataListScrollPane.setMaximumSize(new Dimension(500, 100));
		
		this.rate_panel = new RatePanel(this);
		this.profile_panel = new ProfilePanel(this);
		
		this.add(menu_panel);
		this.add(rate_panel);

		box_profiles = Box.createVerticalBox();
		
		JPanel box_panel = new JPanel();
		box_panel.setLayout(new BoxLayout(box_panel, BoxLayout.Y_AXIS));
		box_panel.add(box_profiles);

		this.add(box_panel);
		
		this.setSize(800,500);
		this.setLayout(new GridLayout(1, 3));
		this.setVisible(true);
		
		this.rate_mode = false;
		this.profile_mode = false;
		
		this.showHome();
	}  
	
	public void addProfileSmall(Profile p){
		box_profiles.add(new ProfilePanelSmall(this,p),BorderLayout.WEST);
		this.revalidate();
	}
	
	private void addButtons(){
		
		btn_home=new JButton("Home");
		btn_home.setBounds(130,100,30,20); 
		btn_home.addActionListener(new ActionListener(){  
			@Override
			public void actionPerformed(ActionEvent e){  
				showHome();
			} 
		}); 
		
		btn_edit=new JButton("Edit Profile");
		btn_edit.setBounds(130,100,30,20); 
		btn_edit.addActionListener(new ActionListener(){  
			@Override
			public void actionPerformed(ActionEvent e){  
				editProfile();
			} 
		}); 
		
		btn_nearby=new JButton("Nearby Matches");
		btn_nearby.setBounds(130,100,30,20); 
		btn_nearby.addActionListener(new ActionListener(){  
			@Override
			public void actionPerformed(ActionEvent e){  
				showNearbyMatches();
			} 
		});  
		
		btn_history=new JButton("Match History");
		btn_history.setBounds(130,100,30,20); 
		btn_history.addActionListener(new ActionListener(){  
			@Override
			public void actionPerformed(ActionEvent e){  
				showMatchHistory();
			}  
		});  
		btn_logout=new JButton("Logout");
		btn_logout.setBounds(130,100,30,20); 
		btn_logout.addActionListener(new ActionListener(){  
			@Override
			public void actionPerformed(ActionEvent e){  
				logout();
			} 
		}); 
	}
	
	public void showHome(){
		this.box_profiles.removeAll();
		showMutualMatches();
		showUserProfile();
	}
	
	public void editProfile(){
		this.box_profiles.removeAll();
		Profile p = this.match_obj.getUserProfile();
		HashMap<String,String> user_values = p.getAllAttributes(false);
		HashMap<String,String> pref_values = p.getAllAttributes(true);
		user_values.forEach((String key,String val) -> this.box_profiles.add(new AttributePanel(this,key,val,pref_values.get("profile_pref_" + key.substring(8)))));
		JButton btn_submit = new JButton("Submit Changes");
		btn_submit.addActionListener(new ActionListener(){  
			@Override
			public void actionPerformed(ActionEvent e){  
				submitEdits();
			} 
		}); 
		this.box_profiles.add(btn_submit);
		showUserProfile();		
		
	}
	
	public void showFilters(){
		this.box_profiles.add(new JLabel("Filter Settings"));
		
		ArrayList<String> headings = new ArrayList<String>();
		headings.add("profile_age");
		headings.add("profile_gender");
		
		for(String h : headings){
			this.box_profiles.add(new FilterPanel(this,h,""));
		}
		
		JButton btn_clear = new JButton("Clear Filters");
		MainWindow ref = this;
		btn_clear.addActionListener(new ActionListener(){  
			@Override
			public void actionPerformed(ActionEvent e){  
				match_obj.clearFilters();
				ref.showNearbyMatches();
			}
			
		});
		
		this.box_profiles.add(btn_clear);
	}
	
	public void logout(){
		this.auth_obj.logout();
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}
	
	public void createMatch(Profile p,String match_option){
		this.match_obj.createMatch(p,match_option);
	}
	
	public void submitEdits(){
		Profile p = this.match_obj.getUserProfile();
		for(Component c : box_profiles.getComponents()){
			if(c instanceof AttributePanel){
				AttributePanel attr = (AttributePanel) c;			
				String[] vals = attr.getAllValues();
				this.user_obj.editProfileSetting(vals[0],vals[1],false);
				this.user_obj.editProfileSetting(vals[0],vals[2],true);
			}
		}
		this.user_obj.saveAll();
		
	}
	
	public void addFilter(String heading,String value){
		this.match_obj.editFilterSetting(heading,value);
	}

	public void showProfile(Profile p,Boolean show_distance){
		this.profileMode();	

		if(p!=null){
			this.profile_panel.showProfile(p,show_distance);
		}
	}
	
	public void showUserProfile(){
		this.profileMode();	

		Profile p = this.match_obj.getUserProfile();

		if(p!=null){
			this.profile_panel.showProfile(p,false);
		}
	}
	
	public void showNearbyMatches(){
		this.rateMode();
		this.box_profiles.removeAll();	
		this.showFilters();
		ProfileList profiles;
		profiles = match_obj.getMatches(true);

		if(profiles!=null){
			this.rate_panel.setProfileList(profiles);
			this.rate_panel.showNextProfile();
		}
	}

	public void showMutualMatches(){
		this.profileMode();
		this.box_profiles.removeAll();				
		ProfileList profiles;
		profiles = match_obj.getMutualMatches();
		
		this.box_profiles.add(new JLabel("Mutual Matches"));
		
		if(profiles!=null){
			Profile p = profiles.getNextProfile();
			
			while(p != null){
				addProfileSmall(p);
				p = profiles.getNextProfile();
			}
		}
	}
	
	public void showMatchHistory(){
		this.profileMode();
		this.box_profiles.removeAll();
		ProfileList profiles;
		profiles = match_obj.getMatchHistory();
		
		this.box_profiles.add(new JLabel("Match History"));
		
		if(profiles!=null){
			Profile p = profiles.getNextProfile();
			
			while(p != null){
				addProfileSmall(p);
				p = profiles.getNextProfile();
			}
		}
	}
	
	private void profileMode(){
		this.rate_mode = false;
		this.rate_panel.setVisible(false);
		this.profile_panel.clear();
		if(!profile_mode){
			this.remove(rate_panel);
			this.add(profile_panel);
			this.profile_mode = true;
		}
		this.profile_panel.setVisible(true);
	}
	
	private void rateMode(){
		this.profile_mode = false;
		this.profile_panel.setVisible(false);
		if(!rate_mode){
			this.remove(profile_panel);
			this.add(rate_panel);
			this.rate_mode = true;
			this.box_profiles.removeAll();
		}
		this.rate_panel.setVisible(true);
	}
	
	public int getDistance(Profile p){
		int d = this.match_obj.getDistance(p);
		return d;
	}

}  