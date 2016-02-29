/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.waziup.waziupbroker;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.waziup.brokersms.BrokerWaziupEmail;

/**
 *
 * @author constantin Drabo
 *
 */
public class SimpleHLConsumer {
    
    static InputStream input;

    public static void main(String[] args) throws MessagingException {
        
        List<String> mydest = new ArrayList<>();
        mydest.add("waziuptest@gmail.com");
        mydest.add("panda.constantin@gmail.com");
        
        Properties props = new Properties();
        props.put("bootstrap.servers", "waziupserver:9092");  
        props.put("group.id", "test");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("enable.auto.commit", "true");
        props.put("session.timeout.ms", "10000");
        props.put("fetch.min.bytes", "5000");
        props.put("receive.buffer.bytes", "262144");
        props.put("max.partition.fetch.bytes","2097152");

        try {
            input = new FileInputStream("./src/main/java/org/waziup/brokersms/smtpconfig.properties");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SimpleHLConsumer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Defining a kafka consumer
        KafkaConsumer consumer = new KafkaConsumer(props);

        //Subscribe to a topic
        consumer.subscribe(Arrays.asList("emailtopic"));

        while (true) {
            
            ConsumerRecords<String, String> records = consumer.poll(10);

            for (ConsumerRecord<String, String> record : records) {          
                
                
              
                System.out.println(record.offset() + "---" + record.key() + "==> " +  record.value());
                BrokerWaziupEmail ms = new BrokerWaziupEmail(mydest, mydest, "Message from Kafka producer Waziup" + record.topic(), record.value());
                ms.sendSTMPEmail(input);
                
            }
        }

    }

}
