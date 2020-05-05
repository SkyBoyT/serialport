package com.skyboyt.serialport.utils;

import com.skyboyt.serialport.entity.SerialEntity;
import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;

/**
 * @Description: 串口处理工具类
 * @Auther: tzx
 * @Date: 2020/1/13 10:29
 */
@SuppressWarnings("all")
public class SerialPortUtils implements SerialPortEventListener {

    //RS232串口
    private SerialPort serialPort;
    // 检测系统中可用的通讯端口类
    private CommPortIdentifier commPortId;
    // 当前所有可用的串口
    private Enumeration<CommPortIdentifier> portList;
    // 输入流
    private InputStream inputStream;
    // 输出流
    private OutputStream outputStream;


    /**
     * 初始化串口
     * @param serialEntity
     */
    public void init(SerialEntity serialEntity) {
        // 获取系统中所有的通讯端口
        portList = CommPortIdentifier.getPortIdentifiers();
        // 记录是否含有指定串口
        boolean isExsist = false;
        // 循环通讯端口
        while (portList.hasMoreElements()) {
            commPortId = portList.nextElement();
            // 判断是否是串口
            if (commPortId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                // 比较串口名称是否是指定串口
                if (serialEntity.getSerialNumber().equals(commPortId.getName())) {
                    // 串口存在
                    isExsist = true;
                    // 打开串口
                    try {
                        // 阻塞时等待的2s
                        serialPort = (SerialPort) commPortId.open(Object.class.getSimpleName(), 2000);
                        // 设置串口监听
                        serialPort.addEventListener(this);
                        // 设置串口数据时间有效(可监听)
                        serialPort.notifyOnDataAvailable(true);
                        // 设置当通信中断时唤醒中断线程
                        serialPort.notifyOnBreakInterrupt(true);
                        // 设置串口通讯参数:波特率，数据位，停止位,校验方式
                        serialPort.setSerialPortParams(serialEntity.getBaudRate(), serialEntity.getDataBit(),
                                serialEntity.getStopBit(), serialEntity.getCheckOutBit());
                    } catch (PortInUseException e) {
                        ShowUtils.warningMessage("端口被占用");
                    } catch (TooManyListenersException e) {
                        ShowUtils.warningMessage("监听器过多");
                    } catch (UnsupportedCommOperationException e) {
                        ShowUtils.warningMessage("不支持的COMM端口操作异常");
                    }
                    // 结束循环
                    break;
                }
            }
        }
        // 若不存在该串口则抛出异常
        if (!isExsist) {
            ShowUtils.warningMessage("不存在该串口！");
        }
    }

    /**
     * 关闭串口
     */
    public void closeSerialPort() {
        if (serialPort != null) {
            serialPort.notifyOnDataAvailable(false);
            serialPort.removeEventListener();
            if (inputStream != null) {
                try {
                    inputStream.close();
                    inputStream = null;
                } catch (IOException e) {
                    ShowUtils.errorMessage("关闭输入流时发生IO异常");
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                    outputStream = null;
                } catch (IOException e) {
                    ShowUtils.errorMessage("关闭输出流时发生IO异常");
                }
            }
            serialPort.close();
            serialPort = null;
        }
    }

    /**
     * 实现接口SerialPortEventListener中的方法，监听端口事件
     * @param serialPortEvent
     */
    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        switch (serialPortEvent.getEventType()) {
            // 1.串口存在有效数据
            case SerialPortEvent.DATA_AVAILABLE:
                readComData();
                break;
            // 2.输出缓冲区已清空
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                break;
            // 3.清除待发送数据
            case SerialPortEvent.CTS:
                break;
            // 4.待发送数据准备好了
            case SerialPortEvent.DSR:
                break;
            // 5.振铃指示
            case SerialPortEvent.RI:
                break;
            // 6.载波检测
            case SerialPortEvent.CD:
                break;
            // 7.溢位（溢出）错误
            case SerialPortEvent.OE:
                break;
            // 8.奇偶校验错误
            case SerialPortEvent.PE:
                break;
            // 9.帧错误
            case SerialPortEvent.FE:
                break;
            // 10.通讯中断
            case SerialPortEvent.BI:
                ShowUtils.errorMessage("与串口设备通讯中断");
                break;

            default:
                break;
        }
    }

    /**
     * 读取串口返回的数据
     */
    public byte[] readComData() {
        byte[] bytes = {};
        try {
            inputStream = serialPort.getInputStream();
            // 缓冲区大小为一个字节
            byte[] readBuffer = new byte[1];
            int bytesNum = inputStream.read(readBuffer);
            while (bytesNum > 0) {
                bytes = ArrayUtils.concat(bytes, readBuffer);
                bytesNum = inputStream.read(readBuffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                    inputStream = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }

    public static void main(String[] args) {
        SerialPortUtils serialPortUtils = new SerialPortUtils();
        SerialEntity serialEntity = new SerialEntity("COM4", 2400, 0, 8, 1);
        serialPortUtils.init(serialEntity);
        while (true) {
            byte[] data = serialPortUtils.readComData();
            String out = new String(data);
            System.out.print(out);
            //RH=34.2，T=+22.9

        }
    }
}
