package com.webapp.service.filesystem;

import org.springframework.web.multipart.MultipartFile;

import com.webapp.model.Message;

public interface Fao {

	public boolean storeImage(Message message, MultipartFile image);

	public byte[] convertToByteArray(String filename);

}
