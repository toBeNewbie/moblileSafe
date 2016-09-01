package com.example.mobilesafe.bean;

public class PhoneServiceNumberBean {

	private String serviceName;
	private String serviceNumber;
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getServiceNumber() {
		return serviceNumber;
	}
	public void setServiceNumber(String serviceNumber) {
		this.serviceNumber = serviceNumber;
	}
	@Override
	public String toString() {
		return "PhoneServiceNumberBean [serviceName=" + serviceName
				+ ", serviceNumber=" + serviceNumber + "]";
	}
	
	
}
