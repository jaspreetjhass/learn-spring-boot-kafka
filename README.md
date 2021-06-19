**Setting Up Kafka**

> Start up the Zookeeper.
zookeeper-server-start.bat ..\..\config\zookeeper.properties

> Start up the Kafka Broker.</n>
kafka-server-start.bat ..\..\config\server.properties

> How to create a topic ?
kafka-topics.bat --create --topic test-topic -zookeeper localhost:2181 --replication-factor 1 --partitions 4

> How to instantiate a Console Producer without key ?
kafka-console-producer.bat --broker-list localhost:9092 --topic test-topic

> How to instantiate a Console Producer with key ?
kafka-console-producer.bat --broker-list localhost:9092 --topic test-topic --property "key.separator=-" --property "parse.key=true"

> How to instantiate a Console Consumer without key ?
kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic test-topic --from-beginning

> How to instantiate a Console Consumer with key ?
kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic test-topic --from-beginning -property "key.separator= - " --property "print.key=true"

> How to instantiate a Console Consumer with consumerGroup ?
kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic test-topic --group <group-name>
  





