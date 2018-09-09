package com.example.demo.core.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "videos")
@Data
public class Video {

    private String query;
    private String nid;
    private List<Item> items;

    @Data
    public static class Item{
        private String title;
        private String thumbnail;

        public Item(){
        }

        public Item(String title, String thumbnail){
            this.title = title;
            this.thumbnail = thumbnail;
        }

    }

}
