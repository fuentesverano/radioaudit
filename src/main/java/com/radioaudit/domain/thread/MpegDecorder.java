package com.radioaudit.domain.thread;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.SampleBuffer;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.musicg.wave.Wave;
import com.musicg.wave.WaveFileManager;
import com.musicg.wave.WaveHeader;
import com.radioaudit.domain.dao.RadioDAO;
import com.radioaudit.domain.model.Radio;

public class MpegDecorder extends AbstractThread {

	private static final float FIVE_SECONDS = 5000f;

	private final Logger LOGGER = LoggerFactory.getLogger(MpegDecorder.class);

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
	public MpegDecorder(String radioCode, RadioDAO radioDAO, String radioUrl, BlockingQueue<Wave> waveBuffer) {
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
				Bitstream bitstream = new Bitstream(inputStream);
				Decoder decoder = new Decoder();
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

				LOGGER.info("Set up connection for radio {} successfully..", radioCode);

				// take first header
				Header firstHeader = bitstream.readFrame();
				LOGGER.info(firstHeader.toString());

				Radio radio = this.radioDAO.loadByCode(radioCode);
				int bitrate = firstHeader.bitrate() / 1000;
				if (bitrate != radio.getBitrate()) {
					// update radio bit rate
					radio.setBitrate(bitrate);
					radioDAO.save(radio);
				}

				float ms = 0f;
				while (active) {
					Header header = bitstream.readFrame();
					SampleBuffer output = (SampleBuffer) decoder.decodeFrame(header, bitstream);

					byte[] frameBytes = toByteArray(output.getBuffer(), 0, output.getBufferLength());
					byteArrayOutputStream.write(frameBytes, 0, output.getBufferLength() * 2);
					ms += header.ms_per_frame();

					if (ms >= FIVE_SECONDS) {

						WaveHeader waveHeader = new WaveHeader();
						waveHeader.setSampleRate(firstHeader.frequency());
						waveHeader.setChannels(1);
						Wave wave = new Wave(waveHeader, byteArrayOutputStream.toByteArray());
						boolean offer = this.waveBuffer.offer(wave, 5, TimeUnit.SECONDS);

						if (offer) {
							byteArrayOutputStream = new ByteArrayOutputStream();
						} else {
							throw new IllegalArgumentException("Error wave list is full - " + this.getName());
						}
						ms = 0f;
					}

					bitstream.closeFrame();
					active = super.isActive();
				}

			} catch (BitstreamException be) {
				LOGGER.warn("BitstreamException in ratio {}", radioCode, be);
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

	private byte[] toByteArray(short[] samples, int offs, int len) {
		byte[] b = getByteArray(len * 2);
		int idx = 0;
		short s;
		while (len-- > 0) {
			s = samples[offs++];
			b[idx++] = (byte) s;
			b[idx++] = (byte) (s >>> 8);
		}
		return b;
	}

	private byte[] getByteArray(int length) {
		byte[] byteBuf = new byte[4096];
		if (byteBuf.length < length) {
			byteBuf = new byte[length + 1024];
		}
		return byteBuf;
	}

	@SuppressWarnings("unused")
	private void saveWave(Wave wave) {
		WaveFileManager waveFileManager = new WaveFileManager();
		waveFileManager.setWave(wave);
		LOGGER.info(wave.toString());
		waveFileManager.saveWaveAsFile("/home/fabro/Desktop/" + Long.toString(System.currentTimeMillis()) + ".wav");
	}

}
