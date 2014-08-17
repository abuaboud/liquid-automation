package org.liquidbot.bot.script.api.util;

/**
 * Created by Kenneth on 8/6/2014.
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import com.google.gson.Gson;

public class PriceGrabber {

    private static final String URL_BASE = "http://forums.zybez.net/runescape-2007-prices/api/item/";
    private int itemId;
    private String name;

    private int average;
    private int low;
    private int high;

    private String imageURL;

    private Offer[] offers;
    private Price price;

    public Offer[] getOffers() {
        return offers;
    }

    public Price getRaw() {
        return price;
    }

    public String getImageURL() {
        return imageURL;
    }

    public int getLow() {
        return low;
    }

    public int getHigh() {
        return high;
    }

    public int getAverage() {
        return average;
    }

    public int getId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public PriceGrabber(final String itemName) {
        this.name = itemName;
        price = new Gson().fromJson(getJson(getName().trim()), Price.class);

        low = price.getLow();
        average = price.getAverage();
        high = price.getHigh();
        name = price.getName();
        imageURL = price.getImageURL();
        itemId = price.getId();
        offers = price.getOffers();
    }

    private String getJson(String end) {
        try {
            URL url = new URL(URL_BASE + end.toLowerCase().replaceAll(" ", "%20"));
            URLConnection urlconn = url.openConnection();
            urlconn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.57 Safari/537.36");
            urlconn.setRequestProperty("Connection", "close");
            BufferedReader in = new BufferedReader(new InputStreamReader(urlconn.getInputStream()));
            return in.readLine();
        } catch(Exception a) {
            System.out.println("Error connecting to server.");
        }
        return null;
    }


    public class Offer {

        private int selling;
        private int quantity;
        private int price;
        private long date;
        private String rs_name;
        private String contact;
        private String notes;

        public boolean isSelling() {
            return selling == 1;
        }

        public int getQuantity() {
            return quantity;
        }

        public int getPrice() {
            return price;
        }

        public String getDate() {
            Date d = new Date(date * 1000L);
            return d.toString();
        }

        public String getRSName() {
            return rs_name;
        }

        public String getContact() {
            return contact;
        }

        public String getNotes() {
            return notes;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(getRSName()).append(" is ").append(isSelling() ? "selling" : "buying").append(" ").append(getQuantity());
            sb.append(" of the item ").append(" for ").append(getPrice()).append(" on ").append(getDate()).append("\t");
            sb.append("Contact ").append(getContact()).append("\t");
            sb.append("Notes: ").append(getNotes());
            return sb.toString();
        }
    }

    public class Price {

        private int id;
        private String name;
        private String image;
        private double average;
        private double recent_high;
        private double recent_low;

        private Offer[] offers;

        public Offer[] getOffers() {
            return offers;
        }

        public int getHigh() {
            return (int) recent_high;
        }

        public int getLow() {
            return (int) recent_low;
        }

        public String getImageURL() {
            return image.replaceAll(" ", "_");
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getAverage() {
            return (int) average;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("id=").append(getId()).append("\t");
            sb.append("name=").append(getName()).append("\t");
            sb.append("image=").append(getImageURL()).append("\t");
            sb.append("average=").append(getAverage()).append("\t");
            sb.append("high=").append(getHigh()).append("\t");
            sb.append("low=").append(getLow()).append("\t");
            sb.append("offers=").append(getOffers().length);
            return sb.toString();
        }

    }
}