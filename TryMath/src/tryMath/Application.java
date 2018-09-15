package tryMath;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Application extends JFrame implements ActionListener, ChangeListener {

	private static final long serialVersionUID = 1L;

	private float cur_time, total_time;
	private int num_times;

	private int num_a = 1, num_b = 1, answer = 0;

	String[] diff_str = new String[] { "Easy Multiply", "Easy Divide", "Hard Multiply", "Hard Divide" };

	JTextField time, numbers, user_answer;

	JSpinner difficulty;

	Timer timer;

	private void outputMath(char type) {
		// calculate the answer
		if (type == '*') {
			answer = num_a * num_b;
		} else {
			answer = num_a * num_b;

			int temp = answer;
			answer = num_a;
			num_a = temp;
		}

		// make the string the appropriate length
		String output = Integer.toString(num_a) + " " + type + " " + Integer.toString(num_b) + " = ";

		if (output.length() > 8) {
			output = Integer.toString(num_a) + type + Integer.toString(num_b) + " = ";
		}

		if (output.length() > 8) {
			output = Integer.toString(num_a) + type + Integer.toString(num_b) + "= ";
		}

		while (output.length() < 10) {
			output = " " + output;
		}

		numbers.setText(output);
	}

	void changeNumbers(int cap) {
		num_a = (int) (Math.random() * cap + 1);
		num_b = (int) (Math.random() * cap + 1);
	}

	private void newNumbers() {
		switch ((String) difficulty.getValue()) {
		case "Easy Multiply":
			changeNumbers(9);
			outputMath('*');
			break;
		case "Easy Divide":
			changeNumbers(9);
			outputMath('/');
			break;
		case "Hard Multiply":
			changeNumbers(99);
			outputMath('*');
			break;
		case "Hard Divide":
			changeNumbers(99);
			outputMath('/');
			break;
		}
	}

	private boolean checkAnswer() {
		// if(!user_answer.getText().matches(".*\\d+.*")){
		// return false;
		// }

		if (answer == Integer.parseInt(user_answer.getText())) {
			user_answer.setText("");
			return true;
		}

		return false;
	}

	private void outputTime() {
		final Color grn = new Color(0, 200, 0);
		
		if (cur_time > total_time / num_times) {
			time.setForeground(Color.RED);
		} else {
			time.setForeground(grn);
		}

		String timestr = Float.toString(Math.round(cur_time * 10) / 10.0f);
		String avgstr = Float.toString(Math.round((total_time / num_times) * 10) / 10.0f);

		if (timestr.length() < 3 || (timestr.length() < 4 && cur_time >= 10))
			timestr += "0";
		
		timestr = " " + timestr + "s ";
		avgstr = "Average: " + avgstr + "s";
		
		while(timestr.length() + avgstr.length() <  25){
			timestr += " ";
		}

		time.setText(timestr + avgstr);
	}

	private void addTime() {
		num_times++;
		total_time += cur_time;

		cur_time = 0;
	}

	private void resetAllTime() {
		cur_time = 0;
		total_time = 0;
	}

	public Application() {
		setResizable(false);
		setFocusable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setSize(160, 160);

		Font title_font = new Font("Serif", Font.ITALIC, 24);
		Font entry_font = new Font(Font.MONOSPACED, Font.PLAIN, 16);
		Font small_font = new Font(Font.MONOSPACED, Font.PLAIN, 10);

		// VERTICAL AREA
		// title area
		JTextField title = new JTextField();
		title.setHorizontalAlignment(JTextField.CENTER);
		title.setEditable(false);
		title.setBorder(null);

		title.setFont(title_font);

		title.setText("TryMath!");

		// difficulty selection spinner
		difficulty = new JSpinner(new SpinnerListModel(diff_str));
		difficulty.setFocusable(false);

		difficulty.addChangeListener(this);

		JComponent difficulty_editor = difficulty.getEditor();

		if (difficulty_editor instanceof JSpinner.DefaultEditor) {
			JSpinner.DefaultEditor dede = (JSpinner.DefaultEditor) difficulty_editor;
			dede.getTextField().setEditable(false);
			dede.getTextField().setHorizontalAlignment(JTextField.CENTER);
			dede.getTextField().setFont(entry_font);
		}

		// time display and timer
		time = new JTextField();
		time.setHorizontalAlignment(JTextField.LEFT);
		time.setEditable(false);
		time.setBorder(null);
		time.setFont(small_font);

		outputTime();

		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				cur_time += 0.1f;
				outputTime();
			}
		}, 100, 100);

		// HORIZONTAL AREA
		// numbers text box
		numbers = new JTextField();
		numbers.setHorizontalAlignment(JTextField.CENTER);
		numbers.setEditable(false);
		numbers.setBorder(null);
		numbers.setFont(entry_font);

		newNumbers();

		// user answer field
		user_answer = new JTextField();
		user_answer.setHorizontalAlignment(JTextField.CENTER);
		user_answer.setFont(entry_font);

		user_answer.addActionListener(this);

		// PANEL SETUP
		// basic panels
		JPanel vert_panel = new JPanel();
		vert_panel.setLayout(new BoxLayout(vert_panel, BoxLayout.PAGE_AXIS));
		vert_panel.setSize(100, 100);

		JPanel hor_panel = new JPanel();
		hor_panel.setLayout(new BorderLayout());

		JPanel v_padding_a = new JPanel();
		v_padding_a.setSize(new Dimension(100, 50));

		JPanel v_padding_b = new JPanel();
		v_padding_b.setSize(new Dimension(100, 50));

		// add everything
		hor_panel.add(v_padding_a, BorderLayout.NORTH);
		hor_panel.add(numbers, BorderLayout.WEST);
		hor_panel.add(user_answer, BorderLayout.CENTER);
		hor_panel.add(time, BorderLayout.SOUTH);

		vert_panel.add(title);
		vert_panel.add(difficulty);
		vert_panel.add(hor_panel);

		add(vert_panel);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				Application app = new Application();
				app.setLocationRelativeTo(null);
				app.setVisible(true);
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (checkAnswer()) {
			newNumbers();
			addTime();
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		newNumbers();
		resetAllTime();
	}
}
