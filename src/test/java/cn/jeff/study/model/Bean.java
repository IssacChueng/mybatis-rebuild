package cn.jeff.study.model;

public class Bean {
    private String id;
    
    private String desc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Bean{" +
                "id='" + id + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
