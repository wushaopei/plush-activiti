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
 * @ClassName 流程变量
 * @Description TODO
 * @Author wushaopei
 * @Date 2020年3月8日
 * @Version 1.0
 */
public class activiti_variable {
	
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
	                 .addClasspathResource("diagrams/MyProcess2.bpmn")  // 加载资源文件
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
     * 设置流程变量以及值
     */
    @Test
    public void setVariablesValues(){
        TaskService taskService=processEngine.getTaskService(); // 任务Service
        String taskId="102504"; // 任务id  对应表act_hi_actinst上的 TASK_ID
        taskService.setVariableLocal(taskId, "days", 2); // 存Integer类型
        taskService.setVariable(taskId, "date", new Date()); // 存日期类型
        taskService.setVariable(taskId, "reason", "发烧"); // 存字符串
        Employee employee=new Employee();
        employee.setId(1);
        employee.setName("李四");
        taskService.setVariable(taskId, "employee", employee);  // 存序列化对象
    }
    
    /**
	 * 获取流程变量数据
	 */
	@Test
	public void getVariableValues(){
		TaskService taskService=processEngine.getTaskService();//获取任务
		String taskId="102504";
		Integer day=(Integer) taskService.getVariable(taskId, "days");//获取请假天数
		Date date=(Date) taskService.getVariable(taskId, "date");//请假日期
		String reason=(String) taskService.getVariable(taskId, "reason");//请假原因
		
		Employee employee=(Employee) taskService.getVariable(taskId, "employee");//序列化对象
		System.out.println("请假天数："+day);
		System.out.println("请假日期："+date);
		System.out.println("请假原因:"+reason);
		System.err.println("请假对象："+employee.getId()+" —— "+employee.getName());
	}
	
	/**
	 * 完成任务
	 */
	@Test
	public void completeTask(){

		processEngine.getTaskService().complete("102504");
	}
	
	/**
	 * 获取流程变量数据
	 */
	@Test
	public void getVariableValues2(){
		TaskService taskService=processEngine.getTaskService();//获取任务
		String taskId="107502";
		Integer day=(Integer) taskService.getVariable(taskId, "days");//获取请假天数
		Date date=(Date) taskService.getVariable(taskId, "date");//请假日期
		String reason=(String) taskService.getVariable(taskId, "reason");//请假原因
		
		Employee employee=(Employee) taskService.getVariable(taskId, "employee");//序列化对象
		System.out.println("请假天数："+day);
		System.out.println("请假日期："+date);
		System.out.println("请假原因:"+reason);
		System.err.println("请假对象："+employee.getId()+" —— "+employee.getName());
	}
	
	/**
	 * 完成任务
	 */
	@Test
	public void completeTask2(){

		processEngine.getTaskService().complete("112502");
	}
	
	/**
	 * 完成任务
	 */
	@Test
	public void completeTask3(){

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
     * 查询当前流程实例状态
     */
    @Test
    public void queryProInstanceStateByProInstanceId(){
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId("87502").singleResult();
        if(processInstance == null){
            System.out.println("当前流程已经完成");
        }else{
            System.out.println("当前流程实例ID："+processInstance.getId());
            System.out.println("当前流程所处的位置："+processInstance.getActivityId());
        }
    }
    
    
	/**
	 * 设置多个流程变量数据
	 */
	@Test
	public void setVariableValue1(){
		TaskService taskService=processEngine.getTaskService();//获取任务
		String taskId="107502";//更加任务id知道是哪个人物，设置流程变量。可以更加查看任务方法查看任务的id，可以到数据库直接看
		
		//下面我们再测试一个额外的知识点，就是流程传输变量，这里我们再新建一个employee对象，对象有id 和name两个属性,还有就是序列化传输
		Employee employee=new Employee();
		employee.setId(1);
		employee.setName("zhangsan");
		taskService.setVariable(taskId, "employee",employee);//序列化对象
		
		Map<String, Object> variables=new HashMap<String,Object>();
		variables.put("days", 3);
		variables.put("date",new Date());
		variables.put("reason", "faShao2");
		variables.put("employee", employee);
		taskService.setVariables(taskId, variables);
		
	}
	
 
	/**
	 * 获取多个流程变量数据
	 */
	@Test
	public void getVariableValue(){
		TaskService taskService=processEngine.getTaskService();//获取任务
		String taskId="97502";
		
		Map<String, Object> variables=taskService.getVariables(taskId);
		Integer day=(Integer) variables.get("days");//获取请假天数
		Date date=(Date) variables.get("date");//请假日期
		String reason=(String) variables.get("reason");//请假原因
		
		Employee employee=(Employee) variables.get("employee");//序列化对象
		System.out.println("请假天数："+day);
		System.out.println("请假日期："+date);
		System.out.println("请假原因:"+reason);
		System.err.println("请假对象："+employee.getId()+",,,"+employee.getName());
	}
    
    /**
	 * 设置流程局部变量数据
	 */
	@Test
	public void setVariableLocalValue(){
		TaskService taskService=processEngine.getTaskService();//获取任务
		String taskId="107502";//更加任务id知道是哪个人物，设置流程变量。可以更加查看任务方法查看任务的id，可以到数据库直接看
		//下面设置任务的内容，比如请假流程，任务的第一节点也就是申请人要写请节哀的原因
		taskService.setVariableLocal(taskId, "days", 5);//请假天数
		taskService.setVariable(taskId, "date", new Date());//请假日期
		taskService.setVariable(taskId, "reason", "局部");//请假原因
		
		//下面我们再测试一个额外的知识点，就是流程传输变量，这里我们再新建一个student对象，对象有id 和name两个属性,还有就是序列化传输
//		Employee employee=new Employee();
//		employee.setId(1);
//		employee.setName("zhangsan");
//		taskService.setVariable(taskId, "employee",employee);//序列化对象
		
	}
	
	/**
	 * 获取流程变量数据
	 */
	@Test
	public void getVariableLocalValues(){
		TaskService taskService=processEngine.getTaskService();//获取任务
		String taskId="112502";
		Integer day=(Integer) taskService.getVariableLocal(taskId, "days");//获取请假天数
		Date date=(Date) taskService.getVariable(taskId, "date");//请假日期
		String reason=(String) taskService.getVariable(taskId, "reason");//请假原因
		
		Employee employee=(Employee) taskService.getVariable(taskId, "employee");//序列化对象
		System.out.println("请假天数："+day);
		System.out.println("请假日期："+date);
		System.out.println("请假原因:"+reason);
		System.err.println("请假对象："+employee.getId()+",,,"+employee.getName());
	}
    
	
	
	
}
