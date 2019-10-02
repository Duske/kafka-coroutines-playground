package global.wavy.kafkaplayground


import kotlinx.coroutines.channels.Channel
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate

fun main() {

    val kafkaConfiguration = mapOf(
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "",
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
        ProducerConfig.COMPRESSION_TYPE_CONFIG to "gzip"
    )

    val producerFactoy = DefaultKafkaProducerFactory<String, String>(kafkaConfiguration)
    val kafkaTemplate = KafkaTemplate<String, String>(producerFactoy)


    val buffer = Channel<String>(10_000)
    buffer.send("MESSAGE $it")

    repeat(10_000) {

    }

    repeat(10_000) { value ->
        kafkaTemplate.send("", "")
    }

}