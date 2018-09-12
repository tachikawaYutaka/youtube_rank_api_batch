# youtube_rank_api_batch
```
mvn package
```

```
java -jar youtube_rank_api_batch-v1.0.jar "save_dir" "save_file_name.json"
```

## param1 

```
{
  "baseUrl": "https://www.googleapis.com/youtube/v3/videos",
  "params": "chart=mostPopular&part=snippet",
  "regionCode":"JP",
  "videoCategoryId":"categoryId",
  "key": "apiKey"
}
```

## param2 

save directory

## param3

save file
