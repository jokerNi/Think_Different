package com.think_different.activedroid;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.think_different.javabean.Statuse;

/**
 * Created by oceancx on 15/3/4.
 */

@Table(name = "Item")
public class Item extends Model {
//    @Column(name = "Name")
//    public String name  ;
//
//    @Column(name = "Category")
//    public Category category;
//
//
//
//    public Item() {
//        super();
//    }
//
//    public Item(String name, Category category) {
//        super();
//        this.name = name;
//        this.category = category;
//    }

    @Column(name = "Name")
    public int name;

    @Column(name = "Status")
    public String  statuse;

    public Item(int name,String statuse) {
        super();
        this.name = name;
        this.statuse = statuse;
    }
}
