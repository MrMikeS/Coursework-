package com.breakingcode.unoptimised.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.csv.CSVRecord;

import com.breakingcode.unoptimised.data.enums.Headers;

public class FoodSafetyData {

  /**
   * Data for each recorded row in FoodSafetyData CSV files.
   */

  // food hygiene rating scheme (FHRS) id
  private int fhrsid;

  // business headers
  private String localAuthorityBusinessID;
  private String businessName;
  private String businessType;
  private int businessTypeID;

  // address headers
  private String addressLine1;
  private String addressLine2;
  private String addressLine3;
  private String addressLine4;
  private String postCode;

  // rating headers
  public String ratingValue;
  public String ratingKey;
  private boolean ratingDateNil;

  // local authority headers
  private int localAuthorityCode;
  private String localAuthorityName;
  private String localAuthorityWebSite;
  private String localAuthorityEmailAddress;

  // utility headers
  private String schemeType;
  private boolean newRatingPending;

  // location headers (precise)
  private double longitude;
  private double latitude;

  // rating information headers
  public Date ratingDate;
  public int hygieneScore;
  public int structuralScore;
  public int confidenceInManagementScore;

  /**
   * Constructor for FoodSafetyData row
   * 
   * @param record CSVRecord passed from our parser
   */
  public FoodSafetyData(CSVRecord record) {

    // food hygiene rating scheme (FHRS) id
    this.fhrsid = tryParseInt(record.get(Headers.FHRSID));

    // business information
    this.localAuthorityBusinessID = record.get(Headers.LocalAuthorityBusinessID);
    this.businessName = record.get(Headers.BusinessName);
    this.businessType = record.get(Headers.BusinessType);
    this.businessTypeID = tryParseInt(record.get(Headers.BusinessTypeID));

    // address information
    this.addressLine1 = record.get(Headers.AddressLine1);
    this.addressLine2 = record.get(Headers.AddressLine2);
    this.addressLine3 = record.get(Headers.AddressLine3);
    this.addressLine4 = record.get(Headers.AddressLine4);
    this.postCode = record.get(Headers.PostCode);

    // rating information
    this.ratingValue = record.get(Headers.RatingValue);
    this.ratingKey = record.get(Headers.RatingKey);
    this.ratingDateNil = Boolean.parseBoolean(record.get(Headers.RatingDateNil.header));

    // local authority information
    this.localAuthorityCode = tryParseInt(record.get(Headers.LocalAuthorityCode));
    this.localAuthorityName = record.get(Headers.LocalAuthorityName);
    this.localAuthorityWebSite = record.get(Headers.LocalAuthorityWebSite);
    this.localAuthorityEmailAddress = record.get(Headers.LocalAuthorityEmailAddress);

    // utility information
    this.schemeType = record.get(Headers.SchemeType);
    this.newRatingPending = Boolean.parseBoolean(record.get(Headers.NewRatingPending));

    // precise location information
    this.longitude = tryParseDouble(record.get(Headers.Longitude.header));
    this.latitude = tryParseDouble(record.get(Headers.Latitude.header));

    // rating information (detailed)
    this.ratingDate = tryParseDate(record.get(Headers.RatingDate));
    this.hygieneScore = tryParseInt(record.get(Headers.Hygiene.header));
    this.structuralScore = tryParseInt(record.get(Headers.Structural.header));
    this.confidenceInManagementScore = tryParseInt(record.get(Headers.ConfidenceInManagement.header));
  }

  /**
   * Utility function to parse an Integer from a String.
   * 
   * Returns a NumberFormatException upon failure to do so.
   * 
   * @param value String to be parsed
   * @return Parsed Integer, upon success Value -1 is returned if method fails.
   */
  private int tryParseInt(String value) {
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      return -1;
    }
  }

  /**
   * Utility function to parse an Double from a String.
   * 
   * Returns a NumberFormatException upon failure to do so.
   * 
   * @param value String to be parsed
   * @return Parsed Double, upon success. Value -1 is returned if method fails.
   */
  private double tryParseDouble(String value) {
    try {
      return Double.parseDouble(value);
    } catch (NumberFormatException e) {
      return -1;
    }
  }

  /**
   * Utility function to parse a Date from a String. Expects a date in the format
   * of yyyy-DD-MM (as provided by the FHRS CSV files).
   * 
   * Return a ParseException upon failure to do so.
   * 
   * @param value String to be parsed
   * @return Parsed Date, upon success. null value if method fails.
   */
  private Date tryParseDate(String value) {
    try {
      SimpleDateFormat format = new SimpleDateFormat("yyyy-DD-MM");
      return format.parse(value);
    } catch (ParseException e) {
      return null;
    }
  }

  /**
   * Utility function to convert this FoodSafetyData object into a human-readable
   * string (for testing purposes).
   * 
   * @return Complete string
   */
  public String toCSVString() {
    return this.fhrsid + "," + this.localAuthorityBusinessID + "," + this.businessName + "," + this.businessType + ","
        + this.businessTypeID + "," + this.addressLine1 + "," + this.addressLine2 + "," + this.addressLine3 + ","
        + this.addressLine4 + "," + this.postCode + "," + this.ratingValue + "," + this.ratingKey + ","
        + this.ratingDateNil + "," + this.localAuthorityCode + "," + this.localAuthorityName + ","
        + this.localAuthorityWebSite + "," + this.localAuthorityEmailAddress + "," + this.schemeType + ","
        + this.newRatingPending + "," + this.longitude + "," + this.latitude + "," + this.ratingDate + ","
        + this.hygieneScore + "," + this.structuralScore + "," + this.confidenceInManagementScore;
  }

  // Getters

  public String getBusinessName() {
    return this.businessName;
  }

}
