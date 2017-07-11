package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

/**
 * The JTabbedFrame class represents frame window with tabbed pane.
 * @author H. Tanuma / SOARS project
 */
public class JTabbedFrame extends JTabbedPane {

	private static final long serialVersionUID = -8542330325349571994L;
	public final JFrame frame;
	public final JToolBar toolBar = new JToolBar();

	public final JButton leftButton = new JButton("<<");
	{
		leftButton.setToolTipText("Move Tab Left");
		leftButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int i = getSelectedIndex();
				if (i < 1) return;
				Component component = getComponentAt(i);
				String title = getTitleAt(i);
				removeTabAt(i);
				insertTab(title, null, component, null, i - 1);
				setSelectedIndex(i - 1);
			}
		});
	}

	public final JButton rightButton = new JButton(">>");
	{
		rightButton.setToolTipText("Move Tab Right");
		rightButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int i = getSelectedIndex();
				if (i < 0 || i == getTabCount() - 1) return;
				Component component = getComponentAt(i);
				String title = getTitleAt(i);
				removeTabAt(i);
				insertTab(title, null, component, null, i + 1);
				setSelectedIndex(i + 1);
			}
		});
	}

	public final JButton fitButton = new JButton("Fit");
	{
		fitButton.setToolTipText("Resize Window to Fit Contents");
		toolBar.add(fitButton);
		fitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.pack();
			}
		});
	}

	public final JButton closeButton = new JButton("X");
	{
		closeButton.setToolTipText("Close Tab");
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int i = getSelectedIndex();
				if (i >= 0) {
					Component c = getComponentAt(i);
					if (!(c instanceof Confirmable) || ((Confirmable) c).confirm(null) == JOptionPane.YES_OPTION) {
						removeTabAt(i);
					}
				} 
			}
		});
	}

	public JTabbedFrame(String title) {
		toolBar.add(leftButton);
		toolBar.add(rightButton);
		toolBar.addSeparator();
		toolBar.add(fitButton);
		toolBar.addSeparator();
		toolBar.add(closeButton);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(toolBar, BorderLayout.NORTH);
		panel.add(this);
		frame = new JFrame(title);
		frame.getContentPane().add(panel);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
}
