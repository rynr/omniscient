package org.rjung.service.tag;

public class TagUsage {

    private final String tag;
    private final int count;

    public TagUsage(String tag, int count) {
        this.tag = tag;
        this.count = count;
    }

    public String getTag() {
        return tag;
    }

    public int getCount() {
        return count;
    }

}
