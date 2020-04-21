package com.webcode;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.junit.Before;
import org.junit.Test;

/**
 * @ClassName activiti_zip.java
 * @Description TODO
 * @Author wushaopei
 * @Date 2020年3月13日
 * @Version 1.0
 */
public class activiti_zip {
	
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
     * 部署流程定义使用zip方式
     */
    @Test
    public void deployWithZip(){
        InputStream inputStream=this.getClass()  // 获取当前class对象
                            .getClassLoader()   // 获取类加载器
                            .getResourceAsStream("diagrams/MyProcess.zip"); // 获取指定文件资源流
        ZipInputStream zipInputStream=new ZipInputStream(inputStream); // 实例化zip输入流对象
        // 获取部署对象
        Deployment deployment=processEngine.getRepositoryService() // 部署Service
                     .createDeployment()  // 创建部署
                     .name("HelloWorld流程2")  // 流程名称
                     .addZipInputStream(zipInputStream)  // 添加zip是输入流
                     .deploy(); // 部署
        System.out.println("流程部署ID:"+deployment.getId());
        System.out.println("流程部署Name:"+deployment.getName());
    }
    
    
    /**
	 * 查询定义流程
	 */
	@Test
	public void findProcessDefinition(){
		//流程定义和部署相关的service
		RepositoryService repositoryService = processEngine.getRepositoryService();
		//创建一个流程定义的查询
		ProcessDefinitionQuery createProcessDefinitionQuery = repositoryService.createProcessDefinitionQuery();
		//createProcessDefinitionQuery.deploymentId(deploymentId)使用对象部署id查询
		//createProcessDefinitionQuery.processDefinitionId(processDefinitionId);使用流程定义的id查询
		//createProcessDefinitionQuery.processDefinitionKey(processDefinitionKey);使用流程定义的key查询
		//createProcessDefinitionQuery.processDefinitionNameLike(processDefinitionNameLike);使用流程定义的名称模糊查询
		//createProcessDefinitionQuery.processDefinitionKey(processDefinitionKey).list();返回一个集合
		//createProcessDefinitionQuery.processDefinitionId(processDefinitionId).singleResult();返回唯一结果集
		ProcessDefinitionQuery desc = createProcessDefinitionQuery.orderByProcessDefinitionVersion().desc();
		List<ProcessDefinition> list = desc.list();
		if(list!=null&&list.size()>0){
			for(ProcessDefinition ProcessDefinition:list){
				System.out.println("流程定义id"+ProcessDefinition.getId());
				System.out.println("流程定义名称"+ProcessDefinition.getName());
				
			}
		}
	}

	@Test
	public void deleteProcessDefinition(){
		/**
		 * 不带级联删除
		 *  只能删除没有启动的流程 ，流程启动无法删除
		 */
//		processEngine.getRepositoryService().deleteDeployment("1");
		//可以删除任何流程
		processEngine.getRepositoryService().deleteDeployment("2501",true);
		System.out.println("ok");
	}

}
