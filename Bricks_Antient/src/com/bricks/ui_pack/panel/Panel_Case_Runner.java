package com.bricks.ui_pack.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.android.ddmlib.IDevice;
import com.bricks.Global_Observer;
import com.bricks.Main_Entry;
import com.bricks.ui_pack.Constants_UI;
import com.bricks.tools.Utils_Property;
/**
 *
 * @author DraLastat
 */

public class Panel_Case_Runner extends JPanel implements Global_Observer {
	private static final long serialVersionUID = 1L;
	private static JPanel panelResult;
	private static JPanel panelData;
	public static JPanel caserunPanelMain;
	private static JPanel caserunPanelResult;
	private static JPanel caserunPanelData;
	
	public Panel_Case_Runner() {
		initialize();
		addComponent();
		addListener();
	}

	private void initialize() {
		this.setBackground(Constants_UI.MAIN_BACK_COLOR);
		this.setLayout(new BorderLayout());
		caserunPanelData = new Panel_Case_Runner_Data();
	}

	private void addComponent() {

		this.add(getUpPanel(), BorderLayout.NORTH);
		this.add(getCenterPanel(), BorderLayout.CENTER);
	}
	
	private JPanel getUpPanel() {
		JPanel panelUp = new JPanel();
		panelUp.setBackground(Constants_UI.MAIN_BACK_COLOR);
		panelUp.setLayout(new FlowLayout(FlowLayout.LEFT, Constants_UI.MAIN_H_GAP, 5));

		JLabel labelTitle = new JLabel(Utils_Property.getProperty("bricks.ui.caserun.title"));
		labelTitle.setFont(Constants_UI.FONT_TITLE);
		labelTitle.setForeground(Constants_UI.TOOL_BAR_BACK_COLOR);
		panelUp.add(labelTitle);

		return panelUp;
	}
	
	private JPanel getCenterPanel() {
		//
		JPanel panelCenter = new JPanel();
		panelCenter.setBackground(Constants_UI.MAIN_BACK_COLOR);
		panelCenter.setLayout(new BorderLayout());

		JPanel panelList = new JPanel();
		Dimension preferredSize = new Dimension(245, Constants_UI.MAIN_WINDOW_HEIGHT);
		panelList.setPreferredSize(preferredSize);
		panelList.setBackground(new Color(62, 62, 62));
		panelList.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

		panelResult = new JPanel();
		panelResult.setBackground(new Color(120, 165, 202));
		panelResult.setLayout(new FlowLayout(FlowLayout.LEFT, 30, 13));
		Dimension preferredSizeListItem = new Dimension(245, 48);
		panelResult.setPreferredSize(preferredSizeListItem);
		panelData = new JPanel();
		panelData.setBackground(Constants_UI.TOOL_BAR_BACK_COLOR);
		panelData.setLayout(new FlowLayout(FlowLayout.LEFT, 30, 13));
		panelData.setPreferredSize(preferredSizeListItem);

		JLabel labelOption = new JLabel(Utils_Property.getProperty("bricks.ui.caserun.result"));
		JLabel labelAbout = new JLabel(Utils_Property.getProperty("bricks.ui.caserun.data"));
		Font fontListItem = new Font(Utils_Property.getProperty("ds.ui.font.family"), 0, 15);
		labelOption.setFont(fontListItem);
		labelAbout.setFont(fontListItem);
		labelOption.setForeground(Color.white);
		labelAbout.setForeground(Color.white);
		panelResult.add(labelOption);
		panelData.add(labelAbout);

		panelList.add(panelResult);
		panelList.add(panelData);

		caserunPanelMain = new JPanel();
		caserunPanelMain.setBackground(Constants_UI.MAIN_BACK_COLOR);
		caserunPanelMain.setLayout(new BorderLayout());

		panelCenter.add(panelList, BorderLayout.WEST);
		panelCenter.add(caserunPanelMain, BorderLayout.CENTER);

		return panelCenter;
	}

	private void addListener() {
		panelResult.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				panelResult.setBackground(new Color(120, 165, 202));
				panelData.setBackground(Constants_UI.TOOL_BAR_BACK_COLOR);

				Panel_Case_Runner.caserunPanelMain.removeAll();
				if (caserunPanelResult != null)
					Panel_Case_Runner.caserunPanelMain.add(caserunPanelResult);
				Main_Entry.caserunPanel.updateUI();
			}
		});

		panelData.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				panelData.setBackground(new Color(120, 165, 202));
				panelResult.setBackground(Constants_UI.TOOL_BAR_BACK_COLOR);

				Panel_Case_Runner.caserunPanelMain.removeAll();
				Panel_Case_Runner.caserunPanelMain.add(caserunPanelData);
				Main_Entry.caserunPanel.updateUI();

			}
		});

	}

	@Override
	public void frameImageChange(BufferedImage image) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ADBChange(IDevice[] devices) {
		if (devices[0] != null) {
			caserunPanelResult = new Panel_Case_Result(devices[0]);
			caserunPanelMain.add(caserunPanelResult);
		} else {
			Panel_Case_Runner.caserunPanelMain.removeAll();
			Main_Entry.caserunPanel.updateUI();
			caserunPanelResult = null;
		}
	}
}
