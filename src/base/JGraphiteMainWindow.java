package base;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Point;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import java.awt.Button;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Stack;

import javax.swing.ImageIcon;

import tools.Brush;
import tools.Line;
import tools.Manipulators;
import tools.Oval;
import tools.PaintBucket;
import tools.Pencil;
import tools.Rectangle;
import tools.Rubber;
import tools.Tool;
import tools.ToolID;


/**
 * Main class of the program. Main window with all its elements and event control.
 * @author spliner21
 * @version 1.0
 */
public class JGraphiteMainWindow {
	/* image dimensions */
	int imgWidth = 500;
	int imgHeight = 400;
	
	/* path to the file - used in saving and opening */
	String bitmapPath = "";
	
	/* Tool colors - foreground and background color */
	Color fgColor = Color.BLACK, bgColor = Color.WHITE;
	
	/* Main window frame */
	private JFrame frmJgraphite;
	
	/* Global elements
	 * those, which vars are needed not only during construction */
	DrawingPanel panel;
	JScrollPane scrollPane;
	JSpinner thickness;
	JCheckBox chckbxFillWithColor;
	JButton btnUndo, btnRedo;
	JMenuItem mntmUndo, mntmRedo;
	Button[] colorBtn;
	
	/* Tools */
	ToolID mode;
	Brush brush;
	Rubber rubber;
	Line line;
	Pencil pencil;
	Rectangle rect;
	Oval oval;
	PaintBucket pBucket;
	Tool currTool;
	
	/* Undo and redo stacks */
	Stack<BufferedImage> undoStack, redoStack;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JGraphiteMainWindow window = new JGraphiteMainWindow();
					
					window.panel.drawResultImage();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public JGraphiteMainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frmJgraphite = new JFrame();
		frmJgraphite.setTitle("JGraphite");
				frmJgraphite.setBounds(100, 100, 720, 565);
		frmJgraphite.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frmJgraphite.setJMenuBar(createMenuBar());
		
		/* Create the toolbars */
		frmJgraphite.getContentPane().add(createTopToolBar(), BorderLayout.NORTH);		
		frmJgraphite.getContentPane().add(createColorToolBar(), BorderLayout.SOUTH);
		frmJgraphite.getContentPane().add(createLeftToolBar(), BorderLayout.WEST);
		
		/* ScrollPane creation - it is main window image view */
		generateContentView();		
		frmJgraphite.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		/* Show the window */
		frmJgraphite.setVisible(true);
		
        /* Initialize undo and redo stacks */
        undoStack = new Stack<BufferedImage>();
        redoStack = new Stack<BufferedImage>();
        
        /* Display initial image */
        panel.drawResultImage();      
        /* Initialize tools */
        constructTools();
	}

	/**
	 * Construct menu bar content
	 * @return menu bar with it contents
	 */
	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		
		/* File menu */
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmNew = new JMenuItem("New");
		mntmNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newImage();
			}
		});
		mnFile.add(mntmNew);
		
		JMenuItem mntmOpen = new JMenuItem("Open...");
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFile();
			}
		});
		mnFile.add(mntmOpen);
		
		JSeparator separator_4 = new JSeparator();
		mnFile.add(separator_4);
		
		JMenuItem mntmSave = new JMenuItem("Save...");
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveFile(false);
			}
		});
		mnFile.add(mntmSave);
		
		JMenuItem mntmSaveAs = new JMenuItem("Save As...");
		mntmSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveFile(true);
			}
		});
		mnFile.add(mntmSaveAs);
		
		JSeparator separator_5 = new JSeparator();
		mnFile.add(separator_5);
		
		JMenuItem mntmClose = new JMenuItem("Close");
		mntmClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (frmJgraphite.isDisplayable()) {
	                frmJgraphite.dispose();
	            }
			}
		});
		mnFile.add(mntmClose);
		
		/* Edit menu */
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		mntmUndo = new JMenuItem("Undo");
		mntmUndo.setEnabled(false);
		mntmUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				undo();
			}
		});
		mnEdit.add(mntmUndo);
		
		mntmRedo = new JMenuItem("Redo");
		mntmRedo.setEnabled(false);
		mntmRedo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				redo();
			}
		});
		mnEdit.add(mntmRedo);
		
		JSeparator separator_8 = new JSeparator();
		mnEdit.add(separator_8);
		
		JMenuItem mntmFlipHorizontally = new JMenuItem("Flip horizontally");
		mntmFlipHorizontally.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	    		addToUndo();
				panel.setResultImageData((Manipulators.flip(panel.getResultImage(), false)));
				panel.copyResultToWip();
	    		panel.drawResultImage();
			}
		});
		mnEdit.add(mntmFlipHorizontally);
		
		JMenuItem mntmFlipVertically = new JMenuItem("Flip vertically");
		mntmFlipVertically.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	    		addToUndo();
				panel.setResultImageData((Manipulators.flip(panel.getResultImage(), true)));
				panel.copyResultToWip();
	    		panel.drawResultImage();
			}
		});
		mnEdit.add(mntmFlipVertically);
		
		JSeparator separator_9 = new JSeparator();
		mnEdit.add(separator_9);
		
		JMenuItem mntmRotate90cw = new JMenuItem("Rotate 90\u00BA CW");
		mntmRotate90cw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	    		addToUndo();
				panel.setResultImage(Manipulators.rotate(panel.getResultImage(), 90));
				panel.setWipImage(Manipulators.rotate(panel.getWipImage(), 90));
				refreshTools();

				refreshScrolls();
			}
		});
		mnEdit.add(mntmRotate90cw);
		
		JMenuItem mntmRotate90ccw = new JMenuItem("Rotate 90\u00BA CCW");
		mntmRotate90ccw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	    		addToUndo();
				panel.setResultImage(Manipulators.rotate(panel.getResultImage(), 270));
				panel.setWipImage(Manipulators.rotate(panel.getWipImage(), 270));
				refreshTools();
				
				refreshScrolls();
			}
		});
		mnEdit.add(mntmRotate90ccw);
		
		JMenuItem mntmRotate180 = new JMenuItem("Rotate 180\u00BA");
		mntmRotate180.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	    		addToUndo();
				panel.setResultImage(Manipulators.rotate(panel.getResultImage(), 180));
				panel.setWipImage(Manipulators.rotate(panel.getWipImage(), 180));
				refreshTools();

	    		panel.drawResultImage();
			}
		});
		mnEdit.add(mntmRotate180);
		
		JSeparator separator_14 = new JSeparator();
		mnEdit.add(separator_14);
		
		JMenuItem mntmResize = new JMenuItem("Resize");
		mntmResize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resize();
			}
		});
		mnEdit.add(mntmResize);
		
		/* Tools menu */
		JMenu mnTools = new JMenu("Tools");
		menuBar.add(mnTools);
		
		JMenuItem mntmPencil = new JMenuItem("Pencil");
		mntmPencil.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeTool(ToolID.Pencil);
			}
		});
		mnTools.add(mntmPencil);
		
		JMenuItem mntmBrush = new JMenuItem("Brush");
		mntmBrush.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeTool(ToolID.Brush);
			}
		});
		mnTools.add(mntmBrush);
		
		JMenuItem mntmRubber = new JMenuItem("Rubber");
		mntmRubber.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeTool(ToolID.Rubber);
			}
		});
		mnTools.add(mntmRubber);
		
		JSeparator separator_6 = new JSeparator();
		mnTools.add(separator_6);
		
		JMenuItem mntmLine = new JMenuItem("Line");
		mntmLine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeTool(ToolID.Line);
			}
		});
		mnTools.add(mntmLine);
		
		JMenuItem mntmRectangle = new JMenuItem("Rectangle");
		mntmRectangle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeTool(ToolID.Rectangle);
			}
		});
		mnTools.add(mntmRectangle);
		
		JMenuItem mntmOval = new JMenuItem("Oval");
		mntmOval.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeTool(ToolID.Oval);
			}
		});
		mnTools.add(mntmOval);
		
		JSeparator separator_7 = new JSeparator();
		mnTools.add(separator_7);
		
		JMenuItem mntmPaintBucket = new JMenuItem("Paint Bucket");
		mntmPaintBucket.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeTool(ToolID.PaintBucket);
			}
		});
		mnTools.add(mntmPaintBucket);
		
		/* Help menu */
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmAbout = new JMenuItem("About...");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(frmJgraphite, "JGraphite 1.0\nCopyright (c) 2014 by spliner21\nhttp://spliner.net", 
												"About...", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		mnHelp.add(mntmAbout);
		
		return menuBar;
	}
	
	/**
	 * Method for top toolbar initialization - file manipulation + undo and redo + tool properties
	 * @return top toolbar
	 */
	private JToolBar createTopToolBar() {

		JToolBar toolBar = new JToolBar();
		
		/* New file button */
		JButton btnNew = new JButton("");
		btnNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newImage();
			}
		});
		btnNew.setToolTipText("New");
		btnNew.setIcon(new ImageIcon(getClass().getResource("/icons/new.png")));
		toolBar.add(btnNew);
		
		/* Open file button */
		JButton btnOpen = new JButton("");
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFile();
			}
		});
		btnOpen.setToolTipText("Open...");
		btnOpen.setIcon(new ImageIcon(getClass().getResource("/icons/open.png")));
		toolBar.add(btnOpen);
		
		/* Save button */
		JButton btnSave = new JButton("");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveFile(false);
			}
		});
		btnSave.setToolTipText("Save...");
		btnSave.setIcon(new ImageIcon(getClass().getResource("/icons/save.png")));
		toolBar.add(btnSave);

		/* Save as button */
		JButton btnSaveAs = new JButton("");
		btnSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveFile(true);
			}
		});
		btnSaveAs.setToolTipText("Save as...");
		btnSaveAs.setIcon(new ImageIcon(getClass().getResource("/icons/saveas.png")));
		toolBar.add(btnSaveAs);
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setPreferredSize(new Dimension(10, 20));
		separator.setMaximumSize(new Dimension(10, 25));
		toolBar.add(separator);
		
		/* Undo button */
		btnUndo = new JButton("");
		btnUndo.setToolTipText("Undo");
		btnUndo.setIcon(new ImageIcon(getClass().getResource("/icons/undo.png")));
		btnUndo.setEnabled(false);
		btnUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				undo();
			}
		});
		toolBar.add(btnUndo);
		
		/* Redo button */
		btnRedo = new JButton("");
		btnRedo.setToolTipText("Redo");
		btnRedo.setIcon(new ImageIcon(getClass().getResource("/icons/redo.png")));
		btnRedo.setEnabled(false);
		btnRedo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				redo();
			}
		});
		toolBar.add(btnRedo);
		
		JSeparator spaceSeparator = new JSeparator();
		toolBar.add(spaceSeparator);
		
		/* Thickness tool property */
		JLabel lblThickness = new JLabel("Thickness");
		toolBar.add(lblThickness);
  
		thickness = new JSpinner(new SpinnerNumberModel(10, 0, 50, 1));
		thickness.setMaximumSize(new Dimension(200, 25));
		thickness.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				switch(mode) {
				case Brush:
					brush.setStroke((int)thickness.getValue());
					break;
				case Line:
					line.setStroke((int)thickness.getValue());
					break;
				case Oval:
					oval.setStroke((int)thickness.getValue());
					break;
				case Rectangle:
					rect.setStroke((int)thickness.getValue());
					break;
				case Rubber:
					rubber.setStroke((int)thickness.getValue());
					break;
				default:
					break;
				
				}
			}
		});
		toolBar.add(thickness);
		
		/* Fill checkbox - for rectangle and oval */
		chckbxFillWithColor = new JCheckBox("Fill with color");
		chckbxFillWithColor.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				switch(mode) {
				case Oval:
					oval.setFill(chckbxFillWithColor.isSelected());
					break;
				case Rectangle:
					rect.setFill(chckbxFillWithColor.isSelected());
					break;
				default:
					break;
				}
			}
		});		
		chckbxFillWithColor.setEnabled(false);
		toolBar.add(chckbxFillWithColor);
		
		return toolBar;
	}
	
	/**
	 * Method for color toolbar initialization. 
	 * @return color toolbar
	 */
	private JToolBar createColorToolBar() {
		JToolBar toolBar = new JToolBar();
		JPanel panel = new JPanel();
		toolBar.add(panel);
		
		/* Foreground and background color buttons */
		final Button fgColorBtn = new Button("");
		fgColorBtn.setActionCommand("");
		fgColorBtn.setBackground(Color.BLACK);
		panel.add(fgColorBtn);
		

		final Button bgColorBtn = new Button("");
		bgColorBtn.setActionCommand("");
		bgColorBtn.setBackground(Color.WHITE);
		panel.add(bgColorBtn);
		
		JSeparator separator = new JSeparator();
		separator.setMaximumSize(new Dimension(10, 25));
		panel.add(separator);
		
		/* Eight basic colors buttons */
		Button color1Btn = new Button("");
		color1Btn.setActionCommand("");
		color1Btn.setBackground(Color.BLACK);
		panel.add(color1Btn);
		

		Button color2Btn = new Button("");
		color2Btn.setActionCommand("");
		color2Btn.setBackground(Color.WHITE);
		panel.add(color2Btn);
		
		Button color3Btn = new Button("");
		color3Btn.setActionCommand("");
		color3Btn.setBackground(Color.RED);
		panel.add(color3Btn);
		

		Button color4Btn = new Button("");
		color4Btn.setActionCommand("");
		color4Btn.setBackground(Color.GREEN);
		panel.add(color4Btn);


		Button color5Btn = new Button("");
		color5Btn.setActionCommand("");
		color5Btn.setBackground(Color.BLUE);
		panel.add(color5Btn);
		

		Button color6Btn = new Button("");
		color6Btn.setActionCommand("");
		color6Btn.setBackground(Color.YELLOW);
		panel.add(color6Btn);
		
		Button color7Btn = new Button("");
		color7Btn.setActionCommand("");
		color7Btn.setBackground(Color.CYAN);
		panel.add(color7Btn);
		

		Button color8Btn = new Button("");
		color8Btn.setActionCommand("");
		color8Btn.setBackground(Color.PINK);
		panel.add(color8Btn);
		

		colorBtn = new Button[]{color1Btn,color2Btn,color3Btn,color4Btn,color5Btn,color6Btn,color7Btn,color8Btn};
		
		for(final Button b: colorBtn)
		{
			b.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					if(e.getButton() == MouseEvent.BUTTON1) {
						fgColorBtn.setBackground(b.getBackground());
						fgColor = b.getBackground();
						refreshColors();
					}
					if(e.getButton() == MouseEvent.BUTTON3) {
						bgColorBtn.setBackground(b.getBackground());
						bgColor = b.getBackground();
						refreshColors();
					}
				}
			});
		}
		
		return toolBar;
	}
	
	/**
	 * Method for left toolbar initialization - tools and manipulators. 
	 * @return left toolbar.
	 */
	private JToolBar createLeftToolBar() {
		JToolBar toolBar = new JToolBar();
		toolBar.setOrientation(SwingConstants.VERTICAL);
		
		JButton btnPencil = new JButton("");
		btnPencil.setVerticalAlignment(SwingConstants.TOP);
		btnPencil.setToolTipText("Pencil");
		btnPencil.setIcon(new ImageIcon(getClass().getResource("/icons/pencil.png")));
		btnPencil.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeTool(ToolID.Pencil);
			}
		});
		toolBar.add(btnPencil);
		
		JButton btnBrush = new JButton("");
		btnBrush.setVerticalAlignment(SwingConstants.TOP);
		btnBrush.setToolTipText("Brush");
		btnBrush.setIcon(new ImageIcon(getClass().getResource("/icons/brush.png")));
		btnBrush.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeTool(ToolID.Brush);
			}
		});
		toolBar.add(btnBrush);
		
		JButton btnRubber = new JButton("");
		btnRubber.setVerticalAlignment(SwingConstants.TOP);
		btnRubber.setToolTipText("Rubber");
		btnRubber.setIcon(new ImageIcon(getClass().getResource("/icons/rubber.png")));
		btnRubber.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeTool(ToolID.Rubber);
			}
		});
		toolBar.add(btnRubber);
		
		JSeparator separator = new JSeparator();
		separator.setMaximumSize(new Dimension(25, 2));
		toolBar.add(separator);
		
		JButton btnLine = new JButton("");
		btnLine.setVerticalAlignment(SwingConstants.TOP);
		btnLine.setToolTipText("Line");
		btnLine.setIcon(new ImageIcon(getClass().getResource("/icons/line.png")));
		btnLine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeTool(ToolID.Line);
			}
		});
		toolBar.add(btnLine);
		
		JButton btnRectangle = new JButton("");
		btnRectangle.setVerticalAlignment(SwingConstants.TOP);
		btnRectangle.setToolTipText("Rectangle");
		btnRectangle.setIcon(new ImageIcon(getClass().getResource("/icons/rectangle.png")));
		btnRectangle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeTool(ToolID.Rectangle);
			}
		});
		toolBar.add(btnRectangle);
		
		JButton btnOval = new JButton("");
		btnOval.setToolTipText("Oval");
		btnOval.setIcon(new ImageIcon(getClass().getResource("/icons/oval.png")));
		btnOval.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeTool(ToolID.Oval);
			}
		});
		toolBar.add(btnOval);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setMaximumSize(new Dimension(25, 2));
		toolBar.add(separator_1);
		
		JButton btnFill = new JButton("");
		btnFill.setToolTipText("Paint bucket");
		btnFill.setIcon(new ImageIcon(getClass().getResource("/icons/paint_bucket.png")));
		btnFill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeTool(ToolID.PaintBucket);
			}
		});
		toolBar.add(btnFill);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setMaximumSize(new Dimension(25, 2));
		toolBar.add(separator_2);
		
		JButton btnFlipH = new JButton("");
		btnFlipH.setToolTipText("Flip horizontally");
		btnFlipH.setIcon(new ImageIcon(getClass().getResource("/icons/flip_h.png")));
		btnFlipH.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addToUndo();

				panel.setResultImageData((Manipulators.flip(panel.getResultImage(), false)));
				panel.copyResultToWip();
				
	    		panel.drawResultImage();
			}
		});
		toolBar.add(btnFlipH);
		
		JButton btnFlipV = new JButton("");
		btnFlipV.setToolTipText("Flip vertically");
		btnFlipV.setIcon(new ImageIcon(getClass().getResource("/icons/flip_v.png")));
		btnFlipV.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addToUndo();

				panel.setResultImageData((Manipulators.flip(panel.getResultImage(), true)));
				panel.copyResultToWip();;
				
	    		panel.drawResultImage();
			}
		});
		toolBar.add(btnFlipV);
		
		JButton btnRotate90cw = new JButton("");
		btnRotate90cw.setToolTipText("Rotate 90 degrees CW");
		btnRotate90cw.setIcon(new ImageIcon(getClass().getResource("/icons/rotate_90_clockwise.png")));
		btnRotate90cw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addToUndo();

				panel.setResultImage(Manipulators.rotate(panel.getResultImage(), 90));
				panel.setWipImage(Manipulators.rotate(panel.getWipImage(), 90));
				refreshTools();
				
				refreshScrolls();
			}
		});
		toolBar.add(btnRotate90cw);
		
		JButton btnRotate90ccw = new JButton("");
		btnRotate90ccw.setToolTipText("Rotate 90 degrees CCW");
		btnRotate90ccw.setIcon(new ImageIcon(getClass().getResource("/icons/rotate_90_counterclockwise.png")));
		btnRotate90ccw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addToUndo();

				panel.setResultImage(Manipulators.rotate(panel.getResultImage(), 270));
				panel.setWipImage(Manipulators.rotate(panel.getWipImage(), 270));
				refreshTools();
				
				refreshScrolls();
			}
		});
		toolBar.add(btnRotate90ccw);
		
		JButton btnRotate180 = new JButton("");
		btnRotate180.setToolTipText("Rotate 180 degrees");
		btnRotate180.setIcon(new ImageIcon(getClass().getResource("/icons/rotate_180.png")));
		btnRotate180.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addToUndo();

				panel.setResultImage(Manipulators.rotate(panel.getResultImage(), 180));
				panel.setWipImage(Manipulators.rotate(panel.getWipImage(), 180));
				refreshTools();

	    		panel.drawResultImage();
			}
		});
		toolBar.add(btnRotate180);
		
		JSeparator separator_3 = new JSeparator();
		toolBar.add(separator_3);
		
		return toolBar;
	}
	
	/**
	 * Content view initialization - panting area.
	 */
	private void generateContentView() {
		/* ScrollPane initialization and event reactions. */
		scrollPane = new JScrollPane();
		scrollPane.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
	    		refreshScrolls();
			}
		});
		scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
	    		panel.drawResultImage();					
			}
		});
		scrollPane.getHorizontalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
	    		panel.drawResultImage();
				
			}
		});
		
		/* Drawing panel initialization and drawing event definition */
		panel = new DrawingPanel(imgWidth, imgHeight);
		panel.setPreferredSize(new Dimension(imgWidth, imgHeight));
		panel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				panel.copyResultToWip();

				currTool.mouseHold(e.getX(), e.getY());
				
				panel.drawWipImage();
			}
		});
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				addToUndo();
				currTool.mousePressed(e.getX(), e.getY());

	    		panel.drawResultImage();
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				
				if(mode != ToolID.PaintBucket)
					currTool.mouseReleased(e.getX(), e.getY());
				else
					pBucket.fillWithColor(e.getX(), e.getY(),panel.getResultImage());

				panel.drawResultImage();
				
			}
		});
		
		scrollPane.setViewportView(panel);
	}
	
	/**
	 * Tools initialization 
	 */
	private void constructTools() {
		Graphics res, wip;
		res = panel.getResultImage().getGraphics();
		wip = panel.getWipImage().getGraphics();
		brush = new Brush(res,wip);
		rubber = new Rubber(res,wip);
		line = new Line(res,wip);
		pencil = new Pencil(res,wip);
		rect = new Rectangle(res,wip);
		oval = new Oval(res,wip);
		pBucket = new PaintBucket(res,wip);
		refreshColors();
		currTool = brush;
		
		mode = ToolID.Brush;
		
	}
	
	/**
	 * Tool change 
	 * @param t ID of Tool to change to.
	 */
	private void changeTool(ToolID t) {
		mode = t;
		switch(mode) {
		case Brush:
			currTool = brush;
			thickness.setEnabled(true);
			thickness.setValue(brush.getStroke());
			chckbxFillWithColor.setEnabled(false);
			break;
		case Line:
			currTool = line;
			thickness.setEnabled(true);
			thickness.setValue(line.getStroke());
			chckbxFillWithColor.setEnabled(false);
			break;
		case Oval:
			currTool = oval;
			thickness.setEnabled(true);
			thickness.setValue(oval.getStroke());
			chckbxFillWithColor.setEnabled(true);
			chckbxFillWithColor.setSelected(oval.getFill());
			break;
		case Pencil:
			currTool = pencil;
			thickness.setEnabled(false);
			chckbxFillWithColor.setEnabled(false);
			break;
		case Rectangle:
			currTool = rect;
			thickness.setEnabled(true);
			thickness.setValue(rect.getStroke());
			chckbxFillWithColor.setEnabled(true);
			chckbxFillWithColor.setSelected(rect.getFill());
			break;
		case Rubber:
			currTool = rubber;
			thickness.setEnabled(true);
			thickness.setValue(rubber.getStroke());
			chckbxFillWithColor.setEnabled(false);
			break;
		case PaintBucket:
			currTool = pBucket;
			thickness.setEnabled(false);
			chckbxFillWithColor.setEnabled(false);
			break;
		default:
			break;
		
		}
		refreshColors();
	}
	
	/**
	 * Refresh tool graphics reference - needed when image variables are completely replaced.
	 */
	private void refreshTools() {
		Graphics res, wip;
		res = panel.getResultImage().getGraphics();
		wip = panel.getWipImage().getGraphics();
		brush.refreshGraphics(res,wip);
		line.refreshGraphics(res,wip);
		pencil.refreshGraphics(res,wip);
		rect.refreshGraphics(res,wip);
		oval.refreshGraphics(res,wip);
		rubber.refreshGraphics(res,wip);
		pBucket.refreshGraphics(res,wip);
		refreshColors();
	}
	
	/**
	 * Colors refresh. Needed for example when image graphics reference is changed.
	 */
	private void refreshColors() {
		brush.changeColors(fgColor, bgColor);
		line.changeColors(fgColor, bgColor);
		pencil.changeColors(fgColor, bgColor);
		rect.changeColors(fgColor, bgColor);
		oval.changeColors(fgColor, bgColor);
		rubber.changeColors(fgColor, bgColor);
		pBucket.changeColors(fgColor, bgColor);
	}
	
	/**
	 * Refresh scrolls - when window is resized or image size changed.
	 */
	private void refreshScrolls() {
		imgWidth = panel.getResultImage().getWidth();
		imgHeight = panel.getResultImage().getHeight();
		panel.setPreferredSize(new Dimension(imgWidth,imgHeight));
		panel.setSize(imgWidth,imgHeight);
		scrollPane.revalidate();
		panel.drawResultImage();
		
	}
		
	/**
	 * New image action.
	 */
	private void newImage() {
		ImageSize newBitmapSize = new ImageSize(frmJgraphite);		
		newBitmapSize.display();
		
		Point dimensions = newBitmapSize.returnDimensions();		
		if(dimensions == null)
			return;

        /* Clear stacks */
        undoStack.clear();
        redoStack.clear();
        
        /* disable undo and redo buttons */
        btnUndo.setEnabled(false);
        btnRedo.setEnabled(false);
        mntmUndo.setEnabled(false);
        mntmRedo.setEnabled(false);
        
		imgWidth = dimensions.x;
		imgHeight = dimensions.y;
		panel.constructImage(imgWidth, imgHeight);

        refreshScrolls();		
		refreshTools();
	}
	
	/**
	 * Open file action.
	 */
	private void openFile() {
		int ret = -1;
		/* Open file dialog */
		JFileChooser fOpenDialog = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("BMP File","bmp");
		fOpenDialog.setFileFilter(filter);
		
		ret = fOpenDialog.showOpenDialog(null);
		
		/* If "Open" was clicked */
		if(ret == JFileChooser.APPROVE_OPTION) {
			bitmapPath = fOpenDialog.getSelectedFile().getAbsolutePath();
			try {
				/* read files */
				URL url = fOpenDialog.getSelectedFile().toURI().toURL();
	            panel.setResultImage(ImageIO.read(url));
	            panel.setWipImage(ImageIO.read(url));

	            imgWidth = panel.getResultImage().getWidth();
	            imgHeight = panel.getResultImage().getHeight();
	            
	            /* Clear stacks */
	            undoStack.clear();
	            redoStack.clear();
	            
	            /* disable undo and redo buttons */
	            btnUndo.setEnabled(false);
	            btnRedo.setEnabled(false);
	            mntmUndo.setEnabled(false);
	            mntmRedo.setEnabled(false);
	            
	            refreshScrolls();				
				refreshTools();
				
		    } catch (IOException ex) {
	            ex.printStackTrace();
		    }
		}
	}
	
	/**
	 * Save and Save as actions
	 * @param saveAs if true, then always opens a save dialog. if false, only when first save of image is performed, the save dialog is opened.
	 */
	private void saveFile(boolean saveAs) {
		int ret = -1;
		/* If file was not saved before or "Save as..." was clicked */
		if(bitmapPath.equals("") || saveAs) {
			JFileChooser fSaveDialog = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("BMP File","bmp");
			fSaveDialog.setFileFilter(filter);
			ret = fSaveDialog.showSaveDialog(null);
			
			/* If "Save" was clicked */
			if(ret == JFileChooser.APPROVE_OPTION) {
				bitmapPath = fSaveDialog.getSelectedFile().getAbsolutePath();
				if(!bitmapPath.endsWith(".bmp"))
					bitmapPath += ".bmp";
			}
		}
		if(ret != JFileChooser.CANCEL_OPTION) {
			try {
	            ImageIO.write(panel.getResultImage(), "bmp", new File(bitmapPath));
		    } catch (IOException ex) {
	            ex.printStackTrace();
		    }
		}
	}
	
	/**
	 * Adds actual result image to undo
	 */
	private void addToUndo() {
		while(undoStack.size() >= 10)
			undoStack.removeElementAt(0);
		BufferedImage res = panel.getResultImage();
		BufferedImage tmp = new BufferedImage(res.getWidth(), res.getHeight(), res.getType());
		tmp.setData(res.getData());
		undoStack.add(tmp);
		redoStack.clear();
		btnUndo.setEnabled(true);
		btnRedo.setEnabled(false);
        mntmUndo.setEnabled(true);
        mntmRedo.setEnabled(false);	
	}

	/**
	 * Undo operation - adds actual result image to redo, and replaces result image with last undo.
	 */
	private void undo() {
		BufferedImage res = panel.getResultImage();
		BufferedImage tmp = new BufferedImage(res.getWidth(), res.getHeight(), res.getType());
		tmp.setData(res.getData());
		redoStack.add(tmp);
		
		panel.setResultImage(undoStack.pop());

		if(undoStack.size() == 0) {
			btnUndo.setEnabled(false);
	        mntmUndo.setEnabled(false);
		}
		
		btnRedo.setEnabled(true);
        mntmRedo.setEnabled(true);
		refreshTools();
		refreshScrolls();
	}

	/**
	 * Redo operation - adds actual result image to undo, and replaces result image with last redo.
	 */
	private void redo() {
		BufferedImage res = panel.getResultImage();
		BufferedImage tmp = new BufferedImage(res.getWidth(), res.getHeight(), res.getType());
		tmp.setData(res.getData());
		undoStack.add(tmp);

		panel.setResultImage(redoStack.pop());

		if(redoStack.size() == 0) {
			btnRedo.setEnabled(false);
			mntmRedo.setEnabled(false);
		}	
		
		btnUndo.setEnabled(true);
        mntmUndo.setEnabled(true);
		refreshTools();
		refreshScrolls();
	}
	
	/**
	 * Resize operation - uses manipulators.scale.
	 */
	private void resize() {
		ImageSize bitmapResize = new ImageSize(frmJgraphite);
		
		bitmapResize.display();
		
		Point dimensions = bitmapResize.returnDimensions();
		
		if(dimensions == null)
			return;

		addToUndo();

		panel.setResultImage(Manipulators.scale(panel.getResultImage(), dimensions.x, dimensions.y));
		panel.setWipImage(Manipulators.scale(panel.getWipImage(), dimensions.x, dimensions.y));
		
		imgWidth = dimensions.x;
		imgHeight = dimensions.y;
		refreshScrolls();
		refreshTools();
	}
}
