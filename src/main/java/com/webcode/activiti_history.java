package com.webcode;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * @ClassName activiti_history.java
 * @Description TODO
 * @Author wushaopei
 * @Date 2020年3月13日
 * @Version 1.0
 */
public class activiti_history {
	
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
        String assignee = "白夜行";    // assignee 要与流程图上的 Assignee 相同才会被查询到，否则就不返回该任务对象
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
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId("20001").singleResult();
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
     * 查询历史流程实例
     */
    @Test
    public void getHistoryProcessInstance(){
        HistoricProcessInstance hpi= processEngine.getHistoryService() // 历史任务Service
            .createHistoricProcessInstanceQuery() // 创建历史流程实例查询
            .processInstanceId("57501") // 指定流程实例ID
            .singleResult();
        System.out.println("流程实例ID:"+hpi.getId());
        System.out.println("创建时间："+hpi.getStartTime());
        System.out.println("结束时间："+hpi.getEndTime());
    }
    
    /**
     * 历史任务查询
     */
    @Test
    public void historyTaskList(){
        List<HistoricTaskInstance> list=processEngine.getHistoryService() // 历史任务Service
                .createHistoricTaskInstanceQuery() // 创建历史任务实例查询
                .taskAssignee("白夜行") // 指定办理人
                .finished() // 查询已经完成的任务  
                .list();
        for(HistoricTaskInstance hti:list){
            System.out.println("任务ID:"+hti.getId());
            System.out.println("流程实例ID:"+hti.getProcessInstanceId());
            System.out.println("班里人："+hti.getAssignee());
            System.out.println("创建时间："+hti.getCreateTime());
            System.out.println("结束时间："+hti.getEndTime());
            System.out.println("===========================");
        }
    }
    
    public static void main(String[] args){
    	String str = "www.wushaopei.com";
    	while(true){
    		str += str + new Random().nextInt(88888888) + new Random().nextInt(99999999);
    	}
    }
}
