package com.webcode;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * @ClassName activiti_delete_procdef.java
 * @Description TODO
 * @Author wushaopei
 * @Date 2020年3月13日
 * @Version 1.0
 */
public class activiti_upload_photo {
	
	/**
	 * 获取默认的流程引擎实例 会自动读取activiti.cfg.xml文件 
	 */
	private ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
	
	
	//定义一个成员变量
    RepositoryService repositoryService;
    
    RuntimeService runtimeService;
    
    TaskService taskService;
    
    HistoryService historyService;
    
    /**
     * 获取流程引擎
     */
    @Before
    public void setUp() throws Exception {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        repositoryService = processEngine.getRepositoryService();
        runtimeService = processEngine.getRuntimeService();
        taskService = processEngine.getTaskService();
        historyService = processEngine.getHistoryService();
        
    }
    
    /**
	 * 部署流程定义
	 */
	@Test
	public void deploy(){
	    // 获取部署对象
	    Deployment deployment=processEngine.getRepositoryService() // 部署Service
	                 .createDeployment()  // 创建部署
	                 .addClasspathResource("diagrams/MyProcess.bpmn")  // 加载资源文件
//	                 .addClasspathResource("diagrams/helloWorld.png")   // 加载资源文件
	                 .name("HelloWorld流程")  // 流程名称
	                 .deploy(); // 部署
	    System.out.println("流程部署ID:"+deployment.getId());
	    System.out.println("流程部署Name:"+deployment.getName());
	}
    
	/**
     * 通过流程部署ID获取流程图图片
     */
    @Test
    public void getImageById()throws Exception{
//        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
//        		.processDefinitionId("myProcess.png")
//        		.singleResult();
        InputStream inputStream=processEngine.getRepositoryService()
            .getResourceAsStream("40001", "myProcess.png"); // 根据流程部署ID和资源名称获取输入流
        FileUtils.copyInputStreamToFile(inputStream, new File("F:/myProcess.png"));
    }
	
}
