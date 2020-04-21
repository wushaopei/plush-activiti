package com.webcode;

import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.Before;
import org.junit.Test;

/**
 * @ClassName activiti_delete_procdef.java
 * @Description TODO
 * @Author wushaopei
 * @Date 2020年3月13日
 * @Version 1.0
 */
public class activiti_delete_byKey {
	
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
//	                 .name("HelloWorld流程")  // 流程名称
	                 .deploy(); // 部署
	    System.out.println("流程部署ID:"+deployment.getId());
	    System.out.println("流程部署Name:"+deployment.getName());
	}
    
	/**
	 * 删除所有Key相同的流程定义
	 * @throws Exception
	 */
	@Test
	public void deleteByKey()throws Exception{
	    List<ProcessDefinition> pdList=processEngine.getRepositoryService()  // 获取service类
	            .createProcessDefinitionQuery() // 创建流程定义查询
	            .processDefinitionKey("myProcess") // 根据Key查询
	            .list();
	    for(ProcessDefinition pd:pdList){  // 遍历集合 获取流程定义的每个部署Id，根据这个id来删除所有流程定义
	        processEngine.getRepositoryService()
	        .deleteDeployment(pd.getDeploymentId(), true); 
	    }
	}
}
