package com.leyou.sms.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * [????]
 * ModelName: 【】
 *
 * @author :  WeiPengFei
 * @version : 1.0
 * @Package com.linlong.utils
 * Create at:  2020/1/17 16:48
 * Company: 沈阳麟龙科技股份有限公司
 */
@Configuration
@Slf4j
@ConditionalOnExpression(value = "${config.rabbitmq: true}")
public class DirectRabbitConfig {

    public static final String RABBIT_TEMPLATE = "producerRabbitTemplate";
    public static final String LISTENER_CONTAINER_FACTORY = "hotStockRabbitListenerContainerFactory";
    /**
     * mq地址
     */
    @Value("${spring.rabbitmq.host}")
    private String host;
    /**
     * 端口号
     */
    @Value("${spring.rabbitmq.port}")
    private String port;
    /**
     * 用户名
     */
    @Value("${spring.rabbitmq.username}")
    private String username;
    /**
     * 密码
     */
    @Value("${spring.rabbitmq.password}")
    private String password;
    /**
     * 虚拟主机
     */
    @Value("${spring.rabbitmq.virtualHost}")
    private String virtualHost;
    /**
     * 消息确认 true为开启
     */
    @Value("${spring.rabbitmq.publisherConfirms}")
    private boolean publisherConfirms;

    @Value("${spring.rabbitmq.maxConcurrentConsumers}")
    private Integer maxConcurrentConsumers;

    @Value("${spring.rabbitmq.concurrentConsumers}")
    private Integer concurrentConsumers;

    @Value("${spring.rabbitmq.prefetchCount}")
    private Integer prefetchCount;

    /**
     * 配置RabbitMQ连接工厂
     *
     * @return
     */
    public CachingConnectionFactory connectionFactory() {
        log.info("DirectRabbitConfig rabbit连接地址={}", host + ":" + port);
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(host + ":" + port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        // 虚拟主机
        connectionFactory.setVirtualHost(virtualHost);
        // 必须要设置，发布确认
        connectionFactory.setPublisherConfirms(publisherConfirms);
        return connectionFactory;
    }

    //队列 起名：audit_task_queue
    public Queue TestDirectQueue() {
        return new Queue("audit_task_queue", true);  //true 是否持久
    }

    //Direct交换机 起名：amq.direct
    public DirectExchange TestDirectExchange() {
        // 是否持久化
        boolean durable = true;
        // 当所有消费客户端连接断开后，是否自动删除队列
        boolean autoDelete = false;
        return new DirectExchange("amq.direct");
    }

    //绑定  将队列和交换机绑定, 并设置用于匹配键：auditKey
    public Binding bindingDirect() {
        Binding auditKey = BindingBuilder.bind(TestDirectQueue()).to(TestDirectExchange()).with("auditKey");
        return auditKey;
    }

    @Bean(name = "hotStockRabbitAdmin")
    public RabbitAdmin rabbitAdmin() {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory());
        rabbitAdmin.declareExchange(TestDirectExchange());
        rabbitAdmin.declareQueue(TestDirectQueue());
        rabbitAdmin.declareBinding(bindingDirect());
        return rabbitAdmin;
    }

    /**
     * 配置RabbitTemplate
     * 因为要设置回调类，所以应是prototype类型，如果是singleton类型，则回调类为最后一次设置
     */
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Bean(name = DirectRabbitConfig.RABBIT_TEMPLATE)
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        return rabbitTemplate;
    }

    /**
     * 配置监听器工厂类
     */
    @Bean(name = DirectRabbitConfig.LISTENER_CONTAINER_FACTORY)
    public SimpleRabbitListenerContainerFactory listenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setAutoStartup(true);
        // 最大消费者个数
        factory.setMaxConcurrentConsumers(maxConcurrentConsumers);
        // 消费者个数
        factory.setConcurrentConsumers(concurrentConsumers);
        // 公平转发，预取数量
        factory.setPrefetchCount(prefetchCount);
        // 设置确认模式手工确认
//        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }
}

