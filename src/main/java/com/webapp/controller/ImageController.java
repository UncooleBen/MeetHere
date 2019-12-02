package com.webapp.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.webapp.service.filesystem.Fao;

@Controller
@RequestMapping("/img")
public class ImageController {

	@Autowired
    Fao fao;

	@ResponseBody
	@RequestMapping(value = "/*", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] getRequestedImage(HttpServletRequest request) throws IOException {
		String[] url = request.getRequestURL().toString().split("/");
		String filename = url[url.length - 1];
		return this.fao.convertToByteArray(filename);
	}

}
