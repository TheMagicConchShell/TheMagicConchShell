# 마법의 싸피고둥
> 자유로운 고민 상담 소통 웹 서비스

[![pipeline status](https://lab.ssafy.com/s03-webmobile1-sub3/S03P13A403/badges/master/build.svg)](https://lab.ssafy.com/s03-webmobile1-sub3/s03p13a403/pipelines)
[![license](https://img.shields.io/badge/license-MIT-green)](https://lab.ssafy.com/s03-webmobile1-sub3/s03p13a403/blob/master/LICENSE)

## Overview
[i3a403.p.ssafy.io](http://i3a403.p.ssafy.io/)

## Project Setup
#### Frontend
``` bash
cd frontend
sudo yarn install
sudo yarn build
service nginx start
```

#### Backend
``` bash
cd backend
mvn package
cd jar
java -jar backend-0.0.1-SNAPSHOT.jar
```
\* 빌드 전에 backend/src/main/resources/application.properties 파일이 존재하는지 확인해주세요.


## Documents
[https://drive.google.com/drive/u/0/folders/1RTENo2sIWKJZDlXICJTc1DmMOvgN5L6U](https://drive.google.com/drive/u/0/folders/1RTENo2sIWKJZDlXICJTc1DmMOvgN5L6U)
