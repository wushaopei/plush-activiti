package com.webcode;

import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * @ClassName activiti_create2.java
 * @Description TODO
 * @Author wushaopei
 * @Date 2020年3月8日
 * @Version 1.0
 */
public class activiti_helloworld {
	
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
     * 启动流程
     */
    @Test
    public void startProcess() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess");
        String processInstanceId = processInstance.getId();
        String activityId = processInstance.getActivityId();
        String definitionId = processInstance.getProcessDefinitionId();
        
        System.out.println("流程实例ID："+processInstanceId);
        System.out.println("正在活动的节点ID："+activityId);
        System.out.println("流程定义ID："+definitionId);
    }
    
    /**
     * 查询正在运行的实例
     */
    @Test
    public void queryExecution(){
        List<Execution> executionList = runtimeService.createExecutionQuery()  //创建正在执行的流程查询对象
                      .processDefinitionKey("myProcess")   //根据流程定义的key查询
                      .orderByProcessInstanceId()  //根据流程实例id排序
                      .desc()  //倒序
                      .list();  //查询出集合
        for(Execution execution: executionList){
            System.out.println("正在执行的流程对象的id: "+execution.getId());
            System.out.println("所属流程实例的id:"+execution.getProcessInstanceId());
            System.out.println("正在活动的节点的id: "+execution.getActivityId());
            System.out.println("任务名称: "+execution.getName());
        }
    }
	    
	
	/**
     * 根据办理人查询任务
     */
    @Test
    public void queryTaskByAssignee(){
        String assignee = "吴刚";    // assignee 要与流程图上的 Assignee 相同才会被查询到，否则就不返回该任务对象
        // 查询并且返回任务即可
        List<Task> taskList = taskService.createTaskQuery()  // 任务相关Service // 创建任务查询
                                     .processDefinitionKey("myProcess")
                                     .taskAssignee(assignee)// 指定某个人
                                     .orderByTaskCreateTime()
                                     .desc()
                                     .list();
        
        for(Task task: taskList){
	        System.out.println("任务ID:"+task.getId());
	        System.out.println("任务名称："+task.getName());
	        System.out.println("任务创建时间："+task.getCreateTime());
	        System.out.println("任务委派人："+task.getAssignee());
	        System.out.println("流程实例ID:"+task.getProcessInstanceId());
        }
    }
    
    /**
     * 查询当前流程实例状态
     */
    @Test
    public void queryProInstanceStateByProInstanceId(){
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId("120008").singleResult();
        if(processInstance == null){
            System.out.println("当前流程已经完成");
        }else{
            System.out.println("当前流程实例ID："+processInstance.getId());
            System.out.println("当前流程所处的位置："+processInstance.getActivityId());
        }
    }
    
	
	/**
	 * 完成任务
	 */
	@Test
	public void completeTask(){

		TaskQuery query = taskService.createTaskQuery();			//创建任务查询对象
		List<Task> list = query.processDefinitionKey("myProcess")	//指定流程定义
							   .taskAssignee("白夜行")				//指定委托人
							   .list();								//执行查询
		for (Task task : list) {
			System.out.println(task);
			//taskService某些框架下需要使用@Autowired注解装配
			taskService.complete(task.getId());						//完成任务，使任务进入下一步，如果是最后一步，就直接end
		}
	}
	
	/**
	 * 查询流程实例历史
	 */
	@Test
	public void queryHistory(){
		
		//1.创建流程实例的历史查询对象，historyService使用@Autowired注解注入
		HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery();
		HistoricProcessInstance instance = 
				query
					.processInstanceId("57501")	//流程实例的id
					.finished()					//调用这个方法后未完成的流程实例会返回null
					.singleResult();
		System.out.println(instance);
	}
	
}
