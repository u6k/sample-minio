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

試しに http://localhost にアクセスすると、MinioのWebインターフェイスが表示されますので、ログインしていじってみます。

### ScalaコードからMinioにアクセスしてみる

TODO

## まとめ

TODO

## 関連リンク

- [u6k.blog()](http://blog.u6k.me)
- TODO: GitHubリポジトリ

## ライセンス

[![license](https://img.shields.io/github/license/mashape/apistatus.svg)](https://opensource.org/licenses/MIT)
