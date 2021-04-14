package step.definition;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.managers.ChromeDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SeleniumDay2 {
    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.noon.com/uae-en/");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        while (driver.findElements(By.tagName("h3")).size()<4){
            js.executeScript("window.scrollBy(0,1000)");
        }
        List<String> carousel = driver.findElements(By.tagName("h3")).stream().map(WebElement::getText).collect(Collectors.toList());
        List<String> output=new ArrayList<>();
        for(int i=0;i<4;i++){
            output.addAll(getDataByCarousel(driver,carousel.get(i)));
        }
        Collections.sort(output);
        System.out.println(output.size());
        output.forEach(System.out::println);
        driver.quit();
    }
    private static List<String> getDataByCarousel(WebDriver driver,String carousel){
        List<String> list= new ArrayList<>();
        String nextXpath="//h3[contains(text(),'"+carousel+"')]//parent::div//parent::div//parent::div//div[contains(@class,'swiper-button-next')]";
        String items="//h3[contains(text(),'"+carousel+"')]//parent::div//parent::div//parent::div//div[@data-qa='product-name']";
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView()", driver.findElement(By.xpath(nextXpath)));
        while(driver.findElement(By.xpath(nextXpath)).isDisplayed()){
            list.addAll(driver.findElements(By.xpath(items)).stream().map(WebElement::getText).collect(Collectors.toList()));
            driver.findElement(By.xpath(nextXpath)).click();
        }
        return list.stream().filter(s->!s.isEmpty()).collect(Collectors.toList());
    }
}
