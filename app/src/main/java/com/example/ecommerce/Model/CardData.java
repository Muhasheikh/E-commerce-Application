package com.example.ecommerce.Model;

public class CardData {
  public String cardNumber, cardExpiry, cardCvv, postalCode, phoneNo;

  public CardData() {
  }

  public String getCardNumber() {
    return cardNumber;
  }

  public void setCardNumber(String cardNumber) {
    this.cardNumber = cardNumber;
  }

  public String getCardExpiry() {
    return cardExpiry;
  }

  public void setCardExpiry(String cardExpiry) {
    this.cardExpiry = cardExpiry;
  }

  public String getCardCvv() {
    return cardCvv;
  }

  public void setCardCvv(String cardCvv) {
    this.cardCvv = cardCvv;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public String getPhoneNo() {
    return phoneNo;
  }

  public void setPhoneNo(String phoneNo) {
    this.phoneNo = phoneNo;
  }

  public CardData(String cardNumber, String cardExpiry, String cardCvv, String postalCode, String phoneNo){
    this.cardNumber=cardNumber;
    this.cardExpiry=cardExpiry;
    this.cardCvv=cardCvv;
    this.postalCode=postalCode;
    this.phoneNo=phoneNo;
    
  }
}
