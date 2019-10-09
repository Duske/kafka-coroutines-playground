package global.wavy.kafkaplayground


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import java.time.Duration
import kotlin.coroutines.CoroutineContext

fun main() {

    val kafkaConfiguration = mapOf(
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
        ConsumerConfig.GROUP_ID_CONFIG to "LOCAL_KAFKA_PLAYGROUND",
        ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to false,
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "latest"
    )

    val consumerFactory = DefaultKafkaConsumerFactory<String, String>(kafkaConfiguration)

    val consumer = consumerFactory.createConsumer()
    val x = consumer.poll(Duration.ofMillis(1000L))

//    val producerFactoy = DefaultKafkaProducerFactory<String, String>(kafkaConfiguration)
//    val kafkaTemplate = KafkaTemplate<String, String>(producerFactoy)

    // kafkaTemplate.println("hello!")
}

//https://jivimberg.io/blog/2019/02/23/sqs-consumer-using-kotlin-coroutines/
class MessageProcessor : CoroutineScope {
    private val job = Job()

    fun cancel() {
        job.cancel()
    }

    override val coroutineContext: CoroutineContext
        get() = job


    fun start() {

    }

}