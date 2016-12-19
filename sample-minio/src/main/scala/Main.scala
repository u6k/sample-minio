import scala.collection.JavaConverters._

import com.amazonaws._
import com.amazonaws.auth._
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3._
import com.amazonaws.services.s3.model._

object Main {

    def main(args: Array[String]): Unit = {
        val accessKey = "WKEO6SU2JYI70K5VSZ1X"
        val secretKey = "3OZChGdvg/mTBGwbMVnXiG2LShsSziVEwC6FthtP"
        val credentials = new BasicAWSCredentials(accessKey, secretKey)

        val config = new ClientConfiguration

        val s3 = new AmazonS3Client(credentials, config)
        val useast1 = Region.getRegion(Regions.US_EAST_1)
        s3.setRegion(useast1)
        s3.setEndpoint("http://localhost")

        val buckets = s3.listBuckets
        for (bucket <- buckets.asScala) {
            println(bucket.getName)
        }
    }

}
