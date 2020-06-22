import "@babel/polyfill";
import chrome from "selenium-webdriver/chrome";
import { Builder, By, Key, Capabilities } from "selenium-webdriver";
import assert from "assert";
import { path } from "chromedriver";
import { waitForDebugger } from "inspector";
let driver = null;
const chromeOptions = new chrome.Options().headless();
const URL = "http://localhost:8081/";

describe("Startup Webpage Tests", () => {
  before(async () => {
    driver = await new Builder(path)
      .forBrowser("chrome")
      .setChromeOptions(chromeOptions)
      .build();
    await driver.get(URL);
  });

  after(async () => {
    await driver.quit();
  });

  it("should have 3 comments on the page at startup", async () => {
    setTimeout(() => { true }, 1000);
    const commentContainer = await driver.findElement(By.id("comment-container"));
    const comments = await driver.findElements(By.className("comments"));
    assert.equal(comments.length, 3);
  });
});
