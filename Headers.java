package com.breakingcode.unoptimised.data.enums;

public enum Headers {
	FHRSID("FHRSID"), 
	LocalAuthorityBusinessID("LocalAuthorityBusinessID"), 
	BusinessName("BusinessName"),
	BusinessType("BusinessType"), 
	BusinessTypeID("BusinessTypeID"), 
	AddressLine1("AddressLine1"),
	AddressLine2("AddressLine2"),
	AddressLine3("AddressLine3"), 
	AddressLine4("AddressLine4"), 
	PostCode("PostCode"),
	RatingValue("RatingValue"), 
	RatingKey("RatingKey"), 
	RatingDate("RatingDate"),
	LocalAuthorityCode("LocalAuthorityCode"), 
	LocalAuthorityName("LocalAuthorityName"),
	LocalAuthorityWebSite("LocalAuthorityWebSite"), 
	LocalAuthorityEmailAddress("LocalAuthorityEmailAddress"),
	Hygiene("Scores/Hygiene"), 
	Structural("Scores/Structural"), 
	ConfidenceInManagement("Scores/ConfidenceInManagement"),
	SchemeType("SchemeType"), 
	NewRatingPending("NewRatingPending"), 
	Longitude("Geocode/Longitude"),
	Latitude("Geocode/Latitude"), 
	RightToReply("RightToReply"), 
	RatingDateNil("RatingDate/_xsi:nil");

	public String header;

	private Headers(String header) {
		this.header = header;
	}
}
