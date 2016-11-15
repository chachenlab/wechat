/**
 * Copyright (c) 2015-2016, Javen Zhou  (javenlife@126.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.jfinal.weixin.demo;

import com.jfinal.core.Controller;

/**
 * @author Javen
 *         2016年1月15日
 */
public class IndexController extends Controller
{
    public void index()
    {
        setAttr("Expedia", "这里是测试...");
        render("index.jsp");
    }
}
