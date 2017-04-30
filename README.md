# sample minio

Amazon S3互換オブジェクト・ストレージであるMinioをJava(SpringBoot)から使用するサンプルコードです。

## 概要

ローカルPCでMinioコンテナを動作させて、SpringBootアプリケーションからアクセスして、バケット一覧を標準出力します。

## 前提条件

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

- Java SDK 8

```
# java -version
openjdk version "1.8.0_121"
OpenJDK Runtime Environment (IcedTea 3.3.0) (Alpine 8.121.13-r0)
OpenJDK 64-Bit Server VM (build 25.121-b13, mixed mode)
```

- Apache Maven 3

```
# mvn -version
Apache Maven 3.3.9 (bb52d8502b132ec0a5a3f4c09453c07478323dc5; 2015-11-10T16:41:47+00:00)
Maven home: /usr/share/java/maven-3
Java version: 1.8.0_121, vendor: Oracle Corporation
Java home: /usr/lib/jvm/java-1.8-openjdk/jre
Default locale: en_US, platform encoding: UTF-8
OS name: "linux", version: "4.4.59-boot2docker", arch: "amd64", family: "unix"
```

- [Minio](https://minio.io/)
    - Minioは、Amazon S3互換オブジェクト・ストレージです。
    - いくつかの使用方法がありますが、ここではDockerコンテナとして動作させます。
    - Minio側のバグにより、latestではなくedgeを使用します。

## 手順

MinioをDockerコンテナとして動作させて、SpringBootアプリケーションからアクセスする手順を説明します。

### Minioコンテナを動作させてみる

[Minio Docs](https://docs.minio.io/)の「Docker Container」を参考に動作させてみます。

```
$ docker run \
    -d \
    --name s3 \
    -p 9000:9000 \
    minio/minio:edge server /export
```

正常に起動すると、以下のようにログ出力されます。

```
$ docker logs s3
Created minio configuration file successfully at /root/.minio

Endpoint:  http://172.17.0.2:9000  http://127.0.0.1:9000
AccessKey: xxxxxxxxxxxxxxxxxxxx
SecretKey: xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
Region:    us-east-1
SQS ARNs:  <none>

Browser Access:
   http://172.17.0.2:9000  http://127.0.0.1:9000

Command-line Access: https://docs.minio.io/docs/minio-client-quickstart-guide
   $ mc config host add myminio http://172.17.0.2:9000 C05JGTT0UWNXW25Y4IHN zg1JZrFIAWM0dZmXkPF93ZOXXf6Vj3F/QgycmLZE

Object API (Amazon S3 compatible):
   Go:         https://docs.minio.io/docs/golang-client-quickstart-guide
   Java:       https://docs.minio.io/docs/java-client-quickstart-guide
   Python:     https://docs.minio.io/docs/python-client-quickstart-guide
   JavaScript: https://docs.minio.io/docs/javascript-client-quickstart-guide

Drive Capacity: 17 GiB Free, 18 GiB Total
```

Minioは9000ポートで起動しますので、WebブラウザやAPIでは9000ポートに接続します。試しに `http://localhost:9000/` にアクセスすると、MinioのWebインターフェイスが表示されますので、ログインしていじってみます。この時、いくつかBucketを作成しておいてください。サンプルコードでBucketのリストを表示してみます。

ログにアクセスキーとシークレットキーが出力されるので、WebブラウザやAPIでアクセスするときはこれを使用します。自動テストなどでアクセスキーとシークレットキーを固定したい場合は、環境変数`MINIO_ACCESS_KEY`と`MINIO_SECRET_KEY`を指定することで、任意に設定できます。

### SpringBootアプリケーションからMinioにアクセスしてみる

SpringBootプロジェクトの作成手順は割愛します。肝心のサンプルコードは、 `src/main/java/me/u6k/sample_minio/Main.java` をご覧ください。一部をここで解説します。

```
@Value("${s3.url}")
private String s3url;

@Value("${s3.access-id}")
private String s3accessId;

@Value("${s3.secret-key}")
private String s3secretKey;

@Value("${s3.timeout}")
private int s3timeout;
```

設定情報として、S3エンドポイントURL、アクセスキー、シークレットキー、タイムアウト値(ミリ秒)を取得しています。SpringBootアプリケーションなので、`application.properties`や環境変数で指定することができます。

```
AWSCredentials credentials = new BasicAWSCredentials(this.s3accessId, this.s3secretKey);
```

アクセスキーとシークレットキーを使用して認証情報を作成します。他に、証明書から認証情報を作成する方法もあるようです。

```
ClientConfiguration configuration = new ClientConfiguration();
configuration.setSocketTimeout(this.s3timeout);
configuration.setSignerOverride("AWSS3V4SignerType");
```

タイムアウト値と署名方法を指定します。ここら辺はおまじない。

```
AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(new EndpointConfiguration(this.s3url, "us-east-1"))
                .withClientConfiguration(configuration)
                .withPathStyleAccessEnabled(true)
                .build();
```

認証情報、S3エンドポイント、クライアント設定情報、パス・スタイルを設定して、S3クライアントを初期化します。

S3エンドポイントは、URLのほかにリージョンを指定します。ここでは`us-east-1`をハードコードしていますが、Minioがデフォルトで`us-east-1`のためです。

パス・スタイルについては、AWS S3におけるバケット名はDNS名であるのに対して、Minioはパスになるため、この指定が必要です。

```
for (Bucket bucket : s3.listBuckets()) {
    L.info("bucket={}", bucket.getName());
}
```

バケットの一覧を取得して、バケット名をログ出力しています。

### Dockerイメージをビルドして、Dockerコンテナからアクセスしてみる

まず、Dockerイメージをビルドします。

```
$ docker build -t sample-minio .
```

事前にMinioコンテナを起動して、バケットをいくつか作成しておき、sample-minioコンテナを起動します。

```
$ docker run \
    --rm \
    --link s3:s3 \
    -e S3_URL=http://s3:9000/ \
    -e S3_ACCESS-ID=xxxxxxxxxxxxxxxxxxxx \
    -e S3_SECRET-KEY=xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx \
    -e S3_TIMEOUT=10000 \
    -v $HOME/.m2:/root/.m2 \
    -v $(pwd):/var/my-app \
    sample-minio
```

正常完了すると、以下のようにログ出力されます。

```
2017-04-30 09:14:54.463  INFO 1 --- [           main] me.u6k.sample_minio.Main                 : s3.url=http://s3:9000/
2017-04-30 09:14:54.476  INFO 1 --- [           main] me.u6k.sample_minio.Main                 : s3.access-id=xxxxxxxxxxxxxxxxxxxx
2017-04-30 09:14:54.476  INFO 1 --- [           main] me.u6k.sample_minio.Main                 : s3.secret-key=xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
2017-04-30 09:14:54.476  INFO 1 --- [           main] me.u6k.sample_minio.Main                 : s3.timeout=10000
2017-04-30 09:14:57.357  INFO 1 --- [           main] me.u6k.sample_minio.Main                 : bucket=bar
2017-04-30 09:14:57.357  INFO 1 --- [           main] me.u6k.sample_minio.Main                 : bucket=boo
2017-04-30 09:14:57.357  INFO 1 --- [           main] me.u6k.sample_minio.Main                 : bucket=foo
```

## まとめ

S3アクセスとクライアント初期化処理のみ異なりますが、他は同様にアクセスできることが確認できました。MinioはS3のローカル環境として利用できそうだと考えます。

## 関連リンク

- [u6k/sample-minio](https://github.com/u6k/sample-minio)
- [u6k.blog()](http://blog.u6k.me)

## ライセンス

[![license](https://img.shields.io/github/license/mashape/apistatus.svg)](https://opensource.org/licenses/MIT)
