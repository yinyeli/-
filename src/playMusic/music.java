package playMusic;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class music implements Runnable {

	/*
     * play playMusic.music
	 */

    private AudioInputStream in;
    private AudioFormat format;
    private DataLine.Info info;
    private SourceDataLine sline;
    private int length;
    private byte[] data;
    public static boolean stop;

    public music() throws Exception {
        in = AudioSystem.getAudioInputStream(new File("muc\\" + 1 + ".wav"));
        format = in.getFormat();
        info = new DataLine.Info(SourceDataLine.class, format, AudioSystem.NOT_SPECIFIED);
        sline = (SourceDataLine) (AudioSystem.getLine(info));
        data = new byte[1024];
        //
        this.stop = false;
    }

    public void run() {
        try {

            sline.open(format);

            sline.start();
            while (true) {

                while ((length = in.read(data)) != -1) {
                    synchronized (this) {

                        while (stop) {
                            wait(500);
                        }
                    }

                    sline.write(data, 0, length);
                }
                try {
                    in = AudioSystem.getAudioInputStream(new File("1.wav"));
                } catch (UnsupportedAudioFileException e) {
                    e.printStackTrace();
                }
            }
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

