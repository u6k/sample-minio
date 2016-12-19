# sample minio for scala

Amazon S3互換オブジェクト・ストレージであるMinioをScalaから使用するサンプルコードを作成します。

## 概要

ローカルPCでMinioコンテナを動作させて、Scalaコードからアクセスして、オブジェクト一覧を標準出力します。この時、`AWS SDK for Java`を使用して、S3へのアクセス処理との相違点を確認します。

## 前提条件

- Java SDK 8

```
>java -version
java version "1.8.0_102"
Java(TM) SE Runtime Environment (build 1.8.0_102-b14)
Java HotSpot(TM) 64-Bit Server VM (build 25.102-b14, mixed mode)
```

- Typesafe Activator 1.3.10
    - なんでactivatorはバージョン情報を表示するコマンドすらないのだ…

- Docker
    - 筆者は、Windows 10にDocker ToolboxをインストールしてDocker環境を構築しています。
    - Dockerが動作すれば、他の環境でも問題ないはず。

```
$ docker version
docker version
Client:
 Version:      1.12.5
 API version:  1.24
 Go version:   go1.6.4
 Git commit:   7392c3b
 Built:        Fri Dec 16 06:14:34 2016
 OS/Arch:      linux/amd64

Server:
 Version:      1.12.5
 API version:  1.24
 Go version:   go1.6.4
 Git commit:   7392c3b
 Built:        Fri Dec 16 06:14:34 2016
 OS/Arch:      linux/amd64
```

- [Minio](https://minio.io/)
    - Minioは、Amazon S3互換オブジェクト・ストレージです。
    - いくつかの使用方法がありますが、ここではDockerコンテナとして動作させます。

## 手順

MinioをDockerコンテナとして動作させて、Scalaコードからアクセスする手順を説明します。

### Minioコンテナを動作させてみる

[Minio Docs](https://docs.minio.io/)の「Docker Container」を参考に動作させてみます。

```
$ docker run --rm -p 80:9000 minio/minio server /var/minio
docker run --rm -p 80:9000 minio/
minio server /var/minio
Created minio configuration file at /root/.minio

Endpoint:  http://172.17.0.2:9000  http://127.0.0.1:9000
AccessKey: ********************
SecretKey: ++++++++++++++++++++++++++++++++++++++++
Region:    us-east-1
SQS ARNs:  <none>

Browser Access:
   http://172.17.0.2:9000  http://127.0.0.1:9000

Command-line Access: https://docs.minio.io/docs/minio-client-quickstart-guide
   $ mc config host add myminio http://172.17.0.2:9000 ******************** ++++++++++++++++++++++++++++++++++++++++

Object API (Amazon S3 compatible):
   Go:         https://docs.minio.io/docs/golang-client-quickstart-guide
   Java:       https://docs.minio.io/docs/java-client-quickstart-guide
   Python:     https://docs.minio.io/docs/python-client-quickstart-guide
   JavaScript: https://docs.minio.io/docs/javascript-client-quickstart-guide

Drive Capacity: 15 GiB Free, 18 GiB Total
```

これでMinioが起動しました。Minioは9000ポートを使用しますが、ここでは80ポートに関連付けています。

ログにアクセスキーとシークレットキーが表示されますので、Webインターフェイスやコードからアクセスするときに使用します。

試しに http://localhost にアクセスすると、MinioのWebインターフェイスが表示されますので、ログインしていじってみます。この時、いくつかBucketを作成しておいてください。サンプルコードでBucketのリストを表示してみます。

### ScalaコードからMinioにアクセスしてみる

`minimal-scala`プロジェクトを作成します。

```
>activator new sample-minio minimal-scala
ACTIVATOR_HOME=C:\tools\activator-1.3.10-minimal
ファイル BIN_DIRECTORY\..\conf\sbtconfig.txt が見つかりません。

Fetching the latest list of templates...

OK, application "sample-minio" is being created using the "minimal-scala" template.

To run "sample-minio" from the command line, "cd sample-minio" then:
C:\Users\u6kyu\Documents\sample-minio\sample-minio/activator run

To run the test for "sample-minio" from the command line, "cd sample-minio" then:
C:\Users\u6kyu\Documents\sample-minio\sample-minio/activator test

To run the Activator UI for "sample-minio" from the command line, "cd sample-minio" then:
C:\Users\u6kyu\Documents\sample-minio\sample-minio/activator ui
```

`build.sbt`を編集します。

- `scalaVersion`を`2.11.8`に変更
- `libraryDependencies`を`aws-java-sdk`のみに変更

```
name := """sample-minio"""

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies += "com.amazonaws" % "aws-java-sdk" % "1.11.68"
```

余分なファイルを削除します。

- `src/test/`フォルダを削除
- `src/main/scala/com/`フォルダを削除

`Main.scala`を作成します。

- `accessKey`変数に、アクセスキーを設定
- `secretKey`変数に、シークレットキーを設定

```
import scala.collection.JavaConverters._

import com.amazonaws._
import com.amazonaws.auth._
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3._
import com.amazonaws.services.s3.model._

object Main {

    def main(args: Array[String]): Unit = {
        val accessKey = "*****"
        val secretKey = "++++++++++"
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
```

実行します。あらかじめ作成したBucketのリストが表示されたことを確認します。

```
>activator run
ACTIVATOR_HOME=C:\tools\activator-1.3.10-minimal
ファイル BIN_DIRECTORY\..\conf\sbtconfig.txt が見つかりません。
[info] Loading project definition from C:\Users\u6kyu\Documents\sample-minio\sample-minio\project
[info] Set current project to sample-minio (in build file:/C:/Users/u6kyu/Documents/sample-minio/sample-minio/)
[info] Compiling 1 Scala source to C:\Users\u6kyu\Documents\sample-minio\sample-minio\target\scala-2.11\classes...
[info] Running Main
bar
boo
foo
sample
[success] Total time: 20 s, completed 2016/12/19 18:27:47
```

## まとめ

S3アクセスと認証処理のみ異なりますが、他は同様にアクセスできることが確認できました。MinioはS3のローカル環境として利用できそうだと考えます。

## 関連リンク

- GitHub
    - [u6k/sample-minio](https://github.com/u6k/sample-minio)
- Author
    - [u6k.blog()](http://blog.u6k.me)

## ライセンス

[![license](https://img.shields.io/github/license/mashape/apistatus.svg)](https://opensource.org/licenses/MIT)
