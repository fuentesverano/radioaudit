package com.radioaudit.endpoint.model;


public class PageTaskModel {

    private String transactionCode;
    private Long transactionId;
    private Long productId;
    private String vendorCode;
    private String vendorDescription;
    private String specialOfferCode;
    private String specialOfferDescription;
    private String countryCode;
    private String countryName;
    private String brandCode;
    private String brandDescription;
    private String transactionVersion;
    private boolean canceled;
    private boolean finalized;
    private boolean vendorLCC;
    private boolean cancelable;

    //
    // Task data
    //
    private String taskName;
    private String taskId;
    //
    // Requests
    //
    private Long requestId;
    private String requestCode;
    private Long originalRequestId;

    //
    // Notifications
    //
    private boolean withNotification;

    //
    // Internal
    //
    private String controllername;


    //
    // General payment settings
    //
    private boolean restrictDepositPayment;


    //
    // CAC
    //
    private String cacOriginTaskId;
    private boolean test; // Used by cac.
    private boolean cac;
    private String cacTaskName;

    public String getBrandCode() {
        return this.brandCode;
    }

    public String getBrandDescription() {
        return this.brandDescription;
    }

    public String getCacOriginTaskId() {
        return this.cacOriginTaskId;
    }

    public String getCacTaskName() {
        return this.cacTaskName;
    }

    public String getControllername() {
        return this.controllername;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public String getCountryName() {
        return this.countryName;
    }

    public Long getOriginalRequestId() {
        return this.originalRequestId;
    }

    public Long getProductId() {
        return this.productId;
    }

    public String getRequestCode() {
        return this.requestCode;
    }

    public Long getRequestId() {
        return this.requestId;
    }


    public String getSpecialOfferCode() {
        return this.specialOfferCode;
    }

    public String getSpecialOfferDescription() {
        return this.specialOfferDescription;
    }

    public String getTaskId() {
        return this.taskId;
    }

    public String getTaskName() {
        return this.taskName;
    }


    public String getTransactionCode() {
        return this.transactionCode;
    }

    public Long getTransactionId() {
        return this.transactionId;
    }

    public String getTransactionVersion() {
        return this.transactionVersion;
    }

    public String getVendorCode() {
        return this.vendorCode;
    }

    public String getVendorDescription() {
        return this.vendorDescription;
    }

    public boolean isCac() {
        return this.cac;
    }

    public boolean isCancelable() {
        return this.cancelable;
    }

    public boolean isCanceled() {
        return this.canceled;
    }

    public boolean isFinalized() {
        return this.finalized;
    }

    public boolean isRestrictDepositPayment() {
        return this.restrictDepositPayment;
    }

    public boolean isTest() {
        return this.test;
    }

    public boolean isVendorLCC() {
        return this.vendorLCC;
    }

    public boolean isWithNotification() {
        return this.withNotification;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    public void setBrandDescription(String brandDescription) {
        this.brandDescription = brandDescription;
    }

    public void setCac(boolean cac) {
        this.cac = cac;
    }

    public void setCacOriginTaskId(String cacOriginTaskId) {
        this.cacOriginTaskId = cacOriginTaskId;
    }

    public void setCacTaskName(String cacTaskName) {
        this.cacTaskName = cacTaskName;
    }

    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public void setControllername(String controllername) {
        this.controllername = controllername;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public void setFinalized(boolean finalized) {
        this.finalized = finalized;
    }

    public void setOriginalRequestId(Long originalRequestId) {
        this.originalRequestId = originalRequestId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }


    public void setRestrictDepositPayment(boolean restrictDepositPayment) {
        this.restrictDepositPayment = restrictDepositPayment;
    }

    public void setSpecialOfferCode(String specialOfferCode) {
        this.specialOfferCode = specialOfferCode;
    }

    public void setSpecialOfferDescription(String specialOfferDescription) {
        this.specialOfferDescription = specialOfferDescription;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }


    public void setTest(boolean test) {
        this.test = test;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public void setTransactionVersion(String transactionVersion) {
        this.transactionVersion = transactionVersion;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }

    public void setVendorDescription(String vendorDescription) {
        this.vendorDescription = vendorDescription;
    }

    public void setVendorLCC(boolean vendorLCC) {
        this.vendorLCC = vendorLCC;
    }

    public void setWithNotification(boolean withNotification) {
        this.withNotification = withNotification;
    }

}
