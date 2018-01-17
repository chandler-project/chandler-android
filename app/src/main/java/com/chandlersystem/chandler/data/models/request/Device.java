package com.chandlersystem.chandler.data.models.request;

import android.content.Context;

import com.chandlersystem.chandler.utilities.DeviceUtil;

public class Device {
    private String os;
    private String version;
    private String uuid;
    private String manufacturer;
    private String registrationId;

    public Device(Context context, String registrationId) {
        this.os = DeviceUtil.getOS();
        this.version = DeviceUtil.getVersion();
        this.uuid = DeviceUtil.getUUID(context);
        this.manufacturer = DeviceUtil.getCarrier(context);
        this.registrationId = registrationId;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }
}
