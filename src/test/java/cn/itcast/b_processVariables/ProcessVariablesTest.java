package cn.itcast.b_processVariables;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述:
 * <p>
 *
 * @author gyc
 * @version 1.0
 * @date 2018/8/2 0002
 */
public class ProcessVariablesTest {

    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    @Test
    public void deploymentVarables() {

        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deploy = repositoryService
                .createDeployment()
                .name("流程定义(变量)")
                .addClasspathResource("diagrams/helloworld.bpmn")
                .addClasspathResource("diagrams/helloworld.png")
                .deploy();
        System.out.println("流程定义id:" + deploy.getId());
        System.out.println("流程定义name:" + deploy.getName());
    }

    /**
     * start instance
     */
    @Test
    public void startProcess() {
        String key = "myVariables";
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey(key);
        String format = MessageFormat.format("processInstanceId={0},processDefinitionId={1},processDefinitionKey={2}", processInstance.getId(), processInstance.getProcessDefinitionId(), processInstance.getProcessDefinitionKey());
        System.out.println(format);
    }

    /**
     * 设置流程变量
     */
    @Test
    public void setVariables() {
        String assignee = "张三";
        List<Task> list = processEngine.getTaskService().createTaskQuery().taskAssignee(assignee).orderByTaskCreateTime().desc().list();
        if (list.isEmpty()) {
            return;
        }
        Task task = list.get(0);
        Map variablesMap = new HashMap();
        variablesMap.put("请假人", "yyyy");
        variablesMap.put("请假天数", 5);
        variablesMap.put("请假日期", new Date());
        processEngine.getTaskService().setVariables(task.getId(), variablesMap);

        processEngine.getTaskService().complete(task.getId());
        System.out.println("任务启动完成");
    }

    /**
     * 获取流程变量
     */
    @Test
    public void getVariables() {
        String assignee = "李四";
        List<Task> list = processEngine.getTaskService().createTaskQuery().taskAssignee(assignee).orderByTaskCreateTime().desc().list();
        if (list.isEmpty()) {
            return;
        }
        Task task = list.get(0);
        Map<String, Object> variables = processEngine.getTaskService().getVariables(task.getId());
        for (Map.Entry<String, Object> stringObjectEntry : variables.entrySet()) {
            String format = MessageFormat.format("varablesName={0},varablesValue={1}.", stringObjectEntry.getKey(), stringObjectEntry.getValue());
            System.out.println(format);
        }
        System.out.println(processEngine.getTaskService().getVariablesLocal(task.getId()));
//        processEngine.getTaskService().setVariableLocal(task.getId(),"请假人","bbb");
//        processEngine.getTaskService().complete(task.getId());
        System.out.println("任务启动完成");
    }

    @Test
    public void getVariablesWw() {
        String assignee = "王五";
        List<Task> list = processEngine.getTaskService().createTaskQuery().taskAssignee(assignee).orderByTaskCreateTime().desc().list();
        if (list.isEmpty()) {
            return;
        }
        Task task = list.get(0);
        Map<String, Object> variables = processEngine.getTaskService().getVariables(task.getId());
        for (Map.Entry<String, Object> stringObjectEntry : variables.entrySet()) {
            String format = MessageFormat.format("varablesName={0},varablesValue={1}.", stringObjectEntry.getKey(), stringObjectEntry.getValue());
            System.out.println(format);
        }
//        processEngine.getTaskService().setVariable(task.getId(),"请假天数",3);
//        processEngine.getTaskService().setVariable(task.getId(),"王五参数","通过");
//        processEngine.getTaskService().complete(task.getId());
        System.out.println("任务启动完成");
    }

}
