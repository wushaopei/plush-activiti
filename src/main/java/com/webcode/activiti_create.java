package com.webcode;

import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;

/**
 * @ClassName activiti_create.java
 * @Description TODO
 * @Author wushaopei
 * @Date 2020年3月8日
 * @Version 1.0
 */
public class activiti_create {
	@Test
    public void test_createDatabase() {
        // 创建流程引擎配置信息对象
        ProcessEngineConfiguration pec = ProcessEngineConfiguration
                .createStandaloneProcessEngineConfiguration();
        // 设置数据库的类型
        pec.setDatabaseType("mysql");
        // 设置创建数据库的方式
        pec.setDatabaseSchemaUpdate("true");
        // 设置数据库驱动
        pec.setJdbcDriver("com.mysql.jdbc.Driver");
        // 设置jdbcURL
        pec.setJdbcUrl("jdbc:mysql://49.234.197.103:3306/activiti-explorer?useUnicode=true&characterEncoding=UTF-8");
        // 设置用户名
        pec.setJdbcUsername("root");
        // 设置密码
        pec.setJdbcPassword("wushaopei");


        // 构建流程引擎对象
        ProcessEngine pe = pec.buildProcessEngine(); // 调用访方法才会创建数据表
        
        String pName = pe.getName();
        String ver = ProcessEngine.VERSION;
        System.out.println("ProcessEngine [" + pName + "] Version: [" + ver + "]");
        
        // 调用close方法时，才会删除
        pe.close();
    }
	
	@Test 
	public void test1(){
		
		
			
	}
}
