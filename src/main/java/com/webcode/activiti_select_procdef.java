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
 * @ClassName activiti_select_procdef.java
 * @Description TODO
 * @Author wushaopei
 * @Date 2020年3月13日
 * @Version 1.0
 */
public class activiti_select_procdef {
	
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
	 * 查询流程定义 返回流程定义集合 ---对应act_re_procdef
	 */
	@Test
	public void listAll(){
	    List<ProcessDefinition> pdList=processEngine.getRepositoryService() // 获取service类
	        .createProcessDefinitionQuery() // 创建流程定义查询
	        .processDefinitionKey("myProcess") // 通过key查询
	        .list(); // 返回一个集合
	    for(ProcessDefinition pd:pdList){
	    	System.out.println("流程定义ID:"+pd.getId());//流程定义的key+版本+随机生成数
	        System.out.println("流程定义名称:"+pd.getName());//对应HelloWorld.bpmn文件中的name属性值
	        System.out.println("流程定义的key:"+pd.getKey());//对应HelloWorld.bpmn文件中的id属性值
	        System.out.println("流程定义的版本:"+pd.getVersion());//当流程定义的key值相同的情况下，版本升级，默认从1开始
	        System.out.println("资源名称bpmn文件:"+pd.getResourceName());
	        System.out.println("资源名称png文件:"+pd.getDiagramResourceName());
	        System.out.println("部署对象ID:"+pd.getDeploymentId());
	        System.out.println("################################");
	    }
	}
	
	 /**
	   * 通过ID查询当个流程定义
		*/
	@Test
	public void getById(){
	   ProcessDefinition pd=processEngine.getRepositoryService() // 获取service类
	            .createProcessDefinitionQuery() // 创建流程定义查询
	            .processDefinitionId("myProcess:10:50004") // 通过id查询
	            .singleResult(); // 查询返回当个结果
		   System.out.println("流程定义ID:"+pd.getId());//流程定义的key+版本+随机生成数
	       System.out.println("流程定义名称:"+pd.getName());//对应HelloWorld.bpmn文件中的name属性值
	       System.out.println("流程定义的key:"+pd.getKey());//对应HelloWorld.bpmn文件中的id属性值
	       System.out.println("流程定义的版本:"+pd.getVersion());//当流程定义的key值相同的情况下，版本升级，默认从1开始
	       System.out.println("资源名称bpmn文件:"+pd.getResourceName());
	       System.out.println("资源名称png文件:"+pd.getDiagramResourceName());
	       System.out.println("部署对象ID:"+pd.getDeploymentId());
	       System.out.println("################################");
	}
}
