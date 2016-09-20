package application;

import java.awt.print.PrinterException;
import java.text.MessageFormat;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

class PrintingTask extends SwingWorker<Object, Object> {
	private JTextArea text = new JTextArea();

	public PrintingTask(String data) {
		//set report string as the text for the JTextArea
		text.setText(data);
		this.execute();
	}

	public static void print(String data) {
		new PrintingTask(data);
	}

	protected Object doInBackground() {
		try {
			PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
			aset.add(OrientationRequested.LANDSCAPE);//Set orientation to landscape for the report
			text.print(new MessageFormat(""), new MessageFormat(""), true, null, aset, true);
		} catch (PrinterException ex) {
			AlertBox.display("Print", "It didn't work :-(");
		}
		return null;
	}
}