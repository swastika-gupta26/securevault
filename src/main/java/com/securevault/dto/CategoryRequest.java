package com.securevault.dto;

public class CategoryRequest {


        private String name;
        private String description;
        private Long parentCategoryId;

        public CategoryRequest() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Long getParentCategoryId() {
            return parentCategoryId;
        }

        public void setParentCategoryId(Long parentCategoryId) {
            this.parentCategoryId = parentCategoryId;
        }

}
