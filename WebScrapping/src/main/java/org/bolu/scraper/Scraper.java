package org.bolu.scraper;


import org.bolu.model.LaptopDao;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The base class for implementing specific website scrapers.
 * Manages common methods and configurations for scraping laptops.
 */

public class Scraper extends Thread{
    private int crawlDelay;

    private LaptopDao laptopDao;

    volatile private boolean runThread = true;

    private WebDriver driver;


    public Scraper(){
        init();
    }


    /**
     * Default constructor initializing the Scraper instance.
     */
    private void init(){
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true);

        options.addArguments("user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit.537.36 (KHTML, like Gecko) Chrome/119.0.6045.199 Safari/537.36");

        //Create instance of web driver
        this.driver = new ChromeDriver(options);
    }


    /**
     * Parses the color of the laptop based on the provided string.
     *
     * @param s The string containing information about the laptop.
     * @return The color of the laptop.
     */
    public String parseColor(String s) {

        String tmp = s.toLowerCase(); // Convert to lowercase for easier comparison

        if (tmp.contains("black"))
            return "Black";
        else if (tmp.contains("white") || tmp.contains("weiß"))
            return "White";
        else if (tmp.contains("blue"))
            return "Blue";
        else if (tmp.contains("gold"))
            return "Gold";
        else if (tmp.contains("silver"))
            return "Silver";
        else if (tmp.contains("gray") || tmp.contains("grey"))
            return "Gray";
        else if(tmp.contains("midnight"))
            return "Midnight";
        else if(tmp.contains("starlight"))
            return "Starlight";
        else if(tmp.contains("space grey"))
            return "Space Grey";
        else if(tmp.contains("space black"))
            return "Platinum Grey";
        else if(tmp.contains("pink"))
            return "Pink";
        else
            return "Unknown";
    }


    /**
     * Extracts the storage capacity of the laptop from the provided string.
     *
     * @param s The string containing information about the laptop.
     * @return The storage capacity in gigabytes (GB).
     */
    public int extractStorage(String s) {
        String tmp = s.toLowerCase();

        String[] storageKeywords = {"64 gb", "64gb","32 gb emmc","64gb emmc", "125gb emmc", "128gb emmc", "256gb emmc", "512gb emmc", "64gb ssd", "128gb ssd", "256gb ssd", "512gb ssd", "125", "128", "240", "250", "256", "500", "512", "1tb", "2tb", "4tb", "8tb", "16tb", "32tb"};
        int[] storageValues = {64,64,32, 64, 125, 128, 256, 512, 64, 128, 256, 512, 125, 128, 240, 250, 256, 500, 512, 1000, 2000, 4000, 8000, 16000, 32000};

        for (int i = 0; i < storageKeywords.length; i++) {
            if (tmp.contains(storageKeywords[i]) || tmp.contains(storageKeywords[i] + "gb")) {
                return storageValues[i];
            }
        }

        return 0;
    }


    /**
     * Parses the brand of the laptop from the provided string.
     *
     * @param s The string containing information about the laptop.
     * @return The brand of the laptop.
     */

    public String parseBrand(String s) {
        String tmp = s.toLowerCase();

        if (tmp.contains("macbook"))
            return "Apple";
        else if (tmp.contains("lenovo") || tmp.contains("ideapad") || tmp.contains("thinkpad"))
            return "Lenovo";
        else if (tmp.contains("hp"))
            return "HP";
        else
            return "Unknown";
    }


    /**
     * Parses the release year of the laptop from the provided string.
     *
     * @param s The string containing information about the laptop.
     * @return The release year of the laptop.
     */

    public int parseYear(String s) {
        String tmp = s.toLowerCase();

        for (int year = 2000; year <= 2024; year++) {
            if (tmp.contains(String.valueOf(year))) {
                return year;
            }
        }

        return 0;
    }


    /**
     * Parses the model of the laptop from the provided string.
     *
     * @param s The string containing information about the laptop.
     * @return The model of the laptop.
     */
    public String parseModel(String s) {
        String tmp = s.toLowerCase();

        if (tmp.contains("macbook air")) {
            return "MacBook Air";
        } else if (tmp.contains("hp elite dragonfly")) {
            return "HP Elite Dragonfly";
        } else if (tmp.contains("hp elitebook x360")) {
            return "HP EliteBook x360";
        } else if (tmp.contains("hp probook x360")) {
            return "HP ProBook x360";
        } else if (tmp.contains("hp elitebook folio")) {
            return "HP EliteBook Folio";
        } else if (tmp.contains("hp elite x2")) {
            return "HP Elite x2";
        } else if (tmp.contains("hp envy x360")) {
            return "HP ENVY x360";
        } else if (tmp.contains("hp pavilion x360")) {
            return "HP Pavilion x360";
        } else if (tmp.contains("hp spectre x360")) {
            return "HP Spectre x360";
        } else if (tmp.contains("macbook pro")) {
            return "MacBook Pro";
        } else if (tmp.contains("thinkpad")) {
            return "ThinkPad";
        } else if (tmp.contains("ideapad")) {
            return "IdeaPad";
        } else if (tmp.contains("spectre")) {
            return "Spectre";
        } else if (tmp.contains("envy")) {
            return "ENVY";
        } else if (tmp.contains("pavilion")) {
            return "Pavilion";
        } else if (tmp.contains("elitebook")) {
            return "EliteBook";
        } else if(tmp.contains("victus")){
            return "Victus";
        } else if (tmp.contains("omen")) {
            return "OMEN";
        } else if (tmp.contains("chromebook")) {
            return "Chromebook";
        } else if (tmp.contains("stream")) {
            return "Stream";
        } else if (tmp.contains("legion")) {
            return "Legion";
        } else if (tmp.contains("probook")) {
            return "Probook";
        } else if (tmp.contains("v15")) {
            return "V 15";
        } else if (tmp.contains("v14")) {
            return "V 14";
        } else if (tmp.contains("hp 250")) {
            return "HP 250";
        } else if (tmp.contains("hp 255")) {
            return "HP 255";
        } else if (tmp.contains("zbook")) {
            return "ZBook";
        } else if (tmp.contains("latitude")) {
            return "Latitude";
        } else if (tmp.contains("surface laptop")) {
            return "Surface Laptop";
        } else if (tmp.contains("surface book")) {
            return "Surface Book";
        } else if (tmp.contains("xps")) {
            return "XPS";
        } else if (tmp.contains("rog strix")) {
            return "ROG Strix";
        } else if (tmp.contains("vivobook")) {
            return "VivoBook";
        } else if (tmp.contains("zenbook")) {
            return "ZenBook";
        } else if (tmp.contains("rog zephyrus")) {
            return "ROG Zephyrus";
        } else if (tmp.contains("gram")) {
            return "Gram";
        } else if (tmp.contains("nitro")) {
            return "Nitro";
        } else if (tmp.contains("legion")) {
            return "Legion";
        } else if (tmp.contains("ideacentre")) {
            return "IdeaCentre";
        } else if (tmp.contains("yoga")) {
            return "Yoga";
        } else if (tmp.contains("legion")) {
            return "Legion";
        } else if (tmp.contains("thinkcentre")) {
            return "ThinkCentre";
        } else if (tmp.contains("v series")) {
            return "V Series";
        } else if (tmp.contains("t series")) {
            return "T Series";
        } else if (tmp.contains("p series")) {
            return "P Series";
        } else if (tmp.contains("m series")) {
            return "M Series";
        } else if (tmp.contains("s series")) {
            return "S Series";
        } else if (tmp.contains("x series")) {
            return "X Series";
        } else {
            return "Unknown";
        }
    }

    public static double extractNumberFromString(String input) {
        Pattern pattern = Pattern.compile("(\\d+(\\.\\d+)?)");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return Double.parseDouble(matcher.group());
        }

        return -1; // Return -1 if no number is found
    }

    public static int extractNumberFromStringRam(String input) {
        Pattern pattern = Pattern.compile("(\\d+)");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }

        return -1; // Return -1 if no number is found
    }


    public static List<String> manipulateArrayRam() {
        List<String> arr = new ArrayList<>(Arrays.asList(
               "48", "32", "36" , "16", "12", "18",  "8", "6", "4", "3", "2", "1"
        ));


        List<String> Arr1 = new ArrayList<>();
        List<String> Arr2 = new ArrayList<>();
        List<String> Arr3 = new ArrayList<>();
        List<String> Arr4 = new ArrayList<>();

        for (String a : arr) {
            Arr2.add(a + "gb ram");
            Arr1.add(a + "gb");
            Arr3.add(a + " gb");
        }

        List<List<String>> overall = new ArrayList<>(Arrays.asList(Arr2, Arr1, Arr3, Arr4));
        List<String> flattenedOverall = new ArrayList<>();

        for (List<String> list : overall) {
            flattenedOverall.addAll(list);
        }

        return flattenedOverall;
    }

    /**
     * Parses the RAM size of the laptop from the provided string.
     *
     * @param s The string containing information about the laptop.
     * @return The RAM size of the laptop.
     */
    public static int extractRam(String s) {
        String tmp = s.toLowerCase();

        List<String> overall = manipulateArrayRam();

        for (String size : overall) {
            if (tmp.contains(size)) {
                return extractNumberFromStringRam(size);
            }
        }

        return -1;
    }


    /**
     * Extracts the RAM size of the laptop directly from the string for the Laptops Direct website
     *
     * @param input The string containing information about the laptop.
     * @return The extracted RAM size of the laptop.
     */
    public static int extractRamDirect(String input) {
        // Remove spaces from the input string
        String stringWithoutSpaces = input.replaceAll("\\s+", "");

        // Regular expression pattern to find RAM size (one, two, or three digits followed by "GB")
        Pattern pattern = Pattern.compile("(\\d{1,3})GB", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(stringWithoutSpaces);

        int ramSize = 0;
        if (matcher.find()) {
            String potentialRam = matcher.group(1);
            try {
                int parsedRam = Integer.parseInt(potentialRam);
                if (parsedRam < 60) {
                    ramSize = parsedRam;
                }
            } catch (NumberFormatException ignored) {
                // Ignore non-numeric RAM sizes
            }
        }

        return ramSize;
    }


    /**
     * Manipulates an array to include display size variations.
     *
     * @return A list of display sizes in different format as seen on the websites.
     */

    public static List<String> manipulateArrayDisplay() {
        List<String> arr = new ArrayList<>(Arrays.asList(
                "8.8",
                "10", "10.1", "10.2", "10.3", "10.4", "10.5", "10.6", "10.7", "10.8", "10.9", "10.0",
                "11", "11.1", "11.2", "11.3", "11.4", "11.5", "11.6", "11.7", "11.8", "11.9", "11.0",
                "12", "12.1", "12.2", "12.3", "12.4", "12.5", "12.6", "12.7", "12.8", "12.9", "12.0",
                "13", "13.1", "13.2", "13.3", "13.4", "13.5", "13.6", "13.7", "13.8", "13.9", "13.0",
                "14", "14.1", "14.2", "14.3", "14.4", "14.5", "14.6", "14.7", "14.8", "14.9", "14.0",
                "15", "15.1", "15.2", "15.3", "15.4", "15.5", "15.6", "15.7", "15.8", "15.9", "15.0",
                "16", "16.1", "16.2", "16.3", "16.4", "16.5", "16.6", "16.7", "16.8", "16.9", "16.0",
                "17", "17.1", "17.2", "17.3", "17.4", "17.5", "17.6", "17.7", "17.8", "17.9", "17.0",
                "18", "18.1", "18.2", "18.3", "18.4", "18.5", "18.6", "18.7", "18.8", "18.9", "18.0"
        ));

        List<String> Arr1 = new ArrayList<>();
        List<String> Arr2 = new ArrayList<>();
        List<String> Arr3 = new ArrayList<>();
        List<String> Arr4 = new ArrayList<>();
        List<String> Arr5 = new ArrayList<>();
        List<String> Arr6 = new ArrayList<>();
        List<String> Arr7 = new ArrayList<>();

        for (String a : arr) {
            Arr1.add(a + "-inch");
            Arr2.add(a + " inches");
            Arr3.add(a + " inch");
            Arr4.add(a + "in");
            Arr5.add(a + "”");
            Arr6.add(a + "\"");
            Arr7.add(a + "''");

        }

        List<List<String>> overall = new ArrayList<>(Arrays.asList(Arr1, Arr2, Arr3, Arr4, Arr5, Arr6, Arr7));
        List<String> flattenedOverall = new ArrayList<>();

        for (List<String> list : overall) {
            flattenedOverall.addAll(list);
        }

        return flattenedOverall;
    }


    /**
     * Parses the display size of the laptop from the provided string.
     *
     * @param s The string containing information about the laptop.
     * @return The display size of the laptop.
     */

    public static String parseDisplaySize(String s) {
        String tmp = s.toLowerCase();

        List<String> overall = manipulateArrayDisplay();

        for (String size : overall) {
            if (tmp.contains(size)) {
                return extractNumberFromString(size) + " inch";
            }
        }

        return "No Display";
    }




    /**
     * Parses the processor details of the laptop from the provided string.
     *
     * @param s The string containing information about the laptop.
     * @return The processor details of the laptop.
     */

    public String parseProcessor(String s){
        String tmp = s.toLowerCase();

        String[] allProcessors = {"i3", "i5", "i7", "i9", "X-series", "S-series", "ryzen 3", "ryzen 5", "ryzen 7", "ryzen 9", "m1", "m1 pro", "m1 max", "m2", "celeron", "m3", "mediatek", "pentium", "amd ryzen z1", "core 2", "core m" };

        for(String processor: allProcessors){
            if(tmp.contains(processor)){
                if(processor.contains("ryzen 1")){
                    return "Ryzen 1";
                } else if (processor.contains("ryzen 2")) {
                    return "Ryzen 2";
                } else if (processor.contains("ryzen 3")) {
                    return "Ryzen 3";
                } else if (processor.contains("ryzen 4")) {
                    return "Ryzen 4";
                } else if (processor.contains("ryzen 5")) {
                    return "Ryzen 5";
                } else if (processor.contains("ryzen 6")) {
                    return "Ryzen 6";
                }  else if (processor.contains("ryzen 7")) {
                    return "Ryzen 7";
                } else if (processor.contains("ryzen 8")) {
                    return "Ryzen 8";
                } else if(processor.contains("ryzen")) {
                    return "AMD Ryzen " + processor.substring(6).toUpperCase();
                } else if(processor.contains("m1")) {
                    return "Apple M1" + (processor.equals("m1") ? "" : " " + processor.substring(3).toUpperCase());
                } else if(processor.equals("m2")) {
                    return "Apple M2";
                } else if(processor.equals("celeron")) {
                    return "Intel Celeron";
                } else if(processor.equals("m3")) {
                    return "Apple M3";
                } else if(processor.equals("intel core m")) {
                    return "Intel Core M";
                } else if(processor.equals("intel core 2")) {
                    return "Intel Core 2";
                }  else if(processor.equals("pentium")) {
                    return "Intel Pentium";
                } else {
                    return "Intel Core " + processor.toUpperCase();
                }
            }
        }

        return "Processor details not recognized";
    }

    /**
     * Parses the operating system of the laptop from the provided string.
     *
     * @param s The string containing information about the laptop.
     * @return The operating system of the laptop.
     */

    public String parseOperatingSystem(String s) {
        String tmp = s.toLowerCase();

        if(tmp.contains("windows 11 pro")){
            return "Windows 11 Pro";
        } else if(tmp.contains("windows 11")) {
            return "Windows 11";
        }  else if(tmp.contains("windows 10 pro")) {
            return "Windows 10 Pro";
        }  else if(tmp.contains("windows 10/11")) {
            return "Windows 10/11";
        } else if(tmp.contains("windows 10")) {
            return "Windows 10";
        } else if(tmp.contains("windows 8")) {
            return "Windows 8";
        } else if(tmp.contains("windows 7")) {
            return "Windows 7";
        } else if(tmp.contains("windows xp")) {
            return "Windows XP";
        } else if(tmp.contains("chrome")) {
            return "Chrome OS";
        } else if(tmp.contains("win home")) {
            return "Win Home";
        } else if(tmp.contains("win pro")) {
            return "Win Pro";
        } else if(tmp.contains("macos")) {
            return "Mac Os";
        } else if(tmp.contains("apple os")) {
            return "Apple Os";
        } else if(tmp.contains("mac os")) {
            return "Mac Os";
        } else if(tmp.contains("appleos")) {
            return "Apple Os";
        } else {
            return "Unknown";
        }
    }
    /**
     * Extracts the price of the laptop from the provided string and get only the numbers.
     *
     * @param input The string containing information about the laptop price.
     * @return The price of the laptop.
     */

    public static double extractPrice(String input) {
            // Regular expression pattern to extract the numerical value
        Pattern pattern = Pattern.compile("£(\\d+(\\.\\d{1,2})?)");
        Matcher matcher = pattern.matcher(input);

        double price = 0.0;
        if (matcher.find()) {
            String priceStr = matcher.group(1);
            try {
                price = Double.parseDouble(priceStr);
            } catch (NumberFormatException ignored) {
                // Handle parsing errors if necessary
            }
        }

        return price;
    }


    /**
     * Retrieves the WebDriver instance used for scraping.
     *
     * @return The WebDriver instance.
     */
    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public int getCrawlDelay() {
        return crawlDelay;
    }

    public void setCrawlDelay(int crawlDelay) {
        this.crawlDelay = crawlDelay;
    }

    public LaptopDao getLaptopDao() {
        return laptopDao;
    }

    public void setLaptopDao(LaptopDao laptopDao) {
        this.laptopDao = laptopDao;
    }

    public boolean setRunThread() {
        return runThread;
    }

    public void stopThread() {
        this.runThread = false;
    }
}
