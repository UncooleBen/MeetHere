package com.webapp.service.filesystem;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.stream.Stream;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.web.multipart.MultipartFile;

import com.webapp.model.Message;


/**
 * This class is for unit testing FAOImpl class.
 *
 * @author Juntao Peng
 *
 * @date 2019.11.28
 **/
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FAOImplTest {
	private File mockedDir = mock(File.class);
	private File mockedFile = mock(File.class);
	private TestableFAOImpl fao;
	private long milliTime;
	private ByteArrayOutputStream outContent;
	private ByteArrayOutputStream errContent;
	private IOException test_io_exception;
	private MultipartFile image;
	private PrintStream originalOut;
	private PrintStream originalErr;

	class TestableFAOImpl extends FaoImpl {

		@Override
		protected File createFile(String path) {
			return mockedDir;
		}

		@Override
		protected File createFile(File dir, String filename) {
			return mockedFile;
		}
	}

	@BeforeEach
	void init() {
		this.fao = new TestableFAOImpl();
		this.milliTime = System.currentTimeMillis();
		this.outContent = new ByteArrayOutputStream();
		this.errContent = new ByteArrayOutputStream();
		this.test_io_exception = new IOException();
		this.image = mock(MultipartFile.class);
		this.originalOut = System.out;
		this.originalErr = System.err;
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
	}

	@AfterEach
	void tear_down() throws IOException {
		this.outContent.close();
		this.errContent.close();
		System.setOut(originalOut);
		System.setErr(originalErr);
	}

	@Order(1)
	@Test
	public void test_io_exception_store_image() throws IOException {
		Date date = new Date(milliTime);
		Message message = new Message("james", "im bond", date);
		/* Mocking behaviours */
		when(mockedDir.exists()).thenReturn(true);
		when(mockedFile.exists()).thenReturn(false);
		when(mockedFile.createNewFile()).thenThrow(test_io_exception);
		/* Run the method */
		this.fao.storeImage(message, image);
		/* Assertions */
		assertTrue(errContent.toString().contains("java.io.IOException"));
	}

	static Stream<Arguments> booleanAndBooleanProvider() {
		return Stream.of(Arguments.of(true, true), Arguments.of(true, false), Arguments.of(false, true),
				Arguments.of(false, false));
	}

	@Order(2)
	@ParameterizedTest
	@MethodSource("booleanAndBooleanProvider")
	public void test_store_image(boolean value1, boolean value2) {
		Date date = new Date(milliTime);
		Message message = new Message("james", "im bond", date);
		/* Mocking behaviours */
		when(mockedDir.exists()).thenReturn(value1);
		when(mockedFile.exists()).thenReturn(value2);
		/* Run the method */
		boolean result = this.fao.storeImage(message, image);
		/* Assertions */
		assertTrue(result);
	}

}
