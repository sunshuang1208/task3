import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;

public class emailTest {
    WebDriver webDriver =null;

    @BeforeClass
    public void  login() {

        File file = new File("E:\\Google\\Chrome\\Application\\chromedriver.exe");
        System.setProperty("webdriver.chrome.driver",file.getAbsolutePath());
        webDriver = new ChromeDriver();
        try {
            //获取qq邮箱连接
            webDriver.get("https://mail.qq.com");
            //给页面加载时间，避免未加载造成获取不到元素而报错
            Thread.sleep(100000);

            //登录页面查看一下是一个frame页面，需要切入
            webDriver.findElement(By.id("qqLoginTab"));

            //设置睡眠时间，本人电脑比较慢所以有时候会响应不到
            Thread.sleep(50000);
            webDriver.switchTo().frame("login_frame");

            //通过id获取邮箱的登录名文本框，运用senkeys进行输入
            webDriver.findElement(By.id("u")).sendKeys("*******@qq.com");
            webDriver.findElement(By.id("p")).sendKeys("*********");

            //运用click进行点击操作
            webDriver.findElement(By.id("login_button")).click();
           Thread.sleep(2000);

        }catch (Exception  e){

            //因为webdriver还需要用所以没有finally处理
            webDriver.quit();

            System.out.println("邮箱登录异常运行异常");
        }

    }

    @Test
    public void writeMil(){
        try {
            //登录成功等待页面加载3秒
            Thread.sleep(10000);

            //通过xpath点击写信
            webDriver.findElement(By.id("composebtn")).click();

            //等待页面加载1秒
            Thread.sleep(10000);

            //通过页面元素发现该页面有不止一个iframe表单,进入要操作的页面
            webDriver.switchTo().frame("mainFrame");

            //选择普通邮件
            webDriver.findElement(By.xpath("//*[@id=\"frm\"]/table/tbody/tr/td[1]/div[2]/div")).click();
            Thread.sleep(20000);

            //输入要发送邮箱的地址
            webDriver.findElement(By.xpath("//*[@id=\"toAreaCtrl\"]/div[2]/input")).sendKeys("********@qq.com");

            //输入标题
            webDriver.findElement(By.id("subject")).sendKeys("testSelenium");
            //运用sendKeys添加附件
            webDriver.findElement(By.xpath("//[@id=\"AttachFrame\"]/span/input")).sendKeys("E://blog.docx");

            Thread.sleep(10000);

            //正文在另外一个iframe下 所以需要切换iframe
            webDriver.switchTo().frame(webDriver.findElement(By.className("qmEditorIfrmEditArea")));

            //输入正文信息
            webDriver.findElement(By.tagName("body")).sendKeys("要发送一个邮箱请注意查收");

            //跳转到原iframe表单
            webDriver.switchTo().parentFrame();

            //点击发送
            webDriver.findElement(By.xpath("//*[@id='toolbar']/div/a[1]")).click();

            //等待页面加载发送成功页面
            Thread.sleep(50000);
            //切入发送成功iframe页面
            webDriver.findElement(By.xpath("//*[@id=\"sendinfomsg\"]"));
            //获取页面发送成功的关键信息
            String value = webDriver.findElement(By.xpath("//*[@id=\"sendinfomsg\"]")).getText();
            System.out.println(value);

            //判断邮箱是否发送成功
            Assert.assertEquals("您的邮件已发送", value);

        }catch (Exception E){
            //异常关闭
           webDriver.quit();
            System.out.println("邮箱发送运行异常");
        }finally {

            //运行完毕进行关闭
            webDriver.quit();

        }

    }
}
