package com.gyc.activity.test;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.List;

/**
 * 描述:
 * <p>
 *
 * @author gyc
 * @version 1.0
 * @date 2018/7/29
 */
public class JunitTest {
    /**
     * 部署流程
     *
     */
    @Test
    public void deploy() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("diagrams/helloworld.bpmn").addClasspathResource("diagrams/helloworld.png")
                .deploy();
        System.out.println("id:"+deploy.getId());
        System.out.println("name:"+deploy.getName());
    }

    /**
     *start instance
     */
    @Test
    public void startProcess(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey("helloworld");
        System.out.println("id:"+processInstance.getId());
        System.out.println("activity id:"+processInstance.getActivityId());
        System.out.println("流程定义id:"+processInstance.getProcessDefinitionId());
        System.out.println(processInstance.getProcessDefinitionKey());
    }

    /**
     * 查看任务
     */
    @Test
    public void getTask(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        TaskService taskService = processEngine.getTaskService();
        List<Task> taskList = taskService.createTaskQuery().taskAssignee("张三").list();
        for (Task task : taskList) {
            System.out.println("taskId:"+task.getId()+",taskName:"+task.getName());
        }
    }
    /**
     * 查看任务
     */
    @Test
    public void getTaskLisi(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        TaskService taskService = processEngine.getTaskService();
        List<Task> taskList = taskService.createTaskQuery().taskAssignee("李四").list();
        for (Task task : taskList) {
            System.out.println("taskId:"+task.getId()+",taskName:"+task.getName());
        }
    }

    /**
     * finish task
     */
    @Test
    public void finishTask(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        TaskService taskService = processEngine.getTaskService();

        String taskId = "2505";
        taskService.complete(taskId);
        System.out.println("任务完成");
    }
}
