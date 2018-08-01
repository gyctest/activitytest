package cn.itcast.b_processDefinition;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.junit.Test;
import org.springframework.util.FileCopyUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
public class ProcessDefinitiontest {

    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    @Test
    public void deploymenProcessDefinition() {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deploy = repositoryService
                .createDeployment()
                .name("流程定义")
                .addClasspathResource("diagrams/helloworld.bpmn")
                .addClasspathResource("diagrams/helloworld.png")
                .deploy();
        System.out.println("流程定义id:" + deploy.getId());
        System.out.println("流程定义name:" + deploy.getName());
    }

    /**
     * 流程定义zip
     */
    @Test
    public void deploymenProcessDefinitionZip() {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("diagrams/helloworld.zip");
        ZipInputStream zip = new ZipInputStream(in);
        Deployment deploy = repositoryService
                .createDeployment()
                .name("流程定义")
                .addZipInputStream(zip)
                .deploy();
        System.out.println("流程定义id:" + deploy.getId());
        System.out.println("流程定义name:" + deploy.getName());
        try {
            zip.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 流程定义查询
     */
    @Test
    public void findProcessDefinition(){
        ProcessDefinitionQuery processDefinitionQuery = processEngine.getRepositoryService().createProcessDefinitionQuery();
        List<ProcessDefinition> hello = processDefinitionQuery.processDefinitionNameLike("hello%").orderByProcessDefinitionVersion().desc().list();
        for (ProcessDefinition processDefinition : hello) {
            System.out.println(processDefinition.getId()+",name:"+processDefinition.getName()+",version:"+processDefinition.getVersion());
        }
    }

    /**
     * 流程定义删除
     */
    @Test
    public void deleteProceDefiniton(){
        String deploymentId = "10001";
        processEngine.getRepositoryService().deleteDeployment(deploymentId);

        System.out.println("删除");
    }

    /**
     * 查看流程图
     */
    @Test
    public void viewPic() throws FileNotFoundException {
        String deploymentId = "10001";
        List<String> deploymentResourceNames = processEngine.getRepositoryService().getDeploymentResourceNames(deploymentId);
        for (String deploymentResourceName : deploymentResourceNames) {
            InputStream resourceAsStream = processEngine.getRepositoryService().getResourceAsStream(deploymentId, deploymentResourceName);
            FileOutputStream out = new FileOutputStream("X:\\cache\\"+deploymentResourceName);
            try {
                FileCopyUtils.copy(resourceAsStream,out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 查询最新版流程定义
     */
    @Test
    public void queryLastProcessDefin(){
        List<ProcessDefinition> list = processEngine.getRepositoryService().createProcessDefinitionQuery().orderByProcessDefinitionVersion().desc().list();
        ProcessDefinition processDefinition = list.get(0);
        System.out.println("id:"+processDefinition.getId()+",version="+processDefinition.getVersion());

    }

    /**
     * 删除流程定义，根据流程定义key
     */
    @Test
    public void deleteProcdefinitionByKey(){
        List<ProcessDefinition> helloworld = processEngine.getRepositoryService().createProcessDefinitionQuery().processDefinitionKey("helloworld").list();
        for (ProcessDefinition processDefinition : helloworld) {
            System.out.println(processDefinition.getId());
            processEngine.getRepositoryService().deleteDeployment(processDefinition.getDeploymentId(),true);
        }
    }
}
