package com.wakabatimes.youtube_rank_api_batch;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.wakabatimes.youtube_rank_api_batch.entity.YoutubeProperty;
import com.wakabatimes.youtube_rank_api_batch.service.FileParseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.*;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class YoutubeRankApiBatchApplication implements CommandLineRunner {

    @Autowired
    private FileParseService fileParseService;


    private static final Logger logger = LoggerFactory.getLogger(YoutubeRankApiBatchApplication.class);
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(YoutubeRankApiBatchApplication.class);
        application.setWebApplicationType(WebApplicationType.NONE);
        ApplicationContext context = application.run(args);
        SpringApplication.exit(context);
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("- - - - - - - - - - - - - - - - - - - - - - - - - - - - ");
        logger.info("the start of youtube rank api batch");

        List<String> argsList = Arrays.asList(args);
        if (argsList.size() == 3) {
            YoutubeProperty youtubeProperty = fileParseService.getParam(argsList.get(0));
            String url = youtubeProperty.getBaseUrl() + "?" + youtubeProperty.getParams() + "&regionCode=" + youtubeProperty.getRegionCode() + "&videoCategoryId=" + youtubeProperty.getVideoCategoryId() + "&key=" + youtubeProperty.getKey();

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();

            //ファイル作成
            String dir = argsList.get(1);
            String fileName = argsList.get(2);

            File dirPath = new File(dir);
            if(!dirPath.exists()) {
                if(dirPath.mkdirs()){
                    logger.info("create dir");
                }
            }

            String filePath = dir + File.separator + fileName;
            File newFile = new File(filePath);
            if(newFile.createNewFile()) {
                logger.info("create file");
            }

            if(checkBeforeWriteFile(newFile)) {
                // 文字コードを指定する
                PrintWriter p_writer = null;
                try {
                    p_writer = new PrintWriter(new BufferedWriter
                            (new OutputStreamWriter(new FileOutputStream(newFile),"UTF-8")));
                } catch (UnsupportedEncodingException | FileNotFoundException e) {
                    e.printStackTrace();
                    logger.error(e.getMessage());
                }
                if (p_writer != null) {
                    p_writer.println(response.body().string());
                    p_writer.close();
                }
                logger.info("Create " + filePath);
            }else {
                logger.error("Error Can not write the file");
            }
        }else {
            logger.error("Incorrect parameters");
        }
    }

    private static boolean checkBeforeWriteFile(File file) {
        return file.exists() && !(!file.isFile() || !file.canWrite());
    }
}
