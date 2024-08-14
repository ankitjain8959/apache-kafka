package org.ankit.consumer;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a simple Kafka consumer example that demonstrates how to consume messages from a Kafka topic with gracefully shutdown.
 *
 * @author Ankit
 */
public class ConsumerDemoWithShutdown {

  private static final Logger log = LoggerFactory.getLogger(ConsumerDemoWithShutdown.class.getSimpleName());

  public static void main(String[] args) {
    log.info("Start Kafka Consumer.......");

    String topic = "firstTopic";
    String groupId = "my-java-application";

    //Create Producer Properties
    Properties properties = new Properties();

    //Connect to Secure server
    properties.setProperty("bootstrap.servers",
        "https://liked-cougar-5356-us1-kafka.upstash.io:9092");
    properties.setProperty("security.protocol", "SASL_SSL");
    properties.setProperty("sasl.jaas.config",
        "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"bGlrZWQtY291Z2FyLTUzNTYkng_vJc9e_qR-MYNRgIEkapzv9eP6PcEShlmRt5E\" password=\"OTY1MGY5ZTEtOWM3ZC00YjVhLThhZTItMmY0YWYxOWVjZjhl\";");
    properties.setProperty("sasl.mechanism", "SCRAM-SHA-256");

    //set consumer properties
    properties.setProperty("key.deserializer",
        "org.apache.kafka.common.serialization.StringDeserializer");
    properties.setProperty("value.deserializer",
        "org.apache.kafka.common.serialization.StringDeserializer");

    properties.setProperty("group.id", groupId);

    //auto.offset.reset value can be none/earliest/latest
    properties.setProperty("auto.offset.reset", "earliest");

    //Create the consumer
    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

    //Get a reference of the main thread
    final Thread mainThread = Thread.currentThread();

    //adding the shutdown hook
    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        log.info("Detected a shutdown, let's exit by calling consumer.wakeup()....");
        consumer.wakeup();

        //join the main thread to allow the execution of the code in the main thread
        try {
          mainThread.join();
        } catch (InterruptedException ex) {
          log.error("Interrupted exception while joining main thread", ex);
        }
      }
    });

    try {
      //Subscribe to the Kafka topic
      consumer.subscribe(List.of(topic));

      // poll for data
      while (true) {
        log.info("Polling");
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

        for (ConsumerRecord<String, String> record : records) {
          log.info("Key: {} , Value: {}", record.key(), record.value());
          log.info("Partition: {} , Offset: {}", record.partition(), record.offset());
        }
      }
    } catch (WakeupException ex) {
      log.info("Consumer is starting to shut down");
    } catch (Exception ex) {
      log.error("Unexpected exception in the consumer", ex);
    } finally {
      consumer.close();                                                         //close the consumer, this will also commit offsets
      log.info("Consumer is gracefully shut down");
    }
  }
}