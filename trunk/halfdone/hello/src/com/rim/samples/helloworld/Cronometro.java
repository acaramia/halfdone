
package com.rim.samples.helloworld;

import java.util.Timer;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.DateField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.NumericChoiceField;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.container.MainScreen;

/*
 * BlackBerry applications that provide a user interface
 * must extend UiApplication.
 */
public class Cronometro extends UiApplication {
	public static void main(String[] args) {
		// create a new instance of the application
		// and start the application on the event thread
		Cronometro theApp = new Cronometro();
		theApp.enterEventDispatcher();
	}

	public Cronometro() {
		// display a new screen
		pushScreen(new CronometroScreen());
	}
}

// create a new screen that extends MainScreen, which provides
// default standard behavior for BlackBerry applications
final class CronometroScreen extends MainScreen implements FieldChangeListener {
	LabelField labelSecondi;
	ButtonField btStart;
	ButtonField btStop;
	NumericChoiceField myNumericChoice;
	Timer myTimer;
	Tempo myTask;

	public CronometroScreen() {
		// invoke the MainScreen constructor
		super();
		// add a title to the screen
		LabelField title = new LabelField("Crono v0.2", LabelField.ELLIPSIS
				| LabelField.USE_ALL_WIDTH);
		setTitle(title);
		// add the text "Hello World!" to the screen
		add(new RichTextField("Cronometro"));
		labelSecondi = new LabelField("secondi", LabelField.ELLIPSIS);
		add(labelSecondi);
		DateField dateField = new DateField("Date: ", System.currentTimeMillis(), net.rim.device.api.ui.component.DateField.TIME);
		dateField.setEditable(false);
		add(dateField);
		
		myNumericChoice = new NumericChoiceField( "Durata: ", 5,60,5,2);
		add(myNumericChoice);
		
		btStart = new ButtonField("Start");
		btStart.setChangeListener(this);
		btStop = new ButtonField("Stop");
		btStop.setChangeListener(this);
		add(btStart);
		add(btStop);
		myTimer = new Timer();
		myTask = new Tempo(this);
	}

	public boolean onClose() {
		//Dialog.alert("Fine");
		System.exit(0);
		return true;
	}

	public void fieldChanged(Field field, int context) {
		if (field.getIndex() == btStart.getIndex()) {
			labelSecondi.setText("partito");
			myTask.cancel();
			myTask = new Tempo(this);
			myTask.myStart(myNumericChoice.getSelectedValue());
			try {
				myTimer.scheduleAtFixedRate(myTask, 0, 500);
			} catch (Exception e) {
				labelSecondi.setText(e.getMessage());
			}
		} else {
			myTask.cancel();
			labelSecondi.setText("fermato");
		}
	}
	
	public void stampa(final String s) {
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				// Perform screen changes here.
				labelSecondi.setText(s);
				// Calling invalidate() on your screen forces the paint
				// method to be called.
				// this.invalidate();
			}
		});
	}

	public void stop() {
		stampa("Fine!");
		stampa(suona());
	}
	
	public String suona(){
		Suona s=new Suona();
		return s.myPlay();
	}
}