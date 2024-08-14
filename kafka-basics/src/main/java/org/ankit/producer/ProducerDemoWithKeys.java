package org.ankit.producer;

import java.util.Properties;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class demonstrates a Kafka producer with keys.
 * It sends messages to a Kafka topic asynchronously and logs the metadata (i.e. partition, metadata etc.) of successfully sent messages.
 *
 * @author Ankit
 * @version 1.0
 */
public class ProducerDemoWithKeys {
  private static final Logger log = LoggerFactory.getLogger(ProducerDemo.class.getSimpleName());

  public static void main(String[] args) {
    log.info("Starting Kafka Producer.......");
    //Create Producer Properties
    Properties properties = new Properties();

    //Connect to Secure server
    properties.setProperty("bootstrap.servers", "https://liked-cougar-5356-us1-kafka.upstash.io:9092");
    properties.setProperty("security.protocol", "SASL_SSL");
    properties.setProperty("sasl.jaas.config", "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"bGlrZWQtY291Z2FyLTUzNTYkng_vJc9e_qR-MYNRgIEkapzv9eP6PcEShlmRt5E\" password=\"OTY1MGY5ZTEtOWM3ZC00YjVhLThhZTItMmY0YWYxOWVjZjhl\";");
    properties.setProperty("sasl.mechanism", "SCRAM-SHA-256");


    //set producer properties
    properties.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    properties.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

    //Create the producer
    KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

    for(int i=0; i<2; i++) {
      for(int j=0; j<5; j++) {
        //Same key goes to same partition
        String key = "id_" + j;
        String value = "Hello World " + j;
        String topic = "firstTopic";

        //Create a Producer Record
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, key, value);

        //Send data: asynchronous operation
        producer.send(producerRecord, new Callback() {
          @Override
          public void onCompletion(RecordMetadata recordMetadata, Exception exception) {
            //executes every time a record is successfully sent or an exception is thrown
            if(exception == null) {
              //record was sent successfully
              log.info("Key: {} | Partition: {}", key, recordMetadata.partition());
            } else {
              log.error("Error while producing");
            }
          }
        });

        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
    }

    //flush the producer - tell the producer to send all data and block until done: synchronous operation
    producer.flush();

    //close the producer
    producer.close();

    log.info("Ending Kafka Producer.......");
  }

}
