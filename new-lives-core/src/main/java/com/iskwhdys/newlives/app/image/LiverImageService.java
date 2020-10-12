package com.iskwhdys.newlives.app.image;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import java.awt.Color;

import com.iskwhdys.newlives.domain.liver.LiverEntity;
import com.iskwhdys.newlives.domain.liver.LiverRepository;
import com.iskwhdys.newlives.infra.config.AppConfig;
import com.iskwhdys.newlives.infra.image.ImageEditor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LiverImageService {

    @Autowired
    private LiverRepository liverRepository;
    @Autowired
    private AppConfig appConfig;

    private static RestTemplate restTemplate = new RestTemplate();

    public void downloadAll() {
        liverRepository.findAll().forEach(this::download);
    }

    public void download(LiverEntity l) {
        if (StringUtils.isEmpty(l.getIcon())) {
            return;
        }

        try {
            String dir = appConfig.getImage().getLiver().getIconPath();

            Path origin = Paths.get(dir, l.getId() + l.getIcon().substring(l.getIcon().lastIndexOf('.')));
            Files.createDirectories(origin.getParent());
            byte[] bytes = restTemplate.getForObject(l.getIcon(), byte[].class);
            Files.write(origin, bytes, StandardOpenOption.CREATE);

            Path resize = Paths.get(dir, "64x64w", l.getId() + ".jpg");
            Files.createDirectories(resize.getParent());
            Files.write(resize, ImageEditor.resize(ImageEditor.png2jpg(bytes, Color.WHITE), 64, 64, 1.0f),
                    StandardOpenOption.CREATE);

            resize = Paths.get(dir, "64x64b", l.getId() + ".jpg");
            Files.createDirectories(resize.getParent());
            Files.write(resize, ImageEditor.resize(ImageEditor.png2jpg(bytes, Color.BLACK), 64, 64, 1.0f),
                    StandardOpenOption.CREATE);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
