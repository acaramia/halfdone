package com.rim.samples.helloworld;

import java.io.IOException;
import java.util.Enumeration;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.media.Control;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.media.control.VolumeControl;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.media.control.AudioPathControl;

public class Suona implements PlayerListener {
	public static final String PATH = "file:///store/samples/ringtones/";
	// private ArraysList ringtones;
	public Player player;

	// private void getRingtones() {
	// Enumeration ringtonesNames = null;
	// try {
	// FileConnection fc = (FileConnection) Connector.open(PATH);
	// ringtonesNames = fc.list();
	// } catch(IOException e) {
	// Dialog.alert("Exception:" + e);
	// }
	// while(ringtonesNames.hasMoreElements()) {
	// String sound = (String) ringtonesNames.nextElement();
	// ringtones.add(sound);
	// }
	// }

	private String getRingtones() {
		Enumeration ringtonesNames = null;
		String ringtones = null;
		try {
			FileConnection fc = (FileConnection) Connector.open(PATH);
			ringtonesNames = fc.list();
		} catch (IOException e) {
			Dialog.alert("Exception:" + e);
		}
		ringtones="Tune_FurElise.mid";
		while (ringtonesNames.hasMoreElements()) {
			String sound = (String) ringtonesNames.nextElement();
			if(sound.startsWith("Notifier"))
					ringtones = sound;
		}
		return ringtones;		
	}

	public void playSound(String soundName) {
		try {
			/** create player to play the specified tune */
			player = Manager.createPlayer(soundName);
			player.addPlayerListener(this);
			player.realize();
			player.prefetch();
			/**
			 * ensure the tune goes to the speaker even when there is something
			 * in the headset jack
			 */
//			Control controls[] = player.getControls();
//			for (int i = controls.length - 1; i >= 0; i--) {
//				if (controls[i] instanceof AudioPathControl) {
//					AudioPathControl apc = (AudioPathControl) controls[i];
//					apc.setAudioPath(AudioPathControl.AUDIO_PATH_HANDSFREE);
//				}
//			}
//			VolumeControl vc = (VolumeControl) player
//					.getControl("VolumeControl");
//			vc.setLevel(80);
			player.start();
		} catch (IllegalArgumentException e) {
			Dialog.alert("Exception: " + e);
		} catch (IOException e) {
			Dialog.alert("Exception: " + e);
		} catch (MediaException e) {
			Dialog.alert("Exception: " + e);
		}
	}

	public void playerUpdate(Player player, String event, Object eventData) {
	}

	public String myPlay() {
		String nome;
		//nome=this.getRingtones();
		nome="Notifier_Lucid.mp3";
		//playSound(PATH + ringtones.get(0));
		playSound(PATH + nome);
		//playSound(PATH + );
		return nome;
	}
}
