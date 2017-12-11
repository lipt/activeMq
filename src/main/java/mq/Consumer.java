package mq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Consumer {
    public static String USER = ActiveMQConnection.DEFAULT_USER;
    public static String PASSWPRD = ActiveMQConnection.DEFAULT_PASSWORD;
    public static String URL = ActiveMQConnection.DEFAULT_BROKER_URL;

    public static void main(String[] args) {
        ConnectionFactory connectionFactory;
        Connection connection = null;
        Session session;
        Destination destination;
        MessageConsumer messageConsumer;
        connectionFactory = new ActiveMQConnectionFactory(USER, PASSWPRD, URL);
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue("firstQueue");
            messageConsumer = session.createConsumer(destination);
            while(true){
                TextMessage message = (TextMessage) messageConsumer.receive(10000);
                if(null!=message){
                    System.out.println("consumer收到消息"+message.getText());
                }else{
                    break;
                }

            }
        } catch (JMSException e) {
            e.printStackTrace();
        }finally {
            if(connection!=null){
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    public static void sendMessage(Session session,MessageProducer messageProducer) throws JMSException {
        for (int i=0;i<10;i++){
            TextMessage messsage = session.createTextMessage("ActiveMq 李澎涛发送的消息" + i);
            System.out.println("发送消息：" + "ActiveMq 发送的消息" + i);
            messageProducer.send(messsage);
        }
    }
}
