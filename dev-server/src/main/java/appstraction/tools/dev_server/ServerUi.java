package appstraction.tools.dev_server;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ServerUi {

	private JFrame frame;
	private ServerListener listener;

	Preferences prefs = Preferences.userRoot();
	
	public interface ServerListener{
		void onStart();
		void onStop();
		String getUrl();
		String getDir();
	}

	boolean running = false;
	private CapturePane capturePane;
	public ServerUi(final ServerListener listener)
	{
		this.listener = listener;
	    capturePane = new CapturePane();
	    capturePane.setAlignmentY(0);
	    //System.setOut(new PrintStream(new StreamCapturer("", capturePane, System.out)));
	    //System.setErr(new PrintStream(new StreamCapturer("ERR", capturePane, System.out)));
	    System.out.println("Output test");

	    final JButton btn = new JButton("Start");
	    
	    btn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				 if(running)
				 {
					listener.onStop();
					btn.setText("Start");
					running = false;
				} else {
					listener.onStart();
					btn.setText("Stop");
					running = true;
				}
			}
		});
	    
	    
	    
	    
	    JPanel panel = new JPanel();
		panel.add(btn);
		
		JButton clearLogs = new JButton("Clear Logs");
		clearLogs.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				capturePane.clear();
			}
		});
		panel.add(clearLogs);
		
		JButton openBrowser = new JButton("Open Browser");
		openBrowser.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().browse(new URI( listener.getUrl() ));
				} catch (Exception e1) {
					log(e1.getMessage());
				}
			}
		});
		panel.add(openBrowser);
		JButton openDir = new JButton("Open Dir");
		openDir.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().open( new File(listener.getDir()) );
				} catch (Exception e1) {
					log(e1.getMessage());
				}
			}
		});
		panel.add(openDir);
		
		frame = new JFrame();
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setLayout(new BorderLayout(0,0));
	    frame.setTitle("AppServer");
	    frame.add(panel, BorderLayout.NORTH);
		frame.add(capturePane, BorderLayout.CENTER);
		
	    frame.setSize(prefs.getInt("w", 500), prefs.getInt("h", 400));
	    
	    int x = prefs.getInt("x", -1);
	    
	    if(x == -1){
	    	frame.setLocationRelativeTo(null);
	    }else{
	    	frame.setLocation(x, prefs.getInt("y", 0));
	    }
	    
	    frame.setVisible(true);
	    frame.addWindowListener(new WindowAdapter() {
	    	public void windowClosing(WindowEvent e) {
				listener.onStop();
			}
	    	
		});
	    frame.addComponentListener(new ComponentAdapter() {
		
	        public void componentResized(ComponentEvent e) {
	        	super.componentResized(e);
	        	prefs.putInt("w", e.getComponent().getWidth());
	        	prefs.putInt("h", e.getComponent().getHeight());
	        }
	        
	        @Override
	        public void componentMoved(ComponentEvent e) {
	        	super.componentMoved(e);
	        	prefs.putInt("x",e.getComponent().getX());
	        	prefs.putInt("y",e.getComponent().getY());
	        }
	    });

	}
	
	public void log(String msg){
		if(capturePane!=null){
			capturePane.appendText(msg);
		}
	}

	public static class CapturePane extends JPanel implements Consumer {

		private static final long serialVersionUID = 612968441503336523L;
		private JLabel output;

	    public CapturePane() {
	        setLayout(new BorderLayout());
	        output = new JLabel("<html>");
	        output.setVerticalAlignment(JLabel.TOP);
	        output.setVerticalTextPosition(JLabel.TOP);
	        add(new JScrollPane(output));
	    }

	    public void appendText(final String text) {
	        if (EventQueue.isDispatchThread()) {
	            output.setText(output.getText() + text + "<br>");
	        } else {

	            EventQueue.invokeLater(new Runnable() {
	                public void run() {
	                    appendText(text);
	                }
	            });

	        }
	    }    
	    
	    public void clear(){
	    	output.setText("");
	    }
	}

	public interface Consumer {        
	    public void appendText(String text);        
	}


	public static class StreamCapturer extends OutputStream {

	    private StringBuilder buffer;
	    private String prefix;
	    private Consumer consumer;
	    private PrintStream old;

	    public StreamCapturer(String prefix, Consumer consumer, PrintStream old) {
	        this.prefix = prefix;
	        buffer = new StringBuilder(128);
	        //buffer.append("[").append(prefix).append("] ");
	        this.old = old;
	        this.consumer = consumer;
	    }

	    @Override
	    public void write(int b) throws IOException {
	        char c = (char) b;
	        String value = Character.toString(c);
	        buffer.append(value);
	        if (value.equals("\n")) {
	            consumer.appendText(buffer.toString());
	            buffer.delete(0, buffer.length());
	            buffer.append("[").append(prefix).append("] ");
	        }
	        old.print(c);
	    }        
	}
	
}
