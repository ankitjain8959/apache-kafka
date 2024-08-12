# Start Zookeeper and Apache Kafka

Before Creating any topics, you need to start Zookeeper and Apache Kafka.

- Start ZOOKEEPER

zookeeper-server-start.sh ~/kafka_2.13-3.1.0/config/zookeeper.properties

- Start APACHE KAFKA

kafka-server-start.sh ~/kafka_2.13-3.1.0/config/server.properties

**List number of ACTIVE BROKERS (servers)**

- Step 1: Connect to Zookeeper server running at localhost:2181 (default port for zookeeper)

/kafka_2.13-3.1.0/bin/zookeeper-shell.sh localhost:2181

- Step 2: List metadata information & Fetch broker details

    - List metadata information (example, ids of the broker) stored in the server:

      ls /brokers/ids

    - Fetch more details for a specific broker:

      get /brokers/ids/0

Note: Locally, you will see only 1 BROKER with id 0


# Kafka Topic CLI
**kafka-topics** is used to create, delete, describe, or change a topic in Kafka.

- Create a topic with partition (1 as default partition if you don't specify)

  kafka-topics.sh --bootstrap-server localhost:9092 --topic firstTopic --create

- Create a topic with 3 partition

  kafka-topics.sh --bootstrap-server localhost:9092 --topic firstTopic --create --partitions 3

- Create a topic with 3 partitions & 1 replication factor

  kafka-topics.sh --bootstrap-server localhost:9092 --topic firstTopic --create --partitions 3 --replication-factor 1

- List all topics

  kafka-topics.sh --bootstrap-server localhost:9092 --list

- Describe all topics (List all topics with details about partitions, replication factor, Leader and In-Sync Replicas)

  kafka-topics.sh --bootstrap-server localhost:9092 --describe

- Describe specific topic (List specific topic with details about partitions, replication factor, Leader and In-Sync Replicas)

  kafka-topics.sh --bootstrap-server localhost:9092 --topic firstTopic --describe

- Delete a topic

  kafka-topics.sh --bootstrap-server localhost:9092 --topic firstTopic --delete

  # Producers
- Producing without keys

  kafka-console-producer.sh --bootstrap-server localhost:9092 --topic firstTopic

  > Hello World

  > This is Ankit

- Producing without keys but with properties
  kafka-console-producer.sh --bootstrap-server localhost:9092 --topic firstTopic --producer-property acks=all

- Producing with keys
  kafka-console-producer.sh --bootstrap-server localhost:9092 --topic firstTopic --property parse.key=true --property key.separator=:

  > example key:example value (works fine)

  > name:ankit (works fine)

  > noValueOnlyKey (exception because of no key separator)

- Only For Demo Purpose (Used Round Robin to Demo the messages produced to different partition in round robin fashion)

  kafka-console-producer.sh --bootstrap-server localhost:9092 --producer-property partitioner.class=org.apache.kafka.clients.producer.RoundRobinPartitioner --topic firstTopic

# Consumers
- Consuming

  kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic firstTopic

- Consuming from the beginning

  kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic firstTopic --from-beginning

- Consuming with display key, values, timestamp and partitions

  kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic firstTopic --formatter kafka.tools.DefaultMessageFormatter --property print.key=true --property print.value=true --property print.timestamp=true --property print.partition=true --from-beginning


# CONSUMER GROUPS

- Consumer-Group (for starting multiple consumers in a same group re-run below command in different terminals)

  kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic firstTopic --group myFirstApplication


- List Consumer Groups

  kafka-consumer-groups.sh --bootstrap-server localhost:9092 --list

- Describe Consumer Group (to check from which topic it's reading data, partition details, current-offset, log-end-offset and Lag of message consumed by the group)

  kafka-consumer-groups.sh --bootstrap-server localhost:9092 --describe --group myFirstApplication


# RESETTING OFFSETS

- Dry Run: Reset offsets to beginning of each partition (will not commit)

  kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group myFirstApplication --reset-offsets --to-earliest --topic firstTopic --dry-run

- Execute: Reset offsets to beginning of each partition (commit)

  kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group myFirstApplication --reset-offsets --to-earliest --topic firstTopic --execute