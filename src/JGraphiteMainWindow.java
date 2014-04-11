import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JButton;
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
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;


public class JGraphiteMainWindow {
	int imgWidth = 500;
	int imgHeight = 400;
	
	Color fgColor = Color.BLACK, bgColor = Color.WHITE;
	
	private JFrame frame;
	
	ToolID mode;
	Brush brush;
	Rubber rubber;
	Line line;
	Pencil pencil;
	Rectangle rect;
	Oval oval;
	PaintBucket pBucket;
	Tool currTool;
	
	BufferedImage resultImage, wipImage;
	JPanel panel;
	JSpinner thickness;
	JCheckBox chckbxFillWithColor;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					@SuppressWarnings("unused")
					JGraphiteMainWindow window = new JGraphiteMainWindow();
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
		frame = new JFrame();
		frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				panel.getGraphics().drawImage(resultImage, 0, 0,null);
			}
		});
		frame.setBounds(100, 100, 720, 565);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmNew = new JMenuItem("New");
		mnFile.add(mntmNew);
		
		JMenuItem mntmOpen = new JMenuItem("Open...");
		mnFile.add(mntmOpen);
		
		JSeparator separator_4 = new JSeparator();
		mnFile.add(separator_4);
		
		JMenuItem mntmSave = new JMenuItem("Save...");
		mnFile.add(mntmSave);
		
		JMenuItem mntmSaveAs = new JMenuItem("Save As...");
		mnFile.add(mntmSaveAs);
		
		JSeparator separator_5 = new JSeparator();
		mnFile.add(separator_5);
		
		JMenuItem mntmClose = new JMenuItem("Close");
		mntmClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (frame.isDisplayable()) {
	                frame.dispose();
	            }
			}
		});
		mnFile.add(mntmClose);
		
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		JMenuItem mntmUndo = new JMenuItem("Undo");
		mnEdit.add(mntmUndo);
		
		JMenuItem mntmRedo = new JMenuItem("Redo");
		mnEdit.add(mntmRedo);
		
		JSeparator separator_8 = new JSeparator();
		mnEdit.add(separator_8);
		
		JMenuItem mntmSwapHorizontally = new JMenuItem("Swap horizontally");
		mntmSwapHorizontally.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resultImage.setData(Manipulators.flip(resultImage, false));
				wipImage.setData(resultImage.getData());
				panel.getGraphics().drawImage(resultImage, 0,0,null);
			}
		});
		mnEdit.add(mntmSwapHorizontally);
		
		JMenuItem mntmSwapVertically = new JMenuItem("Swap vertically");
		mntmSwapVertically.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resultImage.setData(Manipulators.flip(resultImage, true));
				wipImage.setData(resultImage.getData());
				panel.getGraphics().drawImage(resultImage, 0,0,null);
			}
		});
		mnEdit.add(mntmSwapVertically);
		
		JSeparator separator_9 = new JSeparator();
		mnEdit.add(separator_9);
		
		JMenuItem mntmRotate90l = new JMenuItem("Rotate 90\u00BA left");
		mntmRotate90l.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resultImage = Manipulators.rotate(resultImage, 90);
				wipImage = Manipulators.rotate(wipImage, 90);
				refreshTools();
				int tmp = imgWidth;
				imgWidth = imgHeight;
				imgHeight = tmp;
				Graphics g = panel.getGraphics();
				g.clearRect(0, 0, panel.getWidth(), panel.getHeight());
				g.drawImage(resultImage, 0, 0,null);
			}
		});
		mnEdit.add(mntmRotate90l);
		
		JMenuItem mntmRotate90r = new JMenuItem("Rotate 90\u00BA right");
		mntmRotate90r.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resultImage = Manipulators.rotate(resultImage, 270);
				wipImage = Manipulators.rotate(wipImage, 270);
				refreshTools();
				int tmp = imgWidth;
				imgWidth = imgHeight;
				imgHeight = tmp;
				Graphics g = panel.getGraphics();
				g.clearRect(0, 0, panel.getWidth(), panel.getHeight());
				g.drawImage(resultImage, 0, 0,null);
			}
		});
		mnEdit.add(mntmRotate90r);
		
		JMenuItem mntmRotate180 = new JMenuItem("Rotate 180\u00BA");
		mntmRotate180.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resultImage = Manipulators.rotate(resultImage, 180);
				wipImage = Manipulators.rotate(wipImage, 180);
				refreshTools();
				
				Graphics g = panel.getGraphics();
				g.clearRect(0, 0, panel.getWidth(), panel.getHeight());
				g.drawImage(resultImage, 0, 0,null);
			}
		});
		mnEdit.add(mntmRotate180);
		
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
		
		JToolBar toolBar = new JToolBar();
		frame.getContentPane().add(toolBar, BorderLayout.NORTH);
		
		JButton btnNew = new JButton("New");
		toolBar.add(btnNew);
		
		JButton btnOpen = new JButton("Open...");
		toolBar.add(btnOpen);
		
		JButton btnSave = new JButton("Save...");
		toolBar.add(btnSave);
		
		JButton btnUndo = new JButton("Undo");
		toolBar.add(btnUndo);
		
		JButton btnRedo = new JButton("Redo");
		toolBar.add(btnRedo);
		
		JSeparator separator_10 = new JSeparator();
		toolBar.add(separator_10);
		
		JSeparator separator_12 = new JSeparator();
		toolBar.add(separator_12);
		
		JLabel lblThickness = new JLabel("Thickness");
		toolBar.add(lblThickness);

		SpinnerNumberModel model1 = new SpinnerNumberModel(10, 0, 25, 1);   
		thickness = new JSpinner(model1);
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
		
		JToolBar toolBar_1 = new JToolBar();
		frame.getContentPane().add(toolBar_1, BorderLayout.SOUTH);
		
		JPanel panel_1 = new JPanel();
		toolBar_1.add(panel_1);
		
		final Button fgColorBtn = new Button("");
		fgColorBtn.setActionCommand("");
		fgColorBtn.setBackground(Color.BLACK);
		panel_1.add(fgColorBtn);
		

		final Button bgColorBtn = new Button("");
		bgColorBtn.setActionCommand("");
		bgColorBtn.setBackground(Color.WHITE);
		panel_1.add(bgColorBtn);
		
		JSeparator separator_11 = new JSeparator();
		panel_1.add(separator_11);
		

		Button color1Btn = new Button("");
		color1Btn.setActionCommand("");
		color1Btn.setBackground(Color.BLACK);
		panel_1.add(color1Btn);
		

		Button color2Btn = new Button("");
		color2Btn.setActionCommand("");
		color2Btn.setBackground(Color.WHITE);
		panel_1.add(color2Btn);
		
		Button color3Btn = new Button("");
		color3Btn.setActionCommand("");
		color3Btn.setBackground(Color.RED);
		panel_1.add(color3Btn);
		

		Button color4Btn = new Button("");
		color4Btn.setActionCommand("");
		color4Btn.setBackground(Color.GREEN);
		panel_1.add(color4Btn);


		Button color5Btn = new Button("");
		color5Btn.setActionCommand("");
		color5Btn.setBackground(Color.BLUE);
		panel_1.add(color5Btn);
		

		Button color6Btn = new Button("");
		color6Btn.setActionCommand("");
		color6Btn.setBackground(Color.YELLOW);
		panel_1.add(color6Btn);
		
		Button color7Btn = new Button("");
		color7Btn.setActionCommand("");
		color7Btn.setBackground(Color.CYAN);
		panel_1.add(color7Btn);
		

		Button color8Btn = new Button("");
		color8Btn.setActionCommand("");
		color8Btn.setBackground(Color.PINK);
		panel_1.add(color8Btn);
		

		final Button[] colorBtn = new Button[]{color1Btn,color2Btn,color3Btn,color4Btn,color5Btn,color6Btn,color7Btn,color8Btn};
		
		for(final Button b: colorBtn)
		{
			b.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
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
		
		JToolBar toolBar_2 = new JToolBar();
		toolBar_2.setOrientation(SwingConstants.VERTICAL);
		frame.getContentPane().add(toolBar_2, BorderLayout.WEST);
		
		JButton btnPencil = new JButton("Pencil");
		btnPencil.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeTool(ToolID.Pencil);
			}
		});
		toolBar_2.add(btnPencil);
		
		JButton btnBrush = new JButton("Brush");
		btnBrush.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeTool(ToolID.Brush);
			}
		});
		toolBar_2.add(btnBrush);
		
		JButton btnRubber = new JButton("Rubber");
		btnRubber.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeTool(ToolID.Rubber);
			}
		});
		toolBar_2.add(btnRubber);
		
		JSeparator separator = new JSeparator();
		toolBar_2.add(separator);
		
		JButton btnLine = new JButton("Line");
		btnLine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeTool(ToolID.Line);
			}
		});
		toolBar_2.add(btnLine);
		
		JButton btnRectangle = new JButton("Rectangle");
		btnRectangle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeTool(ToolID.Rectangle);
			}
		});
		toolBar_2.add(btnRectangle);
		
		JButton btnOval = new JButton("Oval");
		btnOval.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeTool(ToolID.Oval);
			}
		});
		toolBar_2.add(btnOval);
		
		JSeparator separator_1 = new JSeparator();
		toolBar_2.add(separator_1);
		
		JButton btnFill = new JButton("Fill");
		btnFill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeTool(ToolID.PaintBucket);
			}
		});
		toolBar_2.add(btnFill);
		
		JSeparator separator_2 = new JSeparator();
		toolBar_2.add(separator_2);
		
		JButton btnSwaph = new JButton("SwapH");
		btnSwaph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resultImage.setData(Manipulators.flip(resultImage, false));
				wipImage.setData(resultImage.getData());
				panel.getGraphics().drawImage(resultImage, 0,0,null);
			}
		});
		toolBar_2.add(btnSwaph);
		
		JButton btnSwapv = new JButton("SwapV");
		btnSwapv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resultImage.setData(Manipulators.flip(resultImage, true));
				wipImage.setData(resultImage.getData());
				panel.getGraphics().drawImage(resultImage, 0,0,null);
			}
		});
		toolBar_2.add(btnSwapv);
		
		JButton btnRotatel = new JButton("Rotate90l");
		btnRotatel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resultImage = Manipulators.rotate(resultImage, 90);
				wipImage = Manipulators.rotate(wipImage, 90);
				refreshTools();
				int tmp = imgWidth;
				imgWidth = imgHeight;
				imgHeight = tmp;
				Graphics g = panel.getGraphics();
				g.clearRect(0, 0, panel.getWidth(), panel.getHeight());
				g.drawImage(resultImage, 0, 0,null);
			}
		});
		toolBar_2.add(btnRotatel);
		
		JButton btnRotater = new JButton("Rotate90r");
		btnRotater.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resultImage = Manipulators.rotate(resultImage, 270);
				wipImage = Manipulators.rotate(wipImage, 270);
				refreshTools();
				int tmp = imgWidth;
				imgWidth = imgHeight;
				imgHeight = tmp;
				
				Graphics g = panel.getGraphics();
				g.clearRect(0, 0, panel.getWidth(), panel.getHeight());
				g.drawImage(resultImage, 0, 0,null);
			}
		});
		toolBar_2.add(btnRotater);
		
		JButton btnRotate = new JButton("Rotate180");
		btnRotate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resultImage = Manipulators.rotate(resultImage, 180);
				wipImage = Manipulators.rotate(wipImage, 180);
				refreshTools();
				
				Graphics g = panel.getGraphics();
				g.clearRect(0, 0, panel.getWidth(), panel.getHeight());
				g.drawImage(resultImage, 0, 0,null);
			}
		});
		toolBar_2.add(btnRotate);
		
		JSeparator separator_3 = new JSeparator();
		toolBar_2.add(separator_3);
		
		JScrollPane scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		panel = new JPanel();
		panel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				//panel.setSize(panel.getWidth()<imgWidth?imgWidth:panel.getWidth(), panel.getHeight()<imgHeight?imgHeight:panel.getHeight());
				panel.getGraphics().drawImage(resultImage, 0, 0,null);				
			}
		});
		panel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				wipImage.setData(resultImage.getData());

				currTool.mouseHold(e.getX(), e.getY());
				
				panel.getGraphics().drawImage(wipImage, 0, 0,null);
			}
		});
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				currTool.mousePressed(e.getX(), e.getY());
				
				panel.getGraphics().drawImage(resultImage, 0, 0,null);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if(mode != ToolID.PaintBucket)
					currTool.mouseReleased(e.getX(), e.getY());
				else
					pBucket.fillWithColor(e.getX(), e.getY(),resultImage);
				panel.getGraphics().drawImage(resultImage, 0, 0,null);
			}
		});
		scrollPane.setViewportView(panel);

		frame.setVisible(true);
		
		resultImage = (BufferedImage) panel.createImage(imgWidth, imgHeight); 
		wipImage = (BufferedImage) panel.createImage(imgWidth, imgHeight); 
		Graphics2D gc = (Graphics2D)resultImage.getGraphics();
        gc.setColor(bgColor);
        gc.fillRect(0, 0, imgWidth, imgHeight); // fill in background
            

		brush = new Brush(resultImage.getGraphics(),wipImage.getGraphics());
		rubber = new Rubber(resultImage.getGraphics(),wipImage.getGraphics());
		line = new Line(resultImage.getGraphics(),wipImage.getGraphics());
		pencil = new Pencil(resultImage.getGraphics(),wipImage.getGraphics());
		rect = new Rectangle(resultImage.getGraphics(),wipImage.getGraphics());
		oval = new Oval(resultImage.getGraphics(),wipImage.getGraphics());
		pBucket = new PaintBucket(resultImage.getGraphics(),wipImage.getGraphics());
		
		currTool = brush;
		
		mode = ToolID.Brush;
		

	}

	public void changeTool(ToolID t) {

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
	
	public void refreshTools() {
		brush.refreshGraphics(resultImage.getGraphics(),wipImage.getGraphics());
		line.refreshGraphics(resultImage.getGraphics(),wipImage.getGraphics());
		pencil.refreshGraphics(resultImage.getGraphics(),wipImage.getGraphics());
		rect.refreshGraphics(resultImage.getGraphics(),wipImage.getGraphics());
		oval.refreshGraphics(resultImage.getGraphics(),wipImage.getGraphics());
		rubber.refreshGraphics(resultImage.getGraphics(),wipImage.getGraphics());
		pBucket.refreshGraphics(resultImage.getGraphics(),wipImage.getGraphics());
		refreshColors();
	}
	public void refreshColors() {
		brush.changeColors(fgColor, bgColor);
		line.changeColors(fgColor, bgColor);
		pencil.changeColors(fgColor, bgColor);
		rect.changeColors(fgColor, bgColor);
		oval.changeColors(fgColor, bgColor);
		rubber.changeColors(fgColor, bgColor);
		pBucket.changeColors(fgColor, bgColor);
	}
	
	public Color getColorAt(int x, int y) {
		return new Color(resultImage.getRGB(x, y));
	}
}
