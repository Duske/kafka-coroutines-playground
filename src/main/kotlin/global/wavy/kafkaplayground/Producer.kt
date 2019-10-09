package global.wavy.kafkaplayground


import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import java.util.*

fun main() {

    val kafkaConfiguration = mapOf(
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
        ProducerConfig.COMPRESSION_TYPE_CONFIG to "gzip"
    )

    val producerFactoy = DefaultKafkaProducerFactory<String, String>(kafkaConfiguration)
    val kafkaTemplate = KafkaTemplate<String, String>(producerFactoy)

    repeat(10_000) {
        val partition = (0..99).random()
        kafkaTemplate.send(
            "TEST-5",
            partition,
            UUID.randomUUID().toString(),
            "MESSAGE id: ${it.toString().padStart(4, '0')} customer: $partition "
        )
    }

}