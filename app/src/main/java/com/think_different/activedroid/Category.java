package com.think_different.activedroid;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by oceancx on 15/3/4.
 */
@Table(name = "Categories")
public class Category extends Model {
    @Column(name = "Name")
    public String name;

}