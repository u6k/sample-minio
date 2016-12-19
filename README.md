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

## 手順

1. Minioコンテナを動作させてみる
1. MinioのWebインターフェイスを操作してみる
1. ScalaコードからMinioにアクセスしてみる

### Minioコンテナを動作させてみる

TODO

### MinioのWebインターフェイスを操作してみる

TODO

### ScalaコードからMinioにアクセスしてみる

TODO

## まとめ

TODO

## 関連リンク

- [u6k.blog()](http://blog.u6k.me)
- TODO: GitHubリポジトリ

## ライセンス

[![license](https://img.shields.io/github/license/mashape/apistatus.svg)](https://opensource.org/licenses/MIT)
