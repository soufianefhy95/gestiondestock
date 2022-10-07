package com.soufiane.gestiondestock.controller.api;

import com.flickr4java.flickr.FlickrException;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static com.soufiane.gestiondestock.utils.Constants.APP_ROOT;

@Api(APP_ROOT + "/photos")
public interface PhotoApi {

    @PostMapping(APP_ROOT + "/photos/{id}/{title}/{context}")
    public Object savePhoto(@PathVariable("context") String context,
                            @PathVariable("id") Integer id,
                            @RequestPart("file") MultipartFile photo,
                            @PathVariable("title") String title) throws IOException, FlickrException;
}
