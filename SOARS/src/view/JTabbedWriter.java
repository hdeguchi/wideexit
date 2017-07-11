package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

/**
 * The JTabbedWriter class represents writer object assigned to console window in tabbed pane.
 * @author H. Tanuma / SOARS project
 */
public class JTabbedWriter extends JTextAreaWriter {

	public final JButton copyButton = new JButton("Copy Text");
	{
		copyButton.setToolTipText("Copy Text to Clipboard");
		copyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Clipboard.setString(textArea.getText().replaceAll("\r\n","\n")); // Dirty Hack for Excel
			}
		});
	}

	public final JButton saveButton = new JButton("Save Text...");
	{
		saveButton.setToolTipText("Save Text as File");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				if (chooser.showSaveDialog(panel) == JFileChooser.APPROVE_OPTION) {
					String fileName = chooser.getSelectedFile().getPath();
					try {
						PrintWriter out = new PrintWriter(new FileWriter(fileName));
						out.print(textArea.getText());
						out.flush();
					} catch (IOException ex) {
						PrintWriter out = new PrintWriter(new JTabbedWriter(tabbedPane, "error").setSelected());
						ex.printStackTrace(out);
						ex.printStackTrace();
					}
				}
			}
		});
	}

	public final JToolBar toolBar = new JToolBar();
	public final JPanel panel = new JPanel(new BorderLayout());
	{
		toolBar.add(copyButton);
		toolBar.addSeparator();
		toolBar.add(saveButton);
		panel.add(toolBar, BorderLayout.NORTH);
		panel.add(new JScrollPane(textArea));
	}

	public final JTabbedPane tabbedPane;
	String titleClosed;

	public JTabbedWriter(JTabbedPane tab, final String title, String titleClosed) {
		tabbedPane = tab;
		this.titleClosed = titleClosed;
		invokeLater(new Runnable() {
			public void run() {
				tabbedPane.addTab(title, panel);
			}
		});
	}
	public JTabbedWriter(JTabbedPane tab, String title) {
		this(tab, title, title);
	}
	public JTabbedWriter setSelected() {
		invokeLater(new Runnable() {
			public void run() {
				tabbedPane.setSelectedComponent(panel);
			}
		});
		return this;
	}
	public void close() {
		setTitle(titleClosed);
	}
	public void setTitle(final String title) {
		invokeLater(new Runnable() {
			public void run() {
				tabbedPane.setTitleAt(tabbedPane.indexOfComponent(panel), title);
			}
		});
	}
	public static void invokeLater(Runnable doRun) {
		if (SwingUtilities.isEventDispatchThread()) {
			doRun.run();
		}
		else {
			SwingUtilities.invokeLater(doRun);
		}
	}
}
