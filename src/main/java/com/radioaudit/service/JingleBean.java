package com.radioaudit.service;

import java.io.IOException;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.DecoderException;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.SampleBuffer;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.musicg.wave.Wave;
import com.musicg.wave.WaveHeader;
import com.radioaudit.domain.dao.JingleDAO;
import com.radioaudit.domain.dao.RadioDAO;
import com.radioaudit.domain.dao.UserDAO;
import com.radioaudit.domain.model.Jingle;
import com.radioaudit.domain.model.Radio;
import com.radioaudit.domain.model.User;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
public class JingleBean {

	private final Logger LOGGER = LoggerFactory.getLogger(JingleBean.class);

	@Autowired
	private JingleDAO jingleDAO;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private RadioDAO radioDAO;

	@SuppressWarnings("resource")
	@Transactional(readOnly = false)
	public boolean createCommercial(String username, String radioCode, FileItem fileIitem) {

		try {

			String fileName = fileIitem.getName();

			LOGGER.info("Uploading jingle with name: {}", fileName);

			boolean jingleNotExist = this.jingleDAO.notExistCommercial(fileName);

			if (jingleNotExist) {

				Wave wave = null;
				WaveHeader waveHeader = null;

				if (StringUtils.endsWith(fileName, ".mp3")) {
					// convert to PCM
					Bitstream bitstream = new Bitstream(fileIitem.getInputStream());
					Decoder decoder = new Decoder();
					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
					Header header = bitstream.readFrame();
					SampleBuffer output = null;
					while (header != null && header.framesize > 0) {
						output = (SampleBuffer) decoder.decodeFrame(header, bitstream);
						byte[] frameBytes = toByteArray(output.getBuffer(), 0, output.getBufferLength());
						byteArrayOutputStream.write(frameBytes, 0, output.getBufferLength() * 2);
						header = bitstream.readFrame();
					}
					bitstream.closeFrame();

					waveHeader = new WaveHeader();
					waveHeader.setChannels(output.getChannelCount());
					waveHeader.setSampleRate(output.getSampleFrequency());
					wave = new Wave(waveHeader, byteArrayOutputStream.toByteArray());
				} else {
					// create PCM WAV
					wave = new Wave(fileIitem.getInputStream());
					waveHeader = wave.getWaveHeader();
				}

				int duration = wave.size() / waveHeader.getByteRate();
				LOGGER.info("Create jingle: {} with lenght: {} seconds", fileName, wave.size());
				LOGGER.info("Jingle info: {}", wave.toString());

				String[] fullName = StringUtils.split(fileName, '.');

				Jingle commercial = new Jingle();
				commercial.setName(fileName);
				commercial.setDuration(duration);
				commercial.setFormat(fullName[1]);

				// load user
				User user = this.userDAO.readByUsername(username);
				commercial.setUser(user);
				commercial.setFingerprint(wave.getFingerprint());

				// save commercial
				this.jingleDAO.save(commercial);

				// load radio
				Radio radio = this.radioDAO.loadByCode(radioCode);
				radio.getSuscribeJingles().add(commercial);
				this.radioDAO.save(radio);
			} else {
				System.out.println("Commercial with name: " + fileName + " already exist in database");
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (BitstreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DecoderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
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

}
