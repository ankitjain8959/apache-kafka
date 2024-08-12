# What is Apache Kafka?
Apache Kafka is like a sophisticated postal service designed for digital messages within a company.

**Example:** Imagine a large business with multiple departments that need to send and receive information from each other constantly. Here’s how Kafka fits in:
- **Producers (Senders):** Think of each department as having a mailbox where they drop off letters (messages) they want to send (produce).
  For instance, when a customer places an order on the website, the sales department drops a message about a new order into the “new orders” mailbox (topic) in Kafka.

- **Kafka (Post Office):** Kafka acts as the central post office. It collects all these letters (messages) from different departments and keeps them organized in different sections (topics). Each topic is like a specific mailbox for different types of information, such as orders, inventory updates, or customer feedback.

- **Consumers (Receivers):** Other departments, like the inventory or shipping departments, regularly check their respective mailboxes (topics) at the post office (Kafka). They pick up the letters (messages) relevant to them.

For instance,
- Inventory department checks the “new orders” mailbox in kafka, reads the new order message, and updates the stock levels accordingly.
- Shipping department also checks the “new orders” mailbox, picks up the order details, and starts processing the shipment.
- Notification department checks the same “new orders” mailbox to send confirmation emails to customers.

**Note:** Kafka ensures that messages are delivered quickly and reliably. Even if the post office (Kafka) is handling thousands of letters at once, it makes sure that the data is transferred/received by the consumers (departments) without delays or loss, like how a well-organized postal service ensures timely and accurate mail delivery.

**_Kafka is only used as a transportation mechanism!_**


Apache Kafka is a Data Streaming Platform. Apache Kafka acts as a Data Integration Layer were,
- DATA SOURCES => publish their data and
- TARGET SYSTEMS <= will consume their data.

![What is Apache Kafka](<docs/images/What%20is%20Apache%20Kafka.png>)


# Consumer Offsets?
Each consumer (department) needs to know which messages it has already read to avoid processing the same message multiple times.
In Apache Kafka, this is managed through a concept called consumer offsets (unique position of a message).

**_How to ensure No Duplicate Processing?_**
- Independent Tracking:
  Each consumer (department) independently tracks its own offsets.
  Example: The Shipping department might have a different offset bookmark than the Inventory department, based on how quickly or slowly it processes messages.
- Resuming from the Last Offset: (Fault Tolerance)
  If a consumer restarts (e.g., due to a system reboot or crash), it can resume processing from the last recorded offset. This ensures that no messages are missed or processed multiple times.

**Example Scenario**
- **Inventory System (Consumer):**
    - Reads message at offset 100 (Order #100).
    - Processes the order and updates its offset bookmark to 100.
- **Shipping System (Consumer):**
    - Reads message at offset 100 (Order #100).
    - Processes the shipment and updates its offset bookmark to 100.
- **Notification System (Consumer):**
    - Reads message at offset 100 (Order #100).
    - Sends a confirmation email and updates its offset bookmark to 100.

Each consumer (system) reads the same message but keeps track of its own progress independently using offsets. This mechanism ensures that each department processes every order exactly once and can efficiently manage their workload.

# Kafka Topics
Like databases have tables to organize and segment datasets, Kafka uses the concept of topics to organize related messages.

**Note:** Unlike database tables, Kafka topics are not query-able. Instead, we must create Kafka producers to send data to the topic and Kafka consumers to read the data from the topic in order.

![Kafka Topics](<docs/images/Kafka Topics.png>)




