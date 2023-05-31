package Assignment;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Amazonauto {
public static void main(String [] args) throws InterruptedException {

	//Declaration of variables for Search in Amazon
	String Category = null;
	String SearchVal = null;
	
	//Launch browser
	System.setProperty("webdriver.chrome.driver", "C:\\SeleniumDriver\\chromedriver32\\chromedriver.exe"); 
	ChromeOptions chromeOptions = new ChromeOptions();
    chromeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
    chromeOptions.addArguments("--remote-allow-origins=*");
// Open www.amazon.in
    WebDriver driver = new ChromeDriver(chromeOptions);
	String appurl = "http://amazon.in/";
	driver.get(appurl);
	driver.manage().window().maximize();
    driver.manage().timeouts().implicitlyWait(10, TimeUnit.MILLISECONDS); 
// Connect to JDBC
	try  {
		Class.forName("com.mysql.cj.jdbc.Driver"); //("jdbc:mysql://localhost:3306/ecommerce","root","selenium@123");
		
Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ecommerce","root","Simplilearn");
Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("select * from Amazon");
	// Retrieving Values of category and Searchvalue from table amazon
   while (rs.next())  {

	System.out.println(rs.getString(1)+" "+ rs.getString(3));
   
	Category = rs.getString(1);
	SearchVal = rs.getString(3); 
   

   }
 WebElement searchbox = driver.findElement(By.id("twotabsearchtextbox"));
 searchbox.sendKeys("Soundbars");
 
	driver.findElement(By.id("nav-search-submit-button")).click();
	Thread.sleep(2000);
	 WebElement checkbox= driver.findElement(By.xpath("//li[@id='p_89/ZEBRONICS']//i[@class='a-icon a-icon-checkbox']")); 
	 checkbox.click();
Thread.sleep(2000);	
	WebElement Aavante = driver.findElement(By.linkText("ZEBRONICS Juke BAR 9750 PRO 5.1.2 Surround Dolby Atmos 525W Soundbar with Subwoofer, Dual Wireless Rear Satellites, HDMI eARC, Optical, Bluetooth 5.0, Wall Mount & Remote Control"));
	Aavante.click();
	

	List<WebElement> ResList =driver.findElements(By.xpath("//*[@data-component-type='s-search-result']"));
	System.out.println("Total search result pagewise are: " + ResList.size());
	String ResListCount = String.valueOf(ResList.size());
	
//Retrieving the count of result of search value - pagewise from Result bar appearing in the page	
	WebElement ResultBarText = driver.findElement(By.xpath("//*[@class='a-section a-spacing-small a-spacing-top-small']/span[1]"));
	System.out.println("Result bar: " + ResultBarText.getText());
	
	// Example : 1-30 of over 10,000 results for

	String ResultCountTxt = ResultBarText.getText().substring(2, 4);
	System.out.println("Result Count from result bar = " + ResultCountTxt);
	//Count search result - when Example: 5 results for
	WebElement ResCount = driver.findElement(By.xpath("//*[contains(text(), 'results for')]"));
	String[] texts = ResCount.getText().split(" ");
	String num = "";
	for (int i = 0; i < texts.length; i++) {
		if (texts[i].equals("results")) {
			num = texts[i - 1];
			break;
		}
	}
	System.out.println("Total search results for the particular category are: " + num);
	//checking the search result count c, if its equal to search result count appearing in Result bar text.
	if ( ResultCountTxt.equals(ResListCount)) {
		System.out.println("Yes! Result is matching");	
	}
	else {
		System.out.println("Oh no!! Result is not matching");
		
		System.out.println("Extra numbers are sponsored items");
	}
	System.out.println("____________");
	
}
catch (ClassNotFoundException e) 
{
	
	// e.printStackTrace();
	System.out.println("Class not found");
	} catch (SQLException e) 
{
	
	
	System.out.println("SQL Exception");
		
}
//capture screen 		

TakesScreenshot TsObj = (TakesScreenshot) driver;

File myFile = TsObj.getScreenshotAs(OutputType.FILE);

try {
	FileUtils.copyFile(myFile, new File("C://Amazon images//amazonsearch.png"));
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}

//close after 5 seconds
		try {
			Thread.sleep(5000); //time is in ms (1000 ms = 1 second)
			driver.close();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
	}
}

