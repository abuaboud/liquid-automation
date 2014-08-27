package org.liquidbot.bot.ui.account;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JButton;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.script.api.util.Random;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Font;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import javax.swing.JTextPane;

public class AccountCreator extends JFrame {

	private static final long serialVersionUID = -2081171776222864787L;
	
	private JTextField txtName;
	private JTextField txtEmail;
	private JLabel lblDemoAcc;
	private KeyAdapter keyAdapter;
	private JLabel lblBlocked;
	private static int flag;

	private static boolean asUppercase;
	
	public AccountCreator() {
		setResizable(false);
		setTitle("Account Creator");
		setSize(450, 287);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		keyAdapter = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				updateText();
			}
		};
		
		txtName = new JTextField();
		txtName.addKeyListener(keyAdapter);
		txtName.setText("{C8}{n4}");
		txtName.setBounds(12, 12, 191, 19);
		panel.add(txtName);
		txtName.setColumns(10);
		
		JLabel label = new JLabel("@");
		label.setBounds(217, 14, 23, 15);
		panel.add(label);
		
		txtEmail = new JTextField();
		txtEmail.addKeyListener(keyAdapter);
		txtEmail.setText("gmail.com");
		txtEmail.setBounds(245, 12, 191, 19);
		panel.add(txtEmail);
		txtEmail.setColumns(10);
		
		JLabel lblSample = new JLabel("Sample:");
		lblSample.setBounds(22, 43, 70, 15);
		panel.add(lblSample);
		
		lblDemoAcc = new JLabel("");
		lblDemoAcc.setBounds(104, 43, 332, 15);
		panel.add(lblDemoAcc);
		
		JButton btnCreate = new JButton("Create");
		btnCreate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				lblBlocked.setForeground(Color.BLACK);
				lblBlocked.setText("Creating account...");
				String pass = randomPass();
				String name = parseStr(txtName.getText());
				String email = txtEmail.getText();
				if (createAccount(name, email, pass)) {
					lblBlocked.setForeground(Color.BLACK);
					lblBlocked.setText("Created account!");
					Configuration.getInstance().getAccountManager()
						.addAccount(name+"@"+email, pass);
					System.out.println(name+"@"+email+"   Pass: "+pass);
				} else {
					lblBlocked.setForeground(Color.RED);
					lblBlocked.setText("Blocked for creating too quickly, try again later!");
				}
			}
		});
		btnCreate.setBounds(12, 68, 424, 25);
		panel.add(btnCreate);
		
		lblBlocked = new JLabel("Waiting...");
		lblBlocked.setFont(new Font("Dialog", Font.BOLD, 12));
		lblBlocked.setBounds(12, 105, 424, 15);
		panel.add(lblBlocked);
		
		JTextPane txtpnTestTestTest = new JTextPane();
		txtpnTestTestTest.setFont(new Font("Dialog", Font.PLAIN, 11));
		txtpnTestTestTest.setEditable(false);
		txtpnTestTestTest.setText("Note:\tAccounts are automatically added to the Account Manager.\nKey:\tChar name shouldn't end up longer than 12 chars\n\n{#}\t[a-z 0-9]\n{c#}\t[a-z]\n{C#}\t[A-Z] for first char, then [a-z]\n{n#}\t[0-9]");
		txtpnTestTestTest.setBounds(12, 132, 424, 117);
		panel.add(txtpnTestTestTest);
		
		updateText();
	}
	
	private void updateText() {
		lblDemoAcc.setText(parseStr(txtName.getText()+"@"+txtEmail.getText()));
	}
	
	public static String parseStr(String s) {
		StringBuilder sb = new StringBuilder();
		flag = 0;
		for (int i = 0; i < s.length(); i++) {
			String c = s.substring(i, i+1);
			if (c.equals("{")) {
				asUppercase = false;
				StringBuilder sb2 = new StringBuilder();
				for (; i < s.length(); i++) {
					String cc = s.substring(i, i+1);
					if (isNumeric(cc)) {
						sb2.append(cc);
					} else if (cc.equalsIgnoreCase("c")) {
						flag = 2;
						if (cc.equals("C"))
							asUppercase = true;
					} else if (cc.equalsIgnoreCase("n")) {
						flag = 1;
					}
					
					if (cc.equals("}")) {
						String tmp = sb2.toString();
						sb.append(randomName(tmp.isEmpty() ? 1 : Integer.parseInt(tmp)));
						flag = 0;
						break;
					}
				}
			} else
				sb.append(c);
		}
		
		return sb.toString();
	}
	
	public static boolean createAccount(String name, String email, String password) {
		try {
			System.out.println("Sending account creation...");
			
			String urlParameters = "";
			String request = "https://secure.runescape.com/m=account-creation/g=oldscape/create_account_funnel.ws";
			URL url = new URL(request); 
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();           
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(true); 
	
			urlParameters += "onlyOneEmail=1&";
			urlParameters += "age="+Random.nextInt(18, 80)+"&";
			urlParameters += "displayname_present=true&";
			urlParameters += "displayname="+name+"&";
			urlParameters += "email1="+name+"@"+email+"&";
			urlParameters += "password1="+password+"&";
			urlParameters += "password2="+password+"&";
			urlParameters += "agree_email=on&";
			urlParameters += "agree_pp_and_tac=1&";
			urlParameters += "submit=Join Now";
			
			connection.setRequestMethod("POST"); 
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			connection.setRequestProperty("charset", "utf-8");
			connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
			connection.setUseCaches (false);
			
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
			wr.writeBytes(urlParameters);
			wr.flush();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			Boolean success = false;
			String failMessage = "The username already exists!";
			while ((inputLine = in.readLine()) != null) {
				if (inputLine.contains("Account Created"))
					success = true;
				if (inputLine.contains("You have been blocked from creating too many accounts"))
					failMessage = "Failed to create, please wait a few mins and try again.";
			}
			
			wr.close();
			in.close();
			connection.disconnect();
			System.out.println(success ? "Account created!" : failMessage);
			return success;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean isNumeric(String c) {
		try {
			Integer.parseInt(c);
			return true;
		} catch(NumberFormatException nfe) {  
			return false;  
		}  
	}
	
	private static final String[] passChars = new String[] {
		"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z",
		"A","B","C","D","E","F","G","H","I","J","K","L","M","O","N","P","Q","R","S","T","U","V","W","X","Y","Z",
		"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
	};
	
	private static final String[][] chars = new String[][] {
			new String[] {"b","c","d","f","g","h","j","k","l","m","n","p","r","s","t","v","w","x","y","z"},
			new String[] {"a","e","i","o","u"},
			new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"},
	};
	
	public static String randomName(int length) {
		StringBuilder sb = new StringBuilder();
		int lastIndex = -1;
		for (int i = 0; i < length; i++) {
			if (flag == 1) {
				lastIndex = 2;
				// Force numbers for this one
				sb.append(randChar(chars[2]));
			} else if (lastIndex == 0) {
				// Append a vowel or number
				if (flag == 2)
					lastIndex = 1;
				else
					lastIndex = 1 + (Random.nextInt(0, 99) % 2);
				sb.append(randChar(chars[lastIndex]));
				
			} else {
				// Append a constant
				lastIndex = 0;
				sb.append(randChar(chars[0]));
			}
			
			if (lastIndex < 2)
				asUppercase = false;
		}
		return sb.toString();
	}
	
	private static String randChar(String[] a) {
		String tmp = a[Random.nextInt(0, a.length - 1)];
		return asUppercase ? tmp.toUpperCase() : tmp;
	}

	public static String randomPass() {
		int length = Random.nextInt(11, 20);
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < length; i++) {
			sb.append(randChar(passChars));
		}
		return sb.toString();
	}
}
