import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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
import tools.Oval;
import tools.PaintBucket;
import tools.Pencil;
import tools.Rectangle;
import tools.Rubber;
import tools.Tool;
import tools.ToolID;


public class JGraphiteMainWindow {
	int imgWidth = 500;
	int imgHeight = 400;
	
	String bitmapPath = "";
	
	Color fgColor = Color.BLACK, bgColor = Color.WHITE;
	
	private JFrame frmJgraphite;
	
	ToolID mode;
	Brush brush;
	Rubber rubber;
	Line line;
	Pencil pencil;
	Rectangle rect;
	Oval oval;
	PaintBucket pBucket;
	Tool currTool;
	
	DrawingPanel panel;
	JScrollPane scrollPane;
	JSpinner thickness;
	JCheckBox chckbxFillWithColor;
	JButton btnUndo, btnRedo;
	
	Stack<BufferedImage> undoList, redoList;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JGraphiteMainWindow window = new JGraphiteMainWindow();
					
					window.refreshGraphics();
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
				

		frmJgraphite.getContentPane().add(createTopToolBar(), BorderLayout.NORTH);
		
		
		frmJgraphite.getContentPane().add(createColorToolBar(), BorderLayout.SOUTH);


		frmJgraphite.getContentPane().add(createLeftToolBar(), BorderLayout.WEST);
		
		generateContentView();
		
		frmJgraphite.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		frmJgraphite.setVisible(true);
		
		Graphics2D gc = (Graphics2D)panel.getResultImage().getGraphics();
        gc.setColor(bgColor);
        gc.fillRect(0, 0, imgWidth, imgHeight); // fill in background
            
        undoList = new Stack<BufferedImage>();
        redoList = new Stack<BufferedImage>();
        
        refreshGraphics();
        
        constructTools();
		

	}

	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		
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
		
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		JMenuItem mntmUndo = new JMenuItem("Undo");
		mntmUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				undo();
			}
		});
		mnEdit.add(mntmUndo);
		
		JMenuItem mntmRedo = new JMenuItem("Redo");
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
				panel.setResultImageData((Manipulators.flip(panel.getResultImage(), false)));
				panel.copyResultToWip();
	    		refreshGraphics();
			}
		});
		mnEdit.add(mntmFlipHorizontally);
		
		JMenuItem mntmFlipVertically = new JMenuItem("Flip vertically");
		mntmFlipVertically.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel.setResultImageData((Manipulators.flip(panel.getResultImage(), true)));
				panel.copyResultToWip();
	    		refreshGraphics();
			}
		});
		mnEdit.add(mntmFlipVertically);
		
		JSeparator separator_9 = new JSeparator();
		mnEdit.add(separator_9);
		
		JMenuItem mntmRotate90cw = new JMenuItem("Rotate 90\u00BA CW");
		mntmRotate90cw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel.setResultImage(Manipulators.rotate(panel.getResultImage(), 90));
				panel.setWipImage(Manipulators.rotate(panel.getWipImage(), 90));
				refreshTools();
				int tmp = imgWidth;
				imgWidth = imgHeight;
				imgHeight = tmp;
				refreshScrolls();
			}
		});
		mnEdit.add(mntmRotate90cw);
		
		JMenuItem mntmRotate90ccw = new JMenuItem("Rotate 90\u00BA CCW");
		mntmRotate90ccw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel.setResultImage(Manipulators.rotate(panel.getResultImage(), 270));
				panel.setWipImage(Manipulators.rotate(panel.getWipImage(), 270));
				refreshTools();
				int tmp = imgWidth;
				imgWidth = imgHeight;
				imgHeight = tmp;
				refreshScrolls();
			}
		});
		mnEdit.add(mntmRotate90ccw);
		
		JMenuItem mntmRotate180 = new JMenuItem("Rotate 180\u00BA");
		mntmRotate180.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel.setResultImage(Manipulators.rotate(panel.getResultImage(), 180));
				panel.setWipImage(Manipulators.rotate(panel.getWipImage(), 180));
				refreshTools();

	    		refreshGraphics();
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
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmAbout = new JMenuItem("About...");
		mnHelp.add(mntmAbout);
		
		return menuBar;
	}
	
	private JToolBar createTopToolBar() {

		JToolBar toolBar = new JToolBar();
		
		JButton btnNew = new JButton("");
		btnNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newImage();
			}
		});
		btnNew.setToolTipText("New");
		btnNew.setIcon(new ImageIcon("icons/new.png"));
		toolBar.add(btnNew);
		
		JButton btnOpen = new JButton("");
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFile();
			}
		});
		btnOpen.setToolTipText("Open...");
		btnOpen.setIcon(new ImageIcon("icons/open.png"));
		toolBar.add(btnOpen);
		
		JButton btnSave = new JButton("");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveFile(false);
			}
		});
		btnSave.setToolTipText("Save...");
		btnSave.setIcon(new ImageIcon("icons/save.png"));
		toolBar.add(btnSave);


		JButton btnSaveAs = new JButton("");
		btnSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveFile(true);
			}
		});
		btnSaveAs.setToolTipText("Save...");
		btnSaveAs.setIcon(new ImageIcon("icons/saveas.png"));
		toolBar.add(btnSaveAs);
		
		JSeparator separator_10 = new JSeparator();
		separator_10.setOrientation(SwingConstants.VERTICAL);
		separator_10.setPreferredSize(new Dimension(10, 20));
		separator_10.setMaximumSize(new Dimension(10, 25));
		toolBar.add(separator_10);
		
		btnUndo = new JButton("");
		btnUndo.setToolTipText("Undo");
		btnUndo.setIcon(new ImageIcon("icons/undo.png"));
		btnUndo.setEnabled(false);
		btnUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				undo();
			}
		});
		toolBar.add(btnUndo);
		
		btnRedo = new JButton("");
		btnRedo.setToolTipText("Redo");
		btnRedo.setIcon(new ImageIcon("icons/redo.png"));
		btnRedo.setEnabled(false);
		btnRedo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				redo();
			}
		});
		toolBar.add(btnRedo);
		
		JSeparator separator_11 = new JSeparator();
		toolBar.add(separator_11);
		
		JSeparator separator_12 = new JSeparator();
		toolBar.add(separator_12);
		
		JLabel lblThickness = new JLabel("Thickness");
		toolBar.add(lblThickness);

		SpinnerNumberModel model1 = new SpinnerNumberModel(10, 0, 25, 1);   
		thickness = new JSpinner(model1);
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
	
	private JToolBar createColorToolBar() {
		JToolBar toolBar = new JToolBar();
		JPanel panel = new JPanel();
		toolBar.add(panel);
		
		final Button fgColorBtn = new Button("");
		fgColorBtn.setActionCommand("");
		fgColorBtn.setBackground(Color.BLACK);
		panel.add(fgColorBtn);
		

		final Button bgColorBtn = new Button("");
		bgColorBtn.setActionCommand("");
		bgColorBtn.setBackground(Color.WHITE);
		panel.add(bgColorBtn);
		
		JSeparator separator_13 = new JSeparator();
		separator_13.setMaximumSize(new Dimension(10, 25));
		panel.add(separator_13);
		

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
		

		final Button[] colorBtn = new Button[]{color1Btn,color2Btn,color3Btn,color4Btn,color5Btn,color6Btn,color7Btn,color8Btn};
		
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
	
	private JToolBar createLeftToolBar() {
		JToolBar toolBar = new JToolBar();
		toolBar.setOrientation(SwingConstants.VERTICAL);
		
		JButton btnPencil = new JButton("");
		btnPencil.setVerticalAlignment(SwingConstants.TOP);
		btnPencil.setToolTipText("Pencil");
		btnPencil.setIcon(new ImageIcon("icons/pencil.png"));
		btnPencil.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeTool(ToolID.Pencil);
			}
		});
		toolBar.add(btnPencil);
		
		JButton btnBrush = new JButton("");
		btnBrush.setVerticalAlignment(SwingConstants.TOP);
		btnBrush.setToolTipText("Brush");
		btnBrush.setIcon(new ImageIcon("icons/brush.png"));
		btnBrush.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeTool(ToolID.Brush);
			}
		});
		toolBar.add(btnBrush);
		
		JButton btnRubber = new JButton("");
		btnRubber.setVerticalAlignment(SwingConstants.TOP);
		btnRubber.setToolTipText("Rubber");
		btnRubber.setIcon(new ImageIcon("icons/rubber.png"));
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
		btnLine.setIcon(new ImageIcon("icons/line.png"));
		btnLine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeTool(ToolID.Line);
			}
		});
		toolBar.add(btnLine);
		
		JButton btnRectangle = new JButton("");
		btnRectangle.setVerticalAlignment(SwingConstants.TOP);
		btnRectangle.setToolTipText("Rectangle");
		btnRectangle.setIcon(new ImageIcon("icons/rectangle.png"));
		btnRectangle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeTool(ToolID.Rectangle);
			}
		});
		toolBar.add(btnRectangle);
		
		JButton btnOval = new JButton("");
		btnOval.setToolTipText("Oval");
		btnOval.setIcon(new ImageIcon("icons/oval.png"));
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
		btnFill.setIcon(new ImageIcon("icons/paint_bucket.png"));
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
		btnFlipH.setIcon(new ImageIcon("icons/flip_h.png"));
		btnFlipH.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addToUndo();

				panel.setResultImageData((Manipulators.flip(panel.getResultImage(), false)));
				panel.copyResultToWip();
				
	    		refreshGraphics();
			}
		});
		toolBar.add(btnFlipH);
		
		JButton btnFlipV = new JButton("");
		btnFlipV.setToolTipText("Flip vertically");
		btnFlipV.setIcon(new ImageIcon("icons/flip_v.png"));
		btnFlipV.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addToUndo();

				panel.setResultImageData((Manipulators.flip(panel.getResultImage(), false)));
				panel.copyResultToWip();;
				
	    		refreshGraphics();
			}
		});
		toolBar.add(btnFlipV);
		
		JButton btnRotate90cw = new JButton("");
		btnRotate90cw.setToolTipText("Rotate 90 degrees CW");
		btnRotate90cw.setIcon(new ImageIcon("icons/rotate_90_clockwise.png"));
		btnRotate90cw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addToUndo();

				panel.setResultImage(Manipulators.rotate(panel.getResultImage(), 90));
				panel.setWipImage(Manipulators.rotate(panel.getWipImage(), 90));
				refreshTools();
				int tmp = imgWidth;
				imgWidth = imgHeight;
				imgHeight = tmp;
				
				refreshScrolls();
			}
		});
		toolBar.add(btnRotate90cw);
		
		JButton btnRotate90ccw = new JButton("");
		btnRotate90ccw.setToolTipText("Rotate 90 degrees CCW");
		btnRotate90ccw.setIcon(new ImageIcon("icons/rotate_90_counterclockwise.png"));
		btnRotate90ccw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addToUndo();

				panel.setResultImage(Manipulators.rotate(panel.getResultImage(), 270));
				panel.setWipImage(Manipulators.rotate(panel.getWipImage(), 270));
				refreshTools();
				int tmp = imgWidth;
				imgWidth = imgHeight;
				imgHeight = tmp;
				
				refreshScrolls();
			}
		});
		toolBar.add(btnRotate90ccw);
		
		JButton btnRotate180 = new JButton("");
		btnRotate180.setToolTipText("Rotate 180 degrees");
		btnRotate180.setIcon(new ImageIcon("icons/rotate_180.png"));
		btnRotate180.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addToUndo();

				panel.setResultImage(Manipulators.rotate(panel.getResultImage(), 180));
				panel.setWipImage(Manipulators.rotate(panel.getWipImage(), 180));
				refreshTools();

	    		refreshGraphics();
			}
		});
		toolBar.add(btnRotate180);
		
		JSeparator separator_3 = new JSeparator();
		toolBar.add(separator_3);
		
		return toolBar;
	}
	
	private void generateContentView() {
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
	    		refreshGraphics();					
			}
		});
		scrollPane.getHorizontalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
	    		refreshGraphics();
				
			}
		});
		
				
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

	    		refreshGraphics();
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				
				if(mode != ToolID.PaintBucket)
					currTool.mouseReleased(e.getX(), e.getY());
				else
					pBucket.fillWithColor(e.getX(), e.getY(),panel.getResultImage());

				refreshGraphics();
				
			}
		});
		
		scrollPane.setViewportView(panel);
	}
	
	private void constructTools() {
		brush = new Brush(panel.getResultImage().getGraphics(),panel.getWipImage().getGraphics());
		rubber = new Rubber(panel.getResultImage().getGraphics(),panel.getWipImage().getGraphics());
		line = new Line(panel.getResultImage().getGraphics(),panel.getWipImage().getGraphics());
		pencil = new Pencil(panel.getResultImage().getGraphics(),panel.getWipImage().getGraphics());
		rect = new Rectangle(panel.getResultImage().getGraphics(),panel.getWipImage().getGraphics());
		oval = new Oval(panel.getResultImage().getGraphics(),panel.getWipImage().getGraphics());
		pBucket = new PaintBucket(panel.getResultImage().getGraphics(),panel.getWipImage().getGraphics());
		refreshColors();
		currTool = brush;
		
		mode = ToolID.Brush;
		
	}
	
	private void changeTool(ToolID t) {

		mode = t;
		switch(t) {
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
	}
	
	private void refreshTools() {
		brush.refreshGraphics(panel.getResultImage().getGraphics(),panel.getWipImage().getGraphics());
		line.refreshGraphics(panel.getResultImage().getGraphics(),panel.getWipImage().getGraphics());
		pencil.refreshGraphics(panel.getResultImage().getGraphics(),panel.getWipImage().getGraphics());
		rect.refreshGraphics(panel.getResultImage().getGraphics(),panel.getWipImage().getGraphics());
		oval.refreshGraphics(panel.getResultImage().getGraphics(),panel.getWipImage().getGraphics());
		rubber.refreshGraphics(panel.getResultImage().getGraphics(),panel.getWipImage().getGraphics());
		pBucket.refreshGraphics(panel.getResultImage().getGraphics(),panel.getWipImage().getGraphics());
		refreshColors();
	}
	
	private void refreshColors() {
		brush.changeColors(fgColor, bgColor);
		line.changeColors(fgColor, bgColor);
		pencil.changeColors(fgColor, bgColor);
		rect.changeColors(fgColor, bgColor);
		oval.changeColors(fgColor, bgColor);
		rubber.changeColors(fgColor, bgColor);
		pBucket.changeColors(fgColor, bgColor);
	}

	
	private void refreshGraphics() {
		panel.drawResultImage();
	}
	
	private void refreshScrolls() {
		panel.setPreferredSize(new Dimension(imgWidth,imgHeight));
		panel.setSize(imgWidth,imgHeight);
		scrollPane.revalidate();
		refreshGraphics();
		
	}
		
	private void newImage() {

		ImageSize newBitmapSize = new ImageSize(frmJgraphite);
		
		newBitmapSize.display();
		
		Point dimensions = newBitmapSize.returnDimensions();
		
		if(dimensions == null)
			return;

		imgWidth = dimensions.x;
		imgHeight = dimensions.y;
		panel.constructImage(imgWidth, imgHeight);// fill in background

        refreshScrolls();
		
		refreshTools();
	}
	
	private void openFile() {
		int ret = -1;
		JFileChooser fOpenDialog = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("BMP File","bmp");
		fOpenDialog.setFileFilter(filter);
		ret = fOpenDialog.showOpenDialog(null);
		if(ret == JFileChooser.APPROVE_OPTION) {
			bitmapPath = fOpenDialog.getSelectedFile().getAbsolutePath();
			try {
				URL url = fOpenDialog.getSelectedFile().toURI().toURL();
	            panel.setResultImage(ImageIO.read(url));
	            {
	                System.out.println("-- opened" + bitmapPath);
	            }
	            panel.setWipImage(ImageIO.read(url));

	            imgWidth = panel.getResultImage().getWidth();
	            imgHeight = panel.getResultImage().getHeight();

	            refreshScrolls();
				
				refreshTools();
				
		    } catch (IOException ex) {
	            ex.printStackTrace();
		    }
		}
	}
	
	private void saveFile(boolean saveAs) {
		int ret = -1;
		if(bitmapPath.equals("") || saveAs) {
			JFileChooser fSaveDialog = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("BMP File","bmp");
			fSaveDialog.setFileFilter(filter);
			ret = fSaveDialog.showSaveDialog(null);
			if(ret == JFileChooser.APPROVE_OPTION) {
				bitmapPath = fSaveDialog.getSelectedFile().getAbsolutePath();
			}
		}
		if(ret != JFileChooser.CANCEL_OPTION) {
			try {
	            if (ImageIO.write(panel.getResultImage(), "bmp", new File(bitmapPath)))
	            {
	                System.out.println("-- saved");
	            }
		    } catch (IOException ex) {
	            ex.printStackTrace();
		    }
		}
	}
	
	private void addToUndo() {
		if(undoList.size() > 10) {
			for(int i = undoList.size()-1; i >= 10; --i)
				undoList.removeElementAt(i);
		}
		BufferedImage res = panel.getResultImage();
		BufferedImage tmp = new BufferedImage(res.getWidth(), res.getHeight(), res.getType());
		tmp.setData(res.getData());
		undoList.add(tmp);
		redoList.clear();
		btnUndo.setEnabled(true);
		btnRedo.setEnabled(false);
		
	}

	private void undo() {
		BufferedImage res = panel.getResultImage();
		BufferedImage tmp = new BufferedImage(res.getWidth(), res.getHeight(), res.getType());
		tmp.setData(res.getData());
		redoList.add(tmp);
		
		panel.setResultImage(undoList.pop());

		if(undoList.size() == 0)
			btnUndo.setEnabled(false);
		
		btnRedo.setEnabled(true);
		refreshTools();
		refreshScrolls();
	}
	
	private void redo() {
		BufferedImage res = panel.getResultImage();
		BufferedImage tmp = new BufferedImage(res.getWidth(), res.getHeight(), res.getType());
		tmp.setData(res.getData());
		undoList.add(tmp);

		panel.setResultImage(redoList.pop());

		if(redoList.size() == 0)
			btnRedo.setEnabled(false);
		
		btnUndo.setEnabled(true);
		refreshTools();
		refreshScrolls();
	}
	
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
