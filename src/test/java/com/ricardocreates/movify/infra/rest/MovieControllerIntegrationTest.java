package com.ricardocreates.movify.infra.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swagger.client.codegen.rest.model.GenericErrorResponseDTO;
import com.swagger.client.codegen.rest.model.MovieDetailDTO;
import com.swagger.client.codegen.rest.model.MovieInfoDTO;
import com.swagger.client.codegen.rest.model.PageableMovieInfoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@AutoConfigureMockMvc
public class MovieControllerIntegrationTest {
    private static final String MOVIES_API = "/api/movies/";
    private static final String MOVIES_POPULAR_API = String.format("%s/%s", MOVIES_API, "popular");
    private static final String MOVIES_SEARCH_API = String.format("%s/%s", MOVIES_API, "search");

    private static final String API_KEY = "1234";
    private static final String INCORRECT_API_KEY = "12";

    private static final int PAGEABLE_TOTAL_ELEMENTS = 50;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {

    }

    @Test
    void should_getFlightInfo() throws Exception {
        //GIVEN
        Long movieId = 1L;
        String title = "Mystery Girl";
        String releaseDate = "2001-12-07";
        String fullPosterUrl = "https://dummyimage.com/718x234";
        String overview = "A deep drama exploring the human condition and emotions.";
        List<String> genres = List.of("Romance", "Musical", "Animation", "Drama");
        BigDecimal averageRate = new BigDecimal("6");
        int runtime = 136;
        String language = "German";
        //WHEN
        MvcResult response = this.mockMvc.perform(
                        get(String.format("%s/%s", MOVIES_API, movieId))
                                .param("api_key", API_KEY))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        var responseBody = objectMapper.readValue(response.getResponse().getContentAsString(), MovieDetailDTO.class);
        //THEN
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getTitle()).isEqualTo(title);
        assertThat(responseBody.getReleaseDate()).isEqualTo(releaseDate);
        assertThat(responseBody.getFullPosterUrl()).isEqualTo(fullPosterUrl);
        assertThat(responseBody.getOverview()).isEqualTo(overview);
        assertThat(responseBody.getGenres()).usingRecursiveComparison()
                .ignoringCollectionOrder().isEqualTo(genres);
        assertThat(responseBody.getAverageRate()).isEqualTo(averageRate);
        assertThat(responseBody.getRuntime()).isEqualTo(runtime);
        assertThat(responseBody.getLanguage()).isEqualTo(language);
    }

    @Test
    void should_not_getFlightInfo_notFound() throws Exception {
        //GIVEN
        Long movieId = -1L;
        //WHEN
        MvcResult response = this.mockMvc.perform(
                        get(String.format("%s/%s", MOVIES_API, movieId))
                                .param("api_key", API_KEY))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andReturn();
        var responseBody = objectMapper.readValue(response.getResponse().getContentAsString(), GenericErrorResponseDTO.class);
        //THEN
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getErrorCode()).isEqualTo("404");
    }

    @Test
    void should_not_getFlightInfo_badParameter() throws Exception {
        //GIVEN
        String movieId = "badparameter";
        //WHEN
        MvcResult response = this.mockMvc.perform(
                        get(String.format("%s/%s", MOVIES_API, movieId))
                                .param("api_key", API_KEY))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andReturn();
        var responseBody = objectMapper.readValue(response.getResponse().getContentAsString(), GenericErrorResponseDTO.class);
        //THEN
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getErrorCode()).isEqualTo("400");
    }

    @Test
    void should_not_getFlightInfo_unauthorized() throws Exception {
        //GIVEN
        Long movieId = 1L;
        //WHEN
        MvcResult response = this.mockMvc.perform(
                        get(String.format("%s/%s", MOVIES_API, movieId))
                                .param("api_key", INCORRECT_API_KEY))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnauthorized())
                .andReturn();
        var responseBody = objectMapper.readValue(response.getResponse().getContentAsString(), GenericErrorResponseDTO.class);
        //THEN
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getErrorCode()).isEqualTo("401");
    }

    @Test
    void should_not_getFlightInfo_unauthorized_noApiKey() throws Exception {
        //GIVEN
        Long movieId = 1L;
        //WHEN
        MvcResult response = this.mockMvc.perform(
                        get(String.format("%s/%s", MOVIES_API, movieId))
                                .param("api_key", ""))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnauthorized())
                .andReturn();
        var responseBody = objectMapper.readValue(response.getResponse().getContentAsString(), GenericErrorResponseDTO.class);
        //THEN
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getErrorCode()).isEqualTo("401");
    }

    @Test
    void should_searchMovies() throws Exception {
        //GIVEN
        String title = "the";
        int expectedSize = 19;
        //WHEN
        MvcResult response = this.mockMvc.perform(
                        get(MOVIES_SEARCH_API)
                                .param("api_key", API_KEY)
                                .param("title", title)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        List<MovieInfoDTO> responseBody = objectMapper
                .readValue(response.getResponse().getContentAsString(),
                        new TypeReference<>() {
                        });
        //THEN
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.size()).isEqualTo(expectedSize);
        assertThat(responseBody.getFirst()).isNotNull();
        assertThat(responseBody.getFirst().getTitle()).isNotEmpty();
        assertThat(responseBody.getLast()).isNotNull();
        assertThat(responseBody.getLast().getTitle()).isNotEmpty();
    }

    @Test
    void should_searchMovies_ordered_asc() throws Exception {
        //GIVEN
        String orderColumn = "language";
        String title = "the";
        int expectedSize = 19;
        int firstId = 1325;
        String firstTitle = "Comedy Whether";
        LocalDate firstReleaseDate = LocalDate.of(2016, 12, 24);
        BigDecimal firstAverageRate = new BigDecimal("8");

        //WHEN
        MvcResult response = this.mockMvc.perform(
                        get(MOVIES_SEARCH_API)
                                .param("api_key", API_KEY)
                                .param("title", title)
                                .param("orderBy", orderColumn)
                                .param("orderType", "asc")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        List<MovieInfoDTO> responseBody = objectMapper
                .readValue(response.getResponse().getContentAsString(),
                        new TypeReference<>() {
                        });
        //THEN
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.size()).isEqualTo(expectedSize);
        assertThat(responseBody.getFirst()).isNotNull();
        assertThat(responseBody.getFirst().getId()).isEqualTo(firstId);
        assertThat(responseBody.getFirst().getTitle()).isEqualTo(firstTitle);
        assertThat(responseBody.getFirst().getReleaseDate()).isEqualTo(firstReleaseDate);
        assertThat(responseBody.getFirst().getAverageRate()).isEqualTo(firstAverageRate);
        assertThat(responseBody.getLast()).isNotNull();
        assertThat(responseBody.getLast().getTitle()).isNotEmpty();
    }

    @Test
    void should_searchMovies_ordered_desc() throws Exception {
        //GIVEN
        String orderColumn = "releaseDate";
        String orderType = "desc";
        String title = "the";
        int expectedSize = 19;
        int firstId = 470;
        String firstTitle = "Action Together";
        LocalDate firstReleaseDate = LocalDate.of(2024, 07, 9);
        BigDecimal firstAverageRate = new BigDecimal("9");
        //WHEN
        MvcResult response = this.mockMvc.perform(
                        get(MOVIES_SEARCH_API)
                                .param("api_key", API_KEY)
                                .param("title", title)
                                .param("orderBy", orderColumn)
                                .param("orderType", orderType)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        List<MovieInfoDTO> responseBody = objectMapper
                .readValue(response.getResponse().getContentAsString(),
                        new TypeReference<>() {
                        });
        //THEN
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.size()).isEqualTo(expectedSize);
        assertThat(responseBody.getFirst()).isNotNull();
        assertThat(responseBody.getFirst().getTitle()).isEqualTo(firstTitle);
        assertThat(responseBody.getFirst().getReleaseDate()).isEqualTo(firstReleaseDate);
        assertThat(responseBody.getFirst().getAverageRate()).isEqualTo(firstAverageRate);
        assertThat(responseBody.getLast()).isNotNull();
        assertThat(responseBody.getLast().getTitle()).isNotEmpty();
    }

    @Test
    void should_not_searchMovies_noResults() throws Exception {
        //GIVEN
        String title = "NORESULTS";
        int expectedSize = 0;
        //WHEN
        MvcResult response = this.mockMvc.perform(
                        get(MOVIES_SEARCH_API)
                                .param("api_key", API_KEY)
                                .param("title", title)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        List<MovieInfoDTO> responseBody = objectMapper
                .readValue(response.getResponse().getContentAsString(),
                        new TypeReference<>() {
                        });
        //THEN
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.size()).isEqualTo(expectedSize);
    }

    @Test
    void should_not_searchMovies_badColumn() throws Exception {
        //GIVEN
        String title = "NORESULTS";
        String column = "BAD_COLUMN";
        //WHEN
        MvcResult response = this.mockMvc.perform(
                        get(MOVIES_SEARCH_API)
                                .param("api_key", API_KEY)
                                .param("title", title)
                                .param("orderBy", column)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andReturn();
        var responseBody = objectMapper
                .readValue(response.getResponse().getContentAsString(), GenericErrorResponseDTO.class);
        //THEN
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getErrorCode()).isEqualTo("400");
    }

    @Test
    void should_not_searchMovies_unauthorized() throws Exception {
        //GIVEN
        String title = "the";
        //WHEN
        MvcResult response = this.mockMvc.perform(
                        get(MOVIES_SEARCH_API)
                                .param("api_key", INCORRECT_API_KEY)
                                .param("title", title)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnauthorized())
                .andReturn();
        var responseBody = objectMapper
                .readValue(response.getResponse().getContentAsString(), GenericErrorResponseDTO.class);
        //THEN
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getErrorCode()).isEqualTo("401");
    }

    @Test
    void should_findPopular() throws Exception {
        //GIVEN
        int page = 0;
        int size = 10;
        //WHEN
        MvcResult response = this.mockMvc.perform(
                        get(MOVIES_POPULAR_API)
                                .param("api_key", API_KEY)
                                .param("page", String.valueOf(page))
                                .param("size", String.valueOf(size)
                                )
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        PageableMovieInfoDTO responseBody = objectMapper
                .readValue(response.getResponse().getContentAsString(),
                        PageableMovieInfoDTO.class);
        //THEN
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getContent()).isNotNull();
        assertThat(responseBody.getContent().size()).isEqualTo(size);
        assertThat(responseBody.getPage()).isNotNull();
        assertThat(responseBody.getPage().getTotalElements()).isNotNull();
        assertThat(responseBody.getPage().getTotalElements()).isEqualTo(PAGEABLE_TOTAL_ELEMENTS);
    }

    @Test
    void should_findPopular_pageTooBig() throws Exception {
        //GIVEN
        int page = 10;
        int size = 10;
        //WHEN
        MvcResult response = this.mockMvc.perform(
                        get(MOVIES_POPULAR_API)
                                .param("api_key", API_KEY)
                                .param("page", String.valueOf(page))
                                .param("size", String.valueOf(size)
                                )
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        PageableMovieInfoDTO responseBody = objectMapper
                .readValue(response.getResponse().getContentAsString(),
                        PageableMovieInfoDTO.class);
        //THEN
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getContent()).isNotNull();
        assertThat(responseBody.getContent().size()).isEqualTo(0);
        assertThat(responseBody.getPage()).isNotNull();
        assertThat(responseBody.getPage().getTotalElements()).isNotNull();
        assertThat(responseBody.getPage().getTotalElements()).isEqualTo(PAGEABLE_TOTAL_ELEMENTS);
    }

    @Test
    void should_not_findPopular_badParameter() throws Exception {
        //GIVEN
        int page = -1;
        int size = 10;
        //WHEN
        MvcResult response = this.mockMvc.perform(
                        get(MOVIES_POPULAR_API)
                                .param("api_key", API_KEY)
                                .param("page", String.valueOf(page))
                                .param("size", String.valueOf(size)
                                )
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andReturn();
        GenericErrorResponseDTO responseBody = objectMapper
                .readValue(response.getResponse().getContentAsString(),
                        GenericErrorResponseDTO.class);
        //THEN
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getErrors()).isNotNull();
        assertThat(responseBody.getErrors().get("getPopularMovies.page")).isNotNull();
    }

    @Test
    void should_not_findPopular_noPage() throws Exception {
        //GIVEN
        //WHEN
        MvcResult response = this.mockMvc.perform(
                        get(MOVIES_POPULAR_API)
                                .param("api_key", API_KEY)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andReturn();
        //THEN
        GenericErrorResponseDTO responseBody = objectMapper
                .readValue(response.getResponse().getContentAsString(),
                        GenericErrorResponseDTO.class);
        //THEN
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getErrors().get("page")).isNotNull();
    }

    @Test
    void should_not_findPopular_noSize() throws Exception {
        //GIVEN
        int page = 0;
        int size = 10;
        //WHEN
        MvcResult response = this.mockMvc.perform(
                        get(MOVIES_POPULAR_API)
                                .param("api_key", API_KEY)
                                .param("page", String.valueOf(page))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andReturn();
        GenericErrorResponseDTO responseBody = objectMapper
                .readValue(response.getResponse().getContentAsString(),
                        GenericErrorResponseDTO.class);
        //THEN
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getErrors().get("size")).isNotNull();
    }


}
