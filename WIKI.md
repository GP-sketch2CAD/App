# WIKI

## Professor
이상웅

## Title
Sketch2CAD(Sketch To CAD)

## Introduction
This is an ardroid application for drawing designer who need to draw quickly.
This program that automatically draw actual measurement drawings with a CAD.

## Brief Desciption
If you draw a picture and press the 'Export' button, your picture will be converted into a CAD file.

## Development Environment
 - Android Studio @4.2.1
 - CNN Model

## Application Version
 - minSdkVersion : 16
 - targetSdkVersion : 30

## CNN model Version?
추가할 부분 있으면 해주시면되고, 딱히 없으면 빼셔도 됩니당


## Overall Intermal Structure
<img src= "https://user-images.githubusercontent.com/60349584/168165384-b5ded98c-10dd-4f99-950c-2a3c0d3d6a7d.png" title="overall" alt="overall"></img>


## Using technology

- Coordinate System(Point, Coord)
![Coord](https://user-images.githubusercontent.com/60349584/168165031-4189fe35-cb73-49be-9912-22b7e8cdd663.png)

- Dogulas Peucker Algorithm
![Dogulas](https://user-images.githubusercontent.com/60349584/168165068-8833c45d-b102-40ca-9710-94767dd4a300.png)

- Architecture Rocognition : Wall, Calumn, Door, Window
![Ar(Wall)](https://user-images.githubusercontent.com/60349584/168165096-3119b797-1465-4b1a-86e2-191c379584e7.png)
![Ar(Column)](https://user-images.githubusercontent.com/60349584/168165105-5f2b8e4b-6a29-4efc-980d-7ffaf103a94d.png)
![Ar(Door, Window)](https://user-images.githubusercontent.com/60349584/168165112-99a06f6f-dc8c-446d-bf49-2e3879ffeee4.png)

- CNN model(using MNIST data)
![CNN](https://user-images.githubusercontent.com/60349584/168165133-d29cb9e7-5a19-4572-88fb-9ac25f72ddd0.png)

- Number Recognition
![Nr(App)](https://user-images.githubusercontent.com/60349584/168165181-f83c27f8-fbd6-4e19-8519-3cf412275b73.png)
![Ar(COS)](https://user-images.githubusercontent.com/60349584/168165198-0dd4e892-e085-442f-bc89-695764483b2c.png)

- ezdxf
![ezdxf](https://user-images.githubusercontent.com/60349584/168165257-4bed4dab-0f5b-4724-8f9f-a699b8362729.png)

- Object2Json
![o2j](https://user-images.githubusercontent.com/60349584/168165271-caa3b467-6758-4219-8800-aa1311aca073.png)

- TCP Socket
![tcp](https://user-images.githubusercontent.com/60349584/168165315-e85b3ae4-fb72-469d-ad50-df1783dd7e80.png)


## Manual for Users(API)
일단 공간만 만들어놨어요

## Manual for Developers(API)
여기도 마찬가지로 일단 공간만 

## Team
| |최찬영|정호진|김현민|박승민|
|---|---|---|---|---|
|E-mail|young221718@gachon.ac.kr|hojin19082@gachon.ac.kr|climatc1103@naver.com|smpark0213@naver.com|
|Github ID|Young2218|hojin19082|climatc1103|smpark0213|
