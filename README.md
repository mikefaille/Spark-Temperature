To build light .jar for Spark cluster (without spark depedencies) :
sbt assemble

To run it standalone (with spark depedencies):
sbt run


Note for this next error :

```
15/10/30 23:31:51 ERROR ContextCleaner: Error in cleaning thread
java.lang.InterruptedException
	at java.lang.Object.wait(Native Method)
	at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:143)
	at org.apache.spark.ContextCleaner$$anonfun$org$apache$spark$ContextCleaner$$keepCleaning$1.apply$mcV$sp(ContextCleaner.scala:157)
	at org.apache.spark.util.Utils$.tryOrStopSparkContext(Utils.scala:1136)
	at org.apache.spark.ContextCleaner.org$apache$spark$ContextCleaner$$keepCleaning(ContextCleaner.scala:154)
	at org.apache.spark.ContextCleaner$$anon$3.run(ContextCleaner.scala:67)
15/10/30 23:31:51 ERROR Utils: uncaught error in thread SparkListenerBus, stopping SparkContext
java.lang.InterruptedException
	at java.util.concurrent.locks.AbstractQueuedSynchronizer.doAcquireSharedInterruptibly(AbstractQueuedSynchronizer.java:998)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer.acquireSharedInterruptibly(AbstractQueuedSynchronizer.java:1304)
	at java.util.concurrent.Semaphore.acquire(Semaphore.java:312)
	at org.apache.spark.util.AsynchronousListenerBus$$anon$1$$anonfun$run$1.apply$mcV$sp(AsynchronousListenerBus.scala:65)
	at org.apache.spark.util.Utils$.tryOrStopSparkContext(Utils.scala:1136)
	at org.apache.spark.util.AsynchronousListenerBus$$anon$1.run(AsynchronousListenerBus.scala:63)
[success] Total time: 16 s, completed Oct 30, 2015 11:31:52 PM
```

You can safely disregard this error. This is printed at the end of the
execution when we clean up and kill the daemon context cleaning thread. In
the future it would be good to silence this particular message, as it may
be confusing to users.

Andrew


https://mail-archives.apache.org/mod_mbox/incubator-spark-user/201406.mbox/%3CCAMJOb8=6Q9xDhLALyo-bWQR3AkMz=ost=7jhTad7Zduv7mpQ0w@mail.gmail.com%3E
