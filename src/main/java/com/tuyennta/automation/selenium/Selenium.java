package com.tuyennta.automation.selenium;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.UnexpectedTagNameException;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.tuyennta.automation.dto.TestStepDTO;
import com.tuyennta.automation.dto.TestStepResultDTO;
import com.tuyennta.automation.settings.Setting;
import com.tuyennta.automation.utils.Const;
import com.tuyennta.automation.utils.ReportConfig;

public class Selenium {

	private RemoteWebDriver driver = null;
	private List<String> windowHandles = new ArrayList<>();

	public boolean openBrowser(TestStepDTO testStep) {
		if (testStep.getObject().equalsIgnoreCase(Const.FF)) {
			System.setProperty(Const.FF_WEB_DRIVER_CONF_STRING,
					Setting.WebDriverLocation.get(Const.FF_WEB_DRIVER_NAME));
			/* Create a new FireFox Profile instance. */
			FirefoxProfile ffProfile = new FirefoxProfile();

			/* Set file save to directory. */
			ffProfile.setPreference("browser.download.dir",
					System.getProperty("user.dir") + File.separator + "Download");
			ffProfile.setPreference("browser.download.folderList", 2);

			/*
			 * Set file mime type which do not show save to popup dialog. If the file format
			 * is zip, then we set this preference's value to application/zip. If file
			 * format is csv then you need set the value to application/csv.
			 */
			ffProfile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/vnd.ms-excel;");
			ffProfile.setPreference("browser.helperApps.neverAsk.saveToDisk",
					"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;");

			ffProfile.setPreference("browser.download.manager.showWhenStarting", false);
			ffProfile.setPreference("pdfjs.disabled", true);

			/* Create Firefox browser based on the profile just created. */
			driver = new FirefoxDriver(ffProfile);
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			driver.manage().window().maximize();
		} else {
			System.setProperty(Const.GC_WEB_DRIVER_CONF_STRING,
					Setting.WebDriverLocation.get(Const.GC_WEB_DRIVER_NAME));
			Map<String, Object> chromePreferences = new Hashtable<String, Object>();
			/*
			 * Below two chrome preference settings will disable popup dialog when download
			 * file.
			 */
			chromePreferences.put("profile.default_content_settings.popups", 0);
			chromePreferences.put("download.prompt_for_download", "false");

			/* Set file save to directory. */
			chromePreferences.put("download.default_directory",
					System.getProperty("user.dir") + File.separator + "Download");

			ChromeOptions chromeOptions = new ChromeOptions();
			chromeOptions.setExperimentalOption("prefs", chromePreferences);

			DesiredCapabilities cap = DesiredCapabilities.chrome();
			cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			cap.setCapability(ChromeOptions.CAPABILITY, chromeOptions);

			// Initiate ChromeDriver
			driver = new ChromeDriver(cap);
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			driver.manage().window().maximize();
		}

		driver.get(testStep.getParameters());
		windowHandles.add(driver.getWindowHandle());
		return true;
	}

	public boolean openNewTab(TestStepDTO step) {
		((JavascriptExecutor) driver).executeScript("window.open('" + step.getParameters() + "','_blank');");
		for (String handle : driver.getWindowHandles()) {
			if (!windowHandles.contains(handle)) {
				windowHandles.add(handle);
			}
		}
		// switch to newly opened tab
		driver.switchTo().window(windowHandles.get(windowHandles.size() - 1));
		// TODO wait for page loaded
		return true;
	}

	public TestStepResultDTO closeTab(TestStepDTO step) {
		TestStepResultDTO result = new TestStepResultDTO(step);
		Object[] windows = driver.getWindowHandles().toArray();
		if (windows.length <= 1) {
			closeBrowser();
		} else {
			if (step.getParameters().isEmpty()) {
				driver.switchTo().window(windows[1].toString());
				driver.close();
				driver.switchTo().window(windows[0].toString());
			} else {
				String closedHandle = "";
				for (Object object : windows) {
					if (driver.switchTo().window(object.toString()).getTitle().equals(step.getParameters())) {
						closedHandle = driver.getWindowHandle();
						driver.close();
						break;
					}
				}
				if (closedHandle.isEmpty()) {
					result.setCause("Tab page: " + step.getParameters() + " not found");
					result.setResult(false);
				}
				// switch driver to other window
				for (Object object : windows) {
					if (!object.equals(closedHandle)) {
						driver.switchTo().window(object.toString());
						break;
					}
				}
			}
		}
		return result;
	}

	/**
	 * Close all opened tab, windows of current running browser
	 */
	public void closeBrowser() {
		Set<String> handles = driver.getWindowHandles();
		for (String handle : handles) {
			driver.switchTo().window(handle).close();
		}
		// driver.quit();
	}

	/**
	 * Switch focus from current tab to target tab specified by page title
	 * 
	 * @param step
	 * @return
	 */
	public TestStepResultDTO switchToTab(TestStepDTO step) {
		TestStepResultDTO result = new TestStepResultDTO(step);
		Object[] windows = driver.getWindowHandles().toArray();
		String switchedHandle = "";
		String currentHandle = driver.getWindowHandle();
		for (Object object : windows) {
			if (driver.switchTo().window(object.toString()).getTitle().equals(step.getParameters())) {
				switchedHandle = driver.getWindowHandle();
				break;
			}
		}
		if (switchedHandle.isEmpty()) {
			driver.switchTo().window(currentHandle);
			result.setCause("Tab page: " + step.getParameters() + " not found");
			result.setResult(false);
		}
		return result;
	}

	/**
	 * Click (or Ctrl-click, alt-click, shift-click) on specified element by xpath
	 * 
	 * @param step
	 * @return
	 */
	public TestStepResultDTO click(TestStepDTO step) {
		TestStepResultDTO result = new TestStepResultDTO(step);

		// check if element available
		WebElement elem = null;
		try {
			elem = driver.findElement(By.xpath(step.getObject()));
		} catch (Exception e) {
			result.setResult(false);
			result.setCause("Current active tab page: " + driver.getTitle() + ". ");
			result.setCause(e.toString());
			return result;
		}
		if (step.getParameters().isEmpty()) {
			try {
				elem.click();
			} catch (WebDriverException e) {
				result.setResult(false);
				result.setCause(e.toString());
				return result;
			}
		} else {
			switch (step.getParameters().toLowerCase()) {
			case "alt":
				result = click(elem, Keys.ALT, result);
				break;
			case "ctrl":
				result = click(elem, Keys.CONTROL, result);
				break;
			case "shift":
				result = click(elem, Keys.SHIFT, result);
				break;
			default:
				result.setResult(false);
				result.setCause(
						"Key code: " + step.getParameters() + " is not supported. Supported keys: Alt, Ctrl, Shift");
				break;
			}
		}
		return result;
	}

	private TestStepResultDTO click(WebElement elem, CharSequence keycode, TestStepResultDTO result) {
		Actions actions = new Actions(driver);
		actions.keyDown(keycode).click(elem).keyUp(keycode).build().perform();
		return result;
	}

	public TestStepResultDTO setText(TestStepDTO step) {
		TestStepResultDTO result = new TestStepResultDTO(step);
		// check if element available
		WebElement elem = null;
		try {
			elem = driver.findElement(By.xpath(step.getObject()));
		} catch (Exception e) {
			result.setResult(false);
			result.setCause("Current active tab page: " + driver.getTitle() + ". ");
			result.setCause(e.toString());
			return result;
		}
		elem.sendKeys(step.getParameters());
		return result;
	}

	public TestStepResultDTO select(TestStepDTO step) {
		TestStepResultDTO result = new TestStepResultDTO(step);
		// check if element available
		WebElement elem = null;
		try {
			elem = driver.findElement(By.xpath(step.getObject()));

			Select select = new Select(elem);
			switch (step.getAction().toLowerCase()) {
			case "selectbyvalue":
				for (String item : step.getParameters().split(";")) {
					select.selectByValue(item);
				}
				break;
			case "selectbytext":
				for (String item : step.getParameters().split(";")) {
					select.selectByVisibleText(item);
				}
				break;
			case "selectbyindex":
				for (String item : step.getParameters().split(";")) {
					select.selectByIndex(Integer.parseInt(item));
				}
				break;
			}
		} catch (NoSuchElementException e) {
			result.setResult(false);
			result.setCause("Current active tab page: " + driver.getTitle() + ". ");
			result.setCause(e.toString());
			return result;
		} catch (UnexpectedTagNameException e1) {
			result.setResult(false);
			result.setCause("Specified element is not a type of SELECT TAG!");
		} catch (UnhandledAlertException e2) {
			result.setResult(false);
			result.setCause(e2.toString());
		}

		return result;
	}

	public TestStepResultDTO mouseHover(TestStepDTO step) {
		TestStepResultDTO result = new TestStepResultDTO(step);

		// check if element available
		WebElement elem = null;
		try {
			elem = driver.findElement(By.xpath(step.getObject()));
		} catch (Exception e) {
			result.setResult(false);
			result.setCause("Current active tab page: " + driver.getTitle() + ". ");
			result.setCause(e.toString());
			return result;
		}
		Actions actions = new Actions(driver);
		actions.moveToElement(elem).build().perform();
		return result;
	}

	public TestStepResultDTO captureScreen(TestStepDTO step) {
		TestStepResultDTO result = new TestStepResultDTO(step);
		try {
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			ReportConfig.getInstance().getCurrentReportInstance().addEvidence(scrFile);
		} catch (WebDriverException e) {
			result.setResult(false);
			result.setCause(e.toString());
		}
		return result;
	}

	public TestStepResultDTO uploadFile(TestStepDTO step) {
		TestStepResultDTO result = new TestStepResultDTO(step);
		// check if element available
		WebElement elem = null;
		try {
			elem = driver.findElement(By.xpath(step.getObject()));
		} catch (Exception e) {
			result.setResult(false);
			result.setCause("Current active tab page: " + driver.getTitle() + ". ");
			result.setCause(e.toString());
			return result;
		}
		File uploadFile = new File(step.getParameters());
		if (!uploadFile.exists()) {
			uploadFile = new File(System.getProperty("user.dir") + File.separator + step.getParameters());
			if (!uploadFile.exists()) {
				result.setCause("File: " + step.getParameters() + " is not existed, please check again");
				result.setResult(false);
				return result;
			}
		}

		elem.sendKeys(uploadFile.getAbsolutePath());
		return result;
	}

	public TestStepResultDTO uploadFiles(TestStepDTO step) {
		TestStepResultDTO result = new TestStepResultDTO(step);

		// check if element available
		WebElement elem = null;
		try {
			elem = driver.findElement(By.xpath(step.getObject()));
		} catch (Exception e) {
			result.setResult(false);
			result.setCause("Current active tab page: " + driver.getTitle() + ". ");
			result.setCause(e.toString());
			return result;
		}

		// check upload folder exist
		String folder = step.getParameters().substring(0, step.getParameters().indexOf(";"));
		File uploadFolder = new File(folder);
		if (!uploadFolder.exists()) {
			result.setCause("Folder: " + uploadFolder + " is not existed, please check again");
			result.setResult(false);
			return result;
		}

		// check all upload files exist
		String fileString = step.getParameters().substring(step.getParameters().indexOf(";") + 1);
		String files[] = fileString.split(";");
		File uploadFile = null;
		String uploadFiles = "";
		int index = 0;
		for (String file : files) {
			uploadFile = new File(folder + File.separator + file);
			if (!uploadFile.exists()) {
				result.setCause("File: " + file + " is not existed, please check again");
				result.setResult(false);
				return result;
			}
			if (index == files.length-1) {
				uploadFiles += uploadFile.getAbsolutePath();	
			}else {
				uploadFiles += uploadFile.getAbsolutePath() + "\n";
				index ++;
			}
			
		}
		
		// click to open upload window
		elem.sendKeys(uploadFiles);;

		return result;
	}

	public TestStepResultDTO storeText(TestStepDTO step) {
		TestStepResultDTO result = new TestStepResultDTO(step);

		// check if element available
		WebElement elem = null;
		try {
			elem = driver.findElement(By.xpath(step.getObject()));
		} catch (Exception e) {
			result.setResult(false);
			result.setCause("Current active tab page: " + driver.getTitle() + ". ");
			result.setCause(e.toString());
			return result;
		}

		Setting.TestVariable.put(step.getParameters(), elem.getText());

		return result;
	}

	public TestStepResultDTO storeValue(TestStepDTO step) {
		TestStepResultDTO result = new TestStepResultDTO(step);

		// check if element available
		WebElement elem = null;
		try {
			elem = driver.findElement(By.xpath(step.getObject()));
		} catch (Exception e) {
			result.setResult(false);
			result.setCause("Current active tab page: " + driver.getTitle() + ". ");
			result.setCause(e.toString());
			return result;
		}

		Setting.TestVariable.put(step.getParameters(), elem.getAttribute("value"));

		return result;
	}

	public TestStepResultDTO confirmDialog(TestStepDTO step) {
		TestStepResultDTO result = new TestStepResultDTO(step);

		try {
			driver.switchTo().alert().accept();
		} catch (NoAlertPresentException e) {
			result.setResult(false);
			result.setCause(e.toString());
		}
		return result;
	}

	public TestStepResultDTO cancelDialog(TestStepDTO step) {
		TestStepResultDTO result = new TestStepResultDTO(step);

		try {
			driver.switchTo().alert().dismiss();
		} catch (NoAlertPresentException e) {
			result.setResult(false);
			result.setCause(e.toString());
		}
		return result;
	}

	public TestStepResultDTO keypress(TestStepDTO step) {
		TestStepResultDTO result = new TestStepResultDTO(step);
		// check if element available
		WebElement elem = null;
		try {
			elem = driver.findElement(By.xpath(step.getObject()));
		} catch (Exception e) {
			result.setResult(false);
			result.setCause("Current active tab page: " + driver.getTitle() + ". ");
			result.setCause(e.toString());
			return result;
		}
		String[] keys = step.getParameters().toLowerCase().split(";");
		if (keys.length == 2) {
			elem.sendKeys(Keys.chord(keys[0].length() == 1 ? keys[0] : Keys.valueOf(keys[0].toUpperCase()),
					keys[1].length() == 1 ? keys[1] : Keys.valueOf(keys[1].toUpperCase())));
		} else if (keys.length == 1) {
			elem.sendKeys(Keys.chord(keys[0].length() == 1 ? keys[0] : Keys.valueOf(keys[0].toUpperCase())));
		} else {
			result.setResult(false);
			result.setCause("Only support for pressing single key or group of 2 keys");
		}

		return result;
	}

	public TestStepResultDTO waitForElementVisible(TestStepDTO step) {
		TestStepResultDTO result = new TestStepResultDTO(step);
		// check if element available
		WebElement elem = null;
		try {
			WebDriverWait wait = new WebDriverWait(driver, Long.valueOf(step.getParameters()));
			elem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(step.getObject())));
		} catch (Exception e) {
			result.setResult(false);
			result.setCause("Current active tab page: " + driver.getTitle() + ". ");
			result.setCause(e.toString());
			return result;
		}
		if (elem == null) {
			result.setResult(false);
			result.setCause("Current active tab page: " + driver.getTitle() + ". Element not found after "
					+ step.getParameters() + " miliseconds");
		}

		return result;
	}

	public TestStepResultDTO waitForElementClickable(TestStepDTO step) {
		TestStepResultDTO result = new TestStepResultDTO(step);
		// check if element available
		WebElement elem = null;
		try {
			WebDriverWait wait = new WebDriverWait(driver, Long.valueOf(step.getParameters()));
			elem = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(step.getObject())));
		} catch (Exception e) {
			result.setResult(false);
			result.setCause("Current active tab page: " + driver.getTitle() + ". ");
			result.setCause(e.toString());
			return result;
		}
		if (elem == null) {
			result.setResult(false);
			result.setCause("Current active tab page: " + driver.getTitle() + ". Element is not clickable after "
					+ step.getParameters() + " miliseconds");
		}

		return result;
	}

	public TestStepResultDTO checkVisible(TestStepDTO step) {
		TestStepResultDTO result = new TestStepResultDTO(step);
		// check if element available
		WebElement elem = null;
		try {
			WebDriverWait wait = new WebDriverWait(driver, 5000);
			elem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(step.getObject())));
		} catch (Exception e) {
			result.setResult(false);
			result.setCause("Current active tab page: " + driver.getTitle() + ". ");
			result.setCause(e.toString());
			return result;
		}
		if (elem == null) {
			result.setResult(false);
			result.setCause("Current active tab page: " + driver.getTitle() + ". Element not found after "
					+ step.getParameters() + " miliseconds");
		}

		return result;
	}

	public TestStepResultDTO checkEnable(TestStepDTO step) {
		TestStepResultDTO result = new TestStepResultDTO(step);
		// check if element available
		WebElement elem = null;
		try {
			WebDriverWait wait = new WebDriverWait(driver, 5000);
			elem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(step.getObject())));
		} catch (Exception e) {
			result.setResult(false);
			result.setCause("Current active tab page: " + driver.getTitle() + ". ");
			result.setCause(e.toString());
			return result;
		}
		if (elem == null) {
			result.setResult(false);
			result.setCause("Current active tab page: " + driver.getTitle() + ". Element not found after "
					+ step.getParameters() + " miliseconds");
			return result;	
		}
		result.setResult(elem.isEnabled());

		return result;
	}

	public TestStepResultDTO compareText(TestStepDTO step) {
		TestStepResultDTO result = new TestStepResultDTO(step);
		// check if element available
		WebElement elem = null;
		try {
			WebDriverWait wait = new WebDriverWait(driver, 5000);
			elem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(step.getObject())));
		} catch (Exception e) {
			result.setResult(false);
			result.setCause("Current active tab page: " + driver.getTitle() + ". ");
			result.setCause(e.toString());
			return result;
		}
		if (elem == null) {
			result.setResult(false);
			result.setCause("Current active tab page: " + driver.getTitle() + ". Element not found after "
					+ step.getParameters() + " miliseconds");
			return result;	
		}
		result.setResult(elem.getText().equals(step.getParameters()));
		result.setCause("Element's text: " + elem.getText() + " is not equal to input text: " + step.getParameters());

		return result;
	}

	public TestStepResultDTO compareValue(TestStepDTO step) {
		TestStepResultDTO result = new TestStepResultDTO(step);
		// check if element available
		WebElement elem = null;
		try {
			WebDriverWait wait = new WebDriverWait(driver, 5000);
			elem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(step.getObject())));
		} catch (Exception e) {
			result.setResult(false);
			result.setCause("Current active tab page: " + driver.getTitle() + ". ");
			result.setCause(e.toString());
			return result;
		}
		if (elem == null) {
			result.setResult(false);
			result.setCause("Current active tab page: " + driver.getTitle() + ". Element not found after "
					+ step.getParameters() + " miliseconds");
			return result;	
		}
		result.setResult(elem.getAttribute("value").equals(step.getParameters()));
		result.setCause("Element's vale: " + elem.getAttribute("value") + " is not equal to input text: " + step.getParameters());

		return result;
	}
}
