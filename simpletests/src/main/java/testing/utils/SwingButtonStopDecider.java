package testing.utils;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.loadcoder.load.scenario.StopDecision;

public class SwingButtonStopDecider implements StopDecision{

	boolean stop = false;
	public SwingButtonStopDecider (){
		final JFrame parent = new JFrame();
		JButton button = new JButton();

		button.setText("Stop Test");
		parent.add(button);
		parent.pack();
		parent.setVisible(true);

		button.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				stop = true;
			}
		});
	}

	@Override
	public boolean stopLoad(long startTime, long timesExecuted) {
		// TODO Auto-generated method stub
		return stop;
	}
}
