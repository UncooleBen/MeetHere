package com.webapp.controller;

import java.io.IOException;

import com.webapp.fao.FAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Juntao Peng
 */
@Controller
@RequestMapping("/img")
public class ImageController {

	@Autowired
	FAO fao;

	@ResponseBody
	@RequestMapping(value = "/*", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] getRequestedImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String[] url = request.getRequestURL().toString().split("/");
		String filename = url[url.length - 1];
		System.out.println("filename in contoller:"+filename);
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		return this.fao.convertToByteArray(filename+".png");
	}

}
