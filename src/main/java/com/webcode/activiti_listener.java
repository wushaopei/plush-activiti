package com.webcode;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.webcode.entity.Employee;
/**
 * @ClassName 监听器
 * @Description TODO
 * @Author wushaopei
 * @Date 2020年4月21日
 * @Version 1.0
 */
public class activiti_listener {
	
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
	                 .addClasspathResource("diagrams/MyProcess6.bpmn")  // 加载资源文件
	                 .deploy(); // 部署
	    System.out.println("流程部署ID:"+deployment.getId());
	    System.out.println("流程部署Name:"+deployment.getName());
	}
	
	
	/**
     * 启动流程
     */
    @Test
    public void startProcess() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("listener");
        String processInstanceId = processInstance.getId();
        String activityId = processInstance.getActivityId();
        String definitionId = processInstance.getProcessDefinitionId();
        
        System.out.println("流程实例ID："+processInstanceId);
        System.out.println("正在活动的节点ID："+activityId);
        System.out.println("流程定义ID："+definitionId);
    }
    
  //启动指定流程
    //	
	@Test
	public void testStartProcessInstanceWithVariable() {
		
		String processDefinitionKey = "listener";
		
		String processDefinitionId = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processDefinitionKey).latestVersion().singleResult().getId();
		
		//为启动流程实例，给流程变量指定值
		Map<String,Object> variables = new HashMap<>();
		variables.put("flag", "true");
		
		ProcessInstance processInstance = 
				runtimeService.startProcessInstanceById(processDefinitionId, variables);
		
		System.err.println("----------------"+processInstance);
	}
   
	/**
     * 查看任务
     */
    @Test
    public void findTask(){
        
        String assignee = "吴刚";  
        
        // 查询并且返回任务即可
        List<Task> taskList = taskService.createTaskQuery()  // 任务相关Service // 创建任务查询
                                     .processDefinitionKey("listener")
                                     .taskAssignee(assignee)// 指定某个人
                                     .orderByTaskCreateTime()
                                     .desc()
                                     .list();
        
        for(Task task:taskList){
            System.out.println("任务ID:"+task.getId()); 
            System.out.println("任务名称:"+task.getName());
            System.out.println("任务创建时间:"+task.getCreateTime());
            System.out.println("任务委派人:"+task.getAssignee());
            System.out.println("流程实例ID:"+task.getProcessInstanceId());
        }
    }
    
	/**
	 * 完成任务
	 */
	@Test
	public void completeTask2(){

		  Map<String, Object> variables=new HashMap<String,Object>();
	        //传入流程变量
	        variables.put("flag", "true");
	        processEngine.getTaskService() // 任务相关Service
	            .complete("237504", variables); //完成任务的时候，设置流程变量
	
	}
	
	
}
