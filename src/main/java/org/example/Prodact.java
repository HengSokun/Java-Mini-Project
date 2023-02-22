package org.example;

public class Prodact {
 private String name;
 private int price;
 private int qty;
 private String date;

 public String getName() {
  return name;
 }

 public int getPrice() {
  return price;
 }

 public int getQty() {
  return qty;
 }

 public String getDate() {
  return date;
 }

 public void setName(String name) {
  this.name = name;
 }

 public void setPrice(int price) {
  this.price = price;
 }

 public void setQty(int qty) {
  this.qty = qty;
 }

 public void setDate(String date) {
  this.date = date;
 }

 public Prodact(String name, int price, int qty, String formattedDate) {
  this.name = name;
  this.price = price;
  this.qty = qty;
  this.date = formattedDate;
 }

 public Prodact(String name) {
  this.name = name;
 }

 public Prodact(String name, int price) {
  this.name = name;
  this.price = price;
 }

 public Prodact(String name, int price, int qty) {
  this.name = name;
  this.price = price;
  this.qty = qty;
 }
}
