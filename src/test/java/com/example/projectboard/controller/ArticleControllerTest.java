package com.example.projectboard.controller;

import com.example.projectboard.DTO.ArticleWithCommentsDto;
import com.example.projectboard.DTO.UserAccountDto;
import com.example.projectboard.config.SecurityConfig;
import com.example.projectboard.domain.type.SearchType;
import com.example.projectboard.service.ArticleService;
import com.example.projectboard.service.PaginationService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 게시글")
@Import(SecurityConfig.class)
@WebMvcTest(ArticleController.class)
class ArticleControllerTest {
    private final MockMvc mvc;
    @MockBean
    private ArticleService articleService;

    @MockBean
    private PaginationService paginationService;

    public ArticleControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticlesView_thenReturnsArticlesView() throws Exception {
        //given
        given(articleService.searchArticles(eq(null),eq(null), ArgumentMatchers.any(Pageable.class))).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumber(anyInt(),anyInt())).willReturn(List.of(0,1,2,3,4));

        //when&then
        mvc.perform(get("/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attributeExists("paginationBarNumbers"));
        then(articleService).should().searchArticles(eq(null),eq(null), ArgumentMatchers.any(Pageable.class));
        then(paginationService).should().getPaginationBarNumber(anyInt(),anyInt());
    }


    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 검색어와 함께 호출")
    @Test
    public void givenSearchKeyword_whenSearchingArticlesView_thenReturnsArticlesView() throws Exception {
        //given
        SearchType searchType = SearchType.TITLE;
        String searchValue = "title";
        given(articleService.searchArticles(eq(searchType),eq(searchValue), ArgumentMatchers.any(Pageable.class))).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumber(anyInt(),anyInt())).willReturn(List.of(0,1,2,3,4));

        //when&then
        mvc.perform(get("/articles")
                        .queryParam("searchType", searchType.name())
                        .queryParam("searchValue",searchValue)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attributeExists("searchTypes"));
        then(articleService).should().searchArticles(eq(searchType),eq(searchValue), ArgumentMatchers.any(Pageable.class));
        then(paginationService).should().getPaginationBarNumber(anyInt(),anyInt());
    }


    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 페이징, 정렬 기능")
    @Test
    public void givenPagingAndSortingParams_whenSearchingArticlesPages_thenReturnsArticlesView() throws Exception {
        //given
        String sortName = "title";
        String direction = "desc";
        int pageNumber=0;
        int pageSize= 5;
        Pageable pageable= PageRequest.of(pageNumber,pageSize, Sort.by(Sort.Order.desc(sortName)));
        List<Integer> barNumbers = List.of(1,2,3,4,5);
        given(articleService.searchArticles(null,null,pageable)).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumber(pageable.getPageNumber(),Page.empty().getTotalPages())).willReturn(barNumbers);
        //when&then
        mvc.perform(get("/articles")
                        .queryParam("page",String.valueOf(pageNumber))
                        .queryParam("size",String.valueOf(pageSize))
                        .queryParam("sort",sortName+","+direction)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attribute("paginationBarNumbers",barNumbers));
        then(articleService).should().searchArticles(null,null,pageable);
        then(paginationService).should().getPaginationBarNumber(pageable.getPageNumber(),Page.empty().getTotalPages());
    }

    @DisplayName("[view][GET] 게시글 상세 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleView_thenReturnsArticleView() throws Exception {
        //given
        Long articleId = 1L;
        given(articleService.getArticle(articleId)).willReturn(createArticleWithCommentsDto());
        //when&then
        mvc.perform(get("/articles/"+articleId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/detail"))
                .andExpect(model().attributeExists("article"))// 이 이름에 데이터가 존재하는지만 판단.
                .andExpect(model().attributeExists("articleComments"));
        then(articleService).should().getArticle(articleId);
    }



    @Disabled("구현 중")
    @DisplayName("[view][GET] 게시글 검색 전용 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleSearchView_thenReturnsArticleSearchView() throws Exception {
        //given

        //when&then
        mvc.perform(get("/articles/search"))
                .andExpect(status().isOk())
                .andExpect(view().name("articles/index"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("articles/search"));
    }
    @Disabled("구현 중")
    @DisplayName("[view][GET] 게시글 해시태그 검색 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleHashtagView_thenReturnsArticleHashtagView() throws Exception {
        //given

        //when&then
        mvc.perform(get("/articles/search-hashtag"))
                .andExpect(status().isOk())
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles/search-hashtag"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }




    private ArticleWithCommentsDto createArticleWithCommentsDto() {
        return ArticleWithCommentsDto.of(
                1L,
                createUserAccountDto(),
                Set.of(),
                "title",
                "content",
                "#java",
                LocalDateTime.now(),
                "Minu",
                LocalDateTime.now(),
                "Minu"
        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                1L,
                "Minu",
                "pw",
                "Minu@naver.com",
                "Minutaurus",
                "Memo",
                LocalDateTime.now(),
                "Minu",
                LocalDateTime.now(),
                "Minu"
        );
    }
}