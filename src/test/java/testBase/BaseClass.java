package testBase;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.apache.logging.log4j.LogManager; //log4j
import org.apache.logging.log4j.Logger;  //log4j

public class BaseClass 
{
public WebDriver driver;
public Logger logger;
public Properties p;
   
    @Parameters({"os" ,"browser"})
	@BeforeClass(groups= {"Sanity", "Master", "Regrassion"})
public void setup(@Optional("DefaultOS") String os, @Optional("chrome") String browser) throws IOException 
	{
		//Loading config.properties file
		FileReader file=new FileReader(".//src//test//resources//config.properties");
		 p=new Properties();
		 p.load(file);

		//loading log4j file
		logger=LogManager.getLogger(this.getClass());//Log4j

		 
		 if(p.getProperty("execution_env").equalsIgnoreCase("remote"))
			{
		DesiredCapabilities capabilities=new DesiredCapabilities(); 
				
				//os
		if (os.equalsIgnoreCase("windows"))
            capabilities.setPlatform(Platform.WIN11);
        else if (os.equalsIgnoreCase("linux"))
            capabilities.setPlatform(Platform.LINUX);
        else if (os.equalsIgnoreCase("mac"))
            capabilities.setPlatform(Platform.MAC);
		
		//browser
		 switch(browser.toLowerCase()) 
		 {
         case "chrome": capabilities.setBrowserName("chrome"); break;
         case "firefox": capabilities.setBrowserName("firefox"); break;
         case "edge": capabilities.setBrowserName("MicrosoftEdge"); break;
         }
			
		 driver = new RemoteWebDriver(
		            new URL("http://localhost:4444/wd/hub"),
		            capabilities);
			}
			
		else 
		{

        switch(browser.toLowerCase()) {
            case "chrome": driver = new ChromeDriver(); break;
            case "edge": driver = new EdgeDriver(); break;
            case "firefox": driver = new FirefoxDriver(); break;
            default: throw new RuntimeException("Invalid browser");
        }
		}
        driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		
		driver.get(p.getProperty("appURL"));
		driver.manage().window().maximize();
        }
	
	
		 @AfterClass(alwaysRun = true)
		 public void tearDown() 
		 {
		     driver.quit();
	
		 }
	public String randomeString()
	{
		String generatedString=RandomStringUtils.randomAlphabetic(5);
		return generatedString;
	}
	
	public String randomeNumber()
	{
		String generatedString=RandomStringUtils.randomNumeric(10);
		return generatedString;
	}
	
	public String randomAlphaNumeric()
	{
		String str=RandomStringUtils.randomAlphabetic(3);
		String num=RandomStringUtils.randomNumeric(3);
		
		return (str+"@"+num);
	}
	
	public String captureScreen(String tname) throws IOException {

		String timeStamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
				
		TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
		File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
		
		String targetFilePath=System.getProperty("user.dir")+"\\screenshots\\" + tname + "_" + timeStamp + ".png";
		File targetFile=new File(targetFilePath);
		
		sourceFile.renameTo(targetFile);
			
		return targetFilePath;

	}
}
	

		
	
