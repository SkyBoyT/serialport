package com.skyboyt.serialport.entity;

/**
 * @Description: 串口参数实体类
 * @Auther: tzx
 * @Date: 2020/1/13 10:22
 */
public class SerialEntity {
    //串口号
    private String serialNumber;
    //波特率
    private int baudRate;
    //校验位
    private int checkOutBit;
    //数据位
    private int dataBit;
    //停止位
    private int stopBit;

    public SerialEntity() {
    }

    public SerialEntity(String serialNumber, int baudRate, int checkOutBit, int dataBit, int stopBit) {
        this.serialNumber = serialNumber;
        this.baudRate = baudRate;
        this.checkOutBit = checkOutBit;
        this.dataBit = dataBit;
        this.stopBit = stopBit;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public int getBaudRate() {
        return baudRate;
    }

    public void setBaudRate(int baudRate) {
        this.baudRate = baudRate;
    }

    public int getCheckOutBit() {
        return checkOutBit;
    }

    public void setCheckOutBit(int checkOutBit) {
        this.checkOutBit = checkOutBit;
    }

    public int getDataBit() {
        return dataBit;
    }

    public void setDataBit(int dataBit) {
        this.dataBit = dataBit;
    }

    public int getStopBit() {
        return stopBit;
    }

    public void setStopBit(int stopBit) {
        this.stopBit = stopBit;
    }
}
