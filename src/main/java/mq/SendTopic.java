package mq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class SendTopic {
    //http://blog.csdn.net/jason_deng/article/details/49778165
    public static String USER = ActiveMQConnection.DEFAULT_USER;
    public static String PASSWPRD = ActiveMQConnection.DEFAULT_PASSWORD;
    public static String URL = ActiveMQConnection.DEFAULT_BROKER_URL;

    public static void main(String[] args) {
        ConnectionFactory connectionFactory;
        Connection connection;
        Session session;
        MessageProducer messageProducer;
        Destination destination;

        connectionFactory = new ActiveMQConnectionFactory(USER, PASSWPRD, URL);
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            destination = session.createTopic("fristTopic");
            messageProducer = session.createProducer(destination);
            sendMessage(session,messageProducer);

        } catch (JMSException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void sendMessage(Session session, MessageProducer producer)
            throws Exception {
        for (int i = 1; i <= 10; i++) {
            TextMessage message = session
                    .createTextMessage("ActiveMq topic发送的消息" + i);
            // 发送消息到目的地方
            System.out.println("发送消息：" + "ActiveMq 发送的消息" + i);
            producer.send(message);
        }
    }
}
