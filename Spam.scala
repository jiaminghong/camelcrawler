import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.mllib.feature.HashingTF
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.classification.LogisticRegressionWithLBFGS
import org.apache.spark.mllib.classification.{NaiveBayes, NaiveBayesModel}

//object Spam {
//  val sc = new SparkContext(new SparkConf().setAppName("UrlDetection").setMaster("local[*]"))
//  val original_data = sc.textFile("/Users/jiaminghong/Desktop/Final/dataset.csv")
//}

object Spam {
  def main(args: Array[String]) {
    val sc = new SparkContext(new SparkConf().setAppName("Spam").setMaster("local[*]"))
    val good = sc.textFile("/Users/jiaminghong/Desktop/Final/good.txt")
    val bad = sc.textFile("/Users/jiaminghong/Desktop/Final/bad.txt")
//    val good = sc.textFile("/Users/jiaminghong/Desktop/FinalProject/ham")
//    val bad = sc.textFile("/Users/jiaminghong/Desktop/FinalProject/spam")
    val features = new HashingTF(numFeatures = 1000)
    val features_good = good.map(mail => features.transform(mail.split(",")))
    val features_bad = bad.map(mail => features.transform(mail.split(",")))
    val good_data = features_good.map(features => LabeledPoint(0, features))
    val bad_data = features_bad.map(features => LabeledPoint(1, features))

    val data = good_data.union(bad_data)
    data.cache()
    val Array(training, test) = data.randomSplit(Array(0.5, 0.5))
    //val model = NaiveBayes.train(training, lambda = 1.0)
    val logistic_Learner = new LogisticRegressionWithLBFGS()
    val model = logistic_Learner.run(training)
//    val model = logistic_Learner.
//    println("model = %d", model)

    val predictionLabel = test.map(x=> (model.predict(x.features),x.label))
    //println("predictionLabel = %d", predictionLabel)
    val accuracy = 1.0 * predictionLabel.filter(x => x._1 == x._2).count() / training.count()
    println(accuracy)

    println("features: %s",features)
    println

//    val Ming = "dsadsad.13213.com";
//    val Ming2 = Ming.map(mail => features.transform(mail.split(",")))
    //val Ming2 = sc.parallelize(Ming).collect()
//    val Ming3 = Ming2.map(features => LabeledPoint(0, features))
//    val predictionMing = model.
//    val Ming = "dsadsad.13213.com";
//    val vec = scala.collection.immutable.Vector.empty
//    val vec2 = vec :+ Ming
//    model.predict(vec2)
  }

}