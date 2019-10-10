package global.wavy.kafkaplayground


import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import java.lang.Thread.currentThread
import java.time.Duration
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.CoroutineContext

val shutdown: AtomicBoolean = AtomicBoolean(false)
val logger: Logger = LoggerFactory.getLogger("KafkaPlayground-Consumer")

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

    val processor = MessageProcessor(consumerFactory, 4)
    processor.start()


    Runtime.getRuntime().addShutdownHook(Thread {
        run {
            logger.info("Shutdown started")
            processor.shutdown()
            logger.info("Shutdown completed")
            Thread.sleep(1000L)
            shutdown.set(true)
        }
    })

    run()
}

private fun run() {
    while (!shutdown.get()) {
    }
}

class MessageProcessor(
    private val consumerFactory: DefaultKafkaConsumerFactory<String, String>,
    private val numberOfWorkers: Int = 1
) : CoroutineScope by CoroutineScope(Dispatchers.Default) {
    private val job = Job()
    private val consumer: Consumer<String, String>

    init {
        logger.info("Starting kafka consumer.")
        consumer = consumerFactory.createConsumer()
        logger.info("Kafka consumer started.")
    }


    override val coroutineContext: CoroutineContext
        get() = job

    fun cancel() {
        job.cancel()
    }

    fun shutdown() {
        job.complete()
        logger.info("CoRoutines job completed. job=$job")

        consumer.close(Duration.ofMillis(5000L))
        logger.info("Kafka consumer closed.")
    }

    fun start() = launch {
        val channel = Channel<String>()
        repeat(numberOfWorkers) { startWorker(channel) }
        receiveMessages(channel)
    }


    private suspend fun startWorker(channel: ReceiveChannel<String>) = repeatUntilCancelled {
        for (msg in channel) {
            try {
//                processMsg(msg)
//                deleteMessage(msg)
            } catch (ex: Exception) {
                println("${Thread.currentThread().name} exception trying to process message $msg")
                ex.printStackTrace()
            }
        }
    }

    private suspend fun receiveMessages(channel: SendChannel<String>) = repeatUntilCancelled {


        val x = consumer.poll(Duration.ofMillis(1000L))

        //        val receiveRequest = ReceiveMessageRequest.builder()
//            .queueUrl(SQS_URL)
//            .waitTimeSeconds(20)
//            .maxNumberOfMessages(10)
//            .build()
//
//        val messages = sqs.receiveMessage(receiveRequest).await().messages()
//        println("${Thread.currentThread().name} Retrieved ${messages.size} messages")
//
//        messages.forEach {
//            channel.send(it)
//        }
    }

    private suspend fun repeatUntilCancelled(block: suspend () -> Unit) {
        while (isActive) {
            try {
                block()
                yield()
            } catch (ex: CancellationException) {
                println("coroutine on ${currentThread().name} cancelled")
            } catch (ex: Exception) {
                println("${currentThread().name} failed with {$ex}. Retrying...")
                ex.printStackTrace()
            }
        }

        println("coroutine on ${currentThread().name} exiting")
    }


}