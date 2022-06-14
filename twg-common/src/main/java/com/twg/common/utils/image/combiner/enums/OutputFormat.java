package com.twg.common.utils.image.combiner.enums;

public enum OutputFormat {
    JPG("jpg"),
    PNG("png"),
    BMP("bmp");

    public final String name;

    OutputFormat(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}