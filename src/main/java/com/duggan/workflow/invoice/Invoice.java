package com.duggan.workflow.invoice;

import java.io.Serializable;
import java.util.Date;

public class Invoice implements Serializable {

	private String invoiceNumber;
	private String businessPartner;
	private Double value;
	private Date invoiceDate;
	private Integer partnerCreditWorth;
	private Date dateRequired;
	private Integer priority;
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public String getBusinessPartner() {
		return businessPartner;
	}
	public void setBusinessPartner(String businessPartner) {
		this.businessPartner = businessPartner;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	public Date getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public Integer getPartnerCreditWorth() {
		return partnerCreditWorth;
	}
	public void setPartnerCreditWorth(Integer partnerCreditWorth) {
		this.partnerCreditWorth = partnerCreditWorth;
	}
	public Date getDateRequired() {
		return dateRequired;
	}
	public void setDateRequired(Date dateRequired) {
		this.dateRequired = dateRequired;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	
}
