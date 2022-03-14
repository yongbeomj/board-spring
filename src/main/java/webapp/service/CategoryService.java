package webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import webapp.domain.entity.category.CategoryEntity;
import webapp.domain.entity.category.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    // 카테고리번호 DB 자동 저장
    public boolean categorysave(){
        for (int i = 1; i <= 3; i++){
            CategoryEntity categoryEntity = CategoryEntity.builder()
                    .cano(i)
                    .build();
            categoryRepository.save(categoryEntity);
        }
        return true;
    }

}
