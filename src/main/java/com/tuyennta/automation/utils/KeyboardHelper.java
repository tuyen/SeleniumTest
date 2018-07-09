package com.tuyennta.automation.utils;

import java.io.File;
import java.io.IOException;

public class KeyboardHelper {

	/**
	 * Using AutoIT to press on keyboard
	 * @param keyString
	 * @throws IOException
	 */
	public static void keypress(String keyString) throws IOException {
		File fileRunner = new File(Const.AUTOIT_FILE_RUNNER);
		if (!fileRunner.exists()) {
			throw new IOException("Driver file: " + Const.AUTOIT_FILE_RUNNER + " not found!, please check again");
		}
		File fileCommand = new File(Const.AUTOIT_FILE_COMMAND);
		if (!fileCommand.exists()) {
			throw new IOException("Command file: " + Const.AUTOIT_FILE_COMMAND + " not found!, please check again");
		}
		
		new ProcessBuilder(fileRunner.getAbsolutePath(), fileCommand.getAbsolutePath(), keyString).start();
	}
}
