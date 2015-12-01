package com.radioaudit.domain.thread;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import net.sourceforge.jaad.aac.Decoder;
import net.sourceforge.jaad.aac.SampleBuffer;
import net.sourceforge.jaad.adts.ADTSDemultiplexer;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.musicg.wave.Wave;
import com.musicg.wave.WaveHeader;
import com.radioaudit.domain.dao.RadioDAO;

public class AacDecorder extends AbstractThread {

	private final Logger LOGGER = LoggerFactory.getLogger(AacDecorder.class);

	private String radioUrl;
	private BlockingQueue<Wave> waveBuffer;

	/**
	 * Public Constructor
	 * 
	 * @param radioCode
	 * @param radioDAO
	 * @param logQueuingDAO
	 * @param urlConnection
	 * @param waveBuffer
	 */
	public AacDecorder(String radioCode, RadioDAO radioDAO, String radioUrl, BlockingQueue<Wave> waveBuffer) {
		this.radioCode = radioCode;
		this.radioDAO = radioDAO;
		this.waveBuffer = waveBuffer;
		this.radioUrl = radioUrl;
	}

	@SuppressWarnings("resource")
	@Override
	public void run() {

		boolean active = true;

		do {
			try {

				URLConnection urlConnection = new URL(this.radioUrl).openConnection();
				urlConnection.connect();

				Thread.sleep(100);

				// create connection
				InputStream inputStream = urlConnection.getInputStream();
				ADTSDemultiplexer adts = new ADTSDemultiplexer(inputStream);
				Decoder decoder = new Decoder(adts.getDecoderSpecificInfo());
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

				int frames = 0;
				while (active) {

					SampleBuffer sampleBuffer = new SampleBuffer();
					decoder.decodeFrame(adts.readNextFrame(), sampleBuffer);
					frames += 1;
					byte[] pcmAudio = sampleBuffer.getData();

					byteArrayOutputStream.write(pcmAudio);

					if (frames >= 50) {
						WaveHeader waveHeader = new WaveHeader();
						waveHeader.setChannels(sampleBuffer.getChannels());
						waveHeader.setSampleRate(sampleBuffer.getSampleRate());
						waveHeader.setBitsPerSample(sampleBuffer.getBitsPerSample());
						Wave wave = new Wave(waveHeader, byteArrayOutputStream.toByteArray());
						boolean offer = this.waveBuffer.offer(wave, 5, TimeUnit.SECONDS);
						if (offer) {
							byteArrayOutputStream = new ByteArrayOutputStream();
						} else {
							throw new IllegalArgumentException("Wave buffer is full");
						}
						frames = 0;
					}
					active = super.isActive();
				}

			} catch (IOException e) {
				LOGGER.warn("IOException traying to set up connection for radio {}", radioCode, e);
			} catch (InterruptedException e1) {
				LOGGER.warn("InterruptedException for radio {}", radioCode, e1);
			} catch (IllegalArgumentException illegalArgumentException) {
				LOGGER.warn("IllegalArgumentException  for radio {}", radioCode, illegalArgumentException);
			} catch (Exception exception) {
				LOGGER.error("Unexpected exception ", exception);
			}

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				LOGGER.error("InterruptedException ", e);
			}

			active = super.isActive();

		} while (active);

		// mark as disconnected
		super.disconnect();
	}
}
