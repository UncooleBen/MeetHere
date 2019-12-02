package com.webapp.service.filesystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import com.webapp.model.Message;

public class FaoImpl implements Fao {

	protected File createFile(String path) {
		return new File(path);
	}

	protected File createFile(File dir, String filename) {
		return new File(dir, filename);
	}

	@Override
	public boolean storeImage(Message message, MultipartFile image) {
		String path = System.getenv("TEMP") + "\\timeline_imgs";
		File dir = createFile(path);
		if (!dir.exists()) {
			dir.mkdir();
		}
		String filename = message.get_uuid().toString() + ".jpg";
		File actualFile = createFile(dir, filename);
		try {
			if (!actualFile.exists()) {
				actualFile.createNewFile();
			}
			image.transferTo(actualFile);
			return true;
		} catch (IOException ioE) {
			ioE.printStackTrace(System.err);
			System.out.println("Exception occurs during I/O.");
		}
		return false;
	}

	@Override
	public byte[] convertToByteArray(String filename) {
		try {
			FileInputStream fis = new FileInputStream(new File(System.getenv("TEMP") + "/timeline_imgs/", filename));
			return IOUtils.toByteArray(fis);
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace(System.err);
		} catch (IOException ioe) {
			ioe.printStackTrace(System.err);
		}
		return null;
	}
}
