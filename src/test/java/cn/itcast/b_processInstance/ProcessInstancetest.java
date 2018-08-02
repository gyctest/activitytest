package cn.itcast.b_processInstance;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.util.FileCopyUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * 描述:
 * <p>
 *
 * @author gyc
 * @version 1.0
 * @date 2018/8/1
 */
public class ProcessInstancetest {

    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    /**
     *start instance
     */
    @Test
    public void startProcess(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey("helloworld");
        System.out.println("流程实例id:"+processInstance.getId());
        System.out.println("流程定义id:"+processInstance.getProcessDefinitionId());
        System.out.println(processInstance.getProcessDefinitionKey());
    }

    /**
     * 查看任务
     */
    @Test
    public void getTaskByPerson(){
        String assignee = "张三";

        TaskService taskService = processEngine.getTaskService();
        List<Task> taskList = taskService.createTaskQuery()
//                .taskAssignee(assignee)
//                .taskCandidateUser("")//组任务id查询
                .processDefinitionKey("helloworld")
                .list();
        for (Task task : taskList) {
            String format1 = MessageFormat.format("taskId={0},taskName={1},assignee={2},processDefinId={3},processInstancdId={4}.", task.getId(), task.getName(), task.getAssignee(), task.getProcessDefinitionId(), task.getProcessInstanceId());
            System.out.println(format1);

        }
    }

    /**
     * 完成 task
     */
    @Test
    public void finishTask(){
        TaskService taskService = processEngine.getTaskService();

        String taskId = "15005";
        taskService.complete(taskId);
        System.out.println("任务完成");
    }

    /**
     * 判断流程是否结束
     */
    @Test
    public void processInstanceIsFinish(){
        String processInstanceId = "32501";
        ProcessInstance processInstance = processEngine.getRuntimeService().createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if (processInstance != null) {
            System.out.println("流程未结束");
        }else{
            System.out.println("流程结束");
        }

    }


    /**
     * 查看历史实例
     */
    @Test
    public void queryHistInstance(){
        String processInstanceId = "32501";
        HistoricProcessInstance historicProcessInstance = processEngine.getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        System.out.println(historicProcessInstance.getDeploymentId()+",name:"+historicProcessInstance.getName());
    }
    /**
     * 查看历史实例
     */
    @Test
    public void queryHistTask(){
        String processInstanceId = "32501";
        List<HistoricTaskInstance> list = processEngine.getHistoryService().createHistoricTaskInstanceQuery().list();
        for (HistoricTaskInstance historicTaskInstance : list) {
            System.out.println("分配人："+historicTaskInstance.getAssignee()+",processInstanceId:"+historicTaskInstance.getProcessInstanceId());
        }
    }
}
