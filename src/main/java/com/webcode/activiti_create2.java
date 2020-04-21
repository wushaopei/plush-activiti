package com.webcode;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;

/**
 * @ClassName activiti_create2.java
 * @Description TODO
 * @Author wushaopei
 * @Date 2020年3月8日
 * @Version 1.0
 */
public class activiti_create2 {
	/**
	 * 使用xml配置 简化
	 */
	@Test
	public void testCreateTableWithXml(){
	    // 引擎配置
	    ProcessEngineConfiguration pec=ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
	    // 获取流程引擎对象
	    ProcessEngine processEngine=pec.buildProcessEngine();
	    
	   
        String pName = processEngine.getName();
        String ver = ProcessEngine.VERSION;
        System.out.println("ProcessEngine [" + pName + "] Version: [" + ver + "]");
        
	}
}
