package com.honyum.elevatorMan.data;

/**
 * Created by chang on 2015/11/5.
 */
public enum Organization {

    CHINA("全国", "http://123.57.10.16:8080/lift/mobile"),
    BEIJING("北京", "http://211.147.152.6:8080/lift/mobile"),
    SHANGHAI("上海", "http://123.57.10.16:8081/lift/mobile"),
    //SHOW("演示", "http://182.92.177.247:8080/lift/mobile"),
    //MAC("MAC", "http://192.168.20.182:8080/mobile"),
    TANG("唐", "http://192.168.0.82:8080/mobile"),
    MA("马", "http://192.168.0.81:8080/mobile");
   // LI("李", "http://192.168.0.81:80/lift/mobile");

    private String name;
    private String server;

    private Organization(String name, String server) {
        this.name = name;
        this.server = server;
    }

    /**
     * 根据组织名称返回ip地址
     *
     * @param name
     * @return
     */
    public static String getServerByOrg(String name) {
        for (Organization org : Organization.values()) {
            if (org.getName().equals(name)) {
                return org.getServer();
            }
        }
        return "";
    }

    /**
     * 通过ip获取组织名称
     *
     * @param server
     * @return
     */
    public static String getOrgByServer(String server) {
        for (Organization org : Organization.values()) {
            if (org.getServer().equals(server)) {
                return org.getName();
            }
        }
        return "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }
}
