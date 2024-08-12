package org.ankit;

import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerDemo {

  private static final Logger log = LoggerFactory.getLogger(ProducerDemo.class.getSimpleName());

  public static void main(String[] args) {
    log.info("Start.......");
    //Create Producer Properties
    Properties properties = new Properties();

    //Connect to Localhost
    properties.setProperty("bootstrap.servers", "[::1]:9092");

    //set producer properties
    properties.setProperty("key.serializer", StringSerializer.class.getName());
    properties.setProperty("value.serializer", StringSerializer.class.getName());

    //Create the producer
    KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

    //Create a Producer Record
    ProducerRecord<String, String> producerRecord = new ProducerRecord<>("firstTopic", "Hello World from Java Code");

    //Send data: asynchronous operation
    producer.send(producerRecord);

    //flush the producer - tell the producer to send all data and block until done: synchronous operation
    producer.flush();

    //close the producer
    producer.close();

    log.info("End.......");
  }
}