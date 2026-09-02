package com.liuj.huabo;

public class LanguageBean {
    private String name;
    private String passnbr;
    private String tkttyp;
    private String usesta;
    private Boolean cancheck;
    private Boolean checked;
    private int ichk;          // 0=未选, 1=已选
    private String uuid;

    public LanguageBean() {
    }

    public LanguageBean(String name, String passnbr, String tkttyp, String usesta,
                        Boolean cancheck, Boolean checked, String uuid) {
        this.name = name;
        this.passnbr = passnbr;
        this.tkttyp = tkttyp;
        this.usesta = usesta;
        this.cancheck = cancheck;
        this.checked = checked;
        this.uuid = uuid;
        // 构造时同步设置 ichk
        this.ichk = (checked != null && checked) ? 1 : 0;
    }

    public String getName() {
        return name;
    }

    public String getUuid() {
        return uuid;
    }

    public String getPassnbr() {
        return passnbr;
    }

    public String getTkttyp() {
        return tkttyp;
    }

    public String getUsesta() {
        return usesta;
    }

    public Boolean getCanCheck() {
        return cancheck != null && cancheck;
    }

    public Boolean getChecked() {
        return ichk == 1;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassnbr(String passnbr) {
        this.passnbr = passnbr;
    }

    public void setTkttyp(String tkttyp) {
        this.tkttyp = tkttyp;
    }

    public void setUsesta(String usesta) {
        this.usesta = usesta;
    }

    public void setCanCheck(Boolean cancheck) {
        this.cancheck = cancheck;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
        this.ichk = (checked != null && checked) ? 1 : 0;
    }
}