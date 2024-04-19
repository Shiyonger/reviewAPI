package com.pokemonreview.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokemonreview.api.dto.PokemonDto;
import com.pokemonreview.api.dto.ReviewDto;
import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.models.Review;
import com.pokemonreview.api.service.ReviewService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = ReviewController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ReviewControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ReviewService reviewService;
    @Autowired
    private ObjectMapper objectMapper;

    private Pokemon pokemon;
    private Review review;
    private ReviewDto reviewDto;
    private PokemonDto pokemonDto;

    @BeforeEach
    public void init() {
        pokemon = Pokemon.builder().name("pikachu").type("electric").build();
        pokemonDto = PokemonDto.builder().name("pikachu").type("electric").build();
        review = Review.builder().title("title").content("content").stars(5).build();
        reviewDto = ReviewDto.builder().title("title").content("content").stars(5).build();
    }

    @Test
    public void ReviewController_GetReviewsByPokemonId_ReturnsReviewDto() throws Exception {
        int pokemonId = 1;
        when(reviewService.getReviewsByPokemonId(pokemonId)).thenReturn(Collections.singletonList(reviewDto));

        ResultActions response = mockMvc.perform(get("/api/pokemon/1/reviews")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(Collections.singletonList(reviewDto).size())));
    }

    @Test
    public void ReviewController_UpdateReview_ReturnsReviewDto() throws Exception {
        int pokemonId = 1;
        int reviewId = 1;
        when(reviewService.updateReview(pokemonId, reviewId, reviewDto)).thenReturn(reviewDto);

        ResultActions response = mockMvc.perform(put("/api/pokemon/1/reviews/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is(reviewDto.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", CoreMatchers.is(reviewDto.getContent())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stars", CoreMatchers.is(reviewDto.getStars())));
    }

    @Test
    public void ReviewController_CreateReview_ReturnsCreated() throws Exception {
        int pokemonId = 1;
        when(reviewService.createReview(pokemonId, reviewDto)).thenReturn(reviewDto);

        ResultActions response = mockMvc.perform(post("/api/pokemon/1/review")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewDto)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is(reviewDto.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", CoreMatchers.is(reviewDto.getContent())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stars", CoreMatchers.is(reviewDto.getStars())));
    }

    @Test
    public void ReviewController_GetReviewById_ReturnsReviewDto() throws Exception {
        int pokemonId = 1;
        int reviewId = 1;
        when(reviewService.getReviewById(pokemonId, reviewId)).thenReturn(reviewDto);

        ResultActions response = mockMvc.perform(get("/api/pokemon/1/reviews/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is(reviewDto.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", CoreMatchers.is(reviewDto.getContent())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stars", CoreMatchers.is(reviewDto.getStars())));
    }

    @Test
    public void ReviewController_DeleteReview_ReturnsOk() throws Exception {
        int pokemonId = 1;
        int reviewId = 1;
        doNothing().when(reviewService).deleteReview(pokemonId, reviewId);

        ResultActions response = mockMvc.perform(delete("/api/pokemon/1/reviews/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }
}
