package com.zjxfood.model;

/**
 * Created by Administrator on 2016/6/17.
 */
public class Student implements Comparable{
    private String name;
    private int age;

    public Student() {
    }
    public Student(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "name="+name;
    }

    @Override
    public int compareTo(Object o) {
        Student s = (Student) o;
        // 按name排序
        if (s.name.compareTo(this.name) > 0) {
            return 1;
        }
        if (s.name.compareTo(this.name) < 0) {
            return -1;
        }else {
            return 0;
        }

    }
}

