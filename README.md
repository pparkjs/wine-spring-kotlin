## ✨ 특이사항

- 구현한 모든 다수 조회 API 는 요구사항에는 없었지만 앱에서 많이 활용하는 `무한페이징` 방식으로 구현함
    - 관련 요청 필수 파라미터는 `page` , `size` 입니다.
    - 검색과 복수개의 정렬 및 필터링에 대해서는 QueryDsl을 이용해 동적으로 처리되도록 구현함
- Exception 처리는 제작한 `CustomException`을 통해 전역으로 처리되도록 구현함
- 각 Entity의 한글이름과 영어이름은 재사용 및 유효성 검사 편의성을 위해 `Name`이라는 VO로 묶어서 사용함
- N+1 문제를 해결하기 위해 OneToMany 관계는 전역으로 배치를 통해 `default_batch_fetch_size: 100`
  ManyToOne 관계는 `fetchJoin`으로 해결
- 구현한 모든 API는 `RestAssured`를 이용하여 해당 관련 API 별 `인수테스트`를 작성함

## ✨지역 관련 API

### ✅ *지역 생성 API*

### request

`POST/region`

- 최상위 지역 저장 body

```
{
   "korName" : "미국",
   "engName" : "U.S.A"
}

```

- 자식 지역 저장 body

```
{
   "korName" : "캘리포니아",
   "engName" : "California",
   "parentId": 33
}

```

### response

- 최상위 지역 저장

```
{
    "result": {
        "id": 33,
        "parentId": null,
        "depth": 1,
        "rootId": null,
        "korName": "미국",
        "engName": "U.S.A"
    }
}

```

- 자식 지역 저장

```
{
    "result": {
        "id": 34,
        "parentId": 33,
        "depth": 2,
        "rootId": 33,
        "korName": "캘리포니아",
        "engName": "California"
    }
}

```

### ✅ *지역 수정 API*

### request

`PATCH/region/{regionId}`

- 지역 수정 body

```
{
   "korName" : "호주",
   "engName" : "Australia"
}

```

### response

`NO_CONTENT`

### ✅ *지역 삭제 API*

### request

`DELETE/region/{regionId}`

### response

`NO_CONTENT`

### ✅ *지역 단일 조회 API*

### request

`GET/region/{regionId}`

### response

```
{
    "result": {
        "id": 32,
        "korName": "앤더슨 밸리",
        "engName": "Anderson Valley",
        "parentRegions": [
            {
                "korName": "멘도치노 카운티",
                "engName": "Mendocino County",
                "depth": 3
            },
            {
                "korName": "캘리포니아",
                "engName": "California",
                "depth": 2
            },
            {
                "korName": "미국",
                "engName": "U.S.A",
                "depth": 1
            }
        ],
        "grapes": [
            {
                "korName": "바르베라2",
                "engName": "Barbera2"
            },
            {
                "korName": "카베르네 소비뇽",
                "engName": "Cabernet Sauvignon"
            },
            {
                "korName": "바르베라",
                "engName": "Barbera"
            }
        ],
        "wineries": [
            {
                "korName": "1881 나파",
                "engName": "1881 Napa"
            },
            {
                "korName": "베드락 와인",
                "engName": "Bedrock Wine"
            },
            {
                "korName": "앤드류 윌 와이너리",
                "engName": "Andrew Will Winery"
            }
        ],
        "wines": [
            {
                "korName": "보글, 쁘띠 시라",
                "engName": "Bogle, Petite Sirah"
            }
        ]
    }
}

```

### ✅ *지역 다수 조회 API*

### request

`GET/region?page=0&size=5&keyword=a`

### response

```
{
    "result": [
        {
            "id": 19,
            "korName": "아콩카구아",
            "engName": "Aconcagua"
        },
        {
            "id": 32,
            "korName": "앤더슨 밸리",
            "engName": "Anderson Valley"
        },
        {
            "id": 2,
            "korName": "아르헨티나",
            "engName": "Argentina"
        },
        {
            "id": 3,
            "korName": "호주",
            "engName": "Australia"
        },
        {
            "id": 4,
            "korName": "오스트리아",
            "engName": "Austria"
        }
    ],
    "totalCount": 25,
    "hasNext": true
}

```

## ✨와이너리 관련 API

### ✅ *와이너리 생성 API*

### request

`POST/winery`

- body

```
{
   "korName" : "아소르스 와인 컴퍼니",
   "engName" : "Azores Wine Company",
   "regionId": 10
}

```

### response

```
{
    "result": {
        "id": 14,
        "korName": "아소르스 와인 컴퍼니",
        "engName": "Azores Wine Company",
        "regionKorName": "포르투갈",
        "regionEngName": "Portugal"
    }
}

```

### ✅ *와이너리 수정 API*

### request

`PATCH/winery/{wineryId}`

- body

```
{
   "korName" : "아소르스 와인",
   "engName" : "Azores Wine",
   "wineryId": 1
}

```

### response

`NO_CONTENT`

### ✅ *와이너리 삭제 API*

### request

`DELETE/winery/{wineryId}`

### response

`NO_CONTENT`

### ✅ *와이너리 단일 조회 API*

### request

`GET/winery/{wineryId}`

### response

```
{
    "result": {
        "id": 6,
        "korName": "알프레드 그라티엔",
        "engName": "Alfred Gratien",
        "regionKorName": "프랑스",
        "regionEngName": "France",
        "wines": [
            {
                "korName": "알프레드 그라티엔, 클래식 브뤼",
                "engName": "Alfred Gratien, Classic Brut"
            }
        ]
    }
}

```

### ✅ *와이너리 다수 조회 API*

### request

`GET/winery?page=0&size=5&filter=12&keyword=a`

### response

```
{
    "result": [
        {
            "id": 3,
            "korName": "1881 나파",
            "engName": "1881 Napa",
            "regionKorName": "미국",
            "regionEngName": "U.S.A"
        },
        {
            "id": 5,
            "korName": "앤드류 윌 와이너리",
            "engName": "Andrew Will Winery",
            "regionKorName": "미국",
            "regionEngName": "U.S.A"
        }
    ],
    "totalCount": 2,
    "hasNext": false
}

```

## ✨포도품종 관련 API

### ✅ *포도품종 생성 API*

### request

`POST/grape`

- body

```
{
   "korName" : "카베르네 프랑",
   "engName" : "Cabernet Franc",
   "acidity" : 3,
   "body" : 3,
   "sweetness" : 1,
   "tannin" : 3,
   "shares" : [
       {
           "share": 0.73,
           "regionId" : 6
       },
       {
           "share": 0.07,
           "regionId" : 8
       }
   ]
}

```

### response

```
{
    "result": {
        "id": 11
    }
}

```

### ✅ *포도품종 수정 API*

### request

`PATCH/grape/{grapeId}`

- body

```
{
   "korName" : "알바리뇨",
   "engName" : "Albarino(Alvarinho)",
   "acidity" : 4,
   "body" : 2,
   "sweetness" : 1,
   "tannin" : 3,
   "shares" : [
       {
           "share": 0.95,
           "regionId" : 6
       }

   ]
}

```

### response

`NO_CONTENT`

### ✅ *포도품종 삭제 API*

### request

`DELETE/grape/{grapeId}`

### response

`NO_CONTENT`

### ✅ *포도품종 단일 조회 API*

### request

`GET/grape/{grapeId}`

### response

```
{
    "result": {
        "id": 5,
        "grapeKorName": "카베르네 소비뇽",
        "grapeEngName": "Cabernet Sauvignon",
        "acidity": 4,
        "body": 2,
        "sweetness": 1,
        "tannin": 3,
        "regions": [
            {
                "korName": "칠레",
                "engName": "Chile"
            },
            {
                "korName": "프랑스",
                "engName": "France"
            },
            {
                "korName": "미국",
                "engName": "U.S.A"
            }
        ],
        "wines": [
            {
                "korName": "베드락, 올드 바인 진판델",
                "engName": "Bedrock, Old Vine Zinfandel"
            },
            {
                "korName": "보글, 쁘띠 시라",
                "engName": "Bogle, Petite Sirah"
            }
        ]
    }
}

```

### ✅ *포도품종 다수 조회 API*

### request

`GET/grape?page=0&size=4&order=grape&keyword=카&sort=desc`

### response

```
{
    "result": [
        {
            "id": 5,
            "korName": "카베르네 소비뇽",
            "engName": "Cabernet Sauvignon",
            "regions": [
                {
                    "korName": "칠레",
                    "engName": "Chile"
                },
                {
                    "korName": "프랑스",
                    "engName": "France"
                },
                {
                    "korName": "미국",
                    "engName": "U.S.A"
                }
            ]
        },
        {
            "id": 10,
            "korName": "카베르네 프랑",
            "engName": "Cabernet Franc",
            "regions": [
                {
                    "korName": "프랑스",
                    "engName": "France"
                },
                {
                    "korName": "이탈리아",
                    "engName": "Italy"
                }
            ]
        }
    ],
    "totalCount": 2,
    "hasNext": false
}

```

## ✨와인 관련 API

### ✅ *와인 생성 API*

### request

`POST/wine`

- body

```
{
   "korName" : "알루아도 샤도네이",
   "engName" : "Aluado Chardonnay",
   "type" : "SPARKLING",
   "alcohol" : 13.5,
   "acidity" : 3,
   "body" : 1,
   "sweetness" : 1,
   "tannin" : 1,
   "servingTemperature" : 9,
   "score" : 88.5,
   "price" : 46000,
   "style" : "French Champagne",
   "grade" : "AOC(AOP)",
   "importer" : "나라셀라",
   "wineryId" : 8,
   "regionId" : 18,
   "aromas" : ["리치", "코코넛"],
   "pairings" : ["양갈비","치즈"],
   "grapes" : [6,7,9]
}

```

### response

```
{
    "result": {
        "id": 10
    }
}

```

### ✅ *와인 수정 API*

### request

`PATCH/wine/{wineId}`

- body

```
{
   "korName" : "1881 나파, 카베르네 소비뇽",
   "engName" : "1881 Napa, Cabernet Sauvignon",
   "type" : "RED",
   "alcohol" : 14.5,
   "acidity" : 3,
   "body" : 4,
   "sweetness" : 1,
   "tannin" : 4,
   "servingTemperature" : 17,
   "score" : 80,
   "price" : 200000,
   "style" : "Californian Cabernet Sauvignon",
   "grade" : "AOC(AOP)",
   "importer" : "와인투유코리아",
   "wineryId" : 3,
   "regionId" : 5,
   "aromas" : ["리치", "코코넛", "바닐라"],
   "pairings" : ["양갈비","치즈"],
   "grapes" : [6,7]
}

```

### response

`NO_CONTENT`

### ✅ *와인 삭제 API*

### request

`DELETE/wine/{wineId}`

### response

`NO_CONTENT`

### ✅ *와인 단일 조회 API*

### request

`GET/wine/{wineId}`

### response

```
{
    "result": {
        "id": 7,
        "type": "RED",
        "korName": "보글, 쁘띠 시라",
        "engName": "Bogle, Petite Sirah",
        "alcohol": 14.1,
        "acidity": 3,
        "body": 4,
        "sweetness": 1,
        "tannin": 4,
        "score": 91.0,
        "price": 200003,
        "style": "Californian Cabernet Sauvignon",
        "grade": "AOC(AOP)",
        "importer": "와인투유코리아",
        "wineryKorName": "1881 나파",
        "wineryEngName": "1881 Napa",
        "wineryRegionKorName": "미국",
        "wineryRegionEngName": "U.S.A",
        "regionKorName": "앤더슨 밸리",
        "regionEngName": "Anderson Valley",
        "parentRegions": [
            {
                "korName": "멘도치노 카운티",
                "engName": "Mendocino County",
                "depth": 3
            },
            {
                "korName": "캘리포니아",
                "engName": "California",
                "depth": 2
            },
            {
                "korName": "미국",
                "engName": "U.S.A",
                "depth": 1
            }
        ],
        "grapes": [
            {
                "korName": "바르베라2",
                "engName": "Barbera2"
            },
            {
                "korName": "카베르네 소비뇽",
                "engName": "Cabernet Sauvignon"
            }
        ],
        "aromas": [
            "리치",
            "코코넛",
            "바닐라"
        ],
        "pairings": [
            "양갈비",
            "치즈"
        ]
    }
}

```

### ✅ *와인 다수 조회 API*

### request

`GET/wine?page=0&size=3&order=score&sort=asc&wineType=1&minAlcohol=14.1&maxAlcohol=16.5&wineStyle=Californian Cabernet Sauvignon&wineGrade=AOC(AOP)&region=21&minPrice=4000&maxPrice=200010&keyword=old`

### response

```
{
    "result": [
        {
            "id": 6,
            "type": "RED",
            "korName": "베드락, 올드 바인 진판델",
            "engName": "Bedrock, Old Vine Zinfandel",
            "topLevelKorRegionName": "미국",
            "topLevelEngRegionName": "U.S.A"
        }
    ],
    "totalCount": 1,
    "hasNext": false
}

```

## ✨수입사 관련 API

### ✅ *수입사 단일 조회 API*

### request

`GET/wine/importer/{importerName}`

### response

```
{
    "result": {
        "importerName": "와인투유코리아",
        "wineNames": [
            {
                "korName": "베드락, 올드 바인 진판델",
                "engName": "Bedrock, Old Vine Zinfandel"
            },
            {
                "korName": "보글, 쁘띠 시라",
                "engName": "Bogle, Petite Sirah"
            }
        ]
    }
}

```

### ✅ *수입사 다수 조회 API*

### request

`GET/wine/importer?page=0&size=3&keyword=와인`

### response

```
{
    "result": [
        "레드와인앤제이와이",
        "와인투유코리아"
    ],
    "totalCount": 2,
    "hasNext": false
}

```
