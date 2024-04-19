package com.pokemonreview.api.repository;

import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.models.Review;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ReviewRepositoryTests {

    private ReviewRepository reviewRepository;
    private PokemonRepository pokemonRepository;

    @Autowired
    public ReviewRepositoryTests(ReviewRepository reviewRepository, PokemonRepository pokemonRepository) {
        this.reviewRepository = reviewRepository;
        this.pokemonRepository = pokemonRepository;
    }

    @Test
    public void ReviewRepository_SaveAll_ReturnsSavedReview() {
        Review review = Review.builder().title("title").content("content").stars(5).build();

        Review savedReview = reviewRepository.save(review);

        Assertions.assertThat(savedReview).isNotNull();
        Assertions.assertThat(savedReview.getId()).isGreaterThan(0);
    }

    @Test
    public void ReviewRepository_GetAll_ReturnsMoreThatOneReview() {
        Review review = Review.builder().title("title").content("content").stars(5).build();
        Review review2 = Review.builder().title("title").content("content").stars(5).build();

        reviewRepository.save(review);
        reviewRepository.save(review2);

        List<Review> reviewList = reviewRepository.findAll();

        Assertions.assertThat(reviewList).isNotNull();
        Assertions.assertThat(reviewList.size()).isEqualTo(2);
    }

    @Test
    public void ReviewRepository_FindById_ReturnsReviewNotNull() {
        Review review = Review.builder().title("title").content("content").stars(5).build();

        reviewRepository.save(review);
        Review returnedReview = reviewRepository.findById(review.getId()).get();

        Assertions.assertThat(returnedReview).isNotNull();
    }

    @Test
    public void ReviewRepository_FindByPokemonId_ReturnsAllReviews() {
        Pokemon pokemon = Pokemon.builder()
                .name("pikachu")
                .type("electric").build();
        Review review = Review.builder().title("title").content("content").stars(5).pokemon(pokemon).build();
        Review review2 = Review.builder().title("title").content("content").stars(5).pokemon(pokemon).build();

        pokemonRepository.save(pokemon);
        reviewRepository.save(review);
        reviewRepository.save(review2);
        List<Review> returnedReview = reviewRepository.findByPokemonId(pokemon.getId());

        Assertions.assertThat(returnedReview).isNotNull();
        Assertions.assertThat(returnedReview.size()).isEqualTo(2);
    }

    @Test
    public void ReviewRepository_UpdateReview_ReturnsReviewNotNull() {
        Review review = Review.builder().title("title").content("content").stars(5).build();

        reviewRepository.save(review);
        Review savedReview = reviewRepository.findById(review.getId()).get();
        savedReview.setTitle("Title");
        savedReview.setContent("Content");
        Review updatedReview = reviewRepository.save(savedReview);

        Assertions.assertThat(updatedReview.getTitle()).isNotNull();
        Assertions.assertThat(updatedReview.getContent()).isNotNull();
    }

    @Test
    public void ReviewRepository_DeleteReview_ReturnsReviewIsEmpty() {
        Review review = Review.builder().title("title").content("content").stars(5).build();

        reviewRepository.save(review);

        reviewRepository.deleteById(review.getId());
        Optional<Review> returnedReview = reviewRepository.findById(review.getId());

        Assertions.assertThat(returnedReview).isEmpty();
    }
}
