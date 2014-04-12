

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ImageSize {

    private JFrame parent;
	private JDialog dialog;
	
	private JSpinner textWidth;
	private JSpinner textHeight;
	
	private Point dimensions;

	public ImageSize(JFrame parent) {
		this.parent = parent;
	}
	
	/**
	 * Launch the application.
	 */
	public void display() {
        final int DWIDTH = 200;
        final int DHEIGHT = 180;
		
		dialog = new JDialog(parent, "Image Size", true);
        dialog.setSize (DWIDTH, DHEIGHT);
        dialog.setResizable (false);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setContentPane (createContent());
		dialog.setVisible(true);
	}

	/**
	 * Create the dialog.
	 */
	private JPanel createContent() {
		JPanel contentPanel = new JPanel();
		JPanel panel_0;
		JPanel panel_1;
		JPanel panel_2;
		
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(new GridLayout(4, 1, 0, 0));
	
		panel_0 = new JPanel();
		JLabel label = new JLabel("Set image size (in pixels):");
		panel_0.add(label, BorderLayout.NORTH);
		label.setVerticalAlignment(SwingConstants.TOP);
		
		panel_1 = new JPanel();
		JLabel lblWidth = new JLabel("Width:");
		panel_1.add(lblWidth);
		
		SpinnerNumberModel model = new SpinnerNumberModel(500, 0, 10000, 1);   
		textWidth = new JSpinner(model);
		JComponent field = ((JSpinner.DefaultEditor) textWidth.getEditor());
	    Dimension prefSize = field.getPreferredSize();
	    prefSize = new Dimension(100, prefSize.height);
	    field.setPreferredSize(prefSize);
		panel_1.add(textWidth);
		
		panel_2 = new JPanel();

		JLabel lblHeight = new JLabel("Height:");
		panel_2.add(lblHeight);

		model = new SpinnerNumberModel(400, 0, 10000, 1);   
		textHeight = new JSpinner(model);
		field = ((JSpinner.DefaultEditor) textHeight.getEditor());
	    prefSize = field.getPreferredSize();
	    prefSize = new Dimension(100, prefSize.height);
	    field.setPreferredSize(prefSize);
		panel_2.add(textHeight);
		contentPanel.add(panel_0);
		contentPanel.add(panel_1);
		contentPanel.add(panel_2);
	
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		contentPanel.add(buttonPane, BorderLayout.SOUTH);
		
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dimensions = new Point((int)textWidth.getValue(),(int)textHeight.getValue());
				
				dialog.dispose();
			}
		});
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dimensions = null;
				
				dialog.dispose();
				
			}
		});
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
		
		
		return contentPanel;
	}
	
	public Point returnDimensions() {
		return dimensions;
	}

}
