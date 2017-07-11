package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTabbedPane;

/**
 * The JTabbedConsole class represents console window in tabbed pane.
 * @author H. Tanuma / SOARS project
 */
public class JTabbedConsole extends JTabbedWriter {

	volatile Thread thread = null;

	public final JButton interruptButton = new JButton("Interrupt") {
		private static final long serialVersionUID = 5696147088461171596L;
		public void removeNotify() {
			super.removeNotify();
			interrupt();
		}
	};
	{
		interruptButton.setToolTipText("Interrupt Running Thread");
		interruptButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				interrupt();
			}
		});
		toolBar.addSeparator();
		toolBar.addSeparator();
		toolBar.add(interruptButton);
	}

	public JTabbedConsole(JTabbedPane tab, final String title, String titleClosed) {
		super(tab, title, titleClosed);
	}
	public Thread thread(Runnable run) {
		thread = new Thread(run);
		return thread;
	}
	public void interrupt() {
		Thread t = thread;
		if (t != null) {
			t.interrupt();
		}
	}
}
