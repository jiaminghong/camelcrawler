import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.mllib.feature.HashingTF
import org.apache.spark.mllib.regression.LabeledPoint
import java.io._
import org.apache.spark.mllib.classification.LogisticRegressionWithLBFGS
import org.apache.spark.mllib.classification.{NaiveBayes, NaiveBayesModel}

//object Spam {
//  val sc = new SparkContext(new SparkConf().setAppName("UrlDetection").setMaster("local[*]"))
//  val original_data = sc.textFile("/Users/jiaminghong/Desktop/Final/dataset.csv")
//}

object Spam {
  def main(args: Array[String]) {
    val input = "http://www.boutycone.fr/mail/assure_somtc=true/99f5c49b3237b7e259ce54c7730c0ab4/sms2.php"
    val result0 = input.replaceAll("[^a-zA-Z0-9]+",",")
    val a1 = "https"
    val a2 = "http";
    val a3 = "www";
    println(result0)
    val result1 = result0.replaceFirst(a1, "")
    println(result1)
    val result2 = result1.replaceFirst(a2, "")
    println(result2)
    val result3 = result2.replaceFirst(a3, "")
    println(result3)
    val pw = new PrintWriter(new File("/Users/jiaminghong/Desktop/Final/input.txt" ))
    pw.write(result3)
    pw.close

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
    val Array(training, test) = data.randomSplit(Array(0.6, 0.4))
    //val model = NaiveBayes.train(training, lambda = 1.0)
    val logistic_Learner = new LogisticRegressionWithLBFGS()
    val model = logistic_Learner.run(training)
    val predictionLabel = test.map(x=> (model.predict(x.features),x.label))

    val accuracy = 1.0 * predictionLabel.filter(x => x._1 == x._2).count() / training.count()
    println(accuracy)
    val Ming = sc.textFile("/Users/jiaminghong/Desktop/Final/input.txt")
    val features_Ming = Ming.map(mail => features.transform(mail.split(",")))
    val Ming_data = features_Ming.map(features => LabeledPoint(100, features))
    val Ming_result = Ming_data.map(x=> (model.predict(x.features),x.label))
    if(predictionLabel.first()._1 == 0) {
      println("good website")
    }
    else {
      println("bad website")
    }


//    println("predictionLabel1", predictionLabel.first())
//    println("predictionLabel1", predictionLabel.collect().apply(1))
//    println("predictionLabel2", predictionLabel.collect().apply(2232))
//    println("predictionLabel3", predictionLabel.collect().apply(332))
//    println("predictionLabel4", predictionLabel.collect().apply(443))
//    println("features: %s",features)

//    val browser = JsoupBrowser()
//    val doc = browser.get("http://observador.pt")
//
//    println()
//    println("=== OBSERVADOR ===")
//
//    doc >> extractor(".logo img", attr("src")) |> println
//    doc >> extractorAt[String]("example-extractor") |> println
//
//    println("==================")
//    println()
//
//    doc >> ".small-news-list h4 > a" foreach println
  }

}