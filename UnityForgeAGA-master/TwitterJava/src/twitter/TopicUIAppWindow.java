package twitter;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.net.URLCodec;

import twitter4j.TwitterException;

public class TopicUIAppWindow {

	private JFrame frmFlameWarTopic;
	ArrayList<String> Topics;
	private JTextField textField;
	private JList list;
	String SelectedTopic = "";
	boolean isListEntrySelected = false;
	JButton btnPlayGame;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TopicUIAppWindow window = new TopicUIAppWindow();
					window.frmFlameWarTopic.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void createTopicUI()
	{
		String[] args = new String[1];
		main(args);
	}

	/**
	 * Create the application.
	 * @throws TwitterException 
	 * @throws DecoderException 
	 */
	public TopicUIAppWindow() throws TwitterException, DecoderException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws TwitterException 
	 * @throws DecoderException 
	 */
	private void initialize() throws TwitterException, DecoderException {
		frmFlameWarTopic = new JFrame();
		frmFlameWarTopic.setTitle("Flame War Topic Selector");
		frmFlameWarTopic.setBounds(100, 100, 450, 400);
		frmFlameWarTopic.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmFlameWarTopic.getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(6, 34, 438, 178);
		frmFlameWarTopic.getContentPane().add(scrollPane);
		
		list = new JList();
		TopicListSelectionListener lstHandler = new TopicListSelectionListener();
		list.addListSelectionListener(lstHandler);
		scrollPane.setViewportView(list);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JLabel lblTopics = new JLabel("Select Topics");
		lblTopics.setBounds(200, 6, 61, 16);
		frmFlameWarTopic.getContentPane().add(lblTopics);
		btnPlayGame = new JButton("Play Game!");
		PlayGameButtonListener btnHandler = new PlayGameButtonListener();
		
		btnPlayGame.addActionListener(btnHandler);
		btnPlayGame.setBounds(166, 343, 117, 29);
		frmFlameWarTopic.getContentPane().add(btnPlayGame);
		
		textField = new JTextField();
		textField.setText("Enter Custom Topic...");
		textField.setToolTipText("Enter Custom Topic");
		textField.setBounds(6, 224, 438, 28);
		frmFlameWarTopic.getContentPane().add(textField);
		textField.setColumns(10);
		textFieldListener textFieldChangeListener = new textFieldListener();
		textField.getDocument().addDocumentListener(textFieldChangeListener);
		
		JLabel lblEnteringYourOwn = new JLabel(" Entering your own topic may result in insufficient tweets to ");
		lblEnteringYourOwn.setBounds(6, 263, 438, 29);
		frmFlameWarTopic.getContentPane().add(lblEnteringYourOwn);
		
		JLabel lblTheGameWorld = new JLabel(" populate the game world. It is recommended that a trending topic is ");
		lblTheGameWorld.setBounds(6, 293, 438, 29);
		frmFlameWarTopic.getContentPane().add(lblTheGameWorld);
		
		JLabel lblInstead = new JLabel(" selected instead.");
		lblInstead.setBounds(6, 322, 438, 29);
		frmFlameWarTopic.getContentPane().add(lblInstead);
		
		TwitterMiner tm = new TwitterMiner();
		Topics = tm.getTrends();
		
		Topics = sanitiseTopics(Topics);
		
		Object[] TopicList = Topics.toArray();
		
		list.setListData(TopicList);
	}
	
	public ArrayList<String> sanitiseTopics(ArrayList<String> Topics) throws DecoderException
	{
		ArrayList<String> Results = new ArrayList<String>();
		URLCodec codec = new URLCodec();
		
		for(String topic : Topics)
		{
			Results.add(codec.decode(topic));
		}
		
		return Results;
	}
	
	class PlayGameButtonListener implements ActionListener
	{
	
		@Override
		public void actionPerformed(ActionEvent e) {
			String selectedText = "";
			if(isListEntrySelected)
			{
				selectedText = (String) list.getSelectedValue();
				System.out.println(selectedText);
			}
			else
			{
				selectedText = textField.getText();
				System.out.println(selectedText);
			}
			
			if(selectedText != null && selectedText != "")
			{
				//btnPlayGame.setEnabled(false);
				
				try {
					MainTweet.runTweetsByString(selectedText);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	class TopicListSelectionListener implements ListSelectionListener, ActionListener
	{
		
		@Override
		public void valueChanged(ListSelectionEvent e) {
			isListEntrySelected = true;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			isListEntrySelected = true;
		}
	}
	
	class textFieldListener implements DocumentListener
	{

		@Override
		public void changedUpdate(DocumentEvent e) {
			isListEntrySelected = false;
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			isListEntrySelected = false;
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			isListEntrySelected = false;
		}
		
	}
}