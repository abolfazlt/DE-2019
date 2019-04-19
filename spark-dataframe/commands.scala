
val data = sc.textFile("/home/root/data/sample.csv")
val documents = data.map(_.split("""","""")).filter(_.size>3).flatMap(row=>(row(1) + " " + row(2)).split(" ").map(x=>(row(0).replace("\"",""), x, row(3)))).toDF("id", "token", "tags")

val tf = documents.groupBy("id", "token").count.withColumnRenamed("count", "tf")
val df = documents.groupBy("token").agg(countDistinct("id") as "df")

val docCount = data.count
val calcIdfUdf = udf { df: Long => math.log((docCount + 1)/(df + 1)) }
val idf = df.withColumn("idf", calcIdfUdf(col("df")))
val tfidf = tf.join(idf, Seq("token"), "left").withColumn("tf_idf", col("tf") * col("idf"))
tfidf.orderBy(desc("tf_idf")).show()
tfidf.filter(not(col("token").isin(""))).filter(length(col("token")) > 2).orderBy(desc("tf_idf")).show()

val test = udf { df: String => df.toString.forall(_.isLetter) }
val x = tfidf.withColumn("is", test(col("token")))
x.filter(col("is")).filter(not(col("token").isin(""))).filter(col("token") === "java").orderBy(desc("tf_idf")).join(documents, Seq("id"), "left").show()

tfidf.write.mode('overwrite').parquet('hdfs://172.18.0.2:9000/spark/data/tfidf')

val df = sqlContext.read.parquet('hdfs://172.18.0.2:9000/spark/data/tfidf')
df.printSchema
