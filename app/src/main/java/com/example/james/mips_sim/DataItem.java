package com.example.james.mips_sim;

public class DataItem {

    int[] encoding = new int[6];
    String instName;
    int lineNum;

    public DataItem(String name, int line, int bits[])
    {
        instName = name;
        lineNum = line;
    }
}
