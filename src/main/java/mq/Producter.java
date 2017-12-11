package mq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Producter {
    public static String USER = ActiveMQConnection.DEFAULT_USER;
    public static String PASSWPRD = ActiveMQConnection.DEFAULT_PASSWORD;
    public static String URL = ActiveMQConnection.DEFAULT_BROKER_URL;

    public static void main(String[] args) {
        ConnectionFactory connectionFactory;
        Connection connection = null;
        Session session;
        Destination destination;
        MessageProducer messageProducer;
        connectionFactory = new ActiveMQConnectionFactory(USER, PASSWPRD, URL);
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue("firstQueue");
            messageProducer = session.createProducer(destination);
            sendMessage(session,messageProducer);
            session.commit();
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
            TextMessage messsage = session.createTextMessage("ActiveMq  producter产生的消息" + i);
            System.out.println("发送消息：" + "ActiveMq 发送的消息" + i);
            messageProducer.send(messsage);
        }
    }
}
