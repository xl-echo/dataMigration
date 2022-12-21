package com.echo.one.common.factory;

import com.echo.one.utils.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author echo
 * @wechat t2421499075
 * @date 2022/12/15 12:25
 */
@Component
public class DataMigrationBeanFactory extends DefaultListableBeanFactory {

    private static final Logger logger = LoggerFactory.getLogger(DataMigrationBeanFactory.class);

    /**
     * 动态bean的注入和获取
     *
     * @param requiredType 注入bean类型
     * @param beanName     注入bean名称
     */
    public <T> T produceAndRegisterBean(Class<T> requiredType, String beanName) {

        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) SpringContextUtils.getApplicationContext();
        Object bean = null;
        try {
            bean = configurableApplicationContext.getBean(beanName);
        } catch (Exception e) {
            logger.info("traceId: {}, 当前定时任务在容器中没有对应的数据bean，继续往下执行！", beanName);
        }
        if (bean != null) {
            return (T) bean;
        }

        //获取BeanFactory
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getAutowireCapableBeanFactory();

        //创建bean信息.
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(requiredType);

        //动态注册bean.
        defaultListableBeanFactory.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());

        //获取动态注册的bean.
        return (T) configurableApplicationContext.getBean(beanName);
    }

    /**
     * 根据bean名称移除bean
     *
     * @param beanName bean名称
     */
    public void removeBean(String beanName) {
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) SpringContextUtils.getApplicationContext();

        Object bean = null;
        try {
            bean = configurableApplicationContext.getBean(beanName);
        } catch (Exception e) {
            logger.info("traceId: {}, 当前定时任务在容器中没有对应的数据bean，无需移除，继续往下执行！", beanName);
        }
        if (bean == null) return ;

        BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) configurableApplicationContext.getBeanFactory();
        beanFactory.removeBeanDefinition(beanName);
    }
}
