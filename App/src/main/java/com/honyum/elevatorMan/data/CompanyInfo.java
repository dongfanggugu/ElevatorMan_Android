package com.honyum.elevatorMan.data;

/**
 * Created by changhaozhang on 16/1/8.
 */
public class CompanyInfo extends Atom {

    /**
     * 产生一个"其他"的维保公司
     * @return
     */
    public static CompanyInfo createOtherCompany() {

        CompanyInfo other = new CompanyInfo();
        other.setId("");
        other.setName("其他");
        return other;
    }

    private String id = "";

    private String name = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
