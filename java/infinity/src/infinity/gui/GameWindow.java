package infinity.gui;

/**
 * This test shows the different buffer capabilities for each
 * GraphicsConfiguration on each GraphicsDevice.
 */
import infinity.Infinity;
import infinity.gameengine.Game;
import infinity.networking.GameServer;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * This class wraps a graphics configuration so that it can be displayed nicely
 * in components.
 */
class GCWrapper {
	private GraphicsConfiguration gc;
	private int index;

	public GCWrapper(GraphicsConfiguration gc, int index) {
		this.gc = gc;
		this.index = index;
	}

	public GraphicsConfiguration getGC() {
		return gc;
	}

	public String toString() {
		return gc.toString();
	}
}


class ClientStartClick implements ActionListener {
	private GameWindow window;

	ClientStartClick(GameWindow window) {
		this.window = window;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (window.isClientStarted)
			return;
		// new Thread(new GameServer()).start();
		
		if(window.clientConnectToTextField.getText().equals("localhost") && !window.isServerStarted)
		{
			new ServerStartClick(window).actionPerformed(e);
			window.isServerStarted=true;//new S
		}
		
		new Thread(new Infinity(window.clientConnectToTextField.getText(), Integer.parseInt(window.clientPortTextField
				.getText()), Integer.parseInt(window.numBotsTextField.getText()))).start();
		// new Infinity().run();
		window.clientStatus.setText("Started");
		window.isClientStarted = true;
		window.dispose();
	}
}


class ServerStartClick implements ActionListener {
	private final GameWindow window;

	ServerStartClick(GameWindow window) {
		this.window = window;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (window.isServerStarted)
			return;
		new Thread(new GameServer(Integer.parseInt(window.serverPortTextField.getText()))).start();
		 window.serverStatus.setText("Started");
		window.isServerStarted = true;
	}
}

/**
 * Main frame class.
 */
public class GameWindow extends JFrame implements ItemListener {

	


	private static final long serialVersionUID = 1L;
	private JComboBox gcSelection = new JComboBox();
	private JCheckBox imageAccelerated = new JCheckBox("Accelerated", false);
	private JCheckBox imageTrueVolatile = new JCheckBox("Volatile", false);
	private JCheckBox flipping = new JCheckBox("Flipping", false);
	private JLabel flippingMethod = new JLabel("");
	private JCheckBox fullScreen = new JCheckBox("Full Screen Only", false);
	private JCheckBox multiBuffer = new JCheckBox("Multi-Buffering", false);
	private JCheckBox fbAccelerated = new JCheckBox("Accelerated", false);
	private JCheckBox fbTrueVolatile = new JCheckBox("Volatile", false);
	private JCheckBox bbAccelerated = new JCheckBox("Accelerated", false);
	private JCheckBox bbTrueVolatile = new JCheckBox("Volatile", false);
	
	JTextField serverPortTextField;
	JTextField clientConnectToTextField; 
	JTextField clientPortTextField;
	JTextField numBotsTextField;
	JLabel clientStatus;
	JLabel serverStatus;
	
	public GameWindow(GraphicsDevice dev) {
		super(dev.getDefaultConfiguration());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		initComponents(getContentPane());
		GraphicsConfiguration[] gcs = dev.getConfigurations();
		for (int i = 0; i < gcs.length; i++) {
			gcSelection.addItem(new GCWrapper(gcs[i], i));
		}
		gcSelection.addItemListener(this);
		gcChanged();
	}

	/**
	 * Creates and lays out components in the container. See the comments below
	 * for an organizational overview by panel.
	 */
	private void initComponents(Container frame) {
		// +=c=====================================================+
		// ++=gcPanel==============================================+
		// ++ [gcSelection] +
		// ++=capsPanel============================================+
		// +++=imageCapsPanel======================================+
		// +++ [imageAccelerated] +
		// +++ [imageTrueVolatile] +
		// +++=bufferCapsPanel=====================================+
		// ++++=bufferAccessCapsPanel==============================+
		// +++++=flippingPanel=====================================+
		// +++++ [flipping] +
		// +++++=fsPanel===========================================+
		// +++++ [indentPanel][fullScreen] +
		// +++++=mbPanel===========================================+
		// +++++ [indentPanel][multiBuffer] +
		// ++++=buffersPanel=======================================+
		// +++++=fbPanel===============+=bbPanel===================+
		// +++++ + +
		// +++++ [fbAccelerated] + [bbAccelerated] +
		// +++++ + +
		// +++++ [fbTrueVolatile] + [bbTrueVolatile] +
		// +++++ + +
		// +=======================================================+

		JTabbedPane tab = new JTabbedPane();
		JPanel gamePanel = new JPanel(new GridLayout(2, 1));
		JPanel capabilitesPanel = new JPanel();
		frame.add(tab, BorderLayout.CENTER);
		tab.add("Game", gamePanel);
		tab.add("Graphics", capabilitesPanel);

		capabilitesPanel.setLayout(new BorderLayout());
		// Graphics Config
		JPanel gcPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		capabilitesPanel.add(gcPanel, BorderLayout.NORTH);
		gcSelection.setPreferredSize(new Dimension(600, 30));
		gcPanel.add(gcSelection);
		// Capabilities
		JPanel capsPanel = new JPanel(new BorderLayout());
		capabilitesPanel.add(capsPanel, BorderLayout.CENTER);
		// Image Capabilities
		JPanel imageCapsPanel = new JPanel(new GridLayout(2, 1));
		capsPanel.add(imageCapsPanel, BorderLayout.NORTH);
		imageCapsPanel.setBorder(BorderFactory.createTitledBorder("Image Capabilities"));
		imageAccelerated.setEnabled(false);
		imageCapsPanel.add(imageAccelerated);
		imageTrueVolatile.setEnabled(false);
		imageCapsPanel.add(imageTrueVolatile);
		// Buffer Capabilities
		JPanel bufferCapsPanel = new JPanel(new BorderLayout());
		capsPanel.add(bufferCapsPanel, BorderLayout.CENTER);
		bufferCapsPanel.setBorder(BorderFactory.createTitledBorder("Buffer Capabilities"));
		// Buffer Access
		JPanel bufferAccessCapsPanel = new JPanel(new GridLayout(3, 1));
		bufferAccessCapsPanel.setPreferredSize(new Dimension(300, 88));
		bufferCapsPanel.add(bufferAccessCapsPanel, BorderLayout.NORTH);
		// Flipping
		JPanel flippingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		bufferAccessCapsPanel.add(flippingPanel);
		flippingPanel.add(flipping);
		flipping.setEnabled(false);
		flippingPanel.add(flippingMethod);
		// Full-screen
		JPanel fsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		bufferAccessCapsPanel.add(fsPanel);
		JPanel indentPanel = new JPanel();
		indentPanel.setPreferredSize(new Dimension(30, 30));
		fsPanel.add(indentPanel);
		fsPanel.add(fullScreen);
		fullScreen.setEnabled(false);
		// Multi-buffering
		JPanel mbPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		bufferAccessCapsPanel.add(mbPanel);
		indentPanel = new JPanel();
		indentPanel.setPreferredSize(new Dimension(30, 30));
		mbPanel.add(indentPanel);
		mbPanel.add(multiBuffer);
		multiBuffer.setEnabled(false);
		// Front and Back Buffer Capabilities
		JPanel buffersPanel = new JPanel(new GridLayout(1, 2));
		bufferCapsPanel.add(buffersPanel, BorderLayout.CENTER);
		// Front Buffer
		JPanel fbPanel = new JPanel(new GridLayout(2, 1));
		fbPanel.setBorder(BorderFactory.createTitledBorder("Front Buffer"));
		buffersPanel.add(fbPanel);
		fbPanel.add(fbAccelerated);
		fbAccelerated.setEnabled(false);
		fbPanel.add(fbTrueVolatile);
		fbTrueVolatile.setEnabled(false);
		// Back Buffer
		JPanel bbPanel = new JPanel(new GridLayout(2, 1));
		bbPanel.setPreferredSize(new Dimension(250, 80));
		bbPanel.setBorder(BorderFactory.createTitledBorder("Back and Intermediate Buffers"));
		buffersPanel.add(bbPanel);
		bbPanel.add(bbAccelerated);
		bbAccelerated.setEnabled(false);
		bbPanel.add(bbTrueVolatile);
		bbTrueVolatile.setEnabled(false);

		// GameServer
		JPanel serverPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		serverPanel.setBorder(BorderFactory.createTitledBorder("Server"));
		serverPanel.setPreferredSize(new Dimension(600, 80));
		gamePanel.add(serverPanel);

		JButton serverStartButton = new JButton("Start");
		serverPanel.add(serverStartButton);
		// JButton serverStopButton = new JButton("Stop");
		// serverPanel.add(serverStopButton);
		serverPanel.add(new JLabel("Port"));
		serverPortTextField = new JTextField("5000");
		serverPortTextField.setPreferredSize(new Dimension(60, 24));
		serverPanel.add(serverPortTextField);
		serverPanel.add(new JLabel("Status:"));
		serverStatus = new JLabel("Stopped");
		serverPanel.add(serverStatus);

		JPanel clientPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		clientPanel.setBorder(BorderFactory.createTitledBorder("Client"));
		clientPanel.setPreferredSize(new Dimension(600, 100));
		gamePanel.add(clientPanel, BorderLayout.LINE_START);

		JButton clientStartButton = new JButton("Start");

		clientPanel.add(clientStartButton);
		// JButton clientStopButton = new JButton("Stop");
		// clientPanel.add(clientStopButton);

		clientPanel.add(new JLabel("Bots"));
		numBotsTextField = new JTextField("1");
		numBotsTextField.setPreferredSize(new Dimension(30, 24));
		clientPanel.add(numBotsTextField);

		clientPanel.add(new JLabel("Connect to"));
		clientConnectToTextField = new JTextField("localhost");
		clientConnectToTextField.setPreferredSize(new Dimension(90, 24));
		clientPanel.add(clientConnectToTextField);
		clientPanel.add(new JLabel("Port"));
		clientPortTextField = new JTextField("5000");
		clientPortTextField.setPreferredSize(new Dimension(60, 24));
		clientPanel.add(clientPortTextField);
		clientPanel.add(new JLabel("Status:"));
		clientStatus = new JLabel("Stopped");
		clientPanel.add(clientStatus);
		// Image Capabilities
		// JPanel imageCapsPanel = new JPanel(new GridLayout(2, 1));
		// serverPanel.add(imageCapsPanel, BorderLayout.NORTH);

		serverStartButton.addActionListener(new ServerStartClick(this));

		clientStartButton.addActionListener(new ClientStartClick(this));

	}

	boolean isClientStarted;
	boolean isServerStarted;

	public void itemStateChanged(ItemEvent ev) {
		gcChanged();
	}

	private void gcChanged() {
		GCWrapper wrap = (GCWrapper) gcSelection.getSelectedItem();
		// assert wrap != null;
		GraphicsConfiguration gc = wrap.getGC();
		// assert gc != null;
		// Image Caps
		ImageCapabilities imageCaps = gc.getImageCapabilities();
		imageAccelerated.setSelected(imageCaps.isAccelerated());
		imageTrueVolatile.setSelected(imageCaps.isTrueVolatile());
		// Buffer Caps
		BufferCapabilities bufferCaps = gc.getBufferCapabilities();
		flipping.setSelected(bufferCaps.isPageFlipping());
		flippingMethod.setText(getFlipText(bufferCaps.getFlipContents()));
		fullScreen.setSelected(bufferCaps.isFullScreenRequired());
		multiBuffer.setSelected(bufferCaps.isMultiBufferAvailable());
		// Front buffer caps
		imageCaps = bufferCaps.getFrontBufferCapabilities();
		fbAccelerated.setSelected(imageCaps.isAccelerated());
		fbTrueVolatile.setSelected(imageCaps.isTrueVolatile());
		imageCaps = bufferCaps.getFrontBufferCapabilities();
		// Back buffer caps
		imageCaps = bufferCaps.getBackBufferCapabilities();
		bbAccelerated.setSelected(imageCaps.isAccelerated());
		bbTrueVolatile.setSelected(imageCaps.isTrueVolatile());
	}

	private static String getFlipText(BufferCapabilities.FlipContents flip) {
		if (flip == null) {
			return "";
		} else if (flip == BufferCapabilities.FlipContents.UNDEFINED) {
			return "Method Unspecified";
		} else if (flip == BufferCapabilities.FlipContents.BACKGROUND) {
			return "Cleared to Background";
		} else if (flip == BufferCapabilities.FlipContents.PRIOR) {
			return "Previous Front Buffer";
		} else { // if (flip == BufferCapabilities.FlipContents.COPIED)
			return "Copied";
		}
	}

	public static void display() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] devices = ge.getScreenDevices();
		//for (int i = 0; i < devices.length; i++) {
			GameWindow tst = new GameWindow(devices[0]);
			tst.pack();
			tst.setVisible(true);
		//}
	}

	public static void main(String[] args) {
		GameWindow.display();
	}

}
