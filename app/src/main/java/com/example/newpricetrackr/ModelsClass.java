package com.example.newpricetrackr;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class ModelsClass {

    public class DistributorBase {
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        private String title;
        private String address;

    }


    public class Distributor {
        private String title;
        private String address;
        private int id;
        private List<Item> items;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<Item> getItems() {
            return items;
        }

        public void setItems(List<Item> items) {
            this.items = items;
        }
    }

    public class Item {
        private String name;
        private String itemtype;
        private double price;
        private int id;
        private int distributor_id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getItemtype() {
            return itemtype;
        }

        public void setItemtype(String itemtype) {
            this.itemtype = itemtype;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getDistributor_id() {
            return distributor_id;
        }

        public void setDistributor_id(int distributor_id) {
            this.distributor_id = distributor_id;
        }
    }


}
