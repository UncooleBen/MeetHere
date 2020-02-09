package com.webapp.fao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.IOUtils;


/**
 * @author Juntao Peng
 */
public class FAOImpl implements FAO {

	@Override
	public byte[] convertToByteArray(String filename) {
		byte[] result = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(new File("/meethere_report/img/", filename));
			System.out.println("/meethere_report/img/");
			System.out.println(filename);
			result = IOUtils.toByteArray(fis);
		} catch (IOException ioe) {
			ioe.printStackTrace(System.err);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException ioe) {
					ioe.printStackTrace(System.err);
				}
			}
		}
		return result;
	}
}
