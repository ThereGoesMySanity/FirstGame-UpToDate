package Audio;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.sound.sampled.*;

public class AudioPlayer {
	
	private Clip clip;
	private boolean songBool;
	private MediaPlayer song;
	private FloatControl volume;
	public AudioPlayer(String s){
		try{
			String[] split = s.split(".");
			if(split[split.length-1] == "mp3"){
				Media hit = new Media(s);
				song = new MediaPlayer(hit);
				songBool = true;
			}
			else{
				InputStream is = getClass().getResourceAsStream(s);
				InputStream bufferedIn = new BufferedInputStream(is);
				AudioInputStream ais = AudioSystem.getAudioInputStream(bufferedIn);
				AudioFormat baseFormat = ais.getFormat();
				AudioFormat decodeFormat = new AudioFormat(
						AudioFormat.Encoding.PCM_SIGNED,
						baseFormat.getSampleRate(),
						16,
						baseFormat.getChannels(),
						baseFormat.getChannels()*2,
						baseFormat.getSampleRate(),
						false);
				AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat, ais);
				clip = AudioSystem.getClip();
				clip.open(dais);
				songBool = false;
				volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			}
		}catch(Exception e){e.printStackTrace();}
	}
	public void play(){
		if((clip == null&&song==null)||clip.isRunning()||song.getStatus() == MediaPlayer.Status.PLAYING)return;
		stop();
		if(songBool&&song != null){
			song.play();
		}else{
			clip.setFramePosition(0);
			clip.start();
		}
	}
	public void setVolume(float f){
		volume.setValue(f);
	}
	public void stop(){
		if(clip.isRunning())clip.stop();
		if(song.getStatus() == MediaPlayer.Status.PLAYING)song.stop();
	}
	public void close(){
		stop();
		if(songBool){
			song.dispose();
		}else{
			clip.close();
		}
	}
	public boolean isRunning(){
		if(songBool){
			return song.getStatus() == MediaPlayer.Status.PLAYING;
		}else{
			return clip.isRunning();
		}
	}
}






