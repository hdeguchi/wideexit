package view;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import main.MainGUI;

import script.ScriptParser;
import script.ScriptReader;
import env.Environment;

/**
 * The JMainFrame class represents main frame window of SOARS ModelBuilder.
 * @author H. Tanuma / SOARS project
 */
public class JMainFrame extends JTabbedFrame {

	private static final long serialVersionUID = -1068758918028966406L;

	public final JButton winButton = new JButton("New Window");
	{
		winButton.setToolTipText("Open New Frame Window");
		winButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new JMainFrame(parserClass).frame.setLocationRelativeTo(JMainFrame.this);
			}
		});
	}

	public final JButton newButton = new JButton("New Script");
	{
		newButton.setToolTipText("Create New Script");
		newButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JScriptTable table = new JScriptTable(JMainFrame.this);
					table.read(new BufferedReader(new StringReader("")));
					addTab(JScriptTable.untitled, table.panel);
					setSelectedComponent(table.panel);
					packOnce();
					table.requestFocus();
				}
				catch (Exception ex) {
					PrintWriter out = new PrintWriter(new JTabbedWriter(JMainFrame.this, "error").setSelected());
					ex.printStackTrace(out);
					ex.printStackTrace();
					packOnce();
				}
			}
		});
	}

	public final JButton pasteButton = new JButton("Paste Script");
	{
		pasteButton.setToolTipText("Paste Script from Clipboard");
		pasteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String paste = "";
				try {
					paste = Clipboard.getString();
				}
				catch (Exception ex) {
				}
				try {
					JScriptTable table = new JScriptTable(JMainFrame.this);
					table.read(new BufferedReader(new StringReader(paste)));
					addTab(JScriptTable.untitled, table.panel);
					setSelectedComponent(table.panel);
					packOnce();
					table.requestFocus();
				}
				catch (Exception ex) {
					PrintWriter out = new PrintWriter(new JTabbedWriter(JMainFrame.this, "error").setSelected());
					ex.printStackTrace(out);
					ex.printStackTrace();
					packOnce();
				}
			}
		});
	}

	public final JButton editButton = new JButton("Edit Script...");
	{
		editButton.setToolTipText("Edit Script File");
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(".");
				if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
					editScript(chooser.getSelectedFile().getPath());
				}
			}
		});
		new DropTarget(editButton, new DropTargetAdapter() {
			public void dragEnter(DropTargetDragEvent dtde) {
				dtde.acceptDrag(DnDConstants.ACTION_COPY);
			}
			public void drop(DropTargetDropEvent dtde) {
				Transferable transferable = dtde.getTransferable();
				if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
					dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
					try {
						Iterator<?> it = ((List<?>) transferable.getTransferData(DataFlavor.javaFileListFlavor)).iterator();
						while (it.hasNext()) {
							editScript(((File) it.next()).getPath());
						}
					} catch (UnsupportedFlavorException e) {
					} catch (IOException e) {
					}
				}
			}
		});
	}

	public JButton runButton = new JButton("Run Script...");
	{
		runButton.setToolTipText("Run Script File");
		runButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(".");
				if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
					try {
						runScript(new ScriptReader(chooser.getSelectedFile().getPath()), false);
					} catch (FileNotFoundException ex) {
						PrintWriter out = new PrintWriter(new JTabbedWriter(JMainFrame.this, "error").setSelected());
						ex.printStackTrace(out);
						ex.printStackTrace();
						packOnce();
					}
				}
			}
		});
		new DropTarget(runButton, new DropTargetAdapter() {
			public void dragEnter(DropTargetDragEvent dtde) {
				dtde.acceptDrag(DnDConstants.ACTION_COPY);
			}
			public void drop(DropTargetDropEvent dtde) {
				Transferable transferable = dtde.getTransferable();
				if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
					dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
					try {
						Iterator<?> it = ((List<?>) transferable.getTransferData(DataFlavor.javaFileListFlavor)).iterator();
						while (it.hasNext()) {
							runScript(new ScriptReader(((File) it.next()).getPath()), false);
						}
					} catch (UnsupportedFlavorException e) {
					} catch (IOException e) {
					}
				}
			}
		});
	}

	public JButton enqueButton = new JButton("Enque Script...");
	{
		enqueButton.setToolTipText("Enqueue Script File");
		enqueButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(".");
				if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
					try {
						runScript(new ScriptReader(chooser.getSelectedFile().getPath()), true);
					} catch (FileNotFoundException ex) {
						PrintWriter out = new PrintWriter(new JTabbedWriter(JMainFrame.this, "error").setSelected());
						ex.printStackTrace(out);
						ex.printStackTrace();
						packOnce();
					}
				}
			}
		});
		new DropTarget(enqueButton, new DropTargetAdapter() {
			public void dragEnter(DropTargetDragEvent dtde) {
				dtde.acceptDrag(DnDConstants.ACTION_COPY);
			}
			public void drop(DropTargetDropEvent dtde) {
				Transferable transferable = dtde.getTransferable();
				if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
					dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
					try {
						Iterator<?> it = ((List<?>) transferable.getTransferData(DataFlavor.javaFileListFlavor)).iterator();
						while (it.hasNext()) {
							runScript(new ScriptReader(((File) it.next()).getPath()), true);
						}
					} catch (UnsupportedFlavorException e) {
					} catch (IOException e) {
					}
				}
			}
		});
	}

	public JButton runOneButton = new JButton("Run One");
	{
		runOneButton.setToolTipText("Run One of Enqueued Scripts");
		runOneButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				synchronized (JMainFrame.class) {
					JMainFrame.class.notify();
				}
			}
		});
	}

	public JButton runAllButton = new JButton("Run All");
	{
		runAllButton.setToolTipText("Run All Enqueued Scripts");
		runAllButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				synchronized (JMainFrame.class) {
					JMainFrame.class.notifyAll();
				}
			}
		});
	}

	public JButton aboutButton = new JButton("About SOARS...");
	{
		aboutButton.setToolTipText("Show Version Information");
		aboutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(frame,
					"SOARS (Spot Oriented Agent Role Simulator)"
					+ "\n-  Model Builder (" + Environment.getVersion()
					+ ")\n\nCopyright (c) 2003-2008 SOARS Project.",
					"Version Information", JOptionPane.INFORMATION_MESSAGE
				);
			}
		});
	}

	static ThreadLocal<JMainFrame> current = new ThreadLocal<JMainFrame>();

	public static JMainFrame getCurrent() {
		return current.get();
	}
	public void setCurrent() {
		current.set(this);
	}

	protected Class<?> parserClass;

	public JMainFrame(Class<?> parserClass) {
		this(parserClass, "SOARS Model Builder" + (parserClass.equals(MainGUI.class) ? "" : "  " + parserClass.getName()));
	}
	public JMainFrame(Class<?> parserClass, String title) {
		super(title);
		toolBar.addSeparator();
		toolBar.addSeparator();
		toolBar.add(winButton);
		toolBar.addSeparator();
		toolBar.add(newButton);
		toolBar.addSeparator();
		toolBar.add(pasteButton);
		toolBar.addSeparator();
		toolBar.add(editButton);
		toolBar.addSeparator();
		toolBar.add(runButton);
		toolBar.add(enqueButton);
		toolBar.addSeparator();
		toolBar.add(runOneButton);
		toolBar.add(runAllButton);
		toolBar.addSeparator();
		toolBar.addSeparator();
		toolBar.add(aboutButton);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (getTabCount() == 0 || JOptionPane.showConfirmDialog(frame, "Are you sure to close this window?", "SOARS", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					frame.setVisible(false);
					frame.dispose();
				}
			}
		});
		this.parserClass = parserClass;
		frame.setVisible(true);
	}

	public void editScript(String fileName) {
		try {
			JScriptTable table = new JScriptTable(this);
			table.read(new BufferedReader(new FileReader(fileName)));
			addTab(fileName, table.panel);
			setSelectedComponent(table.panel);
			packOnce();
			table.requestFocus();
		}
		catch (Exception ex) {
			PrintWriter out = new PrintWriter(new JTabbedWriter(this, "error").setSelected());
			ex.printStackTrace(out);
			ex.printStackTrace();
			packOnce();
		}
	}
	public void runScript(final ScriptReader script, final boolean enqueue) {
		final JTabbedConsole console = new JTabbedConsole(this, enqueue ? "console (enqueued)" : "console (waiting)", "console (finished)");
		final PrintWriter out = new PrintWriter(console.setSelected());
		out.println(script.getFileName());
		out.println();
		packOnce();
		console.thread(new Runnable() {
			public void run() {
				try {
					Environment.setConsole(out);
					JMainFrame.this.setCurrent();
					synchronized (JMainFrame.class) {
						if (enqueue) {
							JMainFrame.class.wait();
						}
						console.setTitle("console (running)");
						((ScriptParser) parserClass.newInstance()).parseAll(script);
					}
					out.close();
				}
				catch (Exception e) {
					e.printStackTrace(out);
					e.printStackTrace();
					console.setTitle("console (aborted)");
				}
			}
		}).start();
	}

	protected boolean needsPack = true;

	public void packOnce() {
		if (needsPack) {
			frame.pack();
			needsPack = false;
		}
	}
}
