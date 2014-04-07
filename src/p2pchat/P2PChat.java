/*
 * P2PChat - Peer-to-Peer Chat Application
 *
 * Copyright (c) 2014 Ahmed Samy  <f.fallen45@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package p2pchat;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import java.io.IOException;

import java.util.Iterator;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.text.DefaultCaret;
import javax.swing.DefaultListModel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import netlib.PeerInfo;

public class P2PChat extends javax.swing.JFrame
{
	private Peer peer;
	private final DefaultListModel peerListModel, chatParticipantsModel;
	private String centralHost;
	private int centralPort;
	private boolean hasPublishedSelf;

	private static P2PChat instance;

	@SuppressWarnings("LeakingThisInConstructor")
	public P2PChat(String nick, String host, int port)
	{
		try {
			peer = new Peer(null, nick, host, port);
		} catch (IOException e) {
			e.printStackTrace();
		}

		peerListModel = new DefaultListModel();
		chatParticipantsModel = new DefaultListModel();

		instance = this;
		initComponents();

		DefaultCaret caret = (DefaultCaret) chatTextArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		chatTextArea.setLineWrap(true);
	}

	public static P2PChat get()
	{
		return instance;
	}

	public void setCentralInfo(String host, int port)
	{
		centralHost = host;
		centralPort = port;

		hasPublishedSelf = peer.publishSelf(host, port);
	}

	@SuppressWarnings("unchecked")
	private void initComponents() {
		jScrollPane1 = new javax.swing.JScrollPane();
		chatTextArea = new javax.swing.JTextArea();
		chatTextField = new javax.swing.JTextField();
		findPeersButton = new javax.swing.JButton();
		jScrollPane3 = new javax.swing.JScrollPane();
		chatParticipants = new javax.swing.JList();
		jScrollPane4 = new javax.swing.JScrollPane();
		peerList = new javax.swing.JList();
		sendButton = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		chatTextArea.setEditable(false);
		chatTextArea.setColumns(20);
		chatTextArea.setRows(5);
		jScrollPane1.setViewportView(chatTextArea);

		chatTextField.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(java.awt.event.KeyEvent evt) {
				if (evt.getKeyCode() == KeyEvent.VK_ENTER)
					sendTextMessage();
			}
		});

		findPeersButton.setText("Find Peers");
		findPeersButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				findPeersButtonActionPerformed(evt);
			}
		});

		chatParticipants.setModel(chatParticipantsModel);
		chatParticipants.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				chatParticipantsMouseClicked(evt);
			}
			public void mouseReleased(java.awt.event.MouseEvent evt) {
				if (evt.isPopupTrigger())
					chatParticipantsPopup.show(evt.getComponent(), evt.getX(), evt.getY());
			}
		});
		jScrollPane3.setViewportView(chatParticipants);

		peerList.setModel(peerListModel);
		peerList.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				peerListMouseClicked(evt);
			}
			public void mouseReleased(java.awt.event.MouseEvent evt) {
				if (evt.isPopupTrigger())
					peerListPopup.show(evt.getComponent(), evt.getX(), evt.getY());
			}
		});
		jScrollPane4.setViewportView(peerList);

		sendButton.setText("Send");
		sendButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendTextMessage();
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
			layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
					.addComponent(chatTextField)
					.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
					.addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
					.addComponent(sendButton, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addComponent(findPeersButton, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
					.addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
		);
		layout.setVerticalGroup(
			layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
			.addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
					.addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE)
					.addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
					.addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
					.addComponent(findPeersButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(chatTextField)
					.addComponent(sendButton)))
		);

		peerListPopup = new JPopupMenu("Action...");
		JMenuItem buttonDisconnect = new JMenuItem("Disconnect");
		buttonDisconnect.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String peerInfo = (String) peerList.getSelectedValue();
				if (peerInfo == null)
					return;

				String peerHost = peerInfo.substring(0, peerInfo.indexOf(":"));
				if (peer.disconnectFrom(peerHost))
					chatTextArea.append("<Network> Successfully disconnected from: " + peerHost + "\n");
			}
		});

		JMenuItem buttonConnect = new JMenuItem("Connect");
		buttonConnect.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String hostName = JOptionPane.showInputDialog("Hostname/IP:");
				String portName = JOptionPane.showInputDialog("Port:");

				int port;
				try {
					port = Integer.parseInt(portName);
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "Invalid port name " + portName + "!");
					return;
				}

				try {
					peer.connect(hostName, port);
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(null, "Unable to establish a connection to " + hostName + ":" + portName + "!");
				}

				peerListModel.addElement(hostName + ":" + port);
			}
		});
		peerListPopup.add(buttonDisconnect);
		peerListPopup.add(buttonConnect);

		chatParticipantsPopup = new JPopupMenu("Action...");
		JMenuItem buttonKick = new JMenuItem("Kick");
		buttonKick.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				String nickName = (String) chatParticipants.getSelectedValue();
				if (nickName != null)
					peer.kick(nickName);
			}
		});
		chatParticipantsPopup.add(buttonKick);

		pack();
	}

	private void sendTextMessage()
	{
		String message = chatTextField.getText();
		if ("".equals(message))
			return;

		if (message.charAt(0) == '/') {
			String[] splitted = message.split(" ");
			if (splitted.length > 1) {
				if (splitted[0].equals("/nick")) {
					// A Nickname can contain spaces
					String newNick = new String();
					for (int i = 1; i < splitted.length; ++i)
						newNick += splitted[i] + " ";

					peer.setName(newNick);
					chatTextField.setText("");
					chatTextArea.append("You changed your name to " + newNick + ".");
				} else if (splitted[0].equals("/kick")) {
					// A Nickname can contain spaces
					String nick = new String();
					for (int i = 1; i < splitted.length; ++i)
						nick += splitted[i] + " ";

					if (peerListModel.contains(nick))
						peer.kick(nick);
				} else
					chatTextArea.append("Invalid command.");
			} else if (splitted[0].equals("/help")) {
				chatTextArea.append("Commands available:\n" +
					"/nick <new nickname> (Can contain spaces)\n" +
					"/kick <nickname> (Can contain spaces)\n"
				);
			}

			return;
		}

		peer.sendMessage(message);
		chatTextField.setText("");
		chatTextArea.append("<" + peer.peerName + "> " + message + "\n");
	}

	private void findPeersButtonActionPerformed(java.awt.event.ActionEvent evt)
	{
		if (!hasPublishedSelf)
			hasPublishedSelf = peer.publishSelf(centralHost, centralPort);

		List peers = peer.discoverPeers(centralHost, centralPort);
		if (peers == null) {
			chatTextArea.append("No peers were found.\n");
			return;
		}
		peerListModel.clear();

		Iterator it = peers.iterator();
		while (it.hasNext()) {
			PeerInfo info = (PeerInfo) it.next();
			peerListModel.addElement(info.host + ":" + info.port);
		}
	}

	private void peerListMouseClicked(java.awt.event.MouseEvent evt)
	{
		if (SwingUtilities.isLeftMouseButton(evt)) {
			String peerInfo = (String) peerList.getSelectedValue();
			if (peerInfo == null)
				return;

			int sep = peerInfo.indexOf(":");
			String peerHost = peerInfo.substring(0, sep);
			int peerPort = Integer.parseInt(peerInfo.substring(sep + 1, peerInfo.length()));

			try {
				peer.connect(peerHost, peerPort);
			} catch (IOException e) {
				chatTextArea.append("Unable to connect to: " + peerInfo + "\n");
				e.printStackTrace();
			}
		} else if (evt.isPopupTrigger())
			peerListPopup.show(evt.getComponent(), evt.getX(), evt.getY());
	}

	private void chatParticipantsMouseClicked(java.awt.event.MouseEvent evt)
	{
		if (evt.isPopupTrigger())
			chatParticipantsPopup.show(evt.getComponent(), evt.getX(), evt.getY());
	}

	public void appendText(String sender, String text)
	{
		if (sender == null)
			sender = "unknown";

		chatTextArea.append("<" + sender + "> " + text + "\n");
	}

	public void peerConnected(Peer newPeer)
	{
		if (newPeer.peerName == null)
			newPeer.peerName = "unnamed";

		chatTextArea.append(newPeer.peerName + " has connected.\n");
		chatParticipantsModel.addElement(newPeer.peerName);
	}

	public void peerDisconnected(Peer node, boolean timeout)
	{
		int idx = chatParticipantsModel.indexOf(node.peerName);
		if (idx != -1)
			chatParticipantsModel.remove(idx);

		if (!timeout)
			chatTextArea.append(node.peerName + " has disconnected.\n");
		else
			chatTextArea.append(node.peerName + " has timed out.\n");
	}

	public void peerNameChanged(Peer node, String oldName, String newName)
	{
		int index = chatParticipantsModel.indexOf(oldName);
		if (index != -1) {
			if (peer.isChild(node)) {
				while (chatParticipantsModel.contains(newName) || newName.equals(peer.peerName)) {
					newName += "_";
					node.peerName = newName;
					peer.sendNameChangeRequest(node);
				}
			}

			chatParticipantsModel.setElementAt(newName, index);
			chatTextArea.append(oldName + " has changed name to " + newName + "\n");
		} else {
			System.out.println("Unable to find peer name " + oldName + " (" + newName + ")");
			chatParticipantsModel.addElement(newName);
		}
	}

	public void peerAcked(String from, String hostName, int port)
	{
		if (!peerListModel.contains(hostName + ":" + port)) {
			chatTextArea.append("New Peer Acked from " + from + ": " + hostName + ":" + port + "\n");
			peerListModel.addElement(hostName + ":" + port);
		}
	}

	public void centralConnectionFailed()
	{
		chatTextArea.append("Unable to establish a connection to the central server.\n");
	}

	private javax.swing.JList chatParticipants;
	private javax.swing.JTextArea chatTextArea;
	private javax.swing.JTextField chatTextField;
	private javax.swing.JButton findPeersButton;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane3;
	private javax.swing.JScrollPane jScrollPane4;
	private javax.swing.JList peerList;
	private javax.swing.JButton sendButton;

	private JPopupMenu chatParticipantsPopup;
	private JPopupMenu peerListPopup;
}
